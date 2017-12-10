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
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import de.gishmo.gwt.mvp4g2.client.eventbus.AbstractEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Start;
import de.gishmo.gwt.mvp4g2.client.internal.eventbus.EventMetaData;
import de.gishmo.gwt.mvp4g2.client.history.NavigationEventCommand;
import de.gishmo.gwt.mvp4g2.client.history.annotation.History;
import de.gishmo.gwt.mvp4g2.client.history.annotation.InitHistory;
import de.gishmo.gwt.mvp4g2.client.history.annotation.NotFoundHistory;
import de.gishmo.gwt.mvp4g2.client.internal.ui.EventHandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.internal.ui.PresenterHandlerMetaData;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

// TODO validierung init event
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
            TypeElement historyConverterTypeElement = this.getHistoryConverterTypeElement(executableElement.getAnnotation(Event.class));
            this.generateEventHandlingMethod(historyConverterTypeElement,
                                             executableElement);
            this.generateEventHandlingMethodForExecution(historyConverterTypeElement,
                                                         executableElement);
          });
    // generate the code for the Start event handling
    this.generateStartEventHandlingMethod();
    // generate InitHistory event Method ...
    this.generateInitHistoryEventHandlingMethod();
    // generate NotFoundHistory-event
    this.generateNotFoundHistoryEventHandlingMethod();
  }

  // TODO Validation
  private void validateEvent(Element element)
    throws ProcessorException {
    // check, that no handler is also listed as binding
    ExecutableElement executableElement = (ExecutableElement) element;
    List<String> bindHandlerClasses = this.eventBusUtils.getHandlerElementsAsList(executableElement,
                                                                                  "bind");
    List<String> handlerClasses = this.eventBusUtils.getHandlerElementsAsList(executableElement,
                                                                              "handlers");
    if (bindHandlerClasses != null && handlerClasses != null) {
      for (String bindHandlerClass : bindHandlerClasses) {
        for (String handlerClass : handlerClasses) {
          if (bindHandlerClass.equals(handlerClass)) {
            throw new ProcessorException("Event: >>" + executableElement.getSimpleName() + "<< - handler: >>" + handlerClass + " can not be set in bind- and handlers-attribute");
          }
        }
      }
    }
    // check, that passive events have not binding
    if (executableElement.getAnnotation(Event.class)
                         .passive()) {
      if (bindHandlerClasses.size() > 0) {
        throw new ProcessorException("Event: >>" + executableElement.getSimpleName() + "<< - a passive event can not have a bind-attribute");
      }
    }
  }

  private TypeElement getHistoryConverterTypeElement(Event eventAnnotation) {
    try {
      eventAnnotation.historyConverter();
    } catch (MirroredTypeException exception) {
      TypeElement historyConverterTypeElement = (TypeElement) this.processingEnvironment.getTypeUtils()
                                                                                        .asElement(exception.getTypeMirror());
      if (Event.NoHistoryConverter.class.getCanonicalName()
                                        .equals(historyConverterTypeElement.getQualifiedName()
                                                                           .toString())) {
        return null;
      }

      return historyConverterTypeElement;
    }
    return null;
  }

  private void generateEventHandlingMethod(TypeElement historyConverterTypeElement,
                                           ExecutableElement executableElement) {
    MethodSpec.Builder eventHandlingMethod = MethodSpec.methodBuilder(executableElement.getSimpleName()
                                                                                       .toString())
                                                       .addAnnotation(Override.class)
                                                       .addModifiers(Modifier.PUBLIC,
                                                                     Modifier.FINAL);
    eventHandlingMethod.addStatement("int startLogDepth = $T.logDepth",
                                     ClassName.get(AbstractEventBus.class));
    eventHandlingMethod.beginControlFlow("try");
    // add parametners to method signature ...
    // generate method body
    executableElement.getParameters()
                     .stream()
                     .map(variableElement -> ParameterSpec.builder(ClassName.get(variableElement.asType()),
                                                                   variableElement.getSimpleName()
                                                                                  .toString())
                                                          .addModifiers(Modifier.FINAL)
                                                          .build())
                     .forEach(eventHandlingMethod::addParameter);
    if (executableElement.getAnnotation(Event.class)
                         .navigationEvent()) {
      eventHandlingMethod.addCode("super.logAskingForConfirmation(++$T.logDepth, $S",
                                  ClassName.get(AbstractEventBus.class),
                                  executableElement.getSimpleName()
                                                   .toString());
      for (VariableElement variableElement : executableElement.getParameters()) {
        eventHandlingMethod.addCode(", $N",
                                    variableElement.getSimpleName()
                                                   .toString());
      }
      eventHandlingMethod.addCode(");\n");
      MethodSpec.Builder executeMethod = MethodSpec.methodBuilder("execute")
                                                   .addAnnotation(Override.class)
                                                   .addModifiers(Modifier.PUBLIC);
      this.callEventExecMethod(executeMethod,
                               executableElement);
      TypeSpec confirmCommand = TypeSpec.anonymousClassBuilder(CodeBlock.builder()
                                                                        .add("this")
                                                                        .build())
                                        .addSuperinterface(NavigationEventCommand.class)
                                        .addMethod(executeMethod.build())
                                        .build();
      eventHandlingMethod.addStatement("super.placeService.confirmEvent($L)",
                                       confirmCommand);
    } else {
      this.callEventExecMethod(eventHandlingMethod,
                               executableElement);
    }
    eventHandlingMethod.nextControlFlow("finally");
    eventHandlingMethod.addStatement("$T.logDepth = startLogDepth",
                                     ClassName.get(AbstractEventBus.class));
    eventHandlingMethod.endControlFlow();
    typeSpec.addMethod(eventHandlingMethod.build());
  }

  private void generateEventHandlingMethodForExecution(TypeElement historyConverterTypeElement,
                                                       ExecutableElement executableElement) {
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
                                                       .addModifiers(Modifier.PUBLIC,
                                                                     Modifier.FINAL);
    // add parametners to method signature ...
    // generate method body
    executableElement.getParameters()
                     .stream()
                     .map(variableElement -> ParameterSpec.builder(ClassName.get(variableElement.asType()),
                                                                   variableElement.getSimpleName()
                                                                                  .toString())
                                                          .addModifiers(Modifier.FINAL)
                                                          .build())
                     .forEach(eventHandlingMethod::addParameter);
    // log event
    eventHandlingMethod.addCode("super.logEvent(++$T.logDepth, $S",
                                ClassName.get(AbstractEventBus.class),
                                executableElement.getSimpleName()
                                                 .toString());
    for (VariableElement variableElement : executableElement.getParameters()) {
      eventHandlingMethod.addCode(", $N",
                                  variableElement.getSimpleName()
                                                 .toString());
    }
    eventHandlingMethod.addCode(");\n");
    eventHandlingMethod.addStatement("++$T.logDepth",
                                     ClassName.get(AbstractEventBus.class));
    // generate code for filter event!
    if (executableElement.getParameters()
                         .size() > 0) {
      StringBuilder parameters = new StringBuilder();
      IntStream.range(0,
                      executableElement.getParameters()
                                       .size())
               .forEachOrdered(i -> {
                 parameters.append(executableElement.getParameters()
                                                    .get(i)
                                                    .getSimpleName()
                                                    .toString());
                 if (i != executableElement.getParameters()
                                           .size() - 1) {
                   parameters.append(", ");
                 }
               });
      eventHandlingMethod.beginControlFlow("if (!super.filterEvent($S, $L))",
                                           executableElement.getSimpleName()
                                                            .toString(),
                                           parameters);
    } else {
      eventHandlingMethod.beginControlFlow("if (!super.filterEvent($S))",
                                           executableElement.getSimpleName()
                                                            .toString());
    }
    eventHandlingMethod.addStatement("super.logEventFilter($T.logDepth, $S)",
                                     ClassName.get(AbstractEventBus.class),
                                     executableElement.getSimpleName()
                                                      .toString());
    eventHandlingMethod.addStatement("return");
    eventHandlingMethod.endControlFlow();
    // get event meta data from store ...
    eventHandlingMethod.addStatement("$T<$T> eventMetaData = super.getEventMetaData($S)",
                                     ClassName.get(EventMetaData.class),
                                     ClassName.get(eventBusTypeElement),
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
    /* activaiting handlers */
    eventHandlingMethod.addStatement("super.activate(eventMetaData)");
    /* deactivaiting handlers */
    eventHandlingMethod.addStatement("super.deactivate(eventMetaData)");
    // fire events
    List<String> eventHandlerClasses = this.eventBusUtils.getHandlerElementsAsList(executableElement,
                                                                                   "handlers");
    if (eventHandlerClasses != null) {
      eventHandlingMethod.addStatement("$T<$T<?>> eventHandlers = null",
                                       ClassName.get(List.class),
                                       ClassName.get(EventHandlerMetaData.class));
      eventHandlingMethod.addStatement("$T<$T<?, ?>> presenters = null",
                                       ClassName.get(List.class),
                                       ClassName.get(PresenterHandlerMetaData.class));
      if (historyConverterTypeElement != null) {
        eventHandlingMethod.addStatement("$T<$T> listOfExecutedHandlers = new $T<>()",
                                         ClassName.get(List.class),
                                         ClassName.get(String.class),
                                         ClassName.get(ArrayList.class));
      }
      // start presenter code
      eventHandlerClasses.forEach(eventHandlerClass -> {
        eventHandlingMethod.addComment("handling: " + eventHandlerClass);
        this.createEventHandlingMethod(eventHandlingMethod,
                                       executableElement,
                                       eventHandlerClass,
                                       "eventHandlerMetaDataMap",
                                       false,
                                       historyConverterTypeElement);
        this.createEventHandlingMethod(eventHandlingMethod,
                                       executableElement,
                                       eventHandlerClass,
                                       "presenterHandlerMetaDataMap",
                                       true,
                                       historyConverterTypeElement);
      });
      // Manage broadcasting
      // TODO asdasdsad


      this.createPlaceServicePlaceCall(eventHandlingMethod,
                                       executableElement,
                                       historyConverterTypeElement);
    }
    typeSpec.addMethod(eventHandlingMethod.build());
  }

  private void generateStartEventHandlingMethod() {
    MethodSpec.Builder startEventHandlingMethod = MethodSpec.methodBuilder("fireStartEvent")
                                                            .addAnnotation(Override.class)
                                                            .addModifiers(Modifier.PUBLIC,
                                                                          Modifier.FINAL);
    // get all elements annotated with Start
    List<Element> startEvents = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                          eventBusTypeElement,
                                                                                          Start.class);
    // @Start annotation is optional ...
    if (startEvents.size() > 0) {
      // get event meta data from store ...
      startEventHandlingMethod.addStatement("this.$L()",
                                            startEvents.get(0)
                                                       .getSimpleName()
                                                       .toString());
    }
    typeSpec.addMethod(startEventHandlingMethod.build());
  }

  private void generateInitHistoryEventHandlingMethod() {
    // get all elements annotated with Start
    List<Element> initHistoryEvents = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                                eventBusTypeElement,
                                                                                                InitHistory.class);
    MethodSpec.Builder initHistoryEventHandlingMethod = MethodSpec.methodBuilder("fireInitHistoryEvent")
                                                                  .addAnnotation(Override.class)
                                                                  .addModifiers(Modifier.PUBLIC,
                                                                                Modifier.FINAL);
    if (initHistoryEvents.size() > 0) {
      // get event meta data from store ...
      initHistoryEventHandlingMethod.addStatement("this.$L()",
                                                  initHistoryEvents.get(0)
                                                                   .getSimpleName()
                                                                   .toString());
    } else {
      initHistoryEventHandlingMethod.addStatement("assert false : $S",
                                                  "no @InitHistory-event defined");
    }
    typeSpec.addMethod(initHistoryEventHandlingMethod.build());
  }

  private void generateNotFoundHistoryEventHandlingMethod() {
    // get all elements annotated with Start
    List<Element> notFoundHistoryEvents = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                                    eventBusTypeElement,
                                                                                                    NotFoundHistory.class);
    MethodSpec.Builder initHistoryEventHandlingMethod = MethodSpec.methodBuilder("fireNotFoundHistoryEvent")
                                                                  .addAnnotation(Override.class)
                                                                  .addModifiers(Modifier.PUBLIC,
                                                                                Modifier.FINAL);
    if (notFoundHistoryEvents.size() > 0) {
      // get event meta data from store ...
      initHistoryEventHandlingMethod.addStatement("this.$L()",
                                                  notFoundHistoryEvents.get(0)
                                                                       .getSimpleName()
                                                                       .toString());
    } else {
      initHistoryEventHandlingMethod.addStatement("assert false : $S",
                                                  "no @NotFoundHistory-event defined");
    }
    typeSpec.addMethod(initHistoryEventHandlingMethod.build());
  }

  private void callEventExecMethod(MethodSpec.Builder method,
                                   ExecutableElement executableElement) {
    String sb = EventHandlingMethodGenerator.EXECUTION_METHOD_PREFIX +
                executableElement.getSimpleName()
                                 .toString()
                                 .substring(0,
                                            1)
                                 .toUpperCase() +
                executableElement.getSimpleName()
                                 .toString()
                                 .substring(1);

    method.addCode(sb + "(");
    IntStream.range(0,
                    executableElement.getParameters()
                                     .size())
             .forEachOrdered(i -> {
               method.addCode(executableElement.getParameters()
                                               .get(i)
                                               .getSimpleName()
                                               .toString());
               if (i != executableElement.getParameters()
                                         .size() - 1) {
                 method.addCode(", ");
               }
             });
    method.addCode(");\n");
  }

  private void createEventHandlingMethod(MethodSpec.Builder method,
                                         ExecutableElement executableElement,
                                         String eventHandlerClass,
                                         String handlerMetaDataMapName,
                                         boolean isPresenter,
                                         TypeElement historyConverterTypeElement) {
    String getHandlerMethodName = isPresenter ? "getPresenter" : "getEventHandler";
    String variableName = isPresenter ? "presenters" : "eventHandlers";

    Class<?> metaDataClass = isPresenter ? PresenterHandlerMetaData.class : EventHandlerMetaData.class;

    method.addStatement("$N = this.$N.get($S)",
                        variableName,
                        handlerMetaDataMapName,
                        eventHandlerClass);
    this.generateEventHandlerCall(method,
                                  executableElement,
                                  eventHandlerClass,
                                  getHandlerMethodName,
                                  isPresenter,
                                  historyConverterTypeElement);


//    method.beginControlFlow("if ($N != null && $N.size() != 0)",
//                            variableName,
//                            variableName);
//    method.beginControlFlow("for ($T<$N> metaData : $N)",
//                            ClassName.get(metaDataClass),
//                            isPresenter ? "?, ?" : "?",
//                            variableName);
//    method.addStatement("boolean activated = metaData.$N().isActivated()",
//                        getHandlerMethodName);
//    method.addCode("activated = activated && metaData.$N().pass(eventMetaData.getEventName()",
//                   getHandlerMethodName);
//    executableElement.getParameters()
//                     .forEach(variableElement -> method.addCode(", $N",
//                                                                variableElement.getSimpleName()
//                                                                               .toString()));
//    method.addCode(");\n");
//    method.beginControlFlow("if (activated)");
//    //      method.beginControlFlow("if (presenterHandlerMetaData.getPresenter().isBinded())");
//    // event handling
//    method.addStatement("super.logHandler($T.logDepth, $S, $S)",
//                        ClassName.get(AbstractEventBus.class),
//                        executableElement.getSimpleName()
//                                         .toString(),
//                        eventHandlerClass);
//    method.addStatement("metaData.$N().onBeforeEvent($S)",
//                        getHandlerMethodName,
//                        executableElement.getSimpleName()
//                                         .toString());
//    method.addCode("(($T) metaData.$N()).on$L(",
//                   ClassName.get(eventHandlerClass.substring(0,
//                                                             eventHandlerClass.lastIndexOf(".")),
//                                 eventHandlerClass.substring(eventHandlerClass.lastIndexOf(".") + 1)),
//                   getHandlerMethodName,
//                   executableElement.getSimpleName()
//                                    .toString()
//                                    .substring(0,
//                                               1)
//                                    .toUpperCase() + executableElement.getSimpleName()
//                                                                      .toString()
//                                                                      .substring(1));
//    this.createSignatureForEventCall(method,
//                                     executableElement,
//                                     false);
//    method.addCode(");\n");
//    if (historyConverterTypeElement != null) {
//      method.addStatement("listOfExecutedHandlers.add($S)",
//                          eventHandlerClass);
//    }
//    //      method.endControlFlow();
//    method.endControlFlow();
//    method.endControlFlow();
//    method.endControlFlow();
  }

  private void createPlaceServicePlaceCall(MethodSpec.Builder method,
                                           ExecutableElement executableElement,
                                           TypeElement historyConverterTypeElement) {
    if (historyConverterTypeElement == null) {
      return;
    }
    if (Event.NoHistoryConverter.class.getCanonicalName()
                                      .equals(historyConverterTypeElement.getQualifiedName()
                                                                         .toString())) {
      return;
    }
    History.HistoryConverterType historyConverterType = historyConverterTypeElement.getAnnotation(History.class)
                                                                                   .type();
    method.beginControlFlow("if (listOfExecutedHandlers.size() > 0)");
    switch (historyConverterType) {
      case NONE:
        method.addStatement("super.placeService.place($S, null, false)",
                            executableElement.getSimpleName()
                                             .toString());
        break;
      case SIMPLE:
        method.addCode("super.placeService.place($S, (($L) super.placeService.getHistoryConverter($S)).convertToToken($S",
                       executableElement.getSimpleName()
                                        .toString(),
                       historyConverterTypeElement.getQualifiedName()
                                                  .toString(),
                       executableElement.getSimpleName()
                                        .toString(),
                       executableElement.getSimpleName()
                                        .toString());
        this.createSignatureForEventCall(method,
                                         executableElement,
                                         true);
        method.addCode("), false);\n");
        break;
      case DEFAULT:
        method.addCode("super.placeService.place($S, super.placeService.getHistoryConverter($S).on$L(",
                       executableElement.getSimpleName()
                                        .toString(),
                       executableElement.getSimpleName()
                                        .toString(),
                       executableElement.getSimpleName()
                                        .toString()
                                        .substring(0,
                                                   1)
                                        .toUpperCase() + executableElement.getSimpleName()
                                                                          .toString()
                                                                          .substring(1));
        this.createSignatureForEventCall(method,
                                         executableElement,
                                         false);
        method.addCode("), false);\n");
        break;
      default:
        break;
    }
    method.endControlFlow();
  }

  private void generateEventHandlerCall(MethodSpec.Builder method,
                                        ExecutableElement executableElement,
                                        String eventHandlerClass,
                                        String handlerMetaDataMapName,
                                        boolean isPresenter,
                                        TypeElement historyConverterTypeElement) {
    String getHandlerMethodName = isPresenter ? "getPresenter" : "getEventHandler";
    String variableName = isPresenter ? "presenters" : "eventHandlers";
    String methodName = isPresenter ? "executePresenter" : "executeEventHandler";

    Class<?> metaDataClass = isPresenter ? PresenterHandlerMetaData.class : EventHandlerMetaData.class;

    MethodSpec.Builder passMethod = MethodSpec.methodBuilder("execPass")
                                              .addAnnotation(Override.class)
                                              .addModifiers(Modifier.PUBLIC)
                                              .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(EventMetaData.class),
                                                                                                            WildcardTypeName.subtypeOf(Object.class)),
                                                                                  "eventMetaData")
                                                                         .build())
                                              .addParameter(isPresenter ? ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(PresenterHandlerMetaData.class),
                                                                                                                          WildcardTypeName.subtypeOf(Object.class),
                                                                                                                          WildcardTypeName.subtypeOf(Object.class)),
                                                                                                "metaData")
                                                                                       .build() : ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(EventHandlerMetaData.class),
                                                                                                                                                  WildcardTypeName.subtypeOf(Object.class)),
                                                                                                                        "metaData")
                                                                                                               .build())
                                              .returns(boolean.class)
                                              .addCode("return metaData.$N().pass(eventMetaData.getEventName()",
                                                       getHandlerMethodName);
    createSignatureForEventCall(passMethod,
                                executableElement,
                                true);
    passMethod.addCode(");\n");

    MethodSpec.Builder execMethod = MethodSpec.methodBuilder("execEventHandlingMethod")
                                              .addAnnotation(Override.class)
                                              .addModifiers(Modifier.PUBLIC)
                                              .addParameter(isPresenter ? ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(PresenterHandlerMetaData.class),
                                                                                                                          WildcardTypeName.subtypeOf(Object.class),
                                                                                                                          WildcardTypeName.subtypeOf(Object.class)),
                                                                                                "metaData")
                                                                                       .build() : ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(EventHandlerMetaData.class),
                                                                                                                                                  WildcardTypeName.subtypeOf(Object.class)),
                                                                                                                        "metaData")
                                                                                                               .build())
                                              .addCode("(($T) metaData.$N()).on$L(",
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
    this.createSignatureForEventCall(execMethod,
                                     executableElement,
                                     false);
    execMethod.addCode(");\n");

    ParameterizedTypeName execEventHanderTypeName = ParameterizedTypeName.get(ClassName.get(AbstractEventBus.ExecEventHandler.class),
                                                                              ClassName.get(this.processorUtils.getPackageAsString(this.eventBusTypeElement),
                                                                                            this.eventBusTypeElement.getSimpleName()
                                                                                                                    .toString()));
    ParameterizedTypeName presenterTypeName = ParameterizedTypeName.get(ClassName.get(AbstractEventBus.ExecPresenter.class),
                                                                        ClassName.get(this.processorUtils.getPackageAsString(this.eventBusTypeElement),
                                                                                      this.eventBusTypeElement.getSimpleName()
                                                                                                              .toString()));

    method.addStatement("super.$L(eventMetaData, $L, $L, $L, $L)",
                        methodName,
                        variableName,
                        historyConverterTypeElement != null ? "listOfExecutedHandlers" : "null",
                        TypeSpec.anonymousClassBuilder("")
                                .addSuperinterface(isPresenter ? ClassName.get(AbstractEventBus.ExecPresenter.class) : ClassName.get(AbstractEventBus.ExecEventHandler.class))
                                .addMethod(passMethod.build())
                                .addMethod(execMethod.build())
                                .build(),
                        historyConverterTypeElement != null);
  }

  private void createSignatureForEventCall(MethodSpec.Builder method,
                                           ExecutableElement executableElement,
                                           boolean leadingComma) {
    IntStream.range(0,
                    executableElement.getParameters()
                                     .size())
             .forEachOrdered(i -> {
               if (i != 0) {
                 method.addCode(", ");
               } else {
                 if (leadingComma) {
                   method.addCode(", ");
                 }
               }
               method.addCode("$N",
                              executableElement.getParameters()
                                               .get(i)
                                               .getSimpleName()
                                               .toString());
             });
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

    public EventHandlingMethodGenerator build() {
      return new EventHandlingMethodGenerator(this);
    }
  }
}
