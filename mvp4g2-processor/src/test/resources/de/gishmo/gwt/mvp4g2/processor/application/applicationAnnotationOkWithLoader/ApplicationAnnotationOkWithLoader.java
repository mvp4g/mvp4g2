package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoader;

import de.gishmo.gwt.mvp4g2.core.application.IsApplication;
import de.gishmo.gwt.mvp4g2.core.application.annotation.Application;

@Application(eventBus = MockEventBus.class, loader = MockApplicationLoader.class)
public interface ApplicationAnnotationOkWithLoader
  extends IsApplication {
}