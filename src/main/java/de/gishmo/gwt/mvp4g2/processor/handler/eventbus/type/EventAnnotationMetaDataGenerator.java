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
package de.gishmo.gwt.mvp4g2.processor.handler.eventbus.type;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.client.eventbus.internal.EventMetaData;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.List;

/**
 * <p>The execution context manages all commands.<br>
 * Use run()-method to start execution.</p>
 */
public class EventAnnotationMetaDataGenerator {

  private ProcessorUtils processorUtils;
  private EventBusUtils  eventBusUtils;

  private ProcessingEnvironment processingEnvironment;
  private TypeSpec.Builder      typeSpec;
  private TypeElement           eventBusTypeElement;

  @SuppressWarnings("unused")
  private EventAnnotationMetaDataGenerator() {
  }

  private EventAnnotationMetaDataGenerator(Builder builder) {
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
    // generate the event meta data
    List<Element> events = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                     this.eventBusTypeElement,
                                                                                     Event.class);
    for (Element event : events) {
      this.validateEvent(event);
      this.generateEventMetaData(event);
    }
    // load meta data
    this.generateLoadEventMetaDataMethod();
  }

  private void validateEvent(Element eventElement)
    throws ProcessorException {
    //    try {
    //      ExecutableElement executableElement = (ExecutableElement) element;
    //    } catch (Exception e) {
    //      throw new ProcessorException("@Event can only be used with a method");
    //    }
  }

  private void generateEventMetaData(Element eventElement)
    throws IOException {
    ExecutableElement executableElement = (ExecutableElement) eventElement;

    // List of event handlers (full class names as String)
    List<String> handlersFromAnnotation = this.eventBusUtils.getHandlerElementsAsList(executableElement,
                                                                                      "handlers");
    // List of binding handlers (full class names as String)
    List<String> bindHandlersFromAnnotation = this.eventBusUtils.getHandlerElementsAsList(executableElement,
                                                                                          "bind");

    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.eventBusUtils.createEventMetaDataClassName(this.eventBusTypeElement,
                                                                                                      executableElement))
                                        .superclass(ClassName.get(EventMetaData.class))
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL);

    // constructor ...
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                               .addModifiers(Modifier.PUBLIC)
                                               .addStatement("super($S, $L, $L)",
                                                             executableElement.getSimpleName()
                                                                              .toString(),
                                                             executableElement.getAnnotation(Event.class)
                                                                              .passive(),
                                                             executableElement.getAnnotation(Event.class)
                                                                              .navigationEvent());
    executableElement.getParameters()
                     .stream()
                     .map(variableElement -> constructor.addStatement("super.addParameterType($S, $S)",
                                                                      variableElement.getSimpleName()
                                                                                     .toString(),
                                                                      variableElement.asType()
                                                                                     .toString()));
    if (handlersFromAnnotation != null) {
      handlersFromAnnotation.forEach((s) -> constructor.addStatement("super.addHandler($S)",
                                                                     s));
    }
    if (bindHandlersFromAnnotation != null) {
      bindHandlersFromAnnotation.forEach((s) -> constructor.addStatement("super.addBindHandler($S)",
                                                                         s));
    }
    typeSpec.addMethod(constructor.build());

    JavaFile javaFile = JavaFile.builder(ProcessorUtils.getPackageAsString(eventElement),
                                         typeSpec.build())
                                .build();
    javaFile.writeTo(this.processingEnvironment.getFiler());
    System.out.println(javaFile.toString());
  }

  private void generateLoadEventMetaDataMethod() {
    MethodSpec.Builder loadEventMethod = MethodSpec.methodBuilder("loadEventMetaData")
                                                   .addAnnotation(Override.class)
                                                   .addModifiers(Modifier.PROTECTED);
    // get all elements annotated with Event
    List<Element> events = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                     eventBusTypeElement,
                                                                                     Event.class);
    // List of already created EventHandler used to avoid a second create ...
    // cast to ExecutableElement because @Event can only be used with methods ...
    events.stream()
          .map(eventElement -> (ExecutableElement) eventElement)
          .forEach(executableElement -> {
            String metaDataClassName = this.eventBusUtils.createEventMetaDataClassName(eventBusTypeElement,
                                                                                       executableElement);
            loadEventMethod.addComment("");
            loadEventMethod.addComment("----------------------------------------------------------------------");
            loadEventMethod.addComment("");
            loadEventMethod.addComment("handle $N",
                                       metaDataClassName);
            loadEventMethod.addComment("");
            loadEventMethod.addStatement("super.putEventMetaData($S, new $N())",
                                         executableElement.getSimpleName()
                                                          .toString(),
                                         metaDataClassName);
          });
    typeSpec.addMethod(loadEventMethod.build());
  }

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;
    TypeSpec.Builder      typeSpec;
    TypeElement           eventBusTypeElement;

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
     * Set the eventbus type element
     *
     * @param eventBusTypeElement the eventbvus type element
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

    public EventAnnotationMetaDataGenerator build() {
      return new EventAnnotationMetaDataGenerator(this);
    }
  }
}
