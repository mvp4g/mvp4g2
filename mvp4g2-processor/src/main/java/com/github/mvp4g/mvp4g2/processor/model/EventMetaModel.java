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

package com.github.mvp4g.mvp4g2.processor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.github.mvp4g.mvp4g2.processor.ProcessorConstants;
import com.github.mvp4g.mvp4g2.processor.model.intern.ClassNameModel;
import com.github.mvp4g.mvp4g2.processor.model.intern.IsMetaModel;

import static java.util.Objects.isNull;

public class EventMetaModel
  implements IsMetaModel {

  private static final String KEY_EVENT_EVENT_INTERNAL_NAME = "eventInternalName";
  private static final String KEY_EVENT_NAME                = "eventName";
  private static final String KEY_HISTORY_EVENT_NAME        = "historyEventName";
  private static final String KEY_HISTORY_CONVERTER         = "historyConverter";
  private static final String KEY_BINDINGS                  = "bindings";
  private static final String KEY_HANDLERS                  = "handlers";
  private static final String KEY_NAVIGATION_EVENT          = "navigationEvent";
  private static final String KEY_PASSIVE                   = "passive";
  private static final String KEY_ACTIVATE_HANDELRS         = "activateHandlers";
  private static final String KEY_DEACTIVATE_HANDELRS       = "deactivateHandlers";
  private static final String KEY_PARAMETERS                = "parameters";
  private static final String KEY_START_EVENT               = "startEvent";
  private static final String KEY_INIT_HISTORY              = "initHistory";
  private static final String KEY_NOT_FOUND_HISTORY         = "notFoundHistory";

  private String               eventInternalName;
  private String               eventName;
  private String               startEvent;
  private String               initHistory;
  private String               notFoundHistory;
  private String               historyEventName;
  private ClassNameModel       historyConverter;
  private List<ClassNameModel> bindings;
  private List<ClassNameModel> handlers;
  private String               navigationEvent;
  private String               passive;
  private List<ClassNameModel> activateHandlers;
  private List<ClassNameModel> deactivateHandlers;
  private String               parameters;
  private List<ParameterMetaData> parameterMetaDataList = new ArrayList<>();


  public EventMetaModel() {
  }

  public EventMetaModel(Properties properties) {
    this.eventInternalName = properties.getProperty(EventMetaModel.KEY_EVENT_EVENT_INTERNAL_NAME);
    this.eventName = properties.getProperty(EventMetaModel.KEY_EVENT_NAME);
    this.startEvent = properties.getProperty(EventMetaModel.KEY_START_EVENT);
    this.initHistory = properties.getProperty(EventMetaModel.KEY_INIT_HISTORY);
    this.notFoundHistory = properties.getProperty(EventMetaModel.KEY_NOT_FOUND_HISTORY);
    this.historyEventName = properties.getProperty(EventMetaModel.KEY_HISTORY_EVENT_NAME);
    this.historyConverter = new ClassNameModel(properties.getProperty(EventMetaModel.KEY_HISTORY_CONVERTER));
    String value = properties.getProperty(EventMetaModel.KEY_BINDINGS);
    if (isEmptyOrNull(value)) {
      this.bindings = new ArrayList<>();
    } else {
      this.bindings = Arrays.stream(properties.getProperty(EventMetaModel.KEY_BINDINGS)
                                              .split("\\s*,\\s*"))
                            .map(s -> new ClassNameModel(s))
                            .collect(Collectors.toList());
    }
    if (isEmptyOrNull(properties.getProperty(EventMetaModel.KEY_HANDLERS))) {
      this.handlers = new ArrayList<>();
    } else {
      this.handlers = Arrays.stream(properties.getProperty(EventMetaModel.KEY_HANDLERS)
                                              .split("\\s*,\\s*"))
                            .map(s -> new ClassNameModel(s))
                            .collect(Collectors.toList());
    }
    this.navigationEvent = properties.getProperty(EventMetaModel.KEY_NAVIGATION_EVENT);
    this.passive = properties.getProperty(EventMetaModel.KEY_PASSIVE);
    if (isEmptyOrNull(properties.getProperty(EventMetaModel.KEY_ACTIVATE_HANDELRS))) {
      this.activateHandlers = new ArrayList<>();
    } else {
      this.activateHandlers = Arrays.stream(properties.getProperty(EventMetaModel.KEY_ACTIVATE_HANDELRS)
                                                      .split("\\s*,\\s*"))
                                    .map(s -> new ClassNameModel(s))
                                    .collect(Collectors.toList());
    }
    if (isEmptyOrNull(properties.getProperty(EventMetaModel.KEY_DEACTIVATE_HANDELRS))) {
      this.deactivateHandlers = new ArrayList<>();
    } else {
      this.deactivateHandlers = Arrays.stream(properties.getProperty(EventMetaModel.KEY_DEACTIVATE_HANDELRS)
                                                        .split("\\s*,\\s*"))
                                      .map(s -> new ClassNameModel(s))
                                      .collect(Collectors.toList());
    }
    if (isEmptyOrNull(properties.getProperty(EventMetaModel.KEY_PARAMETERS))) {
      this.parameters = "";
      this.parameterMetaDataList = new ArrayList<>();
    } else {
      this.parameters = properties.getProperty(EventMetaModel.KEY_PARAMETERS);
      if (properties.getProperty(EventMetaModel.KEY_PARAMETERS)
                    .trim()
                    .length() > 0) {
        this.parameterMetaDataList = Arrays.stream(properties.getProperty(EventMetaModel.KEY_PARAMETERS)
                                                             .split("\\s*,\\s*"))
                                           .map(s -> {
                                             String[] values = s.split(ProcessorConstants.TYPE_DELIMITER);
                                             return new ParameterMetaData(values[0],
                                                                          values[1]);
                                           })
                                           .collect(Collectors.toList());
      }
    }
  }

  private boolean isEmptyOrNull(String value) {
    return value == null || value.isEmpty();
  }

  public void addParameter(String name,
                           String type) {
    this.parameterMetaDataList.add(new ParameterMetaData(name,
                                                         type));
  }

  public String getEventInternalName() {
    return eventInternalName;
  }

  public void setEventInternalName(String eventInternalName) {
    this.eventInternalName = eventInternalName;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getHistoryEventName() {
    return historyEventName;
  }

  public void setHistoryEventName(String historyEventName) {
    this.historyEventName = historyEventName;
  }

  public ClassNameModel getHistoryConverter() {
    return historyConverter;
  }

  public void setHistoryConverter(String historyConverter) {
    this.historyConverter = new ClassNameModel(historyConverter);
  }

  public List<ClassNameModel> getBindings() {
    return bindings;
  }

  public void setBindings(List<String> bindings) {
    if (isNull(bindings)) {
      this.bindings = new ArrayList<>();
    } else {
      this.bindings = bindings.stream()
                              .map(s -> new ClassNameModel(s))
                              .collect(Collectors.toList());
    }
  }

  public List<ClassNameModel> getHandlers() {
    return handlers;
  }

  public void setHandlers(List<String> handlers) {
    if (isNull(handlers)) {
      this.handlers = new ArrayList<>();
    } else {
      this.handlers = handlers.stream()
                              .map(s -> new ClassNameModel(s))
                              .collect(Collectors.toList());
    }
  }

  public List<ClassNameModel> getActivateHandlers() {
    return activateHandlers;
  }

  public void setActivateHandlers(List<String> activateHandlers) {
    if (isNull(activateHandlers)) {
      this.activateHandlers = new ArrayList<>();
    } else {
      this.activateHandlers = activateHandlers.stream()
                                              .map(s -> new ClassNameModel(s))
                                              .collect(Collectors.toList());
    }
  }

  public List<ClassNameModel> getDeactivateHandlers() {
    return deactivateHandlers;
  }

  public void setDeactivateHandlers(List<String> deactivateHandlers) {
    if (isNull(deactivateHandlers)) {
      this.deactivateHandlers = new ArrayList<>();
    } else {
      this.deactivateHandlers = deactivateHandlers.stream()
                                                  .map(s -> new ClassNameModel(s))
                                                  .collect(Collectors.toList());
    }
  }

  public String getNavigationEvent() {
    return navigationEvent;
  }

  public boolean isNavigationEvent() {
    return "true".equals(navigationEvent);
  }

  public void setNavigationEvent(String navigationEvent) {
    this.navigationEvent = navigationEvent;
  }

  public String getPassive() {
    return passive;
  }

  public boolean isPassive() {
    return "true".equals(passive);
  }

  public void setPassive(String passive) {
    this.passive = passive;
  }

  public List<ParameterMetaData> getParameterMetaDataList() {
    return parameterMetaDataList;
  }

  public String getStartEvent() {
    return startEvent;
  }

  public boolean isStartEvent() {
    return "true".equals(startEvent);
  }

  public void setStartEvent(String startEvent) {
    this.startEvent = startEvent;
  }

  public String getInitHistory() {
    return initHistory;
  }

  public boolean isInitHistory() {
    return "true".equals(initHistory);
  }

  public void setInitHistory(String initHistory) {
    this.initHistory = initHistory;
  }

  public String getNotFoundHistory() {
    return notFoundHistory;
  }

  public boolean isNotFoundHistory() {
    return "true".equals(notFoundHistory);
  }

  public void setNotFoundHistory(String notFoundHistory) {
    this.notFoundHistory = notFoundHistory;
  }

  @Override
  public Properties createPropertes() {
    Properties properties = new Properties();
    properties.setProperty(EventMetaModel.KEY_EVENT_EVENT_INTERNAL_NAME,
                           this.eventInternalName);
    properties.setProperty(EventMetaModel.KEY_EVENT_NAME,
                           this.eventName);
    properties.setProperty(EventMetaModel.KEY_HISTORY_EVENT_NAME,
                           this.historyEventName);
    properties.setProperty(EventMetaModel.KEY_HISTORY_CONVERTER,
                           this.historyConverter.getClassName());
    properties.setProperty(EventMetaModel.KEY_START_EVENT,
                           this.startEvent);
    properties.setProperty(EventMetaModel.KEY_INIT_HISTORY,
                           this.initHistory);
    properties.setProperty(EventMetaModel.KEY_NOT_FOUND_HISTORY,
                           this.notFoundHistory);
    properties.setProperty(EventMetaModel.KEY_BINDINGS,
                                       this.bindings.stream()
                                                    .map(c -> c.getClassName())
                                                    .collect(Collectors.joining(",")));
    properties.setProperty(EventMetaModel.KEY_HANDLERS,
                                       this.handlers.stream()
                                                    .map(c -> c.getClassName())
                                                    .collect(Collectors.joining(",")));
    properties.setProperty(EventMetaModel.KEY_NAVIGATION_EVENT,
                           this.navigationEvent);
    properties.setProperty(EventMetaModel.KEY_PASSIVE,
                           this.passive);
    properties.setProperty(EventMetaModel.KEY_ACTIVATE_HANDELRS,
                                       this.activateHandlers.stream()
                                                            .map(c -> c.getClassName())
                                                            .collect(Collectors.joining(",")));
    properties.setProperty(EventMetaModel.KEY_DEACTIVATE_HANDELRS,
                                       this.deactivateHandlers.stream()
                                                              .map(c -> c.getClassName())
                                                              .collect(Collectors.joining(",")));
    if (this.parameterMetaDataList != null &&
        this.parameterMetaDataList.size() > 0) {
      properties.setProperty(EventMetaModel.KEY_PARAMETERS,
                                         this.parameterMetaDataList.stream()
                                                                   .map(p -> p.getName() + ProcessorConstants.TYPE_DELIMITER + p.getType()
                                                                                                                                .getClassName())
                                                                   .collect(Collectors.joining(",")));
    } else {
      properties.setProperty(EventMetaModel.KEY_PARAMETERS,
                             "");
    }
    return properties;
  }

  public class ParameterMetaData {

    private String         name;
    private ClassNameModel type;

    public ParameterMetaData(String name,
                             String type) {
      this.name = name;
      this.type = new ClassNameModel(type);
    }

    public String getName() {
      return name;
    }

    public ClassNameModel getType() {
      return type;
    }
  }
}
