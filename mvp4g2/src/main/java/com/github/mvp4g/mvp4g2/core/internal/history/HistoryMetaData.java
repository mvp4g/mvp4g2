package com.github.mvp4g.mvp4g2.core.internal.history;

import com.github.mvp4g.mvp4g2.core.history.annotation.History;

/**
 * meta data history annotation
 */
public abstract class HistoryMetaData {

  /* class name of the history converter */
  private String                       historyConverterClassName;
  /* generator of the history converter */
  private History.HistoryConverterType type;

  public HistoryMetaData(String historyConverterClassName,
                         History.HistoryConverterType type) {
    this.historyConverterClassName = historyConverterClassName;
    this.type = type;
  }

  public String getHistoryConverterClassName() {
    return historyConverterClassName;
  }

  public History.HistoryConverterType getType() {
    return type;
  }

}
