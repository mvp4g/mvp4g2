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

package org.gwt4e.mvp4g.test.apt.eventbus.generated.events;

  import java.lang.Override;
  import org.gwt4e.event.shared.Mvp4gInternalEvent;

public class OneEventMvp4gInternalEvent extends Mvp4gInternalEvent<OneEventMvp4gInternalEventHandler> {
  public static Mvp4gInternalEvent.Type TYPE = new Mvp4gInternalEvent.Type<OneEventMvp4gInternalEventHandler>();

  public OneEventMvp4gInternalEvent() {
  }

  @Override
  public Mvp4gInternalEvent.Type<OneEventMvp4gInternalEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(OneEventMvp4gInternalEventHandler handler) {
    handler.onOneEvent(this);
  }
}
