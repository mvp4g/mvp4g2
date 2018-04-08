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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Event;
import com.github.mvp4g.mvp4g2.core.history.annotation.InitHistory;
import com.github.mvp4g.mvp4g2.core.history.annotation.NotFoundHistory;
import com.github.mvp4g.mvp4g2.processor.ProcessorException;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.EventBusMetaModel;

public class EventAnnotationValidator {

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;
  private EventBusMetaModel     eventBusMetaModel;
  private TypeElement           eventBusTypeElement;

  @SuppressWarnings("unused")
  private EventAnnotationValidator() {
  }

  private EventAnnotationValidator(Builder builder) {
    this.processingEnvironment = builder.processingEnvironment;
    this.roundEnvironment = builder.roundEnvironment;
    this.eventBusMetaModel = builder.eventBusMetaModel;
    this.eventBusTypeElement = builder.eventBusTypeElement;
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
    // check if all historyNames are unique!
    List<String> historyNames = new ArrayList<>();
    for (Element element : this.roundEnvironment.getElementsAnnotatedWith(Event.class)) {
      Event eventAnnotation = element.getAnnotation(Event.class);
      // check, that the parent element extends IsEventBus
      if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                       element.getEnclosingElement()
                                                              .asType(),
                                                       this.processingEnvironment.getElementUtils()
                                                                                 .getTypeElement(IsEventBus.class.getCanonicalName())
                                                                                 .asType())) {
        throw new ProcessorException("Mvp4g2Processor: @Event can only be used inside a event bus! >>" + element.getEnclosingElement()
                                                                                                                .toString() + "<< does no implement IsEventBus");
      }
      if (!Event.DEFAULT_HISTORY_NAME.equals(eventAnnotation.historyName())) {
        if (historyNames.contains(eventAnnotation.historyName())) {
          throw new ProcessorException("Mvp4g2Processor: EventElement: >>" + element.getSimpleName()
                                                                                    .toString() + "<< using a already used historyName -> >>" + eventAnnotation.historyName() + "<<");
        } else {
          historyNames.add(eventAnnotation.historyName());
        }
      }
      ExecutableElement eventExecutableElement = (ExecutableElement) element;
      if (!"void".equals(eventExecutableElement.getReturnType()
                                               .toString())) {
        throw new ProcessorException("Mvp4g2Processor: EventElement: >>" + element.getSimpleName()
                                                                                  .toString() + "<< must return 'void'");
      }
    }
    // check, if there are more than one InitHistory-annotation!
    if (this.roundEnvironment.getElementsAnnotatedWith(InitHistory.class)
                             .size() > 1) {
      throw new ProcessorException("Mvp4g2Processor: @InitHistory can only be set a single time inside a event bus");
    } else if (this.roundEnvironment.getElementsAnnotatedWith(InitHistory.class)
                                    .size() == 1) {
      for (Element element : this.roundEnvironment.getElementsAnnotatedWith(InitHistory.class)) {
        ExecutableElement executableElement = (ExecutableElement) element;
        if (((ExecutableElement) element).getParameters()
                                         .size() > 0) {
          throw new ProcessorException("Mvp4g2Processor: @InitHistory can only be used on a method with no arguments");
        }
      }
    }
    // check, if there are more than one NotFoundHistory-annotation!
    if (this.roundEnvironment.getElementsAnnotatedWith(NotFoundHistory.class)
                             .size() > 1) {
      throw new ProcessorException("Mvp4g2Processor: @NotFoundHistory can only be set a single time inside a event bus");
    } else if (this.roundEnvironment.getElementsAnnotatedWith(NotFoundHistory.class)
                                    .size() == 1) {
      for (Element element : this.roundEnvironment.getElementsAnnotatedWith(NotFoundHistory.class)) {
        ExecutableElement executableElement = (ExecutableElement) element;
        if (((ExecutableElement) element).getParameters()
                                         .size() > 0) {
          throw new ProcessorException("Mvp4g2Processor: @NotFoundHistory can only be used on a method with no arguments");
        }
      }
    }
  }

  public void validate(Element element)
    throws ProcessorException {
    ExecutableElement executableElement = (ExecutableElement) element;
    // a passive event can not have bindings ...
    if (executableElement.getAnnotation(Event.class)
                         .passive()) {
      List<String> bindHandlerClasses = this.getElementsFromAnnotationAsList(executableElement,
                                                                             "bind");
      if (bindHandlerClasses.size() > 0) {
        throw new ProcessorException("Mvp4g2Processor: Event: >>" + executableElement.getSimpleName() + "<< a passive event can not have a bind-attribute");
      }
    }
    // TODo El Hoss: check, that @Event is only used inside a EventBus
  }

  private List<String> getElementsFromAnnotationAsList(ExecutableElement executableElement,
                                                       String attribute) {
    Element eventAnnotation = this.processingEnvironment.getElementUtils()
                                                        .getTypeElement(Event.class.getName());
    TypeMirror eventAnnotationAsTypeMirror = eventAnnotation.asType();
    return executableElement.getAnnotationMirrors()
                            .stream()
                            .filter(annotationMirror -> annotationMirror.getAnnotationType()
                                                                        .equals(eventAnnotationAsTypeMirror))
                            .flatMap(annotationMirror -> annotationMirror.getElementValues()
                                                                         .entrySet()
                                                                         .stream())
                            .filter(entry -> attribute.equals(entry.getKey()
                                                                   .getSimpleName()
                                                                   .toString()))
                            .findFirst()
                            .map(entry -> Arrays.stream(entry.getValue()
                                                             .toString()
                                                             .replace("{",
                                                                      "")
                                                             .replace("}",
                                                                      "")
                                                             .replace(" ",
                                                                      "")
                                                             .split(","))
                                                .map((v) -> v.substring(0,
                                                                        v.indexOf(".class")))
                                                .collect(Collectors.toList()))
                            .orElse(null);
  }

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;
    RoundEnvironment      roundEnvironment;
    TypeElement           eventBusTypeElement;
    EventBusMetaModel     eventBusMetaModel;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public Builder roundEnvironment(RoundEnvironment roundEnvironment) {
      this.roundEnvironment = roundEnvironment;
      return this;
    }

    public Builder eventBusMetaModel(EventBusMetaModel eventBusMetaModel) {
      this.eventBusMetaModel = eventBusMetaModel;
      return this;
    }

    public Builder eventBusTypeElement(TypeElement eventBusTypeElement) {
      this.eventBusTypeElement = eventBusTypeElement;
      return this;
    }

    public EventAnnotationValidator build() {
      return new EventAnnotationValidator(this);
    }
  }
}
