package de.gishmo.gwt.mvp4g2.processor.event.debugAnnotationOnAMethod;

import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.processor.mock.MockShellPresenter;

@EventBus(shell = MockShellPresenter.class)
public interface DebugAnnotationOnAMethod
  extends IsEventBus {

  @Debug
  void oneEvent();

}