package de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterOK;

import de.gishmo.gwt.mvp4g2.core.internal.ui.AbstractHandlerMetaData;
import de.gishmo.gwt.mvp4g2.core.internal.ui.PresenterMetaData;
import de.gishmo.gwt.mvp4g2.core.ui.annotation.Presenter;

public final class De_gishmo_gwt_mvp4g2_processor_eventhandler_presenterOK_PresenterOKMetaData
  extends PresenterMetaData<PresenterOK, IMockOneView> {
  public De_gishmo_gwt_mvp4g2_processor_eventhandler_presenterOK_PresenterOKMetaData() {
    super("de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterOK.PresenterOK",
          AbstractHandlerMetaData.Kind.PRESENTER,
          false,
          Presenter.VIEW_CREATION_METHOD.FRAMEWORK);
    super.presenter = new PresenterOK();
    super.view = (IMockOneView) new MockOneView();
  }
}
