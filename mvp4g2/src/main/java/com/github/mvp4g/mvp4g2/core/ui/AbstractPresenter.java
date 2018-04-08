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

package com.github.mvp4g.mvp4g2.core.ui;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;

public abstract class AbstractPresenter<E extends IsEventBus, V extends IsLazyReverseView<?>>
  extends AbstractHandler<E>
  implements IsPresenter<E, V> {

  protected V view = null;

  /*
   * (non-Javadoc)
   * @see com.mvp4g.core.presenter.PresenterInterface#getView()
   */
  public final V getView() {
    return view;
  }

  /*
   * (non-Javadoc)
   * @see com.mvp4g.core.presenter.PresenterInterface#setView(java.lang.Object)
   */
  public final void setView(V view) {
    this.view = view;
  }

}
