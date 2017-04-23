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
 * <p>
 * An object implementing the CSSStyleSheet interface represents a single CSS style sheet.
 * </p>
 * <p>
 * A CSS style sheet consists of CSS rules, each of which can be manipulated through an object that
 * corresponds to that rule and that implements the CSSStyleRule interface, which in turn implements CSSRule.
 * The CSSStyleSheet itself lets you examine and modify its corresponding style sheet, including its list of
 * rules.
 * </p>
 * <p>
 * In practice, every CSSStyleSheet also implements the more generic StyleSheet interface. A list of
 * CSSStyleSheet-implementing objects corresponding to the style sheets for a given document can be reached by
 * the document.styleSheets property, if the document is styled by an external CSS style sheet or an inline
 * style element.
 * </p>
 */
@JsType(isNative = true)
public interface CssStyleSheet
  extends StyleSheet {

  /**
   * The list of all CSS rules contained within the style sheet
   *
   * @return Returns a CSSRuleList of the CSS rules in the style sheet.
   */
  @JsProperty
  CssRuleList getCssRules();

  /**
   * If this style sheet comes from an @import rule, the ownerRule attribute will contain the CSSImportRule.
   * In that case, the ownerNode attribute in the StyleSheet interface will be null. If the style sheet
   * comes from an element or a processing instruction, the ownerRule attribute will be null and the
   * ownerNode attribute will contain the Node.
   *
   * @return If this style sheet is imported into the document using an @import rule, the ownerRule property
   * will return that CSSImportRule, otherwise it returns null
   */
  @JsProperty
  CssRule getOwnerRule();

  /**
   * Used to delete a rule from the style sheet.
   *
   * @param index
   *   The index within the style sheet's rule list of the rule to remove.
   */
  @JsMethod
  void deleteRule(int index);

  /**
   * Used to insert a new rule into the style sheet. The new rule now becomes part of the cascade.
   *
   * @param rule
   *   The parsable text representing the rule. For rule sets this contains both the selector and
   *   the style declaration. For at-rules, this specifies both the at-identifier and the rule
   *   content.
   * @param index
   *   The index within the style sheet's rule list of the rule before which to insert the
   *   specified rule. If the specified index is equal to the length of the style sheet's rule
   *   collection, the rule will be added to the end of the style sheet.
   *
   * @return The index within the style sheet's rule collection of the newly inserted rule
   */
  @JsMethod
  int insertRule(String rule,
                 int index);

}