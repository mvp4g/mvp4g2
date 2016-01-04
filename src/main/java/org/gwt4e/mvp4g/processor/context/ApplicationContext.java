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
import org.gwt4e.mvp4g.client.Mvp4gApplication;
import org.gwt4e.mvp4g.client.Mvp4gModule;
import org.gwt4e.mvp4g.client.annotations.Application;
import org.gwt4e.mvp4g.processor.Utils;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Processor context for generating events.</p>
 * <br><br>
 */
public class ApplicationContext
  extends AbstractProcessorContext {

  private TypeElement interfaceType;

  private String packageName;
  private String className;
  private String implName;

  private Element element;

  private Application.ApplicationType applicationType;
  private List<TypeMirror>            modules;

  ApplicationContext(Messager messager,
                     Types types,
                     Elements elements,
                     TypeElement interfaceType,
                     String packageName,
                     String className,
                     String implName) {
    super(messager,
          types,
          elements);

    this.modules = new ArrayList<>();

    this.interfaceType = interfaceType;
    this.packageName = packageName;
    this.className = className;
    this.implName = implName;

    Application applicationAnnotation = interfaceType.getAnnotation(Application.class);
    applicationType = applicationAnnotation.applicationType();
    try {
      Class<? extends Mvp4gModule>[] m = applicationAnnotation.modules();
    } catch(MirroredTypesException e) {
      for (int i = 0; i < e.getTypeMirrors().size(); i++) {
        modules.add(e.getTypeMirrors().get(i));
      }
    }
  }

  public static ApplicationContext create(Messager messager,
                                          Types types,
                                          Elements elements,
                                          Element element) {
    // msut be an interface
    if (element.getKind() != ElementKind.INTERFACE) {
      messager.printMessage(Diagnostic.Kind.ERROR,
                            String.format("%s applied on a type %s that's not an interface; ignoring",
                                          Application.class.getSimpleName(),
                                          ((TypeElement) element).getQualifiedName()));
      return null;
    }

    // Should extend org.gwt4e.mvp4g.client.Mvp4gApplication
    boolean foundSuperInterface = false;
    for (TypeMirror superType : types.directSupertypes(element.asType())) {
      if (((DeclaredType) superType).asElement()
                                    .toString()
                                    .equals(ClassName.get(Mvp4gApplication.class)
                                                     .toString())) {
        foundSuperInterface = true;
        break;
      }
    }
    if (!foundSuperInterface) {
      messager.printMessage(Diagnostic.Kind.ERROR,
                            String.format("%s does not extend %s",
                                          ((TypeElement) element).getQualifiedName(),
                                          Mvp4gApplication.class.getName()
                                                                .toString()));
      return null;
    }

    TypeElement interfaceType = (TypeElement) element;
    ClassName interfaceName = ClassName.get(interfaceType);
    String implName = ClassName.get((TypeElement) element)
                               .simpleName() + "Impl";

    return new ApplicationContext(messager,
                                  types,
                                  elements,
                                  interfaceType,
                                  Utils.getEventPackage(element),
                                  Utils.getEventPackage(element) + "." + interfaceName.simpleName(),
                                  implName);
  }

//------------------------------------------------------------------------------

  public String getImplName() {
    return implName;
  }

  public TypeElement getInterfaceType() {
    return interfaceType;
  }

  public String getPackageName() {
    return packageName;
  }

  public List<TypeMirror> getModules() {
    return modules;
  }
//  public String getPackageNameApplication() {
//    return packageNameApplication;
//  }
//
//  public Element getElement() {
//    return element;
//  }
//
//  public ExecutableElement getMethod() {
//    return method;
//  }
//
//  public String getEventHandlerMethodName() {
//    return "on" + Utils.capatilize(getMethodName());
//  }
//
//  public String getMethodName() {
//    return method.getSimpleName()
//                 .toString();
//  }
//
//  public String getEventClassName() {
//    return Utils.capatilize(getMethodName()) + Constants.EVENT_CLASS_POSTFIX;
//  }
//
//  public String getEventHandlerClassName() {
//    return Utils.capatilize(getMethodName()) + Constants.EVENT_CLASS_POSTFIX + Constants.EVENTHANDER_CLASS_POSTFIX;
//  }
}
