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

public class HandlerMetaModel
  implements IsMetaModel {

  private static final String KEY_EVENT_HANDLERS = "handlers";
  private static final String KEY_EVENT_HANDLER  = ".handlerClassName";
  private static final String KEY_HANDLED_EVENTS = ".handledEvents";

  private Map<String, HandlerData> handlerDatas = new HashMap<>();

  public HandlerMetaModel() {
    this.handlerDatas = new HashMap<>();
  }

  public HandlerMetaModel(Properties properties) {
    Set<String> t = properties.stringPropertyNames();
    t.stream()
     .forEach(System.out::println);

//    .getProperty(HandlerMetaModel.KEY_EVENT_HANDLER),
//       props.getProperty(HandlerMetaModel.KEY_HANDLED_EVENTS)
  }

  public void add(String handler,
                  String eventHandlers) {
    this.handlerDatas.put(handler,
                          new HandlerData(handler,
                                          eventHandlers));
  }

  public void add(String handler,
                  String... eventHandlers) {
    this.handlerDatas.put(handler,
                          new HandlerData(handler,
                                          eventHandlers));
  }

  public Set<String> getHandlerKeys() {
    return this.handlerDatas.keySet();
  }

  public HandlerData getEventHandlerData(String key) {
    return this.handlerDatas.get(key);
  }

  public Properties createPropertes() {
    Properties props = new Properties();
    props.setProperty(HandlerMetaModel.KEY_EVENT_HANDLERS,
                      String.join(",",
                                  this.handlerDatas.keySet()));
    this.handlerDatas.values()
                     .stream()
                     .forEach(data -> {
                       props.setProperty(data.getHandler()
                                             .getClassName() + HandlerMetaModel.KEY_EVENT_HANDLER,
                                         data.getHandler()
                                             .getClassName());
                       props.setProperty(data.getHandler()
                                             .getClassName() + HandlerMetaModel.KEY_HANDLED_EVENTS,
                                         String.join(",",
                                                     data.getHandledEvents()));
                     });
    return props;
  }

//  public boolean isHandler(String handlerClassName) {
//    return this.handlerDatas.get(handlerClassName) != null;
//  }

  public class HandlerData {

    private ClassNameModel handler;
    private List<String> handledEvents = new ArrayList<>();

    public HandlerData(String handler,
                       String eventHandlers) {
      this(handler,
           eventHandlers.split("\\s*,\\s*"));
    }

    public HandlerData(String handler,
                       String... eventHandlers) {
      this.handler = new ClassNameModel(handler);
      Arrays.stream(eventHandlers)
            .map(event -> handledEvents.add(event));
    }

    public ClassNameModel getHandler() {
      return handler;
    }

    public List<String> getHandledEvents() {
      return handledEvents;
    }
  }
}
