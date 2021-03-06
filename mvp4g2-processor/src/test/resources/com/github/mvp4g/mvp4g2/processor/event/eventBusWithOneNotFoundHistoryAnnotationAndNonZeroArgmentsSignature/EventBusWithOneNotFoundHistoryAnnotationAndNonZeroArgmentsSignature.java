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

package com.github.mvp4g.mvp4g2.processor.event.eventBusWithOneNotFoundHistoryAnnotationAndNonZeroArgmentsSignature;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Event;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.EventBus;
import com.github.mvp4g.mvp4g2.core.history.annotation.NotFoundHistory;

@EventBus(shell = MockShellPresenter.class)
public interface EventBusWithOneNotFoundHistoryAnnotationAndNonZeroArgmentsSignature
  extends IsEventBus {

  @NotFoundHistory
  @Event(handlers = MockShellPresenter.class)
  public void event01(String arg01);

}
