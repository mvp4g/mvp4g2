package de.gishmo.gwt.mvp4g2.processor.event;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventFilter;
import de.gishmo.gwt.mvp4g2.client.eventbus.PresenterRegistration;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.client.history.IsNavigationConfirmation;
import de.gishmo.gwt.mvp4g2.client.history.PlaceService;
import de.gishmo.gwt.mvp4g2.client.ui.IsPresenter;

import de.gishmo.gwt.mvp4g2.processor.mock.MockShellPresenter;

@Debug
@EventBus(shell = MockShellPresenter.class)
public class DebugAnnotationOnAClass
  implements IsEventBus {

  @Override
  public void fireStartEvent() {

  }

  @Override
  public void fireInitHistoryEvent() {

  }

  @Override
  public void fireNotFoundHistoryEvent() {

  }

  @Override
  public boolean hasHistoryOnStart() {
    return false;
  }

  @Override
  public void setShell() {

  }

  @Override
  public PresenterRegistration addHandler(IsPresenter<?, ?> presenter) {
    return null;
  }

  @Override
  public IsNavigationConfirmation getNavigationConfirmationPresenter() {
    return null;
  }

  @Override
  public void setNavigationConfirmation(IsNavigationConfirmation navigationConfirmationPresenter) {

  }

  @Override
  public void setPlaceService(PlaceService<? extends IsEventBus> placeService) {

  }

  @Override
  public void addEventFilter(IsEventFilter<? extends IsEventBus> filter) {

  }

  @Override
  public void removeEventFilter(IsEventFilter<? extends IsEventBus> filter) {

  }
}