package de.gishmo.gwt.mvp4g2.processor.model;

import de.gishmo.gwt.mvp4g2.processor.model.intern.IsMetaModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class HistoryMetaModel
  implements IsMetaModel {

  private static final String KEY_HISTORY_CONVERTERS = "historyConverters";
  private static final String KEY_HISTORY_CONVERTER  = ".historyConverter";
  private static final String KEY_HISTORY_CONVERTER_TYPE = ".historyConverterType";

  private Map<String, HistoryData> historyDatas;

  public HistoryMetaModel() {
    this.historyDatas = new HashMap<>();
  }

  public HistoryMetaModel(Properties properties) {
    Set<String> t = properties.stringPropertyNames();
    t.stream()
     .forEach(System.out::println);

//    .getProperty(EventHandlerMetaModel.KEY_EVENT_HANDLER),
//       props.getProperty(EventHandlerMetaModel.KEY_HANDLED_EVENTS)
  }

  public void add(String historyConverter,
                  String historyConverterType) {
    this.historyDatas.put(historyConverter,
                          new HistoryData(historyConverter,
                                               historyConverterType));
  }

  public Set<String> getHistoryKeys() {
    return this.historyDatas.keySet();
  }

  public HistoryData getHistoryData(String key) {
    return this.historyDatas.get(key);
  }

  public Properties createPropertes() {
    Properties props = new Properties();
//    props.setProperty(HistoryMetaModel.KEY_HISTORY_CONVERTERS,
//                      ModelUtils.stringify(this.historyDatas.keySet()));
//    this.historyDatas.values()
//                     .stream()
//                     .forEach(data -> {
//                            props.setProperty(data.getHistoryConverter()
//                                                              .getClassName() + HistoryMetaModel.KEY_EVENT_HANDLER,
//                                              data.getEventHandler()
//                                                              .getClassName());
//                            props.setProperty(data.getEventHandler()
//                                                              .getClassName() + HistoryMetaModel.KEY_HANDLED_EVENTS,
//                                              ModelUtils.stringify(data.getHandledEvents()));
//                          });
    return props;
  }

  public class HistoryData {

    private String historyConverter;
    private String historyConverterType;

    public HistoryData(String historyConverter,
                       String historyConverterType) {
      this.historyConverter = historyConverter;
      this.historyConverterType = historyConverterType;
    }

    public String getHistoryConverter() {
      return historyConverter;
    }

    public String getHistoryConverterType() {
      return historyConverterType;
    }
  }
}
