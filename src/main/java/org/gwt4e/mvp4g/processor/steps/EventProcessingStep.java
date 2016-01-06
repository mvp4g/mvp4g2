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
import org.gwt4e.mvp4g.client.annotations.Event;
import org.gwt4e.mvp4g.processor.ProcessorContext;
import org.gwt4e.mvp4g.processor.Utils;
import org.gwt4e.mvp4g.processor.context.EventContext;
import org.gwt4e.mvp4g.processor.writers.EventWriter;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class EventProcessingStep
  extends AbstractProcessStep
  implements ProcessingStep {

  public EventProcessingStep(Messager messager,
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
    return Collections.singleton(Event.class);
  }

  public void process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    for (Element element : elementsByAnnotation.get(Event.class)) {
      String eventBusName = Utils.findEnclosingTypeElement(element)
                                 .getQualifiedName()
                                 .toString();

      // check if an event with the same name already exits!
      Set<String> eventBusNames = processorContext.getEventContextMap()
                                                  .keySet();
      for (String ebName : eventBusNames) {
        Set<String> eventNames = ((Map<String, EventContext>) processorContext.getEventContextMap()
                                                                              .get(ebName)).keySet();
        for (String eName : eventNames) {
          if (element.getSimpleName().toString().equals(eName)) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                                  String.format("Event-name: %s is already used. Please choose another name. (It is not possible to work with same event-name and different signatures.",
                                                element.getSimpleName().toString()));
            return;
          }
        }
      }

      // create context
      EventContext eventContext = EventContext.create(messager,
                                                      types,
                                                      elements,
                                                      element);
      // save eventContext
      this.processorContext.put(eventBusName,
                                element.getSimpleName()
                                       .toString(),
                                eventContext);
      System.out.println("Processing Mvp4gInternalEvent: " + element.getSimpleName());
      if (eventContext == null) {
        return; // error message already emitted
      }
      try {
        EventWriter writer = EventWriter.builder()
                                           .messenger(super.messager)
                                           .context(super.processorContext)
                                           .elements(super.elements)
                                           .filer(super.filer)
                                           .types(super.types)
                                           .eventContext(eventContext)
                                           .build();
        writer.write();
      } catch (IOException ioe) {
        createErrorMessage("Error generating source file for type " + eventContext.getEventClassName(),
                           ioe);
      }
    }
  }
}
