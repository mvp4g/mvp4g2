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

package de.gishmo.gwt.mvp4g2.jsinterop.client.core.events;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true)
public interface Event {

  @JsProperty
  Detail getDetail();

  @JsProperty
  EventTarget getTarget();

  /**
   * Cancels the event (if it is cancelable).
   */
  void preventDefault();

  /**
   * For this particular event, no other listener will be called. Neither
   * those attached on the same element, nor those attached on elements which
   * will be traversed later (in capture phase, for instance)
   */
  void stopImmediatePropagation();

  /**
   * Stops the propagation of events further along in the DOM.
   */
  void stopPropagation();

  interface Events {
    String CLICK                    = "click";
    String CONTEXTMENU              = "contextmenu";
    String DBLCLICK                 = "dblclick";
    String CHANGE                   = "change";
    String MOUSEDOWN                = "mousedown";
    String MOUSEMOVE                = "mousemove";
    String MOUSEOUT                 = "mouseout";
    String MOUSEOVER                = "mouseover";
    String MOUSEUP                  = "mouseup";
    String MOUSEWHEEL               = "mousewheel";
    String FOCUS                    = "focus";
    String FOCUSIN                  = "focusin";
    String FOCUSOUT                 = "focusout";
    String BLUR                     = "blur";
    String KEYDOWN                  = "keydown";
    String KEYPRESS                 = "keypress";
    String KEYUP                    = "keyup";
    String SCROLL                   = "scroll";
    String BEFORECUT                = "beforecut";
    String CUT                      = "cut";
    String BEFORECOPY               = "beforecopy";
    String COPY                     = "copy";
    String BEFOREPASTE              = "beforepaste";
    String PASTE                    = "paste";
    String DRAGENTER                = "dragenter";
    String DRAGOVER                 = "dragover";
    String DRAGLEAVE                = "dragleave";
    String DROP                     = "drop";
    String DRAGSTART                = "dragstart";
    String DRAG                     = "drag";
    String DRAGEND                  = "dragend";
    String RESIZE                   = "resize";
    String SELECTSTART              = "selectstart";
    String SUBMIT                   = "submit";
    String ERROR                    = "error";
    String WEBKITANIMATIONSTART     = "webkitAnimationStart";
    String WEBKITANIMATIONITERATION = "webkitAnimationIteration";
    String WEBKITANIMATIONEND       = "webkitAnimationEnd";
    String WEBKITTRANSITIONEND      = "webkitTransitionEnd";
    String INPUT                    = "input";
    String INVALID                  = "invalid";
    String TOUCHSTART               = "touchstart";
    String TOUCHMOVE                = "touchmove";
    String TOUCHEND                 = "touchend";
    String TOUCHCANCEL              = "touchcancel";
  }

  @JsType(isNative = true)
  interface Detail {
  }

}
