package de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterWithViewCreationMethodPresenter05;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.EventBus;

@EventBus(shell = MockShellPresenter05.class)
public interface EventBusPresenterWithViewCreationMethodPresenter05
  extends IsEventBus {

  @Event
  void doSomething();

}
