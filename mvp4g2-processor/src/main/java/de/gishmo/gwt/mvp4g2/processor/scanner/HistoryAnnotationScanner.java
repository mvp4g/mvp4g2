package de.gishmo.gwt.mvp4g2.processor.scanner;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import de.gishmo.gwt.mvp4g2.core.history.annotation.History;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.HistoryMetaModel;
import de.gishmo.gwt.mvp4g2.processor.scanner.validation.HistoryAnnotationValidator;

public class HistoryAnnotationScanner {

  private final static String HISTORY_PROPERTIES = "history.properties";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
//  private TypeElement           eventBusTypeElement;
//  private EventBusMetaModel     eventBusMetaModel;

  @SuppressWarnings("unused")
  private HistoryAnnotationScanner(Builder builder) {
    super();
    this.processingEnvironment = builder.processingEnvironment;
//    this.eventBusTypeElement = builder.eventBusTypeElement;
//    this.eventBusMetaModel = builder.eventBusMetaModel;
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

  public HistoryMetaModel scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // First we try to read an already created resource ...
    HistoryMetaModel model = this.restore();
    // Check if we have an element annotated with @Application
    if (!roundEnvironment.getElementsAnnotatedWith(History.class)
                         .isEmpty()) {
      // check annotation ...
      HistoryAnnotationValidator validator = HistoryAnnotationValidator.builder()
                                                                       .roundEnvironment(roundEnvironment)
                                                                       .processingEnvironment(this.processingEnvironment)
                                                                       .build();
      // iterate over all history converters
      for (Element element : roundEnvironment.getElementsAnnotatedWith(History.class)) {
        // validate
        validator.validate(element);
        // process ...
        TypeElement typeElement = (TypeElement) element;
        History historyAnnotation = typeElement.getAnnotation(History.class);
        validator.validate(element);
        model.add(typeElement.getQualifiedName()
                             .toString(),
                  historyAnnotation.type()
                                   .toString());
      }
      // let's store the updated model
      this.processorUtils.store(model,
                                this.createRelativeFileName());
    }
    return model;
  }

  private HistoryMetaModel restore() {
    Properties props = new Properties();
    try {
      FileObject resource = this.processingEnvironment.getFiler()
                                                      .getResource(StandardLocation.CLASS_OUTPUT,
                                                                   "",
                                                                   this.createRelativeFileName());
      props.load(resource.openInputStream());
      return new HistoryMetaModel(props);
    } catch (IOException e) {
      // every thing is ok -> no operation
//      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
    }
    return new HistoryMetaModel();
  }

  private String createRelativeFileName() {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + HistoryAnnotationScanner.HISTORY_PROPERTIES;
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public HistoryAnnotationScanner build() {
      return new HistoryAnnotationScanner(this);
    }
  }
}
