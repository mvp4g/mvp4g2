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

package de.gishmo.gwt.mvp4g2.processor.application;

import com.google.testing.compile.JavaFileObjects;
import de.gishmo.gwt.mvp4g2.processor.Mvp4g2Processor;
import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.truth0.Truth.ASSERT;


public class ApplicationTest {

  @Test
  public void testApplicationAnnotationOnAClass() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/application/applicationAnnotationOnClass/ApplicationAnnotationInterfaceOnAClass.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Application annotated must be used with an interface");
  }

  @Test
  public void testApplicationAnnotationWithoutEventBusAttribute() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/application/applicationAnnotationWithoutEventBusAttribute/ApplicationAnnotationWithoutEventBusAttribute.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("is missing a default value for the element 'eventBus");
  }

  @Test
  public void testApplicationInterfaceWithoutExtendsIsApplication() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/application/applicationInterfaceWithoutExtendsIsApplication/ApplicationInterfaceWithoutExtendsIsApplication.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Application must implement IsApplication interface");
  }

  @Test
  public void testApplictionAnnotationOnAMethod() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/application/applicationAnnotationOnAMethod/ApplicationAnnotationOnAMethod.java"))
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("@Application can only be used on a type (interface)");
  }

  @Test
  public void testApplicationAnnotationOkWithLoader() {
    JavaFileObject okSource = JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/application/applicationAnnotationOkWithLoader/ApplicationAnnotationOkWithLoaderImpl.java");

    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/application/applicationAnnotationOkWithLoader/ApplicationAnnotationOkWithLoader.java"))
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(okSource);
  }

  @Test
  public void testApplicationAnnotationOkWithoutLoader() {
    JavaFileObject okSource = JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/application/applicationAnnotationOkWithoutLoader/ApplicationAnnotationOkWithoutLoaderImpl.java");

    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/application/applicationAnnotationOkWithoutLoader/ApplicationAnnotationOkWithoutLoader.java"))
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError()
          .and()
          .generatesSources(okSource);
  }
}
