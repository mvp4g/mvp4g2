package de.gishmo.gwt.mvp4g2.client.eventbus.internal;

import elemental2.dom.DomGlobal;

public class LogToConsole {

  public static void log(String message) {
    String logging = System.getProperty("mvp4g2.logging");
    if (logging != null) {
      if ("true".equals(logging.toLowerCase())) {
        DomGlobal.window.console.log(message);
      }
    }
  }

}
