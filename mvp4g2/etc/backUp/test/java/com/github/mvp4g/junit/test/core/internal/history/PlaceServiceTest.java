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

package com.github.mvp4g.junit.test.core.internal.history;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import com.github.mvp4g.junit.test.core.stub.DefaultHistoryProxyStubImpl;
import com.github.mvp4g.junit.test.core.stub.EventBusStub;
import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.history.IsHistoryConverter;
import com.github.mvp4g.mvp4g2.core.history.annotation.History;
import com.github.mvp4g.mvp4g2.core.internal.history.PlaceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * PlaceService Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Apr 29, 2018</pre>
 */
public class PlaceServiceTest {

  private IsEventBus                 eventBus;
  private PlaceService<EventBusStub> placeService;


  @Before
  public void before()
    throws Exception {
    this.placeService = new PlaceService<>(new EventBusStub(),
                                           new DefaultHistoryProxyStubImpl(),
                                           false,
                                           false);
    // add EventMetaData

    // add HistoryMetaData
  }

  @Test
  public void testPlaceCrawlable() {
    String eventName = "eventName";
    // add history converter
    getHistoryMetaData(placeService,
                       "HistoryConverterWithoutCrawable.properties");
    // addEventName
//    EventMetaData<EventBusStub> eventMetaData = new EventMetaData<EventBusStub>() {
//    }
//
//
//
//    placeServiceDefault.addConverter(historyName,
//                                     buildHistoryConverter(true));
//    placeServiceDefault.place(historyName,
//                              null,
//                              false);
//    assertEquals("!" + historyName,
//                 history.getToken());
//    assertFalse(history.isIssueEvent());
  }

  private void getHistoryMetaData(PlaceService<? extends IsEventBus> placeService,
                                  String uri) {
    ClassLoader loader = Thread.currentThread()
                               .getContextClassLoader();
    Properties historyMetaData = new Properties();
    try (InputStream resourceStream = loader.getResourceAsStream(uri)) {
      historyMetaData.load(resourceStream);
    } catch (IOException e) {
      Assert.fail("Resource >>" + uri + "<< not found!");
    }
    String[] historyConverters = historyMetaData.getProperty("historyConverters").split(",");
    Arrays.stream(historyConverters).forEach(s -> {
      History.HistoryConverterType type;
      String historyConverterType = historyMetaData.getProperty(s + ".historyConverterType");
      switch (historyConverterType) {
        case "DEFAULT":
          type = History.HistoryConverterType.DEFAULT;
          break;
        case "SIMPLE":
          type = History.HistoryConverterType.SIMPLE;
          break;
        default:
          type = History.HistoryConverterType.NONE;
          break;
      }
//      HistoryMetaData metaData = new HistoryMetaData(historyMetaData.getProperty(s + ".historyConverter"), type));

    });

//    return new EventMetaData(eventMetaDataProps.getProperty("eventInternalName"),
//                             eventMetaDataProps.getProperty("eventName"),
//                             eventMetaDataProps.getProperty("historyName"),
//                             eventMetaDataProps.getProperty("eventInternalName"),
//                             eventMetaDataProps.getProperty("eventInternalName"),
//                             eventMetaDataProps.getProperty("eventInternalName"),
//                             eventMetaDataProps.getProperty("eventInternalName"),
//                             eventMetaDataProps.getProperty("eventInternalName"),
//                             );
//    return new HistoryMetaData(historyMetaData.get(historyMetaData.get()));
  }

  private IsHistoryConverter<EventBusStub> buildHistoryConverter(final boolean crawlable) {
    return new IsHistoryConverter<EventBusStub>() {

      public void convertFromToken(String eventType,
                                   String form,
                                   EventBusStub eventBus) {
//        eventBus.dispatch(eventType,
//                          form);
      }

      public boolean isCrawlable() {
        return crawlable;
      }

    };
  }

//  private <T> EventMetaData getEventMetaData(String uri) {
//    ClassLoader loader = Thread.currentThread().getContextClassLoader();
//    Properties eventMetaDataProps = new Properties();
//    try(InputStream resourceStream = loader.getResourceAsStream(uri))  {
//      eventMetaDataProps.load(resourceStream);
//    } catch (IOException e) {
//      Assert.that(false, "Resource >>" + uri + "<< not found!");
//    }
//    return new EventMetaData(eventMetaDataProps.getProperty("eventInternalName"),
//                             eventMetaDataProps.getProperty("eventName"),
//                             eventMetaDataProps.getProperty("historyName"),
//                             eventMetaDataProps.getProperty("eventInternalName"),
//                             eventMetaDataProps.getProperty("eventInternalName"),
//                             eventMetaDataProps.getProperty("eventInternalName"),
//                             eventMetaDataProps.getProperty("eventInternalName"),
//                             eventMetaDataProps.getProperty("eventInternalName"),
//                             );
//
//  }
//
//
//  String internalEventName,
//  String eventName,
//  String historyName,
//  HistoryMetaData historyMetaData,
//  IsHistoryConverter<E> historyConverter,
//  boolean passive,
//  boolean navigationEvent
//    this.placeService.set


}
