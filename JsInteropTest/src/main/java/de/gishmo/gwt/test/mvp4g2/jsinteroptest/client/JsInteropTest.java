/*
 * Copyright (C) 2017 Frank Hossfeld <frank.hossfeld@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.gishmo.gwt.test.mvp4g2.jsinteroptest.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import de.gishmo.gwt.mvp4g2.jsinterop.client.core.html.Window;

public class JsInteropTest
  implements EntryPoint {

  private static final String BUTTON_WIDTH = "200px";

  @Override
  public void onModuleLoad() {
    FlowPanel flowPanel = new FlowPanel();
    RootPanel.get()
             .add(flowPanel);

    flowPanel.add(createButton("Window Alert",
                               event -> Window.alert("Window alert works!")));

    flowPanel.add(createButton("Window Console Log",
                               event -> Window.getConsole()
                                              .log("Write a log to the console")));
    flowPanel.add(createButton("Window getInnerWidth",
                               event -> Window.alert("innerWdith: " + Window.getInnerWidth() + " px")));
    flowPanel.add(createButton("Window getInnerHeight",
                               event -> Window.alert("innerWdith: " + Window.getInnerHeight() + " px")));
    flowPanel.add(createButton("Window getOuterWidth",
                               event -> Window.alert("outerWidth: " + Window.getOuterWidth() + " px")));
    flowPanel.add(createButton("Window getOuterHeight",
                               event -> Window.alert("outerHeigth: " + Window.getOuterHeight() + " px")));
    flowPanel.add(createButton("Window getLocalStorage",
                               event -> {
                                 Window.getLocalStorage()
                                       .setItem("key01",
                                                "item01");
                                 Window.getLocalStorage()
                                       .setItem("key02",
                                                "item02");
                                 StringBuilder html = new StringBuilder();
                                 for (int i = 0; i < Window.getLocalStorage()
                                                           .getLength(); i++) {
                                   html.append("key: ")
                                       .append(Window.getLocalStorage()
                                                     .key(i))
                                       .append(" - item: ")
                                       .append(Window.getLocalStorage()
                                                     .getItem(Window.getLocalStorage()
                                                                    .key(i)))
                                       .append("\n");
                                 }
                                 Window.alert("local storage: " + "\n" + "\n" + html.toString());
                               }));

    flowPanel.add(createButton("Window getSessionStorage",
                               event -> {
                                 Window.getSessionStorage()
                                       .setItem("key01",
                                                "item01");
                                 Window.getSessionStorage()
                                       .setItem("key02",
                                                "item02");
                                 StringBuilder html = new StringBuilder();
                                 for (int i = 0; i < Window.getSessionStorage()
                                                           .getLength(); i++) {
                                   html.append("key: ")
                                       .append(Window.getSessionStorage()
                                                     .key(i))
                                       .append(" - sitem: ")
                                       .append(Window.getSessionStorage()
                                                     .getItem(Window.getSessionStorage()
                                                                    .key(i)))
                                       .append("\n");
                                 }
                                 Window.alert("session storage: " + "\n" + "\n" + html.toString());
                               }));
    flowPanel.add(createButton("Window Navigator userAgent",
                               event -> Window.alert("Navigator appName: " + Window.getNavigator()
                                                                                   .getUserAgent())));
    flowPanel.add(createButton("Window appName",
                               event -> Window.alert("Navigator appName: " + Window.getNavigator()
                                                                                   .getAppName())));
  }

  private Button createButton(String text,
                              ClickHandler clickHandler) {
    Button button = new Button(text);
    button.getElement()
          .getStyle()
          .setMargin(12,
                     Style.Unit.PX);
    button.setWidth(JsInteropTest.BUTTON_WIDTH);
    button.addClickHandler(clickHandler);
    return button;
  }
}
