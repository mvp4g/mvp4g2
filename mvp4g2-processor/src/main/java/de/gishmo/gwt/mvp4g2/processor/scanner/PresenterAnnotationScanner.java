package de.gishmo.gwt.mvp4g2.processor.scanner;

import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterMetaModel;
import de.gishmo.gwt.mvp4g2.processor.scanner.validation.PresenterAnnotationValidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Properties;

public class PresenterAnnotationScanner {

  private final static String PRESENTER_PROPERTIES = "presenter.properties";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private PresenterAnnotationScanner(Builder builder) {
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

  public PresenterMetaModel scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // Validator
    PresenterAnnotationValidator validator = PresenterAnnotationValidator.builder()
                                                                         .processingEnvironment(processingEnvironment)
                                                                         .build();
    // read all already created model
    PresenterMetaModel model = this.restore();
    // iterate over Presenter
    for (Element element : roundEnvironment.getElementsAnnotatedWith(Presenter.class)) {
      TypeElement typeElement = (TypeElement) element;
      // validate
      validator.validate(typeElement,
                         this.getViewClassTypeElement(typeElement.getAnnotation(Presenter.class)),
                         this.getViewInterfaceTypeElement(typeElement.getAnnotation(Presenter.class)));
      // update model
      model.add(((TypeElement) element).getQualifiedName()
                                       .toString(),
                typeElement.getAnnotation(Presenter.class)
                           .multiple() ? "true" : "false",
                this.getViewClassTypeElement(element.getAnnotation(Presenter.class))
                    .getQualifiedName()
                    .toString(),
                this.getViewInterfaceTypeElement(element.getAnnotation(Presenter.class))
                    .getQualifiedName()
                    .toString(),
                typeElement.getAnnotation(Presenter.class)
                           .viewCreator()
                           .toString(),
                this.processorUtils.createHandledEventArray(typeElement));
    }
    // let's store the updated model
    this.processorUtils.store(model,
                              this.createRelativeFileName());
    return model;
  }

  private PresenterMetaModel restore() {
    Properties props = new Properties();
    try {
      FileObject resource = this.processingEnvironment.getFiler()
                                                      .getResource(StandardLocation.CLASS_OUTPUT,
                                                                   "",
                                                                   this.createRelativeFileName());
      props.load(resource.openInputStream());
      return new PresenterMetaModel(props);
    } catch (IOException e) {
      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
    }
    return new PresenterMetaModel();
  }

  private TypeElement getViewClassTypeElement(Presenter presenterAnnotation) {
    try {
      presenterAnnotation.viewClass();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private TypeElement getViewInterfaceTypeElement(Presenter presenterAnnotation) {
    try {
      presenterAnnotation.viewInterface();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private String createRelativeFileName() {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + PresenterAnnotationScanner.PRESENTER_PROPERTIES;
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public PresenterAnnotationScanner build() {
      return new PresenterAnnotationScanner(this);
    }
  }
}
