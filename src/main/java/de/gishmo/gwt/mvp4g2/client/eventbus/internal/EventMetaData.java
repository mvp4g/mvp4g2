package de.gishmo.gwt.mvp4g2.client.eventbus.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * type of the eventBus
 */
public abstract class EventMetaData {

  /* name of the event */
  private String              eventName;
  /* list of the parameter types */
  private Map<String, String> paraemterTypes;
  /* canonical names of the eventhandler to be executed on this event */
  private List<String>        handlerTypes;
  /* canonical names of the eventhandler to be executed when bind is set on this event */
  /* will only be executed in case the event is the first time called */
  private List<String>        bindHandlerTypes;
  /* flag if event is passive */
  private boolean             passive;
  /* flag if event is navigation event */
  private boolean             navigationEvent;

  public EventMetaData(String eventName,
                       boolean passive,
                       boolean navigationEvent) {
    this.passive = passive;
    this.navigationEvent = navigationEvent;

    this.eventName = eventName;
    this.paraemterTypes = new HashMap<>();
    this.handlerTypes = new ArrayList<>();
    this.bindHandlerTypes = new ArrayList<>();
  }

  public void addBindHandler(String handler) {
    this.bindHandlerTypes.add(handler);
  }

  public void addHandler(String handler) {
    this.handlerTypes.add(handler);
  }

  public void addParameterType(String parameterName,
                               String parameterType) {
    this.paraemterTypes.put(parameterName,
                            parameterType);
  }

  public String getEventName() {
    return eventName;
  }

  public List<String> getHandlerTypes() {
    return handlerTypes;
  }

  public Map<String, String> getParaemterTypes() {
    return paraemterTypes;
  }

  public List<String> getBindHandlerTypes() {
    return bindHandlerTypes;
  }

  public boolean isPassive() {
    return passive;
  }

  public boolean isNavigationEvent() {
    return navigationEvent;
  }
}
