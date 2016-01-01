package org.gwt4e.mvp4g.test.apt.eventbus.generated.events;

import java.lang.Override;
import org.gwt4e.event.shared.Mvp4gEvent;

public class OneEventMvp4gEvent extends Mvp4gEvent<OneEventMvp4gEventHandler> {
  public static Mvp4gEvent.Type TYPE = new Mvp4gEvent.Type<OneEventMvp4gEventHandler>();

  public OneEventMvp4gEvent() {
  }

  @Override
  public Mvp4gEvent.Type<OneEventMvp4gEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(OneEventMvp4gEventHandler handler) {
    handler.onOneEvent(this);
  }
}
