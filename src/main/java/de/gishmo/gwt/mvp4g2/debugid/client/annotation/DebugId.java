package de.gishmo.gwt.mvp4g2.debugid.client.annotation;

import java.lang.annotation.*;

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

}
