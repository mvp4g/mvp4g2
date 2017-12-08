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

package de.gishmo.gwt.mvp4g2.processor.event.eventTestHandlerNotInBindAndHandlersAttribute;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Event;

@EventBus(shell = MockShellPresenter.class)
public interface EventTestHandlerNotInBindAndHandlersAttribute
  extends IsEventBus {

  @Event(bind = MockShellPresenter.class)
  public void event01();

  @Event(handlers = MockShellPresenter.class)
  public void event02();

  @Event(bind = MockOneEventHandler.class,
    handlers = MockShellPresenter.class)
  public void event03();

}
