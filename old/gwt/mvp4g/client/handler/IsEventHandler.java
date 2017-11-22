/*
 * Copyright 2010 Pierre-Laurent Coirier
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwt.mvp4g.client.handler;

import gwt.mvp4g.client.event.IsEventBus;

/**
 * Interface that defines an event handler.<br>
 * <br>
 * This interface provides getter and setter for the event bus.<br>
 * <br>
 * It is recommended to use directly <code>BaseEventHandler</code>.
 *
 * @author Frank Hossfeld
 * @since 16.09.2016
 */
public interface IsEventHandler<E extends IsEventBus> {
//
//  /**
//   * Get the event bus associated with the event handler
//   *
//   * @return eventBus manipulated by the event handler.
//   */
//  E getEventBus();
//
//  /**
//   * Set an event bus to the event handler
//   *
//   * @param eventBus event bus to set
//   */
//  void setEventBus(E eventBus);
//
//  /**
//   * Get the event bus associated with the event handler but in token generation mode.
//   * <br>
//   * The next event fire won't be fired but its token will be returned.
//   *
//   * @return eventBus manipulated by the event handler.
//   */
//  E getTokenGenerator();
//
//  /**
//   * Call by the event bus when the handler handles its first event.
//   */
//  void bind();
//
//  /**
//   * Indicates if the presenter is activated or not. If the event handler is activated and if the
//   * bind method hasn't been called yet, then the bind method should be called only if the event
//   * is active.<br>
//   * <br>
//   * If the event is passive and the presenter hasn't been built, the presenter will be consider
//   * as inactive (ie isActivated will return false).
//   *
//   * @param passive indicate if it's a passive event or not
//   * @return true if the presenter is activated, false otherwise
//   */
//  boolean isActivated(boolean passive,
//                      String eventName,
//                      Object... parameters);
//
//  /**
//   * Set if the event handler is activated or not. By default, an event handler should be
//   * activated.
//   *
//   * @param activated new activation parameter
//   */
//  void setActivated(boolean activated);

}
