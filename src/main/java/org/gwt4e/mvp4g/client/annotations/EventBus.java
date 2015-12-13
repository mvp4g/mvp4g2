/*
 * Copyright (C) 2015 Frank Hossfeld
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

package org.gwt4e.mvp4g.client.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>This annotation indicates that the annotated interface should be used to define
 * a event bus of the Application. This annotation can be used only on interfaces
 * that extends <code>Mvp4gEventBus</code>.<br>
 * <br><br><br><br><br>
 *</p>
 *
 *
 * The annotation has the following attributes:
 * <ul>
 * <li>startPresenter: class of the presenter which view should be loaded when the module starts.</li>
 * <li>startPresenterName: you can also specify the name of the start presenter.</li>
 * <li>module: class of the module for which the annotated interface should be used to generate the
 * event bus. If no module is specified, it means that the interface should be used to generate the
 * Root Module (first module to be loaded and only module without parent).</li>
 * <li>historyOnStart: if true, the current history state will be fired when the Application starts.
 * </li>
 * <li>ginModules: classes of the GIN modules the framework should use when generating presenters,
 * event handlers, history converters &amp; views. You can specify zero to severals GIN modules.</li>
 * <li>ginModuleProperties: deferred property names used to retrieve the class of the GIN modules.
 * You can use ginModuleProperties and/or ginModules.</li>
 * </ul>
 *
 * @author Frank Hossfeld
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBus {

//  Class<? extends IsWidget> shell();

}
