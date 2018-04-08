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
package com.github.mvp4g.mvp4g2.processor.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Event;
import com.github.mvp4g.mvp4g2.core.history.NavigationEventCommand;
import com.github.mvp4g.mvp4g2.core.history.annotation.History;
import com.github.mvp4g.mvp4g2.core.internal.eventbus.AbstractEventBus;
import com.github.mvp4g.mvp4g2.core.internal.eventbus.EventMetaData;
import com.github.mvp4g.mvp4g2.core.internal.ui.HandlerMetaData;
import com.github.mvp4g.mvp4g2.core.internal.ui.PresenterMetaData;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.EventBusMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.EventMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.HandlerMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.HistoryMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.PresenterMetaModel;

/**
 * <p>The execution context manages all commands.<br>
 * Use run()-method to start execution.</p>
 */
public class EventHandlingMethodGenerator {

  private final static String EXECUTION_METHOD_PREFIX = "exec";

  private ProcessorUtils processorUtils;

  private TypeSpec.Builder   typeSpec;
  private EventBusMetaModel  eventBusMetaModel;
  private HistoryMetaModel   historyMetaModel;
  private HandlerMetaModel   handlerMetaModel;
  private PresenterMetaModel presenterMetaModel;

  @SuppressWarnings("unused")
  private EventHandlingMethodGenerator() {
  }

  private EventHandlingMethodGenerator(Builder builder) {
    this.processorUtils = builder.processorUtils;
    this.typeSpec = builder.typeSpec;
    this.eventBusMetaModel = builder.eventBusMetaModel;
    this.historyMetaModel = builder.historyMetaModel;
    this.handlerMetaModel = builder.handlerMetaModel;
    this.presenterMetaModel = builder.presenterMetaModel;
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate() {
    this.eventBusMetaModel.getEventMetaModels()
                          .forEach(eventMetaModel -> {
                            this.generateEventHandlingMethod(eventMetaModel);
                            this.generateEventHandlingMethodForExecution(eventMetaModel);
                          });
    // generate the code for the Start event handling
    this.generateStartEventHandlingMethod();
    // generate InitHistory event Method ...
    this.generateInitHistoryEventHandlingMethod();
    // generate NotFoundHistory-event
    this.generateNotFoundHistoryEventHandlingMethod();
  }

  private void generateEventHandlingMethod(EventMetaModel eventMetaModel) {
    MethodSpec.Builder eventHandlingMethod = MethodSpec.methodBuilder(eventMetaModel.getEventName())
                                                       .addAnnotation(Override.class)
                                                       .addModifiers(Modifier.PUBLIC,
                                                                     Modifier.FINAL);
    eventHandlingMethod.addStatement("int startLogDepth = $T.logDepth",
                                     ClassName.get(AbstractEventBus.class));
    eventHandlingMethod.beginControlFlow("try");
    // add parametners to method signature ...
    eventMetaModel.getParameterMetaDataList()
                  .stream()
                  .map(parameterMetaData -> ParameterSpec.builder(parameterMetaData.getType()
                                                                                   .getTypeName(),
                                                                  parameterMetaData.getName())
                                                         .addModifiers(Modifier.FINAL)
                                                         .build())
                  .forEach(eventHandlingMethod::addParameter);
    if (eventMetaModel.isNavigationEvent()) {
      eventHandlingMethod.addCode("super.logAskingForConfirmation(++$T.logDepth, $S",
                                  ClassName.get(AbstractEventBus.class),
                                  eventMetaModel.getEventName());
      eventMetaModel.getParameterMetaDataList()
                    .forEach(parameterMetaData -> eventHandlingMethod.addCode(", $N",
                                                                              parameterMetaData.getName()));
      eventHandlingMethod.addCode(");\n");
      MethodSpec.Builder executeMethod = MethodSpec.methodBuilder("execute")
                                                   .addAnnotation(Override.class)
                                                   .addModifiers(Modifier.PUBLIC);
      this.callEventExecMethod(executeMethod,
                               eventMetaModel);
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
                               eventMetaModel);
    }
    eventHandlingMethod.nextControlFlow("finally");
    eventHandlingMethod.addStatement("$T.logDepth = startLogDepth",
                                     ClassName.get(AbstractEventBus.class));
    eventHandlingMethod.endControlFlow();
    typeSpec.addMethod(eventHandlingMethod.build());
  }

  private void generateEventHandlingMethodForExecution(EventMetaModel eventMetaModel) {
    String sb = EventHandlingMethodGenerator.EXECUTION_METHOD_PREFIX +
                eventMetaModel.getEventName()
                              .substring(0,
                                         1)
                              .toUpperCase() +
                eventMetaModel.getEventName()
                              .substring(1);
    MethodSpec.Builder eventHandlingMethod = MethodSpec.methodBuilder(sb)
                                                       .addModifiers(Modifier.PUBLIC,
                                                                     Modifier.FINAL);
    // add parametners to method signature ...
    eventMetaModel.getParameterMetaDataList()
                  .stream()
                  .map(parameterMetaData -> ParameterSpec.builder(parameterMetaData.getType()
                                                                                   .getTypeName(),
                                                                  parameterMetaData.getName())
                                                         .addModifiers(Modifier.FINAL)
                                                         .build())
                  .forEach(eventHandlingMethod::addParameter);
    // log event
    eventHandlingMethod.addCode("super.logEvent(++$T.logDepth, $S",
                                ClassName.get(AbstractEventBus.class),
                                eventMetaModel.getEventName());
    eventMetaModel.getParameterMetaDataList()
                  .forEach(parameterMetaData -> eventHandlingMethod.addCode(", $N",
                                                                            parameterMetaData.getName()));
    eventHandlingMethod.addCode(");\n");
    eventHandlingMethod.addStatement("++$T.logDepth",
                                     ClassName.get(AbstractEventBus.class));
    // generate code for filter event!
    if (eventMetaModel.getParameterMetaDataList()
                      .size() > 0) {
      StringBuilder parameters = new StringBuilder();
      IntStream.range(0,
                      eventMetaModel.getParameterMetaDataList()
                                    .size())
               .forEachOrdered(i -> {
                 parameters.append(eventMetaModel.getParameterMetaDataList()
                                                 .get(i)
                                                 .getName());
                 if (i != eventMetaModel.getParameterMetaDataList()
                                        .size() - 1) {
                   parameters.append(", ");
                 }
               });
      eventHandlingMethod.beginControlFlow("if (!super.filterEvent($S, $L))",
                                           eventMetaModel.getEventName(),
                                           parameters);
    } else {
      eventHandlingMethod.beginControlFlow("if (!super.filterEvent($S))",
                                           eventMetaModel.getEventName());
    }
    eventHandlingMethod.addStatement("return");
    eventHandlingMethod.endControlFlow();
    // get event meta data from store ...
    eventHandlingMethod.addStatement("$T<$T> eventMetaData = super.getEventMetaData($S)",
                                     ClassName.get(EventMetaData.class),
                                     this.eventBusMetaModel.getEventBus()
                                                           .getTypeName(),
                                     eventMetaModel.getEventInternalName());
    /* bind views */
    eventHandlingMethod.addStatement("super.createAndBindView(eventMetaData)");
    /* Handle Binding */
    eventHandlingMethod.addCode("super.bind(eventMetaData");
    eventMetaModel.getParameterMetaDataList()
                  .forEach(parameterMetaData -> eventHandlingMethod.addCode(", $N",
                                                                            parameterMetaData.getName()));
    eventHandlingMethod.addCode(");\n");
    /* activaiting handlers */
    eventHandlingMethod.addStatement("super.activate(eventMetaData)");
    /* deactivaiting handlers */
    eventHandlingMethod.addStatement("super.deactivate(eventMetaData)");
    // fire events
    List<String> eventHandlerClasses = this.getAllEventHandlersForEvent(eventMetaModel);
    if (eventHandlerClasses != null) {
      eventHandlingMethod.addStatement("$T<$T<?>> handlers = null",
                                       ClassName.get(List.class),
                                       ClassName.get(HandlerMetaData.class));
      eventHandlingMethod.addStatement("$T<$T<?, ?>> presenters = null",
                                       ClassName.get(List.class),
                                       ClassName.get(PresenterMetaData.class));
      eventHandlingMethod.addStatement("$T<$T> listOfExecutedHandlers = new $T<>()",
                                       ClassName.get(List.class),
                                       ClassName.get(String.class),
                                       ClassName.get(ArrayList.class));
      // start presenter code
      eventHandlerClasses.forEach(eventHandlerClass -> {
        eventHandlingMethod.addComment("handling: " + eventHandlerClass);
        this.createEventHandlingMethod(eventHandlingMethod,
                                       eventMetaModel,
                                       eventHandlerClass);
      });
      this.createPlaceServicePlaceCall(eventHandlingMethod,
                                       eventMetaModel);
    }
    typeSpec.addMethod(eventHandlingMethod.build());
  }

  private void generateStartEventHandlingMethod() {
    MethodSpec.Builder startEventHandlingMethod = MethodSpec.methodBuilder("fireStartEvent")
                                                            .addAnnotation(Override.class)
                                                            .addModifiers(Modifier.PUBLIC,
                                                                          Modifier.FINAL);
    // get all elements annotated with Start
    Optional<EventMetaModel> optional = this.eventBusMetaModel.getEventMetaModels()
                                                              .stream()
                                                              .filter(EventMetaModel::isStartEvent)
                                                              .findFirst();
    // get event meta data from store ...
    optional.ifPresent(eventMetaModel -> startEventHandlingMethod.addStatement("this.$L()",
                                                                               eventMetaModel
                                                                                 .getEventName()));
    typeSpec.addMethod(startEventHandlingMethod.build());
  }

  private void generateInitHistoryEventHandlingMethod() {
    MethodSpec.Builder initHistoryEventHandlingMethod = MethodSpec.methodBuilder("fireInitHistoryEvent")
                                                                  .addAnnotation(Override.class)
                                                                  .addModifiers(Modifier.PUBLIC,
                                                                                Modifier.FINAL);
    // get all elements annotated with Start
    Optional<EventMetaModel> optional = this.eventBusMetaModel.getEventMetaModels()
                                                              .stream()
                                                              .filter(EventMetaModel::isInitHistory)
                                                              .findFirst();
    if (optional.isPresent()) {
      // get event meta data from store ...
      initHistoryEventHandlingMethod.addStatement("this.$L()",
                                                  optional.get()
                                                          .getEventName());
    } else {
      initHistoryEventHandlingMethod.addStatement("assert false : $S",
                                                  "no @InitHistory-event defined");
    }
    typeSpec.addMethod(initHistoryEventHandlingMethod.build());
  }

  private void generateNotFoundHistoryEventHandlingMethod() {
    MethodSpec.Builder notFoundHistoryEventHandlingMethod = MethodSpec.methodBuilder("fireNotFoundHistoryEvent")
                                                                      .addAnnotation(Override.class)
                                                                      .addModifiers(Modifier.PUBLIC,
                                                                                    Modifier.FINAL);
    // get all elements annotated with Start
    Optional<EventMetaModel> optional = this.eventBusMetaModel.getEventMetaModels()
                                                              .stream()
                                                              .filter(EventMetaModel::isNotFoundHistory)
                                                              .findFirst();
    if (optional.isPresent()) {
      // get event meta data from store ...
      notFoundHistoryEventHandlingMethod.addStatement("this.$L()",
                                                      optional.get()
                                                              .getEventName());
    } else {
      notFoundHistoryEventHandlingMethod.addStatement("assert false : $S",
                                                      "no @NotFoundHistory-event defined");
    }
    typeSpec.addMethod(notFoundHistoryEventHandlingMethod.build());
  }

  private void callEventExecMethod(MethodSpec.Builder method,
                                   EventMetaModel eventMetaModel) {
    String sb = EventHandlingMethodGenerator.EXECUTION_METHOD_PREFIX +
                eventMetaModel.getEventName()
                              .substring(0,
                                         1)
                              .toUpperCase() +
                eventMetaModel.getEventName()
                              .substring(1);
    method.addCode(sb + "(");
    IntStream.range(0,
                    eventMetaModel.getParameterMetaDataList()
                                  .size())
             .forEachOrdered(i -> {
               method.addCode(eventMetaModel.getParameterMetaDataList()
                                            .get(i)
                                            .getName());
               if (i != eventMetaModel.getParameterMetaDataList()
                                      .size() - 1) {
                 method.addCode(", ");
               }
             });
    method.addCode(");\n");
  }

  private List<String> getAllEventHandlersForEvent(EventMetaModel eventMetaModel) {
    List<String> listOfEventHandlers = new ArrayList<>();
    eventMetaModel.getHandlers()
                  .stream()
                  .filter(s -> !listOfEventHandlers.contains(s.getClassName()))
                  .forEach(s -> listOfEventHandlers.add(s.getClassName()));
    this.handlerMetaModel.getHandlerKeys()
                         .stream()
                         .filter(s -> !listOfEventHandlers.contains(s))
                         .map(s -> this.handlerMetaModel.getHandlerData(s))
                         .forEach(handlerData -> {
                           handlerData.getHandledEvents()
                                      .stream()
                                      .filter(handledEvent -> handledEvent.equals(this.processorUtils.createEventHandlingMethodName(eventMetaModel.getEventInternalName())))
                                      .map(handledEvent -> handlerData.getHandler()
                                                                      .getClassName())
                                      .forEach(listOfEventHandlers::add);
                         });
    this.presenterMetaModel.getPresenterKeys()
                           .stream()
                           .filter(s -> !listOfEventHandlers.contains(s))
                           .map(handlerKey -> this.presenterMetaModel.getPresenterData(handlerKey))
                           .forEach(presenterData -> {
                             presenterData.getHandledEvents()
                                          .stream()
                                          .filter(handledEvent -> handledEvent.equals(this.processorUtils.createEventHandlingMethodName(eventMetaModel.getEventInternalName())))
                                          .map(handledEvent -> presenterData.getPresenter()
                                                                            .getClassName())
                                          .forEach(listOfEventHandlers::add);
                           });
    return listOfEventHandlers;
  }


  private void createEventHandlingMethod(MethodSpec.Builder method,
                                         EventMetaModel eventMetaModel,
                                         String eventHandlerClass) {
    boolean isPresenter = this.presenterMetaModel.getPresenterKeys()
                                                 .contains(eventHandlerClass);
    String handlerMetaDataMapName = isPresenter ? "presenterMetaDataMap" : "handlerMetaDataMap";
    String variableName = isPresenter ? "presenters" : "handlers";
    method.addStatement("$N = this.$N.get($S)",
                        variableName,
                        handlerMetaDataMapName,
                        eventHandlerClass);
    this.generateEventHandlerCall(method,
                                  eventMetaModel,
                                  eventHandlerClass,
                                  isPresenter);
  }

  private void createPlaceServicePlaceCall(MethodSpec.Builder method,
                                           EventMetaModel eventMetaModel) {
    if (eventMetaModel.getHistoryConverter() == null) {
      return;
    }
    if (Event.NoHistoryConverter.class.getCanonicalName()
                                      .equals(eventMetaModel.getHistoryConverter()
                                                            .getClassName())) {
      return;
    }
    String historyConverterType = this.historyMetaModel.getHistoryData(eventMetaModel.getHistoryConverter()
                                                                                     .getClassName())
                                                       .getHistoryConverterType();
    method.beginControlFlow("if (listOfExecutedHandlers.size() > 0)");
    switch (History.HistoryConverterType.valueOf(historyConverterType)) {
      case NONE:
        method.addStatement("super.placeService.place($S, null, false)",
                            eventMetaModel.getEventName());
        break;
      case SIMPLE:
        method.addCode("super.placeService.place($S, (($L) super.placeService.getHistoryConverter($S)).convertToToken($S",
                       eventMetaModel.getEventName(),
                       eventMetaModel.getHistoryConverter()
                                     .getClassName(),
                       eventMetaModel.getEventName(),
                       eventMetaModel.getEventName());
        this.createSignatureForEventCall(method,
                                         eventMetaModel,
                                         true);
        method.addCode("), false);\n");
        break;
      case DEFAULT:
        method.addCode("super.placeService.place($S, (($T) super.placeService.getHistoryConverter($S)).on$L(",
                       eventMetaModel.getEventName(),
                       ClassName.get(eventMetaModel.getHistoryConverter()
                                                   .getPackage(),
                                     eventMetaModel.getHistoryConverter()
                                                   .getSimpleName()),
                       eventMetaModel.getEventName(),
                       eventMetaModel.getEventName()
                                     .substring(0,
                                                1)
                                     .toUpperCase() + eventMetaModel.getEventName()
                                                                    .substring(1));
        this.createSignatureForEventCall(method,
                                         eventMetaModel,
                                         false);
        method.addCode("), false);\n");
        break;
      default:
        break;
    }
    method.endControlFlow();
  }

  private void generateEventHandlerCall(MethodSpec.Builder method,
                                        EventMetaModel eventMetaModel,
                                        String eventHandlerClass,
                                        boolean isPresenter) {
    String getHandlerMethodName = isPresenter ? "getPresenter" : "getHandler";
    String variableName = isPresenter ? "presenters" : "handlers";
    String methodName = isPresenter ? "executePresenter" : "executeHandler";
    MethodSpec.Builder passMethod = MethodSpec.methodBuilder("execPass")
                                              .addAnnotation(Override.class)
                                              .addModifiers(Modifier.PUBLIC)
                                              .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(EventMetaData.class),
                                                                                                            WildcardTypeName.subtypeOf(Object.class)),
                                                                                  "eventMetaData")
                                                                         .build())
                                              .addParameter(isPresenter ? ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(PresenterMetaData.class),
                                                                                                                          WildcardTypeName.subtypeOf(Object.class),
                                                                                                                          WildcardTypeName.subtypeOf(Object.class)),
                                                                                                "metaData")
                                                                                       .build() : ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(HandlerMetaData.class),
                                                                                                                                                  WildcardTypeName.subtypeOf(Object.class)),
                                                                                                                        "metaData")
                                                                                                               .build())
                                              .returns(boolean.class)
                                              .addCode("return metaData.$N().pass(eventMetaData.getEventName()",
                                                       getHandlerMethodName);
    createSignatureForEventCall(passMethod,
                                eventMetaModel,
                                true);
    passMethod.addCode(");\n");

    MethodSpec.Builder execMethod = MethodSpec.methodBuilder("execEventHandlingMethod")
                                              .addAnnotation(Override.class)
                                              .addModifiers(Modifier.PUBLIC)
                                              .addParameter(isPresenter ? ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(PresenterMetaData.class),
                                                                                                                          WildcardTypeName.subtypeOf(Object.class),
                                                                                                                          WildcardTypeName.subtypeOf(Object.class)),
                                                                                                "metaData")
                                                                                       .build() : ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(HandlerMetaData.class),
                                                                                                                                                  WildcardTypeName.subtypeOf(Object.class)),
                                                                                                                        "metaData")
                                                                                                               .build())
                                              .addCode("(($T) metaData.$N()).on$L(",
                                                       ClassName.get(eventHandlerClass.substring(0,
                                                                                                 eventHandlerClass.lastIndexOf(".")),
                                                                     eventHandlerClass.substring(eventHandlerClass.lastIndexOf(".") + 1)),
                                                       getHandlerMethodName,
                                                       eventMetaModel.getEventName()
                                                                     .substring(0,
                                                                                1)
                                                                     .toUpperCase() + eventMetaModel.getEventName()
                                                                                                    .substring(1));
    this.createSignatureForEventCall(execMethod,
                                     eventMetaModel,
                                     false);
    execMethod.addCode(");\n");

    method.addStatement("super.$L(eventMetaData, $L, $L, $L, $L)",
                        methodName,
                        variableName,
                        eventMetaModel.getHistoryConverter() == null || Event.NoHistoryConverter.class.getCanonicalName()
                                                                                                      .equals(eventMetaModel.getHistoryConverter()
                                                                                                                            .getClassName()) ? "null" : "listOfExecutedHandlers",
                        TypeSpec.anonymousClassBuilder("")
                                .addSuperinterface(isPresenter ? ClassName.get(AbstractEventBus.ExecPresenter.class) : ClassName.get(AbstractEventBus.ExecHandler.class))
                                .addMethod(passMethod.build())
                                .addMethod(execMethod.build())
                                .build(),
                        !(eventMetaModel.getHistoryConverter() == null || Event.NoHistoryConverter.class.getCanonicalName()
                                                                                                        .equals(eventMetaModel.getHistoryConverter()
                                                                                                                              .getClassName())
                        ));
  }

  private void createSignatureForEventCall(MethodSpec.Builder method,
                                           EventMetaModel eventMetaModel,
                                           boolean leadingComma) {
    IntStream.range(0,
                    eventMetaModel.getParameterMetaDataList()
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
                              eventMetaModel.getParameterMetaDataList()
                                            .get(i)
                                            .getName());
             });
  }

  public static final class Builder {

    ProcessorUtils     processorUtils;
    TypeSpec.Builder   typeSpec;
    EventBusMetaModel  eventBusMetaModel;
    HistoryMetaModel   historyMetaModel;
    HandlerMetaModel   handlerMetaModel;
    PresenterMetaModel presenterMetaModel;

    public Builder processorUtils(ProcessorUtils processorUtils) {
      this.processorUtils = processorUtils;
      return this;
    }

    public Builder eventBusMetaModel(EventBusMetaModel eventBusMetaModel) {
      this.eventBusMetaModel = eventBusMetaModel;
      return this;
    }

    public Builder historyMetaModel(HistoryMetaModel historyMetaModel) {
      this.historyMetaModel = historyMetaModel;
      return this;
    }

    public Builder handlerMetaModel(HandlerMetaModel handlerMetaModel) {
      this.handlerMetaModel = handlerMetaModel;
      return this;
    }

    public Builder presenterMetaModel(PresenterMetaModel presenterMetaModel) {
      this.presenterMetaModel = presenterMetaModel;
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
