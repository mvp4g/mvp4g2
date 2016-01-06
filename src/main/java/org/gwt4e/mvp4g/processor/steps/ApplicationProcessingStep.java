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

package org.gwt4e.mvp4g.processor.steps;

import com.google.auto.common.BasicAnnotationProcessor.ProcessingStep;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import org.gwt4e.mvp4g.client.annotations.Application;
import org.gwt4e.mvp4g.client.annotations.Module;
import org.gwt4e.mvp4g.processor.ProcessorContext;
import org.gwt4e.mvp4g.processor.context.ApplicationContext;
import org.gwt4e.mvp4g.processor.writers.ApplicationWriter;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

public class ApplicationProcessingStep
  extends AbstractProcessStep
  implements ProcessingStep {

  public ApplicationProcessingStep(Messager messager,
                                   Filer filer,
                                   Types types,
                                   Elements elements,
                                   ProcessorContext processorContext) {
    super(types,
          messager,
          filer,
          elements,
          processorContext);
  }

  public Set<? extends Class<? extends Annotation>> annotations() {
    return ImmutableSet.of(Module.class,
                           Application.class);
  }

  public void process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    // at least there should only one Application annotation!
    if (elementsByAnnotation.get(Application.class)
                            .size() > 1) {
      createErrorMessage("Error generating Application. There should be at least only one interface, that is annotated with @Application.");
      return;
    }

    for (Element element : elementsByAnnotation.get(Application.class)) {
      ApplicationContext applicationContext = ApplicationContext.create(messager,
                                                                        types,
                                                                        elements,
                                                                        element);
      if (applicationContext == null) {
        return; // error message already emitted
      }
      this.processorContext.setApplicationContext(applicationContext);
      try {
        ApplicationWriter writer = ApplicationWriter.builder()
                                                    .messenger(super.messager)
                                                    .context(super.processorContext)
                                                    .elements(super.elements)
                                                    .filer(super.filer)
                                                    .types(super.types)
                                                    .applicationContext(applicationContext)
                                                    .build();
        writer.write();
      } catch (IOException ioe) {
        createErrorMessage("Error generating source file for type " + applicationContext.getInterfaceType()
                                                                                        .getQualifiedName(),
                           ioe);
      }
    }
  }
}
