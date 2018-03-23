package de.gishmo.gwt.mvp4g2.processor.scanner;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import de.gishmo.gwt.mvp4g2.core.ui.annotation.Handler;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.HandlerMetaModel;
import de.gishmo.gwt.mvp4g2.processor.scanner.validation.HandlerAnnotationValidator;

public class HandlerAnnotationScanner {

  private final static String HANDLER_PROPERTIES = "handler.properties";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private HandlerAnnotationScanner(Builder builder) {
    super();
    this.processingEnvironment = builder.processingEnvironment;
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

  public HandlerMetaModel scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // Validator
    HandlerAnnotationValidator validator = HandlerAnnotationValidator.builder()
                                                                     .roundEnvironment(roundEnvironment)
                                                                     .processingEnvironment(processingEnvironment)
                                                                     .build();
    // read all already created model
    HandlerMetaModel model = this.restore();
    for (Element element : roundEnvironment.getElementsAnnotatedWith(Handler.class)) {
      TypeElement typeElement = (TypeElement) element;
      // validate the element. In case of error throw exception!
      validator.validate(typeElement);
      // update model
      model.add(((TypeElement) element).getQualifiedName()
                                       .toString(),
                this.processorUtils.createHandledEventArray(typeElement));
    }
    // let's store the updated model
    this.processorUtils.store(model,
                              this.createRelativeFileName());
    return model;
  }

  private HandlerMetaModel restore() {
    Properties props = new Properties();
    try {
      FileObject resource = this.processingEnvironment.getFiler()
                                                      .getResource(StandardLocation.CLASS_OUTPUT,
                                                                   "",
                                                                   this.createRelativeFileName());
      props.load(resource.openInputStream());
      return new HandlerMetaModel(props);
    } catch (IOException e) {
      // every thing is ok -> no operation
//      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
    }
    return new HandlerMetaModel();
  }

  private String createRelativeFileName() {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + HandlerAnnotationScanner.HANDLER_PROPERTIES;
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public HandlerAnnotationScanner build() {
      return new HandlerAnnotationScanner(this);
    }
  }
}
