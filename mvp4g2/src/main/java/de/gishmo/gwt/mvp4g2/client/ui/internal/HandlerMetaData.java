package de.gishmo.gwt.mvp4g2.client.ui.internal;

/**
 * generator of the eventBus
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
