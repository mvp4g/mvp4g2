package de.gishmo.gwt.mvp4g2.debugid.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>DebugId-Annotation</p>
 * <p>This annotation marks a widget to set the debugId.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DebugId {

  /**
   * <p>The DebugId of the widget</p>
   *
   * @return debugId
   */
  String value();

  /**
   * <p>Attrbute-Key of the debugId</p>
   *
   * @return attribute-key
   */
  String attributeName() default "mvp4g2-gebugId";


}
