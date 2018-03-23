package com.github.mvp4g.mvp4g2.processor.eventhandler.presenterWithWrongImplementation03;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Event;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.EventBus;

@EventBus(shell = MockShellPresenter01.class)
public interface EventBusHandlerWithNotImplementedEvent
  extends IsEventBus {

  @Event
  void doSomething01(String oneAttribute);

  @Event
  void doSomething02(String oneAttribute);

}