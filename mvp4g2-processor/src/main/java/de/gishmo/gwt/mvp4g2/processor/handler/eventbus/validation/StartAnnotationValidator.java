/*
 * Copyright 2015-2017 Frank Hossfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.gishmo.gwt.mvp4g2.processor.handler.eventbus.validation;

import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Start;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.handler.eventbus.type.EventBusUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

// TODO check, that @Filter is annoted at a interface that extends IsEventBus! and other tests ...
public class StartAnnotationValidator {

  private ProcessorUtils processorUtils;
  private EventBusUtils  eventBusUtils;

  private RoundEnvironment      roundEnvironment;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private StartAnnotationValidator() {
  }

  private StartAnnotationValidator(Builder builder) {
    this.roundEnvironment = builder.roundEnvironment;
    this.processingEnvironment = builder.processingEnvironment;

    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
    this.eventBusUtils = EventBusUtils.builder()
                                      .processingEnvironment(this.processingEnvironment)
                                      .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void validate(TypeElement eventBusTypeElement)
    throws ProcessorException {
    // check, if there are more than @Start annotated method
    List<Element> elementsAnnotatedWithStart = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                                         eventBusTypeElement,
                                                                                                         Start.class);
    if (elementsAnnotatedWithStart.size() > 1) {
      throw new ProcessorException("@Start-annotation can only be used a single time in a eventbus interface");
    } else {
      // check, that the @Start annotation is only used on zero-argument methods!
      if (elementsAnnotatedWithStart.size() == 1) {
        ExecutableElement executableElement = (ExecutableElement) elementsAnnotatedWithStart.get(0);
        if (executableElement.getParameters().size() > 0) {
          throw new ProcessorException("@Start-annotation can only be used on zero argument methods");
        }
      }
    }
  }

  public static final class Builder {

    RoundEnvironment      roundEnvironment;
    ProcessingEnvironment processingEnvironment;

    /**
     * Set the round envirement
     *
     * @param roundEnvironment the round envirement
     * @return the Builder
     */
    public Builder roundEnvironment(RoundEnvironment roundEnvironment) {
      this.roundEnvironment = roundEnvironment;
      return this;
    }

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

    public StartAnnotationValidator build() {
      return new StartAnnotationValidator(this);
    }
  }
}
