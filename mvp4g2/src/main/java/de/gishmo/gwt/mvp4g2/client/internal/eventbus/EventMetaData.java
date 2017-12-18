package de.gishmo.gwt.mvp4g2.client.internal.eventbus;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.client.history.IsHistoryConverter;
import de.gishmo.gwt.mvp4g2.client.internal.history.HistoryMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * generator of the eventbus
 */
public abstract class EventMetaData<E extends IsEventBus> {

  /* name of the event */
  private String                eventName;
  /* internal event name */
  private String                internalEventName;
  /* event historyName -> history */
  private String                historyName;
  /* history converter meta Data*/
  private HistoryMetaData       historyMetaData;
  /* history converter */
  private IsHistoryConverter<E> historyConverter;
  /* list of the parameter types */
  private Map<String, String>   paraemterTypes;
  /* canonical names of the eventhandler to be executed on this event */
  private List<String>          handlerTypes;
  /* canonical names of the eventhandler to be executed when bind is set on this event */
  /* will only be executed in case the event is the first time called */
  private List<String>          bindHandlerTypes;
  /* canonical names of the eventhandler to be activeted when this event is fired */
  private List<String>          activateHandlerTypes;
  /* canonical names of the eventhandler to be deactiveted when this event is fired */
  private List<String>          deactivateHandlerTypes;
  /* flag if event is passive */
  private boolean               passive;
  /* flag if event is navigation event */
  private boolean               navigationEvent;

  public EventMetaData(String internalEventName,
                       String eventName,
                       String historyName,
                       HistoryMetaData historyMetaData,
                       IsHistoryConverter<E> historyConverter,
                       boolean passive,
                       boolean navigationEvent) {
    this.internalEventName = internalEventName;
    this.eventName = eventName;
    this.historyName = historyName;
    this.historyMetaData = historyMetaData;
    this.historyConverter = historyConverter;
    this.passive = passive;
    this.navigationEvent = navigationEvent;

    this.paraemterTypes = new HashMap<>();
    this.handlerTypes = new ArrayList<>();
    this.bindHandlerTypes = new ArrayList<>();
    this.activateHandlerTypes = new ArrayList<>();
    this.deactivateHandlerTypes = new ArrayList<>();
  }

  public void addActivateHandler(String handler) {
    this.activateHandlerTypes.add(handler);
  }

  public void addBindHandler(String handler) {
    this.bindHandlerTypes.add(handler);
  }

  public void addDeactivateHandler(String handler) {
    this.deactivateHandlerTypes.add(handler);
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

  public List<String> getActivateHandlerTypes() {
    return activateHandlerTypes;
  }

  public List<String> getDeactivateHandlerTypes() {
    return deactivateHandlerTypes;
  }

  public boolean isPassive() {
    return passive;
  }

  public boolean isNavigationEvent() {
    return navigationEvent;
  }

  public String getHistoryName() {
    return !isNull(this.historyName) && !Event.DEFAULT_HISTORY_NAME.equals(this.historyName) && !"".equals(this.historyName.trim()) ? this.historyName : this.eventName;
  }

  public HistoryMetaData getHistoryMetaData() {
    return historyMetaData;
  }

  public IsHistoryConverter<E> getHistoryConverter() {
    return historyConverter;
  }

  public String getInternalEventName() {
    return internalEventName;
  }

  public void setInternalEventName(String internalEventName) {
    this.internalEventName = internalEventName;
  }
}
