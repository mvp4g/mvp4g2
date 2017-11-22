package de.gishmo.gwt.mvp4g2.client.eventbus;

import de.gishmo.gwt.mvp4g2.client.ui.IsPresenter;

public interface IsEventBus {

  /**
   * Fires the start event!
   */
  void fireStartEvent();

  void setShell();

  PresenterHandlerRegistration addHandler(IsPresenter<?, ?> presenter);

}
