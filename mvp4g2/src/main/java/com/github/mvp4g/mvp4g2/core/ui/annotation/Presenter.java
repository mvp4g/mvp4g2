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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.github.mvp4g.mvp4g2.core.ui.IsLazyReverseView;
import com.github.mvp4g.mvp4g2.core.ui.LazyReverseView;

/**
 * This annotation allows to define a presenter for the framework.<br>
 * <br>
 * To define the presenter, you need to indicate the class of the view to inject in the presenter
 * thanks to the attribute <i>view</i>. The framework will automatically create one instance of the
 * view for each presenter.<br>
 * <br>
 * You can activate the multiple feature to create several instance of the same presenter. <br>
 *
 * @author Frank Hossfeld
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Presenter {

  Class<? extends IsLazyReverseView<?>> viewInterface();

  Class<? extends LazyReverseView<?>> viewClass();

  boolean multiple() default false;

  Presenter.VIEW_CREATION_METHOD viewCreator() default VIEW_CREATION_METHOD.FRAMEWORK;

  enum VIEW_CREATION_METHOD {
    FRAMEWORK,
    PRESENTER
  }
}
