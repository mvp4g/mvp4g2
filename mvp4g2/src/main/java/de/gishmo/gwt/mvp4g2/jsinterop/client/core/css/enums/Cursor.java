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
 * An enumeration for possible values of the css cursor property
 */
public enum Cursor {

  /**
   * The platform-dependent default cursor. Often rendered as an arrow.
   */
  DEFAULT("default"),
  /**
   * The UA determines the cursor to display based on the current context.
   */
  AUTO("auto"),
  /**
   * A simple crosshair (e.g., short line segments resembling a "+" sign).
   */
  CROSSHAIR("crosshair"),
  /**
   * The cursor is a pointer that indicates a link.
   */
  POINTER("pointer"),
  /**
   * Indicates something is to be moved.
   */
  MOVE("move"),
  /**
   * e-resize, ne-resize, nw-resize, n-resize, se-resize, sw-resize, s-resize, w-resize:
   * Indicate that some edge is to be moved. For example, the 'se-resize' cursor is used when
   * the movement starts from the south-east corner of the box.
   */
  E_RESIZE("e-resize"),
  /**
   * e-resize, ne-resize, nw-resize, n-resize, se-resize, sw-resize, s-resize,
   * w-resize: Indicate that some edge is to be moved. For example, the 'se-resize'
   * cursor is used when the movement starts from the south-east corner of the box.
   */
  NE_RESIZE("ne-resize"),
  /**
   * e-resize, ne-resize, nw-resize, n-resize, se-resize, sw-resize, s-resize,
   * w-resize: Indicate that some edge is to be moved. For example, the 'se-resize'
   * cursor is used when the movement starts from the south-east corner of the box.
   */
  NW_RESIZE("nw-resize"),
  /**
   * e-resize, ne-resize, nw-resize, n-resize, se-resize, sw-resize, s-resize,
   * w-resize: Indicate that some edge is to be moved. For example, the 'se-resize'
   * cursor is used when the movement starts from the south-east corner of the box.
   */
  N_RESIZE("n-resize"),
  /**
   * e-resize, ne-resize, nw-resize, n-resize, se-resize, sw-resize, s-resize,
   * w-resize: Indicate that some edge is to be moved. For example, the 'se-resize'
   * cursor is used when the movement starts from the south-east corner of the box.
   */
  SE_RESIZE("se-resize"),
  /**
   * e-resize, ne-resize, nw-resize, n-resize, se-resize, sw-resize, s-resize,
   * w-resize: Indicate that some edge is to be moved. For example, the 'se-resize'
   * cursor is used when the movement starts from the south-east corner of the box.
   */
  SW_RESIZE("sw-resize"),
  /**
   * e-resize, ne-resize, nw-resize, n-resize, se-resize, sw-resize, s-resize,
   * w-resize: Indicate that some edge is to be moved. For example, the 'se-resize'
   * cursor is used when the movement starts from the south-east corner of the box.
   */
  S_RESIZE("s-resize"),
  /**
   * e-resize, ne-resize, nw-resize, n-resize, se-resize, sw-resize, s-resize,
   * w-resize: Indicate that some edge is to be moved. For example, the 'se-resize'
   * cursor is used when the movement starts from the south-east corner of the box.
   */
  W_RESIZE("w-resize"),
  /**
   * Indicates text that may be selected. Often rendered as an I-bar.
   */
  TEXT("text"),
  /**
   * Indicates that the program is busy and the user should wait. Often rendered as a watch or
   * hourglass.
   */
  WAIT("wait"),
  /**
   * Help is available for the object under the cursor. Often rendered as a question mark or a
   * balloon.
   */
  HELP("help"),
  COL_RESIZE("col-resize"),
  ROW_RESIZE("row-resize");

  private String cursor;

  private Cursor(String cursor) {
    this.cursor = cursor;
  }

  /**
   * Converts the given string into a Cursor representation
   *
   * @param value
   *   The value to turn into a Cursor representation
   *
   * @return The Cursor representation for the given string
   */
  public static Cursor fromString(String value) {
    Cursor cursor = DEFAULT;

    if (value != null) {
      for (Cursor c : values()) {
        if (c.getCursor()
             .equalsIgnoreCase(value)) {
          cursor = c;
          break;
        }
      }
    }

    return cursor;
  }

  /**
   * Returns the String representation for the cursor property
   *
   * @return The String representation for the cursor property
   */
  public String getCursor() {
    return this.cursor;
  }

}