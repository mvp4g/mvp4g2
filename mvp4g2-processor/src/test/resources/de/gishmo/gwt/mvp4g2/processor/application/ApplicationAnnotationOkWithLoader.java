package de.gishmo.gwt.mvp4g2.processor.application;

import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.client.application.IsApplication;
import de.gishmo.gwt.mvp4g2.processor.mock.MockApplicationLoader;
import de.gishmo.gwt.mvp4g2.processor.mock.MockEventBus;

@Application(eventBus = MockEventBus.class, loader = MockApplicationLoader.class)
public interface ApplicationAnnotationOkWithLoader
  extends IsApplication {
}