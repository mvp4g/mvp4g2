package de.gishmo.gwt.mvp4g2.core.ui;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventFilter;

public class MockFilter
  extends IsEventFilter<MockEventBus> {

  @Override
  public boolean filterEvent(MockEventBus eventBus,
                             String eventName,
                             Object... params) {
    return true;
  }

}
