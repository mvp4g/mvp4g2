/*
 * Copyright (C) 2015 Frank Hossfeld
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
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.squareup.javapoet.*;
import org.gwt4e.mvp4g.client.annotations.Event;
import org.gwt4e.mvp4g.processor.EventBusProcessorContext;
import org.gwt4e.mvp4g.processor.Utils;
import org.gwt4e.mvp4g.processor.context.EventContext;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class EventProcessingStep
  implements ProcessingStep {

  private final Messager messager;
  private final Filer    filer;
  private final Types    types;
  private final Elements elements;

  private EventBusProcessorContext processorContext;

//------------------------------------------------------------------------------

  public EventProcessingStep(Messager messager,
                             Filer filer,
                             Types types,
                             Elements elements,
                             EventBusProcessorContext processorContext) {
    this.messager = messager;
    this.filer = filer;
    this.types = types;
    this.elements = elements;

    this.processorContext = processorContext;
  }

//------------------------------------------------------------------------------

  public Set<? extends Class<? extends Annotation>> annotations() {
    return Collections.singleton(Event.class);
  }

  public void process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    for (Element element : elementsByAnnotation.get(Event.class)) {
      EventContext context = EventContext.create(messager,
                                                 types,
                                                 elements,
                                                 element);
      System.out.println("Processing Event: " + element.getSimpleName());
      if (context == null) {
        return; // error message already emitted
      }
      try {
        // generate Event
        generate(context);
        // save context
        this.processorContext.put(Utils.findEnclosingTypeElement(element).getQualifiedName().toString(),
                                  element.getSimpleName()
                                         .toString(),
                                  context);
// );

        // TODO
//          this.processorContext.getEventContextMap()
//                               .put(element.getSimpleName()
//                                           .toString(),
//                                    context);
      } catch (IOException ioe) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("Error generating source file for type " + context.getEventClassName());
        ioe.printStackTrace(pw);
        pw.close();
        messager.printMessage(Diagnostic.Kind.ERROR,
                              sw.toString());
      }
    }
  }

//------------------------------------------------------------------------------

  private void generate(EventContext context)
    throws IOException {
    generateEventHandler(context);
    generateEvent(context);
  }

  private void generateEventHandler(EventContext context)
    throws IOException {
    MethodSpec.Builder eventHandlerMethod = MethodSpec.methodBuilder(context.getEventHandlerMethodName())
                                                      .addModifiers(Modifier.PUBLIC,
                                                                    Modifier.ABSTRACT)
                                                      .returns(void.class)
                                                      .addParameter(ClassName.get(context.getPackageNameEvents(),
                                                                                  context.getEventClassName()),
                                                                    "event");

    TypeSpec.Builder typeSpec = TypeSpec.interfaceBuilder(context.getEventHandlerClassName())
                                        .addSuperinterface(EventHandler.class)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addMethod(eventHandlerMethod.build());


    JavaFile.builder(context.getPackageNameEvents(),
                     typeSpec.build())
            .build()
            .writeTo(filer);
  }

  private void generateEvent(EventContext context)
    throws IOException {

    FieldSpec type = FieldSpec.builder(GwtEvent.Type.class,
                                       "TYPE")
                              .addModifiers(Modifier.PUBLIC,
                                            Modifier.STATIC)
                              .initializer("new $T<$T>()",
                                           GwtEvent.Type.class,
                                           ClassName.get(context.getPackageNameEvents(),
                                                         context.getEventHandlerClassName()))
                              .build();


    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(context.getEventClassName())
                                        .superclass(ParameterizedTypeName.get(ClassName.get(GwtEvent.class),
                                                                              ClassName.get(context.getPackageNameEvents(),
                                                                                            context.getEventHandlerClassName())))
                                        .addModifiers(Modifier.PUBLIC)
                                        .addField(type);

    List<FieldSpec> fields = new ArrayList<>();
    List<MethodSpec> getterMethods = new ArrayList<>();
    List<MethodSpec> setterMethods = new ArrayList<>();
    for (VariableElement parameter : context.getMethod()
                                            .getParameters()) {
      FieldSpec field = FieldSpec.builder(ClassName.get(parameter.asType()),
                                          parameter.getSimpleName()
                                                   .toString())
                                 .addModifiers(Modifier.PRIVATE)
                                 .build();

      fields.add(field);
      getterMethods.add(Utils.createGetter(parameter, field));
      setterMethods.add(Utils.createSetter(parameter, field));

      typeSpec.addField(field);
    }


    MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                               .addModifiers(Modifier.PUBLIC);
    for (VariableElement parameter : context.getMethod()
                                            .getParameters()) {
      constructor.addParameter(ClassName.get(parameter.asType()),
                               parameter.getSimpleName()
                                        .toString());
    }
    for (VariableElement parameter : context.getMethod()
                                            .getParameters()) {
      constructor.addStatement("this.$N = $N",
                               parameter.getSimpleName()
                                        .toString(),
                               parameter.getSimpleName()
                                        .toString());
    }
    typeSpec.addMethod(constructor.build());

    for (MethodSpec method : getterMethods) {
      typeSpec.addMethod(method);
    }
    for (MethodSpec method : setterMethods) {
      typeSpec.addMethod(method);
    }

    MethodSpec.Builder methodDispatch = MethodSpec.methodBuilder("dispatch")
                                                  .addAnnotation(Override.class)
                                                  .addModifiers(Modifier.PROTECTED)
                                                  .returns(void.class)
                                                  .addParameter(ClassName.get(context.getPackageNameEvents(),
                                                                              context.getEventHandlerClassName()),
                                                                "handler")
                                                  .addStatement("handler.$L(this)",
                                                                context.getEventHandlerMethodName());

    MethodSpec.Builder methodGetAssociatedType = MethodSpec.methodBuilder("getAssociatedType")
                                                           .addAnnotation(Override.class)
                                                           .addModifiers(Modifier.PUBLIC)
                                                           .returns(ParameterizedTypeName.get(ClassName.get(GwtEvent.Type.class),
                                                                                              ClassName.get(context.getPackageNameEvents(),
                                                                                                            context.getEventHandlerClassName())))
                                                           .addStatement("return $N",
                                                                         type);
    typeSpec.addMethod(methodGetAssociatedType.build())
            .addMethod(methodDispatch.build());


    JavaFile.builder(context.getPackageNameEvents(),
                     typeSpec.build())
            .build()
            .writeTo(filer);
  }
}
