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

package org.gwt4e.mvp4g.client.event;


import org.gwt4e.event.shared.SimpleMvp4gInternalEventBus;

/**
 * <p>Core implementation of a MVP4G,2 event bus. This class contains methods
 * and variables that that are not generated.</p>
 */
public abstract class AbstractMvp4gEventBus {

  /* the GWT internalEventBus of the Application */
  final protected SimpleMvp4gInternalEventBus internalEventBus;
  /* name of the module which contains this eventbus */
  final protected String                      moduleName;

  public AbstractMvp4gEventBus(String moduleName,
                               SimpleMvp4gInternalEventBus internalEventBus) {
    super();

    this.moduleName = moduleName;
    this.internalEventBus = internalEventBus;
  }

//  public SimpleMvp4gInternalEventBus getInternalEventBus() {
//    return internalEventBus;
//  }
}
