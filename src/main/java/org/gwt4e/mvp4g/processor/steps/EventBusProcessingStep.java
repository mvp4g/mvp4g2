/*
 * Copyright (C) 2016 Frank Hossfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gwt4e.mvp4g.processor.steps;

import com.google.auto.common.BasicAnnotationProcessor.ProcessingStep;
import com.google.common.collect.SetMultimap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.gwt4e.mvp4g.client.AbstractEventBus;
import org.gwt4e.mvp4g.client.annotations.EventBus;
import org.gwt4e.mvp4g.processor.Mvp4gProcessorContext;
import org.gwt4e.mvp4g.processor.context.EventBusContext;
import org.gwt4e.mvp4g.processor.context.EventContext;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

public class EventBusProcessingStep
  implements ProcessingStep {

  private final Messager messager;
  private final Filer    filer;
  private final Types    types;
  private final Elements elements;

  private Mvp4gProcessorContext mvp4gProcessorContext;

//------------------------------------------------------------------------------

  public EventBusProcessingStep(Messager messager,
                                Filer filer,
                                Types types,
                                Elements elements,
                                Mvp4gProcessorContext mvp4gProcessorContext) {
    this.messager = messager;
    this.filer = filer;
    this.types = types;
    this.elements = elements;

    this.mvp4gProcessorContext = mvp4gProcessorContext;
  }

//------------------------------------------------------------------------------


  public Set<? extends Class<? extends Annotation>> annotations() {
    return Collections.singleton(EventBus.class);
  }

  public void process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    for (Element element : elementsByAnnotation.get(EventBus.class)) {
      EventBusContext eventBusContext = EventBusContext.create(messager,
                                                               types,
                                                               elements,
                                                               element);

      if (eventBusContext == null) {
        return; // error message already emitted
      }
      if (this.mvp4gProcessorContext.getEventContextMap() == null ||
          this.mvp4gProcessorContext.getEventContextMap()
                                    .size() == 0 ||
          this.mvp4gProcessorContext.getEventContextMap()
                                    .get(eventBusContext.getClassName())
                                    .size() == 0) {
        messager.printMessage(Diagnostic.Kind.ERROR,
                              String.format("%s has no events defined",
                                            eventBusContext.getClassName()));
        return;
      }

      this.mvp4gProcessorContext.getEventBusContextMap()
                                .put((ClassName.get((TypeElement) element)
                                                  .toString()
                                        ),
                                        eventBusContext);
      try {
        generate(eventBusContext);
      } catch (IOException ioe) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("Error generating source file for type " + eventBusContext.getInterfaceType()
                                                                             .getQualifiedName());
        ioe.printStackTrace(pw);
        pw.close();
        messager.printMessage(Diagnostic.Kind.ERROR,
                              sw.toString());
      }
    }
  }

//------------------------------------------------------------------------------

  private void generate(EventBusContext context)
    throws IOException {

    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(context.getImplName())
                                        .addOriginatingElement(context.getInterfaceType())
                                        .addModifiers(Modifier.PUBLIC)
                                        .superclass(AbstractEventBus.class)
                                        .addSuperinterface(ClassName.get(context.getInterfaceType()));

    for (EventContext contextEvent : this.mvp4gProcessorContext.getEventContextMap()
                                                               .get(context.getClassName())
                                                               .values()) {
      typeSpec.addMethod(createEventMethod(contextEvent));
    }

    JavaFile.builder(context.getPackageName(),
                     typeSpec.build())
            .build()
            .writeTo(filer);

    System.out.println(JavaFile.builder(context.getPackageName(),
                                        typeSpec.build())
                               .build()
                               .toString());
  }

  private MethodSpec createEventMethod(EventContext contextEvent) {
    MethodSpec.Builder method = MethodSpec.methodBuilder(contextEvent.getMethodName())
                                          .addAnnotation(Override.class)
                                          .addModifiers(Modifier.PUBLIC,
                                                        Modifier.FINAL)
                                          .returns(void.class);
    // method head
    for (VariableElement parameter : contextEvent.getMethod()
                                                 .getParameters()) {
      method.addParameter(ClassName.get(parameter.asType()),
                          parameter.getSimpleName()
                                   .toString());
    }

    method.addCode("this.internalEventBus.fireEvent(new $T(",
                   ClassName.get(contextEvent.getPackageNameEvents(),
                                 contextEvent.getEventClassName()));
    for (int i = 0; i < contextEvent.getMethod()
                                    .getParameters()
                                    .size(); i++) {
      VariableElement parameter = contextEvent.getMethod()
                                              .getParameters()
                                              .get(i);
      method.addCode("$N",
                     parameter.getSimpleName()
                              .toString());
      if (i < contextEvent.getMethod()
                          .getParameters()
                          .size() - 1) {
        method.addCode(", ");
      }
    }
    method.addCode("));\n");

    return method.build();
  }
}
