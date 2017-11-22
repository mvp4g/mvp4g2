package gwt.core.mvp4g.client.events;

import gwt.core.mvp4g.client.event.Mvp4gEvent;
import gwt.core.mvp4g.client.event.Mvp4gEventHandler;
import gwt.core.mvp4g.client.places.Mvp4gPlace;

public class GotoEvent
  extends Mvp4gEvent<GotoEvent.GotoEventHandler> {

  public static Type<GotoEvent.GotoEventHandler> TYPE = new Type<>();

  private Mvp4gPlace place;
  private String     slot;

  public Mvp4gPlace getPlace() {
    return place;
  }

  public String getSlot() {
    return slot;
  }

  @Override
  public Type getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(GotoEvent.GotoEventHandler handler) {
    handler.onGoto(place,
                   slot);
  }

  public interface GotoEventHandler
    extends Mvp4gEventHandler {

    void onGoto(Mvp4gPlace place,
                String slot);

  }
}
