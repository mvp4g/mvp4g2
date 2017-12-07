package de.gishmo.gwt.mvp4g2.processor;


import com.google.auto.service.AutoService;
import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Filters;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.EventHandler;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.processor.handler.application.ApplicationAnnotationHandler;
import de.gishmo.gwt.mvp4g2.processor.handler.eventbus.EventBusAnnotationHandler;
import de.gishmo.gwt.mvp4g2.processor.handler.eventhandler.EventHandlerAnnotationHandler;
import de.gishmo.gwt.mvp4g2.processor.handler.eventhandler.PresenterAnnotationHandler;
import de.gishmo.gwt.mvp4g2.processor.handler.history.HistoryAnnotationHandler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Mvp4g2Processor.class)
public class Mvp4g2Processor
  extends AbstractProcessor {

  private ProcessorUtils processorUtils;

  public Mvp4g2Processor() {
    super();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return of(Application.class.getCanonicalName(),
              Debug.class.getCanonicalName(),
              Event.class.getCanonicalName(),
              EventBus.class.getCanonicalName(),
              EventHandler.class.getCanonicalName(),
              Filters.class.getCanonicalName(),
              Presenter.class.getCanonicalName()).collect(toSet());
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
        // handling the Presenter annotation
        PresenterAnnotationHandler.builder()
                                  .processingEnvironment(super.processingEnv)
                                  .roundEnvironment(roundEnv)
                                  .build()
                                  .process();
        // handling the History annotation
        HistoryAnnotationHandler.builder()
                                .processingEnvironment(super.processingEnv)
                                .roundEnvironment(roundEnv)
                                .build()
                                .process();
        // handling the eventbus annotation
        EventBusAnnotationHandler.builder()
                                 .processingEnvironment(super.processingEnv)
                                 .roundEnvironment(roundEnv)
                                 .build()
                                 .process();
        // handling the Application annotation
        ApplicationAnnotationHandler.builder()
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
