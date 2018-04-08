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
import com.github.mvp4g.mvp4g2.core.internal.ui.HandlerMetaData;
import com.github.mvp4g.mvp4g2.processor.ProcessorConstants;
import com.github.mvp4g.mvp4g2.processor.ProcessorException;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.HandlerMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.intern.ClassNameModel;

public class HandlerGenerator {

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private HandlerGenerator(Builder builder) {
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

  public void generate(HandlerMetaModel metaModel)
    throws ProcessorException {
    ClassName eventHandlerMetaDataClassName = ClassName.get(HandlerMetaData.class);
    ClassName eventHandlerMetaDataKindClassName = ClassName.get(HandlerMetaData.Kind.class);

    for (String eventHandler : metaModel.getHandlerKeys()) {
      // check if element is existing (to avoid generating code for deleted items)
      if (this.processorUtils.doesExist(new ClassNameModel(eventHandler))) {
        HandlerMetaModel.HandlerData data = metaModel.getHandlerData(eventHandler);
        String className = this.processorUtils.createFullClassName(data.getHandler()
                                                                       .getPackage(),
                                                                   data.getHandler()
                                                                       .getSimpleName() + ProcessorConstants.META_DATA);
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.processorUtils.setFirstCharacterToUpperCase(className))
                                            .superclass(ParameterizedTypeName.get(eventHandlerMetaDataClassName,
                                                                                  data.getHandler()
                                                                                      .getTypeName()))
                                            .addModifiers(Modifier.PUBLIC,
                                                          Modifier.FINAL);
        // constructor ...
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                                   .addModifiers(Modifier.PUBLIC)
                                                   .addStatement("super($S, $T.EVENT_HANDLER, new $T())",
                                                                 data.getHandler()
                                                                     .getClassName(),
                                                                 eventHandlerMetaDataKindClassName,
                                                                 data.getHandler()
                                                                     .getTypeName());
        typeSpec.addMethod(constructor.build());

        JavaFile javaFile = JavaFile.builder(data.getHandler()
                                                 .getPackage(),
                                             typeSpec.build())
                                    .build();
        try {
          javaFile.writeTo(this.processingEnvironment.getFiler());
        } catch (IOException e) {
          throw new ProcessorException("Unable to write generated file: >>" + data.getHandler()
                                                                                  .getSimpleName() + ProcessorConstants.META_DATA + "<< -> exception: " + e.getMessage());
        }
      }
    }
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public HandlerGenerator build() {
      return new HandlerGenerator(this);
    }
  }
}
