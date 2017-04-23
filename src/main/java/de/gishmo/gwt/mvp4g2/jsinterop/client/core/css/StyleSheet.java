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

import de.gishmo.gwt.mvp4g2.jsinterop.client.core.dom.Node;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * An object implementing the StyleSheet interface represents a single style sheet. CSS style sheets will
 * further implement the more specialized {@link CssStyleSheet} interface.
 */
@JsType(isNative = true)
public interface StyleSheet {

  /**
   * Representing whether the current stylesheet has been applied or not.
   *
   * @return false if the style sheet is applied to the document. true if it is not. Modifying this
   * attribute may cause a new resolution of style for the document. A stylesheet only applies if
   * both an appropriate medium definition is present and the disabled attribute is false. So, if
   * the media doesn't apply to the current user agent, the disabled attribute is ignored.
   */
  @JsProperty
  boolean isDisabled();

  /**
   * Setter used to set the disabled property which is used to indicate whether a stylesheet should be
   * applied
   *
   * @param disabled
   *   false if the style sheet is applied to the document. true if it is not. Modifying this
   *   attribute may cause a new resolution of style for the document. A stylesheet only applies if
   *   both an appropriate medium definition is present and the disabled attribute is false. So, if
   *   the media doesn't apply to the current user agent, the disabled attribute is ignored.
   */
  @JsProperty
  void setDisabled(boolean disabled);

  /**
   * Getter for href attribute. If the style sheet is a linked style sheet, the value of its attribute is
   * its location.
   *
   * @return Returns the location of the stylesheet.
   */
  @JsProperty
  String getHref();

  /**
   * The intended destination media for style information. The media is often specified in the ownerNode. If
   * no media has been specified, the MediaList will be empty
   *
   * @return Returns a MediaList representing the intended destination medium for style information.
   */
  @JsProperty
  MediaList getMedia();

  /**
   * The node that associates this style sheet with the document. For HTML, this may be the corresponding
   * LINK or STYLE element. For XML, it may be the linking processing instruction. For style sheets that are
   * included by other style sheets, the value of this attribute is null.
   *
   * @return Returns a Node associating this style sheet with the current document.
   */
  @JsProperty
  Node getOwnerNode();

  /**
   * For style sheet languages that support the concept of style sheet inclusion, this attribute represents
   * the including style sheet, if one exists. If the style sheet is a top-level style sheet, or the style
   * sheet language does not support inclusion, the value of this attribute is null.
   *
   * @return Returns a StyleSheet including this one, if any; returns null if there aren't any.
   */
  @JsProperty
  StyleSheet getParentStyleSheet();

  /**
   * The advisory title. The title is often specified in the ownerNode.
   *
   * @return Returns the advisory title of the current style sheet.
   */
  @JsProperty
  String getTitle();

  /**
   * This specifies the style sheet language for this style sheet. The style sheet language is specified as
   * a content type (e.g. "text/css"). The content type is often specified in the ownerNode
   *
   * @return Returns the style sheet language for this style sheet
   */
  @JsProperty
  String getType();

}