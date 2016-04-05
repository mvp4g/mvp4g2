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

package org.gwt4e.mvp4g.test.apt.eventbus;


import com.google.testing.compile.JavaFileObjects;
import org.gwt4e.mvp4g.processor.Processor;
import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.truth0.Truth.ASSERT;

public class EventBusTest {


  /**
   * <p>Test:
   * <br>
   * Mvp4gInternalEventBus is not an Interface.
   * <br><br>
   * Expected result:
   * <br>
   * error message: EventBusIsNotAnInterface applied on a type that's not an interface; ignoring
   * </p>
   */
  @Test
  public void EventBusIsNotAnInterfaceTest() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusIsNotAnInterface.java"))
          .processedWith(new Processor())
          .failsToCompile()
          .withErrorContaining("EventBus applied on a type org.gwt4e.mvp4g.test.apt.eventbus.EventBusIsNotAnInterface that's not an interface; ignoring");
  }

  /**
   * /**
   * <p>Test:
   * <br>
   * Mvp4gInternalEvent bus with no events
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithNoEventsTest() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithNoEvents.java"))
          .processedWith(new Processor())
          .compilesWithoutError();
  }

  /**
   * /**
   * <p>Test:
   * <br>
   * Mvp4gInternalEvent bus with an events that has a return value
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithAndReturnValue() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEventAndReturnValue.java"))
          .processedWith(new Processor())
          .failsToCompile()
          .withErrorContaining("applied on a method");
  }

  /**
   * <p>Test:
   * <br>
   * Mvp4gInternalEvent bus with one events
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithSameEventNames() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithSameEventNames.java"))
          .processedWith(new Processor())
          .failsToCompile()
          .withErrorContaining("is already used. Please choose another name. (It is not possible to work with same event-name and different signatures.");
  }

  /**
   * <p>Test:
   * <br>
   * Mvp4gInternalEvent bus with one events
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithOneEventTest() {
    JavaFileObject eventBusObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithOneEvent/expectedResults/EventBusWithOneEventImpl.java");
    JavaFileObject eventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithOneEvent/expectedResults/OneEventMvp4gInternalEventHandler.java");
    JavaFileObject eventObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithOneEvent/expectedResults/OneEventMvp4gInternalEvent.java");

    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithOneEvent/EventBusWithOneEvent.java"))
          .processedWith(new Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(eventHandlerObject,
                            eventObject,
                            eventBusObject);
  }

  /**
   * <p>Test:
   * <br>
   * Mvp4gInternalEvent bus with one events
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithEventsTest() {
    JavaFileObject eventBusObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/EventBusWithEventsImpl.java");
    JavaFileObject oneEventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/OneEventMvp4gInternalEventHandler.java");
    JavaFileObject oneEventObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/OneEventMvp4gInternalEvent.java");
    JavaFileObject twoEventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/TwoEventMvp4gInternalEventHandler.java");
    JavaFileObject twoEventObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/TwoEventMvp4gInternalEvent.java");
    JavaFileObject threeEventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/ThreeEventMvp4gInternalEventHandler.java");
    JavaFileObject threEveentObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/ThreeEventMvp4gInternalEvent.java");

    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/EventBusWithEvents.java"))
          .processedWith(new Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(eventBusObject,
                            oneEventHandlerObject,
                            oneEventObject,
                            twoEventHandlerObject,
                            twoEventObject,
                            threeEventHandlerObject,
                            threEveentObject);
  }

  /**
   * <p>Test:
   * <br>
   * Mvp4gInternalEvent bus with one events
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithDebugAnnotationTest() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithDebug.java"))
          .processedWith(new Processor())
          .compilesWithoutError();
  }

  /**
   * <p>Test:
   * <br>
   * Mvp4gInternalEvent bus with one events and @Debug SIMPLE
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithDebugSimpleTest() {
    JavaFileObject eventBusObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/eventBusWithDebugSimple/expectedResults/EventBusWithDebugSimpleImpl.java");
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/eventBusWithDebugSimple/EventBusWithDebugSimple.java"))
          .processedWith(new Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(eventBusObject);
  }

  /**
   * <p>Test:
   * <br>
   * Mvp4gInternalEvent bus with one events and @Debug DETAIL
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithDebugDetailTest() {
    JavaFileObject eventBusObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/eventBusWithDebugDetail/expectedResults/EventBusWithDebugDetailImpl.java");
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/eventBusWithDebugDetail/EventBusWithDebugDetail.java"))
          .processedWith(new Processor())
          .compilesWithoutError();
//          .and()
//          .generatesSources(eventBusObject);
  }
}
