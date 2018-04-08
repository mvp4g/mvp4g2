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

package com.github.mvp4g.mvp4g2.core.internal.application;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.history.PlaceService;
import com.github.mvp4g.mvp4g2.core.application.IsApplication;
import com.github.mvp4g.mvp4g2.core.application.IsApplicationLoader;

/**
 * generator of the eventBus
 */
public abstract class AbstractApplication<E extends IsEventBus>
  implements IsApplication {

  /* the eventbus */
  protected E                                  eventBus;
  /* flag if we have to check history token at the start of the application */
  protected boolean                            historyOnStart;
  /* the PlaceService */
  private   PlaceService<? extends IsEventBus> placeService;

  @Override
  public void run() {
    // execute the loader (if one is present)
    getApplicationLoader().load(() -> onFinishLaoding());
  }

  protected abstract IsApplicationLoader getApplicationLoader();

  /**
   * Once the loader did his job, we will continue
   */
  private void onFinishLaoding() {
    // create place service and bind
    this.placeService = new PlaceService<E>(this.eventBus,
                                            historyOnStart);
    this.eventBus.setPlaceService(this.placeService);
    // start the application
    placeService.startApplication();
  }

}
