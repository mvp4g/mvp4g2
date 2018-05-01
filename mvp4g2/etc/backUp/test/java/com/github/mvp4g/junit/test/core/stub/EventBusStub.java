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
package com.github.mvp4g.junit.test.core.stub;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.eventbus.PresenterRegistration;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Debug;
import com.github.mvp4g.mvp4g2.core.Mvp4g2RuntimeException;
import com.github.mvp4g.mvp4g2.core.internal.eventbus.AbstractEventBus;
import com.github.mvp4g.mvp4g2.core.internal.eventbus.DefaultMvp4g2Logger;
import com.github.mvp4g.mvp4g2.core.ui.IsPresenter;

public final class EventBusStub
  extends AbstractEventBus<IsEventBus>
  implements IsEventBus {

  public EventBusStub() {
    super("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.shell.ShellPresenter");
  }

  @Override
  public void loadDebugConfiguration() {
    super.setDebugEnable(true);
    super.setLogger(new DefaultMvp4g2Logger());
    super.setLogLevel(Debug.LogLevel.SIMPLE);
  }

  @Override
  public void loadFilterConfiguration() {
    super.setFiltersEnable(false);
  }

  @Override
  protected void loadEventMetaData() {
//    //
//    // ----------------------------------------------------------------------
//    //
//    // handle De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_setNavigation
//    //
//    super.putEventMetaData("setNavigation_pPp_elemental2_dom_Element",
//                           new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_setNavigation());
//    //
//    // ----------------------------------------------------------------------
//    //
//    // handle De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_setStatus
//    //
//    super.putEventMetaData("setStatus_pPp_java_lang_String",
//                           new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_setStatus());
//    //
//    // ----------------------------------------------------------------------
//    //
//    // handle De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_gotoList
//    //
//    super.putEventMetaData("gotoList_pPp_java_lang_String_pPp_java_lang_String",
//                           new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_gotoList());
//    //
//    // ----------------------------------------------------------------------
//    //
//    // handle De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_start
//    //
//    super.putEventMetaData("start",
//                           new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_start());
//    //
//    // ----------------------------------------------------------------------
//    //
//    // handle De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_gotoDetail
//    //
//    super.putEventMetaData("gotoDetail_pPp_long",
//                           new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_gotoDetail());
//    //
//    // ----------------------------------------------------------------------
//    //
//    // handle De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_setContent
//    //
//    super.putEventMetaData("setContent_pPp_elemental2_dom_Element",
//                           new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_setContent());
//    //
//    // ----------------------------------------------------------------------
//    //
//    // handle De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_initHistory
//    //
//    super.putEventMetaData("initHistory",
//                           new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_initHistory());
//    //
//    // ----------------------------------------------------------------------
//    //
//    // handle De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_gotoSearch
//    //
//    super.putEventMetaData("gotoSearch_pPp_java_lang_String_pPp_java_lang_String",
//                           new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_Mvp4g2SimpleApplicationEventBus_gotoSearch());
  }

  @Override
  protected void loadEventHandlerMetaData() {
//    //
//    // ===>
//    // handle de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.shell.ShellPresenter (Presenter)
//    //
//    De_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_shell_ShellPresenterMetaData de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_shell_ShellPresenterMetaData = new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_shell_ShellPresenterMetaData();
//    super.putPresenterMetaData("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.shell.ShellPresenter",
//                               de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_shell_ShellPresenterMetaData);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_shell_ShellPresenterMetaData.getPresenter()
//                                                                                         .setEventBus(this);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_shell_ShellPresenterMetaData.getPresenter()
//                                                                                         .setView(de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_shell_ShellPresenterMetaData.getView());
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_shell_ShellPresenterMetaData.getView()
//                                                                                         .setPresenter(de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_shell_ShellPresenterMetaData.getPresenter());
//    //
//    // ===>
//    // handle de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.navigation.NavigationPresenter (Presenter)
//    //
//    De_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_navigation_NavigationPresenterMetaData de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_navigation_NavigationPresenterMetaData = new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_navigation_NavigationPresenterMetaData();
//    super.putPresenterMetaData("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.navigation.NavigationPresenter",
//                               de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_navigation_NavigationPresenterMetaData);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_navigation_NavigationPresenterMetaData.getPresenter()
//                                                                                                   .setEventBus(this);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_navigation_NavigationPresenterMetaData.getPresenter()
//                                                                                                   .setView(de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_navigation_NavigationPresenterMetaData.getView());
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_navigation_NavigationPresenterMetaData.getView()
//                                                                                                   .setPresenter(de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_navigation_NavigationPresenterMetaData.getPresenter());
//    //
//    // ===>
//    // handle de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.list.ListPresenter (Presenter)
//    //
//    De_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_list_ListPresenterMetaData de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_list_ListPresenterMetaData = new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_list_ListPresenterMetaData();
//    super.putPresenterMetaData("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.list.ListPresenter",
//                               de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_list_ListPresenterMetaData);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_list_ListPresenterMetaData.getPresenter()
//                                                                                       .setEventBus(this);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_list_ListPresenterMetaData.getPresenter()
//                                                                                       .setView(de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_list_ListPresenterMetaData.getView());
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_list_ListPresenterMetaData.getView()
//                                                                                       .setPresenter(de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_list_ListPresenterMetaData.getPresenter());
//    //
//    // ===>
//    // handle de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.detail.DetailPresenter (Presenter)
//    //
//    De_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_detail_DetailPresenterMetaData de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_detail_DetailPresenterMetaData = new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_detail_DetailPresenterMetaData();
//    super.putPresenterMetaData("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.detail.DetailPresenter",
//                               de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_detail_DetailPresenterMetaData);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_detail_DetailPresenterMetaData.getPresenter()
//                                                                                           .setEventBus(this);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_detail_DetailPresenterMetaData.getPresenter()
//                                                                                           .setView(de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_detail_DetailPresenterMetaData.getView());
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_detail_DetailPresenterMetaData.getView()
//                                                                                           .setPresenter(de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_detail_DetailPresenterMetaData.getPresenter());
//    //
//    // ===>
//    // handle de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.search.SearchPresenter (Presenter)
//    //
//    De_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_search_SearchPresenterMetaData de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_search_SearchPresenterMetaData = new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_search_SearchPresenterMetaData();
//    super.putPresenterMetaData("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.search.SearchPresenter",
//                               de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_search_SearchPresenterMetaData);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_search_SearchPresenterMetaData.getPresenter()
//                                                                                           .setEventBus(this);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_search_SearchPresenterMetaData.getPresenter()
//                                                                                           .setView(de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_search_SearchPresenterMetaData.getView());
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_search_SearchPresenterMetaData.getView()
//                                                                                           .setPresenter(de_gishmo_gwt_example_mvp4g2_simpleapplication_client_ui_search_SearchPresenterMetaData.getPresenter());
//    //
//    // ===>
//    // handle de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler02 (EventHandler)
//    //
//    De_gishmo_gwt_example_mvp4g2_simpleapplication_client_handler_SimpleApplicationHandler02MetaData de_gishmo_gwt_example_mvp4g2_simpleapplication_client_handler_SimpleApplicationHandler02MetaData = new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_handler_SimpleApplicationHandler02MetaData();
//    super.putHandlerMetaData("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler02",
//                             de_gishmo_gwt_example_mvp4g2_simpleapplication_client_handler_SimpleApplicationHandler02MetaData);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_handler_SimpleApplicationHandler02MetaData.getHandler()
//                                                                                                    .setEventBus(this);
//    //
//    // ===>
//    // handle de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01 (EventHandler)
//    //
//    De_gishmo_gwt_example_mvp4g2_simpleapplication_client_handler_SimpleApplicationHandler01MetaData de_gishmo_gwt_example_mvp4g2_simpleapplication_client_handler_SimpleApplicationHandler01MetaData = new De_gishmo_gwt_example_mvp4g2_simpleapplication_client_handler_SimpleApplicationHandler01MetaData();
//    super.putHandlerMetaData("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01",
//                             de_gishmo_gwt_example_mvp4g2_simpleapplication_client_handler_SimpleApplicationHandler01MetaData);
//    de_gishmo_gwt_example_mvp4g2_simpleapplication_client_handler_SimpleApplicationHandler01MetaData.getHandler()
//                                                                                                    .setEventBus(this);
//    //
//    // ===> add the handler to the handler list of the EventMetaData-class
//    super.getEventMetaData("setNavigation_pPp_elemental2_dom_Element")
//         .addHandler("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01");
//    super.getEventMetaData("gotoDetail_pPp_long")
//         .addHandler("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01");
//    super.getEventMetaData("gotoList_pPp_java_lang_String_pPp_java_lang_String")
//         .addHandler("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01");
//    super.getEventMetaData("gotoSearch_pPp_java_lang_String_pPp_java_lang_String")
//         .addHandler("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01");
  }

  @Override
  public final void fireStartEvent() {
    this.start();
  }

  //  @Override
  public final void start() {
    int startLogDepth = AbstractEventBus.logDepth;
    try {
      execStart();
    } finally {
      AbstractEventBus.logDepth = startLogDepth;
    }
  }

  public final void execStart() {
    super.logEvent(++AbstractEventBus.logDepth,
                   "start");
//    ++AbstractEventBus.logDepth;
//    if (!super.filterEvent("start")) {
//      return;
//    }
//    EventMetaData<Mvp4g2SimpleApplicationEventBus> eventMetaData = super.getEventMetaData("start");
//    super.createAndBindView(eventMetaData);
//    super.bind(eventMetaData);
//    super.activate(eventMetaData);
//    super.deactivate(eventMetaData);
//    List<HandlerMetaData<?>> handlers = null;
//    List<PresenterMetaData<?, ?>> presenters = null;
//    List<String> listOfExecutedHandlers = new ArrayList<>();
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler02
//    handlers = this.handlerMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler02");
//    super.executeHandler(eventMetaData,
//                         handlers,
//                         null,
//                         new AbstractEventBus.ExecHandler() {
//                           @Override
//                           public boolean execPass(EventMetaData<?> eventMetaData,
//                                                   HandlerMetaData<?> metaData) {
//                             return metaData.getHandler()
//                                            .pass(eventMetaData.getEventName());
//                           }
//
//                           @Override
//                           public void execEventHandlingMethod(HandlerMetaData<?> metaData) {
//                             ((SimpleApplicationHandler02) metaData.getHandler()).onStart();
//                           }
//                         },
//                         false);
  }

  @Override
  public final void fireInitHistoryEvent() {
    this.initHistory();
  }

  public final void initHistory() {
    int startLogDepth = AbstractEventBus.logDepth;
    try {
      execInitHistory();
    } finally {
      AbstractEventBus.logDepth = startLogDepth;
    }
  }

  public final void execInitHistory() {
//    super.logEvent(++AbstractEventBus.logDepth,
//                   "initHistory");
//    ++AbstractEventBus.logDepth;
//    if (!super.filterEvent("initHistory")) {
//      return;
//    }
//    EventMetaData<Mvp4g2SimpleApplicationEventBus> eventMetaData = super.getEventMetaData("initHistory");
//    super.createAndBindView(eventMetaData);
//    super.bind(eventMetaData);
//    super.activate(eventMetaData);
//    super.deactivate(eventMetaData);
//    List<HandlerMetaData<?>> handlers = null;
//    List<PresenterMetaData<?, ?>> presenters = null;
//    List<String> listOfExecutedHandlers = new ArrayList<>();
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.search.SearchPresenter
//    presenters = this.presenterMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.search.SearchPresenter");
//    super.executePresenter(eventMetaData,
//                           presenters,
//                           null,
//                           new AbstractEventBus.ExecPresenter() {
//                             @Override
//                             public boolean execPass(EventMetaData<?> eventMetaData,
//                                                     PresenterMetaData<?, ?> metaData) {
//                               return metaData.getPresenter()
//                                              .pass(eventMetaData.getEventName());
//                             }
//
//                             @Override
//                             public void execEventHandlingMethod(PresenterMetaData<?, ?> metaData) {
//                               ((SearchPresenter) metaData.getPresenter()).onInitHistory();
//                             }
//                           },
//                           false);
  }

  @Override
  public final void fireNotFoundHistoryEvent() {
    this.initHistory();
  }

//  @Override
//  public final void setNavigation(final Element element) {
//    int startLogDepth = AbstractEventBus.logDepth;
//    try {
//      execSetNavigation(element);
//    } finally {
//      AbstractEventBus.logDepth = startLogDepth;
//    }
//  }
//
//  public final void execSetNavigation(final Element element) {
//    super.logEvent(++AbstractEventBus.logDepth,
//                   "setNavigation",
//                   element);
//    ++AbstractEventBus.logDepth;
//    if (!super.filterEvent("setNavigation",
//                           element)) {
//      return;
//    }
//    EventMetaData<Mvp4g2SimpleApplicationEventBus> eventMetaData = super.getEventMetaData("setNavigation_pPp_elemental2_dom_Element");
//    super.createAndBindView(eventMetaData);
//    super.bind(eventMetaData,
//               element);
//    super.activate(eventMetaData);
//    super.deactivate(eventMetaData);
//    List<HandlerMetaData<?>> handlers = null;
//    List<PresenterMetaData<?, ?>> presenters = null;
//    List<String> listOfExecutedHandlers = new ArrayList<>();
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.shell.ShellPresenter
//    presenters = this.presenterMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.shell.ShellPresenter");
//    super.executePresenter(eventMetaData,
//                           presenters,
//                           null,
//                           new AbstractEventBus.ExecPresenter() {
//                             @Override
//                             public boolean execPass(EventMetaData<?> eventMetaData,
//                                                     PresenterMetaData<?, ?> metaData) {
//                               return metaData.getPresenter()
//                                              .pass(eventMetaData.getEventName(),
//                                                    element);
//                             }
//
//                             @Override
//                             public void execEventHandlingMethod(PresenterMetaData<?, ?> metaData) {
//                               ((ShellPresenter) metaData.getPresenter()).onSetNavigation(element);
//                             }
//                           },
//                           false);
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01
//    handlers = this.handlerMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01");
//    super.executeHandler(eventMetaData,
//                         handlers,
//                         null,
//                         new AbstractEventBus.ExecHandler() {
//                           @Override
//                           public boolean execPass(EventMetaData<?> eventMetaData,
//                                                   HandlerMetaData<?> metaData) {
//                             return metaData.getHandler()
//                                            .pass(eventMetaData.getEventName(),
//                                                  element);
//                           }
//
//                           @Override
//                           public void execEventHandlingMethod(HandlerMetaData<?> metaData) {
//                             ((SimpleApplicationHandler01) metaData.getHandler()).onSetNavigation(element);
//                           }
//                         },
//                         false);
//  }
//
//  @Override
//  public final void setStatus(final String status) {
//    int startLogDepth = AbstractEventBus.logDepth;
//    try {
//      execSetStatus(status);
//    } finally {
//      AbstractEventBus.logDepth = startLogDepth;
//    }
//  }
//
//  public final void execSetStatus(final String status) {
//    super.logEvent(++AbstractEventBus.logDepth,
//                   "setStatus",
//                   status);
//    ++AbstractEventBus.logDepth;
//    if (!super.filterEvent("setStatus",
//                           status)) {
//      return;
//    }
//    EventMetaData<Mvp4g2SimpleApplicationEventBus> eventMetaData = super.getEventMetaData("setStatus_pPp_java_lang_String");
//    super.createAndBindView(eventMetaData);
//    super.bind(eventMetaData,
//               status);
//    super.activate(eventMetaData);
//    super.deactivate(eventMetaData);
//    List<HandlerMetaData<?>> handlers = null;
//    List<PresenterMetaData<?, ?>> presenters = null;
//    List<String> listOfExecutedHandlers = new ArrayList<>();
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.shell.ShellPresenter
//    presenters = this.presenterMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.shell.ShellPresenter");
//    super.executePresenter(eventMetaData,
//                           presenters,
//                           null,
//                           new AbstractEventBus.ExecPresenter() {
//                             @Override
//                             public boolean execPass(EventMetaData<?> eventMetaData,
//                                                     PresenterMetaData<?, ?> metaData) {
//                               return metaData.getPresenter()
//                                              .pass(eventMetaData.getEventName(),
//                                                    status);
//                             }
//
//                             @Override
//                             public void execEventHandlingMethod(PresenterMetaData<?, ?> metaData) {
//                               ((ShellPresenter) metaData.getPresenter()).onSetStatus(status);
//                             }
//                           },
//                           false);
//  }
//
//  @Override
//  public final void gotoList(final String searchName,
//                             final String searchOrt) {
//    int startLogDepth = AbstractEventBus.logDepth;
//    try {
//      super.logAskingForConfirmation(++AbstractEventBus.logDepth,
//                                     "gotoList",
//                                     searchName,
//                                     searchOrt);
//      super.placeService.confirmEvent(new NavigationEventCommand(this) {
//        @Override
//        public void execute() {
//          execGotoList(searchName,
//                       searchOrt);
//        }
//      });
//    } finally {
//      AbstractEventBus.logDepth = startLogDepth;
//    }
//  }
//
//  public final void execGotoList(final String searchName,
//                                 final String searchOrt) {
//    super.logEvent(++AbstractEventBus.logDepth,
//                   "gotoList",
//                   searchName,
//                   searchOrt);
//    ++AbstractEventBus.logDepth;
//    if (!super.filterEvent("gotoList",
//                           searchName,
//                           searchOrt)) {
//      return;
//    }
//    EventMetaData<Mvp4g2SimpleApplicationEventBus> eventMetaData = super.getEventMetaData("gotoList_pPp_java_lang_String_pPp_java_lang_String");
//    super.createAndBindView(eventMetaData);
//    super.bind(eventMetaData,
//               searchName,
//               searchOrt);
//    super.activate(eventMetaData);
//    super.deactivate(eventMetaData);
//    List<HandlerMetaData<?>> handlers = null;
//    List<PresenterMetaData<?, ?>> presenters = null;
//    List<String> listOfExecutedHandlers = new ArrayList<>();
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.list.ListPresenter
//    presenters = this.presenterMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.list.ListPresenter");
//    super.executePresenter(eventMetaData,
//                           presenters,
//                           listOfExecutedHandlers,
//                           new AbstractEventBus.ExecPresenter() {
//                             @Override
//                             public boolean execPass(EventMetaData<?> eventMetaData,
//                                                     PresenterMetaData<?, ?> metaData) {
//                               return metaData.getPresenter()
//                                              .pass(eventMetaData.getEventName(),
//                                                    searchName,
//                                                    searchOrt);
//                             }
//
//                             @Override
//                             public void execEventHandlingMethod(PresenterMetaData<?, ?> metaData) {
//                               ((ListPresenter) metaData.getPresenter()).onGotoList(searchName,
//                                                                                    searchOrt);
//                             }
//                           },
//                           true);
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01
//    handlers = this.handlerMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01");
//    super.executeHandler(eventMetaData,
//                         handlers,
//                         listOfExecutedHandlers,
//                         new AbstractEventBus.ExecHandler() {
//                           @Override
//                           public boolean execPass(EventMetaData<?> eventMetaData,
//                                                   HandlerMetaData<?> metaData) {
//                             return metaData.getHandler()
//                                            .pass(eventMetaData.getEventName(),
//                                                  searchName,
//                                                  searchOrt);
//                           }
//
//                           @Override
//                           public void execEventHandlingMethod(HandlerMetaData<?> metaData) {
//                             ((SimpleApplicationHandler01) metaData.getHandler()).onGotoList(searchName,
//                                                                                             searchOrt);
//                           }
//                         },
//                         true);
//    if (listOfExecutedHandlers.size() > 0) {
//      super.placeService.place("gotoList",
//                               ((de.gishmo.gwt.example.mvp4g2.simpleapplication.client.history.DefaultHistoryConverter) super.placeService.getHistoryConverter("gotoList")).convertToToken("gotoList",
//                                                                                                                                                                                           searchName,
//                                                                                                                                                                                           searchOrt),
//                               false);
//    }
//  }
//
//  @Override
//  public final void gotoDetail(final long id) {
//    int startLogDepth = AbstractEventBus.logDepth;
//    try {
//      super.logAskingForConfirmation(++AbstractEventBus.logDepth,
//                                     "gotoDetail",
//                                     id);
//      super.placeService.confirmEvent(new NavigationEventCommand(this) {
//        @Override
//        public void execute() {
//          execGotoDetail(id);
//        }
//      });
//    } finally {
//      AbstractEventBus.logDepth = startLogDepth;
//    }
//  }
//
//  public final void execGotoDetail(final long id) {
//    super.logEvent(++AbstractEventBus.logDepth,
//                   "gotoDetail",
//                   id);
//    ++AbstractEventBus.logDepth;
//    if (!super.filterEvent("gotoDetail",
//                           id)) {
//      return;
//    }
//    EventMetaData<Mvp4g2SimpleApplicationEventBus> eventMetaData = super.getEventMetaData("gotoDetail_pPp_long");
//    super.createAndBindView(eventMetaData);
//    super.bind(eventMetaData,
//               id);
//    super.activate(eventMetaData);
//    super.deactivate(eventMetaData);
//    List<HandlerMetaData<?>> handlers = null;
//    List<PresenterMetaData<?, ?>> presenters = null;
//    List<String> listOfExecutedHandlers = new ArrayList<>();
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.detail.DetailPresenter
//    presenters = this.presenterMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.detail.DetailPresenter");
//    super.executePresenter(eventMetaData,
//                           presenters,
//                           listOfExecutedHandlers,
//                           new AbstractEventBus.ExecPresenter() {
//                             @Override
//                             public boolean execPass(EventMetaData<?> eventMetaData,
//                                                     PresenterMetaData<?, ?> metaData) {
//                               return metaData.getPresenter()
//                                              .pass(eventMetaData.getEventName(),
//                                                    id);
//                             }
//
//                             @Override
//                             public void execEventHandlingMethod(PresenterMetaData<?, ?> metaData) {
//                               ((DetailPresenter) metaData.getPresenter()).onGotoDetail(id);
//                             }
//                           },
//                           true);
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01
//    handlers = this.handlerMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01");
//    super.executeHandler(eventMetaData,
//                         handlers,
//                         listOfExecutedHandlers,
//                         new AbstractEventBus.ExecHandler() {
//                           @Override
//                           public boolean execPass(EventMetaData<?> eventMetaData,
//                                                   HandlerMetaData<?> metaData) {
//                             return metaData.getHandler()
//                                            .pass(eventMetaData.getEventName(),
//                                                  id);
//                           }
//
//                           @Override
//                           public void execEventHandlingMethod(HandlerMetaData<?> metaData) {
//                             ((SimpleApplicationHandler01) metaData.getHandler()).onGotoDetail(id);
//                           }
//                         },
//                         true);
//    if (listOfExecutedHandlers.size() > 0) {
//      super.placeService.place("gotoDetail",
//                               ((de.gishmo.gwt.example.mvp4g2.simpleapplication.client.history.DefaultHistoryConverter) super.placeService.getHistoryConverter("gotoDetail")).convertToToken("gotoDetail",
//                                                                                                                                                                                             id),
//                               false);
//    }
//  }
//
//  @Override
//  public final void setContent(final Element element) {
//    int startLogDepth = AbstractEventBus.logDepth;
//    try {
//      execSetContent(element);
//    } finally {
//      AbstractEventBus.logDepth = startLogDepth;
//    }
//  }
//
//  public final void execSetContent(final Element element) {
//    super.logEvent(++AbstractEventBus.logDepth,
//                   "setContent",
//                   element);
//    ++AbstractEventBus.logDepth;
//    if (!super.filterEvent("setContent",
//                           element)) {
//      return;
//    }
//    EventMetaData<Mvp4g2SimpleApplicationEventBus> eventMetaData = super.getEventMetaData("setContent_pPp_elemental2_dom_Element");
//    super.createAndBindView(eventMetaData);
//    super.bind(eventMetaData,
//               element);
//    super.activate(eventMetaData);
//    super.deactivate(eventMetaData);
//    List<HandlerMetaData<?>> handlers = null;
//    List<PresenterMetaData<?, ?>> presenters = null;
//    List<String> listOfExecutedHandlers = new ArrayList<>();
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.shell.ShellPresenter
//    presenters = this.presenterMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.shell.ShellPresenter");
//    super.executePresenter(eventMetaData,
//                           presenters,
//                           null,
//                           new AbstractEventBus.ExecPresenter() {
//                             @Override
//                             public boolean execPass(EventMetaData<?> eventMetaData,
//                                                     PresenterMetaData<?, ?> metaData) {
//                               return metaData.getPresenter()
//                                              .pass(eventMetaData.getEventName(),
//                                                    element);
//                             }
//
//                             @Override
//                             public void execEventHandlingMethod(PresenterMetaData<?, ?> metaData) {
//                               ((ShellPresenter) metaData.getPresenter()).onSetContent(element);
//                             }
//                           },
//                           false);
//  }
//
//  @Override
//  public final void gotoSearch(final String searchName,
//                               final String searchOrt) {
//    int startLogDepth = AbstractEventBus.logDepth;
//    try {
//      super.logAskingForConfirmation(++AbstractEventBus.logDepth,
//                                     "gotoSearch",
//                                     searchName,
//                                     searchOrt);
//      super.placeService.confirmEvent(new NavigationEventCommand(this) {
//        @Override
//        public void execute() {
//          execGotoSearch(searchName,
//                         searchOrt);
//        }
//      });
//    } finally {
//      AbstractEventBus.logDepth = startLogDepth;
//    }
//  }
//
//  public final void execGotoSearch(final String searchName,
//                                   final String searchOrt) {
//    super.logEvent(++AbstractEventBus.logDepth,
//                   "gotoSearch",
//                   searchName,
//                   searchOrt);
//    ++AbstractEventBus.logDepth;
//    if (!super.filterEvent("gotoSearch",
//                           searchName,
//                           searchOrt)) {
//      return;
//    }
//    EventMetaData<Mvp4g2SimpleApplicationEventBus> eventMetaData = super.getEventMetaData("gotoSearch_pPp_java_lang_String_pPp_java_lang_String");
//    super.createAndBindView(eventMetaData);
//    super.bind(eventMetaData,
//               searchName,
//               searchOrt);
//    super.activate(eventMetaData);
//    super.deactivate(eventMetaData);
//    List<HandlerMetaData<?>> handlers = null;
//    List<PresenterMetaData<?, ?>> presenters = null;
//    List<String> listOfExecutedHandlers = new ArrayList<>();
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.search.SearchPresenter
//    presenters = this.presenterMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.ui.search.SearchPresenter");
//    super.executePresenter(eventMetaData,
//                           presenters,
//                           listOfExecutedHandlers,
//                           new AbstractEventBus.ExecPresenter() {
//                             @Override
//                             public boolean execPass(EventMetaData<?> eventMetaData,
//                                                     PresenterMetaData<?, ?> metaData) {
//                               return metaData.getPresenter()
//                                              .pass(eventMetaData.getEventName(),
//                                                    searchName,
//                                                    searchOrt);
//                             }
//
//                             @Override
//                             public void execEventHandlingMethod(PresenterMetaData<?, ?> metaData) {
//                               ((SearchPresenter) metaData.getPresenter()).onGotoSearch(searchName,
//                                                                                        searchOrt);
//                             }
//                           },
//                           true);
//    // handling: de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01
//    handlers = this.handlerMetaDataMap.get("de.gishmo.gwt.example.mvp4g2.simpleapplication.client.handler.SimpleApplicationHandler01");
//    super.executeHandler(eventMetaData,
//                         handlers,
//                         listOfExecutedHandlers,
//                         new AbstractEventBus.ExecHandler() {
//                           @Override
//                           public boolean execPass(EventMetaData<?> eventMetaData,
//                                                   HandlerMetaData<?> metaData) {
//                             return metaData.getHandler()
//                                            .pass(eventMetaData.getEventName(),
//                                                  searchName,
//                                                  searchOrt);
//                           }
//
//                           @Override
//                           public void execEventHandlingMethod(HandlerMetaData<?> metaData) {
//                             ((SimpleApplicationHandler01) metaData.getHandler()).onGotoSearch(searchName,
//                                                                                               searchOrt);
//                           }
//                         },
//                         true);
//    if (listOfExecutedHandlers.size() > 0) {
//      super.placeService.place("gotoSearch",
//                               ((de.gishmo.gwt.example.mvp4g2.simpleapplication.client.history.DefaultHistoryConverter) super.placeService.getHistoryConverter("gotoSearch")).convertToToken("gotoSearch",
//                                                                                                                                                                                             searchName,
//                                                                                                                                                                                             searchOrt),
//                               false);
//    }
//  }

  @Override
  public PresenterRegistration addHandler(IsPresenter<?, ?> presenter,
                                          boolean bind)
    throws
    Mvp4g2RuntimeException {
    throw new Mvp4g2RuntimeException(presenter.getClass()
                                              .getCanonicalName() + ": can not be used with the addHandler()-method, because it is not defined as multiple presenter!");
  }
}
