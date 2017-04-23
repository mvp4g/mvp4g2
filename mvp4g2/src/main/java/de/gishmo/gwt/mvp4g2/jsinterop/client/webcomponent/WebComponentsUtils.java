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

package de.gishmo.gwt.mvp4g2.jsinterop.client.webcomponent;

import de.gishmo.gwt.mvp4g2.jsinterop.client.core.util.CallbackFunction;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Utility class to handle WebComponents
 */
public class WebComponentsUtils {

  /**
   * Native method to call <code>setTimeout(function,milliseconds)</code>
   *
   * @param function
   *   The function which should be executed after the given amount of milliseconds
   * @param milliseconds
   *   The milliseconds to wait before the given function is executed.
   */
  @JsMethod(name = "setTimeout",
            namespace = JsPackage.GLOBAL)
  public static native void setTimeout(CallbackFunction function,
                                       int milliseconds);

  /**
   * A workaround method to overcome a problem when trying to call a method on a webcomponent before it has
   * been upgraded. If you call a custom element specific method before the upgrade of the element has
   * finished it will just fail because the browser does not yet know the element details. Before upgrading
   * every element will be handled as a HTMLUnknownElement and therefore the set of available properties and
   * methods is very limited.<br>
   * The method implemented in pure JavaScript would look like:
   * <p>
   * <pre>
   * <code>
   * function tryWebComponentMethod(e, f){
   *     if(e !== undefined){
   *         if(e.is !== undefined){
   *             f();
   *         } else {
   *             setTimeout(function(){tryWebComponentMethod(e, f);}, 50);
   *         }
   *     }
   * }
   * </code>
   * </pre>
   *
   * @param element
   *   The webcomponent element
   * @param f
   *   The function to execute as soon as the element is upgraded
   */
  public static void tryWebComponentMethod(WebComponentElement element,
                                           CallbackFunction f) {
    if (element != null) {
      if (element.getIs() != null) {
        f.call();
      } else {
        setTimeout(() -> tryWebComponentMethod(element,
                                               f),
                   50);
      }
    }
  }

  /**
   * A utility class to get a reference to the global scoped WebComponents object. In "native" javascript
   * you would do something like:<br>
   * <p>
   * <pre>
   * <code>Window.WebComponents</code>
   * </pre>
   */
  @JsType(isNative = true)
  public static class WebcomponentsResolover {

    /**
     * Returns the global scoped WebComponents object.
     */
    @JsProperty(name = "WebComponents",
                namespace = JsPackage.GLOBAL)
    public static WebComponents webcomponents;
  }

}