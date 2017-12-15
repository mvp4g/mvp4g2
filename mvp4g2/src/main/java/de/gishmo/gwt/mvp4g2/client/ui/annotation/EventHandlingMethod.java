/*
 * Copyright (c) 2009 - 2017 - Pierre-Laurent Coirer, Frank Hossfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.gishmo.gwt.mvp4g2.client.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.history.IsHistoryConverter;
import de.gishmo.gwt.mvp4g2.client.ui.IsEventHandler;

/**
 * This method should be used to annotate a event handling method inside a EventHandler/Presenter.
 * The EventHandler/Presenter has to have a @EventHandler/@Presenter annotation in order to get the
 * event handling processed.
 *
 * The name of a event handling method must be the event name with a leading 'on' and needs to
 * have the signutare as the event!
 *
 * For example:
 *
 * To handle the event:
 *
 * void oneEvent(String arg0);
 *
 * the name of the event handling method should be:
 *
 * public void onOneEvent(String arg0);
 *
 * This annotation has no attributes.
 *
 *
 * @author Frank Hossfeld
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandlingMethod {
}
