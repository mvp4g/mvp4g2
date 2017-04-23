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

package de.gishmo.gwt.mvp4g2.module.client;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import de.gishmo.gwt.mvp4g2.jsinterop.client.core.events.Event;
import de.gishmo.gwt.mvp4g2.module.client.event.Mvp4gInterModuleEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//
public class Mvp4gInterModuleEventBus {

  /* Liste der Mvp4gInterModuleEventHandler */
  private        Map<String, List<Mvp4gInterModuleEventHandler>> mvp4gInterModuleEventHandler;

  /**
   * <p>private constructor, creates the HashMap</p>
   */
  public Mvp4gInterModuleEventBus() {
    this.mvp4gInterModuleEventHandler = new HashMap<>();
  }

  /**
   * <p>Adds a handler to the list of handlers, which will be called
   * if a event of the given type is fired.</p>
   *
   * @param handler
   *   handler to be added to the Mvp4gInterModuleEventBus
   *
   * @return handler registration
   */
  public HandlerRegistration addHandler(Mvp4gInterModuleEventHandler handler) {
    // no type -> do nothing!
    if (handler.getType() == null) {
      return null;
    }
    // get the list of handlers for this type. if listOfHandlers is null, we have to declare it and the list to the map
    List<Mvp4gInterModuleEventHandler> listOfHandlers = this.mvp4gInterModuleEventHandler.computeIfAbsent(handler.getType(),
                                                                                                          k -> new ArrayList<>());
    // add the handler to the list of handlers for the given type
    listOfHandlers.add(handler);
    // TODO native add handler
    // TODO erzeuge Handlerregistration
    // TODO remveo natave geistration
    // copy list to enable using list in removeHandler method TODO Check, ob das geht
    return () -> listOfHandlers.remove(handler);
  }

  public void fireEvent(Mvp4gInterModuleEvent mvp4gInterModuleEvent) {
    // event to fire
    Event event = null;
    // since IE9 doesn't support constructor initialization
    if (Window.Navigator.getUserAgent().contains("MSIE ")) {

    } else {

    }
  }

}
