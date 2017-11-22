package gwt.mvp4g.client.place;

import com.google.gwt.user.client.ui.Widget;

import gwt.core.mvp4g.client.event.Mvp4gEventBus;

/**
 * <p>Marker-interface for PlaceController ... </p>
 */
public interface IsPlaceController {

  /**
   * <p>Gets the shell of the application.
   * <br/><br/>
   * The shell in the viewport of your application, which structures the screen.</p>
   *
   * @return The shell of the application
   */
  Widget getShell();

  /**
   * <p>Sets the internal eventbus.
   * <br/><br/>
   * <b>Warning: Don not use the internal eventbus directly!</b></p>
   *
   * @param internalEventBus
   *   the internal eventbus
   */
  void setMvp4gEventBus(Mvp4gEventBus internalEventBus);

}
