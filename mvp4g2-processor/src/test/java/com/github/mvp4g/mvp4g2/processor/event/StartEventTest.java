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

package com.github.mvp4g.mvp4g2.processor.event;

import java.util.ArrayList;

import javax.tools.JavaFileObject;

import com.github.mvp4g.mvp4g2.processor.Mvp4g2Processor;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static org.truth0.Truth.ASSERT;

public class StartEventTest {

  @Test
  public void testStartEventTestEventBusWithMoreThanOneStartAnnotation() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/startEventTestEventBusWithMoreThanOneStartAnnotation/StartEventTestEventBusWithMoreThanOneStartAnnotation.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Start-annotation can only be used a single time in a eventbus interface");
  }

  @Test
  public void testStartEventTestWithNonZeroArgumentMethod() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/startEventTestWithNonZeroArgumentMethod/StartEventTestWithNonZeroArgumentMethod.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Start-annotation can only be used on zero argument methods");
  }

  @Test
  public void testStartEventTestEventBusWithOneStartAnnotation() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/startEventTestEventBusWithOneStartAnnotation/StartEventTestEventBusWithOneStartAnnotation.java"));
              add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/startEventTestEventBusWithOneStartAnnotation/MockShellPresenter.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/startEventTestEventBusWithOneStartAnnotation/StartEventTestEventBusWithOneStartAnnotationImpl.java"),
                            JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/startEventTestEventBusWithOneStartAnnotation/Com_github_mvp4g_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_MockShellPresenterMetaData.java"),
                            JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/startEventTestEventBusWithOneStartAnnotation/Com_github_mvp4g_mvp4g2_processor_event_startEventTestEventBusWithOneStartAnnotation_StartEventTestEventBusWithOneStartAnnotation_start.java"));
  }
}
