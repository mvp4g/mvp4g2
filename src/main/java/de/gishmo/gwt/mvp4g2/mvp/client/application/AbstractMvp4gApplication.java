package de.gishmo.gwt.mvp4g2.mvp.client.application;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * @param <E>
 *   type of the eventBus
 */
public abstract class AbstractMvp4gApplication
//  <E extends Mvp4gEventBus>
  implements Mvp4gApplication {

  /* the internal eventBus - do not use it! */
//  private Mvp4gEventBus     internalEventBus;
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
//    viewPort.add(this.placeController.getShell());
  }

//  protected abstract IsPlaceController createPlaceController();

}
