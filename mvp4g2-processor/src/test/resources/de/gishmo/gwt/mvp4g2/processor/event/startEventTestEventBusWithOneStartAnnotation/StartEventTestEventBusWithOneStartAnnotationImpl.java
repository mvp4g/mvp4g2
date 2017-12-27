package de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation;

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
    List<HandlerMetaData<?>> handlers = null;
    List<PresenterMetaData<?, ?>> presenters = null;
    List<String> listOfExecutedHandlers = new ArrayList<>();
    // handling: de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter
    presenters = this.presenterMetaDataMap.get("de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter");
    super.executePresenter(eventMetaData, presenters, null, new AbstractEventBus.ExecPresenter() {
      @Override
      public boolean execPass(EventMetaData<?> eventMetaData, PresenterMetaData<?, ?> metaData) {
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

  @Override
  public PresenterRegistration addHandler(IsPresenter<?, ?> presenter) {
    return null;
  }

  @Override
  protected void loadEventHandlerMetaData() {
    //
    // ===>
    // handle de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter (Presenter)
    //
    De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData = new De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData();
    super.putPresenterMetaData("de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter", de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData);
    de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getPresenter().setEventBus(this);
    de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getPresenter().setView(de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getView());
    de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getView().setPresenter(de_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.getPresenter());
    //
    // ===> add the handler to the handler list of the EventMetaData-class
  }
}
