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
import java.util.stream.IntStream;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.eventbus.IsEventFilter;
import com.github.mvp4g.mvp4g2.core.eventbus.IsMvp4g2Logger;
import com.github.mvp4g.mvp4g2.core.eventbus.PresenterRegistration;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Debug;
import com.github.mvp4g.mvp4g2.core.history.IsHistoryConverter;
import com.github.mvp4g.mvp4g2.core.history.IsNavigationConfirmation;
import com.github.mvp4g.mvp4g2.core.internal.history.PlaceService;
import com.github.mvp4g.mvp4g2.core.internal.Mvp4g2InternalUse;
import com.github.mvp4g.mvp4g2.core.internal.ui.HandlerMetaData;
import com.github.mvp4g.mvp4g2.core.internal.ui.PresenterMetaData;
import com.github.mvp4g.mvp4g2.core.internal.ui.PresenterMetaDataRegistration;
import com.github.mvp4g.mvp4g2.core.ui.IsHandler;
import com.github.mvp4g.mvp4g2.core.ui.IsLazyReverseView;
import com.github.mvp4g.mvp4g2.core.ui.IsPresenter;
import com.github.mvp4g.mvp4g2.core.ui.IsShell;

import static java.util.Objects.isNull;

@Mvp4g2InternalUse
public abstract class AbstractEventBus<E extends IsEventBus>
  implements IsEventBus {

  private static final String DEBUG_EVENT = "DEBUG - EventBus -> event: >>";
  private static final String CLOSING     = "<<";

  public static int logDepth = -1;
  /* Map od EventMedaData (key: canonical class name, EventMetaData */
  protected Map<String, EventMetaData<E>>              eventMetaDataMap;
  /* Map od EventHandlerMedaData (key: canonical class name, List<HandlerMetaData> */
  /* value is list -> think on multiple presenter ... */
  protected Map<String, List<HandlerMetaData<?>>>      handlerMetaDataMap;
  /* Map od EventHandlerMedaData (key: canonical class name, List<HandlerMetaData> */
  /* value is list -> think on multiple presenter ... */
  protected Map<String, List<PresenterMetaData<?, ?>>> presenterMetaDataMap;
  /* presenter which creates the shell */
  protected String                                     shellPresenterCanonialName;
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
  private IsMvp4g2Logger                     logger;
  /* log level */
  private Debug.LogLevel                     logLevel;
  /* list history converters */
  private Map<String, IsHistoryConverter<E>> historyConverterMap;

  /* Filters enabled */
  private boolean filtersEnable = false;

  public AbstractEventBus(String shellPresenterCanonialName) {
    super();

    this.historyConverterMap = new HashMap<>();
    this.navigationConfirmationPresenter = null;

    this.shellPresenterCanonialName = shellPresenterCanonialName;

    this.handlerMetaDataMap = new HashMap<>();
    this.presenterMetaDataMap = new HashMap<>();
    this.eventMetaDataMap = new HashMap<>();
    this.eventFilters = new ArrayList<>();

    this.loadDebugConfiguration();
    this.loadFilterConfiguration();
    this.loadEventMetaData();
    this.loadEventHandlerMetaData();
  }

  protected abstract void loadDebugConfiguration();

  protected abstract void loadFilterConfiguration();

  protected abstract void loadEventMetaData();

  protected abstract void loadEventHandlerMetaData();

  protected final void bind(EventMetaData<E> eventMetaData,
                            Object... parameters) {
    for (String eventHandlerClassName : eventMetaData.getBindHandlerTypes()) {
      List<HandlerMetaData<?>> eventHandlers = this.handlerMetaDataMap.get(eventHandlerClassName);
      if (!isNull(eventHandlers) && eventHandlers.size() != 0) {
        for (HandlerMetaData<?> handlerMetaData : eventHandlers) {
          boolean activated = handlerMetaData.getHandler()
                                             .isActivated();
          activated = activated && handlerMetaData.getHandler()
                                                  .pass(eventMetaData.getEventName(),
                                                        parameters);
          if (activated) {
            if (!handlerMetaData.getHandler()
                                .isBound()) {
              if (!eventMetaData.isPassive()) {
                handlerMetaData.getHandler()
                               .setBound(true);
                handlerMetaData.getHandler()
                               .bind();
              }
            }
          }
        }
      } else {
        List<PresenterMetaData<?, ?>> presenters = this.presenterMetaDataMap.get(eventHandlerClassName);
        if (!isNull(presenters) && presenters.size() != 0) {
          for (PresenterMetaData<?, ?> presenterMetaData : presenters) {
            boolean activated = presenterMetaData.getPresenter()
                                                 .isActivated();
            activated = activated && presenterMetaData.getPresenter()
                                                      .pass(eventMetaData.getEventName(),
                                                            parameters);
            if (activated) {
              if (!presenterMetaData.getPresenter()
                                    .isBound()) {
                if (!eventMetaData.isPassive()) {
                  presenterMetaData.getPresenter()
                                   .setBound(true);
                  presenterMetaData.getPresenter()
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
    // create and bind view for the binding entries ...
    for (String eventHandlerClassName : eventMetaData.getBindHandlerTypes()) {
      this.doCreateAndBindView(eventMetaData.getEventName(),
                               eventHandlerClassName);
    }
    // create and bind view for the handler entries ...
    for (String eventHandlerClassName : eventMetaData.getHandlerTypes()) {
      this.doCreateAndBindView(eventMetaData.getEventName(),
                               eventHandlerClassName);
    }
    // TODO: Hier fehlt was ... by @EventHandler wird kein createAndBindView ausgeführt, da er keine Handler findet ...
  }

  private void doCreateAndBindView(String eventName,
                                   String eventHandlerClassName) {
    List<PresenterMetaData<?, ?>> presenters = this.presenterMetaDataMap.get(eventHandlerClassName);
    if (!isNull(presenters) && presenters.size() != 0) {
      presenters.stream()
                .filter(presenterHandlerMetaData -> !presenterHandlerMetaData.getView()
                                                                             .isBound())
                .forEachOrdered(presenterHandlerMetaData -> {
                  this.logHandlerBinding(AbstractEventBus.logDepth,
                                         eventName,
                                         eventHandlerClassName);
                  presenterHandlerMetaData.getView()
                                          .setBound(true);
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
        String sb = AbstractEventBus.DEBUG_EVENT + eventName + AbstractEventBus.CLOSING + " binding handler: >>" + handlerClassName + AbstractEventBus.CLOSING;
        this.log(sb,
                 logDepth);
      }
    }
  }

  //  private void doCreateAndBindView(String eventName,
  //                                   String eventHandlerClassName) {
  //    List<PresenterMetaData<?, ?>> presenters = this.presenterMetaDataMap.get(eventHandlerClassName);
  //    if (presenters != null && presenters.size() != 0) {
  //      presenters.stream()
  //                .filter(presenterHandlerMetaData -> !presenterHandlerMetaData.getView()
  //                                                                             .isBound())
  //                .forEachOrdered(presenterHandlerMetaData -> {
  //                  this.logHandlerBinding(AbstractEventBus.logDepth,
  //                                         eventName,
  //                                         eventHandlerClassName);
  //                  presenterHandlerMetaData.getView()
  //                                          .setBound(true);
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
      List<HandlerMetaData<?>> eventHandler = this.handlerMetaDataMap.get(eventHandlerClassName);
      if (!isNull(eventHandler) && eventHandler.size() != 0) {
        eventHandler.stream()
                    .forEachOrdered(eventHandlerMetaData -> {
                      this.logHandlerActivating(AbstractEventBus.logDepth,
                                                eventMetaData.getEventName(),
                                                eventHandlerClassName);
                      eventHandlerMetaData.getHandler()
                                          .setActivated(true);
                    });
        List<PresenterMetaData<?, ?>> presenters = this.presenterMetaDataMap.get(eventHandlerClassName);
        if (!isNull(presenters) && presenters.size() != 0) {
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
        String sb = AbstractEventBus.DEBUG_EVENT + eventName + AbstractEventBus.CLOSING + " activaiting handler: >>" + handlerClassName + AbstractEventBus.CLOSING;
        this.log(sb,
                 logDepth);
      }
    }
  }

  protected final void deactivate(EventMetaData<E> eventMetaData) {
    for (String eventHandlerClassName : eventMetaData.getDeactivateHandlerTypes()) {
      List<HandlerMetaData<?>> eventHandler = this.handlerMetaDataMap.get(eventHandlerClassName);
      if (!isNull(eventHandler) && eventHandler.size() != 0) {
        eventHandler.forEach(eventHandlerMetaData -> {
          this.logHandlerDeactivating(AbstractEventBus.logDepth,
                                      eventMetaData.getEventName(),
                                      eventHandlerClassName);
          eventHandlerMetaData.getHandler()
                              .setActivated(false);
        });
        List<PresenterMetaData<?, ?>> presenters = this.presenterMetaDataMap.get(eventHandlerClassName);
        if (!isNull(presenters) && presenters.size() != 0) {
          presenters.forEach(presenterHandlerMetaData -> {
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
        String sb = AbstractEventBus.DEBUG_EVENT + eventName + AbstractEventBus.CLOSING + " deactivating handler: >>" + handlerClassName + AbstractEventBus.CLOSING;
        this.log(sb,
                 logDepth);
      }
    }
  }

  public abstract void fireStartEvent();

  public abstract void fireInitHistoryEvent();

  public abstract void fireNotFoundHistoryEvent();

  public void setShell() {
    // no IsShellPresenter found! ==> error
    assert !isNull(this.presenterMetaDataMap.get(this.shellPresenterCanonialName)) : "there is no presenter which implements IsShellPresenter!";
    // more than one IsShellPresenter found! ==> error
    assert this.presenterMetaDataMap.get(this.shellPresenterCanonialName)
                                    .size() > 0 : "there is more than one presenter defined which implements IsShellPresenter!";
    PresenterMetaData<?, ?> presenter = this.presenterMetaDataMap.get(this.shellPresenterCanonialName)
                                                                 .get(0);
    presenter.getPresenter()
             .setBound(true);
    presenter.getPresenter()
             .bind();
    doCreateAndBindView("start",
                        this.shellPresenterCanonialName);
    ((IsShell) presenter.getPresenter()).setShell();
  }

  @Override
  public PresenterRegistration addHandler(IsPresenter<?, ?> presenter) {
    return addHandler(presenter,
                      true);
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
   * @see IsEventBus#addEventFilter(IsEventFilter)
   */
  @Override
  public void addEventFilter(IsEventFilter<? extends IsEventBus> filter) {
    eventFilters.add(filter);
    // in case we do not have a @Filters annotation inside the event bus,
    // we have to set filtersEnable!
    filtersEnable = true;
  }

  /*
   * (non-Javadoc)
   *
   * @see IsEventBus#removeEventFilter(cde.gishmo.gwt.mvp4g2.core.eventbus.IsEventFilter)
   */
  @Override
  public void removeEventFilter(IsEventFilter<? extends IsEventBus> filter) {
    eventFilters.remove(filter);
    // in case we doi not have a @Filters annotation inside the event bus,
    // we have to set filtersEnable!
    filtersEnable = eventFilters.size() > 0;
  }

  protected EventMetaData<E> getEventMetaData(String eventName) {
    return this.eventMetaDataMap.get(eventName);
  }

  protected void putEventMetaData(String eventName,
                                  EventMetaData<E> metaData) {
    eventMetaDataMap.put(eventName,
                         metaData);
    metaData.setEventBus(this);
  }

  protected <E extends IsHandler<?>> void putHandlerMetaData(String className,
                                                             HandlerMetaData<E> metaData) {
    List<HandlerMetaData<?>> handlerMetaDataList = this.handlerMetaDataMap.computeIfAbsent(className,
                                                                                           k -> new ArrayList<>());
    handlerMetaDataList.add(metaData);
  }

  protected <E extends IsPresenter<?, ?>, V extends IsLazyReverseView<?>> PresenterMetaDataRegistration putPresenterMetaData(String className,
                                                                                                                             PresenterMetaData<E, V> metaData) {
    List<PresenterMetaData<?, ?>> presenterMetaDataList = this.presenterMetaDataMap.computeIfAbsent(className,
                                                                                                    k -> new ArrayList<>());
    presenterMetaDataList.add(metaData);
    return () -> presenterMetaDataList.remove(metaData);
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
   * @param debugEnable true - is enable
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
      sb.append(AbstractEventBus.CLOSING)
        .append(" with parameters: ");
      IntStream.range(0,
                      parameters.length)
               .forEach(i -> {
                 sb.append(">>");
                 sb.append(isNull(parameters[i]) ? "null" : parameters[i].toString());
                 if (i < parameters.length - 1) {
                   sb.append(AbstractEventBus.CLOSING)
                     .append(", ");
                 } else {
                   sb.append(AbstractEventBus.CLOSING);
                 }
               });
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
   * @return true - event can be executed
   */
  protected boolean filterEvent(String eventName,
                                Object... params) {
    boolean executeEvent = true;
    if (filtersEnable) {
      executeEvent = doFilterEvent(eventName,
                                   params);
      logEventFilter(AbstractEventBus.logDepth,
                     eventName,
                     executeEvent);
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
    @SuppressWarnings("rawtypes") IsEventFilter eventFilter;
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

  protected void logEventFilter(int logDepth,
                                String eventName,
                                boolean pass) {
    if (debugEnable) {
      StringBuilder sb = new StringBuilder();
      sb.append("DEBUG - EventBus -> handling event: >>")
        .append(eventName)
        .append(pass ? AbstractEventBus.CLOSING + " passed filter(s)" : AbstractEventBus.CLOSING + "  did not pass filter(s)");
      this.log(sb.toString(),
               logDepth);
    }
  }

  /**
   * set the filter state
   *
   * @param filtersEnable true - is enable
   */
  protected void setFiltersEnable(boolean filtersEnable) {
    this.filtersEnable = filtersEnable;
  }

  protected <E extends IsEventBus> void executeHandler(EventMetaData<E> eventMetaData,
                                                       List<HandlerMetaData<?>> eventHandlers,
                                                       List<String> listOfExecutedHandlers,
                                                       ExecHandler execHandler,
                                                       boolean addHandler) {
    if (!isNull(eventHandlers) && eventHandlers.size() != 0) {
      for (HandlerMetaData<?> metaData : eventHandlers) {
        boolean activated = metaData.getHandler()
                                    .isActivated();
        boolean pass = execHandler.execPass(eventMetaData,
                                            metaData);
        if (activated && pass) {
          logHandler(AbstractEventBus.logDepth,
                     eventMetaData.getEventName(),
                     metaData.getCanonicalName());
          metaData.getHandler()
                  .onBeforeEvent(eventMetaData.getEventName());
          execHandler.execEventHandlingMethod(metaData);
          if (!isNull(listOfExecutedHandlers) && addHandler) {
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
        sb.append(AbstractEventBus.DEBUG_EVENT)
          .append(eventName)
          .append(AbstractEventBus.CLOSING)
          .append(" handled by: >>")
          .append(handlerClassName)
          .append(AbstractEventBus.CLOSING);
        this.log(sb.toString(),
                 logDepth);
      }
    }
  }

  protected void logAddHandler(int logDepth,
                               String presenterClass,
                               boolean bind) {
    if (debugEnable) {
      if (Debug.LogLevel.DETAILED.equals(logLevel)) {
        StringBuilder sb = new StringBuilder();
        sb.append("DEBUG - EventBus -> add presenter: >>")
          .append(presenterClass)
          .append(AbstractEventBus.CLOSING)
          .append(" to event bus!");
        if (bind) {
          sb.append("  ==> view is created and bound");
        } else {
          sb.append("  ==> view is not created and not bound");
        }
        this.log(sb.toString(),
                 logDepth);
      }
    }
  }

  protected <E extends IsEventBus> void executePresenter(EventMetaData<E> eventMetaData,
                                                         List<PresenterMetaData<?, ?>> eventHandler,
                                                         List<String> listOfExecutedHandlers,
                                                         ExecPresenter execPresenter,
                                                         boolean addHandler) {
    if (!isNull(eventHandler) && eventHandler.size() != 0) {
      for (PresenterMetaData<?, ?> metaData : eventHandler) {
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
          if (!isNull(listOfExecutedHandlers) && addHandler) {
            listOfExecutedHandlers.add(metaData.getCanonicalName());
          }
        }
      }
    }
  }

  public void add(String historyConverterClassName,
                  IsHistoryConverter<E> historyConverter) {
    this.historyConverterMap.put(historyConverterClassName,
                                 historyConverter);
  }

  public IsHistoryConverter<E> getHistoryConverter(String historyConverterClassName) {
    return this.historyConverterMap.get(historyConverterClassName);

  }

  public interface ExecHandler {

    boolean execPass(EventMetaData<?> eventMetaData,
                     HandlerMetaData<?> metaData);

    void execEventHandlingMethod(HandlerMetaData<?> metaData);

  }

  public interface ExecPresenter {

    boolean execPass(EventMetaData<?> eventMetaData,
                     PresenterMetaData<?, ?> metaData);

    void execEventHandlingMethod(PresenterMetaData<?, ?> metaData);

  }
}
