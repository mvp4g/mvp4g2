package de.gishmo.gwt.mvp4g2.processor.eventhandler.applicationAnnotationOnAMethod;

import de.gishmo.gwt.mvp4g2.client.application.IsApplication;
import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;

public interface ApplicationAnnotationOnAMethod
  extends IsApplication {

  @Application(eventBus = MockEventBus.class)
  void oneMethod();

}