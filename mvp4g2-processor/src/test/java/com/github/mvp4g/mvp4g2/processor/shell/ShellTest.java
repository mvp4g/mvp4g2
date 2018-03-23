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

package com.github.mvp4g.mvp4g2.processor.shell;

import java.util.ArrayList;

import javax.tools.JavaFileObject;

import com.github.mvp4g.mvp4g2.processor.Mvp4g2Processor;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static org.truth0.Truth.ASSERT;

public class ShellTest {
  @Test
  public void eventBusWithOneShells() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithOneShell/EventBusWithOneShell.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithOneShell/IMockShellView01.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithOneShell/MockShellView01.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithOneShell/MockShellPresenter01.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithOneShell/IMockShellView02.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithOneShell/MockShellView02.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithOneShell/MockShellPresenter02.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .compilesWithoutError();
  }

  @Test
  public void eventBusWithTwoShells() {
    ASSERT.about(javaSources())
          .that(
            new ArrayList<JavaFileObject>() {
              {
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithTwoShells/EventBusWithTwoShells.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithTwoShells/IMockShellView01.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithTwoShells/MockShellView01.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithTwoShells/MockShellPresenter01.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithTwoShells/IMockShellView02.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithTwoShells/MockShellView02.java"));
                add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/shell/eventBusWithTwoShells/MockShellPresenter02.java"));
              }
            })
          .processedWith(new Mvp4g2Processor())
          .failsToCompile()
          .withErrorContaining("Mvp4g2Processor: there can be only one presenter implementing IsShell");
  }
}
