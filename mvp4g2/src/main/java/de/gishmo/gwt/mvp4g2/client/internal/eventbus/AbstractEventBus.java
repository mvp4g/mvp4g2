/*
 * Copyright (C) 2016 Frank Hossfeld <frank.hossfeld@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.gishmo.gwt.mvp4g2.client.internal.eventbus;

import de.gishmo.gwt.mvp4g2.client.annotation.internal.ForInternalUseOnly;
import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventFilter;
import de.gishmo.gwt.mvp4g2.client.eventbus.IsMvp4g2Logger;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.client.history.IsNavigationConfirmation;
import de.gishmo.gwt.mvp4g2.client.history.PlaceService;
import de.gishmo.gwt.mvp4g2.client.ui.IsEventHandler;
import de.gishmo.gwt.mvp4g2.client.ui.IsLazyReverseView;
import de.gishmo.gwt.mvp4g2.client.ui.IsPresenter;
import de.gishmo.gwt.mvp4g2.client.ui.IsShell;
import de.gishmo.gwt.mvp4g2.client.internal.ui.EventHandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.internal.ui.PresenterHandlerMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ForInternalUseOnly
public abstract class AbstractEventBus<E extends IsEventBus>
  implements IsEventBus {

  public static int logDepth = -1;
  /* Map od EventMedaData (key: canonical class name, EventMetaData */
  protected Map<String, EventMetaData<E>>                     eventMetaDataMap;
  /* Map od EventHandlerMedaData (key: canonical class name, List<EventHandlerMetaData> */
  /* value is list -> think on multiple presenter ... */
  protected Map<String, List<EventHandlerMetaData<?>>>        eventHandlerMetaDataMap;
  /* Map od EventHandlerMedaData (key: canonical class name, List<EventHandlerMetaData> */
  /* value is list -> think on multiple presenter ... */
  protected Map<String, List<PresenterHandlerMetaData<?, ?>>> presenterHandlerMetaDataMap;
  /* presenter which creates the shell */
  protected String                                            shellPresenterCanonialName;
  /* flag, if the start event is already fired */
  protected boolean startEventFired = false;
  /* the place service */
  protected PlaceService<? extends IsEventBus>        placeService;
  /* list of filters */
  protected List<IsEventFilter<? extends IsEventBus>> eventFilters;
  /* current navigation confirmation handler */
  private   IsNavigationConfirmation                  navigationConfirmationPresenter;
  /* debug enabled? */
  private boolean debugEnable = false;
  /* logger */
  private IsMvp4g2Logger logger;
  /* log level */
  private Debug.LogLevel logLevel;

  /* Filters enabled */
  private boolean filtersEnable = false;

  /* flag if we have to check history token at the start of the application */
  private boolean historyOnStart;

  public AbstractEventBus(String shellPresenterCanonialName,
                          boolean historyOnStart) {
    super();

    this.navigationConfirmationPresenter = null;
    this.historyOnStart = historyOnStart;

    this.shellPresenterCanonialName = shellPresenterCanonialName;

    this.eventHandlerMetaDataMap = new HashMap<>();
    this.presenterHandlerMetaDataMap = new HashMap<>();
    this.eventMetaDataMap = new HashMap<>();
    this.eventFilters = new ArrayList<>();

    this.loadDebugConfiguration();
    this.loadFilterConfiguration();
    this.loadEventHandlerMetaData();
    this.loadEventMetaData();
  }

  protected abstract void loadDebugConfiguration();

  protected abstract void loadFilterConfiguration();

  protected abstract void loadEventHandlerMetaData();

  protected abstract void loadEventMetaData();

  protected final void bind(EventMetaData<E> eventMetaData,
                            Object... parameters) {
    for (String eventHandlerClassName : eventMetaData.getBindHandlerTypes()) {
      List<EventHandlerMetaData<?>> eventHandlers = this.eventHandlerMetaDataMap.get(eventHandlerClassName);
      if (eventHandlers != null && eventHandlers.size() != 0) {
        for (EventHandlerMetaData<?> eventHandlerMetaData : eventHandlers) {
          boolean activated = eventHandlerMetaData.getEventHandler()
                                                  .isActivated();
          activated = activated && eventHandlerMetaData.getEventHandler()
                                                       .pass(eventMetaData.getEventName(),
                                                             parameters);
          if (activated) {
            if (!eventHandlerMetaData.getEventHandler()
                                     .isBinded()) {
              if (!eventMetaData.isPassive()) {
                eventHandlerMetaData.getEventHandler()
                                    .setBinded(true);
                eventHandlerMetaData.getEventHandler()
                                    .bind();
              }
            }
          }
        }
      } else {
        List<PresenterHandlerMetaData<?, ?>> presenters = this.presenterHandlerMetaDataMap.get(eventHandlerClassName);
        if (presenters != null && presenters.size() != 0) {
          for (PresenterHandlerMetaData<?, ?> presenterHandlerMetaData : presenters) {
            boolean activated = presenterHandlerMetaData.getPresenter()
                                                        .isActivated();
            activated = activated && presenterHandlerMetaData.getPresenter()
                                                             .pass(eventMetaData.getEventName(),
                                                                   parameters);
            if (activated) {
              if (!presenterHandlerMetaData.getPresenter()
                                           .isBinded()) {
                if (!eventMetaData.isPassive()) {
                  presenterHandlerMetaData.getPresenter()
                                          .setBinded(true);
                  presenterHandlerMetaData.getPresenter()
                                          .bind();
                }
              }
            }
          }
        }
      }
    }
  }

  protected final void createAndBindView(EventMetaData<E> eventMetaData) {
    for (String eventHandlerClassName : eventMetaData.getBindHandlerTypes()) {
      this.doCreateAndBindView(eventMetaData.getEventName(),
                               eventHandlerClassName);
    }
    for (String eventHandlerClassName : eventMetaData.getHandlerTypes()) {
      this.doCreateAndBindView(eventMetaData.getEventName(),
                               eventHandlerClassName);
    }
  }

  private void doCreateAndBindView(String eventName,
                                   String eventHandlerClassName) {
    List<PresenterHandlerMetaData<?, ?>> presenters = this.presenterHandlerMetaDataMap.get(eventHandlerClassName);
    if (presenters != null && presenters.size() != 0) {
      presenters.stream()
                .filter(presenterHandlerMetaData -> !presenterHandlerMetaData.getView()
                                                                             .isBinded())
                .forEachOrdered(presenterHandlerMetaData -> {
                  this.logHandlerBinding(AbstractEventBus.logDepth,
                                         eventName,
                                         eventHandlerClassName);
                  presenterHandlerMetaData.getView()
                                          .setBinded(true);
                  presenterHandlerMetaData.getView()
                                          .createView();
                  presenterHandlerMetaData.getView()
                                          .bind();
                });
    }
  }

  protected void logHandlerBinding(int logDepth,
                                   String eventName,
                                   String handlerClassName) {
    if (debugEnable) {
      if (Debug.LogLevel.DETAILED.equals(logLevel)) {
        String sb = "DEBUG - EventBus -> event: >>" +
                    eventName +
                    "<< binding handler: >>" +
                    handlerClassName +
                    "<<";
        this.log(sb,
                 logDepth);
      }
    }
  }

//  private void doCreateAndBindView(String eventName,
//                                   String eventHandlerClassName) {
//    List<PresenterHandlerMetaData<?, ?>> presenters = this.presenterHandlerMetaDataMap.get(eventHandlerClassName);
//    if (presenters != null && presenters.size() != 0) {
//      presenters.stream()
//                .filter(presenterHandlerMetaData -> !presenterHandlerMetaData.getView()
//                                                                             .isBinded())
//                .forEachOrdered(presenterHandlerMetaData -> {
//                  this.logHandlerBinding(AbstractEventBus.logDepth,
//                                         eventName,
//                                         eventHandlerClassName);
//                  presenterHandlerMetaData.getView()
//                                          .setBinded(true);
//                  presenterHandlerMetaData.getView()
//                                          .createView();
//                  presenterHandlerMetaData.getView()
//                                          .bind();
//                });
//
//    }
//  }

  protected void log(String message,
                     int depth) {
    this.logger.log(message,
                    depth);
  }

  protected final void activate(EventMetaData<E> eventMetaData) {
    for (String eventHandlerClassName : eventMetaData.getActivateHandlerTypes()) {
      List<EventHandlerMetaData<?>> eventHandler = this.eventHandlerMetaDataMap.get(eventHandlerClassName);
      if (eventHandler != null && eventHandler.size() != 0) {
        eventHandler.stream()
                    .forEachOrdered(eventHandlerMetaData -> {
                      this.logHandlerActivating(AbstractEventBus.logDepth,
                                                eventMetaData.getEventName(),
                                                eventHandlerClassName);
                      eventHandlerMetaData.getEventHandler()
                                          .setActivated(true);
                    });
        List<PresenterHandlerMetaData<?, ?>> presenters = this.presenterHandlerMetaDataMap.get(eventHandlerClassName);
        if (presenters != null && presenters.size() != 0) {
          presenters.stream()
                    .forEachOrdered(presenterHandlerMetaData -> {
                      this.logHandlerActivating(AbstractEventBus.logDepth,
                                                eventMetaData.getEventName(),
                                                eventHandlerClassName);
                      presenterHandlerMetaData.getPresenter()
                                              .setActivated(true);
                    });
        }
      }
    }
  }

  protected void logHandlerActivating(int logDepth,
                                      String eventName,
                                      String handlerClassName) {
    if (debugEnable) {
      if (Debug.LogLevel.DETAILED.equals(logLevel)) {
        String sb = "DEBUG - EventBus -> event: >>" +
                    eventName +
                    "<< activaiting handler: >>" +
                    handlerClassName +
                    "<<";
        this.log(sb,
                 logDepth);
      }
    }
  }

  protected final void deactivate(EventMetaData<E> eventMetaData) {
    for (String eventHandlerClassName : eventMetaData.getDeactivateHandlerTypes()) {
      List<EventHandlerMetaData<?>> eventHandler = this.eventHandlerMetaDataMap.get(eventHandlerClassName);
      if (eventHandler != null && eventHandler.size() != 0) {
        eventHandler.stream()
                    .forEachOrdered(eventHandlerMetaData -> {
                      this.logHandlerDeactivating(AbstractEventBus.logDepth,
                                                  eventMetaData.getEventName(),
                                                  eventHandlerClassName);
                      eventHandlerMetaData.getEventHandler()
                                          .setActivated(false);
                    });
        List<PresenterHandlerMetaData<?, ?>> presenters = this.presenterHandlerMetaDataMap.get(eventHandlerClassName);
        if (presenters != null && presenters.size() != 0) {
          presenters.stream()
                    .forEachOrdered(presenterHandlerMetaData -> {
                      this.logHandlerDeactivating(AbstractEventBus.logDepth,
                                                  eventMetaData.getEventName(),
                                                  eventHandlerClassName);
                      presenterHandlerMetaData.getPresenter()
                                              .setActivated(false);
                    });
        }
      }
    }
  }

  protected void logHandlerDeactivating(int logDepth,
                                        String eventName,
                                        String handlerClassName) {
    if (debugEnable) {
      if (Debug.LogLevel.DETAILED.equals(logLevel)) {
        String sb = "DEBUG - EventBus -> event: >>" +
                    eventName +
                    "<< deactivating handler: >>" +
                    handlerClassName +
                    "<<";
        this.log(sb,
                 logDepth);
      }
    }
  }

  public abstract void fireStartEvent();

  public abstract void fireInitHistoryEvent();

  public abstract void fireNotFoundHistoryEvent();

  public boolean hasHistoryOnStart() {
    return historyOnStart;
  }

  public void setShell() {
    // no IsShellPresenter found! ==> error
    assert this.presenterHandlerMetaDataMap.get(this.shellPresenterCanonialName) != null : "there is no presenter which implements IsShellPresenter!";
    // more than one IsShellPresenter found! ==> error
    assert this.presenterHandlerMetaDataMap.get(this.shellPresenterCanonialName)
                                           .size() > 0 : "there is more than one presenter defined which implements IsShellPresenter!";
    PresenterHandlerMetaData<?, ?> presenter = this.presenterHandlerMetaDataMap.get(this.shellPresenterCanonialName)
                                                                               .get(0);
    presenter.getPresenter()
             .setBinded(true);
    presenter.getPresenter()
             .bind();
    doCreateAndBindView("start",
                        this.shellPresenterCanonialName);
    ((IsShell) presenter.getPresenter()).setShell();
  }

  public IsNavigationConfirmation getNavigationConfirmationPresenter() {
    return navigationConfirmationPresenter;
  }

  public void setNavigationConfirmation(IsNavigationConfirmation navigationConfirmationPresenter) {
    this.navigationConfirmationPresenter = navigationConfirmationPresenter;
  }

  @Override
  public void setPlaceService(PlaceService<? extends IsEventBus> placeService) {
    this.placeService = placeService;
    // now, set up event place service
    this.setUpPlaceService();
  }

  private void setUpPlaceService() {
    for (String eventName : this.eventMetaDataMap.keySet()) {
      this.placeService.addConverter(this.eventMetaDataMap.get(eventName));
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus#addEventFilter(de.gishmo.gwt.mvp4g2.client.eventbus.IsEventFilter)
   */
  @Override
  public void addEventFilter(IsEventFilter<? extends IsEventBus> filter) {
    eventFilters.add(filter);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus#removeEventFilter(cde.gishmo.gwt.mvp4g2.client.eventbus.IsEventFilter)
   */
  @Override
  public void removeEventFilter(IsEventFilter<? extends IsEventBus> filter) {
    eventFilters.remove(filter);
  }

  protected EventMetaData<E> getEventMetaData(String eventName) {
    return this.eventMetaDataMap.get(eventName);
  }

  protected void putEventMetaData(String eventName,
                                  EventMetaData<E> metaData) {
    eventMetaDataMap.put(eventName,
                         metaData);
  }

  protected <E extends IsEventHandler<?>> void putEventHandlerMetaData(String className,
                                                                       EventHandlerMetaData<E> metaData) {
    List<EventHandlerMetaData<?>> eventHandlerMetaDataList = this.eventHandlerMetaDataMap.computeIfAbsent(className,
                                                                                                          k -> new ArrayList<>());
    eventHandlerMetaDataList.add(metaData);
  }

  protected <E extends IsPresenter<?, ?>, V extends IsLazyReverseView<?>> void putPresenterHandlerMetaData(String className,
                                                                                                           PresenterHandlerMetaData<E, V> metaData) {
    List<PresenterHandlerMetaData<?, ?>> presenterHandlerMetaDataList = this.presenterHandlerMetaDataMap.computeIfAbsent(className,
                                                                                                                         k -> new ArrayList<>());
    presenterHandlerMetaDataList.add(metaData);
  }

  protected <E extends IsPresenter<?, ?>, V extends IsLazyReverseView<?>> void removePresenterHandlerMetaData(String className,
                                                                                                              PresenterHandlerMetaData<E, V> metaData) {
    List<EventHandlerMetaData<?>> eventHandlerMetaDataList = this.eventHandlerMetaDataMap.get(className);
    if (eventHandlerMetaDataList != null) {
      eventHandlerMetaDataList.remove(metaData);
    }
  }

  /**
   * Get the logger for the applicaiton
   *
   * @return logger
   */
  protected IsMvp4g2Logger getLogger() {
    return logger;
  }

  /**
   * Sets the logger
   *
   * @param logger logger
   */
  protected void setLogger(IsMvp4g2Logger logger) {
    this.logger = logger;
  }

  /**
   * gets the log level
   *
   * @return the selected log level
   */
  public Debug.LogLevel getLogLevel() {
    return logLevel;
  }

  /**
   * Set the log level
   *
   * @param logLevel the new log level
   */
  protected void setLogLevel(Debug.LogLevel logLevel) {
    this.logLevel = logLevel;
  }

  /**
   * set the debug state
   *
   * @param debugEnable true ->  is enable
   */
  protected void setDebugEnable(boolean debugEnable) {
    this.debugEnable = debugEnable;
  }

  protected void logEvent(int logDepth,
                          String eventName,
                          Object... parameters) {
    if (debugEnable) {
      StringBuilder sb = new StringBuilder();
      sb.append("DEBUG - EventBus -> handling event: >>")
        .append(eventName);
      this.prepareParametersForLog(sb,
                                   parameters);
      this.log(sb.toString(),
               logDepth);
    }
  }

  private void prepareParametersForLog(StringBuilder sb,
                                       Object... parameters) {
    if (parameters.length > 0) {
      sb.append("<< with parameters: ");
      for (int i = 0; i < parameters.length; i++) {
        sb.append(">>");
        sb.append(parameters[i] == null ? "null" : parameters[i].toString());
        if (i < parameters.length - 1) {
          sb.append("<<, ");
        } else {
          sb.append("<<");
        }
      }
    }
  }

  protected void logEventFilter(int logDepth,
                                String eventName) {
    if (debugEnable) {
      StringBuilder sb = new StringBuilder();
      sb.append("DEBUG - EventBus -> handling event: >>")
        .append(eventName)
        .append("<< did not pass filter(s)!");
      this.log(sb.toString(),
               logDepth);
    }
  }

  protected void logAskingForConfirmation(int logDepth,
                                          String eventName,
                                          Object... parameters) {
    if (debugEnable) {
      StringBuilder sb = new StringBuilder();
      sb.append("DEBUG - Asking for confirmation: event: >>")
        .append(eventName);
      this.prepareParametersForLog(sb,
                                   parameters);
      this.log(sb.toString(),
               logDepth);
    }
  }

  /**
   * If filtering is enabled, executes event filters associated with this event bus.
   *
   * @param eventName event's name
   * @param params    event parameters for this event
   */
  protected boolean filterEvent(String eventName,
                                Object... params) {
    boolean executeEvent = true;
    if (filtersEnable) {
      executeEvent = doFilterEvent(eventName,
                                   params);
    }
//    if (changeFilteringEnabledForNextOne) {
//      filteringEnabled = !filteringEnabled;
//      changeFilteringEnabledForNextOne = false;
//    }
    return executeEvent;
  }

  /**
   * Performs the actual filtering by calling each associated event filter in turn. If any event
   * filter returns false, then the event will be canceled.
   *
   * @param eventName event's name
   * @param params    event parameters for this event
   */
  @SuppressWarnings("unchecked")
  private boolean doFilterEvent(String eventName,
                                Object[] params) {
    int filterCount = eventFilters.size();
    @SuppressWarnings("rawtypes")
    IsEventFilter eventFilter;
    for (int i = 0; i < filterCount; i++) {
      eventFilter = eventFilters.get(i);
      if (!eventFilter.filterEvent(this,
                                   eventName,
                                   params)) {
        return false;
      }
    }
    return true;
  }

  /**
   * set the filter state
   *
   * @param filtersEnable true ->  is enable
   */
  protected void setFiltersEnable(boolean filtersEnable) {
    this.filtersEnable = filtersEnable;
  }

  protected <E extends IsEventBus> void executeEventHandler(EventMetaData<E> eventMetaData,
                                                            List<EventHandlerMetaData<?>> eventHandlers,
                                                            List<String> listOfExecutedHandlers,
                                                            ExecEventHandler execEventHandler,
                                                            boolean addHandler) {
    if (eventHandlers != null && eventHandlers.size() != 0) {
      for (EventHandlerMetaData<?> metaData : eventHandlers) {
        boolean activated = metaData.getEventHandler()
                                    .isActivated();
        boolean pass = execEventHandler.execPass(eventMetaData,
                                                 metaData);
        if (activated && pass) {
          logHandler(AbstractEventBus.logDepth,
                     eventMetaData.getEventName(),
                     metaData.getCanonicalName());
          metaData.getEventHandler()
                  .onBeforeEvent(eventMetaData.getEventName());
          execEventHandler.execEventHandlingMethod(metaData);
          if (listOfExecutedHandlers != null && addHandler) {
            listOfExecutedHandlers.add(metaData.getCanonicalName());
          }
        }
      }
    }
  }

  protected void logHandler(int logDepth,
                            String eventName,
                            String handlerClassName) {
    if (debugEnable) {
      if (Debug.LogLevel.DETAILED.equals(logLevel)) {
        StringBuilder sb = new StringBuilder();
        sb.append("DEBUG - EventBus -> event: >>")
          .append(eventName)
          .append("<< handled by: >>")
          .append(handlerClassName)
          .append("<<");
        this.log(sb.toString(),
                 logDepth);
      }
    }
  }

  protected <E extends IsEventBus> void executePresenter(EventMetaData<E> eventMetaData,
                                                         List<PresenterHandlerMetaData<?, ?>> eventHandler,
                                                         List<String> listOfExecutedHandlers,
                                                         ExecPresenter execPresenter,
                                                         boolean addHandler) {
    if (eventHandler != null && eventHandler.size() != 0) {
      for (PresenterHandlerMetaData<?, ?> metaData : eventHandler) {
        boolean activated = metaData.getPresenter()
                                    .isActivated();
        boolean pass = execPresenter.execPass(eventMetaData,
                                              metaData);
        if (activated && pass) {
          logHandler(AbstractEventBus.logDepth,
                     eventMetaData.getEventName(),
                     metaData.getCanonicalName());
          metaData.getPresenter()
                  .onBeforeEvent(eventMetaData.getEventName());
          execPresenter.execEventHandlingMethod(metaData);
          if (listOfExecutedHandlers != null && addHandler) {
            listOfExecutedHandlers.add(metaData.getCanonicalName());
          }
        }
      }
    }
  }

  public interface ExecEventHandler {

    boolean execPass(EventMetaData<?> eventMetaData,
                     EventHandlerMetaData<?> metaData);

    void execEventHandlingMethod(EventHandlerMetaData<?> metaData);

  }

  public interface ExecPresenter {

    boolean execPass(EventMetaData<?> eventMetaData,
                     PresenterHandlerMetaData<?, ?> metaData);

    void execEventHandlingMethod(PresenterHandlerMetaData<?, ?> metaData);

  }
}
