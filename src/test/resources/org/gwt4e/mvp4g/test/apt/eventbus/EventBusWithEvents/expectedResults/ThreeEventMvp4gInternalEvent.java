package org.gwt4e.mvp4g.test.apt.eventbus.generated.events;

import java.lang.Override;
import java.lang.String;
import org.gwt4e.event.shared.Mvp4gInternalEvent;

public class ThreeEventMvp4gInternalEvent extends Mvp4gInternalEvent<ThreeEventMvp4gInternalEventHandler> {
  public static Mvp4gInternalEvent.Type TYPE = new Mvp4gInternalEvent.Type<ThreeEventMvp4gInternalEventHandler>();

  private String arg0;

  private String arg1;

  public ThreeEventMvp4gInternalEvent(String arg0, String arg1) {
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
  public Mvp4gInternalEvent.Type<ThreeEventMvp4gInternalEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(ThreeEventMvp4gInternalEventHandler handler) {
    handler.onThreeEvent(this);
  }
}
