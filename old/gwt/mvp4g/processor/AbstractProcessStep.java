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

package gwt.mvp4g.processor;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public abstract class AbstractProcessStep {

  protected final Types    types;
  protected final Messager messager;
  protected final Filer    filer;
  protected final Elements elements;

  protected AbstractProcessStep(Messager messager,
                                Filer filer,
                                Types types,
                                Elements elements) {
    this.types = types;
    this.messager = messager;
    this.filer = filer;
    this.elements = elements;
  }

  protected void createErrorMessage(String errorMessage) {
    StringWriter sw = new StringWriter();
    PrintWriter  pw = new PrintWriter(sw);
    pw.println(errorMessage);
    pw.close();
    messager.printMessage(Diagnostic.Kind.ERROR,
                          sw.toString());

  }

  protected void createErrorMessage(String errorMessage,
                          Exception exception) {
    StringWriter sw = new StringWriter();
    PrintWriter  pw = new PrintWriter(sw);
    pw.println(errorMessage);
    exception.printStackTrace(pw);
    pw.close();
    messager.printMessage(Diagnostic.Kind.ERROR,
                          sw.toString());
  }

  protected void createWarningMessage(String errorMessage) {
    StringWriter sw = new StringWriter();
    PrintWriter  pw = new PrintWriter(sw);
    pw.println(errorMessage);
    pw.close();
    messager.printMessage(Diagnostic.Kind.WARNING,
                          sw.toString());

  }
}
