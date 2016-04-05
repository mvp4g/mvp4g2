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

package org.gwt4e.mvp4g.test.apt.debug;


import com.google.testing.compile.JavaFileObjects;
import org.gwt4e.mvp4g.processor.Processor;
import org.junit.Test;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.truth0.Truth.ASSERT;

public class DebugTest {


  /**
   * <p>Test:
   * <br>
   *
   * @Debuf is not applied to class, that is annotated with @EventBus.
   * <br><br>
   * Expected result:
   * <br>
   * error message: ... is not applied to class which is annotated with ...
   * </p>
   */
  @Test
  public void DebugIsNotAnEventBusTest() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/debug/DebugIsNotAnEventBus.java"))
          .processedWith(new Processor())
          .failsToCompile()
          .withErrorContaining(" is not applied to class which is annotated with");
  }


  /**
   * <p>Test:
   * <br>
   *
   * @Debuf is not applied to class, that is annotated with @EventBus.
   * <br><br>
   * Expected result:
   * <br>
   * error message: ... is not applied to class which is annotated with ...
   * </p>
   */
  @Test
  public void DebugWithEventBusTest() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g/test/apt/debug/debugWithEventBus/DebugWithEventBus.java"))
          .processedWith(new Processor())
          .compilesWithoutError();
  }
}
