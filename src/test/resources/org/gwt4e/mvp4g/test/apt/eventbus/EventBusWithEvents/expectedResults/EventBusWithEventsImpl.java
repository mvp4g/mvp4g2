package org.gwt4e.mvp4g.test.apt.eventbus;

import java.lang.Override;
import java.lang.String;
import org.gwt4e.mvp4g.client.AbstractMvp4gEventBus;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.OneEventMvp4gEvent;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.ThreeEventMvp4gEvent;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.TwoEventMvp4gEvent;

public class EventBusWithEventsImpl extends AbstractMvp4gEventBus
  implements EventBusWithEvents {
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
