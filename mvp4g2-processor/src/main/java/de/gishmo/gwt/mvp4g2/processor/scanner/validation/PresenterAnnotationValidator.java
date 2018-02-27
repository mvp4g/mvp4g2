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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import de.gishmo.gwt.mvp4g2.core.ui.AbstractPresenter;
import de.gishmo.gwt.mvp4g2.core.ui.IsViewCreator;
import de.gishmo.gwt.mvp4g2.core.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

public class PresenterAnnotationValidator {

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private PresenterAnnotationValidator() {
  }

  private PresenterAnnotationValidator(Builder builder) {
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

  public void validate(Element element,
                       TypeElement viewClassTypeElement,
                       TypeElement viewInterfaceTypeElement)
    throws ProcessorException {
    if (element instanceof TypeElement) {
      TypeElement typeElement = (TypeElement) element;
      // check, that the presenter annotion is only used with classes
      if (!typeElement.getKind()
                      .isClass()) {
        throw new ProcessorException("Mvp4g2Processor:" + typeElement.getSimpleName()
                                                                     .toString() + ": @Presenter can only be used with a class!");
      }
      // check, that the viewClass is a class
      if (!viewClassTypeElement.getKind()
                               .isClass()) {
        throw new ProcessorException("Mvp4g2Processor:" + typeElement.getSimpleName()
                                                                     .toString() + ": the viewClass-attribute of a @Presenter must be a class!");
      }
      // chekc if the vioewInterface is a interface
      if (!viewInterfaceTypeElement.getKind()
                                   .isInterface()) {
        throw new ProcessorException("Mvp4g2Processor:" + typeElement.getSimpleName()
                                                                     .toString() + ": the viewInterface-attribute of a @Presenter must be a interface!");
      }
      // check, if viewClass is implementing viewInterface
      if (!this.processorUtils.implementsInterface(this.processingEnvironment,
                                                   viewClassTypeElement,
                                                   viewInterfaceTypeElement.asType())) {
        throw new ProcessorException("Mvp4g2Processor:" + typeElement.getSimpleName()
                                                                     .toString() + ": the viewClass-attribute of a @Presenter must implement the viewInterface!");
      }
      // check, that the typeElement extends AbstractHandler
      if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                       typeElement.asType(),
                                                       this.processingEnvironment.getElementUtils()
                                                                                 .getTypeElement(AbstractPresenter.class.getCanonicalName())
                                                                                 .asType())) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @Presenter must extend AbstractPresenter.class!");
      }
      // check if annotated class is abstract
      if (typeElement.getModifiers()
                     .contains(Modifier.ABSTRACT)) {
        throw new ProcessorException("Mvp4g2Processor:" + typeElement.getSimpleName()
                                                                     .toString() + ": @Presenter can not be ABSTRACT");
      }
      // check if class attribute is not abstradt
      if (viewClassTypeElement.getModifiers()
                              .contains(Modifier.ABSTRACT)) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": class-attribute of @Presenter can not be ABSTRACT");
      }
      // check, if the viewCreator is set to presenter, the presenter has to implement the
      // IsViewCreator interface!
      Presenter presenterAnnotation = typeElement.getAnnotation(Presenter.class);
      if (Presenter.VIEW_CREATION_METHOD.PRESENTER.equals(presenterAnnotation.viewCreator())) {
        if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                         typeElement.asType(),
                                                         this.processingEnvironment.getElementUtils()
                                                                                   .getTypeElement(IsViewCreator.class.getCanonicalName())
                                                                                   .asType())) {
          throw new ProcessorException(typeElement.getSimpleName()
                                                  .toString() + ": @Presenter must implement the IsViewCreator interface");
        }
      }
    } else {
      throw new ProcessorException("Mvp4g2Processor: @Presenter can only be used on a type (class)");
    }
    // TODo El Hoss: check, that @Event is only used inside a EventBus
  }

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public PresenterAnnotationValidator build() {
      return new PresenterAnnotationValidator(this);
    }
  }
}
