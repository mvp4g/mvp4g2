/*
 * Copyright (c) 2018 - Frank Hossfeld
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 *
 */
package com.github.mvp4g.mvp4g2.processor.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.github.mvp4g.mvp4g2.core.eventbus.PresenterRegistration;
import com.github.mvp4g.mvp4g2.core.internal.Mvp4g2RuntimeException;
import com.github.mvp4g.mvp4g2.core.internal.eventbus.AbstractEventBus;
import com.github.mvp4g.mvp4g2.core.internal.ui.PresenterMetaDataRegistration;
import com.github.mvp4g.mvp4g2.core.ui.IsPresenter;
import com.github.mvp4g.mvp4g2.core.ui.annotation.Presenter;
import com.github.mvp4g.mvp4g2.processor.ProcessorConstants;
import com.github.mvp4g.mvp4g2.processor.ProcessorUtils;
import com.github.mvp4g.mvp4g2.processor.model.PresenterMetaModel;

public class AddPresenterGenerator {

  private ProcessorUtils processorUtils;

  private ProcessingEnvironment processingEnvironment;
  private TypeSpec.Builder      typeSpec;
  private PresenterMetaModel    presenterMetaModel;

  @SuppressWarnings("unused")
  private AddPresenterGenerator() {
  }

  private AddPresenterGenerator(Builder builder) {
    this.processingEnvironment = builder.processingEnvironment;
    this.typeSpec = builder.typeSpec;
    this.presenterMetaModel = builder.presenterMetaModel;

    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate() {
    MethodSpec.Builder addHandlerMethod = MethodSpec.methodBuilder("addHandler")
                                                    .addAnnotation(Override.class)
                                                    .addModifiers(Modifier.PUBLIC)
                                                    .addParameter(ParameterizedTypeName.get(ClassName.get(IsPresenter.class),
                                                                                            WildcardTypeName.subtypeOf(Object.class),
                                                                                            WildcardTypeName.subtypeOf(Object.class)),
                                                                  "presenter")
                                                    .addParameter(TypeName.BOOLEAN,
                                                                  "bind")
                                                    .returns(ClassName.get(PresenterRegistration.class))
                                                    .addException(Mvp4g2RuntimeException.class);
    // search presenters (multiple = true)
    List<PresenterMetaModel.PresenterData> multiPresenters = createPresenterTypeMultipleList();
    if (multiPresenters.size() > 0) {
      multiPresenters.forEach(presenterData -> generateCodeBlockForPresenter(addHandlerMethod,
                                                                             presenterData));
    }
    addHandlerMethod.addStatement("throw new $T(presenter.getClass().getCanonicalName() + \": can not be used with the addHandler()-method, because it is not defined as multiple presenter!\")",
                                  Mvp4g2RuntimeException.class);
    typeSpec.addMethod(addHandlerMethod.build());
  }

  private List<PresenterMetaModel.PresenterData> createPresenterTypeMultipleList() {
    return presenterMetaModel.getPresenterDatas()
                             .stream()
                             .filter(PresenterMetaModel.PresenterData::isMultiple)
                             .collect(Collectors.toCollection(ArrayList::new));
  }

  private void generateCodeBlockForPresenter(MethodSpec.Builder addHandlerMethod,
                                             PresenterMetaModel.PresenterData presenterData) {
    addHandlerMethod.beginControlFlow("if (presenter instanceof $T)",
                                      ClassName.get(presenterData.getPresenter()
                                                                 .getPackage(),
                                                    presenterData.getPresenter()
                                                                 .getSimpleName()));
    // Name of the variable , class name
    String metaDataVariableName = this.processorUtils.createFullClassName(presenterData.getPresenter()
                                                                                       .getClassName() + ProcessorConstants.META_DATA);
    String metaDataClassName = this.processorUtils.setFirstCharacterToUpperCase(metaDataVariableName);
    addHandlerMethod.addStatement("super.logAddHandler(++$T.logDepth, presenter.getClass().getCanonicalName(), bind)",
                                  ClassName.get(AbstractEventBus.class));
    // comment
    addHandlerMethod.addComment("");
    addHandlerMethod.addComment("===> ");
    addHandlerMethod.addComment("add $N to eventbus",
                                presenterData.getPresenter()
                                             .getClassName());
    addHandlerMethod.addComment("");
    // create instance of presenter meta data ...
    addHandlerMethod.addStatement("$T $N = new $T()",
                                  ClassName.get(presenterData.getPresenter()
                                                             .getPackage(),
                                                metaDataClassName),
                                  metaDataVariableName,
                                  ClassName.get(presenterData.getPresenter()
                                                             .getPackage(),
                                                metaDataClassName));
    addHandlerMethod.addStatement("final $T metaDataRegistration = super.putPresenterMetaData($S, $N)",
                                  PresenterMetaDataRegistration.class,
                                  presenterData.getPresenter()
                                               .getClassName(),
                                  metaDataVariableName);
    // set presenter
    addHandlerMethod.addStatement("$N.setPresenter(($T) presenter)",
                                  metaDataVariableName,
                                  ClassName.get(presenterData.getPresenter()
                                                             .getPackage(),
                                                presenterData.getPresenter()
                                                             .getSimpleName()));
    // set eventbus
    addHandlerMethod.addStatement("$N.getPresenter().setEventBus(this)",
                                  metaDataVariableName);
    // create view
    if (Presenter.VIEW_CREATION_METHOD.FRAMEWORK.toString()
                                                .equals(presenterData.getViewCreationMethod())) {
      addHandlerMethod.addStatement("$N.setView(new $T())",
                                    metaDataVariableName,
                                    ClassName.get(presenterData.getViewClass()
                                                               .getPackage(),
                                                  presenterData.getViewClass()
                                                               .getSimpleName()));
    } else {
      addHandlerMethod.addStatement("$N.setView(presenter.createView())",
                                    metaDataVariableName);
    }
    // set view in presenter
    addHandlerMethod.addStatement("$N.getPresenter().setView($N.getView())",
                                  metaDataVariableName,
                                  metaDataVariableName);
    // set presenter in view
    addHandlerMethod.addStatement("$N.getView().setPresenter(($T) presenter)",
                                  metaDataVariableName,
                                  ClassName.get(presenterData.getPresenter()
                                                             .getPackage(),
                                                presenterData.getPresenter()
                                                             .getSimpleName()));
    // not sure, if this is the best solution ...
    addHandlerMethod.beginControlFlow("if (bind)");
    addHandlerMethod.addStatement("$N.getPresenter().getView().setBound(true)",
                                  metaDataVariableName);
    addHandlerMethod.addStatement("$N.getPresenter().getView().createView()",
                                  metaDataVariableName);
    addHandlerMethod.addStatement("$N.getPresenter().getView().bind()",
                                  metaDataVariableName);
    addHandlerMethod.endControlFlow();
    // create retun statement ...
    StringBuilder sb = new StringBuilder();
    addHandlerMethod.addStatement(sb.append("return new $T() {")
                                    .append("\n")
                                    .append("@$T")
                                    .append("\n")
                                    .append("  public void remove() {")
                                    .append("\n")
                                    .append("    metaDataRegistration.remove();")
                                    .append("\n")
                                    .append("  }")
                                    .append("\n")
                                    .append("}")
                                    .toString(),
                                  PresenterRegistration.class,
                                  Override.class);
    addHandlerMethod.endControlFlow();
  }

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;
    TypeSpec.Builder      typeSpec;
    PresenterMetaModel    presenterMetaModel;

    /**
     * Set the processing envirement
     *
     * @param processingEnvirement the processing envirement
     * @return the Builder
     */
    public Builder processingEnvironment(ProcessingEnvironment processingEnvirement) {
      this.processingEnvironment = processingEnvirement;
      return this;
    }

    public Builder presenterMetaModel(PresenterMetaModel presenterMetaModel) {
      this.presenterMetaModel = presenterMetaModel;
      return this;
    }

    /**
     * Set the typeSpec of the currently generated eventBus
     *
     * @param typeSpec ttype spec of the crruent eventbus
     * @return the Builder
     */
    public Builder typeSpec(TypeSpec.Builder typeSpec) {
      this.typeSpec = typeSpec;
      return this;
    }

    public AddPresenterGenerator build() {
      return new AddPresenterGenerator(this);
    }
  }
}
