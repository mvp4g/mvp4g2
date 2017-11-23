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
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Start;
import de.gishmo.gwt.mvp4g2.client.eventbus.internal.EventMetaData;
import de.gishmo.gwt.mvp4g2.client.ui.internal.EventHandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.ui.internal.PresenterHandlerMetaData;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import java.util.List;
import java.util.stream.IntStream;

public class EventHandlingMethodGenerator {

  private final static String EXECUTION_METHOD_PREFIX = "exec";

  private ProcessorUtils processorUtils;
  private EventBusUtils  eventBusUtils;

  private RoundEnvironment      roundEnvironment;
  private ProcessingEnvironment processingEnvironment;
  private TypeElement           eventBusTypeElement;
  private TypeSpec.Builder      typeSpec;

  @SuppressWarnings("unused")
  private EventHandlingMethodGenerator() {
  }

  private EventHandlingMethodGenerator(Builder builder) {
    this.roundEnvironment = builder.roundEnvironment;
    this.processingEnvironment = builder.processingEnvironment;
    this.eventBusTypeElement = builder.eventBusTypeElement;
    this.typeSpec = builder.typeSpec;

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
    throws ProcessorException {
    // generate the event meta data
    List<Element> events = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                     this.eventBusTypeElement,
                                                                                     Event.class);
    for (Element element : events) {
      this.validateEvent(element);
    }
    // generate Code
    events.stream()
          .map(eventElement -> (ExecutableElement) eventElement)
          .forEach(executableElement -> {
            this.generateEventHandlingMethod(executableElement);
            this.generateEventHandlingMethodForExecution(executableElement);
          });
    // generate the code for the Start event handling
    this.generateStartEventHandlingMethods();
  }

  // TODO Validation
  private void validateEvent(Element element)
    throws ProcessorException {
    //    try {
    //      ExecutableElement executableElement = (ExecutableElement) element;
    //    } catch (Exception e) {
    //      throw new ProcessorException("@Event can only be used with a method");
    //    }
  }

  private void generateEventHandlingMethod(ExecutableElement executableElement) {
    StringBuilder sb = new StringBuilder();
    MethodSpec.Builder eventHandlingMethod = MethodSpec.methodBuilder(executableElement.getSimpleName()
                                                                                       .toString())
                                                       .addAnnotation(Override.class)
                                                       .addModifiers(Modifier.PUBLIC);
    // add parametners to method signature ...
    // generate method body
    executableElement.getParameters()
                     .stream()
                     .map(variableElement -> ParameterSpec.builder(ClassName.get(variableElement.asType()),
                                                                   variableElement.getSimpleName()
                                                                                  .toString())
                                                          .build())
                     .forEach(eventHandlingMethod::addParameter);
    // TODO implement here navagation envet handling!
    sb.append(EventHandlingMethodGenerator.EXECUTION_METHOD_PREFIX)
      .append(executableElement.getSimpleName()
                               .toString()
                               .substring(0,
                                          1)
                               .toUpperCase())
      .append(executableElement.getSimpleName()
                               .toString()
                               .substring(1));
    eventHandlingMethod.addCode(sb.toString() + "(");
    IntStream.range(0,
                    executableElement.getParameters()
                                     .size())
             .forEachOrdered(i -> {
               eventHandlingMethod.addCode(executableElement.getParameters()
                                                            .get(i)
                                                            .getSimpleName()
                                                            .toString());
               if (i != executableElement.getParameters()
                                         .size() - 1) {
                 eventHandlingMethod.addCode(", ");
               }
             });
    eventHandlingMethod.addCode(");\n");
    typeSpec.addMethod(eventHandlingMethod.build());
  }

  private void generateEventHandlingMethodForExecution(ExecutableElement executableElement) {
    String sb = EventHandlingMethodGenerator.EXECUTION_METHOD_PREFIX +
                executableElement.getSimpleName()
                                 .toString()
                                 .substring(0,
                                            1)
                                 .toUpperCase() +
                executableElement.getSimpleName()
                                 .toString()
                                 .substring(1);
    MethodSpec.Builder eventHandlingMethod = MethodSpec.methodBuilder(sb)
                                                       .addModifiers(Modifier.PUBLIC);
    // add parametners to method signature ...
    // generate method body
    executableElement.getParameters()
                     .stream()
                     .map(variableElement -> ParameterSpec.builder(ClassName.get(variableElement.asType()),
                                                                   variableElement.getSimpleName()
                                                                                  .toString())
                                                          .build())
                     .forEach(eventHandlingMethod::addParameter);
    // get event meta data from store ...
    eventHandlingMethod.addStatement("$T eventMetaData = super.getEventMetaData($S)",
                                     ClassName.get(EventMetaData.class),
                                     executableElement.getSimpleName()
                                                      .toString());
    /* bind views */
    eventHandlingMethod.addStatement("super.createAndBindView(eventMetaData)");
    /* Handle Binding */
    eventHandlingMethod.addCode("super.bind(eventMetaData");
    executableElement.getParameters()
                     .forEach(variableElement -> eventHandlingMethod.addCode(", $N",
                                                                             variableElement.getSimpleName()
                                                                                            .toString()));
    eventHandlingMethod.addCode(");\n");
    // fire events
    List<String> eventHandlerClasses = this.eventBusUtils.getHandlerElementsAsList(executableElement,
                                                                                   "handlers");

    eventHandlingMethod.addCode("super.logEvent($S",
                                executableElement.getSimpleName()
                                                 .toString());
    for (VariableElement variableElement : executableElement.getParameters()) {
      eventHandlingMethod.addCode(", $N",
                                  variableElement.getSimpleName()
                                                 .toString());
    }
    eventHandlingMethod.addCode(");\n");

    eventHandlingMethod.addStatement("$T<$T<?>> eventHandlers = null",
                                     ClassName.get(List.class),
                                     ClassName.get(EventHandlerMetaData.class));
    eventHandlingMethod.addStatement("$T<$T<?, ?>> presenters = null",
                                     ClassName.get(List.class),
                                     ClassName.get(PresenterHandlerMetaData.class));
    // start presenter code
    eventHandlerClasses.forEach(eventHandlerClass -> {
      eventHandlingMethod.addComment("handling: " + eventHandlerClass);
      this.createEventHandlingMethod(eventHandlingMethod,
                                     executableElement,
                                     eventHandlerClass,
                                     "eventHandlerMetaDataMap",
                                     false);
      this.createEventHandlingMethod(eventHandlingMethod,
                                     executableElement,
                                     eventHandlerClass,
                                     "presenterHandlerMetaDataMap",
                                     true);
    });
    typeSpec.addMethod(eventHandlingMethod.build());
  }

  private void generateStartEventHandlingMethods() {
    MethodSpec.Builder startEventHandlingMethod = MethodSpec.methodBuilder("fireStartEvent")
                                                            .addAnnotation(Override.class)
                                                            .addModifiers(Modifier.PUBLIC,
                                                                          Modifier.FINAL);
    // get all elements annotated with Start
    List<Element> startEvents = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                          eventBusTypeElement,
                                                                                          Start.class);
    // get event meta data from store ...
    startEventHandlingMethod.addStatement("this.$L()",
                                          startEvents.get(0)
                                                     .getSimpleName()
                                                     .toString());
    typeSpec.addMethod(startEventHandlingMethod.build());
  }

  private void createEventHandlingMethod(MethodSpec.Builder method,
                                         ExecutableElement executableElement,
                                         String eventHandlerClass,
                                         String handlerMetaDataMapName,
                                         boolean isPresenter) {
    String getHandlerMethodName = isPresenter ? "getPresenter" : "getEventHandler";
    String variableName = isPresenter ? "presenters" : "eventHandlers";

    Class<?> metaDataClass = isPresenter ? PresenterHandlerMetaData.class : EventHandlerMetaData.class;

    method.addStatement("$N = this.$N.get($S)",
                        variableName,
                        handlerMetaDataMapName,
                        eventHandlerClass);
    method.beginControlFlow("if ($N != null && $N.size() != 0)",
                            variableName,
                            variableName);
    method.beginControlFlow("for ($T<$N> metaData : $N)",
                            ClassName.get(metaDataClass),
                            isPresenter ? "?, ?" : "?",
                            variableName);
    method.addStatement("boolean activated = metaData.$N().isActivated()",
                        getHandlerMethodName);
    method.addCode("activated = activated && metaData.$N().pass(eventMetaData.getEventName()",
                   getHandlerMethodName);
    executableElement.getParameters()
                     .forEach(variableElement -> method.addCode(", $N",
                                                                variableElement.getSimpleName()
                                                                               .toString()));
    method.addCode(");\n");
    method.beginControlFlow("if (activated)");
    //      method.beginControlFlow("if (presenterHandlerMetaData.getPresenter().isBinded())");
    // event handling
    method.addStatement("super.logHandler($S, $S)",
                        executableElement.getSimpleName()
                                         .toString(),
                        eventHandlerClass);
    method.addStatement("metaData.$N().onBeforeEvent($S)",
                        getHandlerMethodName,
                        executableElement.getSimpleName()
                                         .toString());
    method.addCode("(($T) metaData.$N()).on$L(",
                   ClassName.get(eventHandlerClass.substring(0,
                                                             eventHandlerClass.lastIndexOf(".")),
                                 eventHandlerClass.substring(eventHandlerClass.lastIndexOf(".") + 1)),
                   getHandlerMethodName,
                   executableElement.getSimpleName()
                                    .toString()
                                    .substring(0,
                                               1)
                                    .toUpperCase() + executableElement.getSimpleName()
                                                                      .toString()
                                                                      .substring(1));
    boolean firstElement = true;
    for (VariableElement variableElement : executableElement.getParameters()) {
      if (firstElement) {
        firstElement = false;
      } else {
        method.addCode(", ");
      }
      method.addCode("$N",
                     variableElement.getSimpleName()
                                    .toString());
    }
    method.addCode(");\n");
    //      method.endControlFlow();
    method.endControlFlow();
    method.endControlFlow();
    method.endControlFlow();
  }

  public static final class Builder {

    RoundEnvironment      roundEnvironment;
    ProcessingEnvironment processingEnvironment;
    TypeElement           eventBusTypeElement;
    TypeSpec.Builder      typeSpec;

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

    public EventHandlingMethodGenerator build() {
      return new EventHandlingMethodGenerator(this);
    }
  }
}
