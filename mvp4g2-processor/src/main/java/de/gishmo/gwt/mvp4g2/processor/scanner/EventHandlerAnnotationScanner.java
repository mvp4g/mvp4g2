package de.gishmo.gwt.mvp4g2.processor.scanner;

import de.gishmo.gwt.mvp4g2.client.ui.AbstractEventHandler;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.EventHandler;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventHandlerModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Properties;

public class EventHandlerAnnotationScanner {

  private final static String EVENT_HANDLER_PROPERTIES  = "eventHandler.properties";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private EventHandlerAnnotationScanner(Builder builder) {
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

  public EventHandlerModel scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // read all already created model
    EventHandlerModel model = this.restore();
    for (Element element : roundEnvironment.getElementsAnnotatedWith(EventHandler.class)) {
      TypeElement typeElement = (TypeElement) element;
      // validate the element. In case of error throw exception!
      validate(typeElement);
      // update model
      model.add(((TypeElement) element).getQualifiedName().toString(),
                this.processorUtils.createHandledEventArray(typeElement));
      // let's store the updated model
      this.processorUtils.store(model,
                                this.createRelativeFileName());
    }
    return model;
  }

  private EventHandlerModel restore() {
    Properties props = new Properties();
    try {
      FileObject resource = this.processingEnvironment.getFiler()
                                                      .getResource(StandardLocation.CLASS_OUTPUT,
                                                                   "",
                                                                   this.createRelativeFileName());
      props.load(resource.openInputStream());
      return new EventHandlerModel(props);
    } catch (IOException e) {
      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
    }
    return new EventHandlerModel();
  }


  private void validate(Element element)
    throws ProcessorException {
    if (element instanceof TypeElement) {
      TypeElement typeElement = (TypeElement) element;
      // check, that the presenter annotion is only used with classes
      if (!typeElement.getKind()
                      .isClass()) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @EventHandler can only be used with as class!");
      }
      // check, that the typeElement extends AbstarctEventHandler
      if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                       typeElement.asType(),
                                                       this.processingEnvironment.getElementUtils()
                                                                                 .getTypeElement(AbstractEventHandler.class.getCanonicalName())
                                                                                 .asType())) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @EventHandler must extend AbstractEventHandler.class!");
      }
      // check if annotated class is abstract
      if (typeElement.getModifiers()
                     .contains(Modifier.ABSTRACT)) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @EventHandler can not be ABSTRACT");
      }
    } else {
      throw new ProcessorException("@EventHandler can only be used on a type (class)");
    }
  }

  private String createRelativeFileName() {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + EventHandlerAnnotationScanner.EVENT_HANDLER_PROPERTIES;
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public EventHandlerAnnotationScanner build() {
      return new EventHandlerAnnotationScanner(this);
    }
  }
}
