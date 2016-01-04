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

package org.gwt4e.mvp4g.processor.steps;

import org.gwt4e.mvp4g.processor.ProcessorContext;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by hoss on 04.01.16.
 */
public abstract class AbstractProcessStep {

  protected final Types            types;
  protected final Messager         messager;
  protected final Filer            filer;
  protected final Elements         elements;
  protected       ProcessorContext processorContext;

  public AbstractProcessStep(Types types,
                             Messager messager,
                             Filer filer,
                             Elements elements,
                             ProcessorContext processorContext) {
    this.types = types;
    this.messager = messager;
    this.filer = filer;
    this.elements = elements;
    this.processorContext = processorContext;
  }

  void createErrorMessage(String errorMessage) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    pw.println(errorMessage);
    pw.close();
    messager.printMessage(Diagnostic.Kind.ERROR,
                          sw.toString());

  }

  void createErrorMessage(String errorMessage,
                          Exception exception) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    pw.println(errorMessage);
    exception.printStackTrace(pw);
    pw.close();
    messager.printMessage(Diagnostic.Kind.ERROR,
                          sw.toString());
  }

}
