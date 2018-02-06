package de.gishmo.gwt.mvp4g2.core.internal.application;

import de.gishmo.gwt.mvp4g2.core.application.IsApplication;
import de.gishmo.gwt.mvp4g2.core.application.IsApplicationLoader;
import de.gishmo.gwt.mvp4g2.core.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.core.history.PlaceService;

/**
 * generator of the eventBus
 */
public abstract class AbstractApplication<E extends IsEventBus>
  implements IsApplication {

  /* the eventbus */
  protected E                                  eventBus;
  /* flag if we have to check history token at the start of the application */
  protected    boolean                            historyOnStart;
  /* the PlaceService */
  private   PlaceService<? extends IsEventBus> placeService;

  @Override
  public void run() {
    // execute the loader (if one is present)
    getApplicationLoader().load(() -> onFinishLaoding());
  }

  /**
   * Once the loader did his job, we will continue
   */
  private void onFinishLaoding() {
    // create place service and bind
    this.placeService = new PlaceService<E>(this.eventBus,
                                            historyOnStart);
    this.eventBus.setPlaceService(this.placeService);
    // start the application
    placeService.startApplication();
  }

  protected abstract IsApplicationLoader getApplicationLoader();

}
