package de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterWithViewCreationMethodPresenter03;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.EventBus;

@EventBus(shell = MockShellPresenter03.class)
public interface EventBusPresenterWithViewCreationMethodPresenter03
  extends IsEventBus {

  @Event
  void doSomething();

}
