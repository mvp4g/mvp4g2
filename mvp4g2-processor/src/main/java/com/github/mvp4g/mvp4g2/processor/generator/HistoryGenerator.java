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
import com.squareup.javapoet.TypeSpec;
import com.github.mvp4g.mvp4g2.core.history.annotation.History;
import com.github.mvp4g.mvp4g2.core.internal.history.HistoryMetaData;
import com.github.mvp4g.mvp4g2.processor.ProcessorException;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.HistoryMetaModel;

public class HistoryGenerator {

  private ProcessorUtils processorUtils;

  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private HistoryGenerator(Builder builder) {
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

  public void generate(HistoryMetaModel metaModel)
    throws ProcessorException {
    for (String historyConverterClassName : metaModel.getHistoryConverterClassNames()) {
      HistoryMetaModel.HistoryData historyData = metaModel.getHistoryData(historyConverterClassName);
      TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.processorUtils.createHistoryMetaDataClassName(historyData.getHistoryConverter()
                                                                                                                      .getClassName()))
                                          .superclass(ClassName.get(HistoryMetaData.class))
                                          .addModifiers(Modifier.PUBLIC,
                                                        Modifier.FINAL);
      // constructor ...
      MethodSpec constructor = MethodSpec.constructorBuilder()
                                         .addModifiers(Modifier.PUBLIC)
                                         .addStatement("super($S, $T.$L)",
                                                       historyConverterClassName,
                                                       ClassName.get(History.HistoryConverterType.class),
                                                       historyData.getHistoryConverterType())
                                         .build();
      typeSpec.addMethod(constructor);

      JavaFile javaFile = JavaFile.builder(historyData.getHistoryConverter()
                                                      .getPackage(),
                                           typeSpec.build())
                                  .build();
      try {
        javaFile.writeTo(this.processingEnvironment.getFiler());
      } catch (IOException e) {
        throw new ProcessorException("Unable to write generated file: >>" + this.processorUtils.createHistoryMetaDataClassName(historyData.getHistoryConverter()
                                                                                                                                          .getClassName()) + "<< -> exception: " + e.getMessage());
      }
    }
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public HistoryGenerator build() {
      return new HistoryGenerator(this);
    }
  }
}
