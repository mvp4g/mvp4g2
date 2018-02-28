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
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterAnnotationAnnotatedOnAbstractClass" + "/PresenterAnnotationAnnotatedOnAbstractClass.java"))
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
          .that(new ArrayList<JavaFileObject>() {
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
          .that(new ArrayList<JavaFileObject>() {
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
          .that(new ArrayList<JavaFileObject>() {
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
          .that(new ArrayList<JavaFileObject>() {
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
          .that(new ArrayList<JavaFileObject>() {
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

  @Test
  public void testHandlerWithWrongImplementation03() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation03/EventBusHandlerWithNotImplementedEvent.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation03/MockShellPresenter01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation03/IMockShellView01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation03/MockShellView01.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError();
  }

  @Test
  public void testHandlerWithWrongImplementation04() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation04/EventBusHandlerWithNotImplementedEvent.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation04/MockShellPresenter01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation04/IMockShellView01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithWrongImplementation04/MockShellView01.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError();
  }

  @Test
  public void testEventHandlingMethodDoesNotReturnVoid03() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlingMethodDoesNotReturnVoid03/EventBusEventHandlingMethodDoesNotReturnVoid.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlingMethodDoesNotReturnVoid03/MockShellPresenter01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlingMethodDoesNotReturnVoid03/IMockShellView01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlingMethodDoesNotReturnVoid03/MockShellView01.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("Mvp4g2Processor: >>de.gishmo.gwt.mvp4g2.processor.eventhandler.eventHandlingMethodDoesNotReturnVoid03.MockShellPresenter01<< -> EventElement: >>onDoSomething()<< must return 'void'");
  }

  @Test
  public void testEventHandlingMethodDoesNotReturnVoid04() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlingMethodDoesNotReturnVoid04/EventBusEventHandlingMethodDoesNotReturnVoid.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlingMethodDoesNotReturnVoid04/MockShellPresenter01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlingMethodDoesNotReturnVoid04/IMockShellView01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventHandlingMethodDoesNotReturnVoid04/MockShellView01.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("Mvp4g2Processor: >>de.gishmo.gwt.mvp4g2.processor.eventhandler.eventHandlingMethodDoesNotReturnVoid04.MockShellPresenter01<< -> EventElement: >>onDoSomething()<< must return 'void'");
  }

  /**
   * Check, that compilation works, if handler-attribute and EventHandler annotation is used for one event
   */
  @Test
  public void testEventBusEventhandlerWithHanderlsAttributeAndEventHandlerAnnotation() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventhandlerWithHanderlsAttributeAndEventHandlerAnnotation/EventBusEventhandlerWithHanderlsAttributeAndEventHandlerAnnotation.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventhandlerWithHanderlsAttributeAndEventHandlerAnnotation/MockShellPresenter01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventhandlerWithHanderlsAttributeAndEventHandlerAnnotation/IMockShellView01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventhandlerWithHanderlsAttributeAndEventHandlerAnnotation/MockShellView01.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/eventhandlerWithHanderlsAttributeAndEventHandlerAnnotation/EventBusEventhandlerWithHanderlsAttributeAndEventHandlerAnnotationImpl.java"));
  }

  @Test
  public void testPresenterWithMultipleAttribute01() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute01/EventBusPresenterWithMultipleAttibute01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute01/MockShellPresenter01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute01/IMockShellView01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute01/MockShellView01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute01/MockMultiplePresenter01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute01/IMockMultipleView01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute01/MockMultipleView01.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError();
  }

  // missng IsViewCerator-interface
  @Test
  public void testPresenterWithViewCreationMethodPresenter01() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter01/EventBusPresenterWithViewCreationMethodPresenter01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter01/MockShellPresenter01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter01/IMockShellView01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter01/MockShellView01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter01/MockPresenter01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter01/IMockView01.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter01/MockView01.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Presenter must implement the IsViewCreator interface");
  }

  // ok
  @Test
  public void testPresenterWithViewCreationMethodPresenter02() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter02/EventBusPresenterWithViewCreationMethodPresenter02.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter02/MockShellPresenter02.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter02/IMockShellView02.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter02/MockShellView02.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter02/MockPresenter02.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter02/IMockView02.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter02/MockView02.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError();
  }

  // IsViewCreator interface without generic
  @Test
  public void testPresenterWithViewCreationMethodPresenter03() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter03/EventBusPresenterWithViewCreationMethodPresenter03.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter03/MockShellPresenter03.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter03/IMockShellView03.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter03/MockShellView03.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter03/MockPresenter03.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter03/IMockView03.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter03/MockView03.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("IsViewCreator interface needs a generic parameter");
  }

  // IsViewCreator interface used without generic viewCreator = Presenter.VIEW_CREATION_METHOD.PRESENTER
  @Test
  public void testPresenterWithViewCreationMethodPresenter04() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter04/EventBusPresenterWithViewCreationMethodPresenter04.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter04/MockShellPresenter04.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter04/IMockShellView04.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter04/MockShellView04.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter04/MockPresenter04.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter04/IMockView04.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter04/MockView04.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("the IsViewCreator interface can only be used in case of viewCreator = Presenter.VIEW_CREATION_METHOD.PRESENTER");
  }

  // IsViewCreator: check that the generic parameter is the view interface
  @Test
  public void testPresenterWithViewCreationMethodPresenter05() {
    ASSERT.about(javaSources())
          .that(new ArrayList<JavaFileObject>() {
            {
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter05/EventBusPresenterWithViewCreationMethodPresenter05.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter05/MockShellPresenter05.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter05/IMockShellView05.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter05/MockShellView05.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter05/MockPresenter05.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter05/IMockView05.java"));
              add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithViewCreationMethodPresenter05/MockView05.java"));
            }
          })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("IsViewCreator interface only allows the generic parameter ->");
  }

  //  @Test
  //  public void testPresenterWithMultipleAttribute02() {
  //    ASSERT.about(javaSources())
  //          .that(
  //            new ArrayList<JavaFileObject>() {
  //              {
  //                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute02/EventBusPresenterWithMultipleAttibute02.java"));
  //                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute02/MockShellPresenter01.java"));
  //                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute02/IMockShellView01.java"));
  //                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute02/MockShellView01.java"));
  //                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute02/MockMultiplePresenter01.java"));
  //                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute02/IMockMultipleView01.java"));
  //                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute02/MockMultipleView01.java"));
  //                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute02/MockMultiplePresenter02.java"));
  //                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute02/IMockMultipleView02.java"));
  //                add(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterWithMultipleAttribute02/MockMultipleView02.java"));
  //              }
  //            })
  //          .processedWith(new Mvp4g2Processor())
  //          .compilesWithoutError();
  //  }
}
