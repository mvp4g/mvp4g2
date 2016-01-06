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

package org.gwt4e.mvp4g.test.apt.module.ModuleOK.generated;

import org.gwt4e.event.shared.SimpleMvp4gInternalEventBus;
import org.gwt4e.mvp4g.client.module.AbstractMvp4gModule;
import org.gwt4e.mvp4g.test.apt.module.ModuleOK.ModuleOKEventBus;

public final class ModuleOKModuleImpl extends AbstractMvp4gModule {
  private ModuleOKEventBus eventBusModule;

  public ModuleOKModuleImpl(SimpleMvp4gInternalEventBus eventBus) {
    super(eventBus);
    eventBusModule = new ModuleOKEventBusImpl("org.gwt4e.mvp4g.test.apt.module.ModuleOK.ModuleOKEventBus", getInternalEventBus());
  }

  public ModuleOKEventBus getModuleEventBus() {
    return eventBusModule;
  }
}