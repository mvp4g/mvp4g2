/*
 * Copyright (C) 2016 Frank Hossfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gwt4e.mvp4g.processor.context;

import com.squareup.javapoet.ClassName;
import org.gwt4e.mvp4g.client.annotations.Event;
import org.gwt4e.mvp4g.processor.Constants;
import org.gwt4e.mvp4g.processor.Utils;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * <p>Processor context for generating events.</p>
 * <br><br>
 */
public class EventContext
  extends AbstractProcessorContext {

  private String packageNameEvents;

  private Element           element;
  private ExecutableElement method;

//------------------------------------------------------------------------------

  EventContext(Messager messager,
               Types types,
               Elements elements,
               Element element,
               ExecutableElement method,
               String packageNameEvents) {
    super(messager,
          types,
          elements);

    this.element = element;
    this.method = method;
    this.packageNameEvents = packageNameEvents + ".generated.events";
  }

//------------------------------------------------------------------------------


  public static EventContext create(Messager messager,
                                    Types types,
                                    Elements elements,
                                    Element element) {
    if (element.getKind() != ElementKind.METHOD) {
      messager.printMessage(Diagnostic.Kind.ERROR,
                            String.format("%s applied on a type that's not an method; ignoring.",
                                          Event.class.getCanonicalName()));
      return null;
    }

    if (!((ExecutableElement) element).getReturnType().toString().equals("void")) {
      messager.printMessage(Diagnostic.Kind.ERROR,
                            String.format("%s applied on a method %s that's return type is %s but should be \"void\"",
                                          Event.class.getCanonicalName(),
                                          element.toString(),
                                          ((ExecutableElement) element).getReturnType().toString()));
      return null;
    }

   TypeElement enclosingType = (TypeElement) element.getEnclosingElement();

    return new EventContext(messager,
                            types,
                            elements,
                            element,
                            (ExecutableElement) element,
                            ClassName.get(enclosingType).packageName());
  }

//------------------------------------------------------------------------------

  public String getPackageNameEvents() {
    return packageNameEvents;
  }

  public Element getElement() {
    return element;
  }

  public ExecutableElement getMethod() {
    return method;
  }

  public String getEventHandlerMethodName() {
    return "on" + Utils.capatilize(getMethodName());
  }

  public String getMethodName() {
    return method.getSimpleName()
                 .toString();
  }

  public String getEventClassName() {
    return Utils.capatilize(getMethodName()) + Constants.EVENT_CLASS_POSTFIX;
  }

  public String getEventHandlerClassName() {
    return Utils.capatilize(getMethodName()) + Constants.EVENT_CLASS_POSTFIX + Constants.EVENTHANDER_CLASS_POSTFIX;
  }
}
