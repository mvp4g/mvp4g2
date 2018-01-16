package de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute;

import de.gishmo.gwt.mvp4g2.core.eventbus.PresenterRegistration;
import de.gishmo.gwt.mvp4g2.core.internal.eventbus.AbstractEventBus;
import de.gishmo.gwt.mvp4g2.core.internal.eventbus.EventMetaData;
import de.gishmo.gwt.mvp4g2.core.internal.ui.HandlerMetaData;
import de.gishmo.gwt.mvp4g2.core.internal.ui.PresenterMetaData;
import de.gishmo.gwt.mvp4g2.core.ui.IsPresenter;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public final class EventTestHandlerNotInBindAndHandlersAttributeImpl extends AbstractEventBus<EventTestHandlerNotInBindAndHandlersAttribute> implements EventTestHandlerNotInBindAndHandlersAttribute {
  public EventTestHandlerNotInBindAndHandlersAttributeImpl() {
    super("de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockShellPresenter", false);
  }

  @Override
  public void loadDebugConfiguration() {
    super.setDebugEnable(false);
  }

  @Override
  public void loadFilterConfiguration() {
    super.setFiltersEnable(false);
  }

  @Override
  protected void loadEventMetaData() {
    //
    // ----------------------------------------------------------------------
    //
    // handle De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event02
    //
    super.putEventMetaData("event02", new De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event02());
    //
    // ----------------------------------------------------------------------
    //
    // handle De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event03
    //
    super.putEventMetaData("event03", new De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event03());
    //
    // ----------------------------------------------------------------------
    //
    // handle De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event01
    //
    super.putEventMetaData("event01", new De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event01());
  }

  @Override
  public final void event02() {
    int startLogDepth = AbstractEventBus.logDepth;
    try {
      execEvent02();
    } finally {
      AbstractEventBus.logDepth = startLogDepth;
    }
  }

  public final void execEvent02() {
    super.logEvent(++AbstractEventBus.logDepth, "event02");
    ++AbstractEventBus.logDepth;
    if (!super.filterEvent("event02")) {
      return;
    }
    EventMetaData<EventTestHandlerNotInBindAndHandlersAttribute> eventMetaData = super.getEventMetaData("event02");
    super.createAndBindView(eventMetaData);
    super.bind(eventMetaData);
    super.activate(eventMetaData);
    super.deactivate(eventMetaData);
    List<HandlerMetaData<?>> handlers = null;
    List<PresenterMetaData<?, ?>> presenters = null;
    List<String> listOfExecutedHandlers = new ArrayList<>();
    // handling: de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockShellPresenter
    presenters = this.presenterMetaDataMap.get("de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockShellPresenter");
    super.executePresenter(eventMetaData, presenters, null, new AbstractEventBus.ExecPresenter() {
      @Override
      public boolean execPass(EventMetaData<?> eventMetaData, PresenterMetaData<?, ?> metaData) {
        return metaData.getPresenter().pass(eventMetaData.getEventName());
      }

      @Override
      public void execEventHandlingMethod(PresenterMetaData<?, ?> metaData) {
        ((MockShellPresenter) metaData.getPresenter()).onEvent02();
      }
    }, false);
  }

  @Override
  public final void event03() {
    int startLogDepth = AbstractEventBus.logDepth;
    try {
      execEvent03();
    } finally {
      AbstractEventBus.logDepth = startLogDepth;
    }
  }

  public final void execEvent03() {
    super.logEvent(++AbstractEventBus.logDepth, "event03");
    ++AbstractEventBus.logDepth;
    if (!super.filterEvent("event03")) {
      return;
    }
    EventMetaData<EventTestHandlerNotInBindAndHandlersAttribute> eventMetaData = super.getEventMetaData("event03");
    super.createAndBindView(eventMetaData);
    super.bind(eventMetaData);
    super.activate(eventMetaData);
    super.deactivate(eventMetaData);
    List<HandlerMetaData<?>> handlers = null;
    List<PresenterMetaData<?, ?>> presenters = null;
    List<String> listOfExecutedHandlers = new ArrayList<>();
    // handling: de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockShellPresenter
    presenters = this.presenterMetaDataMap.get("de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockShellPresenter");
    super.executePresenter(eventMetaData, presenters, null, new AbstractEventBus.ExecPresenter() {
      @Override
      public boolean execPass(EventMetaData<?> eventMetaData, PresenterMetaData<?, ?> metaData) {
        return metaData.getPresenter().pass(eventMetaData.getEventName());
      }

      @Override
      public void execEventHandlingMethod(PresenterMetaData<?, ?> metaData) {
        ((MockShellPresenter) metaData.getPresenter()).onEvent03();
      }
    }, false);
  }

  @Override
  public final void event01() {
    int startLogDepth = AbstractEventBus.logDepth;
    try {
      execEvent01();
    } finally {
      AbstractEventBus.logDepth = startLogDepth;
    }
  }

  public final void execEvent01() {
    super.logEvent(++AbstractEventBus.logDepth, "event01");
    ++AbstractEventBus.logDepth;
    if (!super.filterEvent("event01")) {
      return;
    }
    EventMetaData<EventTestHandlerNotInBindAndHandlersAttribute> eventMetaData = super.getEventMetaData("event01");
    super.createAndBindView(eventMetaData);
    super.bind(eventMetaData);
    super.activate(eventMetaData);
    super.deactivate(eventMetaData);
    List<HandlerMetaData<?>> handlers = null;
    List<PresenterMetaData<?, ?>> presenters = null;
    List<String> listOfExecutedHandlers = new ArrayList<>();
  }

  @Override
  public final void fireStartEvent() {
  }

  @Override
  public final void fireInitHistoryEvent() {
    assert false : "no @InitHistory-event defined";
  }

  @Override
  public final void fireNotFoundHistoryEvent() {
    assert false : "no @NotFoundHistory-event defined";
  }

  @Override
  public PresenterRegistration addHandler(IsPresenter<?, ?> presenter) {
    return null;
  }

  @Override
  protected void loadEventHandlerMetaData() {
    //
    // ===>
    // handle de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockShellPresenter (Presenter)
    //
    De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData de_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData = new De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData();
    super.putPresenterMetaData("de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockShellPresenter", de_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData);
    de_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData.getPresenter().setEventBus(this);
    de_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData.getPresenter().setView(de_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData.getView());
    de_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData.getView().setPresenter(de_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData.getPresenter());
    //
    // ===>
    // handle de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockOneEventHandler (EventHandler)
    //
    De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockOneEventHandlerMetaData de_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockOneEventHandlerMetaData = new De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockOneEventHandlerMetaData();
    super.putHandlerMetaData("de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockOneEventHandler", de_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockOneEventHandlerMetaData);
    de_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockOneEventHandlerMetaData.getHandler().setEventBus(this);
    //
    // ===> add the handler to the handler list of the EventMetaData-class
  }
}
