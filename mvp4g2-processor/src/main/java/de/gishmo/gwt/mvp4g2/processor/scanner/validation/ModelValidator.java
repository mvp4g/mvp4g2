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

import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.EventMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.HandlerMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.intern.ClassNameModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

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
                               .forEach(presenterData -> {
                                 presenterData.getHandledEvents()
                                              .stream()
                                              .map(handledEvents -> Arrays.asList(handledEvents.split(",")))
                                              .flatMap(Collection::stream)
                                              .filter(event -> this.eventBusMetaModel.getEventMetaModel(event) == null)
                                              .forEach(event -> processorUtils.createWarningMessage("Mvp4g2Processor: presenter >>" + presenterData.getPresenter()
                                                                                                                                                   .getClassName() + "<< -> event >>" + createMethodName(event) + " is never called by the eventbus"));
                               });
      }
    }
  }

  private String createMethodName(String eventHandlingName) {
    List<String> tokens = Arrays.asList(eventHandlingName.split("_pPp_"));
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
