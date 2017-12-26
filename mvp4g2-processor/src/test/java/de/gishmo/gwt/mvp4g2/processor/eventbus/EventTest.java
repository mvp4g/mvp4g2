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

package de.gishmo.gwt.mvp4g2.processor.eventbus;

import com.google.testing.compile.JavaFileObjects;
import de.gishmo.gwt.mvp4g2.processor.Mvp4g2Processor;
import org.junit.Test;

import javax.tools.JavaFileObject;
import java.util.ArrayList;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static org.truth0.Truth.ASSERT;

public class EventTest {

  @Test
  public void testEventTestHistoryNamesNotUnique() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestHistoryNamesNotUnique/EventTestHistoryNamesNotUnique.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("using a already used historyName");
  }

// nach gelagert gegen die metadaten testen!
//  @Test
//  public void testEventTestHandlerInBindAndHandlersAttribute() {
//    ASSERT.about(javaSource())
//          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestHandlerInBindAndHandlersAttribute/EventTestHandlerInBindAndHandlersAttribute.java"))
//          .processedWith(new Mvp4g2Processor())
//          .failsToCompile()
//          .withErrorContaining("can not be set in bind- and handlers-attribute");
//  }

  @Test
  public void testEventTestHandlerNotInBindAndHandlersAttribute() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {{
            add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/EventTestHandlerNotInBindAndHandlersAttribute.java"));
            add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/MockOneEventHandler.java"));
            add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/MockShellPresenter.java"));
          }})
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event01.java"),
                            JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event02.java"),
                            JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event03.java"),
                            JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockOneEventHandlerMetaData.java"),
                            JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/De_gishmo_gwt_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData.java"),
                            JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/EventTestHandlerNotInBindAndHandlersAttributeImpl.java"));
  }

//  @Test
//  public void testEventTestPasiveEventWithBindAttribute() {
//    ASSERT.about(javaSource())
//          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/eventTestPasiveEventWithBindAttribute/EventTestPasiveEventWithBindAttribute.java"))
//          .processedWith(new Mvp4g2Processor())
//          .failsToCompile()
//          .withErrorContaining("a passive event can not have a bind-attribute");
//  }
}
