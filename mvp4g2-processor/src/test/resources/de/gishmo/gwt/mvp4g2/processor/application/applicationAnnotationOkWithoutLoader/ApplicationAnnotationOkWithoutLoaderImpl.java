package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithoutLoader;

import de.gishmo.gwt.mvp4g2.client.application.AbstractApplication;
import de.gishmo.gwt.mvp4g2.client.application.IsApplicationLoader;
import de.gishmo.gwt.mvp4g2.client.application.internal.NoApplicationLoader;
import de.gishmo.gwt.mvp4g2.processor.mock.MockEventBus;
import java.lang.Override;

public final class ApplicationAnnotationOkWithoutLoaderImpl extends AbstractApplication<MockEventBus> implements ApplicationAnnotationOkWithoutLoader {
  public ApplicationAnnotationOkWithoutLoaderImpl() {
    super();
    super.eventBus = new de.gishmo.gwt.mvp4g2.processor.mock.MockEventBusImpl();
  }

  @Override
  public IsApplicationLoader getApplicationLoader() {
    return new NoApplicationLoader();
  }
}
