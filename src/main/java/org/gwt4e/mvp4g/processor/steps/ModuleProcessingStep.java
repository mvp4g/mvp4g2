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

package org.gwt4e.mvp4g.processor.steps;

import com.google.auto.common.BasicAnnotationProcessor.ProcessingStep;
import com.google.common.collect.SetMultimap;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import org.gwt4e.event.shared.SimpleMvp4gInternalEventBus;
import org.gwt4e.mvp4g.client.AbstractMvp4gModule;
import org.gwt4e.mvp4g.client.annotations.Module;
import org.gwt4e.mvp4g.processor.ProcessorContext;
import org.gwt4e.mvp4g.processor.context.ModuleContext;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

public class ModuleProcessingStep
  extends AbstractProcessStep
  implements ProcessingStep {

  public ModuleProcessingStep(Messager messager,
                              Filer filer,
                              Types types,
                              Elements elements,
                              ProcessorContext processorContext) {
    super(types,
          messager,
          filer,
          elements,
          processorContext);
  }

  public Set<? extends Class<? extends Annotation>> annotations() {
    return Collections.singleton(Module.class);
  }

  public void process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    for (Element element : elementsByAnnotation.get(Module.class)) {
      ModuleContext moduleContext = ModuleContext.create(messager,
                                                         types,
                                                         elements,
                                                         element);
      if (moduleContext == null) {
        return; // error message already emitted
      }
      processorContext.put(element.getSimpleName()
                                  .toString(),
                           moduleContext);
      try {
        generate(moduleContext);
      } catch (IOException ioe) {
        createErrorMessage("Error generating source file for type " + moduleContext.getInterfaceType()
                                                                                   .getQualifiedName());
      }
    }
  }

//------------------------------------------------------------------------------

  private void generate(ModuleContext context)
    throws IOException {

    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(context.getImplName())
                                        .addOriginatingElement(context.getInterfaceType())
                                        .addModifiers(Modifier.PUBLIC)
                                        .superclass(AbstractMvp4gModule.class);

    ParameterSpec eventBusParameter = ParameterSpec.builder(SimpleMvp4gInternalEventBus.class,
                                                            "eventBus")
                                                   .build();
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                               .addModifiers(Modifier.PUBLIC)
                                               .addParameter(eventBusParameter)
                                               .addStatement("super($N)",
                                                             eventBusParameter);
    typeSpec.addMethod(constructor.build());

    JavaFile.builder(context.getPackageName() + ".generated",
                     typeSpec.build())
            .build()
            .writeTo(filer);

    System.out.println(JavaFile.builder(context.getPackageName() + ".generated",
                                        typeSpec.build())
                               .build()
                               .toString());


//    generateApplicaitonImpl(context);

//    generateDaggerComponent(context);
//    generateDaggerModule(context);
//    generateDagger(context);

  }

////
////  private MethodSpec createEventMethod(EventContext contextEvent) {
////    MethodSpec.Builder method = MethodSpec.methodBuilder(contextEvent.getMethodName())
////                                          .addAnnotation(Override.class)
////                                          .addModifiers(Modifier.PUBLIC,
////                                                        Modifier.FINAL)
////                                          .returns(void.class);
////    // method head
////    for (VariableElement parameter : contextEvent.getMethod()
////                                                 .getParameters()) {
////      method.addParameter(ClassName.get(parameter.asType()),
////                          parameter.getSimpleName()
////                                   .toString());
////    }
////
////    method.addCode("this.internalEventBus.fireEvent(new $T(",
////                   ClassName.get(contextEvent.getPackageNameEvents(),
////                                 contextEvent.getEventClassName()));
////    for (int i = 0; i < contextEvent.getMethod()
////                                    .getParameters()
////                                    .size(); i++) {
////      VariableElement parameter = contextEvent.getMethod()
////                                              .getParameters()
////                                              .get(i);
////      method.addCode("$N",
////                     parameter.getSimpleName()
////                              .toString());
////      if (i < contextEvent.getMethod()
////                          .getParameters()
////                          .size() - 1) {
////        method.addCode(", ");
////      }
////    }
////    method.addCode("));\n");
////
////    return method.build();
////  }
//
//  private void generateApplicaitonImpl(ApplicationContext context)
//    throws IOException {
//
//    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(context.getImplName())
//                                        .addOriginatingElement(context.getInterfaceType())
//                                        .addModifiers(Modifier.PUBLIC)
//                                        .superclass(AbstractMvp4gEventBus.class);
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
//  }
//
//  private void generateDaggerComponent(ApplicationContext context)
//    throws IOException {
//
//    AnnotationSpec.Builder componentAnnotation = AnnotationSpec.builder(Component.class)
//                                                               .addMember("modules",
//                                                                          "ApplicationDaggerModule.class");
//
//    TypeSpec.Builder typeSpec = TypeSpec.interfaceBuilder("ApplicationDaggerComponent")
//                                        .addModifiers(Modifier.PUBLIC)
//                                        .addAnnotation(Singleton.class)
//                                        .addAnnotation(componentAnnotation.build());
//
//    MethodSpec.Builder provideEventBusMethod = MethodSpec.methodBuilder("provideEventBus")
//                                                         .addModifiers(Modifier.PUBLIC,
//                                                                       Modifier.ABSTRACT)
//                                                         .returns(SimpleMvp4gInternalEventBus.class);
//    typeSpec.addMethod(provideEventBusMethod.build());
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
//  }
//
//  private void generateDaggerModule(ApplicationContext context)
//    throws IOException {
//
//    TypeSpec.Builder typeSpec = TypeSpec.classBuilder("ApplicationDaggerModule")
//                                        .addModifiers(Modifier.PUBLIC)
//                                        .addAnnotation(Module.class);
//
//    MethodSpec.Builder provideEventBusMethod = MethodSpec.methodBuilder("provideEventBus")
//                                                         .addAnnotation(Provides.class)
//                                                         .addAnnotation(Singleton.class)
//                                                         .returns(SimpleMvp4gInternalEventBus.class)
//                                                         .addStatement("return new $N()",
//                                                                       SimpleMvp4gInternalEventBus.class.getSimpleName());
//    typeSpec.addMethod(provideEventBusMethod.build());
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
//  }
//
//  private void generateDagger(ApplicationContext context)
//    throws IOException {
//
//    TypeSpec.Builder typeSpec = TypeSpec.classBuilder("ApplicationDagger")
//                                        .addOriginatingElement(context.getInterfaceType())
//                                        .addModifiers(Modifier.PUBLIC);
//
//    FieldSpec instanceField = FieldSpec.builder(ClassName.get(context.getPackageName() + ".generated",
//                                                              "ApplicationDagger"),
//                                                "instance")
//                                       .addModifiers(Modifier.PRIVATE,
//                                                     Modifier.STATIC)
//                                       .build();
//    typeSpec.addField(instanceField);
//    FieldSpec componentField = FieldSpec.builder(ClassName.get(context.getPackageName() + ".generated",
//                                                               "ApplicationDaggerComponent"),
//                                                 "component")
//                                        .addModifiers(Modifier.PRIVATE)
//                                        .build();
//    typeSpec.addField(componentField);
//
//
//    MethodSpec.Builder constructorMethod = MethodSpec.constructorBuilder()
//                                                     .addStatement("$N = $T.builder().applicaitonDaggerModule(new $T()).build()",
//                                                                   componentField,
//                                                                   ClassName.get(context.getPackageName() + ".generated",
//                                                                                 "DaggerApplicationDaggerComponent"),
//                                                                   ClassName.get(context.getPackageName() + ".generated",
//                                                                                 "ApplicationDagger"));
//    typeSpec.addMethod(constructorMethod.build());
//
//    MethodSpec.Builder getMethod = MethodSpec.methodBuilder("get")
//                                             .returns(ClassName.get(context.getPackageName() + ".generated",
//                                                                    "ApplicationDagger"))
//                                             .beginControlFlow("if ($N == null)",
//                                                               instanceField)
//                                             .addStatement("$N = new $T()",
//                                                           instanceField,
//                                                           ClassName.get(context.getPackageName() + ".generated",
//                                                                         "ApplicationDagger"))
//                                             .endControlFlow()
//                                             .addStatement("return $N",
//                                                           instanceField);
//    typeSpec.addMethod(getMethod.build());
//
//    MethodSpec.Builder getComponentMethod = MethodSpec.methodBuilder("getComponent")
//                                                      .returns(ClassName.get(context.getPackageName() + ".generated",
//                                                                             "ApplicationDaggerComponent"))
//                                                      .addStatement("return $N",
//                                                                    componentField);
//    typeSpec.addMethod(getComponentMethod.build());
//
////    MethodSpec.Builder provideEventBusMethod = MethodSpec.methodBuilder("provideEventBus")
////                                                         .addAnnotation(Provides.class)
////                                                         .addAnnotation(Singleton.class)
////                                                         .returns(Mvp4gInternalEventBus.class)
////                                                         .addStatement("return new $N()",
////                                                                       SimpleMvp4gInternalEventBus.class.getSimpleName());
////    typeSpec.addMethod(provideEventBusMethod.build());
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
//  }
}
