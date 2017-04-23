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

import de.gishmo.gwt.mvp4g2.jsinterop.client.core.css.enums.CssRuleType;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * <p>
 * An object implementing the <code>CSSRule</code> DOM interface represents a single CSS rule. References to a
 * <code>CSSRule</code>-implementing object may be obtained by looking at a
 * <a title="en/DOM/stylesheet" rel="internal" href="https://developer.mozilla.org/en/DOM/CSSStyleSheet">CSS
 * style sheet's</a>
 * <code><a title="en/DOM/CSSStyleSheet/cssRules" rel="internal" href="https://developer.mozilla.org/en/DOM/CSSStyleSheet">cssRules</a></code>
 * list.
 * </p>
 * <p>
 * There are several kinds of rules. The <code>CSSRule</code> interface specifies the properties common to all
 * rules, while properties unique to specific rule types are specified in the more specialized interfaces for
 * those rules' respective types.
 * </p>
 */
@JsType(isNative = true)
public interface CssRule {

  /**
   * Returns the textual representation of the rule, e.g. <code>"h1,h2 { font-size: 16pt }"</code>
   *
   * @return The textual representation of the rule
   */
  @JsProperty
  String getCssText();

  /**
   * Sets the textual representation of the rule, e.g. <code>"h1,h2 { font-size: 16pt }"</code>
   *
   * @param cssText
   *   The textual representation of the rule
   */
  @JsProperty
  void setCssText(String cssText);

  /**
   * Returns the containing rule, otherwise <code>null</code>. E.g. if this rule is a style rule inside an
   * <code><a title="en/CSS/@media" rel="internal" href="https://developer.mozilla.org/en/CSS/@media">@media</a></code>
   * block, the parent rule would be that
   * <code><a title="en/DOM/CSSMediaRule" rel="internal" href="https://developer.mozilla.org/en/DOM/CSSMediaRule">CSSMediaRule</a></code>
   * .
   */
  @JsProperty
  CssRule getParentRule();

  /**
   * Returns the stylesheet object in which the current rule is defined.
   *
   * @return The stylesheet object in which the current rule is defined.
   */
  @JsProperty
  CssStyleSheet getParentStyleSheet();

  /**
   * One of the Type constants indicating the type of CSS rule.
   *
   * @return The Type constants indicating the type of CSS rule.
   *
   * @see CssRuleType
   */
  @JsProperty
  int getType();

}