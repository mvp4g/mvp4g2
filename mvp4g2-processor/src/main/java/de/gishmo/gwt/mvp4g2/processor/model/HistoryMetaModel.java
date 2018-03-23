package de.gishmo.gwt.mvp4g2.processor.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import de.gishmo.gwt.mvp4g2.processor.model.intern.ClassNameModel;
import de.gishmo.gwt.mvp4g2.processor.model.intern.IsMetaModel;

import static java.util.Objects.isNull;

public class HistoryMetaModel
  implements IsMetaModel {

  private static final String KEY_HISTORY_CONVERTERS     = "historyConverters";
  private static final String KEY_HISTORY_CONVERTER      = ".historyConverter";
  private static final String KEY_HISTORY_CONVERTER_TYPE = ".historyConverterType";

  private Map<String, HistoryData> historyDatas = new HashMap<>();

  public HistoryMetaModel() {
    this.historyDatas = new HashMap<>();
  }

  public HistoryMetaModel(Properties properties) {
    if (isNull(properties.getProperty(HistoryMetaModel.KEY_HISTORY_CONVERTERS))) {
      this.historyDatas = new HashMap<>();
    } else {
      if (properties.getProperty(HistoryMetaModel.KEY_HISTORY_CONVERTERS)
                    .trim()
                    .length() > 0) {
        Arrays.stream(properties.getProperty(HistoryMetaModel.KEY_HISTORY_CONVERTERS)
                                .split("\\s*,\\s*"))
              .forEach(s -> {
                historyDatas.put(properties.getProperty(s + HistoryMetaModel.KEY_HISTORY_CONVERTER),
                                 new HistoryData(properties.getProperty(s + HistoryMetaModel.KEY_HISTORY_CONVERTER),
                                                 properties.getProperty(s + HistoryMetaModel.KEY_HISTORY_CONVERTER_TYPE)));
              });
      }
    }
  }

  public void add(String historyConverter,
                  String historyConverterType) {
    this.historyDatas.put(historyConverter,
                          new HistoryData(historyConverter,
                                          historyConverterType));
  }

  public Set<String> getHistoryConverterClassNames() {
    return this.historyDatas.keySet();
  }

  public HistoryData getHistoryData(String key) {
    return this.historyDatas.get(key);
  }

  public Properties createPropertes() {
    Properties props = new Properties();
    props.setProperty(HistoryMetaModel.KEY_HISTORY_CONVERTERS,
                      String.join(",",
                                  this.historyDatas.keySet()));
    this.historyDatas.values()
                     .stream()
                     .forEach(data -> {
                       props.setProperty(data.historyConverter.getClassName() + HistoryMetaModel.KEY_HISTORY_CONVERTER,
                                         data.historyConverter
                                           .getClassName());
                       props.setProperty(data.historyConverter.getClassName() + HistoryMetaModel.KEY_HISTORY_CONVERTER_TYPE,
                                         data.historyConverterType);
//                       props.setProperty(data.historyConverter.getClassName() + HistoryMetaModel.KEY_HISTORY_CONVERTER_TYPE,
//                                         ModelUtils.stringify(data.getHandledEvents()));
                     });
    return props;
  }

  public class HistoryData {

    private ClassNameModel historyConverter;
    private String         historyConverterType;

    public HistoryData(String historyConverter,
                       String historyConverterType) {
      this.historyConverter = new ClassNameModel(historyConverter);
      this.historyConverterType = historyConverterType;
    }

    public ClassNameModel getHistoryConverter() {
      return historyConverter;
    }

    public String getHistoryConverterType() {
      return historyConverterType;
    }
  }
}
