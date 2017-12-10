package de.gishmo.gwt.mvp4g2.client.internal.ui;

/**
 * generator of the eventbus
 */
public abstract class HandlerMetaData {

  private String               canonicalName;
  private HandlerMetaData.Kind kind;

  public HandlerMetaData(String canonicalName,
                         HandlerMetaData.Kind kind) {
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
