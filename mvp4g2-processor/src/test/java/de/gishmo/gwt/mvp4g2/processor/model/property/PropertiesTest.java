package de.gishmo.gwt.mvp4g2.processor.model.property;

import de.gishmo.gwt.mvp4g2.processor.model.ApplicationMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.EventMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.HandlerMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.HistoryMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterMetaModel;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static junit.framework.TestCase.assertEquals;

public class PropertiesTest {

  @Test
  public void testApplicationMetaModel() {
    Properties properties = new Properties();
    try {
      properties.load(ClassLoader.getSystemResourceAsStream("META-INF" + File.separator + "mvp4g2" + File.separator + "application.properties"));
    } catch (IOException e) {
      assert false : "IOException reading application.properties";
    }

    ApplicationMetaModel metaModel = new ApplicationMetaModel(properties);
    Properties newProperties = metaModel.createPropertes();

    assertEquals(properties,
                 newProperties);
  }

  @Test
  public void testEventBusMetaModel() {
    Properties properties = new Properties();
    try {
      properties.load(ClassLoader.getSystemResourceAsStream("META-INF" + File.separator + "mvp4g2" + File.separator + "eventBus.properties"));
    } catch (IOException e) {
      assert false : "IOException reading eventBus.properties";
    }

    EventBusMetaModel metaModel = new EventBusMetaModel(properties);
    for (String eventInternalName : properties.getProperty("events").split(",")) {
      try {
        Properties eventProperties = new Properties();
        eventProperties.load(ClassLoader.getSystemResourceAsStream("META-INF" + File.separator + "mvp4g2" + File.separator + eventInternalName + ".properties"));
        metaModel.add(new EventMetaModel(eventProperties));
      } catch (IOException e) {
        assert false : "IOException reading " + eventInternalName + ".properties";
      }
    }

    Properties newProperties = metaModel.createPropertes();

    assertEquals(properties,
                 newProperties);
  }

  @Test
  public void testEventMetaModel() {
    Properties properties = new Properties();
    try {
      properties.load(ClassLoader.getSystemResourceAsStream("META-INF" + File.separator + "mvp4g2" + File.separator + "gotoList_pPp_java_lang_String_pPp_java_lang_String.properties"));
    } catch (IOException e) {
      assert false : "IOException reading gotoList_pPp_java_lang_String_pPp_java_lang_String.properties";
    }

    EventMetaModel metaModel = new EventMetaModel(properties);
    Properties newProperties = metaModel.createPropertes();

    assertEquals(properties,
                 newProperties);
  }

  @Test
  public void testHandlerMetaModel() {
    Properties properties = new Properties();
    try {
      properties.load(ClassLoader.getSystemResourceAsStream("META-INF" + File.separator + "mvp4g2" + File.separator + "handler.properties"));
    } catch (IOException e) {
      assert false : "IOException reading handler.properties";
    }

    HandlerMetaModel metaModel = new HandlerMetaModel(properties);
    Properties newProperties = metaModel.createPropertes();

    assertEquals(properties,
                 newProperties);
  }

  @Test
  public void testHistoryMetaModel() {
    Properties properties = new Properties();
    try {
      properties.load(ClassLoader.getSystemResourceAsStream("META-INF" + File.separator + "mvp4g2" + File.separator + "history.properties"));
    } catch (IOException e) {
      assert false : "IOException reading history.properties";
    }

    HistoryMetaModel metaModel = new HistoryMetaModel(properties);
    Properties newProperties = metaModel.createPropertes();

    assertEquals(properties,
                 newProperties);
  }

  @Test
  public void testPresenteryMetaModel() {
    Properties properties = new Properties();
    try {
      properties.load(ClassLoader.getSystemResourceAsStream("META-INF" + File.separator + "mvp4g2" + File.separator + "presenter.properties"));
    } catch (IOException e) {
      assert false : "IOException reading presenter.properties";
    }

    PresenterMetaModel metaModel = new PresenterMetaModel(properties);
    Properties newProperties = metaModel.createPropertes();

    assertEquals(properties,
                 newProperties);
  }
}