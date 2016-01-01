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
import org.gwt4e.mvp4g.processor.Mvp4gProcessor;
import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.truth0.Truth.ASSERT;


public class EventBusTest {


  /**
   * <p>Test:
   * <br>
   * Mvp4gEventBus is not an Interface.
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
          .processedWith(new Mvp4gProcessor())
          .failsToCompile()
          .withErrorContaining("EventBusIsNotAnInterface applied on a type that's not an interface; ignoring");
  }

  /**
   * /**
   * <p>Test:
   * <br>
   * Mvp4gEvent bus with no events
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
          .processedWith(new Mvp4gProcessor())
          .failsToCompile()
          .withErrorContaining("EventBusWithNoEvents has no events defined");
  }

  /**
   * /**
   * <p>Test:
   * <br>
   * Mvp4gEvent bus with an events that has a return value
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
          .processedWith(new Mvp4gProcessor())
          .failsToCompile()
          .withErrorContaining("applied on a method");
  }

  /**
   * <p>Test:
   * <br>
   * Mvp4gEvent bus with one events
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
          .processedWith(new Mvp4gProcessor())
          .failsToCompile()
          .withErrorContaining("is already used. Please choose another name. (It is not possible to work with same event-name and different signatures.");
  }

  /**
   * <p>Test:
   * <br>
   * Mvp4gEvent bus with one events
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithOneEventTest() {
    JavaFileObject eventBusObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithOneEvent/expectedResults/EventBusWithOneEventImpl.java");
    JavaFileObject eventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithOneEvent/expectedResults/OneEventMvp4gEventHandler.java");
    JavaFileObject eventObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithOneEvent/expectedResults/OneEventMvp4gEvent.java");

    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithOneEvent/EventBusWithOneEvent.java"))
          .processedWith(new Mvp4gProcessor())
          .compilesWithoutError()
          .and()
          .generatesSources(eventHandlerObject,
                            eventObject,
                            eventBusObject);
  }

  /**
   * <p>Test:
   * <br>
   * Mvp4gEvent bus with one events
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithEventsTest() {
    JavaFileObject eventBusObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/EventBusWithEventsImpl.java");
    JavaFileObject oneEventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/OneEventMvp4gEventHandler.java");
    JavaFileObject oneEventObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/OneEventMvp4gEvent.java");
    JavaFileObject twoEventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/TwoEventMvp4gEventHandler.java");
    JavaFileObject twoEventObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/TwoEventMvp4gEvent.java");
    JavaFileObject threeEventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/ThreeEventMvp4gEventHandler.java");
    JavaFileObject threEveentObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/ThreeEventMvp4gEvent.java");

    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/EventBusWithEvents.java"))
          .processedWith(new Mvp4gProcessor())
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
}
