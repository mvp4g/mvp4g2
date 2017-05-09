package de.gishmo.gwt.mvp4g2.debugid.client;

/**
 * <p>Marker-Interface to trigger the generator</p>
 */
public interface SimpleDebugIdDriver<V> {

  /**
   * <p>The method will trigger the setting of the debugId</p>
   *
   * @param container
   *   the container which contains the widget
   */
  void bindDebugIds(V container);

}
