package de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterWithWrongImplementation03;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.EventBus;

@EventBus(shell = MockShellPresenter01.class)
public interface EventBusHandlerWithNotImplementedEvent
  extends IsEventBus {

  @Event
  void doSomething01(String oneAttribute);

  @Event
  void doSomething02(String oneAttribute);

}