package de.gishmo.gwt.mvp4g2.client.ui;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventFilter;

public class MockFilter
  extends IsEventFilter<MockEventBus> {

  @Override
  public boolean filterEvent(MockEventBus eventBus,
                             String eventName,
                             Object... params) {
    return true;
  }

}
