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

package com.github.mvp4g.junit.test.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import com.github.mvp4g.mvp4g2.core.history.annotation.History;
import com.github.mvp4g.mvp4g2.core.internal.history.HistoryMetaData;
import org.junit.Assert;

public class PropertyFactory {


  private static HistoryMetaData getHistoryMetaData(String requestedHistoryConverter,
                                                    String uri) {
    if (requestedHistoryConverter == null) {
      return null;
    }
    final HistoryMetaData[] historyMetaData = {null};
    ClassLoader loader = Thread.currentThread()
                               .getContextClassLoader();
    Properties historyMetaDataProps = new Properties();
    try (InputStream resourceStream = loader.getResourceAsStream(uri)) {
      historyMetaDataProps.load(resourceStream);
    } catch (IOException e) {
      Assert.fail("Resource >>" + uri + "<< not found!");
    }
    String[] historyConverters = historyMetaDataProps.getProperty("historyConverters")
                                                     .split(",");
    Arrays.stream(historyConverters)
          .filter(s -> requestedHistoryConverter.equals(s))
          .forEach(s -> {
            History.HistoryConverterType type;
            String historyConverterType = historyMetaDataProps.getProperty(s + ".historyConverterType");
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
            historyMetaData[0] = new HistoryMetaData(historyMetaDataProps.getProperty(s + ".historyConverter"),
                                                     type) {
              @Override
              public String getHistoryConverterClassName() {
                return super.getHistoryConverterClassName();
              }
            };
          });
    return historyMetaData.length == 0 ? null : historyMetaData[0];
  }
}
