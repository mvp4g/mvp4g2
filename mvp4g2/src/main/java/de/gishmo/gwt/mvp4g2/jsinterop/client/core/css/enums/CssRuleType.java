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

import de.gishmo.gwt.mvp4g2.jsinterop.client.core.css.CssRule;

/**
 * The CSSRule interface specifies integer constants that can be used in conjunction with a CSSRule's type
 * property to discern the rule type (and therefore, which specialized interface it implements).
 */
public enum CssRuleType {
  UNKNOWN_RULE(0),
  STYLE_RULE(1),
  CHARSET_RULE(2),
  IMPORT_RULE(3),
  MEDIA_RULE(4),
  FONT_FACE_RULE(5),
  PAGE_RULE(6),
  WEBKIT_KEYFRAMES_RULE(7),
  WEBKIT_KEYFRAME_RULE(8);

  private int typeId;

  private CssRuleType(int typeId) {
    this.typeId = typeId;
  }

  /**
   * @param type
   *   The type represented as integer as it is returned by {@link CssRule#getType()}
   *
   * @return The CssRuleType matching the given type id
   */
  public static CssRuleType fromInt(int type) {
    CssRuleType valueType = CssRuleType.UNKNOWN_RULE;
    for (CssRuleType t : values()) {
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