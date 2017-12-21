package de.gishmo.gwt.mvp4g2.processor.generator;

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import de.gishmo.gwt.mvp4g2.client.internal.eventbus.AbstractEventBus;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.EventHandlerMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.HistoryMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterMetaModel;

public class EventBusGenerator {

  private final static String IMPL_NAME = "Impl";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private EventBusGenerator(Builder builder) {
    super();

    this.processingEnvironment = builder.processingEnvironment;

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
                       EventHandlerMetaModel eventHandlerMetaModel,
                       PresenterMetaModel presenterMetaModel,
                       HistoryMetaModel historyMetaModel)
    throws ProcessorException {
    // check if element is existing (to avoid generating code for deleted items)
    if (!this.processorUtils.doesExist(eventBusMetaMetaModel.getEventBus())) {
      return;
    }

    ClassName abstractEventBusClassName = ClassName.get(AbstractEventBus.class);
    ClassName eventBusClassName = ClassName.get(eventBusMetaMetaModel.getEventBus()
                                                                     .getPackage(),
                                                eventBusMetaMetaModel.getEventBus()
                                                                     .getSimpleName());
    ClassName shellClassName = ClassName.get(eventBusMetaMetaModel.getShell()
                                                                  .getPackage(),
                                             eventBusMetaMetaModel.getShell()
                                                                  .getSimpleName());

    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(eventBusMetaMetaModel.getEventBus()
                                                                           .getSimpleName() + EventBusGenerator.IMPL_NAME)
                                        .superclass(ParameterizedTypeName.get(abstractEventBusClassName,
                                                                              eventBusClassName))
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL)
                                        .addSuperinterface(eventBusClassName);

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

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public EventBusGenerator build() {
      return new EventBusGenerator(this);
    }
  }
}
