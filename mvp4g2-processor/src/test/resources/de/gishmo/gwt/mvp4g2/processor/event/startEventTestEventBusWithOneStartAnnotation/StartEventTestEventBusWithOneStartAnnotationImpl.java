
package de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation;

import de.gishmo.gwt.mvp4g2.client.eventbus.PresenterRegistration;
import de.gishmo.gwt.mvp4g2.client.internal.eventbus.AbstractEventBus;
import de.gishmo.gwt.mvp4g2.client.internal.eventbus.EventMetaData;
import de.gishmo.gwt.mvp4g2.client.internal.ui.EventHandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.internal.ui.PresenterMetaData;
import de.gishmo.gwt.mvp4g2.client.ui.IsPresenter;
import java.lang.Override;
import java.util.List;

public final class StartEventTestEventBusWithOneStartAnnotationImpl extends AbstractEventBus<StartEventTestEventBusWithOneStartAnnotation> implements StartEventTestEventBusWithOneStartAnnotation {
  public StartEventTestEventBusWithOneStartAnnotationImpl() {
    super("de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter", false);
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
    // handle De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_StartEventTestEventBusWithOneStartAnnotation_start
    //
    super.putEventMetaData("start", new De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_StartEventTestEventBusWithOneStartAnnotation_start());
  }

  @Override
  protected void loadEventHandlerMetaData() {
    //
    // ----------------------------------------------------------------------
    //
    // handle de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter (Presenter)
    //
    de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData = new de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData();
    if (!de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.isMultiple()) {
      super.putPresenterHandlerMetaData("de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter", de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData);
      de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getPresenter().setEventBus(this);
      de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getPresenter().setView(de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getView());
      de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getView().setPresenter(de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getPresenter());
    }
  }

  @Override
  public PresenterRegistration addHandler(IsPresenter<?, ?> presenter) {
    final de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData = new de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData();
    if (de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.isMultiple()) {
      super.putPresenterHandlerMetaData("de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter", de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData);
      de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getPresenter().setEventBus(this);
      de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getPresenter().setView(de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getView());
      de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getView().setPresenter(de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getPresenter());
      return new PresenterRegistration() {
        @Override
        public void remove() {
          removePresenterHandlerMetaData("de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter", de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData);
        }
      };
    } else {
      assert false : "de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter: is not annotated with @Presenter(...,multiple = true)";
    }
    return null;
  }

  @Override
  public final void start() {
    int startLogDepth = AbstractEventBus.logDepth;
    try {
      execStart();
    } finally {
      AbstractEventBus.logDepth = startLogDepth;
    }
  }

  public final void execStart() {
    super.logEvent(++AbstractEventBus.logDepth, "start");
    ++AbstractEventBus.logDepth;
    if (!super.filterEvent("start")) {
      super.logEventFilter(AbstractEventBus.logDepth, "start");
      return;
    }
    EventMetaData<StartEventTestEventBusWithOneStartAnnotation> eventMetaData = super.getEventMetaData("start");
    super.createAndBindView(eventMetaData);
    super.bind(eventMetaData);
    super.activate(eventMetaData);
    super.deactivate(eventMetaData);
    List<EventHandlerMetaData<?>> eventHandlers = null;
    List<PresenterMetaData<?, ?>> presenters = null;
    // handling: de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter
    eventHandlers = this.eventHandlerMetaDataMap.get("de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter");
    super.executeEventHandler(eventMetaData, eventHandlers, null, new AbstractEventBus.ExecEventHandler() {
      @Override
      public boolean execPass(EventMetaData<?> eventMetaData, EventHandlerMetaData<?> metaData) {
        return metaData.getEventHandler().pass(eventMetaData.getEventName());
      }

      @Override
      public void execEventHandlingMethod(EventHandlerMetaData<?> metaData) {
        ((MockShellPresenter) metaData.getEventHandler()).onStart();
      }
    }, false);
    presenters = this.presenterHandlerMetaDataMap.get("de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter");
    super.executePresenter(eventMetaData, presenters, null, new AbstractEventBus.ExecPresenter() {
      @Override
      public boolean execPass(EventMetaData<?> eventMetaData,
                              PresenterMetaData<?, ?> metaData) {
        return metaData.getPresenter().pass(eventMetaData.getEventName());
      }

      @Override
      public void execEventHandlingMethod(PresenterMetaData<?, ?> metaData) {
        ((MockShellPresenter) metaData.getPresenter()).onStart();
      }
    }, false);
  }

  @Override
  public final void fireStartEvent() {
    this.start();
  }

  @Override
  public final void fireInitHistoryEvent() {
    assert false : "no @InitHistory-event defined";
  }

  @Override
  public final void fireNotFoundHistoryEvent() {
    assert false : "no @NotFoundHistory-event defined";
  }
}
