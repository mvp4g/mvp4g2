package de.gishmo.gwt.mvp4g2.processor.eventhandler.eventhandlerWithHanderlsAttributeAndEventHandlerAnnotation;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.EventBus;

@EventBus(shell = MockShellPresenter01.class)
public interface EventBusEventhandlerWithHanderlsAttributeAndEventHandlerAnnotation
  extends IsEventBus {

  @Event(handlers = MockShellPresenter01.class)
  void doSomething01(String oneAttribute);

}