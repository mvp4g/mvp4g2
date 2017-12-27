package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithoutLoaderAsInnerInterface;

import de.gishmo.gwt.mvp4g2.core.application.IsApplication;
import de.gishmo.gwt.mvp4g2.core.application.annotation.Application;

public class ApplicationAnnotationOkWithoutLoaderAsInnerInterface {

  MyApplication myApplication = new MyApplicationImpl();

  @Application(eventBus = MockEventBus.class)
  public interface MyApplication
    extends IsApplication {
  }
}

