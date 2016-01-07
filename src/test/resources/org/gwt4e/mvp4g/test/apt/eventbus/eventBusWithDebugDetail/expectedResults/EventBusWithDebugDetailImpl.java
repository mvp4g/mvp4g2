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

package org.gwt4e.mvp4g.test.apt.eventbus.generated;

import java.lang.Override;
import java.lang.String;
import org.gwt4e.event.shared.SimpleMvp4gInternalEventBus;
import org.gwt4e.mvp4g.client.event.AbstractMvp4gEventBus;
import org.gwt4e.mvp4g.client.event.DefaultMvp4gLogger;
import org.gwt4e.mvp4g.test.apt.eventbus.EventBusWithDebugSimple;
import org.gwt4e.mvp4g.test.apt.eventbus.generated.events.OneEventMvp4gInternalEvent;

public final class EventBusWithDebugSimpleImpl extends AbstractMvp4gEventBus implements EventBusWithDebugSimple {
  private DefaultMvp4gLogger logger;

  public EventBusWithDebugSimpleImpl(String moduleName, SimpleMvp4gInternalEventBus eventBus) {
    super(moduleName, eventBus);
  }

  @Override
  public void oneEvent() {
    logger.log("MVP4G-Logger: firing event: oneEvent", 1);
    this.internalEventBus.fireEvent(new OneEventMvp4gInternalEvent());
  }
}
