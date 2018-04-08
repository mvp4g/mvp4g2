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

import com.github.mvp4g.mvp4g2.core.ui.IsLazyReverseView;
import com.github.mvp4g.mvp4g2.core.ui.IsPresenter;
import com.github.mvp4g.mvp4g2.core.ui.annotation.Presenter;

/**
 * Preenter meta data information.
 *
 * @param <P> the meta data presenter
 * @param <P> the meta data view
 */
public abstract class PresenterMetaData<P extends IsPresenter<?, ?>, V extends IsLazyReverseView<?>>
  extends AbstractHandlerMetaData {

  protected P                              presenter;
  protected V                              view;
  private   boolean                        multiple;
  private   Presenter.VIEW_CREATION_METHOD viewCreationMethod;

  public PresenterMetaData(String canonicalName,
                           Kind kind,
                           boolean multiple,
                           Presenter.VIEW_CREATION_METHOD viewCreationMethod) {
    super(canonicalName,
          kind);
    this.multiple = multiple;
    this.viewCreationMethod = viewCreationMethod;
  }

  public P getPresenter() {
    return presenter;
  }

  public void setPresenter(P presenter) {
    this.presenter = presenter;
  }

  public V getView() {
    return view;
  }

  public void setView(V view) {
    this.view = view;
  }

  public boolean isMultiple() {
    return multiple;
  }

  public Presenter.VIEW_CREATION_METHOD getViewCreationMethod() {
    return viewCreationMethod;
  }
}
