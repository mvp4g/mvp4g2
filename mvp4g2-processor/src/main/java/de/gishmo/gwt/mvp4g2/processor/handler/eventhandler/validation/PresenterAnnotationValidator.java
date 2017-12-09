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

import de.gishmo.gwt.mvp4g2.client.ui.AbstractPresenter;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.handler.eventhandler.PresenterUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class PresenterAnnotationValidator {

  private ProcessorUtils processorUtils;
  private PresenterUtils presenterUtils;

  private RoundEnvironment      roundEnvironment;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private PresenterAnnotationValidator() {
  }

  private PresenterAnnotationValidator(Builder builder) {
    this.roundEnvironment = builder.roundEnvironment;
    this.processingEnvironment = builder.processingEnvironment;

    setUp();
  }

  private void setUp() {
    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
    this.presenterUtils = PresenterUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void validate()
    throws ProcessorException {
//    // get elements annotated with Applicaiton annotation
//    Set<? extends Element> elementsWithApplicaitonAnnotation = this.roundEnvironment.getElementsAnnotatedWith(Application.class);
//    // at least there should exatly one Application annotation!
//    if (elementsWithApplicaitonAnnotation.size() == 0) {
//      throw new ProcessorException("Missing Mvp4g Application interface");
//    }
//    // at least there should only one Application annotation!
//    if (elementsWithApplicaitonAnnotation.size() > 1) {
//      throw new ProcessorException("There should be at least only one interface, that is annotated with @Application");
//    }
  }

  public void validate(Element element)
    throws ProcessorException {
    if (element instanceof TypeElement) {
      TypeElement typeElement = (TypeElement) element;
      // check, that the presenter annotion is only used with classes
      if (!typeElement.getKind()
                      .isClass()) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @Presenter can only be used with as class!");
      }
      // check, that the view attribute is a class
      TypeElement viewClassElement = this.presenterUtils.getViewClassTypeElement(element.getAnnotation(Presenter.class));
      TypeElement viewInterfaceElement = this.presenterUtils.getViewInterfaceTypeElement(element.getAnnotation(Presenter.class));
      // check, that the viewClass is a class
      if (!viewClassElement.getKind()
                           .isClass()) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": the viewClass-attribute of a @Presenter must be a class!");
      }
      // chekc if the vioewInterface is a interface
      if (!viewInterfaceElement.getKind()
                               .isInterface()) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": the viewInterface-attribute of a @Presenter must be a interface!");
      }
      // check, if viewClass is implementing viewInterface
      if (!this.processorUtils.implementsInterface(this.processingEnvironment,
                                                   viewClassElement,
                                                   viewInterfaceElement.asType())) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": the viewClass-attribute of a @Presenter must implement the viewInterface!");
      }
      // check, that the typeElement extends AbstarctEventHandler
      if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                       typeElement.asType(),
                                                       this.processingEnvironment.getElementUtils()
                                                                                 .getTypeElement(AbstractPresenter.class.getCanonicalName())
                                                                                 .asType())) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @Presenter must extend AbstractPresenter.class!");
      }
    } else {
      throw new ProcessorException("@Presenter can only be used on a type (class)");
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

    public PresenterAnnotationValidator build() {
      return new PresenterAnnotationValidator(this);
    }
  }
}
