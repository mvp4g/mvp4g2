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
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.EventMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.HandlerMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.intern.ClassNameModel;

public class ModelValidator {

  private EventBusMetaModel  eventBusMetaModel;
  private HandlerMetaModel   handlerMetaModel;
  private PresenterMetaModel presenterMetaModel;

  @SuppressWarnings("unused")
  private ModelValidator() {
  }

  private ModelValidator(Builder builder) {
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
  }

  private void checkHandlerUsedAsBindAndHandler()
    throws ProcessorException {
    for (EventMetaModel eventMetaModel : this.eventBusMetaModel.getEventMetaModels()) {
      for (ClassNameModel bindingClass : eventMetaModel.getBindings()) {
        for (ClassNameModel handlerClass : eventMetaModel.getHandlers()) {
          if (bindingClass.equals(handlerClass)) {
            throw new ProcessorException("Event: >>" + eventMetaModel.getEventName() + "<< - handler: >>" + handlerClass.getClassName() + "<< can not be set in bind- and handlers-attribute");
          }
        }
        if (this.handlerMetaModel.getHandlerData(bindingClass.getClassName()) != null) {
          for (String eventName : this.handlerMetaModel.getHandlerData(bindingClass.getClassName())
                                                       .getHandledEvents()) {
            if (eventMetaModel.getEventName()
                              .equals(eventName)) {
              throw new ProcessorException("Event: >>" + eventMetaModel.getEventName() + "<< - handler: >>" + bindingClass.getClassName() + "<< can not be set in bind- and handlers-attribute");
            }
          }
        }
        if (this.presenterMetaModel.getPresenterData(bindingClass.getClassName()) != null) {
          for (String eventName : this.presenterMetaModel.getPresenterData(bindingClass.getClassName())
                                                         .getHandledEvents()) {
            if (eventMetaModel.getEventName()
                              .equals(eventName)) {
              throw new ProcessorException("Event: >>" + eventMetaModel.getEventName() + "<< - handler: >>" + bindingClass.getClassName() + "<< can not be set in bind- and handlers-attribute");
            }
          }
        }
      }
    }
  }

  public static final class Builder {

    EventBusMetaModel  eventBusMetaModel;
    HandlerMetaModel   handlerMetaModel;
    PresenterMetaModel presenterMetaModel;

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
