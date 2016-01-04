package org.gwt4e.mvp4g.test.apt.eventbus;

import java.lang.Override;
import java.lang.String;
import org.gwt4e.mvp4g.client.AbstractMvp4gEventBus;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.OneEventMvp4gInternalEvent;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.ThreeEventMvp4gInternalEvent;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.TwoEventMvp4gInternalEvent;

public class EventBusWithEventsImpl extends AbstractMvp4gEventBus implements EventBusWithEvents {
  @Override
  public final void oneEvent() {
    this.internalEventBus.fireEvent(new OneEventMvp4gInternalEvent());
  }

  @Override
  public final void twoEvent(String arg0) {
    this.internalEventBus.fireEvent(new TwoEventMvp4gInternalEvent(arg0));
  }

  @Override
  public final void threeEvent(String arg0, String arg1) {
    this.internalEventBus.fireEvent(new ThreeEventMvp4gInternalEvent(arg0, arg1));
  }
}
