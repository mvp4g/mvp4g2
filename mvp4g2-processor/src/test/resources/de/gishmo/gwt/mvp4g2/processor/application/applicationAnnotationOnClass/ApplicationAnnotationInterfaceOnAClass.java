package de.gishmo.gwt.mvp4g2.processor.eventhandler.applicationAnnotationOnClass;

import de.gishmo.gwt.mvp4g2.core.application.IsApplication;
import de.gishmo.gwt.mvp4g2.core.application.annotation.Application;

@Application(eventBus = MockEventBus.class)
public class ApplicationAnnotationInterfaceOnAClass
  implements IsApplication {
}