package de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation;

import de.gishmo.gwt.mvp4g2.core.internal.eventbus.EventMetaData;

public final class De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_StartEventTestEventBusWithOneStartAnnotation_start
  extends EventMetaData<StartEventTestEventBusWithOneStartAnnotation> {
  public De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_StartEventTestEventBusWithOneStartAnnotation_start() {
    super("start",
          "start",
          null,
          null,
          null,
          false,
          false);
    super.addHandler("de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter");
  }
}
