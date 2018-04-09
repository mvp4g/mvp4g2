/*
 * Copyright (c) 2018 - Frank Hossfeld
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 *
 */

package com.github.mvp4g.mvp4g2.processor.model;

import java.util.Properties;

import com.github.mvp4g.mvp4g2.processor.model.intern.ClassNameModel;
import com.github.mvp4g.mvp4g2.processor.model.intern.IsMetaModel;

public class ApplicationMetaModel
  implements IsMetaModel {

  private static final String KEY_APPLICATION      = "application";
  private static final String KEY_EVENTBUS         = "eventBus";
  private static final String KEY_LOADER           = "loader";
  private static final String KEY_HISTORY_ON_START = "historyOnStart";

  private ClassNameModel application;
  private ClassNameModel eventBus;
  private ClassNameModel loader;
  private String         historyOnStart;

  public ApplicationMetaModel(Properties properties) {
    this.application = new ClassNameModel(properties.getProperty(ApplicationMetaModel.KEY_APPLICATION));
    this.historyOnStart = properties.getProperty(ApplicationMetaModel.KEY_HISTORY_ON_START);
    this.eventBus = new ClassNameModel(properties.getProperty(ApplicationMetaModel.KEY_EVENTBUS));
    this.loader = new ClassNameModel(properties.getProperty(ApplicationMetaModel.KEY_LOADER));
  }

  public ApplicationMetaModel(String application,
                              String eventBus,
                              String loader,
                              String historyOnStart) {
    this.application = new ClassNameModel(application);
    this.eventBus = new ClassNameModel(eventBus);
    this.loader = new ClassNameModel(loader);
    this.historyOnStart = historyOnStart;
  }

  public ClassNameModel getApplication() {
    return application;
  }

  public ClassNameModel getEventBus() {
    return eventBus;
  }

  public ClassNameModel getLoader() {
    return loader;
  }

  public String getHistoryOnStart() {
    return historyOnStart;
  }

  public boolean isHistoryOnStart() {
    return "true".equals(historyOnStart);
  }

  @Override
  public Properties createPropertes() {
    Properties properties = new Properties();
    properties.setProperty(ApplicationMetaModel.KEY_APPLICATION,
                           this.application.getClassName());
    properties.setProperty(ApplicationMetaModel.KEY_EVENTBUS,
                           this.eventBus.getClassName());
    properties.setProperty(ApplicationMetaModel.KEY_LOADER,
                           this.loader.getClassName());
    properties.setProperty(ApplicationMetaModel.KEY_HISTORY_ON_START,
                           this.historyOnStart);
    return properties;
  }
}
