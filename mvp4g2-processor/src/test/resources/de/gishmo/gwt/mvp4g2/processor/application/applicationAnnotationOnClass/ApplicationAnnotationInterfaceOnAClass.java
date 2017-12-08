package de.gishmo.gwt.mvp4g2.processor.eventhandler.applicationAnnotationOnClass;

import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.client.application.IsApplication;

@Application(eventBus = MockEventBus.class)
public class ApplicationAnnotationInterfaceOnAClass
  implements IsApplication {
}