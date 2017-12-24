package de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation;

import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.client.application.IsApplication;

@Application(eventBus = StartEventTestEventBusWithOneStartAnnotation.class)
public interface StartEventTestEventBusWithOneAnnotation
  extends IsApplication {
}