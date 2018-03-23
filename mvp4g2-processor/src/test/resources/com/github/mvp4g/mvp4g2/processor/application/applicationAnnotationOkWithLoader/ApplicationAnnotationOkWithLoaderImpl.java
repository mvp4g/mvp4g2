package com.github.mvp4g.mvp4g2.processor.application.applicationAnnotationOkWithLoader;

import com.github.mvp4g.mvp4g2.core.application.IsApplicationLoader;
import com.github.mvp4g.mvp4g2.core.internal.application.AbstractApplication;
import java.lang.Override;

public final class ApplicationAnnotationOkWithLoaderImpl extends AbstractApplication<MockEventBus> implements ApplicationAnnotationOkWithLoader {
  public ApplicationAnnotationOkWithLoaderImpl() {
    super();
    super.eventBus = new com.github.mvp4g.mvp4g2.processor.application.applicationAnnotationOkWithLoader.MockEventBusImpl();
    super.historyOnStart = false;
  }

  @Override
  public IsApplicationLoader getApplicationLoader() {
    return new MockApplicationLoader();
  }
}
