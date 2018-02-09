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

public class EventbusTest {

  @Test
  public void testEventBusAnnotationOnAMethod() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventbus/eventBusAnnotationOnAMethod/EventBusAnnotationOnAMethod.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Eventbus can only be used on a type (interface)");
  }

  @Test
  public void testEventBusAnnotationOnAClass() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventbus/eventBusAnnotationOnAClass/EventBusAnnotationOnAClass.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Eventbus can only be used with an interface");
  }

  @Test
  public void testEventBusNotExtendingAbstractEventHandlerd() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventbus/eventBusNotExtendingIsEventBus/EventBusNotExtendingIsEventBus.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Eventbus must extend IsEventBus.class!");
  }

  @Test
  public void testStartEventTestEventBusWithMoreThanOneStartAnnotation() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/startEventTestEventBusWithMoreThanOneStartAnnotation/StartEventTestEventBusWithMoreThanOneStartAnnotation.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Start-annotation can only be used a single time in a eventbus interface");
  }

  @Test
  public void testStartEventTestWithNonZeroArgumentMethod() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/startEventTestWithNonZeroArgumentMethod/StartEventTestWithNonZeroArgumentMethod.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Start-annotation can only be used on zero argument methods");
  }

  @Test
  public void testEventWithHandlerAttributeNotImplemented01() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventbus/eventWithHandlerAttributeNotImplemented01/EventBusEventWithHandlerAttributeNotImplemented.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventbus/eventWithHandlerAttributeNotImplemented01/MockShellPresenter01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventbus/eventWithHandlerAttributeNotImplemented01/IMockShellView01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventbus/eventWithHandlerAttributeNotImplemented01/MockShellView01.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("Mvp4g2Processor: presenter >>de.gishmo.gwt.mvp4g2.processor.eventbus.eventWithHandlerAttributeNotImplemented01.MockShellPresenter01<< -> event >>onDoSomething()<< is not handled by presenter/handler");
  }

  @Test
  public void testEventWithHandlerAttributeNotImplemented02() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventbus/eventWithHandlerAttributeNotImplemented02/EventBusEventWithHandlerAttributeNotImplemented.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventbus/eventWithHandlerAttributeNotImplemented02/MockShellPresenter01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventbus/eventWithHandlerAttributeNotImplemented02/IMockShellView01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventbus/eventWithHandlerAttributeNotImplemented02/MockShellView01.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("Mvp4g2Processor: presenter >>de.gishmo.gwt.mvp4g2.processor.eventbus.eventWithHandlerAttributeNotImplemented02.MockShellPresenter01<< -> event >>onDoSomething(java.lang.String)<< is not handled by presenter/handler");
  }
}
