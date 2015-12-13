/*
 * Copyright (C) 2015 Frank Hossfeld
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
import org.gwt4e.mvp4g.client.Mvp4gEventBus;
import org.gwt4e.mvp4g.client.annotations.EventBus;
import org.gwt4e.mvp4g.processor.ProcessorContext;
import org.gwt4e.mvp4g.processor.Utils;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Processor context for generating teh event bus.</p>
 */
public class EventBusContext
  extends ProcessorContext {

  private TypeElement interfaceType;

  private String packageName;
  private String className;
  private String implName;

  private Map<String, EventContext> eventProcessorContextMap;

//------------------------------------------------------------------------------

  public EventBusContext(Messager messager,
                         Types types,
                         Elements elements,
                         TypeElement interfaceType,
                         String packageName,
                         String className,
                         String implName) {
    super(messager,
          types,
          elements);

    this.interfaceType = interfaceType;
    this.packageName = packageName;
    this.className = className;
    this.implName = implName;

    this.eventProcessorContextMap = new HashMap<>();
  }

//------------------------------------------------------------------------------

  public static EventBusContext create(Messager messager,
                                       Types types,
                                       Elements elements,
                                       Element element) {

    if (element.getKind() != ElementKind.INTERFACE) {
      messager.printMessage(Diagnostic.Kind.ERROR,
                            String.format("%s applied on a type that's not an interface; ignoring",
                                          ((TypeElement) element).getQualifiedName()));
      return null;
    }

    TypeElement eventBusType = Utils.requireType(elements,
                                                 Mvp4gEventBus.class);

    if (!types.isSubtype(element.asType(),
                         eventBusType.asType())) {
      messager.printMessage(Diagnostic.Kind.ERROR,
                            String.format("%s: %s applied on a type that doesn't implement %s; ignoring",
                                          ((TypeElement) element).getQualifiedName(),
                                          EventBus.class.getCanonicalName(),
                                          Mvp4gEventBus.class.getCanonicalName()));
      return null;
    }

    TypeElement interfaceType = (TypeElement) element;
    ClassName interfaceName = ClassName.get(interfaceType);
    String implName = ClassName.get((TypeElement) element)
                               .simpleName() + "Impl";

    return new EventBusContext(messager,
                               types,
                               elements,
                               interfaceType,
                               Utils.getEventPackage(element),
                               Utils.getEventPackage(element) + "." + interfaceName.simpleName(),
                               implName);
  }

//------------------------------------------------------------------------------

  public String getPackageName() {
    return packageName;
  }

  public String getClassName() {
    return className;
  }

  public String getImplName() {
    return implName;
  }

  public TypeElement getInterfaceType() {
    return interfaceType;
  }
}
