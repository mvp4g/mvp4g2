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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.EventBusMetaModel;

/**
 * <p>The execution context manages all commands.<br>
 * Use run()-method to start execution.</p>
 */
public class EventLoadMetaDataGenerator {

  private ProcessorUtils processorUtils;

  private ProcessingEnvironment processingEnvironment;
  private EventBusMetaModel     eventBusMetaModel;
  private TypeSpec.Builder      typeSpec;

  @SuppressWarnings("unused")
  private EventLoadMetaDataGenerator() {
  }

  private EventLoadMetaDataGenerator(Builder builder) {
    this.processingEnvironment = builder.processingEnvironment;
    this.eventBusMetaModel = builder.eventBusMetaModel;
    this.typeSpec = builder.typeSpec;

    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate() {
    MethodSpec.Builder loadEventMethod = MethodSpec.methodBuilder("loadEventMetaData")
                                                   .addAnnotation(Override.class)
                                                   .addModifiers(Modifier.PROTECTED);
    this.eventBusMetaModel.getEventMetaModels()
                          .forEach(eventMetaModel -> {
                            String metaDataClassName = this.createEventMetaDataClassName(this.eventBusMetaModel.getEventBus()
                                                                                                               .getClassName(),
                                                                                         eventMetaModel.getEventName());
                            loadEventMethod.addComment("");
                            loadEventMethod.addComment("----------------------------------------------------------------------");
                            loadEventMethod.addComment("");
                            loadEventMethod.addComment("handle $N",
                                                       metaDataClassName);
                            loadEventMethod.addComment("");
                            loadEventMethod.addStatement("super.putEventMetaData($S, new $N())",
                                                         eventMetaModel.getEventInternalName(),
                                                         metaDataClassName);
                          });
    typeSpec.addMethod(loadEventMethod.build());
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

  private void generateLoadEventMetaDataMethod(TypeSpec.Builder typeSpec) {
  }

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;
    EventBusMetaModel     eventBusMetaModel;
    TypeSpec.Builder      typeSpec;

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

    /**
     * Set the typeSpec of the currently generated eventBus
     *
     * @param typeSpec ttype spec of the crruent eventbus
     * @return the Builder
     */
    public Builder typeSpec(TypeSpec.Builder typeSpec) {
      this.typeSpec = typeSpec;
      return this;
    }

    public EventLoadMetaDataGenerator build() {
      return new EventLoadMetaDataGenerator(this);
    }
  }
}
