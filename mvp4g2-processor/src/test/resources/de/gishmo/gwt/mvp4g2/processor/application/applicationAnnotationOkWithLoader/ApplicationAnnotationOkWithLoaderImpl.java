package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoader;

import de.gishmo.gwt.mvp4g2.core.application.IsApplicationLoader;
import de.gishmo.gwt.mvp4g2.core.internal.application.AbstractApplication;
import java.lang.Override;

public final class ApplicationAnnotationOkWithLoaderImpl extends AbstractApplication<MockEventBus> implements ApplicationAnnotationOkWithLoader {
  public ApplicationAnnotationOkWithLoaderImpl() {
    super();
    super.eventBus = new de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithLoader.MockEventBusImpl();
    super.historyOnStart = false;
  }

  @Override
  public IsApplicationLoader getApplicationLoader() {
    return new MockApplicationLoader();
  }
}
