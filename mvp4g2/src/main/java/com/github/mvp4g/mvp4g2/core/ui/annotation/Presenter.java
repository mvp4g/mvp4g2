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
