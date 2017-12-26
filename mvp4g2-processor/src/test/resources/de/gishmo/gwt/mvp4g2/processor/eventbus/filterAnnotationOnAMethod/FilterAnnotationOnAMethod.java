package de.gishmo.gwt.mvp4g2.processor.event.filterAnnotationOnAMethod;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Filters;

@EventBus(shell = MockShellPresenter.class)
public interface FilterAnnotationOnAMethod
  extends IsEventBus {

  @Filters(filterClasses = MockFilter.class)
  void oneEvent();

}