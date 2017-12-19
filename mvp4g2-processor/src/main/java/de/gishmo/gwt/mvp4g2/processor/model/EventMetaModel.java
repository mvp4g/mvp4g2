package de.gishmo.gwt.mvp4g2.processor.model;

import de.gishmo.gwt.mvp4g2.processor.model.intern.ClassNameModel;
import de.gishmo.gwt.mvp4g2.processor.model.intern.IsMetaModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class EventMetaModel
  implements IsMetaModel {

  private static final String KEY_EVENT_EVENT_INTERNAL_NAME = "eventInternalName";
  private static final String KEY_EVENT_NAME                = "eventName";
  private static final String KEY_HISTORY_EVENT_NAME        = "historyEventName";
  private static final String KEY_HISTORY_CONVERTER         = "historyConverter";
  private static final String KEY_BINDINGS                  = "bindings";
  private static final String KEY_HANDLERS                  = "handlers";
  private static final String KEY_NAVIGATION_EVENT          = "navigationEvent";
  private static final String KEY_PASSIVE                   = "passive";
  private static final String KEY_ACTIVATE_HANDELRS         = "activateHandlers";
  private static final String KEY_DEACTIVATE_HANDELRS       = "deactivateHandlers";

  private String               eventInternalName;
  private String               eventName;
  private String               historyEventName;
  private ClassNameModel       historyConverter;
  private List<ClassNameModel> bindings;
  private List<ClassNameModel> handlers;
  private String               navigationEvent;
  private String               passive;
  private List<ClassNameModel> activateHandlers;
  private List<ClassNameModel> deactivateHandlers;


  public EventMetaModel(Properties properties) {
    this.eventInternalName = properties.getProperty(EventMetaModel.KEY_EVENT_EVENT_INTERNAL_NAME);
    this.eventName = properties.getProperty(this.eventInternalName + EventMetaModel.KEY_EVENT_NAME);
    this.historyEventName = properties.getProperty(this.eventInternalName + EventMetaModel.KEY_HISTORY_EVENT_NAME);
    this.historyConverter = new ClassNameModel(properties.getProperty(this.eventInternalName + EventMetaModel.KEY_HISTORY_CONVERTER));
    if (isNull(properties.getProperty(this.eventInternalName + EventMetaModel.KEY_BINDINGS))) {
      this.bindings = new ArrayList<>();
    } else {
      this.bindings = Arrays.stream(properties.getProperty(this.eventInternalName + EventMetaModel.KEY_BINDINGS)
                                              .split("\\s*,\\s*"))
                            .map(s -> new ClassNameModel(s))
                            .collect(Collectors.toCollection(ArrayList::new));
    }
    if (isNull(properties.getProperty(this.eventInternalName + EventMetaModel.KEY_HANDLERS))) {
      this.bindings = new ArrayList<>();
    } else {
      this.handlers = Arrays.stream(properties.getProperty(this.eventInternalName + EventMetaModel.KEY_HANDLERS)
                                              .split("\\s*,\\s*"))
                            .map(s -> new ClassNameModel(s))
                            .collect(Collectors.toCollection(ArrayList::new));
    }
    this.navigationEvent = properties.getProperty(this.eventInternalName + EventMetaModel.KEY_NAVIGATION_EVENT);
    this.passive = properties.getProperty(this.eventInternalName + EventMetaModel.KEY_PASSIVE);
    if (isNull(properties.getProperty(this.eventInternalName + EventMetaModel.KEY_ACTIVATE_HANDELRS))) {
      this.bindings = new ArrayList<>();
    } else {
      this.activateHandlers = Arrays.stream(properties.getProperty(this.eventInternalName + EventMetaModel.KEY_ACTIVATE_HANDELRS)
                                                      .split("\\s*,\\s*"))
                                    .map(s -> new ClassNameModel(s))
                                    .collect(Collectors.toCollection(ArrayList::new));
    }
    if (isNull(properties.getProperty(this.eventInternalName + EventMetaModel.KEY_DEACTIVATE_HANDELRS))) {
      this.bindings = new ArrayList<>();
    } else {
      this.deactivateHandlers = Arrays.stream(properties.getProperty(this.eventInternalName + EventMetaModel.KEY_DEACTIVATE_HANDELRS)
                                                        .split("\\s*,\\s*"))
                                      .map(s -> new ClassNameModel(s))
                                      .collect(Collectors.toCollection(ArrayList::new));
    }
  }

  public EventMetaModel(String eventInternalName,
                        String eventName,
                        String historyEventName,
                        ClassNameModel historyConverter,
                        List<String> bindings,
                        List<String> handlers,
                        String navigationEvent,
                        String passive,
                        List<String> activateHandlers,
                        List<String> deactivateHandlers) {
    this.eventInternalName = eventInternalName;
    this.eventName = eventName;
    this.historyEventName = historyEventName;
    this.historyConverter = historyConverter;
    this.bindings = bindings.stream()
                            .map(s -> new ClassNameModel(s))
                            .collect(Collectors.toCollection(ArrayList::new));
    this.handlers = handlers.stream()
                            .map(s -> new ClassNameModel(s))
                            .collect(Collectors.toCollection(ArrayList::new));
    this.navigationEvent = navigationEvent;
    this.passive = passive;
    this.activateHandlers = activateHandlers.stream()
                                            .map(s -> new ClassNameModel(s))
                                            .collect(Collectors.toCollection(ArrayList::new));
    this.deactivateHandlers = deactivateHandlers.stream()
                                                .map(s -> new ClassNameModel(s))
                                                .collect(Collectors.toCollection(ArrayList::new));
  }

  public String getEventInternalName() {
    return eventInternalName;
  }

  public String getEventName() {
    return eventName;
  }

  public String getHistoryEventName() {
    return historyEventName;
  }

  public ClassNameModel getHistoryConverter() {
    return historyConverter;
  }

  public List<ClassNameModel> getBindings() {
    return bindings;
  }

  public List<ClassNameModel> getHandlers() {
    return handlers;
  }

  public List<ClassNameModel> getActivateHandlers() {
    return activateHandlers;
  }

  public List<ClassNameModel> getDeactivateHandlers() {
    return deactivateHandlers;
  }

  public boolean isNavigationEvent() {
    return "true".equals(this.navigationEvent);
  }

  public boolean isPassive() {
    return "true".equals(this.passive);
  }

  @Override
  public Properties createPropertes() {
    Properties properties = new Properties();
    properties.setProperty(EventMetaModel.KEY_EVENT_EVENT_INTERNAL_NAME,
                           this.eventInternalName);
    properties.setProperty(EventMetaModel.KEY_EVENT_NAME,
                           this.eventName);
    properties.setProperty(EventMetaModel.KEY_HISTORY_EVENT_NAME,
                           this.historyEventName);
    properties.setProperty(EventMetaModel.KEY_HISTORY_CONVERTER,
                           this.historyConverter.getClassName());
    properties.setProperty(EventMetaModel.KEY_BINDINGS,
                           String.join(",",
                                       this.bindings.stream()
                                                    .map(c -> c.getClassName())
                                                    .collect(Collectors.toCollection(ArrayList::new))));
    properties.setProperty(EventMetaModel.KEY_HANDLERS,
                           String.join(",",
                                       this.handlers.stream()
                                                    .map(c -> c.getClassName())
                                                    .collect(Collectors.toCollection(ArrayList::new))));
    properties.setProperty(EventMetaModel.KEY_NAVIGATION_EVENT,
                           this.navigationEvent);
    properties.setProperty(EventMetaModel.KEY_PASSIVE,
                           this.passive);
    properties.setProperty(EventMetaModel.KEY_ACTIVATE_HANDELRS,
                           String.join(",",
                                       this.activateHandlers.stream()
                                                            .map(c -> c.getClassName())
                                                            .collect(Collectors.toCollection(ArrayList::new))));
    properties.setProperty(EventMetaModel.KEY_DEACTIVATE_HANDELRS,
                           String.join(",",
                                       this.deactivateHandlers.stream()
                                                              .map(c -> c.getClassName())
                                                              .collect(Collectors.toCollection(ArrayList::new))));
    return properties;
  }
}
