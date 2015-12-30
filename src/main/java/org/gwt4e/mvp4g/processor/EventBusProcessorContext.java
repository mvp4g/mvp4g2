/*
 * Copyright (C) 2015 Frank Hossfeld
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

package org.gwt4e.mvp4g.processor;

import org.gwt4e.mvp4g.processor.context.EventBusContext;
import org.gwt4e.mvp4g.processor.context.EventContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventBusProcessorContext {

  /* map of annotated eventBus */
  private Map<String, EventBusContext>           generatorContextEventBusMap;
  /* map of annotated events */
  private Map<String, Map<String, EventContext>> generatorContextEventMap;

//------------------------------------------------------------------------------

  EventBusProcessorContext() {
    super();

    generatorContextEventMap = new HashMap<>();
    generatorContextEventBusMap = new HashMap<>();
  }

//------------------------------------------------------------------------------

  public Map<String, EventBusContext> getEventBusContextMap() {
    return generatorContextEventBusMap;
  }

  public Map<String, EventContext> getEventContextMap(String eventBusName) {
    return this.generatorContextEventMap.get(eventBusName);
  }

  public Map<String, Map<String, EventContext>> getEventContextMap() {
    return generatorContextEventMap;
  }

  public void put(String eventBusName,
                  String eventName,
                  EventContext context) {
    if (!generatorContextEventMap.containsKey(eventBusName)) {
      generatorContextEventMap.put(eventBusName,
                                   new HashMap<String, EventContext>());
    }
    generatorContextEventMap.get(eventBusName)
                            .put(eventName,
                                 context);
  }

  /**
   * Method checks weather the eventname is unique or not.
   * <br>
   * This is a requirement because based on the event name a
   * MVP4GEvent will be generated.
   *
   * @param eventName name of the event
   * @return
   */
  public boolean isEventNameUnique(String eventName) {
    Iterator<String> eventBusNames = generatorContextEventMap.keySet().iterator();

    return true;
  }
}
