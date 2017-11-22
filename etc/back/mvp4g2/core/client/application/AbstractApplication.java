package de.gishmo.gwt.mvp4g2.core.client.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;

import de.gishmo.gwt.mvp4g2.core.client.event.Mvp4gEventBus;
import de.gishmo.gwt.mvp4g2.core.client.event.Mvp4gSimpleEventBus;

/**
 * @param <E>
 *   type of the eventBus
 */
public abstract class AbstractApplication
//  <E extends Mvp4gEventBus>
  implements IsApplication {

  /* the internal eventBus - do not use it! */
  private Mvp4gEventBus     internalEventBus;
  /* place controller */
//  private IsPlaceController placeController;

//  private Mvp4gContext<E> mvp4gContext;
//
//  public AbstractApplication(Framework framework,
//                             ApplicationType applicationType,
//                             String applicationId,
//                             E eventBus) {
//
//    this.mvp4gContext = new Mvp4gContext<>(framework,
//                                           applicationType,
//                                           applicationId,
//                                           eventBus);
//  }

  public void run(HasWidgets.ForIsWidget viewPort) {
//    this.internalEventBus = new Mvp4gSimpleEventBus();
//
//    this.placeController = createPlaceController();
//    this.placeController.setMvp4gEventBus(this.internalEventBus);
//
//    GWT.debugger();
//    viewPort.add(this.placeController.getShellPresenter());
  }

//  protected abstract IsPlaceController createPlaceController();

}
