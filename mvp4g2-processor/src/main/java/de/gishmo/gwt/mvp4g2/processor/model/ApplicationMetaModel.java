package de.gishmo.gwt.mvp4g2.processor.model;

import de.gishmo.gwt.mvp4g2.processor.model.intern.ClassNameModel;
import de.gishmo.gwt.mvp4g2.processor.model.intern.IsMetaModel;

import java.util.Properties;

public class ApplicationMetaModel
  implements IsMetaModel {

  private static final String KEY_APPLICATION = "application";
  private static final String KEY_EVENTBUS    = "eventBus";
  private static final String KEY_LOADER      = "loader";

  private ClassNameModel application;
  private ClassNameModel eventBus;
  private ClassNameModel loader;

  public ApplicationMetaModel(Properties properties) {
    this.application = new ClassNameModel(properties.getProperty(ApplicationMetaModel.KEY_APPLICATION));
    this.eventBus = new ClassNameModel(properties.getProperty(ApplicationMetaModel.KEY_EVENTBUS));
    this.loader = new ClassNameModel(properties.getProperty(ApplicationMetaModel.KEY_LOADER));
  }

  public ApplicationMetaModel(String application,
                              String eventBus,
                              String loader) {
    this.application = new ClassNameModel(application);
    this.eventBus = new ClassNameModel(eventBus);
    this.loader = new ClassNameModel(loader);
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

  @Override
  public Properties createPropertes() {
    Properties properties = new Properties();
    properties.setProperty(ApplicationMetaModel.KEY_APPLICATION,
                           this.application.getClassName());
    properties.setProperty(ApplicationMetaModel.KEY_EVENTBUS,
                           this.eventBus.getClassName());
    properties.setProperty(ApplicationMetaModel.KEY_LOADER,
                           this.loader.getClassName());
    return properties;
  }
}
