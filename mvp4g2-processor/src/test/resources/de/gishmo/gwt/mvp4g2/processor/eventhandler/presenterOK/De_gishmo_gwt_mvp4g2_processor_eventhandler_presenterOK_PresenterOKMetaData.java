package de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterOK;

import de.gishmo.gwt.mvp4g2.client.internal.ui.HandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.internal.ui.PresenterHandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.processor.mock.IMockOneView;
import de.gishmo.gwt.mvp4g2.processor.mock.MockOneView;

public final class De_gishmo_gwt_mvp4g2_processor_eventhandler_presenterOK_PresenterOKMetaData extends PresenterHandlerMetaData<PresenterOK, IMockOneView> {
  public De_gishmo_gwt_mvp4g2_processor_eventhandler_presenterOK_PresenterOKMetaData() {
    super("de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterOK.PresenterOK", HandlerMetaData.Kind.PRESENTER, false, Presenter.VIEW_CREATION_METHOD.FRAMEWORK);
    super.presenter = new PresenterOK();
    super.view = (IMockOneView) new MockOneView();
  }
}
