package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoaderAsInnerInterface;

import de.gishmo.gwt.mvp4g2.core.application.IsApplicationLoader;
import de.gishmo.gwt.mvp4g2.core.internal.application.AbstractApplication;
import de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoaderAsInnerInterface.ApplicationAnnotationOkWithLoaderAsInnerInterface.MyApplicationLoader;
import java.lang.Override;

public final class ApplicationAnnotationOkWithLoaderAsInnerInterfaceImpl extends AbstractApplication<MockEventBus> implements ApplicationAnnotationOkWithLoaderAsInnerInterface {
  public ApplicationAnnotationOkWithLoaderAsInnerInterfaceImpl() {
    super();
    super.eventBus = new de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoaderAsInnerInterface.MockEventBusImpl();
    super.historyOnStart = false;
  }

  @Override
  public IsApplicationLoader getApplicationLoader() {
    return new MyApplicationLoader();
  }
}
