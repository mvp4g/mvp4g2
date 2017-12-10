package de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute;

import de.gishmo.gwt.mvp4g2.client.internal.eventbus.EventMetaData;

public final class De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event03 extends EventMetaData<EventTestHandlerNotInBindAndHandlersAttribute> {
  public De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event03() {
    super("event03", null, null, null, false, false);
    super.addHandler("de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockShellPresenter");
    super.addBindHandler("de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockOneEventHandler");
  }
}
