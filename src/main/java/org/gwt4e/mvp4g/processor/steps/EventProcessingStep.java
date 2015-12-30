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
import com.squareup.javapoet.*;
import org.gwt4e.event.shared.Mvp4gEvent;
import org.gwt4e.event.shared.Mvp4gEventHandler;
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
import java.util.*;

public class EventProcessingStep
  implements ProcessingStep {

  private final Messager messager;
  private final Filer    filer;
  private final Types    types;
  private final Elements elements;

  private EventBusProcessorContext eventBusProcessorContext;

//------------------------------------------------------------------------------

  public EventProcessingStep(Messager messager,
                             Filer filer,
                             Types types,
                             Elements elements,
                             EventBusProcessorContext eventBusProcessorContext) {
    this.messager = messager;
    this.filer = filer;
    this.types = types;
    this.elements = elements;

    this.eventBusProcessorContext = eventBusProcessorContext;
  }

//------------------------------------------------------------------------------

  public Set<? extends Class<? extends Annotation>> annotations() {
    return Collections.singleton(Event.class);
  }

  public void process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    for (Element element : elementsByAnnotation.get(Event.class)) {
      String eventBusName = Utils.findEnclosingTypeElement(element)
                                 .getQualifiedName()
                                 .toString();

      // check if an event with the same name already exits!
      Set<String> eventBusNames = eventBusProcessorContext.getEventContextMap()
                                                          .keySet();
      for (String ebName : eventBusNames) {
        Set<String> eventNames = ((Map<String, EventContext>) eventBusProcessorContext.getEventContextMap()
                                                                                      .get(ebName)).keySet();
        for (String eName : eventNames) {
          if (element.getSimpleName().toString().equals(eName)) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                                  String.format("Event-name: %s is already used. Please choose another name. (It is not possible to work with same event-name and different signatures.",
                                                element.getSimpleName().toString()));
            return;
          }
        }
      }

      // create context
      EventContext eventContext = EventContext.create(messager,
                                                      types,
                                                      elements,
                                                      element);
      System.out.println("Processing Mvp4gEvent: " + element.getSimpleName());
      if (eventContext == null) {
        return; // error message already emitted
      }
      try {
        // generate Mvp4gEvent
        generate(eventContext);
        // save eventContext
        this.eventBusProcessorContext.put(eventBusName,
                                          element.getSimpleName()
                                                 .toString(),
                                          eventContext);

      } catch (IOException ioe) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("Error generating source file for type " + eventContext.getEventClassName());
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
                                        .addSuperinterface(Mvp4gEventHandler.class)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addMethod(eventHandlerMethod.build());


    JavaFile.builder(context.getPackageNameEvents(),
                     typeSpec.build())
            .build()
            .writeTo(filer);

    System.out.println(JavaFile.builder(context.getPackageNameEvents(),
                                        typeSpec.build())
                               .build()
                               .toString());
  }

  private void generateEvent(EventContext context)
    throws IOException {

    FieldSpec type = FieldSpec.builder(Mvp4gEvent.Type.class,
                                       "TYPE")
                              .addModifiers(Modifier.PUBLIC,
                                            Modifier.STATIC)
                              .initializer("new $T<$T>()",
                                           Mvp4gEvent.Type.class,
                                           ClassName.get(context.getPackageNameEvents(),
                                                         context.getEventHandlerClassName()))
                              .build();


    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(context.getEventClassName())
                                        .superclass(ParameterizedTypeName.get(ClassName.get(Mvp4gEvent.class),
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
      getterMethods.add(Utils.createGetter(parameter,
                                           field));
      setterMethods.add(Utils.createSetter(parameter,
                                           field));

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
                                                           .returns(ParameterizedTypeName.get(ClassName.get(Mvp4gEvent.Type.class),
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

    System.out.println(JavaFile.builder(context.getPackageNameEvents(),
                                        typeSpec.build())
                               .build()
                               .toString());
  }
}
