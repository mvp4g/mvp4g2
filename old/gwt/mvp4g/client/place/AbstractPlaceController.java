package gwt.mvp4g.client.place;

import com.google.gwt.user.client.ui.Widget;

import gwt.core.mvp4g.client.event.Mvp4gEventBus;

/**
 */
public abstract class AbstractPlaceController
  implements IsPlaceController {

  // the shell of the application */
  protected IsShellWrapper shellWrapper;
  /* the internal eventBus - do not use it! */
  private   Mvp4gEventBus  internalEventBus;

  public AbstractPlaceController(Mvp4gEventBus internalEventBus) {
    super();

    this.internalEventBus = internalEventBus;

    initialize();
  }

  /**
   * <p>Abstract initialize method. will be created by the processor.</p>
   */
  protected abstract void initialize();

  /**
   * <p>Gets the shell of the application.
   * <br/><br/>
   * The shell in the viewport of your application, which structures the screen.</p>
   *
   * @return The shell of the application
   */
  @Override
  public Widget getShell() {
    return this.shellWrapper.getShell();
  }

  /**
   * <p>Sets the internal eventbus.
   * <br/><br/>
   * <b>Warning: Don not use the internal eventbus directly!</b></p>
   *
   * @param internalEventBus
   *   the internal eventbus
   */
  @Override
  public void setMvp4gEventBus(Mvp4gEventBus internalEventBus) {
    this.internalEventBus = internalEventBus;
  }

}
