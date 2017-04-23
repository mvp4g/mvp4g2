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

package de.gishmo.gwt.mvp4g2.jsinterop.client.core.html;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * The Storage interface of the Web Storage API provides access to the session storage or local storage for a
 * particular domain, allowing you to for example add, modify or delete stored data items.
 */
@JsType(isNative = true,
        namespace = JsPackage.GLOBAL,
        name = "Storage")
public interface Storage {

  /**
   * Returns an integer representing the number of data items stored in the Storage object.
   *
   * @return The number of data items stored in the Storage object.
   */
  @JsProperty
  int getLength();

  /**
   * Will empty all keys out of the storage.
   */
  void clear();

  /**
   * Returns the value for the given key which is currently stored in the storage
   *
   * @param key The name of the key you want to retrieve the value of.
   * @return The value of the key. If the key does not exist, null is returned.
   */
  String getItem(String key);

  /**
   * It will return the name of the nth key in the storage.
   *
   * @param index The number of the key you want to get the name of. This is a zero-based index.
   * @return The name of the nth key in the storage.
   */
  String key(int index);

  /**
   * Removes the given key from the storage.
   *
   * @param key The name of the key you want to remove.
   */
  void removeItem(String key);

  /**
   * The method will add that key to the storage, or update that key's value if it already exists
   *
   * @param key  The key of the record which should be stored
   * @param data The data which should be stored for the given key.
   */
  void setItem(String key,
               String data);
}