package de.gishmo.gwt.mvp4g2.processor.eventbus.eventWithHandlerAttributeNotImplemented02;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.EventBus;

@EventBus(shell = MockShellPresenter01.class)
public interface EventBusEventWithHandlerAttributeNotImplemented
  extends IsEventBus {

  @Event(handlers = MockShellPresenter01.class)
  void doSomething(String oneAttribute);

}