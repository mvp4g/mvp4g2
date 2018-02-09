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

public class PresenterTest {

  @Test
  public void testPresenterAnnotationAnnotatedOnAbstractClass() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterAnnotationAnnotatedOnAbstractClass" +
                                            "/PresenterAnnotationAnnotatedOnAbstractClass.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Presenter can not be ABSTRACT");
  }

  @Test
  public void testPresenterAnnotationAnnotatedOnAInterface() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterAnnotationAnnotatedOnAInterface/PresenterAnnotationAnnotatedOnAInterface.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Presenter can only be used with a class");
  }

  @Test
  public void testPresenterNotExtendingAbstractPresenter() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterAnnotationNotExtendingAbstractPresenter/PresenterAnnotationNotExtendingAbstractPresenter.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Presenter must extend AbstractPresenter.class");
  }

  @Test
  public void testPresenterAnnotationUsingViewClassAsInterface() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterAnnotationUsingViewClassAsInterface/PresenterAnnotationUsingViewClassAsInterface.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("the viewInterface-attribute of a @Presenter must be a interface!");
  }

  @Test
  public void testPresenterAnnotationUsingViewInterfaceAsClass() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterAnnotationUsingViewInterfaceAsClass/PresenterAnnotationUsingViewInterfaceAsClass.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("incompatible types: java.lang.Class<de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterAnnotationUsingViewInterfaceAsClass.IMockOneView> cannot be converted to java.lang.Class<? extends de.gishmo.gwt.mvp4g2.core.ui.LazyReverseView<?>>");
  }

  @Test
  public void testPresenterAnnotationUsingViewAbstractClass() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterAnnotationUsingViewAbstractClass/PresenterAnnotationUsingViewAbstractClass.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("class-attribute of @Presenter can not be ABSTRACT");
  }

  @Test
  public void testPresenterOK() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterOK/PresenterOK.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterOK/MockEventBus.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterOK/MockShellPresenter.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterOK/De_gishmo_gwt_mvp4g2_processor_eventhandler_presenterOK_PresenterOKMetaData.java"));
  }

  @Test
  public void testEventHandlerWithUnusedEventHandlerImplementation() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerWithUnusedEventHandlerImplementation/EventBusEventHandlerWithUnusedEventHandlerImplementation.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerWithUnusedEventHandlerImplementation/MockShellPresenter01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerWithUnusedEventHandlerImplementation/IMockShellView01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlerWithUnusedEventHandlerImplementation/MockShellView01.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError();
  }

  @Test
  public void testEventHandlerWithNotImplementedEvent() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithNotImplementedEvent/EventBusEventHandlerWithNotImplementedEvent.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithNotImplementedEvent/MockShellPresenter01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithNotImplementedEvent/IMockShellView01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithNotImplementedEvent/MockShellView01.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("event >>doSomething()<< is never handled by a presenter or handler");
  }

  @Test
  public void testPresenterWithWrongImplementation01() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation01/EventBusHandlerWithNotImplementedEvent.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation01/MockShellPresenter01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation01/IMockShellView01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation01/MockShellView01.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("event >>doSomething()<< is never handled by a presenter or handler");
  }

  @Test
  public void testHandlerWithWrongImplementation02() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation02/EventBusHandlerWithNotImplementedEvent.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation02/MockShellPresenter01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation02/IMockShellView01.java"));
                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation02/MockShellView01.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("event >>doSomething(java.lang.String)<< is never handled by a presenter or handler");
  }
}
