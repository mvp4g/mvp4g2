package de.gishmo.gwt.mvp4g2.processor.scanner;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;

import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.scanner.validation.DebugAnnotationValidator;

import static java.util.Objects.isNull;

public class DebugAnnotationScanner {

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private TypeElement           eventBusTypeElement;
  private EventBusMetaModel     eventBusMetaModel;

  @SuppressWarnings("unused")
  private DebugAnnotationScanner(Builder builder) {
    super();
    this.processingEnvironment = builder.processingEnvironment;
    this.eventBusTypeElement = builder.eventBusTypeElement;
    this.eventBusMetaModel = builder.eventBusMetaModel;
    setUp();
  }

  private void setUp() {
    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public EventBusMetaModel scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // do validation
    DebugAnnotationValidator.builder()
                            .roundEnvironment(roundEnvironment)
                            .processingEnvironment(processingEnvironment)
                            .build()
                            .validate();
    // handle debug-annotation
    Debug debugAnnotation = eventBusTypeElement.getAnnotation(Debug.class);
    if (!isNull(debugAnnotation)) {
      this.eventBusMetaModel.setHasDebugAnnotation("true");
      this.eventBusMetaModel.setDebugLogLevel(debugAnnotation.logLevel()
                                            .toString());
      if (!isNull(getLogger(debugAnnotation))) {
        this.eventBusMetaModel.setDebugLogger(getLogger(debugAnnotation).getQualifiedName()
                                                       .toString());
      }
    } else {
      this.eventBusMetaModel.setHasDebugAnnotation("false");
      this.eventBusMetaModel.setDebugLogLevel("");
      this.eventBusMetaModel.setDebugLogger("");
    }
    return this.eventBusMetaModel;
  }

  private TypeElement getLogger(Debug debugAnnotation) {
    try {
      debugAnnotation.logger();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;
    TypeElement           eventBusTypeElement;
    EventBusMetaModel     eventBusMetaModel;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public Builder eventBusTypeElement(TypeElement eventBusTypeElement) {
      this.eventBusTypeElement = eventBusTypeElement;
      return this;
    }

    public Builder eventBusMetaModel(EventBusMetaModel eventBusMetaModel) {
      this.eventBusMetaModel = eventBusMetaModel;
      return this;
    }

    public DebugAnnotationScanner build() {
      return new DebugAnnotationScanner(this);
    }
  }
}
