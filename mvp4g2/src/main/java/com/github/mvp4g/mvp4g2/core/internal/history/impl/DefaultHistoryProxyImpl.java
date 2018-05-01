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

package com.github.mvp4g.mvp4g2.core.internal.history.impl;

import com.github.mvp4g.mvp4g2.core.internal.Mvp4g2InternalUse;
import com.github.mvp4g.mvp4g2.core.internal.history.IsHistoryProxy;
import com.github.mvp4g.mvp4g2.core.internal.history.PopStateHandler;
import elemental2.dom.DomGlobal;
import elemental2.dom.PopStateEvent;

@Mvp4g2InternalUse
public class DefaultHistoryProxyImpl
  implements IsHistoryProxy {

  @Override
  public void addPopStateListener(PopStateHandler handler) {
    DomGlobal.window.addEventListener("popstate",
                                      (e) -> handler.onPopState((PopStateEvent) e));
  }

  @Override
  public String getLocation() {
    return DomGlobal.window.location.toString();
  }

  @Override
  public void pushState(String param,
                        String title,
                        String url) {
    DomGlobal.window.history.pushState(param,
                                       "",
                                       url);
  }

}
