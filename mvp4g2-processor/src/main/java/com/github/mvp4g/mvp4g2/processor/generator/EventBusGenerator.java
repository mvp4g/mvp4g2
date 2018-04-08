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
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.github.mvp4g.mvp4g2.core.internal.eventbus.AbstractEventBus;
import com.github.mvp4g.mvp4g2.processor.ProcessorException;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.EventBusMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.HandlerMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.HistoryMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.PresenterMetaModel;

public class EventBusGenerator {

  private final static String IMPL_NAME = "Impl";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private TypeElement           eventBusTypeElement;

  @SuppressWarnings("unused")
  private EventBusGenerator(Builder builder) {
    super();
    this.processingEnvironment = builder.processingEnvironment;
    this.eventBusTypeElement = builder.eventBusTypeElement;
    setUp();
  }

  private void setUp() {
    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate(EventBusMetaModel eventBusMetaMetaModel,
                       HandlerMetaModel handlerMetaModel,
                       PresenterMetaModel presenterMetaModel,
                       HistoryMetaModel historyMetaModel)
    throws ProcessorException {
    // check if element is existing (to avoid generating code for deleted items)
    if (!this.processorUtils.doesExist(eventBusMetaMetaModel.getEventBus())) {
      return;
    }

    ClassName abstractEventBusClassName = ClassName.get(AbstractEventBus.class);
    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(eventBusMetaMetaModel.getEventBus()
                                                                           .getSimpleName() + EventBusGenerator.IMPL_NAME)
                                        .superclass(ParameterizedTypeName.get(abstractEventBusClassName,
                                                                              eventBusMetaMetaModel.getEventBus()
                                                                                                   .getTypeName()))
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL)
                                        .addSuperinterface(eventBusMetaMetaModel.getEventBus()
                                                                                .getTypeName());

    MethodSpec constructor = MethodSpec.constructorBuilder()
                                       .addModifiers(Modifier.PUBLIC)
                                       .addStatement("super($S)",
                                                     eventBusMetaMetaModel.getShell()
                                                                          .getClassName())
                                       .build();
    typeSpec.addMethod(constructor);

    DebugGenerator.builder()
                  .eventBusMetaModel(eventBusMetaMetaModel)
                  .typeSpec(typeSpec)
                  .build()
                  .generate();

    FilterGenerator.builder()
                   .eventBusMetaModel(eventBusMetaMetaModel)
                   .typeSpec(typeSpec)
                   .build()
                   .generate();

    EventLoadMetaDataGenerator.builder()
                              .processingEnvironment(this.processingEnvironment)
                              .eventBusMetaModel(eventBusMetaMetaModel)
                              .typeSpec(typeSpec)
                              .build()
                              .generate();

    EventMetaDataGenerator.builder()
                          .processingEnvironment(this.processingEnvironment)
                          .eventBusMetaModel(eventBusMetaMetaModel)
                          .build()
                          .generate();

    EventHandlingMethodGenerator.builder()
                                .processorUtils(this.processorUtils)
                                .typeSpec(typeSpec)
                                .eventBusMetaModel(eventBusMetaMetaModel)
                                .historyMetaModel(historyMetaModel)
                                .presenterMetaModel(presenterMetaModel)
                                .handlerMetaModel(handlerMetaModel)
                                .build()
                                .generate();

    AddPresenterGenerator.builder()
                         .typeSpec(typeSpec)
                         .processingEnvironment(this.processingEnvironment)
                         .presenterMetaModel(presenterMetaModel)
                         .build()
                         .generate();

    HandlerAndPresenterRegristrationGenerator.builder()
                                             .processingEnvironment(this.processingEnvironment)
                                             .typeSpec(typeSpec)
                                             .eventBusMetaModel(eventBusMetaMetaModel)
                                             .presenterMetaModel(presenterMetaModel)
                                             .handlerMetaModel(handlerMetaModel)
                                             .build()
                                             .generate();

    JavaFile javaFile = JavaFile.builder(eventBusMetaMetaModel.getEventBus()
                                                              .getPackage(),
                                         typeSpec.build())
                                .build();
//    System.out.println(javaFile.toString());
    try {
      javaFile.writeTo(this.processingEnvironment.getFiler());
    } catch (IOException e) {
      throw new ProcessorException("Unable to write generated file: >>" + eventBusMetaMetaModel.getEventBus()
                                                                                               .getSimpleName() + EventBusGenerator.IMPL_NAME + "<< -> exception: " + e.getMessage());
    }
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;
    TypeElement           eventBusTypeElement;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public Builder eventBusTypeElement(TypeElement eventBusTypeElement) {
      this.eventBusTypeElement = eventBusTypeElement;
      return this;
    }

    public EventBusGenerator build() {
      return new EventBusGenerator(this);
    }
  }
}
