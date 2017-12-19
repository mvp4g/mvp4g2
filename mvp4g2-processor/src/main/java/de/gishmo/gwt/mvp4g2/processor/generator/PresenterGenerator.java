package de.gishmo.gwt.mvp4g2.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.internal.ui.EventHandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.internal.ui.PresenterMetaData;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterMetaModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.io.IOException;

public class PresenterGenerator {

  private final static String IMPL_NAME = "MetaData";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private PresenterGenerator(Builder builder) {
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

  public void generate(PresenterMetaModel metaModel)
    throws ProcessorException {
    ClassName presenterrMetaDataClassName = ClassName.get(PresenterMetaData.class);
    ClassName presenterMetaDataKindClassName = ClassName.get(EventHandlerMetaData.Kind.class);
    ClassName presenterViewCreationMethodClassName = ClassName.get(Presenter.VIEW_CREATION_METHOD.class);

    for (String presenter : metaModel.getPresenterKeys()) {
      PresenterMetaModel.PresenterData data = metaModel.getPresenterData(presenter);

      ClassName presenterClassName = ClassName.get(data.getPresenter()
                                                       .getPackage(),
                                                   data.getPresenter()
                                                       .getSimpleName());
      ClassName viewClasseName = ClassName.get(data.getViewClass()
                                                      .getPackage(),
                                                  data.getViewClass()
                                                      .getSimpleName());
      ClassName viewInterfaceName = ClassName.get(data.getViewInterface()
                                                      .getPackage(),
                                                  data.getViewInterface()
                                                      .getSimpleName());
      String className = this.processorUtils.createFullClassName(data.getPresenter()
                                                                     .getPackage(),
                                                                 data.getPresenter()
                                                                     .getSimpleName() + PresenterGenerator.IMPL_NAME);
      TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.processorUtils.setFirstCharacterToUpperCase(className))
                                          .superclass(ParameterizedTypeName.get(presenterrMetaDataClassName,
                                                                                presenterClassName,
                                                                                viewInterfaceName))
                                          .addModifiers(Modifier.PUBLIC,
                                                        Modifier.FINAL);
      // constructor ...
      MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .addStatement("super($S, $T.PRESENTER, $N, $T.$L)",
                                                               data.getPresenter().getClassName(),
                                                               presenterMetaDataKindClassName,
                                                               data.getIsMultiple(),
                                                               presenterViewCreationMethodClassName,
                                                               data.getViewCreationMethod());
      constructor.addStatement("super.presenter = new $T()",
                               presenterClassName);
      if (Presenter.VIEW_CREATION_METHOD.FRAMEWORK.toString().equals(data.getViewCreationMethod())) {
        constructor.addStatement("super.view = ($T) new $T()",
                                 viewInterfaceName,
                                 viewClasseName);
      } else {
        constructor.addStatement("super.view = presenter.createView()");
      }
      typeSpec.addMethod(constructor.build());

      JavaFile javaFile = JavaFile.builder(data.getPresenter()
                                               .getPackage(),
                                           typeSpec.build())
                                  .build();
      try {
        javaFile.writeTo(this.processingEnvironment.getFiler());
      } catch (IOException e) {
        throw new ProcessorException("Unable to write generated file: >>" + data.getPresenter()
                                                                                .getSimpleName() + PresenterGenerator.IMPL_NAME + "<< -> exception: " + e.getMessage());
      }
    }
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public PresenterGenerator build() {
      return new PresenterGenerator(this);
    }
  }
}
