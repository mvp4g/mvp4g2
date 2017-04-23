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

package de.gishmo.gwt.mvp4g2.jsinterop.client.webcomponent;

import de.gishmo.gwt.mvp4g2.jsinterop.client.core.dom.Element;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * A base class for WebComponenent elements.
 */
@JsType(isNative = true)
public interface WebComponentElement
  extends Element {

  /**
   * Returns the value of the &quot;is&quot; property of the element. Each webcomponent has this attribute
   * set as soon as it has been upgraded and can fully be used.
   *
   * @return The &quot;is&quot; property or null in case the element has not yet been upgraded
   */
  @JsProperty
  String getIs();

}