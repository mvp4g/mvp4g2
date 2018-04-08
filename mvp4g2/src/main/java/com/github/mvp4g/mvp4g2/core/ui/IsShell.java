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

/**
 * <p>Marks an class as mvp4g2 shell.</p>
 */
public interface IsShell<E extends IsEventBus, V extends IsLazyReverseView<?>>
  extends IsPresenter<E, V> {

  /**
   * <p>
   * This method is used by the framework, to delegate the adding
   * of the shell to the user. Here the user has to add the shell
   * of the application to the viewport.
   * </p>
   * <p>
   * It is a good idea to use a presenter/view pair as shell:
   * </p>
   * <code>
   *  public void setShell() {
   *    RootLayoutPanel.get().add(view.asWidget());
   * }
   * </code>
   * <p>This will make the framework indepent of GWT or user implemantations!</p>
   */
  void setShell();

}
