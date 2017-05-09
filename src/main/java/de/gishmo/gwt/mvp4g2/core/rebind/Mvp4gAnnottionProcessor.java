package de.gishmo.gwt.mvp4g2.core.rebind;

import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class Mvp4gAnnottionProcessor
  extends AbstractProcessor {

  protected void note(Element element,
                      Object... message) {
    log(Diagnostic.Kind.NOTE,
        element,
        message);
  }

  protected void log(Diagnostic.Kind logLevel,
                     Element element,
                     Object... message) {
    StringBuilder sb = new StringBuilder();
    for (Object o : message) {
      sb.append(o);
    }
    if (element == null) {
      processingEnv.getMessager()
                   .printMessage(logLevel,
                                 sb.toString());
    } else {
      processingEnv.getMessager()
                   .printMessage(logLevel,
                                 sb.toString(),
                                 element);
    }
  }

  protected void warn(Element element,
                      Object... message) {
    log(Diagnostic.Kind.MANDATORY_WARNING,
        element,
        message);
  }

  protected void logException(Element element,
                              Exception e) {
    StringWriter stringWriter = new StringWriter();
    e.printStackTrace(new PrintWriter(stringWriter));
    error(element,
          "Mvp4g2: Error Processing Annotation:\n" + stringWriter.getBuffer()
                                                                 .toString());
  }

  protected void error(Element element,
                       Object... message) {
    log(Diagnostic.Kind.ERROR,
        element,
        message);
  }

}
