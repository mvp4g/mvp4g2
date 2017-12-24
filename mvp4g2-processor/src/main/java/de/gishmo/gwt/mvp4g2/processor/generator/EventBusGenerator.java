package de.gishmo.gwt.mvp4g2.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.internal.eventbus.AbstractEventBus;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.HandlerMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.HistoryMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterMetaModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;

public class EventBusGenerator {

  private final static String IMPL_NAME = "Impl";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private TypeElement           eventBusTypeElement;

  @SuppressWarnings("unused")
  private EventBusGenerator(Builder builder) {
    super();
    this.processingEnvironment = builder.processingEnvironment;
    this.eventBusTypeElement = builder.eventBusTypeElement;
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

  public void generate(EventBusMetaModel eventBusMetaMetaModel,
                       HandlerMetaModel handlerMetaModel,
                       PresenterMetaModel presenterMetaModel,
                       HistoryMetaModel historyMetaModel)
    throws ProcessorException {
    // check if element is existing (to avoid generating code for deleted items)
    if (!this.processorUtils.doesExist(eventBusMetaMetaModel.getEventBus())) {
      return;
    }

    ClassName abstractEventBusClassName = ClassName.get(AbstractEventBus.class);
    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(eventBusMetaMetaModel.getEventBus()
                                                                           .getSimpleName() + EventBusGenerator.IMPL_NAME)
                                        .superclass(ParameterizedTypeName.get(abstractEventBusClassName,
                                                                              eventBusMetaMetaModel.getEventBus()
                                                                                                   .getTypeName()))
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL)
                                        .addSuperinterface(eventBusMetaMetaModel.getEventBus()
                                                                                .getTypeName());

    MethodSpec constructor = MethodSpec.constructorBuilder()
                                       .addModifiers(Modifier.PUBLIC)
                                       .addStatement("super($S, $L)",
                                                     eventBusMetaMetaModel.getShell()
                                                                          .getClassName(),
                                                     eventBusMetaMetaModel.getHistoryOnStart())
                                       .build();
    typeSpec.addMethod(constructor);

    DebugGenerator.builder()
                  .eventBusMetaModel(eventBusMetaMetaModel)
                  .typeSpec(typeSpec)
                  .build()
                  .generate();

    FilterGenerator.builder()
                   .eventBusMetaModel(eventBusMetaMetaModel)
                   .typeSpec(typeSpec)
                   .build()
                   .generate();

    EventLoadMetaDataGenerator.builder()
                              .processingEnvironment(this.processingEnvironment)
                              .eventBusMetaModel(eventBusMetaMetaModel)
                              .typeSpec(typeSpec)
                              .build()
                              .generate();

    EventMetaDataGenerator.builder()
                          .processingEnvironment(this.processingEnvironment)
                          .eventBusMetaModel(eventBusMetaMetaModel)
                          .build()
                          .generate();

    EventHandlingMethodGenerator.builder()
                                .typeSpec(typeSpec)
                                .eventBusMetaModel(eventBusMetaMetaModel)
                                .historyMetaModel(historyMetaModel)
                                .presenterMetaModel(presenterMetaModel)
                                .handlerMetaModel(handlerMetaModel)
                                .build()
                                .generate();

    AddPresenterGenerator.builder()
                         .typeSpec(typeSpec)
                         .build()
                         .generate();

    HandlerAndPresenterRegristrationGenerator.builder()
                                             .processingEnvironment(this.processingEnvironment)
                                             .typeSpec(typeSpec)
                                             .eventBusMetaModel(eventBusMetaMetaModel)
                                             .presenterMetaModel(presenterMetaModel)
                                             .handlerMetaModel(handlerMetaModel)
                                             .build()
                                             .generate();

    JavaFile javaFile = JavaFile.builder(eventBusMetaMetaModel.getEventBus()
                                                              .getPackage(),
                                         typeSpec.build())
                                .build();
    System.out.println(javaFile.toString());
    try {
      javaFile.writeTo(this.processingEnvironment.getFiler());
    } catch (IOException e) {
      throw new ProcessorException("Unable to write generated file: >>" + eventBusMetaMetaModel.getEventBus()
                                                                                               .getSimpleName() + EventBusGenerator.IMPL_NAME + "<< -> exception: " + e.getMessage());
    }
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;
    TypeElement           eventBusTypeElement;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public Builder eventBusTypeElement(TypeElement eventBusTypeElement) {
      this.eventBusTypeElement = eventBusTypeElement;
      return this;
    }

    public EventBusGenerator build() {
      return new EventBusGenerator(this);
    }
  }
}
