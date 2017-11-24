package de.gishmo.gwt.mvp4g2.client.history;

import com.google.gwt.core.client.GWT;
import de.gishmo.gwt.mvp4g2.client.Mvp4g2;
import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.internal.EventMetaData;
import elemental2.dom.DomGlobal;
import jsinterop.base.Js;

import java.util.HashMap;
import java.util.Map;

public class PlaceService<E extends IsEventBus> {

  private IsEventBus                                       eventBus;
  private Map<String, EventMetaData<? extends IsEventBus>> eventMetaDataMap;

  public PlaceService(E eventBus) {
    super();

    this.eventMetaDataMap = new HashMap<>();

    this.eventBus = eventBus;

    DomGlobal.window.onpopstate = (e) -> {
GWT.debugger();
      Mvp4g2.log("hallo ");
      return null;
    };

    DomGlobal.window.addEventListener("popstate",
                                      (e) -> {GWT.debugger();
                                        Mvp4g2.log("hallo ");});
  }

  public void startApplication() {
    // the last thing we do, is to add the shell to the viewport
    eventBus.setShell();
    GWT.debugger();
    // manage history and start application
    if (eventBus.hasHistoryOnStart()) {
      // TODO history management on Start

GWT.debugger();

GWT.log(String.valueOf(DomGlobal.window.history.state));
      Js.debugger();
      Object o = DomGlobal.window.history.state;


    } else {
      // no history ...
      eventBus.fireStartEvent();
    }
  }

  /**
   * Add a converter for an event.
   *
   * @param eventMetaData EventMetaDAta object containing all relevant informations
   */
  public void addConverter(EventMetaData<? extends IsEventBus> eventMetaData) {
    this.eventMetaDataMap.put(eventMetaData.getEventName(),
                              eventMetaData);
  }

  /**
   * Convert an event and its associated parameters to a token.<br>
   *
   * @param eventName name of the event to store
   * @param param     string representation of the objects associated with the event that needs to be
   *                  stored in the token
   * @param onlyToken if true, only the token will be generated and browser history won't change
   * @return the generated token
   */
  public String place(String eventName,
                      String param,
                      boolean onlyToken) {
    GWT.debugger();
    EventMetaData<? extends IsEventBus> metaData = this.eventMetaDataMap.get(eventName);

//    if (!enabled && !onlyToken) {
//      return null;
//    }

    String token = tokenize(metaData.getHistoryName(),
                            param);

//    if (converters.get(eventName)
//                  .isCrawlable()) {
//      token = CRAWLABLE + token;
//    }
    if (!onlyToken) {
      DomGlobal.window.history.pushState(param,
                                         "",
                                         "#" + param);
//      history.newItem(token,
//                      false);
    }
    return token;
  }

  /**
   * Ask for user's confirmation before firing an event
   *
   * @param event
   *   event to confirm
   */
  public void confirmEvent(NavigationEventCommand event) {
    GWT.debugger();
    if (this.eventBus.getNavigationConfirmationPresenter() == null) {
      //no need to remove the confirmation, there is none
      event.fireEvent(false);
    } else {
      eventBus.getNavigationConfirmationPresenter().confirm(event);
    }
  }

  /**
   * Transform an event and its parameters to a token
   *
   * @param eventName event's name
   * @param param     event's parameters
   * @return token to store in the history
   */
  private String tokenize(String eventName,
                          String param) {
    String token = eventName;
    if ((param != null) && (param.length() > 0)) {
      token = token + getParamSeparator() + param;
    }
    return token;
  }

  /**
   * @return separator used to differenciate the event's name and its parameters
   */
  protected String getParamSeparator() {
    return "?";
  }

  public IsHistoryConverter<? extends IsEventBus> getHistoryConverter(String eventName) {
    return this.eventMetaDataMap.get(eventName)
                                .getHistoryConverter();
  }
}
