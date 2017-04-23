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

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * The history object contains the URLs visited by the user (within a browser window).<br>
 * The history object is part of the window object and is accessed through the window.history property.
 */
@JsType(isNative = true,
        namespace = JsPackage.GLOBAL,
        name = "history")
public interface History {

  /**
   * The length property returns the number of URLs in the history list of the current browser window.<br>
   * The property returns at least 1, because the list includes the currently loaded page.<br>
   * Tip: This property is useful to find out how many pages the user has visited in the current browsing
   * session.<br>
   * Note: Internet Explorer and Opera start at 0, while Firefox, Chrome, and Safari start at 1.<br>
   * Note: Maximum length is 50.<br>
   *
   * @return A Number, representing the number of entries in the session history
   */
  @JsProperty
  int getLength();

  /**
   * The back() method loads the previous URL in the history list. This is the same as clicking the
   * "Back button" in your browser, or history.go(-1).
   */
  void back();

  /**
   * The forward() method loads the next URL in the history list. This is the same as clicking the
   * "Forward button" in your browser, or history.go(1).
   */
  void forward();

  /**
   * Loads a specific URL from the history list.
   *
   * @param step
   *   A number which goes to the URL within the specific position (-1 goes back one page, 1 goes
   *   forward one page)
   */
  void go(int step);

}