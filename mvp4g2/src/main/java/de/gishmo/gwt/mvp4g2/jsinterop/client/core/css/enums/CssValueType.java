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

import de.gishmo.gwt.mvp4g2.jsinterop.client.core.css.CssValue;

/**
 * Indicating which type of unit applies to a value.
 */
public enum CssValueType {
  /**
   * The value is inherited and the cssText contains "inherit".
   */
  INHERIT(0),
  /**
   * The value is a primitive value and an instance of the CSSPrimitiveValue interface
   * can be obtained by using binding-specific casting methods on this instance of the
   * CSSValue interface.
   */
  PRIMITIVE_VALUE(1),
  /**
   * The value is a CSSValue list and an instance of the CSSValueList interface can be
   * obtained by using binding-specific casting methods on this instance of the CSSValue
   * interface.
   */
  VALUE_LIST(2),
  /**
   * The value is a custom value.
   */
  CUSTOM(3);

  private int typeId;

  private CssValueType(int typeId) {
    this.typeId = typeId;
  }

  /**
   * @param type
   *   The type represented as integer as it is returned by {@link CssValue#getCssValueType()}
   *
   * @return The CssValueType matching the given type id
   */
  public static CssValueType fromInt(int type) {
    CssValueType valueType = CssValueType.INHERIT;
    for (CssValueType t : values()) {
      if (t.getTypeId() == type) {
        valueType = t;
        break;
      }
    }

    return valueType;
  }

  /**
   * @return Returns the type id for the current CssRuleType
   */
  public int getTypeId() {
    return typeId;
  }
}