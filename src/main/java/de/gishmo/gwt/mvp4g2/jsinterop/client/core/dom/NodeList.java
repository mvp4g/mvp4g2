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

/**
 * NodeList objects are collections of nodes such as those returned by Node.childNodes and the
 * document.querySelectorAll method.
 */
@JsType(isNative = true)
public interface NodeList {

  /**
   * Returns the number of nodes in the NodeList.
   *
   * @return The number of nodes in the NodeList.
   */
  @JsProperty
  int getLength();

  /**
   * Returns an item in the list by its index, or null if the index is out-of-bounds; can be used as an
   * alternative to simply accessing nodeList[index] (which instead returns undefined when index is
   * out-of-bounds).
   *
   * @param index
   *   The index to get the the item for
   *
   * @return The item for the given index or null
   */
  <T extends Node> T item(int index);
}