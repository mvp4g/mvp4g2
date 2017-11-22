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

package de.gishmo.gwt.mvp4g2.client.eventbus;

import com.google.gwt.core.client.GWT;
import de.gishmo.gwt.mvp4g2.client.annotation.internal.ForInternalUseOnly;
import de.gishmo.gwt.mvp4g2.client.eventbus.internal.EventMetaData;
import de.gishmo.gwt.mvp4g2.client.ui.*;
import de.gishmo.gwt.mvp4g2.client.ui.internal.EventHandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.ui.internal.PresenterHandlerMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ForInternalUseOnly
public abstract class AbstractEventBus
  implements IsEventBus {

  /* Map od EventHandlerMedaData (key: canonical class name, List<EventHandlerMetaData> */
  /* value is list -> think on multiple presenter ... */
  protected Map<String, List<EventHandlerMetaData<?>>>        eventHandlerMetaDataMap;
  /* Map od EventHandlerMedaData (key: canonical class name, List<EventHandlerMetaData> */
  /* value is list -> think on multiple presenter ... */
  protected Map<String, List<PresenterHandlerMetaData<?, ?>>> presenterHandlerMetaDataMap;
  /* Map od EventMedaData (key: canonical class name, EventMetaData */
  protected Map<String, EventMetaData>                        eventMetaDataMap;
  /* Map od EventHandlerMedaData (key: canonical class name, Instance of EventHandler */
  /* value is list -> think on multiple presenter ... */
//  protected Map<String, IsEventHandler<? extends IsEventBus>> eventHandlerMap;
  //  private Map<String, List<EventConsumer>> eventHandlerMap;
//  /* list of events whith binding presenters */
//  /* <"event name". ""id of event consumer"> */
//  private Map<String, List<String>> bindingMap;
//  /* <"event name". ""id of event consumer"> */
//  private Map<String, List<String>> eventMap;
  /* presenter which creates the shell */
  protected String                                            shellPresenterCanonialName;
  /* flag, if the start event is already fired */
  protected boolean startEventFired = false;
  /* current navigation confirmation handler */
  private INavigationConfirmation navigationConfirmation;


  public AbstractEventBus(String shellPresenterCanonialName) {
    super();

    this.navigationConfirmation = null;

    this.shellPresenterCanonialName = shellPresenterCanonialName;

    this.eventHandlerMetaDataMap = new HashMap<>();
    this.presenterHandlerMetaDataMap = new HashMap<>();
    this.eventMetaDataMap = new HashMap<>();

    this.loadEventHandlerMetaData();
    this.loadEventMetaData();
  }

  protected abstract void loadEventHandlerMetaData();

  protected abstract void loadEventMetaData();

  protected final void bind(EventMetaData eventMetaData,
                            Object... parameters) {
    GWT.debugger();
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

  protected final void createAndBindView(EventMetaData eventMetaData) {
    GWT.debugger();
    for (String eventHandlerClassName : eventMetaData.getBindHandlerTypes()) {
      this.doCreateAndBindView(eventHandlerClassName);
    }
    for (String eventHandlerClassName : eventMetaData.getHandlerTypes()) {
      this.doCreateAndBindView(eventHandlerClassName);
    }
  }

  private void doCreateAndBindView(String eventHandlerClassName) {
    List<PresenterHandlerMetaData<?, ?>> presenters = this.presenterHandlerMetaDataMap.get(eventHandlerClassName);
    if (presenters != null && presenters.size() != 0) {
      for (PresenterHandlerMetaData<?, ?> presenterHandlerMetaData : presenters) {
        if (!presenterHandlerMetaData.getView()
                                     .isBinded()) {
          presenterHandlerMetaData.getView()
                                  .setBinded(true);
          presenterHandlerMetaData.getView()
                                  .createView();
          presenterHandlerMetaData.getView()
                                  .bind();
        }
      }
    }
  }

  public abstract void fireStartEvent();

  public void setShell() {
    // no IsShellPresenter found! ==> error
    assert this.presenterHandlerMetaDataMap.get(this.shellPresenterCanonialName)
                                           .get(0) != null : "there is no presenter which implements IsShellPresenter!";
    // more than one IsShellPresenter found! ==> error
    assert this.presenterHandlerMetaDataMap.get(this.shellPresenterCanonialName)
                                           .size() > 0 : "there is more than one presenter defined which implements IsShellPresenter!";
    PresenterHandlerMetaData<?, ?> presenter = this.presenterHandlerMetaDataMap.get(this.shellPresenterCanonialName)
                                                                               .get(0);
    presenter.getPresenter()
             .setBinded(true);
    presenter.getPresenter()
             .bind();
    doCreateAndBindView(this.shellPresenterCanonialName);
    ((IsShell) presenter.getPresenter()).setShell();
  }

  protected EventMetaData getEventMetaData(String eventName) {
    return this.eventMetaDataMap.get(eventName);
  }

  protected void putEventMetaData(String eventName,
                                  EventMetaData metaData) {
    eventMetaDataMap.put(eventName,
                         metaData);
  }

  protected <E extends IsEventHandler<?>> void putEventHandlerMetaData(String className,
                                                                       EventHandlerMetaData<E> metaData) {
    List<EventHandlerMetaData<?>> eventHandlerMetaDataList = this.eventHandlerMetaDataMap.computeIfAbsent(className,
                                                                                                          k -> new ArrayList<>());
    eventHandlerMetaDataList.add(metaData);
  }

  protected <E extends IsPresenter<?, ?>, V extends IsLazyReverseView<?>> void putPresenterHandlerMetaData(String
                                                                                                             className,
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

  public void setNavigationConfirmation(INavigationConfirmation navigationConfirmation) {
    this.navigationConfirmation = navigationConfirmation;
  }
}
