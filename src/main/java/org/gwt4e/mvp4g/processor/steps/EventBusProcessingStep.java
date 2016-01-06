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
import com.google.common.collect.SetMultimap;
import com.squareup.javapoet.ClassName;
import org.gwt4e.mvp4g.client.annotations.EventBus;
import org.gwt4e.mvp4g.processor.ProcessorContext;
import org.gwt4e.mvp4g.processor.context.EventBusContext;
import org.gwt4e.mvp4g.processor.writers.EventBusWriter;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

public class EventBusProcessingStep
  extends AbstractProcessStep
  implements ProcessingStep {

  public EventBusProcessingStep(Messager messager,
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
    return Collections.singleton(EventBus.class);
  }

  public void process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    for (Element element : elementsByAnnotation.get(EventBus.class)) {
      EventBusContext eventBusContext = EventBusContext.create(messager,
                                                               types,
                                                               elements,
                                                               element);

      if (eventBusContext == null) {
        return; // error message already emitted
      }
      if (processorContext.getEventContextMap() == null ||
          processorContext.getEventContextMap()
                          .size() == 0 ||
          processorContext.getEventContextMap()
                          .get(eventBusContext.getClassName())
                          .size() == 0) {
        messager.printMessage(Diagnostic.Kind.WARNING,
                              String.format("%s has no events defined",
                                            eventBusContext.getClassName()));
      }

      this.processorContext.getEventBusContextMap()
                           .put((ClassName.get((TypeElement) element)
                                          .toString()
                                ),
                                eventBusContext);
      try {
        EventBusWriter writer = EventBusWriter.builder()
                                              .messenger(super.messager)
                                              .context(super.processorContext)
                                              .elements(super.elements)
                                              .filer(super.filer)
                                              .types(super.types)
                                              .eventBusContext(eventBusContext)
                                              .build();
        writer.write();
      } catch (IOException ioe) {
        createErrorMessage("Error generating source file for type " + eventBusContext.getInterfaceType()
                                                                             .getQualifiedName(),
                           ioe);
      }
    }
  }
}
