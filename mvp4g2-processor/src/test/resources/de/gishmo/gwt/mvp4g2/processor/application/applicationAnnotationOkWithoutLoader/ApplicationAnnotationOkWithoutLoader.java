package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithoutLoader;

import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.client.application.IsApplication;

@Application(eventBus = MockEventBus.class)
public interface ApplicationAnnotationOkWithoutLoader
  extends IsApplication {
}