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
package com.github.mvp4g.mvp4g2.core.eventbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.history.IsHistoryConverter;
import com.github.mvp4g.mvp4g2.core.ui.IsHandler;

/**
 * This annotation should be used to annotate methods of interfaces that extends
 * <code>IsEventBus</code> in order to define event.<br>
 * <br>
 * The annotation has the following attributes:
 * <br>
 * <ul>
 * <li> bind: classes that need to be bound when this event occurs. You can have zero to several classes
 * for an event. </li>
 * <li>handlers: classes of the handlers of this event. You can have zero to several handlers for an
 * event.</li>
 * <li>historyConverter: class of the history converter that should be used to store the event in
 * browse history. If no history converter is specified, event won't be stored in browse history.
 * You can define only one history converter for each event.
 * <li>historyName: name of the event that should be stored in the history token. By default, this
 * name is equal to the name of the event's method.
 * <li>navigationEvent: indicates that when this event is fired, a navigation control is done to
 * verify the event can be fired. Usually a navigation event is an event that will change the
 * displayed screen.</li>
 * <li>passive: when an event is fired, it will build any handlers not built yet and/or load any
 * child modules not loaded yet expect if the event is passive.</li>
 * <li>activate: classes of handlers that should be activated with this event. You can activate zero
 * to several handlers. Handlers to activate don't have to handle the event.</li>
 * <li>deactivate: classes of handlers that should be deactivated with this event. You can activate
 * zero to several handlers. Handlers to deactivate must not handle the event.</li>
 * </ul>
 *
 * @author Frank Hossfeld
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Event {

  //default name that developers are unlikely to enter to know when method name should be used
  String DEFAULT_HISTORY_NAME = "#%!|&*+!##%$";

  String historyName() default DEFAULT_HISTORY_NAME;

  Class<? extends IsHandler<? extends IsEventBus>>[] bind() default {};

  Class<? extends IsHandler<? extends IsEventBus>>[] handlers() default {};

  Class<? extends IsHistoryConverter<? extends IsEventBus>> historyConverter() default NoHistoryConverter.class;

  boolean navigationEvent() default false;

  boolean passive() default false;

  Class<? extends IsHandler<? extends IsEventBus>>[] deactivate() default {};

  Class<? extends IsHandler<? extends IsEventBus>>[] activate() default {};

  class NoHistoryConverter
    implements IsHistoryConverter<IsEventBus> {

    private NoHistoryConverter() {
      //to prevent this class to be used
    }

    @Override
    public void convertFromToken(String historyName,
                                 String param,
                                 IsEventBus eventBus) {
    }

    public boolean isCrawlable() {
      return false;
    }
  }
}
