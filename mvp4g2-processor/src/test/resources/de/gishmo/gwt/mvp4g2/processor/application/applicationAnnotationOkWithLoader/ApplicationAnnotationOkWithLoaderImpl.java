package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoader;

import de.gishmo.gwt.mvp4g2.client.application.IsApplicationLoader;
import de.gishmo.gwt.mvp4g2.client.internal.application.AbstractApplication;
import java.lang.Override;

public final class ApplicationAnnotationOkWithLoaderImpl extends AbstractApplication<MockEventBus> implements ApplicationAnnotationOkWithLoader {
  public ApplicationAnnotationOkWithLoaderImpl() {
    super();
    super.eventBus = new de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoader.MockEventBusImpl();
  }

  @Override
  public IsApplicationLoader getApplicationLoader() {
    return new MockApplicationLoader();
  }
}
