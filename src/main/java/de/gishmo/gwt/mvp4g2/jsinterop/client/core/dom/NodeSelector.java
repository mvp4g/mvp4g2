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

import jsinterop.annotations.JsType;

@JsType(isNative = true)
public interface NodeSelector {

  /**
   * The method must return the first matching Element node within the subtrees of the context node. If
   * there is no matching Element, the method must return null.
   *
   * @param selector
   *   The selector used to search for the matching Element
   *
   * @return The first matching Element node or null if there is no matching Element
   */
  <T extends Element> T querySelector(String selector);

  /**
   * The method return a NodeList containing all of the matching Element nodes within the subtrees of the
   * context node, in document order. If there are no matching nodes, the method must return an empty
   * NodeList.
   *
   * @param selector
   *   The selector used to search for the matching Elements
   *
   * @return A NodeList with all matching Elements in document order or null if there are not matching
   * Elements
   */
  NodeList querySelectorAll(String selector);

}