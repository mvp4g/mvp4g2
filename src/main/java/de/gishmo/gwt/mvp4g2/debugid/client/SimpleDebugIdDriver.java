package de.gishmo.gwt.mvp4g2.debugid.client;

import com.google.gwt.user.client.ui.Widget;

/**
 * <p>Marker-Interface to rigger the generator</p>
 */
public interface SimpleDebugIdDriver<V extends Widget> {

  /**
   * <p>The method will trigger the setting of the debugId</p>
   *
   * @param Widget the container whoch contains the widget
   */
  void initialize(V Widget);

}
