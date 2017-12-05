package de.gishmo.gwt.mvp4g2.processor;


import com.google.auto.service.AutoService;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Filters;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Start;
import de.gishmo.gwt.mvp4g2.client.history.annotation.InitHistory;
import de.gishmo.gwt.mvp4g2.client.history.annotation.NotFoundHistory;
import de.gishmo.gwt.mvp4g2.processor.handler.eventbus.EventBusAnnotationHandler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashSet;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(EventBusProcessor.class)
public class EventBusProcessor
  extends AbstractProcessor {

  private ProcessorUtils processorUtils;

  public EventBusProcessor() {
    super();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new LinkedHashSet<String>();
    annotations.add(Debug.class.getCanonicalName());
    annotations.add(Event.class.getCanonicalName());
    annotations.add(EventBus.class.getCanonicalName());
    annotations.add(Filters.class.getCanonicalName());
    annotations.add(InitHistory.class.getCanonicalName());
    annotations.add(NotFoundHistory.class.getCanonicalName());
    annotations.add(Start.class.getCanonicalName());
    return annotations;
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations,
                         RoundEnvironment roundEnv) {
    if (!roundEnv.processingOver()) {
      // setup processor ...
      setUp();
      try {
        // handling the eventbus annotation
        EventBusAnnotationHandler.builder()
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
