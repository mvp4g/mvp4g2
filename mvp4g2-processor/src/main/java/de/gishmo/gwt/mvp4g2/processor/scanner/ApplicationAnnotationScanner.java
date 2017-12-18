package de.gishmo.gwt.mvp4g2.processor.scanner;

import de.gishmo.gwt.mvp4g2.client.application.IsApplication;
import de.gishmo.gwt.mvp4g2.client.application.annotation.Application;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.ApplicationMetaModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import static java.util.Objects.isNull;

public class ApplicationAnnotationScanner {

  private final static String APPLICATION_PROPERTIES  = "application.properties";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;

  @SuppressWarnings("unused")
  private ApplicationAnnotationScanner(Builder builder) {
    super();
    this.processingEnvironment = builder.processingEnvironment;
    this.roundEnvironment = builder.roundEnvironment;
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

  public ApplicationMetaModel scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // First we try to read an already created resource ...
    ApplicationMetaModel model = this.restore();
    // Check if we have an element annotated with @Application
    if (!roundEnvironment.getElementsAnnotatedWith(Application.class)
                         .isEmpty()) {
      // check, whether we have o do something ...
      validate();
      // should only be one, so we can search for the first! ...
      Optional<? extends Element> optionalElement = this.roundEnvironment.getElementsAnnotatedWith(Application.class)
                                                                         .stream()
                                                                         .findFirst();
      if (optionalElement.isPresent()) {
        Element applicationAnnotationElement = optionalElement.get();
        validate(applicationAnnotationElement);
        Application applicationAnnotation = applicationAnnotationElement.getAnnotation(Application.class);
        if (!isNull(applicationAnnotation)) {
          TypeElement eventBusTypeElement = this.getEventBusTypeElement(applicationAnnotation);
          TypeElement applicationLoaderTypeElement = this.getApplicationLoaderTypeElement(applicationAnnotation);
          model = new ApplicationMetaModel(applicationAnnotationElement.toString(),
                                       isNull(eventBusTypeElement) ? "" : eventBusTypeElement.toString(),
                                       isNull(applicationLoaderTypeElement) ? "" : applicationLoaderTypeElement.toString());
          // let's store the updated model
          this.processorUtils.store(model,
                                    this.createRelativeFileName());
        }
      }
    }
    return model;
  }

  private ApplicationMetaModel restore() {
    Properties props = new Properties();
    try {
      FileObject resource = this.processingEnvironment.getFiler()
                                                      .getResource(StandardLocation.CLASS_OUTPUT,
                                                                   "",
                                                                   this.createRelativeFileName());
      props.load(resource.openInputStream());
      return new ApplicationMetaModel(props);
    } catch (IOException e) {
      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
    }
    return null;
  }

  private void validate()
    throws ProcessorException {
    // get elements annotated with Application annotation
    Set<? extends Element> elementsWithApplicaitonAnnotation = this.roundEnvironment.getElementsAnnotatedWith(Application.class);
    // at least there should exatly one Application annotation!
    if (elementsWithApplicaitonAnnotation.size() == 0) {
      throw new ProcessorException("Missing Mvp4g Application interface");
    }
    // at least there should only one Application annotation!
    if (elementsWithApplicaitonAnnotation.size() > 1) {
      throw new ProcessorException("There should be at least only one interface, that is annotated with @Application");
    }
  }

  private void validate(Element element)
    throws ProcessorException {
    if (element instanceof TypeElement) {
      TypeElement typeElement = (TypeElement) element;
      // annotated element has to be a interface
      if (!typeElement.getKind()
                      .isInterface()) {
        throw new ProcessorException("@Application annotated must be used with an interface");
      }
      // check, that the typeElement implements IsApplication
      if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                       typeElement.asType(),
                                                       this.processingEnvironment.getElementUtils()
                                                                                 .getTypeElement(IsApplication.class.getCanonicalName())
                                                                                 .asType())) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @Application must implement IsApplication interface");
      }
    } else {
      throw new ProcessorException("@Application can only be used on a type (interface)");
    }
  }

  private TypeElement getEventBusTypeElement(Application applicationAnnotation) {
    try {
      applicationAnnotation.eventBus();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private TypeElement getApplicationLoaderTypeElement(Application applicationAnnotation) {
    try {
      applicationAnnotation.loader();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private String createRelativeFileName() {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + ApplicationAnnotationScanner.APPLICATION_PROPERTIES;
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;
    RoundEnvironment      roundEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public Builder roundEnvironment(RoundEnvironment roundEnvironment) {
      this.roundEnvironment = roundEnvironment;
      return this;
    }

    public ApplicationAnnotationScanner build() {
      return new ApplicationAnnotationScanner(this);
    }
  }
}
