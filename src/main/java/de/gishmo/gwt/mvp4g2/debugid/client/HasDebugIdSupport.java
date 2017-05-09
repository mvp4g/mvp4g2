package de.gishmo.gwt.mvp4g2.debugid.client;

/**
 * <p>This interface tells the framework, that the marked widget has a
 * setDebugId-Method.<br/><br/>
 * This enables complex widgets to have an own implementation for a debugId</p>
 */
public interface HasDebugIdSupport {

  /**
   * <p>Method to implement a different debugId setting</p>
   *
   * @param debugId
   *   the debug id
   */
  void setDebugId(String debugId);

}
