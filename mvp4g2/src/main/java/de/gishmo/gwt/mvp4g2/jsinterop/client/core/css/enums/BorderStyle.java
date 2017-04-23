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

package de.gishmo.gwt.mvp4g2.jsinterop.client.core.css.enums;

/**
 * An enumeration for possible values of the css border-style property
 */
public enum BorderStyle {

  /**
   * No border. This value forces the computed value of 'border-width' to be '0'.
   */
  NONE("none"),
  /**
   * Same as 'none', except in terms of border <a href=
   * "http://www.w3.org/TR/1998/REC-CSS2-19980512/tables.html#border-conflict-resolution">
   * conflict resolution</a> for table elements.
   */
  HIDDEN("hidden"),
  /**
   * The border is a series of dots.
   */
  DOTTED("dotted"),
  /**
   * The border is a series of short line segments.
   */
  DASHED("dashed"),
  /**
   * The border is a single line segment.
   */
  SOLID("solid"),
  /**
   * The border is two solid lines. The sum of the two lines and the space between them
   * equals the value of 'border-width'.
   */
  DOUBLE("double"),
  /**
   * The border looks as though it were carved into the canvas.
   */
  GROOVE("groove"),
  /**
   * The opposite of GROOVE: the border looks as though it were coming out of the canvas.
   */
  RIDGE("ridge"),
  /**
   * The border makes the entire box look as though it were embedded in the canvas.
   */
  INSET("inset"),
  /**
   * The opposite of INSET: the border makes the entire box look as though it were coming
   * out of the canvas.
   */
  OUTSET("outset");

  private String value;

  private BorderStyle(String value) {
    this.value = value;
  }

  /**
   * Converts the given string into a BorderStyle representation
   *
   * @param value
   *   The value to turn into a BorderStyle representation
   *
   * @return The BorderStyle representation for the given string
   */
  public static BorderStyle fromString(String value) {
    BorderStyle element = null;

    if (value != null) {
      for (BorderStyle e : values()) {
        if (e.getValue()
             .equalsIgnoreCase(value)) {
          element = e;
          break;
        }
      }
    }

    return element;
  }

  /**
   * Returns the String representation for the border-style property
   *
   * @return The String representation for the border-style property
   */
  public String getValue() {
    return this.value;
  }

}