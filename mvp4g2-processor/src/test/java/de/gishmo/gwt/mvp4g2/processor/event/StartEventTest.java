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

package de.gishmo.gwt.mvp4g2.processor.event;

import com.google.testing.compile.JavaFileObjects;
import de.gishmo.gwt.mvp4g2.processor.Mvp4g2Processor;
import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.truth0.Truth.ASSERT;


public class StartEventTest {

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
  public void testStartEventTestEventBusWithOneStartAnnotation() {
    JavaFileObject expectedSource = JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/startEventTestEventBusWithOneStartAnnotation/StartEventTestEventBusWithOneStartAnnotationImpl.java");

    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/startEventTestEventBusWithOneStartAnnotation/StartEventTestEventBusWithOneStartAnnotation.java"))
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(expectedSource);
  }
}
