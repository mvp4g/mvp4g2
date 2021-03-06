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

public abstract class LazyReverseView<P>
  implements IsLazyReverseView<P> {

  protected P presenter;

  private boolean bound = false;


  public final P getPresenter() {
    return presenter;
  }

  public final void setPresenter(P presenter) {
    this.presenter = presenter;
  }

  public void bind() {
  }

  public boolean isBound() {
    return bound;
  }

  public void setBound(boolean bound) {
    this.bound = bound;
  }
}
