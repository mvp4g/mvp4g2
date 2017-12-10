package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithoutLoader;

import de.gishmo.gwt.mvp4g2.client.application.IsApplicationLoader;
import de.gishmo.gwt.mvp4g2.client.internal.application.AbstractApplication;
import de.gishmo.gwt.mvp4g2.client.internal.application.NoApplicationLoader;
import java.lang.Override;

public final class ApplicationAnnotationOkWithoutLoaderImpl extends AbstractApplication<MockEventBus> implements ApplicationAnnotationOkWithoutLoader {
  public ApplicationAnnotationOkWithoutLoaderImpl() {
    super();
    super.eventBus = new de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithoutLoader.MockEventBusImpl();
  }

  @Override
  public IsApplicationLoader getApplicationLoader() {
    return new NoApplicationLoader();
  }
}
