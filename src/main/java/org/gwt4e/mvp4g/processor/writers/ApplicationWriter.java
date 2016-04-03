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

package org.gwt4e.mvp4g.processor.writers;

import com.google.gwt.user.client.ui.HasWidgets;
import com.squareup.javapoet.*;
import org.gwt4e.mvp4g.client.application.AbstractApplication;
import org.gwt4e.mvp4g.processor.ProcessorContext;
import org.gwt4e.mvp4g.processor.context.ApplicationContext;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;

/**
 * Created by hoss on 05.01.16.
 */
public class ApplicationWriter
  extends AbstractWriter
  implements Writer {

  private ApplicationContext applicationContext;

  private ApplicationWriter(Builder builder) {
    super(Builder.types,
          Builder.messager,
          Builder.filer,
          Builder.elements,
          Builder.processorContext);

    this.applicationContext = Builder.applicationContext;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public void write()
    throws IOException {

    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(applicationContext.getImplName())
                                        .addOriginatingElement(applicationContext.getInterfaceType())
                                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                        .superclass(AbstractApplication.class);

    MethodSpec.Builder runMethod = MethodSpec.methodBuilder("run")
                                             .addModifiers(Modifier.PUBLIC)
                                             .addParameter(ParameterSpec.builder(HasWidgets.ForIsWidget.class,
                                                                                 "viewPort")
                                                                        .build())
                                             .returns(void.class);
    for (int i = 0; i < applicationContext.getModules()
                                          .size(); i++) {
      TypeElement moduleElement = (TypeElement) ((DeclaredType) applicationContext.getModules()
                                                                                  .get(i)
      ).asElement();
      runMethod.addStatement("new $T(getInternalEventBus())",
                             ClassName.get(applicationContext.getPackageName() + ".generated",
                                           moduleElement.getSimpleName()
                                                        .toString() + "Impl"));
    }
    typeSpec.addMethod(runMethod.build());

    JavaFile.builder(applicationContext.getPackageName() + ".generated",
                     typeSpec.build())
            .build()
            .writeTo(filer);

    System.out.println(JavaFile.builder(applicationContext.getPackageName() + ".generated",
                                        typeSpec.build())
                               .build()
                               .toString());

  }

  public final static class Builder {
    static Types              types;
    static Messager           messager;
    static Filer              filer;
    static Elements           elements;
    static ProcessorContext   processorContext;
    static ApplicationContext applicationContext;


    public Builder types(Types value) {
      types = value;
      return this;
    }

    public Builder messenger(Messager value) {
      messager = value;
      return this;
    }

    public Builder filer(Filer value) {
      filer = value;
      return this;
    }

    public Builder elements(Elements value) {
      elements = value;
      return this;
    }

    public Builder context(ProcessorContext value) {
      processorContext = value;
      return this;
    }

    public Builder applicationContext(ApplicationContext value) {
      applicationContext = value;
      return this;
    }

    public ApplicationWriter build() {
      return new ApplicationWriter(this);
    }
  }
}

//
////------------------------------------------------------------------------------
//
//  private void generate(ApplicationContext context)
//    throws IOException {
//
//    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(context.getImplName())
//                                        .addOriginatingElement(context.getInterfaceType())
//                                        .addModifiers(Modifier.PUBLIC)
//                                        .superclass(AbstractApplication.class);
//
//    MethodSpec.Builder runMethod = MethodSpec.methodBuilder("run")
//                                             .addModifiers(Modifier.PUBLIC)
//                                             .addParameter(ParameterSpec.builder(ForIsWidget.class,
//                                                                                 "viewPort")
//                                                                        .build())
//                                             .returns(void.class);
//    for (int i = 0; i < context.getModules()
//                               .size(); i++) {
//      TypeElement moduleElement = (TypeElement) ((DeclaredType) context.getModules()
//                                                                       .get(i)
//      ).asElement();
//      runMethod.addStatement("new $T(getInternalEventBus())",
//                             ClassName.get(context.getPackageName() + ".generated",
//                                           moduleElement.getSimpleName()
//                                                        .toString() + "Impl"));
//    }
//    typeSpec.addMethod(runMethod.build());
//
////    for (EventContext contextEvent : this.processorContext.getEventContextMap()
////                                                               .get(context.getClassName())
////                                                               .values()) {
////      typeSpec.addMethod(createEventMethod(contextEvent));
////    }
//
//    JavaFile.builder(context.getPackageName() + ".generated",
//                     typeSpec.build())
//            .build()
//            .writeTo(filer);
//
//    System.out.println(JavaFile.builder(context.getPackageName() + ".generated",
//                                        typeSpec.build())
//                               .build()
//                               .toString());
//
//
////    generateApplicaitonImpl(context);
//
////    generateDaggerComponent(context);
////    generateDaggerModule(context);
////    generateDagger(context);
//
//  }
//
//////
//////  private MethodSpec createEventMethod(EventContext contextEvent) {
//////    MethodSpec.Builder method = MethodSpec.methodBuilder(contextEvent.getMethodName())
//////                                          .addAnnotation(Override.class)
//////                                          .addModifiers(Modifier.PUBLIC,
//////                                                        Modifier.FINAL)
//////                                          .returns(void.class);
//////    // method head
//////    for (VariableElement parameter : contextEvent.getMethod()
//////                                                 .getParameters()) {
//////      method.addParameter(ClassName.get(parameter.asType()),
//////                          parameter.getSimpleName()
//////                                   .toString());
//////    }
//////
//////    method.addCode("this.internalEventBus.fireEvent(new $T(",
//////                   ClassName.get(contextEvent.getPackageNameEvents(),
//////                                 contextEvent.getEventClassName()));
//////    for (int i = 0; i < contextEvent.getMethod()
//////                                    .getParameters()
//////                                    .size(); i++) {
//////      VariableElement parameter = contextEvent.getMethod()
//////                                              .getParameters()
//////                                              .get(i);
//////      method.addCode("$N",
//////                     parameter.getSimpleName()
//////                              .toString());
//////      if (i < contextEvent.getMethod()
//////                          .getParameters()
//////                          .size() - 1) {
//////        method.addCode(", ");
//////      }
//////    }
//////    method.addCode("));\n");
//////
//////    return method.build();
//////  }
////
////  private void generateApplicaitonImpl(ApplicationContext context)
////    throws IOException {
////
////    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(context.getImplName())
////                                        .addOriginatingElement(context.getInterfaceType())
////                                        .addModifiers(Modifier.PUBLIC)
////                                        .superclass(AbstractMvp4gEventBus.class);
////
//////    for (EventContext contextEvent : this.processorContext.getEventContextMap()
//////                                                               .get(context.getClassName())
//////                                                               .values()) {
//////      typeSpec.addMethod(createEventMethod(contextEvent));
//////    }
////
////    JavaFile.builder(context.getPackageName() + ".generated",
////                     typeSpec.build())
////            .build()
////            .writeTo(filer);
////
////    System.out.println(JavaFile.builder(context.getPackageName() + ".generated",
////                                        typeSpec.build())
////                               .build()
////                               .toString());
////
////  }
////
////  private void generateDaggerComponent(ApplicationContext context)
////    throws IOException {
////
////    AnnotationSpec.Builder componentAnnotation = AnnotationSpec.builder(Component.class)
////                                                               .addMember("modules",
////                                                                          "ApplicationDaggerModule.class");
////
////    TypeSpec.Builder typeSpec = TypeSpec.interfaceBuilder("ApplicationDaggerComponent")
////                                        .addModifiers(Modifier.PUBLIC)
////                                        .addAnnotation(Singleton.class)
////                                        .addAnnotation(componentAnnotation.build());
////
////    MethodSpec.Builder provideEventBusMethod = MethodSpec.methodBuilder("provideEventBus")
////                                                         .addModifiers(Modifier.PUBLIC,
////                                                                       Modifier.ABSTRACT)
////                                                         .returns(Mvp4gInternalSimpleEventBus.class);
////    typeSpec.addMethod(provideEventBusMethod.build());
////
////    JavaFile.builder(context.getPackageName() + ".generated",
////                     typeSpec.build())
////            .build()
////            .writeTo(filer);
////
////    System.out.println(JavaFile.builder(context.getPackageName() + ".generated",
////                                        typeSpec.build())
////                               .build()
////                               .toString());
////  }
////
////  private void generateDaggerModule(ApplicationContext context)
////    throws IOException {
////
////    TypeSpec.Builder typeSpec = TypeSpec.classBuilder("ApplicationDaggerModule")
////                                        .addModifiers(Modifier.PUBLIC)
////                                        .addAnnotation(Module.class);
////
////    MethodSpec.Builder provideEventBusMethod = MethodSpec.methodBuilder("provideEventBus")
////                                                         .addAnnotation(Provides.class)
////                                                         .addAnnotation(Singleton.class)
////                                                         .returns(Mvp4gInternalSimpleEventBus.class)
////                                                         .addStatement("return new $N()",
////                                                                       Mvp4gInternalSimpleEventBus.class.getSimpleName());
////    typeSpec.addMethod(provideEventBusMethod.build());
////
////    JavaFile.builder(context.getPackageName() + ".generated",
////                     typeSpec.build())
////            .build()
////            .writeTo(filer);
////
////    System.out.println(JavaFile.builder(context.getPackageName() + ".generated",
////                                        typeSpec.build())
////                               .build()
////                               .toString());
////  }
////
////  private void generateDagger(ApplicationContext context)
////    throws IOException {
////
////    TypeSpec.Builder typeSpec = TypeSpec.classBuilder("ApplicationDagger")
////                                        .addOriginatingElement(context.getInterfaceType())
////                                        .addModifiers(Modifier.PUBLIC);
////
////    FieldSpec instanceField = FieldSpec.builder(ClassName.get(context.getPackageName() + ".generated",
////                                                              "ApplicationDagger"),
////                                                "instance")
////                                       .addModifiers(Modifier.PRIVATE,
////                                                     Modifier.STATIC)
////                                       .build();
////    typeSpec.addField(instanceField);
////    FieldSpec componentField = FieldSpec.builder(ClassName.get(context.getPackageName() + ".generated",
////                                                               "ApplicationDaggerComponent"),
////                                                 "component")
////                                        .addModifiers(Modifier.PRIVATE)
////                                        .build();
////    typeSpec.addField(componentField);
////
////
////    MethodSpec.Builder constructorMethod = MethodSpec.constructorBuilder()
////                                                     .addStatement("$N = $T.builder().applicaitonDaggerModule(new $T()).build()",
////                                                                   componentField,
////                                                                   ClassName.get(context.getPackageName() + ".generated",
////                                                                                 "DaggerApplicationDaggerComponent"),
////                                                                   ClassName.get(context.getPackageName() + ".generated",
////                                                                                 "ApplicationDagger"));
////    typeSpec.addMethod(constructorMethod.build());
////
////    MethodSpec.Builder getMethod = MethodSpec.methodBuilder("get")
////                                             .returns(ClassName.get(context.getPackageName() + ".generated",
////                                                                    "ApplicationDagger"))
////                                             .beginControlFlow("if ($N == null)",
////                                                               instanceField)
////                                             .addStatement("$N = new $T()",
////                                                           instanceField,
////                                                           ClassName.get(context.getPackageName() + ".generated",
////                                                                         "ApplicationDagger"))
////                                             .endControlFlow()
////                                             .addStatement("return $N",
////                                                           instanceField);
////    typeSpec.addMethod(getMethod.build());
////
////    MethodSpec.Builder getComponentMethod = MethodSpec.methodBuilder("getComponent")
////                                                      .returns(ClassName.get(context.getPackageName() + ".generated",
////                                                                             "ApplicationDaggerComponent"))
////                                                      .addStatement("return $N",
////                                                                    componentField);
////    typeSpec.addMethod(getComponentMethod.build());
////
//////    MethodSpec.Builder provideEventBusMethod = MethodSpec.methodBuilder("provideEventBus")
//////                                                         .addAnnotation(Provides.class)
//////                                                         .addAnnotation(Singleton.class)
//////                                                         .returns(Mvp4gInternalEventBus.class)
//////                                                         .addStatement("return new $N()",
//////                                                                       Mvp4gInternalSimpleEventBus.class.getSimpleName());
//////    typeSpec.addMethod(provideEventBusMethod.build());
////
////    JavaFile.builder(context.getPackageName() + ".generated",
////                     typeSpec.build())
////            .build()
////            .writeTo(filer);
////
////    System.out.println(JavaFile.builder(context.getPackageName() + ".generated",
////                                        typeSpec.build())
////                               .build()
////                               .toString());
////  }
