package de.gishmo.gwt.mvp4g2.old.processor.application;

import com.sun.tools.internal.ws.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.core.client.annotation.Application;
import de.gishmo.gwt.mvp4g2.old.processor.AbstractMvp4g2Processor;
import de.gishmo.gwt.mvp4g2.old.processor.Mvp4g2ClassNames;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes(Mvp4g2ClassNames.APPLICATION)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ApplicationProcessor
  extends AbstractMvp4g2Processor {

  public ApplicationProcessor() {
    super();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations,
                         RoundEnvironment roundEnv) {
    if (!roundEnv.processingOver()) {
      // setUpprocessor ...
      setUp();
      // valildate @Application annotation
      this.validate(roundEnv.getElementsAnnotatedWith(Application.class));

      for (Element element : roundEnv.getElementsAnnotatedWith(Application.class)) {
        try {

        } catch (Exception e) {
          super.createErrorMessage(e.getMessage());
          throw new RuntimeException(e);
        }
      }
      return true;
    }
    return false;
  }

  /**
   * <p>Validate the @Application annotation</p>
   *
   * @param elementsByAnnotation
   *   environment for information about the current and prior round
   */
  private void validate(Set<? extends Element> elementsByAnnotation) {
    // at least there should exatly one Application annotation!
    if (elementsByAnnotation.size() == 0) {
      throw new ProcessorException("Missing Mvp4g Application interface");
    }
    // at least there should only one Application annotation!
    if (elementsByAnnotation.size() > 1) {
      throw new ProcessorException("There should be at least only one interface, that is annotated with @Application.");
    }
  }

}
