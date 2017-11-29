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

package de.gishmo.gwt.mvp4g2.processor.eventhandler;

import com.google.testing.compile.JavaFileObjects;
import de.gishmo.gwt.mvp4g2.processor.EventHandlerProcessor;
import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.truth0.Truth.ASSERT;


public class EventHandlerTest {

  @Test
  public void testStartEventTestEventBusWithMoreThanOneStartAnnotation() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/EventHandlerAnnotationAnnotatedOnAInterface.java"))
          .processedWith(new EventHandlerProcessor())
          .failsToCompile()
          .withErrorContaining("@EventHandler can only be used with as class");
  }

  @Test
  public void testEventHandlerNotExtendingAbstractEventHandlerd() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/EventHandlerNotExtendingAbstractEventHandler.java"))
          .processedWith(new EventHandlerProcessor())
          .failsToCompile()
          .withErrorContaining("@EventHandler must extend AbstractEventHandler.class!");
  }

  @Test
  public void testEventHandlerOK() {
    JavaFileObject eventHandlerOKExpectedSource = JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/De_gishmo_gwt_mvp4g2_processor_eventhandler_EventHandlerOKMetaData.java");

    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/EventHandlerOK.java"))
          .processedWith(new EventHandlerProcessor())
          .compilesWithoutError()
          .and()
          .generatesSources(eventHandlerOKExpectedSource);
  }
}
