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
package com.github.mvp4g.mvp4g2.core.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This method should be used to annotate a event handling method inside a Handler/Presenter.
 * The Handler/Presenter has to have a @Handler/@Presenter annotation in order to get the
 * event handling processed.
 * <p>
 * The name of a event handling method must be the event name with a leading 'on' and needs to
 * have the signutare as the event!
 * <p>
 * For example:
 * <p>
 * To handle the event:
 * <p>
 * void oneEvent(String arg0);
 * <p>
 * the name of the event handling method should be:
 * <p>
 * public void onOneEvent(String arg0);
 * <p>
 * This annotation has no attributes.
 *
 * @author Frank Hossfeld
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
}
