/*
 * Copyright (c) 2018 - Frank Hossfeld
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 *
 */

package com.github.mvp4g.mvp4g2.processor.history;

import java.util.ArrayList;

import javax.tools.JavaFileObject;

import com.github.mvp4g.mvp4g2.processor.Mvp4g2Processor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.testing.compile.Compiler.javac;

public class HistoryTest {

  @Test
  public void testHistoryAnnoationOnAInterface() {
    Compilation compilation = javac().withProcessors(new Mvp4g2Processor())
                                     .compile(new ArrayList<JavaFileObject>() {
                                       {
                                         add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/history/historyAnnotationOnAInterface/HistoryAnnoationOnAInterface.java"));
                                       }
                                     });
    CompilationSubject.assertThat(compilation)
                      .failed();
    CompilationSubject.assertThat(compilation)
                      .hadErrorContaining("@History can only be used with a class!");
  }

  @Test
  public void testHistoryAnnotationOnAClassWhichDoesNotExtendsIsHistoryConverter() {
    Compilation compilation = javac().withProcessors(new Mvp4g2Processor())
                                     .compile(new ArrayList<JavaFileObject>() {
                                       {
                                         add(JavaFileObjects.forResource("com/github/mvp4g/mvp4g2/processor/history/historyAnnotationOnAClassWhichDoesNotExtendsIsHistoryConverter/HistoryAnnotationOnAClassWhichDoesNotExtendsIsHistoryConverter.java"));
                                       }
                                     });
    CompilationSubject.assertThat(compilation)
                      .failed();
    CompilationSubject.assertThat(compilation)
                      .hadErrorContaining("a class annotated with @History must extend IsHistoryConverter.class!");
  }
}
