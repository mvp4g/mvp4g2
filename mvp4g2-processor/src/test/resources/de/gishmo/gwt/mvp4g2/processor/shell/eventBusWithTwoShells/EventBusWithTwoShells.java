package de.gishmo.gwt.mvp4g2.processor.shell.eventBusWithTwoShells;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.EventBus;

@EventBus(shell = MockShellPresenter01.class)
public interface EventBusWithTwoShells
  extends IsEventBus {

  @Event
  void doSomethingInPresenter02();

}