package de.gishmo.gwt.mvp4g2.client.eventbus;

import de.gishmo.gwt.mvp4g2.client.ui.INavigationConfirmation;
import de.gishmo.gwt.mvp4g2.client.ui.IsPresenter;

public interface IsEventBus {

  /**
   * Fires the start event!
   */
  void fireStartEvent();

  /**
   * Framework m,ethod to set the shell
   * <br/>
   * <b>Do not use!</b>
   */
  void setShell();

  /**
   * Adds a presetner instance to the eventBis.<br>
   * <br>
   * Using this feature requires setting "multiple = true"  in the
   *
   * @param presenter
   *   instance of the new presenter to add to the eventbus
   *
   * @return returns a PresenterRegistration to remove the registration!
   */
  PresenterRegistration addHandler(IsPresenter<?, ?> presenter);

  /**
   * Method to manually ask if an action can occur
   *
   * @param event event to be executed in case the presenter does not interrupt navigation
   */
  //  void confirmNavigation(NavigationEventCommand event);

  /**
   * Set a confirmation that will be called before each navigation event or when history token
   * changes. This will set the navigationConfirmation for the whole application. You can have
   * only one navigationConfirmation for the whole application.
   *
   * @param navigationConfirmation
   *   presenter which should be called in case of confirmation
   */
  void setNavigationConfirmation(INavigationConfirmation navigationConfirmation);

}
