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

package de.gishmo.gwt.mvp4g2.jsinterop.client.core.dom;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true)
public interface DomTokenList {

  /**
   * @return Returns the number of contained elements
   */
  @JsProperty
  int getLength();

  /**
   * Adds token to the underlying string
   *
   * @param token
   *   The token to add
   */
  void add(String token);

  /**
   * Checks whether the underlying string contains the token
   *
   * @param token
   *   The token to be checked
   *
   * @return Returns true if the underlying string contains token, otherwise false
   */
  boolean contains(String token);

  /**
   * Returns an item in the list by its index (or undefined if the number is greater than or equal to the
   * length of the list, prior to Gecko 7.0 returned null)
   *
   * @param index
   *   The index to get the item for
   *
   * @return The item with the given index in the list
   */
  String item(int index);

  /**
   * Removes token from the underlying string
   *
   * @param token
   *   The token to remove
   */
  void remove(String token);

  /**
   * Removes token from string and returns false. If token doesn't exist it's added and the function returns
   * true
   *
   * @param token
   *   The token which will either be removed or added
   *
   * @return true in case the token was added - otherwise false
   */
  boolean toggle(String token);

}