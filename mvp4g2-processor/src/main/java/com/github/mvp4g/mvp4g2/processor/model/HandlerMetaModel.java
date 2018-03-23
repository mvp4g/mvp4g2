package com.github.mvp4g.mvp4g2.processor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.github.mvp4g.mvp4g2.processor.model.intern.ClassNameModel;
import com.github.mvp4g.mvp4g2.processor.model.intern.IsMetaModel;

public class HandlerMetaModel
  implements IsMetaModel {

  private static final String KEY_HANDLERS       = "handlers";
  private static final String KEY_HANDLER        = ".handlerClassName";
  private static final String KEY_HANDLED_EVENTS = ".handledEvents";

  private Map<String, HandlerData> handlerDatas = new HashMap<>();

  public HandlerMetaModel() {
    this.handlerDatas = new HashMap<>();
  }

  public HandlerMetaModel(Properties properties) {
    Arrays.stream(properties.getProperty(HandlerMetaModel.KEY_HANDLERS)
                            .split("\\s*,\\s*"))
          .forEach(s -> {
            String handlerClassName = properties.getProperty(s + HandlerMetaModel.KEY_HANDLER);
            this.handlerDatas.put(handlerClassName,
                                  new HandlerData(handlerClassName,
                                                  properties.getProperty(s + HandlerMetaModel.KEY_HANDLED_EVENTS)
                                                            .split("\\s*,\\s*")));
          });
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

  public HandlerData getHandlerData(String key) {
    return this.handlerDatas.get(key);
  }

  public Collection<HandlerData> getHandlerDatas() {
    return this.handlerDatas.values();
  }

  public Properties createPropertes() {
    Properties props = new Properties();
    props.setProperty(HandlerMetaModel.KEY_HANDLERS,
                      String.join(",",
                                  this.handlerDatas.keySet()));
    this.handlerDatas.values()
                     .stream()
                     .forEach(data -> {
                       props.setProperty(data.getHandler()
                                             .getClassName() + HandlerMetaModel.KEY_HANDLER,
                                         data.getHandler()
                                             .getClassName());
                       props.setProperty(data.getHandler()
                                             .getClassName() + HandlerMetaModel.KEY_HANDLED_EVENTS,
                                         String.join(",",
                                                     data.getHandledEvents()));
                     });
    return props;
  }

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
            .forEach(eventHandler -> handledEvents.add(eventHandler));
    }

    public ClassNameModel getHandler() {
      return handler;
    }

    public List<String> getHandledEvents() {
      return handledEvents;
    }

    public boolean handlesEvents(String eventName) {
      return handledEvents.contains(eventName);
    }
  }
}
