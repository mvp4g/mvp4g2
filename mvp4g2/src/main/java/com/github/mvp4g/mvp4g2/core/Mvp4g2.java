package com.github.mvp4g.mvp4g2.core;

import elemental2.dom.DomGlobal;

public class Mvp4g2 {

  private Mvp4g2() {
  }

  public static void log(String message) {
    if ("on".equals(System.getProperty("superdevmode", "off"))) {
      DomGlobal.window.console.log(message);
    }
  }

}
