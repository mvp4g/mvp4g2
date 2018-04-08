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
package com.github.mvp4g.mvp4g2.processor.scanner.validation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import com.github.mvp4g.mvp4g2.core.ui.AbstractPresenter;
import com.github.mvp4g.mvp4g2.core.ui.IsShell;
import com.github.mvp4g.mvp4g2.core.ui.IsViewCreator;
import com.github.mvp4g.mvp4g2.core.ui.annotation.Presenter;
import com.github.mvp4g.mvp4g2.processor.ProcessorException;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.intern.ClassNameModel;

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
      // check if a shell presenter does not implememt the multiple feature
      TypeMirror isShellMirror = this.processorUtils.getFlattenedSupertype(this.processingEnvironment.getTypeUtils(),
                                                                           typeElement.asType(),
                                                                           this.processingEnvironment.getElementUtils()
                                                                                                     .getTypeElement(IsShell.class.getCanonicalName())
                                                                                                     .asType());
      if (isShellMirror != null) {
        if (typeElement.getAnnotation(Presenter.class)
                       .multiple()) {
          throw new ProcessorException(typeElement.getSimpleName()
                                                  .toString() + ": IsShell interface can not be used on a presenter which is defiend as multiple = true");
        }
      }
      Presenter presenterAnnotation = typeElement.getAnnotation(Presenter.class);
      if (Presenter.VIEW_CREATION_METHOD.PRESENTER.equals(presenterAnnotation.viewCreator())) {
        // check, if the viewCreator is set to presenter, the presenter has to implement the
        // IsViewCreator interface!
        if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                         typeElement.asType(),
                                                         this.processingEnvironment.getElementUtils()
                                                                                   .getTypeElement(IsViewCreator.class.getCanonicalName())
                                                                                   .asType())) {
          throw new ProcessorException(typeElement.getSimpleName()
                                                  .toString() + ": @Presenter must implement the IsViewCreator interface");
        }
        // check, if the viewCreator has a generic parameter
        if (!this.processorUtils.supertypeHasGeneric(this.processingEnvironment.getTypeUtils(),
                                                     typeElement.asType(),
                                                     this.processingEnvironment.getElementUtils()
                                                                               .getTypeElement(IsViewCreator.class.getCanonicalName())
                                                                               .asType())) {
          throw new ProcessorException(typeElement.getSimpleName()
                                                  .toString() + ": IsViewCreator interface needs " + "a generic parameter (add: >>" + viewInterfaceTypeElement.toString() + "<< as generic to IsViewCreator)");
        } else {
          TypeMirror isViewCreatorTypeMirror = this.processorUtils.getFlattenedSupertype(this.processingEnvironment.getTypeUtils(),
                                                                                         typeElement.asType(),
                                                                                         this.processingEnvironment.getElementUtils()
                                                                                                                   .getTypeElement(IsViewCreator.class.getCanonicalName())
                                                                                                                   .asType());
          ClassNameModel classNameModel = new ClassNameModel(viewInterfaceTypeElement.toString());
          if (!isViewCreatorTypeMirror.toString()
                                      .contains(classNameModel.getSimpleName())) {
            throw new ProcessorException(typeElement.getSimpleName()
                                                    .toString() + ": IsViewCreator interface only allows the generic parameter -> " + viewInterfaceTypeElement.toString());
          }
        }
      } else if (Presenter.VIEW_CREATION_METHOD.FRAMEWORK.equals(presenterAnnotation.viewCreator())) {
        // check, if a presenter implements IsViewCreator, that the viewCreation method is set to PRESENTER!
        if (this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                        typeElement.asType(),
                                                        this.processingEnvironment.getElementUtils()
                                                                                  .getTypeElement(IsViewCreator.class.getCanonicalName())
                                                                                  .asType())) {
          throw new ProcessorException(typeElement.getSimpleName()
                                                  .toString() + ": the IsViewCreator interface can only be used in case of viewCreator = Presenter.VIEW_CREATION_METHOD.PRESENTER");
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
