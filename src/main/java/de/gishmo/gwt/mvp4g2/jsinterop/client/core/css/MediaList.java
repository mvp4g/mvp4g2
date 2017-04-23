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
 * The MediaList interface provides the abstraction of an ordered collection of media, without defining or
 * constraining how this collection is implemented. An empty list is the same as a list that contains the
 * medium "all".
 * </p>
 * <p>
 * The items in the MediaList are accessible via an integral index, starting from 0.
 * </p>
 */
@JsType
public interface MediaList {

  // Object at(int index); TODO: How to do this!?

  /**
   * @return The number of media in the list.
   */
  @JsProperty
  int getLength();

  /**
   * @return The parsable textual representation of the media list.
   */
  @JsProperty
  String getMediaText();

  /**
   * @param mediaText
   *   The parsable textual representation of the media list.
   */
  @JsProperty
  void setMediaText(String mediaText);

  /**
   * Adds the medium newMedium to the end of the list.
   *
   * @param newMedium
   *   The medium to add to the end of the list
   */
  @JsMethod
  void appendMedium(String newMedium);

  /**
   * Deletes the medium indicated by oldMedium from the list.
   *
   * @param oldMedium
   *   The medium to delete
   */
  @JsMethod
  void deleteMedium(String oldMedium);

  /**
   * Returns the medium with the given index.
   *
   * @param index
   *   The index to get the medium for
   *
   * @return Returns the medium with the given index.
   */
  @JsMethod
  String item(int index);

}