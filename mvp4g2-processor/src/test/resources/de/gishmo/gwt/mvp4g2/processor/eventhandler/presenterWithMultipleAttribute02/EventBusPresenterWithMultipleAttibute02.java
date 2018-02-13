package de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterWithMultipleAttribute02;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.EventBus;

@EventBus(shell = MockShellPresenter01.class)
public interface EventBusPresenterWithMultipleAttibute02

  extends IsEventBus {

  @Event
  void doSomething01(String oneAttribute);

  @Event
  void doSomething02(String oneAttribute);

  @Event
  void doSomethingInMultiplePresenter01();

  @Event
  void doSomethingInMultiplePresenter02();

}