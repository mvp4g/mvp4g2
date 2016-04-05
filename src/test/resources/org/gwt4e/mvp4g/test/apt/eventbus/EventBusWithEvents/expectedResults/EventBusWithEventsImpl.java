package org.gwt4e.mvp4g.test.apt.eventbus.generated;

import java.lang.Override;
import java.lang.String;

import org.gwt4e.event.shared.Mvp4gInternalSimpleEventBus;
import org.gwt4e.mvp4g.client.event.AbstractMvp4gEventBus;
import org.gwt4e.mvp4g.test.apt.eventbus.EventBusWithEvents;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.OneEventMvp4gInternalEvent;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.ThreeEventMvp4gInternalEvent;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.TwoEventMvp4gInternalEvent;

public final class EventBusWithEventsImpl extends AbstractMvp4gEventBus implements EventBusWithEvents {
  public EventBusWithEventsImpl(String moduleName, Mvp4gInternalSimpleEventBus eventBus) {
    super(moduleName, eventBus);
  }

  @Override
  public void threeEvent(String arg0, String arg1) {
    this.internalEventBus.fireEvent(new ThreeEventMvp4gInternalEvent(arg0, arg1));
  }

  @Override
  public void twoEvent(String arg0) {
    this.internalEventBus.fireEvent(new TwoEventMvp4gInternalEvent(arg0));
  }

  @Override
  public void oneEvent() {
    this.internalEventBus.fireEvent(new OneEventMvp4gInternalEvent());
  }
}

