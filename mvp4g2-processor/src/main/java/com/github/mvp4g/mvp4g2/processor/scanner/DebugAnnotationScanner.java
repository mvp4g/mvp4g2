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

package com.github.mvp4g.mvp4g2.processor.scanner;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;

import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Debug;
import com.github.mvp4g.mvp4g2.processor.ProcessorException;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.EventBusMetaModel;
import com.github.mvp4g.mvp4g2.processor.scanner.validation.DebugAnnotationValidator;

import static java.util.Objects.isNull;

public class DebugAnnotationScanner {

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private TypeElement           eventBusTypeElement;
  private EventBusMetaModel     eventBusMetaModel;

  @SuppressWarnings("unused")
  private DebugAnnotationScanner(Builder builder) {
    super();
    this.processingEnvironment = builder.processingEnvironment;
    this.eventBusTypeElement = builder.eventBusTypeElement;
    this.eventBusMetaModel = builder.eventBusMetaModel;
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

  public EventBusMetaModel scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // do validation
    DebugAnnotationValidator.builder()
                            .roundEnvironment(roundEnvironment)
                            .processingEnvironment(processingEnvironment)
                            .build()
                            .validate();
    // handle debug-annotation
    Debug debugAnnotation = eventBusTypeElement.getAnnotation(Debug.class);
    if (!isNull(debugAnnotation)) {
      this.eventBusMetaModel.setHasDebugAnnotation("true");
      this.eventBusMetaModel.setDebugLogLevel(debugAnnotation.logLevel()
                                                             .toString());
      if (!isNull(getLogger(debugAnnotation))) {
        this.eventBusMetaModel.setDebugLogger(getLogger(debugAnnotation).getQualifiedName()
                                                                        .toString());
      }
    } else {
      this.eventBusMetaModel.setHasDebugAnnotation("false");
      this.eventBusMetaModel.setDebugLogLevel("");
      this.eventBusMetaModel.setDebugLogger("");
    }
    return this.eventBusMetaModel;
  }

  private TypeElement getLogger(Debug debugAnnotation) {
    try {
      debugAnnotation.logger();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;
    TypeElement           eventBusTypeElement;
    EventBusMetaModel     eventBusMetaModel;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public Builder eventBusTypeElement(TypeElement eventBusTypeElement) {
      this.eventBusTypeElement = eventBusTypeElement;
      return this;
    }

    public Builder eventBusMetaModel(EventBusMetaModel eventBusMetaModel) {
      this.eventBusMetaModel = eventBusMetaModel;
      return this;
    }

    public DebugAnnotationScanner build() {
      return new DebugAnnotationScanner(this);
    }
  }
}
