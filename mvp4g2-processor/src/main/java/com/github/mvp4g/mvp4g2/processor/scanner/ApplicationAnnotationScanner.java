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
import java.util.Optional;
import java.util.Properties;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.github.mvp4g.mvp4g2.core.application.annotation.Application;
import com.github.mvp4g.mvp4g2.processor.ProcessorConstants;
import com.github.mvp4g.mvp4g2.processor.ProcessorException;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.ApplicationMetaModel;
import com.github.mvp4g.mvp4g2.processor.scanner.validation.ApplicationAnnotationValidator;

import static java.util.Objects.isNull;

public class ApplicationAnnotationScanner {

  private final static String APPLICATION_PROPERTIES = "application.properties";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;

  @SuppressWarnings("unused")
  private ApplicationAnnotationScanner(Builder builder) {
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

  public ApplicationMetaModel scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // First we try to read an already created resource ...
    ApplicationMetaModel model = this.restore();
    // Check if we have an element annotated with @Application
    if (!roundEnvironment.getElementsAnnotatedWith(Application.class)
                         .isEmpty()) {
      // check, whether we have o do something ...
      ApplicationAnnotationValidator validator = ApplicationAnnotationValidator.builder()
                                                                               .roundEnvironment(roundEnvironment)
                                                                               .processingEnvironment(this.processingEnvironment)
                                                                               .build();
      validator.validate();
      // should only be one, so we can search for the first! ...
      Optional<? extends Element> optionalElement = this.roundEnvironment.getElementsAnnotatedWith(Application.class)
                                                                         .stream()
                                                                         .findFirst();
      if (optionalElement.isPresent()) {
        Element applicationAnnotationElement = optionalElement.get();
        validator.validate(applicationAnnotationElement);
        Application applicationAnnotation = applicationAnnotationElement.getAnnotation(Application.class);
        if (!isNull(applicationAnnotation)) {
          TypeElement eventBusTypeElement = this.getEventBusTypeElement(applicationAnnotation);
          TypeElement applicationLoaderTypeElement = this.getApplicationLoaderTypeElement(applicationAnnotation);
          model = new ApplicationMetaModel(applicationAnnotationElement.toString(),
                                           isNull(eventBusTypeElement) ? "" : eventBusTypeElement.toString(),
                                           isNull(applicationLoaderTypeElement) ? "" : applicationLoaderTypeElement.toString(),
                                           String.valueOf(applicationAnnotation.historyOnStart()));
          // let's store the updated model
          this.processorUtils.store(model,
                                    this.createRelativeFileName());
        }
      }
    }
    return model;
  }

  private ApplicationMetaModel restore() {
    Properties props = new Properties();
    try {
      FileObject resource = this.processingEnvironment.getFiler()
                                                      .getResource(StandardLocation.CLASS_OUTPUT,
                                                                   "",
                                                                   this.createRelativeFileName());
      props.load(resource.openInputStream());
      return new ApplicationMetaModel(props);
    } catch (IOException e) {
      // every thing is ok -> no operation
//      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
    }
    return null;
  }

  private TypeElement getEventBusTypeElement(Application applicationAnnotation) {
    try {
      applicationAnnotation.eventBus();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private TypeElement getApplicationLoaderTypeElement(Application applicationAnnotation) {
    try {
      applicationAnnotation.loader();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private String createRelativeFileName() {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + ApplicationAnnotationScanner.APPLICATION_PROPERTIES;
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

    public ApplicationAnnotationScanner build() {
      return new ApplicationAnnotationScanner(this);
    }
  }
}
