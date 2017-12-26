package de.gishmo.gwt.mvp4g2.processor.eventbus.eventBusAnnotationOnAClass;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.processor.mock.MockShellPresenter;

@EventBus(shell = MockShellPresenter.class)
public class EventBusAnnotationOnAClass
  implements IsEventBus {

  @Debug
  void event();

}