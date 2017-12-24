package de.gishmo.gwt.mvp4g2.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.history.annotation.History;
import de.gishmo.gwt.mvp4g2.client.internal.history.HistoryMetaData;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.HistoryMetaModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.io.IOException;

public class HistoryGenerator {

  private ProcessorUtils processorUtils;

  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private HistoryGenerator(Builder builder) {
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

  public void generate(HistoryMetaModel metaModel)
    throws ProcessorException {
    for (String historyConverterClassName : metaModel.getHistoryConverterClassNames()) {
      HistoryMetaModel.HistoryData historyData = metaModel.getHistoryData(historyConverterClassName);
      TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.processorUtils.createHistoryMetaDataClassName(historyData.getHistoryConverter()
                                                                                                                      .getClassName()))
                                          .superclass(ClassName.get(HistoryMetaData.class))
                                          .addModifiers(Modifier.PUBLIC,
                                                        Modifier.FINAL);
      // constructor ...
      MethodSpec constructor = MethodSpec.constructorBuilder()
                                         .addModifiers(Modifier.PUBLIC)
                                         .addStatement("super($T.$L)",
                                                       ClassName.get(History.HistoryConverterType.class),
                                                       historyData.getHistoryConverterType())
                                         .build();
      typeSpec.addMethod(constructor);

      JavaFile javaFile = JavaFile.builder(historyData.getHistoryConverter()
                                                      .getPackage(),
                                           typeSpec.build())
                                  .build();
      try {
        javaFile.writeTo(this.processingEnvironment.getFiler());
      } catch (IOException e) {
        throw new ProcessorException("Unable to write generated file: >>" + this.processorUtils.createHistoryMetaDataClassName(historyData.getHistoryConverter()
                                                                                                                                          .getClassName()) + "<< -> exception: " + e.getMessage());
      }
    }
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public HistoryGenerator build() {
      return new HistoryGenerator(this);
    }
  }
}
