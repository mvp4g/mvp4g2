package org.gwt4e.mvp4g.test.apt.eventbus.generated.events;

import java.lang.Override;
import java.lang.String;
import org.gwt4e.event.shared.Mvp4gInternalEvent;

public class TwoEventMvp4gInternalEvent extends Mvp4gInternalEvent<TwoEventMvp4gInternalEventHandler> {
  public static Mvp4gInternalEvent.Type TYPE = new Mvp4gInternalEvent.Type<TwoEventMvp4gInternalEventHandler>();

  private String arg0;

  public TwoEventMvp4gInternalEvent(String arg0) {
    this.arg0 = arg0;
  }

  public String getArg0() {
    return arg0;
  }

  public void setArg0(String arg0) {
    this.arg0 = arg0;
  }

  @Override
  public Mvp4gInternalEvent.Type<TwoEventMvp4gInternalEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(TwoEventMvp4gInternalEventHandler handler) {
    handler.onTwoEvent(this);
  }
}
