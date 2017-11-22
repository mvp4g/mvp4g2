package gwt.mvp4g.client.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;

import gwt.core.mvp4g.client.event.Mvp4gEventBus;
import gwt.core.mvp4g.client.event.Mvp4gSimpleEventBus;
import gwt.mvp4g.client.place.IsPlaceController;
import gwt.mvp4g.client.place.IsShellWrapper;

/**
 * @param <E>
 *   type of the eventBus
 */
public abstract class AbstractApplication
//  <E extends Mvp4gEventBus>
  implements IsApplication {

  /* the internal eventBus - do not use it! */
  private Mvp4gEventBus internalEventBus;
  /* place controller */
  private IsPlaceController placeController;

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
    this.internalEventBus = new Mvp4gSimpleEventBus();

    this.placeController = createPlaceController();
    this.placeController.setMvp4gEventBus(this.internalEventBus);

    GWT.debugger();
    viewPort.add(this.placeController.getShell());
  }

  protected abstract IsPlaceController createPlaceController();

}
