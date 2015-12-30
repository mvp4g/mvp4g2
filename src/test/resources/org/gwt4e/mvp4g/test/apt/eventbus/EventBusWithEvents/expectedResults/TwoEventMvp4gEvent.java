package org.gwt4e.mvp4g.test.apt.eventbus.generated.events;

import java.lang.Override;
import java.lang.String;
import org.gwt4e.event.shared.Mvp4gEvent;

public class TwoEventMvp4gEvent extends Mvp4gEvent<TwoEventMvp4gEventHandler> {
  public static Mvp4gEvent.Type TYPE = new Mvp4gEvent.Type<TwoEventMvp4gEventHandler>();

  private String arg0;

  public TwoEventMvp4gEvent(String arg0) {
    this.arg0 = arg0;
  }

  public String getArg0() {
    return arg0;
  }

  public void setArg0(String arg0) {
    this.arg0 = arg0;
  }

  @Override
  public Mvp4gEvent.Type<TwoEventMvp4gEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(TwoEventMvp4gEventHandler handler) {
    handler.onTwoEvent(this);
  }
}
