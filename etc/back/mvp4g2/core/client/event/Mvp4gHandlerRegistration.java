/*
 * Copyright (C) 2016 Frank Hossfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.gishmo.gwt.mvp4g2.core.client.event;

import de.gishmo.gwt.mvp4g2.core.client.annotation.NotUpdatedYet;

/**
 * Registration objects returned when an event handler is bound
 * (e.g. via {@link Mvp4gEventBus#addHandler}), used to deregister.
 * <p> A tip: to make a handler deregister itself try something
 * like the following:
 * <code><pre>new MyHandler() {
 *  Mvp4gInternalHandlerRegistration reg = MyEvent.register(eventBus, this);
 * <p/>
 *  public void onMyThing(MyEvent event) {
 *    {@literal /}* do your thing *{@literal /}
 *    reg.removeHandler();
 *  }
 * };
 * </pre></code>
 */
@NotUpdatedYet
public interface Mvp4gHandlerRegistration {

  /**
   * Deregisters the handler associated with this registration
   * object if the handler is still attached to the event source.
   * If the handler is no longer attached to the event source,
   * this is a no-op.
   */
  void removeHandler();
}
