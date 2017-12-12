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
package de.gishmo.gwt.mvp4g2.processor.handler.eventhandler.validation;

import de.gishmo.gwt.mvp4g2.client.ui.AbstractEventHandler;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class EventHandlerAnnotationValidator {

  private ProcessorUtils processorUtils;

  private RoundEnvironment      roundEnvironment;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private EventHandlerAnnotationValidator() {
  }

  private EventHandlerAnnotationValidator(Builder builder) {
    this.roundEnvironment = builder.roundEnvironment;
    this.processingEnvironment = builder.processingEnvironment;

    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void validate()
    throws ProcessorException {
  }

  public void validate(Element element)
    throws ProcessorException {
    if (element instanceof TypeElement) {
      TypeElement typeElement = (TypeElement) element;
      // check, that the presenter annotion is only used with classes
      if (!typeElement.getKind()
                      .isClass()) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @EventHandler can only be used with as class!");
      }
      // check, that the typeElement extends AbstarctEventHandler
      if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                       typeElement.asType(),
                                                       this.processingEnvironment.getElementUtils()
                                                                                 .getTypeElement(AbstractEventHandler.class.getCanonicalName())
                                                                                 .asType())) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @EventHandler must extend AbstractEventHandler.class!");
      }
      // check if annotated class si abstract
      if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @EventHandler can not be ABSTRACT");
      }
    } else {
      throw new ProcessorException("@EventHandler can only be used on a type (class)");
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

    public EventHandlerAnnotationValidator build() {
      return new EventHandlerAnnotationValidator(this);
    }
  }
}
