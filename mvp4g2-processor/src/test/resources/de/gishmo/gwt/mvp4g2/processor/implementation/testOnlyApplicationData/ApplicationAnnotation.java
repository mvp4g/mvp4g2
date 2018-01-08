package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithoutLoader;

import de.gishmo.gwt.mvp4g2.core.application.IsApplication;
import de.gishmo.gwt.mvp4g2.core.application.annotation.Application;

@Application(eventBus = MockEventBus.class)
public interface ApplicationAnnotation
  extends IsApplication {
}