package com.github.mvp4g.mvp4g2.core.internal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to mark classes, which will used by the framework.
 * <br>
 * <p>Do not use lasses annotated with internalFrameworkClass!</p>
 *
 * @author Frank Hossfeld
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ForInternalUseOnly {
}
