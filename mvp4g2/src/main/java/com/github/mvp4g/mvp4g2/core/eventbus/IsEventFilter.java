/*
 * Copyright (c) 2018 - Frank Hossfeld
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 *
 */

package com.github.mvp4g.mvp4g2.core.eventbus;

/**
 * This interface can be implemented in order to receive events all events from an EventBus.<br>
 * <br>
 * Classes that implement this interface are associated with an EventBus class by adding the<br>
 * filter class literal to the filterClasses list in the EventBus' @Events annotation<br>
 *
 * @author Nick Hebner
 */
public interface IsEventFilter<E extends IsEventBus> {

  /**
   * Filter an event
   *
   * @param eventBus  event bus used to fire the event
   * @param eventName name of the event to filter
   * @param params    objects sent with the event
   * @return false if event should be stopped, true otherwise
   */
  boolean filterEvent(E eventBus,
                      String eventName,
                      Object... params);

}
