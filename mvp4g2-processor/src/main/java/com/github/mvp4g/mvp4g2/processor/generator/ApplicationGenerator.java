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
import com.github.mvp4g.mvp4g2.core.application.IsApplicationLoader;
import com.github.mvp4g.mvp4g2.core.internal.application.AbstractApplication;
import com.github.mvp4g.mvp4g2.processor.ProcessorException;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.ApplicationMetaModel;

public class ApplicationGenerator {

  private final static String IMPL_NAME = "Impl";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private ApplicationGenerator(Builder builder) {
    super();

    this.processingEnvironment = builder.processingEnvironment;

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

  public void generate(ApplicationMetaModel metaModel)
    throws ProcessorException {
    // check if element is existing (to avoid generating code for deleted items)
    if (!this.processorUtils.doesExist(metaModel.getApplication())) {
      return;
    }
    // generate code
    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(metaModel.getApplication()
                                                               .getSimpleName() + ApplicationGenerator.IMPL_NAME)
                                        .superclass(ParameterizedTypeName.get(ClassName.get(AbstractApplication.class),
                                                                              metaModel.getEventBus()
                                                                                       .getTypeName()))
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL)
                                        .addSuperinterface(metaModel.getApplication()
                                                                    .getTypeName());

    // constructor ...
    MethodSpec constructor = MethodSpec.constructorBuilder()
                                       .addModifiers(Modifier.PUBLIC)
                                       .addStatement("super()")
                                       .addStatement("super.eventBus = new $N.$N()",
                                                     metaModel.getEventBus()
                                                              .getPackage(),
                                                     metaModel.getEventBus()
                                                              .getSimpleName() + ApplicationGenerator.IMPL_NAME)
                                       .addStatement("super.historyOnStart = $L",
                                                     metaModel.getHistoryOnStart())
                                       .build();
    typeSpec.addMethod(constructor);

    // method "getApplicaitonLoader"
    MethodSpec getApplicaitonLaoderMethod = MethodSpec.methodBuilder("getApplicationLoader")
                                                      .addModifiers(Modifier.PUBLIC)
                                                      .addAnnotation(Override.class)
                                                      .returns(IsApplicationLoader.class)
                                                      .addStatement("return new $T()",
                                                                    metaModel.getLoader()
                                                                             .getTypeName())
                                                      .build();
    typeSpec.addMethod(getApplicaitonLaoderMethod);

    JavaFile javaFile = JavaFile.builder(metaModel.getEventBus()
                                                  .getPackage(),
                                         typeSpec.build())
                                .build();
    try {
      javaFile.writeTo(this.processingEnvironment.getFiler());
    } catch (IOException e) {
      throw new ProcessorException("Unable to write generated file: >>" + metaModel.getEventBus()
                                                                                   .getSimpleName() + ApplicationGenerator.IMPL_NAME + "<< -> exception: " + e.getMessage());
    }
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public ApplicationGenerator build() {
      return new ApplicationGenerator(this);
    }
  }
}
