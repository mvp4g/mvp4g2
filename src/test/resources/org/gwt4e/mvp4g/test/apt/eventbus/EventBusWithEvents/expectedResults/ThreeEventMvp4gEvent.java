package org.gwt4e.mvp4g.test.apt.eventbus;

import org.gwt4e.mvp4g.client.AbstractEventBus;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.OneEventMvp4gEvent;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.ThreeEventMvp4gEvent;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.TwoEventMvp4gEvent;

public class EventBusWithEventsImpl extends AbstractEventBus implements EventBusWithEvents {
  @Override
  public final void oneEvent() {
    this.internalEventBus.fireEvent(new OneEventMvp4gEvent());
  }

  @Override
  public final void twoEvent(String arg0) {
    this.internalEventBus.fireEvent(new TwoEventMvp4gEvent(arg0));
  }

  @Override
  public final void threeEvent(String arg0, String arg1) {
    this.internalEventBus.fireEvent(new ThreeEventMvp4gEvent(arg0, arg1));
  }
}
