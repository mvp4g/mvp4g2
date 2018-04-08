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

package com.github.mvp4g.mvp4g2.processor.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Debug;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.EventBus;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Filters;
import com.github.mvp4g.mvp4g2.processor.ProcessorConstants;
import com.github.mvp4g.mvp4g2.processor.ProcessorException;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.EventBusMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.EventMetaModel;
import com.github.mvp4g.mvp4g2.processor.scanner.validation.EventBusAnnotationValidator;

import static java.util.Objects.isNull;

public class EventBusAnnotationScanner {

  private final static String EVENTBUS_PROPERTIES = "eventBus.properties";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;

  @SuppressWarnings("unused")
  private EventBusAnnotationScanner(Builder builder) {
    super();
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

  public EventBusMetaModel scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // First we try to read an already created resource ...
    EventBusMetaModel model = this.restore();
    // Check if we have an element annotated with @Application
    if (!roundEnvironment.getElementsAnnotatedWith(EventBus.class)
                         .isEmpty()) {
      // create validator
      EventBusAnnotationValidator eventBusValidaitor = EventBusAnnotationValidator.builder()
                                                                                  .roundEnvironment(roundEnvironment)
                                                                                  .processingEnvironment(processingEnvironment)
                                                                                  .build();
      // check, whether we have o do something ...
      eventBusValidaitor.validate();
      // should only be one, so we can search for the first! ...
      Optional<? extends Element> optionalElement = this.roundEnvironment.getElementsAnnotatedWith(EventBus.class)
                                                                         .stream()
                                                                         .findFirst();
      if (optionalElement.isPresent()) {
        Element eventBusAnnotationElement = optionalElement.get();
        eventBusValidaitor.validate(eventBusAnnotationElement);
        EventBus eventBusAnnotation = eventBusAnnotationElement.getAnnotation(EventBus.class);
        if (!isNull(eventBusAnnotation)) {
          TypeElement shellTypeElement = this.getShellTypeElement(eventBusAnnotation);
          model = new EventBusMetaModel(eventBusAnnotationElement.toString(),
                                        isNull(shellTypeElement) ? ""
                                                                 : shellTypeElement.toString());
          // Debug-Annotation
          model = DebugAnnotationScanner.builder()
                                        .processingEnvironment(processingEnvironment)
                                        .eventBusTypeElement((TypeElement) eventBusAnnotationElement)
                                        .eventBusMetaModel(model)
                                        .build()
                                        .scan(roundEnvironment);
          // Filters-Annotation
          model = FiltersAnnotationScanner.builder()
                                          .processingEnvironment(processingEnvironment)
                                          .eventBusTypeElement((TypeElement) eventBusAnnotationElement)
                                          .eventBusMetaModel(model)
                                          .build()
                                          .scan(roundEnvironment);
          // handle Event-annotation
          model = EventAnnotationScanner.builder()
                                        .processingEnvironment(processingEnvironment)
                                        .eventBusTypeElement((TypeElement) eventBusAnnotationElement)
                                        .eventBusMetaModel(model)
                                        .build()
                                        .scan(roundEnvironment);

          // let's store the updated model
          this.processorUtils.store(model,
                                    this.createRelativeFileName());
        }
      }
    } else {
      // @Debug annotation with no @EventBus annotation ... bad idea!
      Optional<? extends Element> optionalDebugElement = this.roundEnvironment.getElementsAnnotatedWith(Debug.class)
                                                                              .stream()
                                                                              .findFirst();
      if (optionalDebugElement.isPresent()) {
        EventBus eventBusAnnotation = optionalDebugElement.get()
                                                          .getAnnotation(EventBus.class);
        if (eventBusAnnotation == null) {
          throw new ProcessorException(((TypeElement) optionalDebugElement.get()).getQualifiedName()
                                                                                 .toString() + " -> @Debug can only be used with an interfaces annotated with @EventBus");
        }
      }
      // @Filters annotation with no @EventBus annotation ... bad idea!
      Optional<? extends Element> optionalFiltersElement = this.roundEnvironment.getElementsAnnotatedWith(Filters.class)
                                                                                .stream()
                                                                                .findFirst();
      if (optionalFiltersElement.isPresent()) {
        EventBus eventBusAnnotation = optionalFiltersElement.get()
                                                            .getAnnotation(EventBus.class);
        if (eventBusAnnotation == null) {
          throw new ProcessorException(((TypeElement) optionalFiltersElement.get()).getQualifiedName()
                                                                                   .toString() + " -> @Filters can only be used with an interfaces annotated with @EventBus");
        }
      }
    }
    return model;
  }

  private EventBusMetaModel restore() {
    Properties props = new Properties();
    try {
      FileObject resource = this.processingEnvironment.getFiler()
                                                      .getResource(StandardLocation.CLASS_OUTPUT,
                                                                   "",
                                                                   this.createRelativeFileName());
      props.load(resource.openInputStream());
      EventBusMetaModel model = new EventBusMetaModel(props);
      List<EventMetaModel> eventModels = new ArrayList<>();
      for (String eventInternalName : model.getEvents()) {
        FileObject resourceEvent = this.processingEnvironment.getFiler()
                                                             .getResource(StandardLocation.CLASS_OUTPUT,
                                                                          "",
                                                                          this.createRelativeEventModelFileName(eventInternalName));
        props.load(resourceEvent.openInputStream());
        eventModels.add(new EventMetaModel(props));
      }
      eventModels.forEach(model::add);
      return model;
    } catch (IOException e) {
      // every thing is ok -> no operation
//      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
    }
    return null;
  }

  private TypeElement getShellTypeElement(EventBus eventBusAnnotation) {
    try {
      eventBusAnnotation.shell();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private String createRelativeFileName() {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + EventBusAnnotationScanner.EVENTBUS_PROPERTIES;
  }

  private String createRelativeEventModelFileName(String eventInternalName) {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + eventInternalName + ProcessorConstants.PROPERTIES_POSTFIX;
  }

  public static class Builder {

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

    public EventBusAnnotationScanner build() {
      return new EventBusAnnotationScanner(this);
    }
  }
}
