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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import com.github.mvp4g.mvp4g2.processor.model.intern.ClassNameModel;
import com.github.mvp4g.mvp4g2.processor.model.intern.IsMetaModel;

public class EventBusMetaModel
  implements IsMetaModel {

  private static final String KEY_EVENTBUS = "eventBus";
  private static final String KEY_SHELL    = "shell";
  private static final String KEY_EVENTS   = "events";

  private static final String KEY_HAS_DEBUG_ANNOTATION = "hasDebugAnnotation";
  private static final String KEY_DEBUG_LOG_LEVEL      = "debugLogLevel";
  private static final String KEY_DEBUG_LOGGER         = "debugLogger";

  private static final String KEY_HAS_FILTERS_ANNOTATION = "hasFiltersAnnotation";
  private static final String KEY_FILTERS                = "filters";


  private ClassNameModel eventBus;
  private ClassNameModel shell;
  private List<String> events = new ArrayList<>();


  private String         hasDebugAnnotation;
  private String         debugLogLevel;
  private ClassNameModel debugLogger;


  private String hasFiltersAnnotation;
  private List<ClassNameModel> filters = new ArrayList<>();


  private Map<String, EventMetaModel> eventMetaModels = new HashMap<>();


  public EventBusMetaModel(Properties properties) {
    this.eventBus = new ClassNameModel(properties.getProperty(EventBusMetaModel.KEY_EVENTBUS));
    this.shell = new ClassNameModel(properties.getProperty(EventBusMetaModel.KEY_SHELL));
    this.events = Arrays.stream(properties.getProperty(EventBusMetaModel.KEY_EVENTS)
                                          .split("\\s*,\\s*"))
                        .collect(Collectors.toList());

    this.hasDebugAnnotation = properties.getProperty(EventBusMetaModel.KEY_HAS_DEBUG_ANNOTATION);
    this.debugLogger = new ClassNameModel(properties.getProperty(EventBusMetaModel.KEY_DEBUG_LOGGER));
    this.debugLogLevel = properties.getProperty(EventBusMetaModel.KEY_DEBUG_LOG_LEVEL);

    this.hasFiltersAnnotation = properties.getProperty(EventBusMetaModel.KEY_HAS_FILTERS_ANNOTATION);
    if (isEmptyOrNull(properties.getProperty(EventBusMetaModel.KEY_FILTERS))) {
      this.filters = new ArrayList<>();
    } else {
      this.filters = Arrays.stream(properties.getProperty(EventBusMetaModel.KEY_FILTERS)
                                             .split("\\s*,\\s*"))
                           .map(ClassNameModel::new)
                           .collect(Collectors.toList());
    }
  }

  public EventBusMetaModel(String eventBus,
                           String shell) {
    this.eventBus = new ClassNameModel(eventBus);
    this.shell = new ClassNameModel(shell);
  }

  public void add(EventMetaModel eventMetaModel) {
    this.eventMetaModels.put(eventMetaModel.getEventInternalName(),
                             eventMetaModel);
    if (!this.events.contains(eventMetaModel.getEventInternalName())) {
      this.events.add(eventMetaModel.getEventInternalName());
    }
  }

  private boolean isEmptyOrNull(String value) {
    return value == null || value.isEmpty();
  }

  public Collection<String> getEventMetaModelKeys() {
    return this.eventMetaModels.keySet();
  }

  public Collection<EventMetaModel> getEventMetaModels() {
    return this.eventMetaModels.values();
  }

  public EventMetaModel getEventMetaModel(String eventInternalName) {
    return this.eventMetaModels.get(eventInternalName);
  }

  public ClassNameModel getEventBus() {
    return eventBus;
  }

  public ClassNameModel getShell() {
    return shell;
  }

  public String getDebugLogLevel() {
    return debugLogLevel;
  }

  public void setDebugLogLevel(String debugLogLevel) {
    this.debugLogLevel = debugLogLevel;
  }

  public ClassNameModel getDebugLogger() {
    return debugLogger;
  }

  public void setDebugLogger(String debugLogger) {
    this.debugLogger = new ClassNameModel(debugLogger);
  }

  public boolean hasDebugAnnotation() {
    return "true".equals(hasDebugAnnotation);
  }

  public void setHasDebugAnnotation(String hasDebugAnnotation) {
    this.hasDebugAnnotation = hasDebugAnnotation;
  }

  public boolean hasFiltersAnnotation() {
    return "true".equals(hasFiltersAnnotation);
  }

  public void setHasFiltersAnnotation(String hasFiltersAnnotation) {
    this.hasFiltersAnnotation = hasFiltersAnnotation;
  }

  public List<ClassNameModel> getFilters() {
    return filters;
  }

  public void setFilters(List<String> filters) {
    filters.stream()
           .forEach(s -> this.filters.add(new ClassNameModel(s)));
  }

  public List<String> getEvents() {
    return events;
  }

  @Override
  public Properties createPropertes() {
    Properties properties = new Properties();
    properties.setProperty(EventBusMetaModel.KEY_EVENTBUS,
                           this.eventBus.getClassName());
    properties.setProperty(EventBusMetaModel.KEY_SHELL,
                           this.shell.getClassName());
    properties.setProperty(EventBusMetaModel.KEY_EVENTS,
                           String.join(",",
                                       String.join(",",
                                                   this.eventMetaModels.keySet()
                                                                       .stream()
                                                                       .collect(Collectors.toCollection(ArrayList::new)))));
    properties.setProperty(EventBusMetaModel.KEY_HAS_DEBUG_ANNOTATION,
                           this.hasDebugAnnotation);
    properties.setProperty(EventBusMetaModel.KEY_DEBUG_LOGGER,
                           this.debugLogger.getClassName());
    properties.setProperty(EventBusMetaModel.KEY_DEBUG_LOG_LEVEL,
                           this.debugLogLevel);

    properties.setProperty(EventBusMetaModel.KEY_HAS_FILTERS_ANNOTATION,
                           this.hasFiltersAnnotation);
    properties.setProperty(EventBusMetaModel.KEY_FILTERS,
                           String.join(",",
                                       filters.stream()
                                              .map(c -> c.getClassName())
                                              .collect(Collectors.toCollection(ArrayList::new))));
    return properties;
  }
}
