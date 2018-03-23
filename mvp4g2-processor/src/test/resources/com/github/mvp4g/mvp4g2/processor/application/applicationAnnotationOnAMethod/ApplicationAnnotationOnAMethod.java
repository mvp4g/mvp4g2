package com.github.mvp4g.mvp4g2.processor.application.applicationAnnotationOnAMethod;

import com.github.mvp4g.mvp4g2.core.application.IsApplication;
import com.github.mvp4g.mvp4g2.core.application.annotation.Application;

public interface ApplicationAnnotationOnAMethod
  extends IsApplication {

  @Application(eventBus = MockEventBus.class)
  void oneMethod();

}