package org.gwt4e.mvp4g.test.apt.eventbus.generated.events;

import java.lang.Override;
import org.gwt4e.event.shared.Mvp4gInternalEvent;

public final class OneEventMvp4gInternalEvent extends Mvp4gInternalEvent<OneEventMvp4gInternalEventHandler> {
  public static Mvp4gInternalEvent.Type TYPE = new Mvp4gInternalEvent.Type<OneEventMvp4gInternalEventHandler>();

  public OneEventMvp4gInternalEvent() {
  }

  @Override
  public Mvp4gInternalEvent.Type<OneEventMvp4gInternalEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(OneEventMvp4gInternalEventHandler handler) {
    handler.onOneEvent(this);
  }
}
