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
import de.gishmo.gwt.mvp4g2.core.eventbus.IsMvp4g2Logger;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import java.util.Set;

public class DebugAnnotationValidator {

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;

  @SuppressWarnings("unused")
  private DebugAnnotationValidator() {
  }

  private DebugAnnotationValidator(Builder builder) {
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
    // get elements annotated with Debug annotation
    Set<? extends Element> elementsWithDebugAnnotation = this.roundEnvironment.getElementsAnnotatedWith(Debug.class);
    // at least there should only one Application annotation!
    if (elementsWithDebugAnnotation.size() > 1) {
      throw new ProcessorException("There should be at least only one interface, that is annotated with @Debug");
    }
    for (Element element : elementsWithDebugAnnotation) {
      if (element instanceof TypeElement) {
        TypeElement typeElement = (TypeElement) element;
        // @Debug can only be used on a interface
        if (!typeElement.getKind()
                        .isInterface()) {
          throw new ProcessorException("@Debug can only be used with an interface");
        }
        // @Debug can only be used on a interface that extends IsEventBus
        if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                         typeElement.asType(),
                                                         this.processingEnvironment.getElementUtils()
                                                                                   .getTypeElement(IsEventBus.class.getCanonicalName())
                                                                                   .asType())) {
          throw new ProcessorException("@Debug can only be used on interfaces that extends IsEventBus");
        }
        // the loggerinside the annotation must extends IsMvp4g2Logger!
        TypeElement loggerElement = this.getLogger(typeElement.getAnnotation(Debug.class));
        if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                         loggerElement.asType(),
                                                         this.processingEnvironment.getElementUtils()
                                                                                   .getTypeElement(IsMvp4g2Logger.class.getCanonicalName())
                                                                                   .asType())) {
          throw new ProcessorException("@Debug - the logger attribute needs class that extends IsMvp4g2Logger");
        }
      } else {
        throw new ProcessorException("@Debug can only be used on a type (interface)");
      }
    }
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

    public DebugAnnotationValidator build() {
      return new DebugAnnotationValidator(this);
    }
  }
}
