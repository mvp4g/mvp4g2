package de.gishmo.gwt.mvp4g2.processor.model;

import de.gishmo.gwt.mvp4g2.processor.model.intern.ClassNameModel;
import de.gishmo.gwt.mvp4g2.processor.model.intern.IsMetaModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class EventHandlerMetaModel
  implements IsMetaModel {

  private static final String KEY_EVENT_HANDLERS = "eventHandlers";
  private static final String KEY_EVENT_HANDLER  = ".eventHandlerClassName";
  private static final String KEY_HANDLED_EVENTS = ".handledEvents";

  private Map<String, EventHandlerData> eventHandlerDatas = new HashMap<>();

  public EventHandlerMetaModel() {
    this.eventHandlerDatas = new HashMap<>();
  }

  public EventHandlerMetaModel(Properties properties) {
    Set<String> t = properties.stringPropertyNames();
    t.stream()
     .forEach(System.out::println);

//    .getProperty(EventHandlerMetaModel.KEY_EVENT_HANDLER),
//       props.getProperty(EventHandlerMetaModel.KEY_HANDLED_EVENTS)
  }

  public void add(String eventHandler,
                  String eventHandlers) {
    this.eventHandlerDatas.put(eventHandler,
                               new EventHandlerData(eventHandler,
                                                    eventHandlers));
  }

  public void add(String eventHandler,
                  String... eventHandlers) {
    this.eventHandlerDatas.put(eventHandler,
                               new EventHandlerData(eventHandler,
                                                    eventHandlers));
  }

  public Set<String> getEventHandlerKeys() {
    return this.eventHandlerDatas.keySet();
  }

  public EventHandlerData getEventHandlerData(String key) {
    return this.eventHandlerDatas.get(key);
  }

  public Properties createPropertes() {
    Properties props = new Properties();
    props.setProperty(EventHandlerMetaModel.KEY_EVENT_HANDLERS,
                      String.join(",", this.eventHandlerDatas.keySet()));
    this.eventHandlerDatas.values()
                          .stream()
                          .forEach(data -> {
                            props.setProperty(data.getEventHandler()
                                                  .getClassName() + EventHandlerMetaModel.KEY_EVENT_HANDLER,
                                              data.getEventHandler()
                                                  .getClassName());
                            props.setProperty(data.getEventHandler()
                                                  .getClassName() + EventHandlerMetaModel.KEY_HANDLED_EVENTS,
                                              String.join(",",
                                                          data.getHandledEvents()));
                          });
    return props;
  }

  public class EventHandlerData {

    private ClassNameModel eventHandler;
    private List<String>   handledEvents = new ArrayList<>();

    public EventHandlerData(String eventHandler,
                            String eventHandlers) {
      this(eventHandler,
           eventHandlers.split("\\s*,\\s*"));
    }

    public EventHandlerData(String eventHandler,
                            String... eventHandlers) {
      this.eventHandler = new ClassNameModel(eventHandler);
      Arrays.stream(eventHandlers)
            .map(event -> handledEvents.add(event));
    }

    public ClassNameModel getEventHandler() {
      return eventHandler;
    }

    public List<String> getHandledEvents() {
      return handledEvents;
    }
  }
}
