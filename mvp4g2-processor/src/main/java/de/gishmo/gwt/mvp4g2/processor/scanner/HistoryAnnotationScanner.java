package de.gishmo.gwt.mvp4g2.processor.scanner;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.client.history.annotation.History;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.ApplicationMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.HistoryMetaModel;
import de.gishmo.gwt.mvp4g2.processor.scanner.validation.ApplicationAnnotationValidator;
import de.gishmo.gwt.mvp4g2.processor.scanner.validation.DebugAnnotationValidator;

import static java.util.Objects.isNull;

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

  public void scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // First we try to read an already created resource ...
    HistoryMetaModel model = this.restore();
    // Check if we have an element annotated with @Application
    if (!roundEnvironment.getElementsAnnotatedWith(History.class)
                         .isEmpty()) {
//      // check, whether we have o do something ...
//      ApplicationAnnotationValidator validator = ApplicationAnnotationValidator.builder()
//                                                                               .roundEnvironment(roundEnvironment)
//                                                                               .processingEnvironment(this.processingEnvironment)
//                                                                               .build();
//      validator.validate();
//      // should only be one, so we can search for the first! ...
//      Optional<? extends Element> optionalElement = this.roundEnvironment.getElementsAnnotatedWith(Application.class)
//                                                                         .stream()
//                                                                         .findFirst();
//      if (optionalElement.isPresent()) {
//        Element applicationAnnotationElement = optionalElement.get();
//        validator.validate(applicationAnnotationElement);
//        Application applicationAnnotation = applicationAnnotationElement.getAnnotation(Application.class);
//        if (!isNull(applicationAnnotation)) {
//          TypeElement eventBusTypeElement = this.getEventBusTypeElement(applicationAnnotation);
//          TypeElement applicationLoaderTypeElement = this.getApplicationLoaderTypeElement(applicationAnnotation);
//          model = new ApplicationMetaModel(applicationAnnotationElement.toString(),
//                                           isNull(eventBusTypeElement) ? "" : eventBusTypeElement.toString(),
//                                           isNull(applicationLoaderTypeElement) ? "" : applicationLoaderTypeElement.toString());
          // let's store the updated model
          this.processorUtils.store(model,
                                    this.createRelativeFileName());
//        }
//      }
    }
  }
//
//  private TypeElement getLogger(Debug debugAnnotation) {
//    try {
//      debugAnnotation.logger();
//    } catch (MirroredTypeException exception) {
//      return (TypeElement) this.processingEnvironment.getTypeUtils()
//                                                     .asElement(exception.getTypeMirror());
//    }
//    return null;
//  }

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
      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
    }
    return null;
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
