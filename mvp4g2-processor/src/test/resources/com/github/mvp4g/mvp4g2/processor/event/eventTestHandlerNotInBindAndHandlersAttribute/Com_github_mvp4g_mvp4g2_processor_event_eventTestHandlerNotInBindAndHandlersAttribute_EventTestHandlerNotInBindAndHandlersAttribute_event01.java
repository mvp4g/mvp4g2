package com.github.mvp4g.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute;

import com.github.mvp4g.mvp4g2.core.internal.eventbus.EventMetaData;

public final class Com_github_mvp4g_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event01
  extends EventMetaData<EventTestHandlerNotInBindAndHandlersAttribute> {
  public Com_github_mvp4g_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event01() {
    super("event01",
          "event01",
          null,
          null,
          null,
          false,
          false);
    super.addBindHandler("com.github.mvp4g.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockShellPresenter");
  }
}
