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

package org.gwt4e.mvp4g.processor.writers;

import com.squareup.javapoet.*;
import org.gwt4e.event.shared.Mvp4gInternalEvent;
import org.gwt4e.event.shared.Mvp4gInternalEventHandler;
import org.gwt4e.mvp4g.processor.ProcessorContext;
import org.gwt4e.mvp4g.processor.Utils;
import org.gwt4e.mvp4g.processor.context.EventContext;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventWriter
  extends AbstractWriter
  implements Writer {

  private EventContext eventContext;

  private EventWriter(Builder builder) {
    super(Builder.types,
          Builder.messager,
          Builder.filer,
          Builder.elements,
          Builder.processorContext);

    this.eventContext = Builder.eventContext;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public void write()
    throws IOException {
    writeEventHandler();
    writeEvent();
  }

  private void writeEventHandler()
    throws IOException {
    MethodSpec.Builder eventHandlerMethod = MethodSpec.methodBuilder(eventContext.getEventHandlerMethodName())
                                                      .addModifiers(Modifier.PUBLIC,
                                                                    Modifier.ABSTRACT)
                                                      .returns(void.class)
                                                      .addParameter(ClassName.get(eventContext.getPackageNameEvents(),
                                                                                  eventContext.getEventClassName()),
                                                                    "event");

    TypeSpec.Builder typeSpec = TypeSpec.interfaceBuilder(eventContext.getEventHandlerClassName())
                                        .addSuperinterface(Mvp4gInternalEventHandler.class)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addMethod(eventHandlerMethod.build());


    JavaFile.builder(eventContext.getPackageNameEvents(),
                     typeSpec.build())
            .build()
            .writeTo(filer);

    System.out.println(JavaFile.builder(eventContext.getPackageNameEvents(),
                                        typeSpec.build())
                               .build()
                               .toString());
  }

  private void writeEvent()
    throws IOException {

    FieldSpec type = FieldSpec.builder(Mvp4gInternalEvent.Type.class,
                                       "TYPE")
                              .addModifiers(Modifier.PUBLIC,
                                            Modifier.STATIC)
                              .initializer("new $T<$T>()",
                                           Mvp4gInternalEvent.Type.class,
                                           ClassName.get(eventContext.getPackageNameEvents(),
                                                         eventContext.getEventHandlerClassName()))
                              .build();


    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(eventContext.getEventClassName())
                                        .superclass(ParameterizedTypeName.get(ClassName.get(Mvp4gInternalEvent.class),
                                                                              ClassName.get(eventContext.getPackageNameEvents(),
                                                                                            eventContext.getEventHandlerClassName())))
                                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                        .addField(type);

    List<FieldSpec> fields = new ArrayList<>();
    List<MethodSpec> getterMethods = new ArrayList<>();
    List<MethodSpec> setterMethods = new ArrayList<>();
    for (VariableElement parameter : eventContext.getMethod()
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
    for (VariableElement parameter : eventContext.getMethod()
                                                 .getParameters()) {
      constructor.addParameter(ClassName.get(parameter.asType()),
                               parameter.getSimpleName()
                                        .toString());
    }
    for (VariableElement parameter : eventContext.getMethod()
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
                                                  .addParameter(ClassName.get(eventContext.getPackageNameEvents(),
                                                                              eventContext.getEventHandlerClassName()),
                                                                "handler")
                                                  .addStatement("handler.$L(this)",
                                                                eventContext.getEventHandlerMethodName());

    MethodSpec.Builder methodGetAssociatedType = MethodSpec.methodBuilder("getAssociatedType")
                                                           .addAnnotation(Override.class)
                                                           .addModifiers(Modifier.PUBLIC)
                                                           .returns(ParameterizedTypeName.get(ClassName.get(Mvp4gInternalEvent.Type.class),
                                                                                              ClassName.get(eventContext.getPackageNameEvents(),
                                                                                                            eventContext.getEventHandlerClassName())))
                                                           .addStatement("return $N",
                                                                         type);
    typeSpec.addMethod(methodGetAssociatedType.build())
            .addMethod(methodDispatch.build());


    JavaFile.builder(eventContext.getPackageNameEvents(),
                     typeSpec.build())
            .build()
            .writeTo(filer);

    System.out.println(JavaFile.builder(eventContext.getPackageNameEvents(),
                                        typeSpec.build())
                               .build()
                               .toString());
  }

  public final static class Builder {
    static Types            types;
    static Messager         messager;
    static Filer            filer;
    static Elements         elements;
    static ProcessorContext processorContext;
    static EventContext     eventContext;

    public Builder types(Types value) {
      types = value;
      return this;
    }

    public Builder messenger(Messager value) {
      messager = value;
      return this;
    }

    public Builder filer(Filer value) {
      filer = value;
      return this;
    }

    public Builder elements(Elements value) {
      elements = value;
      return this;
    }

    public Builder context(ProcessorContext value) {
      processorContext = value;
      return this;
    }

    public Builder eventContext(EventContext value) {
      eventContext = value;
      return this;
    }

    public EventWriter build() {
      return new EventWriter(this);
    }
  }
}
