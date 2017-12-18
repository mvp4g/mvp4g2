package de.gishmo.gwt.mvp4g2.processor;


import com.google.auto.service.AutoService;
import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Filters;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.EventHandler;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.processor.generator.ApplicationGenerator;
import de.gishmo.gwt.mvp4g2.processor.generator.EventHandlerGenerator;
import de.gishmo.gwt.mvp4g2.processor.generator.PresenterGenerator;
import de.gishmo.gwt.mvp4g2.processor.model.ApplicationMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.EventHandlerModel;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterModel;
import de.gishmo.gwt.mvp4g2.processor.scanner.ApplicationAnnotationScanner;
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
  private EventHandlerAnnotationScanner eventHandlerAnnotationScanner;
  private PresenterAnnotationScanner    presenterAnnotationScanner;

  private ApplicationGenerator  applicationGenerator;
  private EventHandlerGenerator eventHandlerGenerator;
  private PresenterGenerator    presenterGenerator;

  private ApplicationMetaModel applicationMetaModel;
  private EventHandlerModel    eventHandlerModel;
  private PresenterModel       presenterModel;

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
    this.eventHandlerAnnotationScanner = EventHandlerAnnotationScanner.builder()
                                                                      .processingEnvironment(this.processingEnv)
                                                                      .build();
    this.presenterAnnotationScanner = PresenterAnnotationScanner.builder()
                                                                .processingEnvironment(this.processingEnv)
                                                                .build();

    this.applicationGenerator = ApplicationGenerator.builder()
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
    if (!isNull(this.eventHandlerModel)) {
      this.eventHandlerGenerator.generate(this.eventHandlerModel);
    }
    if (!isNull(this.presenterModel)) {
      this.presenterGenerator.generate(this.presenterModel);
    }
  }

  private void scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    this.applicationMetaModel = this.applicationAnnotationScanner.scan(roundEnvironment);
    this.eventHandlerModel = this.eventHandlerAnnotationScanner.scan(roundEnvironment);
    this.presenterModel = this.presenterAnnotationScanner.scan(roundEnvironment);
  }
}
