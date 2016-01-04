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

package org.gwt4e.mvp4g.test.apt.module;

import com.google.testing.compile.JavaFileObjects;
import org.gwt4e.mvp4g.processor.Processor;
import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.truth0.Truth.ASSERT;

public class ModuleTest {


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
  public void ModuleIsNotAnInterfaceTest() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/module/ModuleIsNotAnInterface.java"))
          .processedWith(new Processor())
          .failsToCompile()
          .withErrorContaining("Module applied on a type org.gwt4e.mvp4g.test.apt.module.ModuleIsNotAnInterface that's not an interface; ignoring");
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
  public void ModuleOKTest() {
    JavaFileObject applicationOKImplOKObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/module/ModuleOK/expectedResults/ModuleOKModuleImpl.java");
//    JavaFileObject applicationOKDaggerModuleObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/application/ApplicationOK/expectedResults/ApplicationDaggerModule.java");
//    JavaFileObject applicationOKApplicationDaggerComponentObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/application/ApplicationOK/expectedResults/ApplicationDaggerComponent.java");
//    JavaFileObject twoEventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/TwoEventMvp4gInternalEventHandler.java");
//    JavaFileObject twoEventObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/TwoEventMvp4gInternalEvent.java");
//    JavaFileObject threeEventHandlerObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/ThreeEventMvp4gInternalEventHandler.java");
//    JavaFileObject threEveentObject = JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/eventbus/EventBusWithEvents/expectedResults/ThreeEventMvp4gInternalEvent.java");

    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/module/ModuleOK/ModuleOKModule.java"))
          .processedWith(new Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(applicationOKImplOKObject);
  }
}
