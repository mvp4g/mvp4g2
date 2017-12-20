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
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

public class EventAnnotationValidator {

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;
  private TypeElement           eventBusTypeElement;
  private Element               eventElement;

  @SuppressWarnings("unused")
  private EventAnnotationValidator() {
  }

  private EventAnnotationValidator(Builder builder) {
    this.processingEnvironment = builder.processingEnvironment;
    this.roundEnvironment = builder.roundEnvironment;
    this.eventBusTypeElement = builder.eventBusTypeElement;
    this.eventElement = builder.eventElement;
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
    //    // get elements annotated with Debug annotation
    //    Set<? extends Element> elementsWithDebugAnnotation = this.roundEnvironment.getElementsAnnotatedWith(Debug.class);
    //    // at least there should only one Application annotation!
    //    if (elementsWithDebugAnnotation.size() > 1) {
    //      throw new ProcessorException("There should be at least only one interface, that is annotated with @Debug");
    //    }
    //    for (Element element : elementsWithDebugAnnotation) {
    //      if (element instanceof TypeElement) {
    //        TypeElement typeElement = (TypeElement) element;
    //        // @Debug can only be used on a interface
    //        if (!typeElement.getKind()
    //                        .isInterface()) {
    //          throw new ProcessorException("@Debug can only be used with an interface");
    //        }
    //        // @Debug can only be used on a interface that extends IsEventBus
    //        if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
    //                                                         typeElement.asType(),
    //                                                         this.processingEnvironment.getElementUtils()
    //                                                                                   .getTypeElement(IsEventBus.class.getCanonicalName())
    //                                                                                   .asType())) {
    //          throw new ProcessorException("@Debug can only be used on interfaces that extends IsEventBus");
    //        }
    //        // the loggerinside the annotation must extends IsMvp4g2Logger!
    //        TypeElement loggerElement = this.getLogger(typeElement.getAnnotation(Debug.class));
    //        if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
    //                                                         loggerElement.asType(),
    //                                                         this.processingEnvironment.getElementUtils()
    //                                                                                   .getTypeElement(IsMvp4g2Logger.class.getCanonicalName())
    //                                                                                   .asType())) {
    //          throw new ProcessorException("@Debug - the logger attribute needs class that extends IsMvp4g2Logger");
    //        }
    //      } else {
    //        throw new ProcessorException("@Debug can only be used on a type (interface)");
    //      }
    //    }
  }

  //  private TypeElement getLogger(Debug debugAnnotation) {
  //    try {
  //      debugAnnotation.logger();
  //    } catch (MirroredTypeException exception) {
  //      return (TypeElement) this.processingEnvironment.getTypeUtils()
  //                                                     .asElement(exception.getTypeMirror());
  //    }
  //    return null;
  //  }

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;
    RoundEnvironment      roundEnvironment;
    TypeElement           eventBusTypeElement;
    Element               eventElement;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public Builder roundEnvironment(RoundEnvironment roundEnvironment) {
      this.roundEnvironment = roundEnvironment;
      return this;
    }

    public Builder eventBusTypeElement(TypeElement eventBusTypeElement) {
      this.eventBusTypeElement = eventBusTypeElement;
      return this;
    }

    public Builder eventElement(Element eventElement) {
      this.eventElement = eventElement;
      return this;
    }

    public EventAnnotationValidator build() {
      return new EventAnnotationValidator(this);
    }
  }
}
