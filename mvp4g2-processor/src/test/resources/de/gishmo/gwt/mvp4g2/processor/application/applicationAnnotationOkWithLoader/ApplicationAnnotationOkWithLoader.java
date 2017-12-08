package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoader;

import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.client.application.IsApplication;

@Application(eventBus = MockEventBus.class, loader = MockApplicationLoader.class)
public interface ApplicationAnnotationOkWithLoader
  extends IsApplication {
}