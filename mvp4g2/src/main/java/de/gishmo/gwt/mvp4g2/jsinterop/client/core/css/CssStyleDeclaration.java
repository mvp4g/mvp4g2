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
 * CSSStyleDeclaration represents a collection of CSS property-value pairs
 */
@JsType(isNative = true)
public interface CssStyleDeclaration {

  /**
   * Getter for the cssText property
   *
   * @return Textual representation of the declaration block. Setting this attribute changes the style.
   */
  @JsProperty
  String getCssText();

  /**
   * Setter for cssText property
   *
   * @param cssText
   *   Textual representation of the declaration block. Setting this attribute changes the
   *   style.
   */
  @JsProperty
  void setCssText(String cssText);

  /**
   * Getter for length property
   *
   * @return The number of properties. See the <strong>item</strong> method below.
   */
  @JsProperty
  int getLength();

  /**
   * Getter for parentRule property
   *
   * @return The containing CssRule
   */
  @JsProperty
  CssRule getParentRule();

  /**
   * Returns the optional priority, "important". Example: priString= styleObj.getPropertyPriority('color')
   *
   * @param propertyName
   *   The name to get the priority for
   *
   * @return The optional priority
   */
  @JsMethod
  String getPropertyPriority(String propertyName);

  /**
   * Returns the property value. Example: valString= styleObj.getPropertyValue('color')
   *
   * @param propertyName
   *   The name to get the property value for
   *
   * @return The value for the given property
   */
  @JsMethod
  String getPropertyValue(String propertyName);

  /**
   * The item(index) method must return the property name of the CSS declaration at position index.
   *
   * @param index
   *   The position index to get the property name for
   *
   * @return The property name of the CSS declaration at position index
   */
  @JsMethod
  String item(int index);

  /**
   * Returns the value deleted. Example: valString= styleObj.removeProperty('color')
   *
   * @param propertyName
   *   The name of the property which should be removed
   *
   * @return The value which was set for the given property
   */
  @JsMethod
  String removeProperty(String propertyName);

  /**
   * Used to set a property
   *
   * @param propertyName
   *   The name of the property to set
   * @param value
   *   The value of the property to set
   */
  @JsMethod
  void setProperty(String propertyName,
                   String value);

  /**
   * Used to set a property
   *
   * @param propertyName
   *   The name of the property to set
   * @param value
   *   The value of the property to set
   * @param priority
   *   Optional parameter to specify the priority (e.g.: "important").
   */
  @JsMethod
  void setProperty(String propertyName,
                   String value,
                   String priority);

  /**
   * Getter for BackgroundColor property
   *
   * @return The BackgroundColor property
   */
  @JsProperty
  String getBackgroundColor();

  /**
   * Setter for BackgroundColor property
   *
   * @param value
   *   The value to set for the BackgroundColor property
   */
  @JsProperty
  void setBackgroundColor(String value);

  /**
   * Getter for BackgroundImage property
   *
   * @return The BackgroundImage property
   */
  @JsProperty
  String getBackgroundImage();

  /**
   * Setter for BackgroundImage property
   *
   * @param value
   *   The value to set for the BackgroundImage property
   */
  @JsProperty
  void setBackgroundImage(String value);

  /**
   * Getter for BorderColor property
   *
   * @return The BorderColor property
   */
  @JsProperty
  String getBorderColor();

  /**
   * Setter for BorderColor property
   *
   * @param value
   *   The value to set for the BorderColor property
   */
  @JsProperty
  void setBorderColor(String value);

  /**
   * Getter for BorderStyle property
   *
   * @return The BorderStyle property
   */
  @JsProperty
  String getBorderStyle();

  /**
   * Setter for BorderStyle property
   *
   * @param value
   *   The value to set for the BorderStyle property
   */
  @JsProperty
  void setBorderStyle(String value);

  /**
   * Getter for BorderWidth property
   *
   * @return The BorderWidth property
   */
  @JsProperty
  String getBorderWidth();

  /**
   * Setter for BorderWidth property
   *
   * @param value
   *   The value to set for the BorderWidth property
   */
  @JsProperty
  void setBorderWidth(String value);

  /**
   * Getter for Bottom property
   *
   * @return The Bottom property
   */
  @JsProperty
  String getBottom();

  /**
   * Setter for Bottom property
   *
   * @param value
   *   The value to set for the Bottom property
   */
  @JsProperty
  void setBottom(String value);

  /**
   * Getter for Color property
   *
   * @return The Color property
   */
  @JsProperty
  String getColor();

  /**
   * Setter for Color property
   *
   * @param value
   *   The value to set for the Color property
   */
  @JsProperty
  void setColor(String value);

  /**
   * Getter for Cursor property
   *
   * @return The Cursor property
   */
  @JsProperty
  String getCursor();

  /**
   * Setter for Cursor property
   *
   * @param value
   *   The value to set for the Cursor property
   */
  @JsProperty
  void setCursor(String value);

  /**
   * Getter for Display property
   *
   * @return The Display property
   */
  @JsProperty
  String getDisplay();

  /**
   * Setter for Display property
   *
   * @param value
   *   The value to set for the Display property
   */
  @JsProperty
  void setDisplay(String value);

  /**
   * Getter for FontSize property
   *
   * @return The FontSize property
   */
  @JsProperty
  String getFontSize();

  /**
   * Setter for FontSize property
   *
   * @param value
   *   The value to set for the FontSize property
   */
  @JsProperty
  void setFontSize(String value);

  /**
   * Getter for FontStyle property
   *
   * @return The FontStyle property
   */
  @JsProperty
  String getFontStyle();

  /**
   * Setter for FontStyle property
   *
   * @param value
   *   The value to set for the FontStyle property
   */
  @JsProperty
  void setFontStyle(String value);

  /**
   * Getter for FontWeight property
   *
   * @return The FontWeight property
   */
  @JsProperty
  String getFontWeight();

  /**
   * Setter for FontWeight property
   *
   * @param value
   *   The value to set for the FontWeight property
   */
  @JsProperty
  void setFontWeight(String value);

  /**
   * Getter for Height property
   *
   * @return The Height property
   */
  @JsProperty
  String getHeight();

  /**
   * Setter for Height property
   *
   * @param value
   *   The value to set for the Height property
   */
  @JsProperty
  void setHeight(String value);

  /**
   * Getter for Left property
   *
   * @return The Left property
   */
  @JsProperty
  String getLeft();

  /**
   * Setter for Left property
   *
   * @param value
   *   The value to set for the Left property
   */
  @JsProperty
  void setLeft(String value);

  /**
   * Getter for ListStyleType property
   *
   * @return The ListStyleType property
   */
  @JsProperty
  String getListStyleType();

  /**
   * Setter for ListStyleType property
   *
   * @param value
   *   The value to set for the ListStyleType property
   */
  @JsProperty
  void setListStyleType(String value);

  /**
   * Getter for Margin property
   *
   * @return The Margin property
   */
  @JsProperty
  String getMargin();

  /**
   * Setter for Margin property
   *
   * @param value
   *   The value to set for the Margin property
   */
  @JsProperty
  void setMargin(String value);

  /**
   * Getter for MarginBottom property
   *
   * @return The MarginBottom property
   */
  @JsProperty
  String getMarginBottom();

  /**
   * Setter for MarginBottom property
   *
   * @param value
   *   The value to set for the MarginBottom property
   */
  @JsProperty
  void setMarginBottom(String value);

  /**
   * Getter for MarginLeft property
   *
   * @return The MarginLeft property
   */
  @JsProperty
  String getMarginLeft();

  /**
   * Setter for MarginLeft property
   *
   * @param value
   *   The value to set for the MarginLeft property
   */
  @JsProperty
  void setMarginLeft(String value);

  /**
   * Getter for MarginRight property
   *
   * @return The MarginRight property
   */
  @JsProperty
  String getMarginRight();

  /**
   * Setter for MarginRight property
   *
   * @param value
   *   The value to set for the MarginRight property
   */
  @JsProperty
  void setMarginRight(String value);

  /**
   * Getter for MarginTop property
   *
   * @return The MarginTop property
   */
  @JsProperty
  String getMarginTop();

  /**
   * Setter for MarginTop property
   *
   * @param value
   *   The value to set for the MarginTop property
   */
  @JsProperty
  void setMarginTop(String value);

  /**
   * Getter for Opacity property
   *
   * @return The Opacity property
   */
  @JsProperty
  double getOpacity();

  /**
   * Setter for Opacity property
   *
   * @param value
   *   The value to set for the Opacity property
   */
  @JsProperty
  void setOpacity(double value);

  /**
   * Getter for Overflow property
   *
   * @return The Overflow property
   */
  @JsProperty
  String getOverflow();

  /**
   * Setter for Overflow property
   *
   * @param value
   *   The value to set for the Overflow property
   */
  @JsProperty
  void setOverflow(String value);

  /**
   * Getter for OverflowX property
   *
   * @return The OverflowX property
   */
  @JsProperty
  String getOverflowX();

  /**
   * Setter for OverflowX property
   *
   * @param value
   *   The value to set for the OverflowX property
   */
  @JsProperty
  void setOverflowX(String value);

  /**
   * Getter for OverflowY property
   *
   * @return The OverflowY property
   */
  @JsProperty
  String getOverflowY();

  /**
   * Setter for OverflowY property
   *
   * @param value
   *   The value to set for the OverflowY property
   */
  @JsProperty
  void setOverflowY(String value);

  /**
   * Getter for Padding property
   *
   * @return The Padding property
   */
  @JsProperty
  String getPadding();

  /**
   * Setter for Padding property
   *
   * @param value
   *   The value to set for the Padding property
   */
  @JsProperty
  void setPadding(String value);

  /**
   * Getter for PaddingBottom property
   *
   * @return The PaddingBottom property
   */
  @JsProperty
  String getPaddingBottom();

  /**
   * Setter for PaddingBottom property
   *
   * @param value
   *   The value to set for the PaddingBottom property
   */
  @JsProperty
  void setPaddingBottom(String value);

  /**
   * Getter for PaddingLeft property
   *
   * @return The PaddingLeft property
   */
  @JsProperty
  String getPaddingLeft();

  /**
   * Setter for PaddingLeft property
   *
   * @param value
   *   The value to set for the PaddingLeft property
   */
  @JsProperty
  void setPaddingLeft(String value);

  /**
   * Getter for PaddingRight property
   *
   * @return The PaddingRight property
   */
  @JsProperty
  String getPaddingRight();

  /**
   * Setter for PaddingRight property
   *
   * @param value
   *   The value to set for the PaddingRight property
   */
  @JsProperty
  void setPaddingRight(String value);

  /**
   * Getter for PaddingTop property
   *
   * @return The PaddingTop property
   */
  @JsProperty
  String getPaddingTop();

  /**
   * Setter for PaddingTop property
   *
   * @param value
   *   The value to set for the PaddingTop property
   */
  @JsProperty
  void setPaddingTop(String value);

  /**
   * Getter for Position property
   *
   * @return The Position property
   */
  @JsProperty
  String getPosition();

  /**
   * Setter for Position property
   *
   * @param value
   *   The value to set for the Position property
   */
  @JsProperty
  void setPosition(String value);

  /**
   * Getter for Right property
   *
   * @return The Right property
   */
  @JsProperty
  String getRight();

  /**
   * Setter for Right property
   *
   * @param value
   *   The value to set for the Right property
   */
  @JsProperty
  void setRight(String value);

  /**
   * Getter for TextDecoration property
   *
   * @return The TextDecoration property
   */
  @JsProperty
  String getTextDecoration();

  /**
   * Setter for TextDecoration property
   *
   * @param value
   *   The value to set for the TextDecoration property
   */
  @JsProperty
  void setTextDecoration(String value);

  /**
   * Getter for Top property
   *
   * @return The Top property
   */
  @JsProperty
  String getTop();

  /**
   * Setter for Top property
   *
   * @param value
   *   The value to set for the Top property
   */
  @JsProperty
  void setTop(String value);

  /**
   * Getter for Visibility property
   *
   * @return The Visibility property
   */
  @JsProperty
  String getVisibility();

  /**
   * Setter for Visibility property
   *
   * @param value
   *   The value to set for the Visibility property
   */
  @JsProperty
  void setVisibility(String value);

  /**
   * Getter for WhiteSpace property
   *
   * @return The WhiteSpace property
   */
  @JsProperty
  String getWhiteSpace();

  /**
   * Setter for WhiteSpace property
   *
   * @param value
   *   The value to set for the WhiteSpace property
   */
  @JsProperty
  void setWhiteSpace(String value);

  /**
   * Getter for Width property
   *
   * @return The Width property
   */
  @JsProperty
  String getWidth();

  /**
   * Setter for Width property
   *
   * @param value
   *   The value to set for the Width property
   */
  @JsProperty
  void setWidth(String value);

  /**
   * Getter for ZIndex property
   *
   * @return The ZIndex property
   */
  @JsProperty
  int getZIndex();

  /**
   * Setter for ZIndex property
   *
   * @param value
   *   The value to set for the ZIndex property
   */
  @JsProperty
  void setZIndex(int value);

}