package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithoutLoaderAsInnerInterface;

import de.gishmo.gwt.mvp4g2.core.application.IsApplicationLoader;
import de.gishmo.gwt.mvp4g2.core.internal.application.AbstractApplication;
import de.gishmo.gwt.mvp4g2.core.internal.application.NoApplicationLoader;
import de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithoutLoaderAsInnerInterface.ApplicationAnnotationOkWithoutLoaderAsInnerInterface.MyApplication;
import java.lang.Override;

public final class MyApplicationImpl extends AbstractApplication<MockEventBus> implements MyApplication {
  public MyApplicationImpl() {
    super();
    super.eventBus = new de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithoutLoaderAsInnerInterface.MockEventBusImpl();
  }

  @Override
  public IsApplicationLoader getApplicationLoader() {
    return new NoApplicationLoader();
  }
}
