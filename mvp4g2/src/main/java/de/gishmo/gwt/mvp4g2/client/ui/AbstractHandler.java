package de.gishmo.gwt.mvp4g2.client.ui;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;

/**
 * Super class of a mvp4g2 event handler.
 *
 * @param <E> the eventbus of the applicaiton
 */
public abstract class AbstractHandler<E extends IsEventBus>
  implements IsHandler<E> {

  /* bind mehtod already called */
  protected boolean binded    = false;
  /* ehather the eventhandler is enable to handle events or not ... */
  protected boolean activated = true;
  protected E eventBus;

  /**
   * (non-Javadoc)
   *
   * @see IsHandler#getEventBus()
   */
  public final E getEventBus() {
    return eventBus;
  }

  /**
   * (non-Javadoc)
   *
   * @see IsHandler#setEventBus(de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus)
   */
  public final void setEventBus(E eventBus) {
    this.eventBus = eventBus;
  }

  /**
   * (non-Javadoc)
   *
   * @see IsHandler#bind()
   */
  public void bind() {
    /*
     * Default implementation does nothing: extensions are responsible for (optionally) defining
     * binding specifics.
     */
  }

  /*
   * (non-Javadoc)
   *
   * @see de.gishmo.gwt.mvp4g2.client.ui.IsHandler#isActivated()
   */
  public final boolean isActivated() {
//    boolean activated = this.activated &&
//                        pass(eventName,
//                             parameters);
//    if (activated) {
//      if (passive) {
//        return binded;
//      } else {
//        onBeforeEvent(eventName);
//        if (!binded) {
//          bind();
//          binded = true;
//        }
//      }
//    }
    return activated;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.gishmo.gwt.mvp4g2.client.ui.IsHandler#setBinded(boolean)
   */
  public final void setActivated(boolean activated) {
    this.activated = activated;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.gishmo.gwt.mvp4g2.client.ui.IsHandler#isBinded()
   */
  public final boolean isBinded() {
    return binded;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.gishmo.gwt.mvp4g2.client.ui.IsHandler#setActivated(boolean)
   */
  public final void setBinded(boolean binded) {
    this.binded = binded;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.gishmo.gwt.mvp4g2.client.ui.IsHandler#pass(String, Object...)
   */
  public boolean pass(String eventName,
                      Object... parameters) {
    return true;
  }

  /**
   * Method called before each time an handler has to handle an event.
   *
   * @param eventName name of the event
   */
  public void onBeforeEvent(String eventName) {
  }
}
