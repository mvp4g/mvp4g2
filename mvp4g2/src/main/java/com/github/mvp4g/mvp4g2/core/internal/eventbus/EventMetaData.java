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

package com.github.mvp4g.mvp4g2.core.internal.eventbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Event;
import com.github.mvp4g.mvp4g2.core.history.IsHistoryConverter;
import com.github.mvp4g.mvp4g2.core.internal.Mvp4g2InternalUse;
import com.github.mvp4g.mvp4g2.core.internal.history.HistoryMetaData;

import static java.util.Objects.isNull;

/**
 * generator of the eventbus
 */
@Mvp4g2InternalUse
public abstract class EventMetaData<E extends IsEventBus> {

  /* name of the event */
  private String                eventName;
  /* internal event name */
  private String                internalEventName;
  /* event historyName -> history */
  private String                historyName;
  /* history converter meta Data*/
  private HistoryMetaData       historyMetaData;
  /* history converter */
  private IsHistoryConverter<E> historyConverter;
  /* list of the parameter types */
  private Map<String, String>   paraemterTypes;
  /* canonical names of the eventhandler to be executed on this event */
  private List<String>          handlerTypes;
  /* canonical names of the eventhandler to be executed when bind is set on this event */
  /* will only be executed in case the event is the first time called */
  private List<String>          bindHandlerTypes;
  /* canonical names of the eventhandler to be activeted when this event is fired */
  private List<String>          activateHandlerTypes;
  /* canonical names of the eventhandler to be deactiveted when this event is fired */
  private List<String>          deactivateHandlerTypes;
  /* flag if event is passive */
  private boolean               passive;
  /* flag if event is navigation event */
  private boolean               navigationEvent;
  /* EventBus implementation */
  private AbstractEventBus<E>   eventBus;

  public EventMetaData(String internalEventName,
                       String eventName,
                       String historyName,
                       HistoryMetaData historyMetaData,
                       IsHistoryConverter<E> historyConverter,
                       boolean passive,
                       boolean navigationEvent) {
    this.internalEventName = internalEventName;
    this.eventName = eventName;
    this.historyName = historyName;
    this.historyMetaData = historyMetaData;
    this.historyConverter = historyConverter;
    this.passive = passive;
    this.navigationEvent = navigationEvent;

    this.paraemterTypes = new HashMap<>();
    this.handlerTypes = new ArrayList<>();
    this.bindHandlerTypes = new ArrayList<>();
    this.activateHandlerTypes = new ArrayList<>();
    this.deactivateHandlerTypes = new ArrayList<>();
  }

  public void setEventBus(AbstractEventBus<E> eventBus) {
    this.eventBus = eventBus;
    if (this.historyMetaData != null) {
      this.eventBus.add(historyMetaData.getHistoryConverterClassName(),
                        historyConverter);
    }
  }

  public void addActivateHandler(String handler) {
    this.activateHandlerTypes.add(handler);
  }

  public void addBindHandler(String handler) {
    this.bindHandlerTypes.add(handler);
  }

  public void addDeactivateHandler(String handler) {
    this.deactivateHandlerTypes.add(handler);
  }

  public void addHandler(String handler) {
    if (!this.handlerTypes.contains(handler)) {
      this.handlerTypes.add(handler);
    }
  }

  public void addParameterType(String parameterName,
                               String parameterType) {
    this.paraemterTypes.put(parameterName,
                            parameterType);
  }

  public String getEventName() {
    return eventName;
  }

  public List<String> getHandlerTypes() {
    return handlerTypes;
  }

  public Map<String, String> getParaemterTypes() {
    return paraemterTypes;
  }

  public List<String> getBindHandlerTypes() {
    return bindHandlerTypes;
  }

  public List<String> getActivateHandlerTypes() {
    return activateHandlerTypes;
  }

  public List<String> getDeactivateHandlerTypes() {
    return deactivateHandlerTypes;
  }

  public boolean isPassive() {
    return passive;
  }

  public boolean isNavigationEvent() {
    return navigationEvent;
  }

  public String getHistoryName() {
    return !isNull(this.historyName) && !Event.DEFAULT_HISTORY_NAME.equals(this.historyName) && !"".equals(this.historyName.trim()) ? this.historyName : this.eventName;
  }

  public HistoryMetaData getHistoryMetaData() {
    return historyMetaData;
  }

  public IsHistoryConverter<E> getHistoryConverter() {
    return this.historyMetaData == null ? null : this.eventBus.getHistoryConverter(this.historyMetaData.getHistoryConverterClassName());
  }

  public String getInternalEventName() {
    return internalEventName;
  }

  public void setInternalEventName(String internalEventName) {
    this.internalEventName = internalEventName;
  }
}
