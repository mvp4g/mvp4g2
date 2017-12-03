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
package de.gishmo.gwt.mvp4g2.processor.handler.eventbus.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventFilter;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Filters;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.List;
import java.util.Set;

// TODO check, that @Filter is annoted at a interface that extends IsEventBus! and other tests ...
public class FilterAnnotationGenerator {

  private ProcessorUtils processorUtils;
  private EventBusUtils  eventBusUtils;

  private RoundEnvironment      roundEnvironment;
  private ProcessingEnvironment processingEnvironment;
  private TypeSpec.Builder      typeSpec;
  private TypeElement           eventBusTypeElement;

  @SuppressWarnings("unused")
  private FilterAnnotationGenerator() {
  }

  private FilterAnnotationGenerator(Builder builder) {
    this.roundEnvironment = builder.roundEnvironment;
    this.processingEnvironment = builder.processingEnvironment;
    this.typeSpec = builder.typeSpec;
    this.eventBusTypeElement = builder.eventBusTypeElement;

    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
    this.eventBusUtils = EventBusUtils.builder()
                                      .processingEnvironment(this.processingEnvironment)
                                      .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate()
    throws ProcessorException, IOException {
    this.validate();
    this.generateLoadFilterConfigurationMethod();
  }

  private void validate()
    throws ProcessorException {
    // get elements annotated with EventBus annotation
    Set<? extends Element> elementsWithFilterAnnotation = this.roundEnvironment.getElementsAnnotatedWith(Filters.class);
    // at least there should only one Application annotation!
    if (elementsWithFilterAnnotation.size() > 1) {
      throw new ProcessorException("There should be at least only one interface, that is annotated with @Filter");
    }
    // annotated element has to be a interface
    for (Element element : elementsWithFilterAnnotation) {
      if (element instanceof TypeElement) {
        TypeElement typeElement = (TypeElement) element;
        if (!typeElement.getKind()
                        .isInterface()) {
          throw new ProcessorException("@Filter can only be used with an interface");
        }
        // @Filter can only be used on a interface that extends IsEventBus
        if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                         typeElement.asType(),
                                                         this.processingEnvironment.getElementUtils()
                                                                                   .getTypeElement(IsEventBus.class.getCanonicalName())
                                                                                   .asType())) {
          throw new ProcessorException("@Filter can only be used on interfaces that extends IsEventBus");
        }
        // test, that all filters implement IsEventFilter!
        List<String> eventFilterAsStringList = this.eventBusUtils.getEventFiltersAsList(this.eventBusTypeElement);
        for (String eventFilterClassname : eventFilterAsStringList) {
          TypeElement filterElement = this.processingEnvironment.getElementUtils()
                                                                .getTypeElement(eventFilterClassname);
          if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                           filterElement.asType(),
                                                           this.processingEnvironment.getElementUtils()
                                                                                     .getTypeElement(IsEventFilter.class.getCanonicalName())
                                                                                     .asType())) {
            throw new ProcessorException("@Filter - the filters attribute needs class that implements IsEventFilter");
          }
        }
      } else {
        throw new ProcessorException("@Filter can only be used on a type (interface)");
      }
    }
  }

  private void generateLoadFilterConfigurationMethod() {
    // method msut always be created!
    MethodSpec.Builder loadFilterConfigurationMethod = MethodSpec.methodBuilder("loadFilterConfiguration")
                                                                 .addAnnotation(Override.class)
                                                                 .addModifiers(Modifier.PUBLIC);

    // get elements annotated with EventBus annotation
    Set<? extends Element> elementsWithFiltersAnnotation = this.roundEnvironment.getElementsAnnotatedWith(Filters.class);
    if (elementsWithFiltersAnnotation.size() == 0) {
      loadFilterConfigurationMethod.addStatement("super.setFiltersEnable(false)");
    } else {
      List<String> eventFilterAsStringList = this.eventBusUtils.getEventFiltersAsList(this.eventBusTypeElement);
      if (eventFilterAsStringList != null) {
        if (eventFilterAsStringList.size() > 0) {
          loadFilterConfigurationMethod.addStatement("super.setFiltersEnable(true)");
          for (String eventFilter : eventFilterAsStringList) {
            loadFilterConfigurationMethod.addStatement("super.eventFilters.add(new $T())",
                                                       ClassName.get(this.processingEnvironment.getElementUtils()
                                                                                               .getTypeElement(eventFilter)
                                                                                               .asType()));
          }
        } else {
          loadFilterConfigurationMethod.addStatement("super.setFiltersEnable(false)");
        }
      }
    }
    typeSpec.addMethod(loadFilterConfigurationMethod.build());
  }

  public static final class Builder {

    RoundEnvironment      roundEnvironment;
    ProcessingEnvironment processingEnvironment;
    TypeSpec.Builder      typeSpec;
    TypeElement           eventBusTypeElement;

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

    /**
     * Set the eventbus generator element
     *
     * @param eventBusTypeElement the eventbvus generator element
     * @return the Builder
     */
    public Builder eventBusTypeElement(TypeElement eventBusTypeElement) {
      this.eventBusTypeElement = eventBusTypeElement;
      return this;
    }

    /**
     * Set the typeSpec of the currently generated eventBus
     *
     * @param typeSpec ttype spec of the crruent eventbus
     * @return the Builder
     */
    public Builder typeSpec(TypeSpec.Builder typeSpec) {
      this.typeSpec = typeSpec;
      return this;
    }

    public FilterAnnotationGenerator build() {
      return new FilterAnnotationGenerator(this);
    }
  }
}
