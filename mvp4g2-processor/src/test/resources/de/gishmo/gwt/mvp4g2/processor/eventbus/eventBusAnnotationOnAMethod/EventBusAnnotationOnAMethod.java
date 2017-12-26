package de.gishmo.gwt.mvp4g2.processor.eventbus.eventBusAnnotationOnAMethod;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.processor.mock.MockShellPresenter;

public interface EventBusAnnotationOnAMethod
  extends IsEventBus {

  @EventBus(shell = MockShellPresenter.class)
  void oneEvent();

}