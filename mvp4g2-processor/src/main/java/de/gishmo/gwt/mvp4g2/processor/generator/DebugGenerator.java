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

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;

import javax.lang.model.element.Modifier;

public class DebugGenerator {

  private EventBusMetaModel eventBusMetaModel;
  private TypeSpec.Builder  typeSpec;

  @SuppressWarnings("unused")
  private DebugGenerator() {
  }

  private DebugGenerator(Builder builder) {
    this.eventBusMetaModel = builder.eventBusMetaModel;
    this.typeSpec = builder.typeSpec;
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate()
    throws ProcessorException {
    // method must always be created!
    MethodSpec.Builder loadDebugConfigurationMethod = MethodSpec.methodBuilder("loadDebugConfiguration")
                                                                .addAnnotation(Override.class)
                                                                .addModifiers(Modifier.PUBLIC);
    if (eventBusMetaModel.hasDebugAnnotation()) {
      loadDebugConfigurationMethod.addStatement("super.setDebugEnable(true)")
                                  .addStatement("super.setLogger(new $T())",
                                                eventBusMetaModel.getDebugLogger())
                                  .addStatement("super.setLogLevel($T.LogLevel.$L)",
                                                Debug.class,
                                                eventBusMetaModel.getDebugLogLevel());
    } else {
      loadDebugConfigurationMethod.addStatement("super.setDebugEnable(false)");
    }
    typeSpec.addMethod(loadDebugConfigurationMethod.build());
  }

  public static final class Builder {

    EventBusMetaModel eventBusMetaModel;
    TypeSpec.Builder  typeSpec;

    /**
     * Set the EventBusMetaModel of the currently generated eventBus
     *
     * @param eventBusMetaModel meta data model of the eventbus
     * @return the Builder
     */
    public Builder eventBusMetaModel(EventBusMetaModel eventBusMetaModel) {
      this.eventBusMetaModel = eventBusMetaModel;
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

    public DebugGenerator build() {
      return new DebugGenerator(this);
    }
  }
}
