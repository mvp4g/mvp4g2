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

package com.github.mvp4g.mvp4g2.core.internal.ui;

import com.github.mvp4g.mvp4g2.core.internal.Mvp4g2InternalUse;
import com.github.mvp4g.mvp4g2.core.ui.IsHandler;

/**
 * generator of the eventBus
 *
 * @param <P> the meta data event handler
 */
@Mvp4g2InternalUse
public abstract class HandlerMetaData<P extends IsHandler<?>>
  extends AbstractHandlerMetaData {

  private P handler;

  public HandlerMetaData(String canonicalName,
                         HandlerMetaData.Kind kind,
                         P handler) {
    super(canonicalName,
          kind);
    this.handler = handler;
  }

  public P getHandler() {
    return handler;
  }
}
