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

package org.gwt4e.mvp4g.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * <p>Utility class</p>
 */
public class Utils {

  public static MethodSpec createGetter(VariableElement element,
                                        FieldSpec field) {
    MethodSpec.Builder method = MethodSpec.methodBuilder("get" + Utils.capatilize(field.name))
                                          .addModifiers(Modifier.PUBLIC)
                                          .returns(ClassName.get(element.asType()))
                                          .addStatement("return $N",
                                                        field);
    return method.build();
  }

//------------------------------------------------------------------------------

  public static MethodSpec createSetter(VariableElement parameter,
                                        FieldSpec field) {
    MethodSpec.Builder method = MethodSpec.methodBuilder("set" + Utils.capatilize(field.name))
                                          .addModifiers(Modifier.PUBLIC)
                                          .returns(void.class)
                                          .addParameter(ClassName.get(parameter.asType()),
                                                        parameter.getSimpleName()
                                                                 .toString())
                                          .addStatement("this.$N = $N",
                                                        field,
                                                        field);
    return method.build();
  }

//------------------------------------------------------------------------------

  public static String capatilize(String value) {
    return value.substring(0,
                           1)
                .toUpperCase() + value.substring(1);
  }

//------------------------------------------------------------------------------

  public static TypeElement findEnclosingTypeElement(Element e) {
    while (e != null && !(e instanceof TypeElement)) {
      e = e.getEnclosingElement();
    }
    return TypeElement.class.cast(e);
  }

//------------------------------------------------------------------------------

//  public static TypeElement convert(AnnotationValue value) {
//    return (TypeElement) ((DeclaredType) value.getValue()).asElement();
//  }
//
//------------------------------------------------------------------------------

  public static String getEventPackage(Element element) {
    return ClassName.get((TypeElement) element).packageName();
  }

////------------------------------------------------------------------------------
//
//  public static void printErrorMessage(Messager messager,
//                                       String sourceName,
//                                       Exception e) {
//    StringWriter sw = new StringWriter();
//    PrintWriter pw = new PrintWriter(sw);
//    pw.println("Error generating source file for type " + sourceName);
//    e.printStackTrace(pw);
//    pw.close();
//    messager.printMessage(Diagnostic.Kind.ERROR,
//                          sw.toString());
//  }

//------------------------------------------------------------------------------

  public static TypeElement requireType(Elements elements,
                                        Class<?> clazz) {
    TypeElement type = elements.getTypeElement(clazz.getCanonicalName());
    if (type == null) {
      throw new AssertionError();
    }
    return type;
  }
}
