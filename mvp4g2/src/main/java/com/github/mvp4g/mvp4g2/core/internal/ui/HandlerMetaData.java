package com.github.mvp4g.mvp4g2.core.internal.ui;

import com.github.mvp4g.mvp4g2.core.ui.IsHandler;

/**
 * generator of the eventBus
 *
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
