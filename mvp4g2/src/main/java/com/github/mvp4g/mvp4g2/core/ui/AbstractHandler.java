package com.github.mvp4g.mvp4g2.core.ui;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;

/**
 * Super class of a mvp4g2 event handler.
 *
 * @param <E> the eventbus of the applicaiton
 */
public abstract class AbstractHandler<E extends IsEventBus>
  implements IsHandler<E> {

  /* bind mehtod already called */
  protected boolean bound     = false;
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
   * @see IsHandler#setEventBus(IsEventBus)
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
   * @see IsHandler#isActivated()
   */
  public final boolean isActivated() {
//    boolean activated = this.activated &&
//                        pass(eventName,
//                             parameters);
//    if (activated) {
//      if (passive) {
//        return bound;
//      } else {
//        onBeforeEvent(eventName);
//        if (!bound) {
//          bind();
//          bound = true;
//        }
//      }
//    }
    return activated;
  }

  /*
   * (non-Javadoc)
   *
   * @see IsHandler#setBound(boolean)
   */
  public final void setActivated(boolean activated) {
    this.activated = activated;
  }

  /*
   * (non-Javadoc)
   *
   * @see IsHandler#isBound()
   */
  public final boolean isBound() {
    return bound;
  }

  /*
   * (non-Javadoc)
   *
   * @see IsHandler#setActivated(boolean)
   */
  public final void setBound(boolean bound) {
    this.bound = bound;
  }

  /*
   * (non-Javadoc)
   *
   * @see IsHandler#pass(String, Object...)
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
