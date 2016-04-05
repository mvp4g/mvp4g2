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
import org.gwt4e.mvp4g.client.Mvp4gEventBus;
import org.gwt4e.mvp4g.client.annotations.Debug;
import org.gwt4e.mvp4g.client.annotations.EventBus;
import org.gwt4e.mvp4g.client.event.Mvp4gLogger;
import org.gwt4e.mvp4g.processor.Utils;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Processor context for generating teh event bus.</p>
 */
public class EventBusContext
  extends AbstractProcessorContext {

  private TypeElement interfaceType;

  private String packageName;
  private String className;
  private String implName;

  private Map<String, EventContext> eventProcessorContextMap;
  private boolean                   debug;
  private Debug.LogLevel            logLevel;
  private TypeMirror                logger;

  private EventBusContext(Messager messager,
                          Types types,
                          Elements elements,
                          TypeElement interfaceType,
                          String packageName,
                          String className,
                          String implName,
                          boolean debug,
                          Debug.LogLevel logLevel,
                          TypeMirror logger) {
    super(messager,
          types,
          elements);

    this.interfaceType = interfaceType;
    this.packageName = packageName;
    this.className = className;
    this.implName = implName;

    this.eventProcessorContextMap = new HashMap<>();
    this.debug = debug;
    this.logLevel = logLevel;
    this.logger = logger;
  }

  public static EventBusContext create(Messager messager,
                                       Types types,
                                       Elements elements,
                                       Element element) {

    // msut be an interface
    if (element.getKind() != ElementKind.INTERFACE) {
      messager.printMessage(Diagnostic.Kind.ERROR,
                            String.format("%s applied on a type %s that's not an interface; ignoring",
                                          EventBus.class.getSimpleName(),
                                          ((TypeElement) element).getQualifiedName()));
      return null;
    }

    // Should extend org.gwt4e.mvp4g.client.Mvp4gInternalEventBus
    if (!Utils.isExtending(types,
                           element,
                           Mvp4gEventBus.class)) {
      messager.printMessage(Diagnostic.Kind.ERROR,
                            String.format("%s does not extend %s",
                                          ((TypeElement) element).getQualifiedName(),
                                          Mvp4gEventBus.class.getName()
                                                             .toString()));
      return null;
    }

    TypeElement interfaceType = (TypeElement) element;
    ClassName interfaceName = ClassName.get(interfaceType);
    String implName = ClassName.get((TypeElement) element)
                               .simpleName() + "Impl";
    boolean debug = false;
    Debug.LogLevel logLevel = null;
    TypeMirror logger = null;
    if (Utils.hasAnnotation(element,
                            Debug.class)) {
      debug = true;
      Debug debugAnnotion = element.getAnnotation(Debug.class);
      logLevel = debugAnnotion.logLevel();
      try {
        Class<? extends Mvp4gLogger> l = debugAnnotion.logger();
      } catch (MirroredTypeException e) {
        logger = e.getTypeMirror();
      }
    }

    return new EventBusContext(messager,
                               types,
                               elements,
                               interfaceType,
                               Utils.getEventPackage(element),
                               Utils.getEventPackage(element) + "." + interfaceName.simpleName(),
                               implName,
                               debug,
                               logLevel,
                               logger);
  }

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

  public boolean hasDebug() {
    return debug;
  }

  public Debug.LogLevel getLogLevel() {
    return logLevel;
  }

  public TypeMirror getLogger() {
    return logger;
  }
}
