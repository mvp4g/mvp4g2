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
package de.gishmo.gwt.mvp4g2.processor.scanner.validation;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Start;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

public class EventBusAnnotationValidator {

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;

  @SuppressWarnings("unused")
  private EventBusAnnotationValidator() {
  }

  private EventBusAnnotationValidator(Builder builder) {
    this.processingEnvironment = builder.processingEnvironment;
    this.roundEnvironment = builder.roundEnvironment;
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

  public void validate()
    throws ProcessorException {
    // get elements annotated with EventBus annotation
    Set<? extends Element> elementsWithEventBusAnnotation = this.roundEnvironment.getElementsAnnotatedWith(EventBus.class);
    // at least there should exatly one Application annotation!
    if (elementsWithEventBusAnnotation.size() == 0) {
      throw new ProcessorException("Mvp4g2Processor: Missing Mvp4g EventBus interface");
    }
    // at least there should only one Application annotation!
    if (elementsWithEventBusAnnotation.size() > 1) {
      throw new ProcessorException("Mvp4g2Processor: There should be at least only one interface, that is annotated with @EventBus");
    }
  }

  public void validate(Element element)
    throws ProcessorException {
    // get elements annotated with EventBus annotation
    Set<? extends Element> elementsWithEventBusAnnotation = this.roundEnvironment.getElementsAnnotatedWith(EventBus.class);
    // annotated element has to be a interface
    if (element instanceof TypeElement) {
      TypeElement typeElement = (TypeElement) element;
      // Eventbus must be an interface!
      if (!typeElement.getKind()
                      .isInterface()) {
        throw new ProcessorException("Mvp4g2Processor: @Eventbus can only be used with an interface");
      }
      // check, that the typeElement extends IsEventBus
      if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                       typeElement.asType(),
                                                       this.processingEnvironment.getElementUtils()
                                                                                 .getTypeElement(IsEventBus.class.getCanonicalName())
                                                                                 .asType())) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @Eventbus must extend IsEventBus.class!");
      }



      // shell-attribute validation

      // @Start validation
      // check, if there are more than @Start annotated method
      List<Element> elementsAnnotatedWithStart = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                                           typeElement,
                                                                                                           Start.class);
      if (elementsAnnotatedWithStart.size() > 1) {
        throw new ProcessorException("Mvp4g2Processor: @Start-annotation can only be used a single time in a eventbus interface");
      } else {
        // check, that the @Start annotation is only used on zero-argument methods!
        if (elementsAnnotatedWithStart.size() == 1) {
          ExecutableElement executableElement = (ExecutableElement) elementsAnnotatedWithStart.get(0);
          if (executableElement.getParameters()
                               .size() > 0) {
            throw new ProcessorException("Mvp4g2Processor: @Start-annotation can only be used on zero argument methods");
          }
        }
      }
    } else {
      throw new ProcessorException("Mvp4g2Processor: @Eventbus can only be used on a type (interface)");
    }
  }

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;
    RoundEnvironment      roundEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public Builder roundEnvironment(RoundEnvironment roundEnvironment) {
      this.roundEnvironment = roundEnvironment;
      return this;
    }

    public EventBusAnnotationValidator build() {
      return new EventBusAnnotationValidator(this);
    }
  }
}
