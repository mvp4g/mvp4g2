package de.gishmo.gwt.mvp4g2.old.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class AbstractMvp4g2Processor
  extends AbstractProcessor {

  protected Types    types;
  protected Messager messager;
  protected Filer    filer;
  protected Elements elements;

  protected void setUp() {
    this.types = processingEnv.getTypeUtils();
    this.messager = processingEnv.getMessager();
    this.filer = processingEnv.getFiler();
    this.elements = processingEnv.getElementUtils();
  }

  public AbstractMvp4g2Processor() {
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

  protected void log(Element element,
                   Object... message) {
    StringBuilder sb = new StringBuilder();
    for (Object o : message) {
      sb.append(o);
    }
    if (element == null) {
      processingEnv.getMessager()
                   .printMessage(Diagnostic.Kind.ERROR,
                                 sb.toString());
    } else {
      processingEnv.getMessager()
                   .printMessage(Diagnostic.Kind.ERROR,
                                 sb.toString(),
                                 element);
    }
  }
}
