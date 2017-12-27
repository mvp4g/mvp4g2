package de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute;

import de.gishmo.gwt.mvp4g2.core.internal.ui.AbstractHandlerMetaData;
import de.gishmo.gwt.mvp4g2.core.internal.ui.PresenterMetaData;
import de.gishmo.gwt.mvp4g2.core.ui.annotation.Presenter;

public final class De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData
  extends PresenterMetaData<MockShellPresenter, IMockShellView> {
  public De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData() {
    super("de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute.MockShellPresenter",
          AbstractHandlerMetaData.Kind.PRESENTER,
          false,
          Presenter.VIEW_CREATION_METHOD.FRAMEWORK);
    super.presenter = new MockShellPresenter();
    super.view = (IMockShellView) new MockShellView();
  }
}
