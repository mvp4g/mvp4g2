package de.gishmo.gwt.mvp4g2.processor;


import com.google.auto.service.AutoService;
import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Filters;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Handler;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.processor.generator.ApplicationGenerator;
import de.gishmo.gwt.mvp4g2.processor.generator.EventBusGenerator;
import de.gishmo.gwt.mvp4g2.processor.generator.EventHandlerGenerator;
import de.gishmo.gwt.mvp4g2.processor.generator.PresenterGenerator;
import de.gishmo.gwt.mvp4g2.processor.model.ApplicationMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.EventHandlerMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.HistoryMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterMetaModel;
import de.gishmo.gwt.mvp4g2.processor.scanner.ApplicationAnnotationScanner;
import de.gishmo.gwt.mvp4g2.processor.scanner.EventBusAnnotationScanner;
import de.gishmo.gwt.mvp4g2.processor.scanner.EventHandlerAnnotationScanner;
import de.gishmo.gwt.mvp4g2.processor.scanner.PresenterAnnotationScanner;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Mvp4g2Processor.class)
public class Mvp4g2Processor
  extends AbstractProcessor {

  private ProcessorUtils processorUtils;

  private ApplicationAnnotationScanner  applicationAnnotationScanner;
  private EventBusAnnotationScanner     eventBusAnnotationScanner;
  private EventHandlerAnnotationScanner eventHandlerAnnotationScanner;
  private PresenterAnnotationScanner    presenterAnnotationScanner;

  private ApplicationGenerator  applicationGenerator;
  private EventBusGenerator     eventBusGenerator;
  private EventHandlerGenerator eventHandlerGenerator;
  private PresenterGenerator    presenterGenerator;

  private ApplicationMetaModel  applicationMetaModel;
  private EventBusMetaModel     eventBusMetaModel;
  private EventHandlerMetaModel eventHandlerMetaModel;
  private HistoryMetaModel      historyMetaModel;
  private PresenterMetaModel    presenterMetaModel;

  public Mvp4g2Processor() {
    super();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return of(Application.class.getCanonicalName(),
              Debug.class.getCanonicalName(),
              Event.class.getCanonicalName(),
              EventBus.class.getCanonicalName(),
              Handler.class.getCanonicalName(),
              Filters.class.getCanonicalName(),
              Presenter.class.getCanonicalName()).collect(toSet());
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations,
                         RoundEnvironment roundEnv) {
    setUp(roundEnv);
    try {
      if (roundEnv.processingOver()) {
        this.generate();
      } else {
        this.scan(roundEnv);
      }
      return true;
    } catch (ProcessorException e) {
      this.processorUtils.createErrorMessage(e.getMessage());
    }
    return false;

//    try {
//      if (!roundEnv.processingOver()) {
//        // handling the eventBus annotation
//        EventHandlerAnnotationHandler.builder()
//                                     .processingEnvironment(super.processingEnv)
//                                     .roundEnvironment(roundEnv)
//                                     .build()
//                                     .process();
//        // handling the Presenter annotation
//        PresenterAnnotationHandler.builder()
//                                  .processingEnvironment(super.processingEnv)
//                                  .roundEnvironment(roundEnv)
//                                  .build()
//                                  .process();
//        // handling the History annotation
//        HistoryAnnotationHandler.builder()
//                                .processingEnvironment(super.processingEnv)
//                                .roundEnvironment(roundEnv)
//                                .build()
//                                .process();
//        // handling the eventbus annotation
//        EventBusAnnotationHandler.builder()
//                                 .processingEnvironment(super.processingEnv)
//                                 .roundEnvironment(roundEnv)
//                                 .build()
//                                 .process();
//        // handling the Application annotation
//        ApplicationAnnotationHandler.builder()
//                                    .processingEnvironment(super.processingEnv)
//                                    .roundEnvironment(roundEnv)
//                                    .build()
//                                    .process();
//        return false;
//      } else {
//        EventBusGenerator.builder()
//                         .processingEnvironment(super.processingEnv)
//                         .roundEnvironment(roundEnv)
//                         .build()
//                         .generate();
//        return true;
//      }
//    } catch (Exception e) {
//      this.processorUtils.createErrorMessage(e.getMessage());
//    }
  }

  private void setUp(RoundEnvironment roundEnv) {
    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(processingEnv)
                                        .build();

    this.applicationAnnotationScanner = ApplicationAnnotationScanner.builder()
                                                                    .roundEnvironment(roundEnv)
                                                                    .processingEnvironment(this.processingEnv)
                                                                    .build();
    this.eventBusAnnotationScanner = EventBusAnnotationScanner.builder()
                                                              .roundEnvironment(roundEnv)
                                                              .processingEnvironment(this.processingEnv)
                                                              .build();
    this.eventHandlerAnnotationScanner = EventHandlerAnnotationScanner.builder()
                                                                      .processingEnvironment(this.processingEnv)
                                                                      .build();
    this.presenterAnnotationScanner = PresenterAnnotationScanner.builder()
                                                                .processingEnvironment(this.processingEnv)
                                                                .build();

    this.applicationGenerator = ApplicationGenerator.builder()
                                                    .processingEnvironment(this.processingEnv)
                                                    .build();
    this.eventBusGenerator = EventBusGenerator.builder()
                                              .processingEnvironment(this.processingEnv)
                                              .build();
    this.eventHandlerGenerator = EventHandlerGenerator.builder()
                                                      .processingEnvironment(this.processingEnv)
                                                      .build();
    this.presenterGenerator = PresenterGenerator.builder()
                                                .processingEnvironment(this.processingEnv)
                                                .build();
  }

  private void generate()
    throws ProcessorException {
    if (!isNull(this.applicationMetaModel)) {
      this.applicationGenerator.generate(this.applicationMetaModel);
    }
    if (!isNull(this.eventHandlerMetaModel)) {
      this.eventHandlerGenerator.generate(this.eventHandlerMetaModel);
    }
    if (!isNull(this.presenterMetaModel)) {
      this.presenterGenerator.generate(this.presenterMetaModel);
    }
    if (!isNull(this.eventBusMetaModel)) {
      this.eventBusGenerator.generate(this.eventBusMetaModel,
                                      this.eventHandlerMetaModel,
                                      this.presenterMetaModel,
                                      this.historyMetaModel);
    }
  }

  private void scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    this.applicationMetaModel = this.applicationAnnotationScanner.scan(roundEnvironment);
    this.eventHandlerMetaModel = this.eventHandlerAnnotationScanner.scan(roundEnvironment);
    this.presenterMetaModel = this.presenterAnnotationScanner.scan(roundEnvironment);
    this.eventBusMetaModel = this.eventBusAnnotationScanner.scan(roundEnvironment);
  }
}
