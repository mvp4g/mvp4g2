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

public class EventTest {

  @Test
  public void testEventTestHistoryNamesNotUnique() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHistoryNamesNotUnique/EventTestHistoryNamesNotUnique.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("using a already used historyName");
  }

  @Test
  public void testEventTestHandlerInBindAndHandlersAttribute01() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerInBindAndHandlersAttribute01/EventTestHandlerInBindAndHandlersAttribute.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("can not be set in bind- and handlers-attribute");
  }

  @Test
  public void testEventTestHandlerInBindAndHandlersAttribute02() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {{
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerInBindAndHandlersAttribute02/EventTestHandlerInBindAndHandlersAttribute.java"));
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerInBindAndHandlersAttribute02/MockShellPresenter.java"));
          }})
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError();
  }

  @Test
  public void testEventTestHandlerNotInBindAndHandlersAttribute() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {{
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/EventTestHandlerNotInBindAndHandlersAttribute.java"));
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/MockOneEventHandler.java"));
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/MockShellPresenter.java"));
          }})
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/Com_github_mvp4g_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event01.java"),
                            JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/Com_github_mvp4g_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event02.java"),
                            JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/Com_github_mvp4g_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_EventTestHandlerNotInBindAndHandlersAttribute_event03.java"),
                            JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/Com_github_mvp4g_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockOneEventHandlerMetaData.java"),
                            JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/Com_github_mvp4g_mvp4g2_processor_event_eventTestHandlerNotInBindAndHandlersAttribute_MockShellPresenterMetaData.java"),
                            JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestHandlerNotInBindAndHandlersAttribute/EventTestHandlerNotInBindAndHandlersAttributeImpl.java"));
  }

  @Test
  public void testEventTestPasiveEventWithBindAttribute() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventTestPasiveEventWithBindAttribute/EventTestPasiveEventWithBindAttribute.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("a passive event can not have a bind-attribute");
  }


  @Test
  public void testEventBusWithMoreThanOneInitHistoryAnnodation() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {{
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventBusWithMoreThanOneInitHistoryAnnodation/EventBusWithMoreThanOneInitHistoryAnnodation.java"));
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventBusWithMoreThanOneInitHistoryAnnodation/MockShellPresenter.java"));
          }})
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@InitHistory can only be set a single time inside a event bus");
  }

  @Test
  public void testEventBusWithMoreThanOnNotFoundHistoryAnnodation() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {{
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventBusWithMoreThanOnNotFoundHistoryAnnodation/EventBusWithMoreThanOnNotFoundHistoryAnnodation.java"));
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventBusWithMoreThanOnNotFoundHistoryAnnodation/MockShellPresenter.java"));
          }})
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@NotFoundHistory can only be set a single time inside a event bus");
  }

  @Test
  public void testEventBusWithOneInitHistoryAnnotationAndNonZeroArgmentsSignature() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {{
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventBusWithOneInitHistoryAnnotationAndNonZeroArgmentsSignature/EventBusWithOneInitHistoryAnnotationAndNonZeroArgmentsSignature.java"));
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventBusWithOneInitHistoryAnnotationAndNonZeroArgmentsSignature/MockShellPresenter.java"));
          }})
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@InitHistory can only be used on a method with no arguments");
  }

  @Test
  public void testEventBusWithOneNotFoundHistoryAnnotationAndNonZeroArgmentsSignature() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {{
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventBusWithOneNotFoundHistoryAnnotationAndNonZeroArgmentsSignature/EventBusWithOneNotFoundHistoryAnnotationAndNonZeroArgmentsSignature.java"));
            add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventBusWithOneNotFoundHistoryAnnotationAndNonZeroArgmentsSignature/MockShellPresenter.java"));
          }})
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@NotFoundHistory can only be used on a method with no arguments");
  }

  @Test
  public void testEventDoesNotReturnVoid() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventDoesNotReturnVoid/EventBusEventDoesNotReturnVoid.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventDoesNotReturnVoid/MockShellPresenter01.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventDoesNotReturnVoid/IMockShellView01.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventDoesNotReturnVoid/MockShellView01.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("Mvp4g2Processor: EventElement: >>doSomething<< must return 'void'");
  }

  @Test
  public void testEventAnnotationInsideAOtherClassThenEventBus() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventAnnotationInsideAOtherClassThenEventBus/EventAnnotationInsideAOtherClassThenEventBus.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventAnnotationInsideAOtherClassThenEventBus/MockShellPresenter.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventAnnotationInsideAOtherClassThenEventBus/IMockShellView.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/event/eventAnnotationInsideAOtherClassThenEventBus/MockShellView.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("Mvp4g2Processor: @Event can only be used inside a event bus! >>com.github.mvp4g.mvp4g2.processor.event.eventAnnotationInsideAOtherClassThenEventBus.MockShellPresenter<< does no implement IsEventBus");
  }
}