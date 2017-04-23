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

package de.gishmo.gwt.mvp4g2.jsinterop.client.core.css;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * A <code>CSSRuleList</code> is an array-like object containing an ordered collection of {@link CssRule}
 * objects
 */
@JsType(isNative = true)
public class CssRuleList {

  /**
   * @return The number of contained {@link CssRule} objects
   */
  @JsProperty
  public native int getLength();

  /**
   * Returns the CssRule for the given index
   *
   * @param index
   *   The index to get the CssRule for
   *
   * @return The CssRule for the given index
   */
  @JsMethod
  public native CssRule item(int index);

}