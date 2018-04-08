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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import com.github.mvp4g.mvp4g2.processor.ProcessorException;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.EventBusMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.EventMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.HandlerMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.PresenterMetaModel;
import com.github.mvp4g.mvp4g2.processor.model.intern.ClassNameModel;

import static java.util.Objects.isNull;

public class ModelValidator {

  private ProcessorUtils processorUtils;

  private EventBusMetaModel  eventBusMetaModel;
  private HandlerMetaModel   handlerMetaModel;
  private PresenterMetaModel presenterMetaModel;

  @SuppressWarnings("unused")
  private ModelValidator() {
  }

  private ModelValidator(Builder builder) {
    this.processorUtils = builder.processorUtils;
    this.eventBusMetaModel = builder.eventBusMetaModel;
    this.handlerMetaModel = builder.handlerMetaModel;
    this.presenterMetaModel = builder.presenterMetaModel;
  }

  public static Builder builder() {
    return new Builder();
  }

  public void validate()
    throws ProcessorException {
    // check if a handler is used inside the bind and the handler attribute for one event
    this.checkHandlerUsedAsBindAndHandler();
    // check weather there are more then one presenter annotated with IsShell
    this.checkNumbersOfPrensentersWhichImplementsIsShell();
    // check, if all event handler methods are used ...
    this.checkIfAllEventHandlerMethodsAreUsed();
    // check, if all events got an event handling method
    this.checkIfAllEventMethodsAreHandled();
    // check, if all events which have the handler-attribute have an implementation
    this.checkIfAllEventMethodsOfHandlerAreHandled();
    //check, if all event handling mehtod return void!
  }

  private void checkHandlerUsedAsBindAndHandler()
    throws ProcessorException {
    if (!isNull(this.eventBusMetaModel)) {
      for (EventMetaModel eventMetaModel : this.eventBusMetaModel.getEventMetaModels()) {
        for (ClassNameModel bindingClass : eventMetaModel.getBindings()) {
          for (ClassNameModel handlerClass : eventMetaModel.getHandlers()) {
            if (bindingClass.equals(handlerClass)) {
              throw new ProcessorException("Mvp4g2Processor: Event: >>" + eventMetaModel.getEventName() + "<< - handler: >>" + handlerClass.getClassName() + "<< can not be set in bind- and handlers-attribute");
            }
          }
          if (this.handlerMetaModel.getHandlerData(bindingClass.getClassName()) != null) {
            for (String eventName : this.handlerMetaModel.getHandlerData(bindingClass.getClassName())
                                                         .getHandledEvents()) {
              if (eventMetaModel.getEventName()
                                .equals(eventName)) {
                throw new ProcessorException("Mvp4g2Processor: Event: >>" + eventMetaModel.getEventName() + "<< - handler: >>" + bindingClass.getClassName() + "<< can not be set in bind- and handlers-attribute");
              }
            }
          }
          if (this.presenterMetaModel.getPresenterData(bindingClass.getClassName()) != null) {
            for (String eventName : this.presenterMetaModel.getPresenterData(bindingClass.getClassName())
                                                           .getHandledEvents()) {
              if (eventMetaModel.getEventName()
                                .equals(eventName)) {
                throw new ProcessorException("Mvp4g2Processor: Event: >>" + eventMetaModel.getEventName() + "<< - handler: >>" + bindingClass.getClassName() + "<< can not be set in bind- and handlers-attribute");
              }
            }
          }
        }
      }
    } else {
      throw new ProcessorException("Mvp4g2Processor: no EventBusMetaModel found! Did you forget to create an EventBus for mvp4g2 or forget to annotate the EventBus with @EventBus?");
    }
  }

  private void checkNumbersOfPrensentersWhichImplementsIsShell()
    throws ProcessorException {
    if (!isNull(this.presenterMetaModel)) {
      boolean isShell = false;
      for (PresenterMetaModel.PresenterData presenterData : this.presenterMetaModel.getPresenterDatas()) {
        if (presenterData.isShell()) {
          if (isShell) {
            throw new ProcessorException("Mvp4g2Processor: there can be only one presenter implementing IsShell");
          }
          isShell = true;
        }
      }
    }
  }

  private void checkIfAllEventHandlerMethodsAreUsed() {
    if (!isNull(this.presenterMetaModel)) {
      if (!isNull(this.eventBusMetaModel)) {
        this.presenterMetaModel.getPresenterDatas()
                               .forEach(presenterData -> presenterData.getHandledEvents()
                                                                      .stream()
                                                                      .map(handledEvents -> Arrays.asList(handledEvents.split(",")))
                                                                      .flatMap(Collection::stream)
                                                                      .filter(event -> event != null && event.trim()
                                                                                                             .length() > 0)
                                                                      .map(event -> processorUtils.createEventNameFromHandlingMethod(event))
                                                                      .filter(event -> this.eventBusMetaModel.getEventMetaModel(event) == null)
                                                                      .forEach(event -> processorUtils.createWarningMessage("Mvp4g2Processor: presenter >>" + presenterData.getPresenter()
                                                                                                                                                                           .getClassName() + "<< -> event >>" + createMethodName(event) + "<< is never called by the eventbus")));
      }
    }
  }

  private void checkIfAllEventMethodsAreHandled() {
    if (!isNull(this.eventBusMetaModel)) {
      for (EventMetaModel eventModel : this.eventBusMetaModel.getEventMetaModels()) {
        if (eventModel.getHandlers()
                      .size() == 0) {
          boolean eventIsHandled = false;
          if (!isNull(this.presenterMetaModel)) {
            eventIsHandled = this.presenterMetaModel.getPresenterDatas()
                                                    .stream()
                                                    .anyMatch(presenterData -> presenterData.handlesEvents(this.processorUtils.createEventHandlingMethodName(eventModel.getEventInternalName())));
          }
          if (!eventIsHandled) {
            if (!isNull(this.handlerMetaModel)) {
              eventIsHandled = this.handlerMetaModel.getHandlerDatas()
                                                    .stream()
                                                    .anyMatch(handlerData -> handlerData.handlesEvents(this.processorUtils.createEventHandlingMethodName(eventModel.getEventInternalName())));
            }
          }
          if (!eventIsHandled) {
            if (eventModel.getBindings()
                          .size() > 0) {
              this.processorUtils.createNoteMessage("Mvp4g2Processor: event >>" + createMethodName(eventModel.getEventInternalName()) + "<< is only used for binding");
            } else {
              this.processorUtils.createErrorMessage("Mvp4g2Processor: event >>" + createMethodName(eventModel.getEventInternalName()) + "<< is never handled by a presenter or handler");
            }
          }
        }
      }
    }
  }

  private void checkIfAllEventMethodsOfHandlerAreHandled()
    throws ProcessorException {
    if (!isNull(this.eventBusMetaModel)) {
      for (EventMetaModel eventMetaModel : this.eventBusMetaModel.getEventMetaModels()) {
        for (ClassNameModel classNameModel : eventMetaModel.getHandlers()) {
          hasEventHandlingMethodImplemented(eventMetaModel,
                                            eventMetaModel.getEventInternalName(),
                                            classNameModel);
        }
      }
    }
  }

  private String createMethodName(String eventInternalName) {
    List<String> tokens = Arrays.asList(eventInternalName.split("_pPp_"));
    StringBuilder sb = new StringBuilder();
    sb.append(tokens.get(0))
      .append("(");
    int bound = tokens.size();
    IntStream.range(1,
                    bound)
             .forEachOrdered(i -> {
               sb.append(tokens.get(i)
                               .replace("_",
                                        "."));
               if (i < tokens.size() - 1) {
                 sb.append(", ");
               }
             });
    sb.append(")");
    return sb.toString();
  }

  private void hasEventHandlingMethodImplemented(EventMetaModel eventMetaModel,
                                                 String eventInternalName,
                                                 ClassNameModel classNameModel)
    throws ProcessorException {
    TypeElement typeElement = this.processorUtils.getElements()
                                                 .getTypeElement(classNameModel.getClassName());
    if (typeElement != null) {
      // improvement: get all ExecutabelElement of type,
      // convert to String and add to list and use this list
      // for the compare!
      Map<String, ExecutableElement> nameOfExecutableElements = new HashMap<>();
      this.processorUtils.getElements()
                         .getAllMembers(typeElement)
                         .stream()
                         .filter(element -> element instanceof ExecutableElement)
                         .map(element -> (ExecutableElement) element)
                         .forEach(executableElement -> nameOfExecutableElements.put(executableElement.toString(),
                                                                                    executableElement));
      // method to look for
      String methodNameToLookFor = this.createEventHandlungMethodName(eventInternalName);
      // try to find in Map
      ExecutableElement handlingElement = nameOfExecutableElements.get(methodNameToLookFor);
      if (handlingElement != null) {
        if (!"void".equals(handlingElement.getReturnType()
                                          .toString())) {
          throw new ProcessorException("Mvp4g2Processor: EventElement: >>" + eventInternalName.split(",")[0] + "<< must return 'void'");
        }
        return;
      }
      // if we ran into this code,we dod not find a method to handle this event.
      // in this case we check if there are bindings, then this is ok,
      // otherwiese we throw an excpetion!
      if (eventMetaModel.getBindings()
                        .size() > 0) {
        this.processorUtils.createNoteMessage("Mvp4g2Processor: event >>" + eventInternalName.split(",")[0] + "<< is only used for binding");
      } else {
        this.processorUtils.createErrorMessage("Mvp4g2Processor: presenter >>" + classNameModel.getClassName() + "<< -> event >>" + createEventHandlungMethodName(eventInternalName) + "<< is not handled by presenter/handler and has no bindings");
      }
    }
  }

  private String createEventHandlungMethodName(String eventInternalName) {
    List<String> tokens = Arrays.asList(eventInternalName.split("_pPp_"));
    StringBuilder sb = new StringBuilder();
    sb.append("on")
      .append(tokens.get(0)
                    .substring(0,
                               1)
                    .toUpperCase())
      .append(tokens.get(0)
                    .substring(1))
      .append("(");
    int bound = tokens.size();
    IntStream.range(1,
                    bound)
             .forEachOrdered(i -> {
               sb.append(tokens.get(i)
                               .replace("_",
                                        "."));
               if (i < tokens.size() - 1) {
                 sb.append(",");
               }
             });
    sb.append(")");
    return sb.toString();
  }

  public static final class Builder {

    ProcessorUtils processorUtils;

    EventBusMetaModel  eventBusMetaModel;
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

    public Builder handlerMetaModel(HandlerMetaModel handlerMetaModel) {
      this.handlerMetaModel = handlerMetaModel;
      return this;
    }

    public Builder presenterMetaModel(PresenterMetaModel presenterMetaModel) {
      this.presenterMetaModel = presenterMetaModel;
      return this;
    }

    public ModelValidator build() {
      return new ModelValidator(this);
    }
  }
}
