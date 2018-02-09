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
import de.gishmo.gwt.mvp4g2.processor.Mvp4g2Processor;
import org.junit.Test;

import javax.tools.JavaFileObject;
import java.util.ArrayList;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static org.truth0.Truth.ASSERT;

public class HandlerTest {

  @Test
  public void testEventHandlerAnnotationAnnotatedAbstractClass() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerAnnotationAnnotatedOnAbstractClass/EventHandlerAnnotationAnnotatedOnAbstractClass.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Handler can not be ABSTRACT");
  }


  @Test
  public void testEventHandlerAnnotationAnnotatedOnAInterface() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerAnnotationAnnotatedOnAInterface/EventHandlerAnnotationAnnotatedOnAInterface.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Handler can only be used with a class");
  }

  @Test
  public void testEventHandlerNotExtendingAbstractEventHandlerd() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerNotExtendingAbstractEventHandler/EventHandlerNotExtendingAbstractEventHandler.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Handler must extend AbstractHandler.class!");
  }

  @Test
  public void testEventHandlerOK() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerOK/EventHandlerOK.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerOK/MockEventBus.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerOK/MockShellPresenter.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerOK/MockOneEventHandler.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerOK/De_gishmo_gwt_mvp4g2_processor_eventhandler_eventHandlerOK_EventHandlerOKMetaData.java"));
  }

  @Test
  public void testEventHandlerWithNotImplementedEvent() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/handlerWithNotImplementedEvent/EventBusHandlerWithNotImplementedEvent.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/handlerWithNotImplementedEvent/MockOneEventHandler.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/handlerWithNotImplementedEvent/MockShellPresenter01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/handlerWithNotImplementedEvent/IMockShellView01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/handlerWithNotImplementedEvent/MockShellView01.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("event >>doSomethingInHandler<< is never handled by a presenter or handler");
  }
}
