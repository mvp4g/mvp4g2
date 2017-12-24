package de.gishmo.gwt.mvp4g2.client.internal.ui;

import de.gishmo.gwt.mvp4g2.client.ui.IsHandler;

/**
 * generator of the eventBus

 * @param <P> the meta data event handler
 */
public abstract class HandlerMetaData<P extends IsHandler<?>>
  extends AbstractHandlerMetaData {

  private P handler;

  public HandlerMetaData(String canonicalName,
                         HandlerMetaData.Kind kind,
                         P handler) {
    super(canonicalName,
          kind);
    this.handler = handler;
  }

  public P getHandler() {
    return handler;
  }
}
