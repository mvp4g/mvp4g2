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

package gwt.mvp4g.processor.shell;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

import com.google.auto.common.BasicAnnotationProcessor.ProcessingStep;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import gwt.core.mvp4g.client.event.Mvp4gEventBus;
import gwt.mvp4g.client.annotations.Shell;
import gwt.mvp4g.client.place.AbstractPlaceController;
import gwt.mvp4g.client.place.IsShellWrapper;
import gwt.mvp4g.processor.AbstractProcessStep;
import gwt.mvp4g.processor.ProcessorException;
import gwt.mvp4g.processor.ProcessorUtils;
import gwt.mvp4g.processor.shell.model.ShellModel;

class ShellProcessingStep
  extends AbstractProcessStep
  implements ProcessingStep {

  private ShellProcessingStep(Builder builder) {
    super(builder.messager,
          builder.filer,
          builder.types,
          builder.elements);
  }

  static Builder processingStepBuilder() {
    return new Builder();
  }

  @Override
  public Set<? extends Class<? extends Annotation>> annotations() {
    return ImmutableSet.of(Shell.class);
  }

  @Override
  public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    // generel validations
//    try {
//      validateElementsByAnnotation(elementsByAnnotation);
//    } catch (ProcessorException e) {
//      createErrorMessage("Error creating application model -> general validations fails: " + e.getMessage());
//      return ImmutableSet.of();
//    }

    for (Element element : elementsByAnnotation.get(Shell.class)) {
//      // validate element
//      try {
//        validateEleemnt((TypeElement) element);
//      } catch (ProcessorException e) {
//        createErrorMessage("Error validating application model -> element validations fails: " + e.getMessage());
//        return ImmutableSet.of();
//      }
      // create model
      ShellModel shellModel;
      try {
        shellModel = ShellModel.modelBuilder()
                               .setMessager(messager)
                               .setElements(elements)
                               .setTypes(types)
                               .setShell((TypeElement) element)
                               .build();
      } catch (ProcessorException e) {
        createErrorMessage("Error creating Shell model -> element validations fails: " + e.getMessage());
        return ImmutableSet.of();
      }
      // Generate ...
      try {
        generatePlaceController(shellModel);
      } catch (IOException e) {
        createErrorMessage("Error generating PlaceController. Unable to write generated source file: " + e.getMessage());
        return ImmutableSet.of();
      }
      try {
        generateShellWrapper(shellModel);
      } catch (IOException e) {
        createErrorMessage("Error generating PlaceController. Unable to write generated source file: " + e.getMessage());
        return ImmutableSet.of();
      }
    }

    return ImmutableSet.of();
  }

//  private void validateElementsByAnnotation(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation)
//    throws ProcessorException {
//    // at least there should exatly one Application annotation!
//    if (elementsByAnnotation.get(Application.class)
//                            .size() == 0) {
//      throw new ProcessorException("Missing Mvp4g Application interface");
//    }
//    // at least there should only one Application annotation!
//    if (elementsByAnnotation.get(Application.class)
//                            .size() > 1) {
//      throw new ProcessorException("There should be at least only one interface, that is annotated with @Application.");
//    }
//  }
//
//  private void validateEleemnt(TypeElement element)
//    throws ProcessorException {
//    if (element.getKind() != ElementKind.INTERFACE) {
//      throw new ProcessorException(String.format("%s applied on a type that's not an interface; ignoring.",
//                                                 Application.class.getCanonicalName()));
//    }
//  }

  private void generatePlaceController(ShellModel shellModel)
    throws IOException {
    String pkgName = elements.getPackageOf(shellModel.getApplication())
                             .toString();
    String         typeName       = ProcessorUtils.createNameFromClass("PlaceController");
    JavaFileObject javaFileObject = filer.createSourceFile(pkgName + "." + typeName);

    try (Writer writer = javaFileObject.openWriter()) {
      TypeSpec.Builder typeSpec = TypeSpec.classBuilder(typeName)
                                          .addModifiers(Modifier.PUBLIC)
                                          .addAnnotation(AnnotationSpec.builder(Generated.class)
                                                                       .addMember("value",
                                                                                  "\"$L\"",
                                                                                  ShellProcessor.class.getName())
                                                                       .build())
                                          .superclass(ParameterizedTypeName.get(AbstractPlaceController.class));

      MethodSpec constructor = MethodSpec.constructorBuilder()
                                         .addParameter(ParameterSpec.builder(Mvp4gEventBus.class,
                                                                             "internalEventBus")
                                                                    .build())
                                         .addModifiers(Modifier.PUBLIC)
                                         .addStatement("super(internalEventBus)")
                                         .build();
      typeSpec.addMethod(constructor);

      MethodSpec initializeMethod = MethodSpec.methodBuilder("initialize")
                                              .addModifiers(Modifier.PROTECTED)
                                              .returns(void.class)
                                              .addStatement("this.shellWrapper = new $T()",
                                                            ClassName.get(elements.getPackageOf(shellModel.getShell())
                                                                                  .toString(),
                                                                          ProcessorUtils.createWrapperNameFromEnclosedTypes(shellModel.getShell())))
                                              .build();
      typeSpec.addMethod(initializeMethod);




      JavaFile applicationFileGenerated = JavaFile.builder(pkgName,
                                                           typeSpec.build())
                                                  .build();
      System.out.println(applicationFileGenerated.toString());
      applicationFileGenerated.writeTo(writer);
    }
  }

  private void generateShellWrapper(ShellModel shellModel)
    throws IOException {
    String pkgName = elements.getPackageOf(shellModel.getShell())
                             .toString();
    String         typeName       = ProcessorUtils.createWrapperNameFromEnclosedTypes(shellModel.getShell());
    JavaFileObject javaFileObject = filer.createSourceFile(pkgName + "." + typeName);

    try (Writer writer = javaFileObject.openWriter()) {
      TypeSpec.Builder typeSpec = TypeSpec.classBuilder(typeName)
                                          .addModifiers(Modifier.PUBLIC)
                                          .addAnnotation(AnnotationSpec.builder(Generated.class)
                                                                       .addMember("value",
                                                                                  "\"$L\"",
                                                                                  ShellProcessor.class.getName())
                                                                       .build())
                                          .addSuperinterface(TypeName.get(IsShellWrapper.class));

      FieldSpec shellField = FieldSpec.builder(TypeName.get(shellModel.getShell()
                                                                      .asType()),
                                               "shell",
                                               Modifier.PRIVATE)
                                      .build();
      typeSpec.addField(shellField);

      typeSpec.addMethod(MethodSpec.constructorBuilder()
                                   .addModifiers(Modifier.PUBLIC)
                                   .addStatement("super()")
                                   .addStatement("this.$N = new $T()",
                                                 shellField,
                                                 TypeName.get(shellModel.getShell()
                                                                        .asType()))
                                   .addStatement("$T view = $T.create($T.class)",
                                                 TypeName.get(shellModel.getView()
                                                                        .asType()),
                                                 TypeName.get(GWT.class),
                                                 TypeName.get(shellModel.getView()
                                                                        .asType()))
                                   .addStatement("$N.setView(view)",
                                                 shellField)
                                   .addStatement("view.setPresenter($N)",
                                                 shellField)
                                   .build());

      typeSpec.addMethod(MethodSpec.methodBuilder("getShellPresenter")
                                   .addAnnotation(Override.class)
                                   .addModifiers(Modifier.PUBLIC)
                                   .returns(Widget.class)
                                   .addStatement("return this.$N.asWidget()",
                                                 shellField)
                                   .build());

//      MethodSpec createInitializeMethod = MethodSpec.methodBuilder("initialize")
//                                                    .addModifiers(Modifier.PROTECTED)
//                                                    .returns(Void.class)
//                                                    .addStatement()
//                                                    .build();
//      typeSpec.addMethod(createInitializeMethod);
//
////      MethodSpec createPlaceControllerMethod = MethodSpec.methodBuilder("createPlaceController")
////                                                         .addModifiers(Modifier.PROTECTED)
////                                                         .returns(IsShellWrapper.class)
////                                                         .addStatement("return new $T()",
////                                                                       ClassName.get(IsShellWrapper.class.getPackage()
////                                                                                                          .getName(),
////                                                                                     ProcessorUtils.createNameFromClass(IsShellWrapper.class.getSimpleName())))
////                                                         .build();
////      typeSpec.addMethod(createPlaceControllerMethod);
////
      JavaFile applicationFileGenerated = JavaFile.builder(pkgName,
                                                           typeSpec.build())
                                                  .build();
      System.out.println(applicationFileGenerated.toString());
      applicationFileGenerated.writeTo(writer);
    }
  }

  static final class Builder {
    private Messager messager;
    private Filer    filer;
    private Types    types;
    private Elements elements;

    Builder setMessager(Messager messager) {
      this.messager = messager;
      return this;
    }

    Builder setFiler(Filer filer) {
      this.filer = filer;
      return this;
    }

    Builder setTypes(Types types) {
      this.types = types;
      return this;
    }

    Builder setElements(Elements elements) {
      this.elements = elements;
      return this;
    }

    ShellProcessingStep build() {
      return new ShellProcessingStep(this);
    }
  }

////  public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
////    // at least there should only one Application annotation!
////    if (elementsByAnnotation.get(Application.class)
////                            .size() > 1) {
////      createErrorMessage("Error generating Application. There should be at least only one interface, that is annotated with @Application.");
////      return ImmutableSet.of();
////    }
////
////    for (Element element : elementsByAnnotation.get(Application.class)) {
////      FastClasspathScanner fastClasspathScanner = new FastClasspathScanner("org.gwt4e.mvp4g.test.apt.application").matchClassesWithAnnotation(Application.class,
////                                                                                                                                              c -> System.out.println("Has a BindTo class annotation: " + c.getName()))
////                                                                                                                  .verbose()
////                                                                                                                  .scan();
////      ApplicationContext applicationContext = ApplicationContext.create(messager,
////                                                                        types,
////                                                                        elements,
////                                                                        element);
////      if (applicationContext == null) {
////        return ImmutableSet.of(); // error message already emitted
////      }
////      this.processorContext.setApplicationContext(applicationContext);
////      try {
////        ApplicationWriter writer = ApplicationWriter.builder()
////                                                    .messenger(super.messager)
////                                                    .context(super.processorContext)
////                                                    .elements(super.elements)
////                                                    .filer(super.filer)
////                                                    .types(super.types)
////                                                    .applicationContext(applicationContext)
////                                                    .build();
////        writer.write();
////      } catch (IOException ioe) {
////        createErrorMessage("Error generating source file for type " + applicationContext.getInterfaceType()
////                                                                                        .getQualifiedName(),
////                           ioe);
////      }
////    }
////
////    return ImmutableSet.of();
////  }
}
