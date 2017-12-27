package de.gishmo.gwt.mvp4g2.core.internal.ui;

/**
 * generator of the eventbus
 */
public abstract class AbstractHandlerMetaData {

  private String                       canonicalName;
  private AbstractHandlerMetaData.Kind kind;

  public AbstractHandlerMetaData(String canonicalName,
                                 AbstractHandlerMetaData.Kind kind) {
    this.canonicalName = canonicalName;
    this.kind = kind;
  }

  public String getCanonicalName() {
    return canonicalName;
  }

  public Kind getKind() {
    return kind;
  }

  /**
   * Type of event handlers.
   */
  public enum Kind {
    EVENT_HANDLER,
    PRESENTER
  }
}
