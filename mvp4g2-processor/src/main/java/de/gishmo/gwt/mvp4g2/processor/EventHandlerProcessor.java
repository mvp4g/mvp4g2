package de.gishmo.gwt.mvp4g2.processor;


import com.google.auto.service.AutoService;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.EventHandler;
import de.gishmo.gwt.mvp4g2.processor.handler.eventhandler.EventHandlerAnnotationHandler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashSet;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(EventHandlerProcessor.class)
public class EventHandlerProcessor
  extends AbstractProcessor {

  private ProcessorUtils processorUtils;

  public EventHandlerProcessor() {
    super();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new LinkedHashSet<String>();
    annotations.add(EventHandler.class.getCanonicalName());
    return annotations;
  }
  @Override
  public boolean process(Set<? extends TypeElement> annotations,
                         RoundEnvironment roundEnv) {
    if (!roundEnv.processingOver()) {
      // setup processor ...
      setUp();
      try {
        // handling the eventBus annotation
        EventHandlerAnnotationHandler.builder()
                                     .processingEnvironment(super.processingEnv)
                                     .roundEnvironment(roundEnv)
                                     .build()
                                     .process();
      } catch (Exception e) {
        this.processorUtils.createErrorMessage(e.getMessage());
      }
      return true;
    }
    return false;
  }

  private void setUp() {
    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(super.processingEnv)
                                        .build();
  }
}
