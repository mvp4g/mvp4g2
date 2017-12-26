package de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation;

import de.gishmo.gwt.mvp4g2.client.internal.ui.AbstractHandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.internal.ui.PresenterMetaData;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;

public final class De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData
  extends PresenterMetaData<MockShellPresenter, IMockShellView> {
  public De_gishmo_gwt_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData() {
    super("de.gishmo.gwt.mvp4g2.processor.event.startEventTestEventBusWithOneStartAnnotation.MockShellPresenter",
          AbstractHandlerMetaData.Kind.PRESENTER,
          false,
          Presenter.VIEW_CREATION_METHOD.FRAMEWORK);
    super.presenter = new MockShellPresenter();
    super.view = (IMockShellView) new MockShellView();
  }
}
