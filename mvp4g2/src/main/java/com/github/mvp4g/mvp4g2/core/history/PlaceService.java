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

package com.github.mvp4g.mvp4g2.core.history;

import java.util.HashMap;
import java.util.Map;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.internal.eventbus.EventMetaData;
import elemental2.dom.DomGlobal;

import static java.util.Objects.isNull;

public class PlaceService<E extends IsEventBus> {

  //  private static final String MODULE_SEPARATOR = "/";
  private static final String URL_SEPARATOR = "#";
  private static final String CRAWLABLE     = "!";
  /* flag if we have to check history token at the start of the application */
  protected boolean                                          historyOnStart;
  /* flag if we have to encode the token */
  protected boolean                                          encodeToken;
  private   IsEventBus                                       eventBus;
  private   Map<String, EventMetaData<? extends IsEventBus>> eventMetaDataMap;
  private   Map<String, String>                              historyNameMap;
  private boolean enabled = true;

  public PlaceService(E eventBus,
                      boolean historyOnStart,
                      boolean encodeToken) {
    super();

    this.eventMetaDataMap = new HashMap<>();
    this.historyNameMap = new HashMap<>();

    this.eventBus = eventBus;
    this.historyOnStart = historyOnStart;
    this.encodeToken = encodeToken;

    DomGlobal.window.addEventListener("popstate",
                                      (e) -> confirmEvent(new NavigationEventCommand(eventBus) {
                                        protected void execute() {
                                          enabled = false;
                                          convertToken(getTokenFromUrl(DomGlobal.window.location.toString()));
                                          enabled = true;
                                        }
                                      }));
  }

  /**
   * Ask for user's confirmation before firing an event
   *
   * @param event event to confirm
   */
  public void confirmEvent(NavigationEventCommand event) {
    if (isNull(this.eventBus.getNavigationConfirmationPresenter())) {
      //no need to remove the confirmation, there is none
      event.fireEvent(false);
    } else {
      eventBus.getNavigationConfirmationPresenter()
              .confirm(event);
    }
  }

  /**
   * Convert the token to an event
   *
   * @param token the token to convert
   */
  protected void convertToken(String token) {
    boolean toContinue = false;
    if (!isNull(token)) {
      if (token.startsWith(CRAWLABLE)) {
        token = token.substring(1);
      }
      toContinue = (token.length() > 0);
    }
    if (toContinue) {
      String[] result = parseToken(token);
      //      if (!forwardToChildModuleIfNeeded(result[0],
      //                                        result[1])) {
      dispatchEvent(result[0],
                    result[1]);
      //      }
    } else {
      eventBus.fireInitHistoryEvent();
    }
  }

  private String getTokenFromUrl(String url) {
    if (!url.contains(PlaceService.URL_SEPARATOR)) {
      return "";
    }
    return url.substring(url.indexOf(PlaceService.URL_SEPARATOR) + 1);
  }

  /**
   * Parse the token and return a string array. The first element of this array contains the event
   * name whereas the second element contains the parameters associated to the event.
   *
   * @param token token to parse
   * @return array of string
   */
  protected String[] parseToken(String token) {
    String[] result = new String[2];
    int index = token.lastIndexOf(getParamSeparator());
    result[0] = (index == -1) ? token : token.substring(0,
                                                        index);
    result[1] = (index == -1) ? null : token.substring(index + 1);
    return result;
  }

  /**
   * Dispatch the event thanks to the history converter.
   *
   * @param historyName name of the event stored in the token
   * @param param       parameters stored in the token
   */
  @SuppressWarnings("unchecked")
  protected void dispatchEvent(String historyName,
                               String param) {
    if (!isNull(historyName)) {
      String key = this.historyNameMap.get(historyName);
      if (!isNull(key)) {
        EventMetaData<? extends IsEventBus> metaData = this.eventMetaDataMap.get(key);
        if (!isNull(metaData)) {
          @SuppressWarnings("rawtypes") IsHistoryConverter converter = metaData.getHistoryConverter();
          if (isNull(converter)) {
            eventBus.fireNotFoundHistoryEvent();
          } else {
            converter.convertFromToken(metaData.getEventName(),
                                       param,
                                       this.eventBus);
          }
        } else {
          eventBus.fireNotFoundHistoryEvent();
        }
      } else {
        eventBus.fireNotFoundHistoryEvent();
      }
    } else {
      eventBus.fireNotFoundHistoryEvent();
    }
  }

  /**
   * @return separator used to differenciate the event's name and its parameters
   */
  protected String getParamSeparator() {
    return "?";
  }

  public void startApplication() {
    // the last thing we do, is to add the shell to the viewport
    eventBus.setShell();
    // fire Start event
    eventBus.fireStartEvent();
    // do we have history?
    if (this.hasHistory()) {
      if (this.historyOnStart) {
        convertToken(getTokenFromUrl(DomGlobal.window.location.toString()));
      } else {
        eventBus.fireInitHistoryEvent();
      }
    }
  }

  private boolean hasHistory() {
    return this.eventMetaDataMap.values()
                                .stream()
                                .anyMatch(metaData -> !isNull(metaData.getHistoryConverter()));
  }

  /**
   * Add a converter for an event.
   *
   * @param eventMetaData EventMetaDAta object containing all relevant informations
   */
  public void addConverter(EventMetaData<? extends IsEventBus> eventMetaData) {
    String historyName = !isNull(eventMetaData.getHistoryName()) && eventMetaData.getHistoryName()
                                                                                 .trim()
                                                                                 .length() > 0
                         ? eventMetaData.getHistoryName() : eventMetaData.getEventName();
    this.historyNameMap.put(historyName,
                            eventMetaData.getEventName());
    this.eventMetaDataMap.put(eventMetaData.getEventName(),
                              eventMetaData);
  }

  /**
   * Convert an event and its associated parameters to a token.<br>
   *
   * @param eventName name of the event to store
   * @param param     string representation of the objects associated with the event that needs to be
   *                  stored in the token
   * @param onlyToken if true, only the token will be generated and browser history won't change
   * @return the generated token
   */
  public String place(String eventName,
                      String param,
                      boolean onlyToken) {
    EventMetaData<? extends IsEventBus> metaData = this.eventMetaDataMap.get(eventName);
    if (!enabled && !onlyToken) {
      return null;
    }
    String token = tokenize(metaData.getHistoryName(),
                            param);
    //    if (converters.get(eventName)
    //                  .isCrawlable()) {
    //      token = CRAWLABLE + token;
    //    }
    if (!onlyToken) {
      DomGlobal.window.history.pushState(param,
                                         "",
                                         PlaceService.URL_SEPARATOR + token);
    }
    return token;
  }

  /**
   * Transform an event and its parameters to a token
   *
   * @param eventName event's name
   * @param param     event's parameters
   * @return token to store in the history
   */
  private String tokenize(String eventName,
                          String param) {
    String token = eventName;
    if ((!isNull(param)) && (param.length() > 0)) {
      token = token + getParamSeparator() + param;
    }
    return token;
  }

  public IsHistoryConverter<? extends IsEventBus> getHistoryConverter(String eventName) {
    String key = this.historyNameMap.get(eventName);
    if (!isNull(key)) {
      return this.eventMetaDataMap.get(key)
                                  .getHistoryConverter();
    } else {
      for (String eventNameFromMap : this.historyNameMap.values()) {
        if (eventNameFromMap.equals(eventName)) {
          return this.eventMetaDataMap.get(eventNameFromMap)
                                      .getHistoryConverter();
        }
      }
    }
    return null;
  }
}
