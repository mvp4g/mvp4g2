package de.gishmo.gwt.mvp4g2.processor;


import de.gishmo.gwt.mvp4g2.processor.handler.application.ApplicationAnnotationHandler;
import de.gishmo.gwt.mvp4g2.processor.handler.event.EventBusAnnotationHandler;
import de.gishmo.gwt.mvp4g2.processor.handler.eventHandler.EventHandlerAnnotationHandler;
import de.gishmo.gwt.mvp4g2.processor.handler.eventHandler.PresenterAnnotationHandler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes({SupportedAnnotationClassNames.APPLICATION,
                           SupportedAnnotationClassNames.EVENTBUS,
                           SupportedAnnotationClassNames.PRESENTER})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Processor
  extends AbstractProcessor {

  private ProcessorUtils processorUtils;

  public Processor() {
    super();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations,
                         RoundEnvironment roundEnv) {
    if (!roundEnv.processingOver()) {
      // setup processor ...
      setUp();
      try {
        // handling the Application annotation
        ApplicationAnnotationHandler.builder()
                                    .processingEnvironment(super.processingEnv)
                                    .roundEnvironment(roundEnv)
                                    .build()
                                    .process();
        // handling the eventBus annotation
        EventBusAnnotationHandler.builder()
                                 .processingEnvironment(super.processingEnv)
                                 .roundEnvironment(roundEnv)
                                 .build()
                                 .process();
        // handling the eventBus annotation
        EventHandlerAnnotationHandler.builder()
                                     .processingEnvironment(super.processingEnv)
                                     .roundEnvironment(roundEnv)
                                     .build()
                                     .process();
        // handling the Presenter annotation
        PresenterAnnotationHandler.builder()
                                  .processingEnvironment(super.processingEnv)
                                  .roundEnvironment(roundEnv)
                                  .build()
                                  .process();


      } catch (Exception e) {
        this.processorUtils.createErrorMessage(e.getMessage());
        throw new RuntimeException(e);
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
