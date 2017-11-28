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
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/De_gishmo_gwt_mvp4g2_processor_eventhandler_EventHandlerOKMetaData.java"))
          .processedWith(new EventHandlerProcessor())
          .compilesWithoutError();
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
//  @Test
//  public void ApplicationOKTest() {
//    JavaFileObject applicationOKImplOKObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/application/ApplicationOK/expectedResults/ApplicationOKImpl.java");
////    JavaFileObject applicationOKDaggerModuleObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/application/ApplicationOK/expectedResults/ApplicationDaggerModule.java");
////    JavaFileObject applicationOKApplicationDaggerComponentObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/application/ApplicationOK/expectedResults/ApplicationDaggerComponent.java");
////    JavaFileObject twoEventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/TwoEventMvp4gInternalEventHandler.java");
////    JavaFileObject twoEventObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/TwoEventMvp4gInternalEvent.java");
////    JavaFileObject threeEventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/ThreeEventMvp4gInternalEventHandler.java");
////    JavaFileObject threEveentObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/ThreeEventMvp4gInternalEvent.java");
//
//    ASSERT.about(javaSource())
//          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/application/ApplicationOK/ApplicationOK.java"))
//          .processedWith(new Processor())
//          .compilesWithoutError()
//          .and()
//          .generatesSources(applicationOKImplOKObject);
//  }
}
