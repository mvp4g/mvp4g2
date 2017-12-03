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
import de.gishmo.gwt.mvp4g2.processor.EventBusProcessor;
import org.junit.Test;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.truth0.Truth.ASSERT;

public class DebugEventTest {

  @Test
  public void testDebugAnnotationOnAMethod() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("de/gishmo/gwt/mvp4g2/processor/event/DebugAnnotationOnAMethod.java"))
          .processedWith(new EventBusProcessor())
          .failsToCompile()
          .withErrorContaining("@Debug can only be used on a type (interface)");
  }
}
