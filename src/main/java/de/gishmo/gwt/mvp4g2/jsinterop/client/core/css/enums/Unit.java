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
 * Represents unit types which can be used for different CSS properties.
 */
public enum Unit {

  PX("px"),
  PCT("%"),
  EM("em"),
  EX("ex"),
  PT("pt"),
  PC("pc"),
  IN("in"),
  CM("cm"),
  MM("mm");

  private String unit;

  Unit(String unit) {
    this.unit = unit;
  }

  /**
   * Returns a string representation which can be passed as value to css properties with a given size and
   * the current unit.
   *
   * @param value
   *   The size which should be used for the unit
   *
   * @return A string which concatenates the given value and the current unit.
   * <code>Unit.PX.getUnit(100)</code> will return "100px"
   */
  public String getUnit(double value) {
    return value + getUnit();
  }

  /**
   * @return The string representation of the unit
   */
  public String getUnit() {
    return this.unit;
  }

}