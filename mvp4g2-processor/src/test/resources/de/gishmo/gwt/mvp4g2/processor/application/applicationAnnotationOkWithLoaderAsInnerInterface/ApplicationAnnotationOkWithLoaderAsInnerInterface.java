package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoaderAsInnerInterface;

import de.gishmo.gwt.mvp4g2.core.application.IsApplication;
import de.gishmo.gwt.mvp4g2.core.application.IsApplicationLoader;
import de.gishmo.gwt.mvp4g2.core.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoaderAsInnerInterface.ApplicationAnnotationOkWithLoaderAsInnerInterface.MyApplicationLoader;

@Application(eventBus = MockEventBus.class, loader = MyApplicationLoader.class)
public interface ApplicationAnnotationOkWithLoaderAsInnerInterface
  extends IsApplication {

  class MyApplicationLoader
    implements IsApplicationLoader {

    @Override
    public void load(FinishLoadCommand finishLoadCommand) {
      finishLoadCommand.finishLoading();
    }

  }
}