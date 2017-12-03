package de.gishmo.gwt.mvp4g2.processor.event;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Filters;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.client.ui.MockFilter;
import de.gishmo.gwt.mvp4g2.processor.mock.MockShellPresenter;

@EventBus(shell = MockShellPresenter.class)
public interface FilterAnnotationOnAMethod
  extends IsEventBus {

  @Filters(filterClasses = MockFilter.class)
  void oneEvent();

}