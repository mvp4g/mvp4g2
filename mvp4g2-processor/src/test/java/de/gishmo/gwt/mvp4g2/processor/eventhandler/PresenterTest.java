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

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
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
          .withErrorContaining("@Presenter can only be used with as class");
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
          .withErrorContaining("incompatible types: java.lang.Class<de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterAnnotationUsingViewInterfaceAsClass.MockOneView> cannot be converted to java.lang.Class<? extends de.gishmo.gwt.mvp4g2.client.ui.LazyReverseView<?>>");
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
    JavaFileObject presenterOKExpectedSource = JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterOK/De_gishmo_gwt_mvp4g2_processor_eventhandler_presenterOK_PresenterOKMetaData.java");

    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/eventhandler/presenterOK/PresenterOK.java"))
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(presenterOKExpectedSource);
  }
}
