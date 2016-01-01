package org.gwt4e.mvp4g.test.apt.eventbus.generated.events;

import java.lang.Override;
import java.lang.String;
import org.gwt4e.event.shared.Mvp4gEvent;

public class ThreeEventMvp4gEvent extends Mvp4gEvent<ThreeEventMvp4gEventHandler> {
  public static Mvp4gEvent.Type TYPE = new Mvp4gEvent.Type<ThreeEventMvp4gEventHandler>();

  private String arg0;

  private String arg1;

  public ThreeEventMvp4gEvent(String arg0, String arg1) {
    this.arg0 = arg0;
    this.arg1 = arg1;
  }

  public String getArg0() {
    return arg0;
  }

  public String getArg1() {
    return arg1;
  }

  public void setArg0(String arg0) {
    this.arg0 = arg0;
  }

  public void setArg1(String arg1) {
    this.arg1 = arg1;
  }

  @Override
  public Mvp4gEvent.Type<ThreeEventMvp4gEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(ThreeEventMvp4gEventHandler handler) {
    handler.onThreeEvent(this);
  }
}
