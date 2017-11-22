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

package gwt.mvp4g.processor.application;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

import com.google.auto.common.BasicAnnotationProcessor.ProcessingStep;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import gwt.mvp4g.client.annotations.Application;
import gwt.mvp4g.client.application.AbstractApplication;
import gwt.mvp4g.client.place.IsPlaceController;
import gwt.mvp4g.processor.AbstractProcessStep;
import gwt.mvp4g.processor.ProcessorException;
import gwt.mvp4g.processor.ProcessorUtils;
import gwt.mvp4g.processor.application.model.ApplicationModel;

class ApplicationProcessingStep
  extends AbstractProcessStep
  implements ProcessingStep {

  private ApplicationProcessingStep(Builder builder) {
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
    return ImmutableSet.of(Application.class);
  }

  @Override
  public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    // generel validations
    try {
      validateElementsByAnnotation(elementsByAnnotation);
    } catch (ProcessorException e) {
      createErrorMessage("Error creating application model -> general validations fails: " + e.getMessage());
      return ImmutableSet.of();
    }

    for (Element element : elementsByAnnotation.get(Application.class)) {
      // validate element
      try {
        validateEleemnt((TypeElement) element);
      } catch (ProcessorException e) {
        createErrorMessage("Error validating application model -> element validations fails: " + e.getMessage());
        return ImmutableSet.of();
      }
      // create model
      ApplicationModel applicationModel;
      try {
        applicationModel = ApplicationModel.modelBuilder()
                                           .setMessager(messager)
                                           .setElements(elements)
                                           .setTypes(types)
                                           .setApplication((TypeElement) element)
                                           .build();
      } catch (ProcessorException e) {
        createErrorMessage("Error creating application model -> element validations fails: " + e.getMessage());
        return ImmutableSet.of();
      }
      // Generate ...
      try {
        generate(applicationModel);
      } catch (IOException e) {
        createErrorMessage("Error generating application. Unable to write generated source file: " + e.getMessage());
        return ImmutableSet.of();
      }
    }

    return ImmutableSet.of();
  }

  private void validateElementsByAnnotation(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation)
    throws ProcessorException {
    // at least there should exatly one Application annotation!
    if (elementsByAnnotation.get(Application.class)
                            .size() == 0) {
      throw new ProcessorException("Missing Mvp4g Application interface");
    }
    // at least there should only one Application annotation!
    if (elementsByAnnotation.get(Application.class)
                            .size() > 1) {
      throw new ProcessorException("There should be at least only one interface, that is annotated with @Application.");
    }
  }

  private void validateEleemnt(TypeElement element)
    throws ProcessorException {
    if (element.getKind() != ElementKind.INTERFACE) {
      throw new ProcessorException(String.format("%s applied on a type that's not an interface; ignoring.",
                                                 Application.class.getCanonicalName()));
    }
  }

  private void generate(ApplicationModel applicationModel)
    throws IOException {
    String pkgName = elements.getPackageOf(applicationModel.getApplication())
                             .toString();
    String         typeName       = ProcessorUtils.createNameFromEnclosedTypes(applicationModel.getApplication());
    JavaFileObject javaFileObject = filer.createSourceFile(pkgName + "." + typeName);

    try (Writer writer = javaFileObject.openWriter()) {
      TypeSpec.Builder typeSpec = TypeSpec.classBuilder(typeName)
                                          .addModifiers(Modifier.PUBLIC)
                                          .addAnnotation(AnnotationSpec.builder(Generated.class)
                                                                       .addMember("value",
                                                                                  "\"$L\"",
                                                                                  ApplicationProcessor.class.getName())
                                                                       .build())
                                          .superclass(ParameterizedTypeName.get(AbstractApplication.class))
                                          .addSuperinterface(TypeName.get(applicationModel.getApplication()
                                                                                          .asType()));

      MethodSpec placeControllerMethod = MethodSpec.methodBuilder("createPlaceController")
                                                   .addModifiers(Modifier.PROTECTED)
                                                   .returns(IsPlaceController.class)
                                                   .addStatement("return new $T(this.internalEventBus)",
                                                                 ClassName.get(pkgName,
                                                                               ProcessorUtils.createNameFromClass("PlaceController")))
                                                   .build();
      typeSpec.addMethod(placeControllerMethod);

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

    ApplicationProcessingStep build() {
      return new ApplicationProcessingStep(this);
    }
  }

//  public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
//    // at least there should only one Application annotation!
//    if (elementsByAnnotation.get(Application.class)
//                            .size() > 1) {
//      createErrorMessage("Error generating Application. There should be at least only one interface, that is annotated with @Application.");
//      return ImmutableSet.of();
//    }
//
//    for (Element element : elementsByAnnotation.get(Application.class)) {
//      FastClasspathScanner fastClasspathScanner = new FastClasspathScanner("org.gwt4e.mvp4g.test.apt.application").matchClassesWithAnnotation(Application.class,
//                                                                                                                                              c -> System.out.println("Has a BindTo class annotation: " + c.getName()))
//                                                                                                                  .verbose()
//                                                                                                                  .scan();
//      ApplicationContext applicationContext = ApplicationContext.create(messager,
//                                                                        types,
//                                                                        elements,
//                                                                        element);
//      if (applicationContext == null) {
//        return ImmutableSet.of(); // error message already emitted
//      }
//      this.processorContext.setApplicationContext(applicationContext);
//      try {
//        ApplicationWriter writer = ApplicationWriter.builder()
//                                                    .messenger(super.messager)
//                                                    .context(super.processorContext)
//                                                    .elements(super.elements)
//                                                    .filer(super.filer)
//                                                    .types(super.types)
//                                                    .applicationContext(applicationContext)
//                                                    .build();
//        writer.write();
//      } catch (IOException ioe) {
//        createErrorMessage("Error generating source file for type " + applicationContext.getInterfaceType()
//                                                                                        .getQualifiedName(),
//                           ioe);
//      }
//    }
//
//    return ImmutableSet.of();
//  }
}
