package de.gishmo.gwt.mvp4g2.processor.handler.application;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.application.AbstractApplication;
import de.gishmo.gwt.mvp4g2.client.application.IsApplicationLoader;
import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.handler.application.validation.ApplicationAnnotationValidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import java.io.IOException;

public class ApplicationAnnotationHandler {

  private final static String    IMPL_NAME           = "Impl";
  private final static ClassName AbstractApplication = ClassName.get(AbstractApplication.class);

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;

  @SuppressWarnings("unused")
  private ApplicationAnnotationHandler(Builder builder) {
    super();

    this.processingEnvironment = builder.processingEnvironment;
    this.roundEnvironment = builder.roundEnvironment;

    setUp();
  }

  private void setUp() {
    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void process()
    throws ProcessorException, IOException {
    // check, whether we have o do something ...
    if (roundEnvironment.getElementsAnnotatedWith(Application.class)
                        .size() == 0) {
      return;
    }
    // create @Application-Validator
    ApplicationAnnotationValidator applicationAnnotationValidator = ApplicationAnnotationValidator.builder()
                                                                                      .processingEnvironment(this.processingEnvironment)
                                                                                      .roundEnvironment(this.roundEnvironment)
                                                                                      .build();

    // valildate @Application annotation
    applicationAnnotationValidator.validate();
    // valdaite and generate
    for (Element element : this.roundEnvironment.getElementsAnnotatedWith(Application.class)) {
      applicationAnnotationValidator.validate(element);
      this.generate(element);
    }
  }

  private void generate(Element element)
    throws IOException {
    Application applicationAnnotation = element.getAnnotation(Application.class);

    TypeElement eventBusTypeElement = this.getEventBusTypeElement(applicationAnnotation);
    TypeElement apllicaitonLoaderTypeElement = this.getApplicationLoaderTypeElement(applicationAnnotation);

    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(element.getSimpleName() + ApplicationAnnotationHandler.IMPL_NAME)
                                        .superclass(ParameterizedTypeName.get(AbstractApplication,
                                                                              ClassName.get(this.processorUtils.getPackageAsString(eventBusTypeElement),
                                                                                            eventBusTypeElement.getSimpleName()
                                                                                                               .toString())))
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL)
                                        .addSuperinterface(ClassName.get(this.processorUtils.getPackageAsString(element),
                                                                         element.getSimpleName()
                                                                                .toString()));

    // constructor ...
    MethodSpec constructor = MethodSpec.constructorBuilder()
                                       .addModifiers(Modifier.PUBLIC)
                                       .addStatement("super()")
                                       .addStatement("super.eventBus = new $N.$N()",
                                                     this.processorUtils.getPackageAsString(eventBusTypeElement),
                                                     eventBusTypeElement.getSimpleName()
                                                                        .toString() + ApplicationAnnotationHandler.IMPL_NAME)
                                       .build();
    typeSpec.addMethod(constructor);

    // method "getApplicaitonLoader"
    MethodSpec getApplicaitonLaoderMethod = MethodSpec.methodBuilder("getApplicationLoader")
                                                      .addModifiers(Modifier.PUBLIC)
                                                      .addAnnotation(Override.class)
                                                      .returns(IsApplicationLoader.class)
                                                      .addStatement("return new $T()",
                                                                    ClassName.get(this.processorUtils.getPackageAsString(apllicaitonLoaderTypeElement),
                                                                                  apllicaitonLoaderTypeElement.getSimpleName()
                                                                                                              .toString()))
                                                      .build();
    typeSpec.addMethod(getApplicaitonLaoderMethod);


    JavaFile javaFile = JavaFile.builder(this.processorUtils.getPackageAsString(element),
                                         typeSpec.build())
                                .build();
    javaFile.writeTo(this.processingEnvironment.getFiler());
  }

  private TypeElement getEventBusTypeElement(Application applicationAnnotation) {
    try {
      applicationAnnotation.eventBus();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private TypeElement getApplicationLoaderTypeElement(Application applicationAnnotation) {
    try {
      applicationAnnotation.loader();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
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

    public ApplicationAnnotationHandler build() {
      return new ApplicationAnnotationHandler(this);
    }

  }
}
