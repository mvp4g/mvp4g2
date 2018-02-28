package de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterWithViewCreationMethodPresenter04;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.EventBus;

@EventBus(shell = MockShellPresenter04.class)
public interface EventBusPresenterWithViewCreationMethodPresenter04
  extends IsEventBus {

  @Event
  void doSomething();

}
