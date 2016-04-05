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

package org.gwt4e.mvp4g.processor;

import org.gwt4e.mvp4g.processor.context.ApplicationContext;
import org.gwt4e.mvp4g.processor.context.EventBusContext;
import org.gwt4e.mvp4g.processor.context.EventContext;
import org.gwt4e.mvp4g.processor.context.ModuleContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProcessorContext {

  /* applicaiton context */
  private ApplicationContext                     applicationContext;
  /* module context */
  private Map<String, ModuleContext>             moduleContext;
  /* map of annotated eventBus */
  private Map<String, EventBusContext>           eventBusContextMap;
  /* map of annotated events */
  private Map<String, Map<String, EventContext>> eventContextMap;

  ProcessorContext() {
    super();
    moduleContext = new HashMap<>();
    eventContextMap = new HashMap<>();
    eventBusContextMap = new HashMap<>();
  }

  public Map<String, EventBusContext> getEventBusContextMap() {
    return eventBusContextMap;
  }

  public Map<String, EventContext> getEventContextMap(String eventBusName) {
    return this.eventContextMap.get(eventBusName);
  }

  public Map<String, Map<String, EventContext>> getEventContextMap() {
    return eventContextMap;
  }

  public void put(String eventBusName,
                  String eventName,
                  EventContext context) {
    if (!eventContextMap.containsKey(eventBusName)) {
      eventContextMap.put(eventBusName,
                          new HashMap<String, EventContext>());
    }
    eventContextMap.get(eventBusName)
                   .put(eventName,
                        context);
  }

  public void put(String moduleName,
                  ModuleContext context) {
    if (!moduleContext.containsKey(moduleName)) {
      moduleContext.put(moduleName,
                        context);
    }
  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
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
    Iterator<String> eventBusNames = eventContextMap.keySet()
                                                    .iterator();

    return true;
  }
}
