package de.gishmo.gwt.mvp4g2.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.application.IsApplicationLoader;
import de.gishmo.gwt.mvp4g2.client.internal.application.AbstractApplication;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.model.ApplicationMetaModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.io.IOException;

public class ApplicationGenerator {

  private final static String IMPL_NAME = "Impl";

  private final ClassName abstractApplicationClassName = ClassName.get(AbstractApplication.class);

  //  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private ApplicationGenerator(Builder builder) {
    super();

    this.processingEnvironment = builder.processingEnvironment;

    setUp();
  }

  private void setUp() {
//    this.processorUtils = ProcessorUtils.builder()
//                                        .processingEnvironment(this.processingEnvironment)
//                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate(ApplicationMetaModel metaModel)
    throws ProcessorException {
    ClassName applicationClassName = ClassName.get(metaModel.getApplication()
                                                            .getPackage(),
                                                   metaModel.getApplication()
                                                            .getSimpleName());
    ClassName eventBusClassName = ClassName.get(metaModel.getEventBus()
                                                         .getPackage(),
                                                metaModel.getEventBus()
                                                         .getSimpleName());
    ClassName applicaitonLoaderClassName = ClassName.get(metaModel.getLaoder()
                                                                  .getPackage(),
                                                         metaModel.getLaoder()
                                                                  .getSimpleName());

    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(metaModel.getApplication()
                                                               .getSimpleName() + ApplicationGenerator.IMPL_NAME)
                                        .superclass(ParameterizedTypeName.get(this.abstractApplicationClassName,
                                                                              eventBusClassName))
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL)
                                        .addSuperinterface(applicationClassName);

    // constructor ...
    MethodSpec constructor = MethodSpec.constructorBuilder()
                                       .addModifiers(Modifier.PUBLIC)
                                       .addStatement("super()")
                                       .addStatement("super.eventBus = new $N.$N()",
                                                     metaModel.getEventBus()
                                                              .getPackage(),
                                                     metaModel.getEventBus()
                                                              .getSimpleName() + ApplicationGenerator.IMPL_NAME)
                                       .build();
    typeSpec.addMethod(constructor);

    // method "getApplicaitonLoader"
    MethodSpec getApplicaitonLaoderMethod = MethodSpec.methodBuilder("getApplicationLoader")
                                                      .addModifiers(Modifier.PUBLIC)
                                                      .addAnnotation(Override.class)
                                                      .returns(IsApplicationLoader.class)
                                                      .addStatement("return new $T()",
                                                                    applicaitonLoaderClassName)
                                                      .build();
    typeSpec.addMethod(getApplicaitonLaoderMethod);


    JavaFile javaFile = JavaFile.builder(metaModel.getEventBus()
                                                  .getPackage(),
                                         typeSpec.build())
                                .build();
    try {
      javaFile.writeTo(this.processingEnvironment.getFiler());
    } catch (IOException e) {
      throw new ProcessorException("Unable to write generated file: >>" + metaModel.getEventBus()
                                                                                   .getSimpleName() + ApplicationGenerator.IMPL_NAME + "<< -> exception: " + e.getMessage());
    }
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public ApplicationGenerator build() {
      return new ApplicationGenerator(this);
    }
  }
}
