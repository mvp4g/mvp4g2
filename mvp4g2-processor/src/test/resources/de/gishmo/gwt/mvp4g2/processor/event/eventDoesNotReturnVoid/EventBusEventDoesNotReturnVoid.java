package de.gishmo.gwt.mvp4g2.processor.event.eventDoesNotReturnVoid;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.EventBus;

@EventBus(shell = MockShellPresenter01.class)
public interface EventBusEventDoesNotReturnVoid
  extends IsEventBus {

  @Event(handlers = MockShellPresenter01.class)
  String doSomething(String oneAttribute);

}