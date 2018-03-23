package com.github.mvp4g.mvp4g2.processor.application.applicationAnnotationOkWithLoaderAsInnerInterface;

import com.github.mvp4g.mvp4g2.core.application.IsApplicationLoader;
import com.github.mvp4g.mvp4g2.core.internal.application.AbstractApplication;
import com.github.mvp4g.mvp4g2.processor.application.applicationAnnotationOkWithLoaderAsInnerInterface.ApplicationAnnotationOkWithLoaderAsInnerInterface.MyApplicationLoader;
import java.lang.Override;

public final class ApplicationAnnotationOkWithLoaderAsInnerInterfaceImpl extends AbstractApplication<MockEventBus> implements ApplicationAnnotationOkWithLoaderAsInnerInterface {
  public ApplicationAnnotationOkWithLoaderAsInnerInterfaceImpl() {
    super();
    super.eventBus = new com.github.mvp4g.mvp4g2.processor.application.applicationAnnotationOkWithLoaderAsInnerInterface.MockEventBusImpl();
    super.historyOnStart = false;
  }

  @Override
  public IsApplicationLoader getApplicationLoader() {
    return new MyApplicationLoader();
  }
}
