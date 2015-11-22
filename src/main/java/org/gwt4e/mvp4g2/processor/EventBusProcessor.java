/*
 * Copyright (C) 2015 Frank Hossfeld
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

package org.gwt4e.mvp4g2.processor;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;
import org.gwt4e.mvp4g2.processor.steps.EventBusProcessingStep;
import org.gwt4e.mvp4g2.processor.steps.EventProcessingStep;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class EventBusProcessor
  extends BasicAnnotationProcessor {

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  protected Iterable<? extends ProcessingStep> initSteps() {

    Messager messager = processingEnv.getMessager();
    Filer    filer    = processingEnv.getFiler();
    Types    types    = processingEnv.getTypeUtils();
    Elements elements = processingEnv.getElementUtils();

    EventBusProcessorContext context = new EventBusProcessorContext();

    return ImmutableList.of(new EventProcessingStep(messager,
                                                    filer,
                                                    types,
                                                    elements,
                                                    context),
                            new EventBusProcessingStep(messager,
                                                       filer,
                                                       types,
                                                       elements,
                                                       context));
  }
}
