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
package de.gishmo.gwt.mvp4g2.processor.handler.application.validation;

import de.gishmo.gwt.mvp4g2.client.application.IsApplication;
import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public class ApplicationAnnotationValidator {

  private ProcessorUtils processorUtils;

  private RoundEnvironment      roundEnvironment;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private ApplicationAnnotationValidator() {
  }

  private ApplicationAnnotationValidator(Builder builder) {
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
    // get elements annotated with Applicaiton annotation
    Set<? extends Element> elementsWithApplicaitonAnnotation = this.roundEnvironment.getElementsAnnotatedWith(Application.class);
    // at least there should exatly one Application annotation!
    if (elementsWithApplicaitonAnnotation.size() == 0) {
      throw new ProcessorException("Missing Mvp4g Application interface");
    }
    // at least there should only one Application annotation!
    if (elementsWithApplicaitonAnnotation.size() > 1) {
      throw new ProcessorException("There should be at least only one interface, that is annotated with @Application");
    }
  }

  public void validate(Element element)
    throws ProcessorException {
    if (element instanceof TypeElement) {
      TypeElement typeElement = (TypeElement) element;
      // annotated element has to be a interface
      if (!typeElement.getKind()
                      .isInterface()) {
        throw new ProcessorException("@Application annotated must be used with an interface");
      }
      // check, that the typeElement implements IsApplication
      if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                       typeElement.asType(),
                                                       this.processingEnvironment.getElementUtils()
                                                                                 .getTypeElement(IsApplication.class.getCanonicalName())
                                                                                 .asType())) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @Application must implement IsApplication interface");
      }
    } else {
      throw new ProcessorException("@Application can only be used on a type (interface)");
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

    public ApplicationAnnotationValidator build() {
      return new ApplicationAnnotationValidator(this);
    }
  }
}
