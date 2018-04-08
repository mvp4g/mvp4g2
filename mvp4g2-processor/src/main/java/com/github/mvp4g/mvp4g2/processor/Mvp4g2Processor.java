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

package com.github.mvp4g.mvp4g2.processor;


import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import com.google.auto.service.AutoService;
import com.github.mvp4g.mvp4g2.core.application.annotation.Application;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Debug;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Event;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.EventBus;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Filters;
import com.github.mvp4g.mvp4g2.core.history.annotation.History;
import com.github.mvp4g.mvp4g2.core.ui.annotation.EventHandler;
import com.github.mvp4g.mvp4g2.core.ui.annotation.Handler;
import com.github.mvp4g.mvp4g2.core.ui.annotation.Presenter;
import com.github.mvp4g.mvp4g2.processor.generator.ApplicationGenerator;
import com.github.mvp4g.mvp4g2.processor.generator.EventBusGenerator;
import com.github.mvp4g.mvp4g2.processor.generator.HandlerGenerator;
import com.github.mvp4g.mvp4g2.processor.generator.HistoryGenerator;
import com.github.mvp4g.mvp4g2.processor.generator.PresenterGenerator;
import com.github.mvp4g.mvp4g2.processor.model.ApplicationMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.EventBusMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.HandlerMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.HistoryMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.PresenterMetaModel;
import com.github.mvp4g.mvp4g2.processor.scanner.ApplicationAnnotationScanner;
import com.github.mvp4g.mvp4g2.processor.scanner.EventBusAnnotationScanner;
import com.github.mvp4g.mvp4g2.processor.scanner.HandlerAnnotationScanner;
import com.github.mvp4g.mvp4g2.processor.scanner.HistoryAnnotationScanner;
import com.github.mvp4g.mvp4g2.processor.scanner.PresenterAnnotationScanner;
import com.github.mvp4g.mvp4g2.processor.scanner.validation.ModelValidator;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class Mvp4g2Processor
  extends AbstractProcessor {

  private ProcessorUtils processorUtils;

  private ApplicationAnnotationScanner applicationAnnotationScanner;
  private EventBusAnnotationScanner    eventBusAnnotationScanner;
  private HandlerAnnotationScanner     handlerAnnotationScanner;
  private HistoryAnnotationScanner     historyAnnotationScanner;
  private PresenterAnnotationScanner   presenterAnnotationScanner;

  private ApplicationGenerator applicationGenerator;
  private EventBusGenerator    eventBusGenerator;
  private HandlerGenerator     handlerGenerator;
  private HistoryGenerator     historyGenerator;
  private PresenterGenerator   presenterGenerator;

  private ApplicationMetaModel applicationMetaModel;
  private EventBusMetaModel    eventBusMetaModel;
  private HandlerMetaModel     handlerMetaModel;
  private HistoryMetaModel     historyMetaModel;
  private PresenterMetaModel   presenterMetaModel;

  public Mvp4g2Processor() {
    super();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return of(Application.class.getCanonicalName(),
              Debug.class.getCanonicalName(),
              Event.class.getCanonicalName(),
              EventBus.class.getCanonicalName(),
              EventHandler.class.getCanonicalName(),
              Filters.class.getCanonicalName(),
              Handler.class.getCanonicalName(),
              History.class.getCanonicalName(),
              Presenter.class.getCanonicalName()).collect(toSet());
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations,
                         RoundEnvironment roundEnv) {
    setUp(roundEnv);
    processorUtils.createNoteMessage("Mvp4g2-Processor triggered: " + System.currentTimeMillis());
    try {
      if (roundEnv.processingOver()) {
        this.generate();
      } else {
        this.scan(roundEnv);
        this.validateModels(roundEnv);
      }
      return false;
    } catch (ProcessorException e) {
      this.processorUtils.createErrorMessage(e.getMessage());
    }
    return false;
  }

  private void setUp(RoundEnvironment roundEnv) {
    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(processingEnv)
                                        .build();

    this.applicationAnnotationScanner = ApplicationAnnotationScanner.builder()
                                                                    .roundEnvironment(roundEnv)
                                                                    .processingEnvironment(this.processingEnv)
                                                                    .build();
    this.eventBusAnnotationScanner = EventBusAnnotationScanner.builder()
                                                              .roundEnvironment(roundEnv)
                                                              .processingEnvironment(this.processingEnv)
                                                              .build();
    this.handlerAnnotationScanner = HandlerAnnotationScanner.builder()
                                                            .processingEnvironment(this.processingEnv)
                                                            .build();
    this.historyAnnotationScanner = HistoryAnnotationScanner.builder()
                                                            .processingEnvironment(this.processingEnv)
                                                            .build();
    this.presenterAnnotationScanner = PresenterAnnotationScanner.builder()
                                                                .processingEnvironment(this.processingEnv)
                                                                .build();

    this.applicationGenerator = ApplicationGenerator.builder()
                                                    .processingEnvironment(this.processingEnv)
                                                    .build();
    this.eventBusGenerator = EventBusGenerator.builder()
                                              .processingEnvironment(this.processingEnv)
                                              .build();
    this.handlerGenerator = HandlerGenerator.builder()
                                            .processingEnvironment(this.processingEnv)
                                            .build();
    this.historyGenerator = HistoryGenerator.builder()
                                            .processingEnvironment(this.processingEnv)
                                            .build();
    this.presenterGenerator = PresenterGenerator.builder()
                                                .processingEnvironment(this.processingEnv)
                                                .build();
  }

  private void generate()
    throws ProcessorException {
    if (!isNull(this.applicationMetaModel)) {
      this.applicationGenerator.generate(this.applicationMetaModel);
    }
    if (!isNull(this.handlerMetaModel)) {
      this.handlerGenerator.generate(this.handlerMetaModel);
    }
    if (!isNull(this.presenterMetaModel)) {
      this.presenterGenerator.generate(this.presenterMetaModel);
    }
    if (!isNull(this.historyMetaModel)) {
      this.historyGenerator.generate(this.historyMetaModel);
    }
    if (!isNull(this.eventBusMetaModel)) {
      this.eventBusGenerator.generate(this.eventBusMetaModel,
                                      this.handlerMetaModel,
                                      this.presenterMetaModel,
                                      this.historyMetaModel);
    }
  }

  private void scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    this.applicationMetaModel = this.applicationAnnotationScanner.scan(roundEnvironment);
    this.handlerMetaModel = this.handlerAnnotationScanner.scan(roundEnvironment);
    this.presenterMetaModel = this.presenterAnnotationScanner.scan(roundEnvironment);
    this.historyMetaModel = this.historyAnnotationScanner.scan(roundEnvironment);
    this.eventBusMetaModel = this.eventBusAnnotationScanner.scan(roundEnvironment);
  }

  private void validateModels(RoundEnvironment roundEnv)
    throws ProcessorException {
    ModelValidator.builder()
                  .processorUtils(this.processorUtils)
                  .eventBusMetaModel(this.eventBusMetaModel)
                  .handlerMetaModel(this.handlerMetaModel)
                  .presenterMetaModel(this.presenterMetaModel)
                  .build()
                  .validate();
  }
}
