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

import de.gishmo.gwt.mvp4g2.jsinterop.client.core.css.CssStyleDeclaration;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true)
public interface Element
  extends Node,
          NodeSelector {

  /**
   * Getter for innerHTML attribute
   *
   * @return The markup of the element's content.
   */
  @JsProperty
  String getInnerHTML();

  /**
   * Setter for innerHTML attribute
   *
   * @param s
   *   The markup of the element's content.
   */
  @JsProperty
  void setInnerHTML(String s);

  /**
   * Getter for classList attribute
   *
   * @return The list of class attributes.
   */
  @JsProperty
  DomTokenList getClassList();

  @JsProperty
  CssStyleDeclaration getStyle();

  /**
   * Adds a new attribute or changes the value of an existing attribute on the specified element.
   *
   * @param name
   *   The name of the attribute as a string.
   * @param value
   *   The desired new value of the attribute.
   */
  void setAttribute(String name,
                    Object value);

  /**
   * Returns the value of a specified attribute on the element. If the given attribute does not exist, the
   * value returned will either be null or ""
   *
   * @param name
   *   The name of the attribute whose value you want to get.
   *
   * @return The value of a specified attribute on the element or null
   */
  String getAttribute(String name);

  /**
   * Method returns whether the current element has the specified attribute
   *
   * @param name
   *   A string representing the name of the attribute.
   *
   * @return Indicates whether the current element has the specified attribute
   */
  boolean hasAttribute(String name);

  /**
   * Removes an attribute from the specified element.
   *
   * @param name
   *   The name of the attribute to be removed from element.
   */
  void removeAttribute(String name);

}