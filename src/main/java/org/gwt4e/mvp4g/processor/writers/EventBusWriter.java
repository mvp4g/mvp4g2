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
import org.gwt4e.event.shared.SimpleMvp4gInternalEventBus;
import org.gwt4e.mvp4g.client.annotations.Debug;
import org.gwt4e.mvp4g.client.event.AbstractMvp4gEventBus;
import org.gwt4e.mvp4g.processor.ProcessorContext;
import org.gwt4e.mvp4g.processor.context.EventBusContext;
import org.gwt4e.mvp4g.processor.context.EventContext;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;

/**
 * Created by hoss on 05.01.16.
 */
public class EventBusWriter
  extends AbstractWriter
  implements Writer {

  private EventBusContext eventBusContext;

  private EventBusWriter(Builder builder) {
    super(builder.types,
          builder.messager,
          builder.filer,
          builder.elements,
          builder.processorContext);

    this.eventBusContext = builder.eventBusContext;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public void write()
    throws IOException {
    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(eventBusContext.getImplName())
                                        .addOriginatingElement(eventBusContext.getInterfaceType())
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL)
                                        .superclass(AbstractMvp4gEventBus.class)
                                        .addSuperinterface(ClassName.get(eventBusContext.getInterfaceType()));

    if (eventBusContext.hasDebug()) {
      TypeElement loggerElement = (TypeElement) ((DeclaredType) eventBusContext.getLogger()).asElement();
      FieldSpec logger = FieldSpec.builder(TypeName.get(loggerElement.asType()),
                                           "logger")
                                  .addModifiers(Modifier.PRIVATE)
                                  .build();
      typeSpec.addField(logger);
    }

    ParameterSpec moduleNameParameter = ParameterSpec.builder(String.class,
                                                              "moduleName")
                                                     .build();
    ParameterSpec eventBusParameter = ParameterSpec.builder(SimpleMvp4gInternalEventBus.class,
                                                            "eventBus")
                                                   .build();
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                               .addModifiers(Modifier.PUBLIC)
                                               .addParameter(moduleNameParameter)
                                               .addParameter(eventBusParameter)
                                               .addStatement("super($N, $N)",
                                                             moduleNameParameter,
                                                             eventBusParameter);
    typeSpec.addMethod(constructor.build());

    if (this.processorContext.getEventContextMap()
                             .get(eventBusContext.getClassName()) != null) {
      for (EventContext contextEvent : this.processorContext.getEventContextMap()
                                                            .get(eventBusContext.getClassName())
                                                            .values()) {
        typeSpec.addMethod(writeEventMethod(contextEvent));
      }
    }

    JavaFile.builder(eventBusContext.getPackageName() + ".generated",
                     typeSpec.build())
            .build()
            .writeTo(filer);

    System.out.println(JavaFile.builder(eventBusContext.getPackageName() + ".generated",
                                        typeSpec.build())
                               .build()
                               .toString());
  }

  private MethodSpec writeEventMethod(EventContext contextEvent) {
    MethodSpec.Builder method = MethodSpec.methodBuilder(contextEvent.getMethodName())
                                          .addAnnotation(Override.class)
                                          .addModifiers(Modifier.PUBLIC)
                                          .returns(void.class);
    for (VariableElement parameter : contextEvent.getMethod()
                                                 .getParameters()) {
      method.addParameter(ClassName.get(parameter.asType()),
                          parameter.getSimpleName()
                                   .toString());
    }

    writeEventLog(contextEvent,
                  method);

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

  private void writeEventLog(EventContext contextEvent,
                             MethodSpec.Builder method) {
    if (eventBusContext.hasDebug()) {
      StringBuilder sb = new StringBuilder();
      sb.append("logger.log(\"MVP4G-Logger: firing event: ")
        .append(contextEvent.getMethodName());
      if (eventBusContext.getLogLevel()
                         .equals(Debug.LogLevel.SIMPLE)) {
        sb.append("\"");
      } else {
        if (contextEvent.getMethod()
                        .getParameters()
                        .size() > 0) {
          sb.append(" || param(s): \" + attr0");
          for (int i = 1; i < contextEvent.getMethod()
                                          .getParameters()
                                          .size(); i++) {
            sb.append(" + \", \" + attr");
            sb.append(i);
          }
        } else {
          sb.append("\"");
        }
      }
      method.addStatement(sb.toString());
    }
  }

  public final static class Builder {
    static Types            types;
    static Messager         messager;
    static Filer            filer;
    static Elements         elements;
    static ProcessorContext processorContext;
    static EventBusContext  eventBusContext;

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

    public Builder eventBusContext(EventBusContext value) {
      eventBusContext = value;
      return this;
    }

    public EventBusWriter build() {
      return new EventBusWriter(this);
    }
  }
}
