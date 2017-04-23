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
 * An enumeration for possible values of the css display property
 */
public enum Display {

  /**
   * This value causes an element to generate one or more inline boxes.
   */
  INLINE("inline"),
  /**
   * This value causes an element to generate a principal block box.
   */
  BLOCK("block"),
  /**
   * This value causes an element (e.g., LI in HTML) to generate a principal block box and a
   * list-item inline box. For information about lists and examples of list formatting,
   * please consult the section on lists.
   */
  LIST_ITEM("list-item"),
  /**
   * run-in and compact: These values create either block or inline boxes, depending on
   * context. Properties apply to run-in and compact boxes based on their final status
   * (inline-level or block-level). For example, the 'white-space' property only applies if
   * the box becomes a block box.
   */
  /**
   * run-in and compact: These values create either block or inline boxes, depending on
   * context. Properties apply to run-in and compact boxes based on their final status
   * (inline-level or block-level). For example, the 'white-space' property only applies if
   * the box becomes a block box.
   */
  RUN_IN("run-in"),
  /**
   * run-in and compact: These values create either block or inline boxes, depending on
   * context. Properties apply to run-in and compact boxes based on their final status
   * (inline-level or block-level). For example, the 'white-space' property only applies
   * if the box becomes a block box.
   */
  COMPACT("compact"),
  /**
   * This value declares generated content before or after a box to be a marker. This
   * value should only be used with :before and :after pseudo-elements attached to
   * block-level elements. In other cases, this value is interpreted as 'inline'. Please
   * consult the section on markers for more information.
   */
  MARKER("marker"),
  /**
   * table, inline-table, table-row-group, table-column, table-column-group,
   * table-header-group, table-footer-group, table-row, table-cell, and table-caption:
   * These values cause an element to behave like a table element
   */
  TABLE("table"),
  /**
   * table, inline-table, table-row-group, table-column, table-column-group,
   * table-header-group, table-footer-group, table-row, table-cell, and table-caption: These
   * values cause an element to behave like a table element
   */
  INLINE_TABLE("inline-table"),
  /**
   * table, inline-table, table-row-group, table-column, table-column-group,
   * table-header-group, table-footer-group, table-row, table-cell, and table-caption:
   * These values cause an element to behave like a table element
   */
  TABLE_ROW_GROUP("table-row-group"),
  /**
   * table, inline-table, table-row-group, table-column, table-column-group,
   * table-header-group, table-footer-group, table-row, table-cell, and table-caption:
   * These values cause an element to behave like a table element
   */
  TABLE_HEADER_GROUP("table-header-group"),
  /**
   * table, inline-table, table-row-group, table-column, table-column-group,
   * table-header-group, table-footer-group, table-row, table-cell, and table-caption:
   * These values cause an element to behave like a table element
   */
  TABLE_FOOTER_GROUP("table-footer-group"),
  /**
   * table, inline-table, table-row-group, table-column, table-column-group,
   * table-header-group, table-footer-group, table-row, table-cell, and table-caption:
   * These values cause an element to behave like a table element
   */
  TABLE_ROW("table-row"),
  /**
   * table, inline-table, table-row-group, table-column, table-column-group,
   * table-header-group, table-footer-group, table-row, table-cell, and
   * table-caption: These values cause an element to behave like a table element
   */
  TABLE_COLUMN_GROUP("table-column-group"),
  /**
   * table, inline-table, table-row-group, table-column, table-column-group,
   * table-header-group, table-footer-group, table-row, table-cell, and table-caption:
   * These values cause an element to behave like a table element
   */
  TABLE_COLUMN("table-column"),
  /**
   * table, inline-table, table-row-group, table-column, table-column-group,
   * table-header-group, table-footer-group, table-row, table-cell, and table-caption:
   * These values cause an element to behave like a table element
   */
  TABLE_CELL("table-cell"),
  /**
   * table, inline-table, table-row-group, table-column, table-column-group,
   * table-header-group, table-footer-group, table-row, table-cell, and
   * table-caption: These values cause an element to behave like a table element
   */
  TABLE_CAPTION("table-caption"),
  /**
   * This value causes an element to generate no boxes in the formatting structure (i.e.,
   * the element has no effect on layout). Descendant elements do not generate any boxes
   * either; this behavior cannot be overridden by setting the 'display' property on the
   * descendants.
   * Please note that a display of 'none' does not create an invisible box; it creates no
   * box at all. CSS includes mechanisms that enable an element to generate boxes in the
   * formatting structure that affect formatting but are not visible themselves. Please
   * consult the section on visibility for details.
   */
  NONE("none"),
  INHERIT("inherit");

  private String display;

  private Display(String display) {
    this.display = display;
  }

  /**
   * Converts the given string into a Display representation
   *
   * @param value
   *   The value to turn into a Display representation
   *
   * @return The Display representation for the given string
   */
  public static Display fromString(String value) {
    Display display = null;

    if (value != null) {
      for (Display d : values()) {
        if (d.getDisplay()
             .equalsIgnoreCase(value)) {
          display = d;
          break;
        }
      }
    }

    return display;
  }

  /**
   * Returns the String representation for the display property
   *
   * @return The String representation for the display property
   */
  public String getDisplay() {
    return this.display;
  }

}