package com.github.mvp4g.mvp4g2.processor.eventbus.debugAnnotationOnAMethod;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Debug;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.EventBus;

@EventBus(shell = MockShellPresenter.class)
public interface DebugAnnotationOnAMethod
  extends IsEventBus {

  @Debug
  void oneEvent();

}