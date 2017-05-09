package de.gishmo.gwt.mvp4g2.debugid.rebind;

import de.gishmo.gwt.mvp4g2.core.rebind.Mvp4gAnnottionProcessor;
import de.gishmo.gwt.mvp4g2.core.rebind.Mvp4gClassNames;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes({Mvp4gClassNames.DEBUG_ID})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DebugIdProcessor
  extends Mvp4gAnnottionProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations,
                         RoundEnvironment roundEnv) {
    try {
      if (!roundEnv.processingOver()) {
        for (TypeElement annotation : annotations) {
          // gather all of the annotated elements and sort them first for a more consistent run order
          Element[] elements = roundEnv.getElementsAnnotatedWith(annotation).stream()
//                                       .sorted((o1, o2) -> getQualifiedName(o1).compareTo(getQualifiedName(o2)))
                                       .toArray(Element[]::new);
//
          // process each element one-by-one
          for (Element element : elements) {
//            processElement(element);
          }
        }
        return true;
      }
      return false;
    } catch (Exception error) {
      // log the error
//      logException(getCurrentElement(), error);
      return false;
    }
  }
}
