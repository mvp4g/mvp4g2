package de.gishmo.gwt.mvp4g2.processor.handler.eventhandler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.client.internal.ui.EventHandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.internal.ui.PresenterHandlerMetaData;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.handler.eventhandler.validation.PresenterAnnotationValidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO check, that @Eventhandler is annoted at a class that extends AbstractPresent!
public class PresenterAnnotationHandler {

  private final static String IMPL_NAME = "MetaData";

  private ProcessorUtils processorUtils;
  private PresenterUtils presenterUtils;

  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;

  @SuppressWarnings("unused")
  private PresenterAnnotationHandler(Builder builder) {
    super();

    this.processingEnvironment = builder.processingEnvironment;
    this.roundEnvironment = builder.roundEnvironment;

    setUp();
  }

  private void setUp() {
    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
    this.presenterUtils = PresenterUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void process()
    throws ProcessorException, IOException {
    // check, whether we have o do something ...
    if (roundEnvironment.getElementsAnnotatedWith(Presenter.class)
                        .size() == 0) {
      return;
    }
    // valildate @Application annotation
    PresenterAnnotationValidator presenterAnnotationValidator = PresenterAnnotationValidator.builder()
                                                                                            .processingEnvironment(this.processingEnvironment)
                                                                                            .roundEnvironment(this.roundEnvironment)
                                                                                            .build();

    // valildate @Application annotation
    presenterAnnotationValidator.validate();
    // valdaite and generate
    for (Element element : this.roundEnvironment.getElementsAnnotatedWith(Presenter.class)) {
      presenterAnnotationValidator.validate(element);
      this.generate(element);
      // handlng the annotated event handling metohds
      this.presenterUtils.createMetaFile((TypeElement) element,
                                         true);
    }
  }

  private void generate(Element element)
    throws IOException {
    Presenter presenter = element.getAnnotation(Presenter.class);
    TypeElement typeElement = (TypeElement) element;

    TypeElement viewClassElement = this.presenterUtils.getViewClassTypeElement(presenter);
    TypeElement viewInterfaceElement = this.presenterUtils.getViewInterfaceTypeElement(presenter);

    String className = this.processorUtils.createFullClassName(this.processorUtils.getPackageAsString(element),
                                                               element.getSimpleName()
                                                                      .toString()) + PresenterAnnotationHandler.IMPL_NAME;
    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.processorUtils.setFirstCharacterToUpperCase(className))
                                        .superclass(ParameterizedTypeName.get(ClassName.get(PresenterHandlerMetaData.class),
                                                                              ClassName.get(this.processorUtils.getPackageAsString(typeElement),
                                                                                            typeElement.getSimpleName()
                                                                                                       .toString()),
                                                                              ClassName.get(this.processorUtils.getPackageAsString(viewInterfaceElement),
                                                                                            viewInterfaceElement.getSimpleName()
                                                                                                                .toString())))
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL);

    // constructor ...
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                               .addModifiers(Modifier.PUBLIC)
                                               .addStatement("super($S, $T.PRESENTER, $N, $T.$L)",
                                                             this.processorUtils.getCanonicalClassName(typeElement),
                                                             ClassName.get(EventHandlerMetaData.Kind.class),
                                                             presenter.multiple() ? "true" : "false",
                                                             ClassName.get(Presenter.VIEW_CREATION_METHOD.class),
                                                             this.presenterUtils.getCreator(typeElement));
    constructor.addStatement("super.presenter = new $T()",
                             ClassName.get(this.processorUtils.getPackageAsString(typeElement),
                                           typeElement.getSimpleName()
                                                      .toString()));
    if (Presenter.VIEW_CREATION_METHOD.FRAMEWORK.equals(this.presenterUtils.getCreator(typeElement))) {
      constructor.addStatement("super.view = ($T) new $T()",
                               ClassName.get(this.processorUtils.getPackageAsString(viewClassElement),
                                             viewInterfaceElement.getSimpleName()
                                                                 .toString()),
                               ClassName.get(this.processorUtils.getPackageAsString(viewClassElement),
                                             viewClassElement.getSimpleName()
                                                             .toString()));
    } else {
      constructor.addStatement("super.view = presenter.createView()");
    }
    typeSpec.addMethod(constructor.build());

    JavaFile javaFile = JavaFile.builder(this.processorUtils.getPackageAsString(element),
                                         typeSpec.build())
                                .build();
    javaFile.writeTo(this.processingEnvironment.getFiler());
//    System.out.println(javaFile.toString());
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;
    RoundEnvironment      roundEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public Builder roundEnvironment(RoundEnvironment roundEnvironment) {
      this.roundEnvironment = roundEnvironment;
      return this;
    }

    public PresenterAnnotationHandler build() {
      return new PresenterAnnotationHandler(this);
    }

  }


}
