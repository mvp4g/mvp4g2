package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoaderAsInnerInterface;

import de.gishmo.gwt.mvp4g2.client.application.AbstractApplication;
import de.gishmo.gwt.mvp4g2.client.application.IsApplicationLoader;
import java.lang.Override;

public final class ApplicationAnnotationOkWithLoaderAsInnerInterfaceImpl extends AbstractApplication<MockEventBus> implements ApplicationAnnotationOkWithLoaderAsInnerInterface {
  public ApplicationAnnotationOkWithLoaderAsInnerInterfaceImpl() {
    super();
    super.eventBus = new de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoaderAsInnerInterface.MockEventBusImpl();
  }

  @Override
  public IsApplicationLoader getApplicationLoader() {
    return new MyApplicationLoader();
  }
}
