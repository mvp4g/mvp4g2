/*
 * Copyright (c) 2018 - Frank Hossfeld
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 *
 */
package com.github.mvp4g.mvp4g2.processor.generator;

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Event;
import com.github.mvp4g.mvp4g2.core.internal.eventbus.EventMetaData;
import com.github.mvp4g.mvp4g2.processor.ProcessorException;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.EventBusMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.EventMetaModel;

/**
 * <p>The execution context manages all commands.<br>
 * Use run()-method to start execution.</p>
 */
public class EventMetaDataGenerator {

  private ProcessorUtils processorUtils;

  private ProcessingEnvironment processingEnvironment;
  private EventBusMetaModel     eventBusMetaModel;

  @SuppressWarnings("unused")
  private EventMetaDataGenerator() {
  }

  private EventMetaDataGenerator(Builder builder) {
    this.processingEnvironment = builder.processingEnvironment;
    this.eventBusMetaModel = builder.eventBusMetaModel;

    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate()
    throws ProcessorException {
    for (EventMetaModel eventMetaModel : this.eventBusMetaModel.getEventMetaModels()) {
      TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.createEventMetaDataClassName(this.eventBusMetaModel.getEventBus()
                                                                                                                .getClassName(),
                                                                                          eventMetaModel.getEventName()))
                                          .superclass(ParameterizedTypeName.get(ClassName.get(EventMetaData.class),
                                                                                this.eventBusMetaModel.getEventBus()
                                                                                                      .getTypeName()))
                                          .addModifiers(Modifier.PUBLIC,
                                                        Modifier.FINAL);
      // constructor ...
      MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .addCode("super($S, $S, ",
                                                          eventMetaModel.getEventInternalName(),
                                                          eventMetaModel.getEventName());
      if (Event.DEFAULT_HISTORY_NAME.equals(eventMetaModel.getHistoryEventName())) {
        constructor.addCode("null, ");
      } else {
        constructor.addCode("$S, ",
                            eventMetaModel.getHistoryEventName());
      }
      if (Event.NoHistoryConverter.class.getCanonicalName()
                                        .equals(eventMetaModel.getHistoryConverter()
                                                              .getClassName())) {
        constructor.addCode("null, null, ");
      } else {
        constructor.addCode("new $L(), new $T(), ",
                            eventMetaModel.getHistoryConverter()
                                          .getPackage() + "." + this.processorUtils.createHistoryMetaDataClassName(eventMetaModel.getHistoryConverter()
                                                                                                                                 .getClassName()),
                            eventMetaModel.getHistoryConverter()
                                          .getTypeName());
      }
      constructor.addCode("$L, $L);\n",
                          eventMetaModel.getPassive(),
                          eventMetaModel.getNavigationEvent());

      eventMetaModel.getBindings()
                    .forEach(s -> constructor.addStatement("super.addBindHandler($S)",
                                                           s.getClassName()));

      eventMetaModel.getHandlers()
                    .forEach(s -> constructor.addStatement("super.addHandler($S)",
                                                           s.getClassName()));

      eventMetaModel.getActivateHandlers()
                    .forEach(s -> constructor.addStatement("super.addActivateHandler($S)",
                                                           s.getClassName()));

      eventMetaModel.getDeactivateHandlers()
                    .forEach(s -> constructor.addStatement("super.addDeactivateHandler($S)",
                                                           s.getClassName()));
      typeSpec.addMethod(constructor.build());

      JavaFile javaFile = JavaFile.builder(eventBusMetaModel.getEventBus()
                                                            .getPackage(),
                                           typeSpec.build())
                                  .build();
      try {
        javaFile.writeTo(this.processingEnvironment.getFiler());
      } catch (IOException e) {
        throw new ProcessorException("Unable to write generated file: >>" + eventBusMetaModel.getEventBus()
                                                                                             .getPackage() + "." + this.createEventMetaDataClassName(this.eventBusMetaModel.getEventBus()
                                                                                                                                                                           .getClassName(),
                                                                                                                                                     eventMetaModel.getEventName()) + "<< -> exception: " + e.getMessage());
      }
    }
  }

  private String createEventMetaDataClassName(String eventBusClassName,
                                              String eventName) {
    return this.processorUtils.setFirstCharacterToUpperCase(this.createEventMetaDataVariableName(eventBusClassName,
                                                                                                 eventName));
  }

  private String createEventMetaDataVariableName(String eventBusClassName,
                                                 String eventName) {
    return this.processorUtils.createFullClassName(eventBusClassName + "_" + eventName);
  }

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;
    EventBusMetaModel     eventBusMetaModel;

    /**
     * Set the processing envirement
     *
     * @param processingEnvirement the processing envirement
     * @return the Builder
     */
    public Builder processingEnvironment(ProcessingEnvironment processingEnvirement) {
      this.processingEnvironment = processingEnvirement;
      return this;
    }

    public Builder eventBusMetaModel(EventBusMetaModel eventBusMetaModel) {
      this.eventBusMetaModel = eventBusMetaModel;
      return this;
    }

    public EventMetaDataGenerator build() {
      return new EventMetaDataGenerator(this);
    }
  }
}
