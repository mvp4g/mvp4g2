package de.gishmo.gwt.mvp4g2.processor.eventhandler.applicationAnnotationOnAMethod;

import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.client.application.IsApplication;

public interface ApplicationAnnotationOnAMethod
  extends IsApplication {

  @Application(eventBus = MockEventBus.class)
  void oneMethod();

}