/*
 * Copyright (C) 2016 Frank Hossfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gwt4e.mvp4g.processor.writers;

import com.squareup.javapoet.*;
import org.gwt4e.event.shared.Mvp4gInternalSimpleEventBus;
import org.gwt4e.mvp4g.client.module.AbstractMvp4gModule;
import org.gwt4e.mvp4g.processor.ProcessorContext;
import org.gwt4e.mvp4g.processor.context.ModuleContext;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;

/**
 * Created by hoss on 05.01.16.
 */
public class ModuleWriter
  extends AbstractWriter
  implements Writer {

  private ModuleContext moduleContext;

  private ModuleWriter(Builder builder) {
    super(Builder.types,
          Builder.messager,
          Builder.filer,
          Builder.elements,
          Builder.processorContext);

    this.moduleContext = Builder.moduleContext;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public void write()
    throws IOException {


    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(moduleContext.getImplName())
                                        .addOriginatingElement(moduleContext.getInterfaceType())
                                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                        .superclass(AbstractMvp4gModule.class);

    TypeElement moduleEventBusElement = (TypeElement) ((DeclaredType) moduleContext.getModuleEventBus()).asElement();
    FieldSpec moduleEventBusField = FieldSpec.builder(TypeName.get(moduleEventBusElement.asType()),
                                                      "eventBusModule")
                                             .addModifiers(Modifier.PRIVATE)
                                             .build();
    typeSpec.addField(moduleEventBusField);

    ParameterSpec eventBusParameter = ParameterSpec.builder(Mvp4gInternalSimpleEventBus.class,
                                                            "eventBus")
                                                   .build();
    ClassName classNameEventBusImpl = ClassName.get(moduleContext.getPackageName() + ".generated",
                                                    moduleEventBusElement.getSimpleName()
                                                                         .toString() + "Impl");
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                               .addModifiers(Modifier.PUBLIC)
                                               .addParameter(eventBusParameter)
                                               .addStatement("super($N)",
                                                             eventBusParameter)
                                               .addStatement("$N = new $T($S, getInternalEventBus())",
                                                             moduleEventBusField,
                                                             classNameEventBusImpl,
                                                             moduleEventBusElement.getQualifiedName().toString());
    typeSpec.addMethod(constructor.build());

    MethodSpec getEventBusMethod = MethodSpec.methodBuilder("getModuleEventBus")
                                             .addModifiers(Modifier.PUBLIC)
                                             .returns(moduleEventBusField.type)
                                             .addStatement("return $N",
                                                           moduleEventBusField)
                                             .build();
    typeSpec.addMethod(getEventBusMethod);

    JavaFile.builder(moduleContext.getPackageName() + ".generated",
                     typeSpec.build())
            .build()
            .writeTo(filer);

    System.out.println(JavaFile.builder(moduleContext.getPackageName() + ".generated",
                                        typeSpec.build())
                               .build()
                               .toString());
  }

  public final static class Builder {
    static Types            types;
    static Messager         messager;
    static Filer            filer;
    static Elements         elements;
    static ProcessorContext processorContext;
    static ModuleContext    moduleContext;

    public Builder types(Types value) {
      types = value;
      return this;
    }

    public Builder messenger(Messager value) {
      messager = value;
      return this;
    }

    public Builder filer(Filer value) {
      filer = value;
      return this;
    }

    public Builder elements(Elements value) {
      elements = value;
      return this;
    }

    public Builder context(ProcessorContext value) {
      processorContext = value;
      return this;
    }

    public Builder moduleContext(ModuleContext value) {
      moduleContext = value;
      return this;
    }

    public ModuleWriter build() {
      return new ModuleWriter(this);
    }
  }
}
