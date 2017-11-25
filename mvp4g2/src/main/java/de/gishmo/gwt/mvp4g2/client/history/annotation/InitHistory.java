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

package de.gishmo.gwt.mvp4g2.client.history.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be used to annotate the method of an event in your EventBus interface once you have history converters. It
 * indicates which event should be fired in case the history token is empty.
 * <br>
 * You must not have more than one <code>InitHistory</code> annotation in a class.<br>
 * <br>
 * You can only annotate methods with no parameter (ie only event with no object associated can be
 * used).
 *
 * @author plcoirier
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InitHistory {
}
