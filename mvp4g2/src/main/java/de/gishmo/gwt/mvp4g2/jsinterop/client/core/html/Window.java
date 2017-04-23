/*
 * Copyright (C) 2017 Frank Hossfeld <frank.hossfeld@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.gishmo.gwt.mvp4g2.jsinterop.client.core.html;

import de.gishmo.gwt.mvp4g2.jsinterop.client.core.dom.Document;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * The window object represents an open window in a browser.
 */
@JsType(isNative = true,
        namespace = JsPackage.GLOBAL,
        name = "window")
public class Window {

  /**
   * Returns the Console object
   *
   * @return The console object
   */
  @JsProperty
  public static native Console getConsole();

  /**
   * Returns the Document object for the window
   *
   * @return The Document object for the window
   */
  @JsProperty
  public static native Document getDocument();

  /**
   * Returns the Navigator object for the window
   *
   * @return The Navigator object for the window
   */
  @JsProperty
  public static native Navigator getNavigator();

  /**
   * The innerWidth property returns the inner width of a window's content area.
   *
   * @return The inner width of a window's content area.
   */
  @JsProperty
  public static native int getInnerWidth();

  /**
   * The innerHeight property returns the inner height of a window's content area.
   *
   * @return The inner height of a window's content area.
   */
  @JsProperty
  public static native int getInnerHeight();

  /**
   * The outerWidth property returns the outer width of a window, including all interface elements (like
   * toolbars/scrollbars).
   *
   * @return The outer width of a window, including all interface elements (like toolbars/scrollbars).
   */
  @JsProperty
  public static native int getOuterWidth();

  /**
   * The outerHeight property returns the outer height of a window, including all interface elements (like
   * toolbars/scrollbars).
   *
   * @return The outer height of a window, including all interface elements (like toolbars/scrollbars).
   */
  @JsProperty
  public static native int getOuterHeight();

  /**
   * Returns a reference to the local storage object used to store data. Stores data with no expiration date
   *
   * @return A reference to the local storage
   */
  @JsProperty
  public static native Storage getLocalStorage();

  /**
   * Returns a reference to the local storage object used to store data. Stores data for one session (lost
   * when the browser tab is closed)
   *
   * @return A reference to the local session storage object
   */
  @JsProperty
  public static native Storage getSessionStorage();

  /**
   * The location object contains information about the current URL.<br>
   * <br>
   * The location object is part of the window object and is accessed through the window.location property.
   *
   * @return The location object of the current URL
   */
  @JsProperty
  public static native Location getLocation();

  /**
   * Displays an alert box with a specified message and an OK button.
   *
   * @param message
   *   The text to display in the alert box
   */
  public static native void alert(String message);

  /**
   * Decode a base-64 encoded string
   *
   * @param encoded
   *   Required. The string which has been encoded by the btoa() method
   *
   * @return Decoded a base-64 encoded string
   */
  public static native String atob(String encoded);

  /**
   * Encodes a string in base-64.
   * <p>
   * This method uses the "A-Z", "a-z", "0-9", "+", "/" and "=" characters to encode the string.
   *
   * @param str
   *   Required. The string to be encoded
   *
   * @return A String, representing the base-64 encoded string
   */
  public static native String btoa(String str);

}