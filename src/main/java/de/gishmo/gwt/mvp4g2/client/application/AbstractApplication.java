package de.gishmo.gwt.mvp4g2.client.application;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;

/**
 * type of the eventBus
 */
public abstract class AbstractApplication<E extends IsEventBus>
//  <E extends Mvp4gEventBus>
  implements IsApplication {

  protected E eventBus;

  @Override
  public void run() {

    // TODO vorher alles vorbereiten, dass ggfs.
    // execute the loader (if one is present)
    getApplicationLoader().load(() -> onFinishLaoding());
  }

  protected abstract IsApplicationLoader getApplicationLoader();

  /**
   * Once the loader did his job, we will continue
   */
  private void onFinishLaoding() {


    // the last thing we do, is to add the shell to the viewport
    eventBus.setShell();
    // fire start event
    eventBus.fireStartEvent();
  }
}
