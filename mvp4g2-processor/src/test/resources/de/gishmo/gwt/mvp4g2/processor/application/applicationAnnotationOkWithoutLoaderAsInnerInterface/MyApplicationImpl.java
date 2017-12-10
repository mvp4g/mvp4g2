package de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithoutLoaderAsInnerInterface;

import de.gishmo.gwt.mvp4g2.client.application.AbstractApplication;
import de.gishmo.gwt.mvp4g2.client.application.IsApplicationLoader;
import de.gishmo.gwt.mvp4g2.client.application.internal.NoApplicationLoader;
import java.lang.Override;

public final class MyApplicationImpl extends AbstractApplication<MockEventBus> implements ApplicationAnnotationOkWithoutLoaderAsInnerInterface.MyApplication {
  public MyApplicationImpl() {
    super();
    super.eventBus = new de.gishmo.gwt.mvp4g2.processor.application.applicationAnnotationOkWithoutLoaderAsInnerInterface.MockEventBusImpl();
  }

  @Override
  public IsApplicationLoader getApplicationLoader() {
    return new NoApplicationLoader();
  }
}
