package de.gishmo.gwt.mvp4g2.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.internal.ui.EventHandlerMetaData;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventHandlerMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.intern.ClassNameModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.io.IOException;

public class HandlerGenerator {

  private final static String IMPL_NAME = "MetaData";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private HandlerGenerator(Builder builder) {
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

  public void generate(EventHandlerMetaModel metaModel)
    throws ProcessorException {
    ClassName eventHandlerMetaDataClassName = ClassName.get(EventHandlerMetaData.class);
    ClassName eventHandlerMetaDataKindClassName = ClassName.get(EventHandlerMetaData.Kind.class);

    for (String eventHandler : metaModel.getEventHandlerKeys()) {
      // check if element is existing (to avoid generating code for deleted items)
      if (this.processorUtils.doesExist(new ClassNameModel(eventHandler))) {
        EventHandlerMetaModel.EventHandlerData data = metaModel.getEventHandlerData(eventHandler);

        ClassName eventHandlerClassName = ClassName.get(data.getEventHandler()
                                                            .getPackage(),
                                                        data.getEventHandler()
                                                            .getSimpleName());

        String className = this.processorUtils.createFullClassName(data.getEventHandler()
                                                                       .getPackage(),
                                                                   data.getEventHandler()
                                                                       .getSimpleName() + HandlerGenerator.IMPL_NAME);
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.processorUtils.setFirstCharacterToUpperCase(className))
                                            .superclass(ParameterizedTypeName.get(eventHandlerMetaDataClassName,
                                                                                  eventHandlerClassName))
                                            .addModifiers(Modifier.PUBLIC,
                                                          Modifier.FINAL);
        // constructor ...
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                                   .addModifiers(Modifier.PUBLIC)
                                                   .addStatement("super($S, $T.EVENT_HANDLER, new $T())",
                                                                 data.getEventHandler()
                                                                     .getClassName(),
                                                                 eventHandlerMetaDataKindClassName,
                                                                 eventHandlerClassName);
        typeSpec.addMethod(constructor.build());

        JavaFile javaFile = JavaFile.builder(data.getEventHandler()
                                                 .getPackage(),
                                             typeSpec.build())
                                    .build();
        try {
          javaFile.writeTo(this.processingEnvironment.getFiler());
        } catch (IOException e) {
          throw new ProcessorException("Unable to write generated file: >>" + data.getEventHandler()
                                                                                  .getSimpleName() + HandlerGenerator.IMPL_NAME + "<< -> exception: " + e.getMessage());
        }
      }}
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public HandlerGenerator build() {
      return new HandlerGenerator(this);
    }
  }
}
