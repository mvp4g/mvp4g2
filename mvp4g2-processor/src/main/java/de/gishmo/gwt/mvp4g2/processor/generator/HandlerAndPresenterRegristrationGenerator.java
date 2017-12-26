/*
 * Copyright 2015-2017 Frank Hossfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.gishmo.gwt.mvp4g2.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.HandlerMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.intern.ClassNameModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>The execution context manages all commands.<br>
 * Use run()-method to start execution.</p>
 */
// TODO validation
public class HandlerAndPresenterRegristrationGenerator {

  private ProcessorUtils processorUtils;

  private ProcessingEnvironment processingEnvironment;
  private TypeSpec.Builder      typeSpec;
  private EventBusMetaModel     eventBusMetaModel;
  private HandlerMetaModel      handlerMetaModel;
  private PresenterMetaModel    presenterMetaModel;

  @SuppressWarnings("unused")
  private HandlerAndPresenterRegristrationGenerator() {
  }

  private HandlerAndPresenterRegristrationGenerator(Builder builder) {
    this.processingEnvironment = builder.processingEnvironment;
    this.typeSpec = builder.typeSpec;
    this.eventBusMetaModel = builder.eventBusMetaModel;
    this.handlerMetaModel = builder.handlerMetaModel;
    this.presenterMetaModel = builder.presenterMetaModel;

    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate()
    throws ProcessorException {
    MethodSpec.Builder loadEventHandlerMethod = MethodSpec.methodBuilder("loadEventHandlerMetaData")
                                                          .addAnnotation(Override.class)
                                                          .addModifiers(Modifier.PROTECTED);
    // List of already created EventHandler used to avoid a second create ...
    List<ClassNameModel> listOfEventHandlersToCreate = this.createListOfEventHandlersToCreate();
    listOfEventHandlersToCreate.forEach(handlerClassName -> this.addHandlerToMetaList(loadEventHandlerMethod,
                                                                                      handlerClassName));
    // search for events (@EventHandler) and add the handler to the handler list of the EventMetaData-class
    loadEventHandlerMethod.addComment("");
    loadEventHandlerMethod.addComment("===> add the handler to the handler list of the EventMetaData-class");
    this.handlerMetaModel.getHandlerKeys()
                         .stream()
                         .filter(s -> !listOfEventHandlersToCreate.contains(s))
                         .map(s -> this.handlerMetaModel.getHandlerData(s))
                         .forEach(handlerData -> {
                           handlerData.getHandledEvents()
                                      .stream()
                                      .map(handledEvent -> this.eventBusMetaModel.getEventMetaModel(handledEvent))
                                      .filter(Objects::nonNull)
                                      .filter(eventMetaModel -> !eventMetaModel.getHandlers()
                                                                               .contains(handlerData.getHandler()))
                                      .forEach(eventMetaModel -> loadEventHandlerMethod.addStatement("super.getEventMetaData($S).addHandler($S)",
                                                                                                     eventMetaModel.getEventInternalName(),
                                                                                                     handlerData.getHandler()
                                                                                                                .getClassName()));
                         });
    this.presenterMetaModel.getPresenterKeys()
                           .stream()
                           .filter(s -> !listOfEventHandlersToCreate.contains(s))
                           .map(s -> this.presenterMetaModel.getPresenterData(s))
                           .forEach(presenterData -> {
                             presenterData.getHandledEvents()
                                          .stream()
                                          .map(handledEvent -> this.eventBusMetaModel.getEventMetaModel(handledEvent))
                                          .filter(Objects::nonNull)
                                          .filter(eventMetaModel -> !eventMetaModel.getHandlers()
                                                                                   .contains(presenterData.getPresenter()))
                                          .forEach(eventMetaModel -> loadEventHandlerMethod.addStatement("super.getEventMetaData($S).addHandler($S)",
                                                                                                         eventMetaModel.getEventInternalName(),
                                                                                                         presenterData.getPresenter()
                                                                                                                      .getClassName()));
                           });
    typeSpec.addMethod(loadEventHandlerMethod.build());
  }


  private List<ClassNameModel> createListOfEventHandlersToCreate() {
    // List of already created EventHandler used to avoid a second create ...
    List<ClassNameModel> listOfHandlersToCreate = new ArrayList<>();
    // add the ShellPresenter to the list!
    if (this.eventBusMetaModel.getShell() != null) {
      listOfHandlersToCreate.add(eventBusMetaModel.getShell());
    }
    this.eventBusMetaModel.getEventMetaModels()
                          .stream()
                          .forEach(eventMetaModel -> {
                            eventMetaModel.getBindings()
                                          .stream()
                                          .filter(handlerClassName -> !listOfHandlersToCreate.contains(handlerClassName))
                                          .forEach(listOfHandlersToCreate::add);
                          });
    this.eventBusMetaModel.getEventMetaModels()
                          .stream()
                          .forEach(eventMetaModel -> {
                            eventMetaModel.getHandlers()
                                          .stream()
                                          .filter(handlerClassName -> !listOfHandlersToCreate.contains(handlerClassName))
                                          .forEach(listOfHandlersToCreate::add);
                          });
    this.handlerMetaModel.getHandlerKeys()
                         .stream()
                         .filter(handlerClassName -> !listOfHandlersToCreate.contains(new ClassNameModel(handlerClassName)))
                         .forEach(handlerClassName -> listOfHandlersToCreate.add(new ClassNameModel(handlerClassName)));
    this.presenterMetaModel.getPresenterKeys()
                           .stream()
                           .filter(handlerClassName -> !listOfHandlersToCreate.contains(new ClassNameModel(handlerClassName)))
                           .forEach(handlerClassName -> listOfHandlersToCreate.add(new ClassNameModel(handlerClassName)));
    return listOfHandlersToCreate;
  }

  private void addHandlerToMetaList(MethodSpec.Builder methodToGenerate,
                                    ClassNameModel handlerClassName) {
    // check if we deal with a presenter
    boolean isPresenter = this.presenterMetaModel.isPresenter(handlerClassName.getClassName());
    // Name of the variable , class name
    String metaDataVariableName = this.processorUtils.createFullClassName(handlerClassName.getClassName() + ProcessorConstants.META_DATA);
    String metaDataClassName = this.processorUtils.setFirstCharacterToUpperCase(metaDataVariableName);
    // comment
    methodToGenerate.addComment("");
    methodToGenerate.addComment("===> ");
    methodToGenerate.addComment("handle $N ($N)",
                                handlerClassName.getClassName(),
                                isPresenter ? "Presenter" : "EventHandler");
    methodToGenerate.addComment("");
    // code ...
    methodToGenerate.addStatement("$T $N = new $T()",
                                  ClassName.get(handlerClassName.getPackage(),
                                                metaDataClassName),
                                  metaDataVariableName,
                                  ClassName.get(handlerClassName.getPackage(),
                                                metaDataClassName));
    if (isPresenter) {
      if (!this.presenterMetaModel.getPresenterData(handlerClassName.getClassName())
                                  .isMultiple()) {
//                                   // check, that multiple is false! (We can do this not here during code generation, because we don't know it ...)
//                                   methodToGenerate.beginControlFlow("if (!$N.isMultiple())",
//                                                                     metaDataVariableName);
        this.generatePresenterBinding(methodToGenerate,
                                      handlerClassName.getClassName(),
                                      metaDataVariableName);
      }
//      methodToGenerate.endControlFlow();
    } else {
      methodToGenerate.addStatement("super.putHandlerMetaData($S, $N)",
                                    handlerClassName.getClassName(),
                                    metaDataVariableName);
      // set eventbus statement
      methodToGenerate.addStatement("$N.getHandler().setEventBus(this)",
                                    metaDataVariableName);
    }
  }

  private void generatePresenterBinding(MethodSpec.Builder methodToGenerate,
                                        String eventHandlerClassName,
                                        String metaDataVariableName) {
    methodToGenerate.addStatement("super.putPresenterMetaData($S, $N)",
                                  eventHandlerClassName,
                                  metaDataVariableName);
    // set eventbus statement
    methodToGenerate.addStatement("$N.getPresenter().setEventBus(this)",
                                  metaDataVariableName);
    // set view statement in presenter
    methodToGenerate.addStatement("$N.getPresenter().setView($N.getView())",
                                  metaDataVariableName,
                                  metaDataVariableName);
    // set presenter statement in view
    methodToGenerate.addStatement("$N.getView().setPresenter($N.getPresenter())",
                                  metaDataVariableName,
                                  metaDataVariableName);
  }

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;
    TypeSpec.Builder      typeSpec;
    EventBusMetaModel     eventBusMetaModel;
    HandlerMetaModel      handlerMetaModel;
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

    public Builder eventBusMetaModel(EventBusMetaModel eventBusMetaModel) {
      this.eventBusMetaModel = eventBusMetaModel;
      return this;
    }

    public Builder handlerMetaModel(HandlerMetaModel handlerMetaModel) {
      this.handlerMetaModel = handlerMetaModel;
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

    public HandlerAndPresenterRegristrationGenerator build() {
      return new HandlerAndPresenterRegristrationGenerator(this);
    }
  }
}
