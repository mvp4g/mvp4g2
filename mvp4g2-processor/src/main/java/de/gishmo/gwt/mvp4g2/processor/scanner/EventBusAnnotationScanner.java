package de.gishmo.gwt.mvp4g2.processor.scanner;

import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.EventMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterMetaModel;
import de.gishmo.gwt.mvp4g2.processor.scanner.validation.EventBusAnnotationValidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static java.util.Objects.isNull;

public class EventBusAnnotationScanner {

  private final static String EVENTBUS_PROPERTIES = "eventBus.properties";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;

  @SuppressWarnings("unused")
  private EventBusAnnotationScanner(Builder builder) {
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

  public EventBusMetaModel scan(RoundEnvironment roundEnvironment,
                                PresenterMetaModel presenterMetaModel)
    throws ProcessorException {
    // First we try to read an already created resource ...
    EventBusMetaModel model = this.restore();
    // Check if we have an element annotated with @Application
    if (!roundEnvironment.getElementsAnnotatedWith(EventBus.class)
                         .isEmpty()) {
      // create validator
      EventBusAnnotationValidator evengtbusValidaitor = EventBusAnnotationValidator.builder()
                                                                                   .roundEnvironment(roundEnvironment)
                                                                                   .processingEnvironment(processingEnvironment)
                                                                                   .build();
      // check, whether we have o do something ...
      evengtbusValidaitor.validate();
      // should only be one, so we can search for the first! ...
      Optional<? extends Element> optionalElement = this.roundEnvironment.getElementsAnnotatedWith(EventBus.class)
                                                                         .stream()
                                                                         .findFirst();
      if (optionalElement.isPresent()) {
        Element eventBusAnnotationElement = optionalElement.get();
        evengtbusValidaitor.validate(eventBusAnnotationElement);
        EventBus eventBusAnnotation = eventBusAnnotationElement.getAnnotation(EventBus.class);
        if (!isNull(eventBusAnnotation)) {
          TypeElement shellTypeElement = this.getShellTypeElement(eventBusAnnotation);
          model = new EventBusMetaModel(eventBusAnnotationElement.toString(),
                                        isNull(shellTypeElement) ? "" : shellTypeElement.toString(),
                                        String.valueOf(eventBusAnnotation.historyOnStart()));
          // Debug-Annotation
          model = DebugAnnotationScanner.builder()
                                        .processingEnvironment(processingEnvironment)
                                        .eventBusTypeElement((TypeElement) eventBusAnnotationElement)
                                        .eventBusMetaModel(model)
                                        .build()
                                        .scan(roundEnvironment);
          // Filters-Annotation
          model = FiltersAnnotationScanner.builder()
                                          .processingEnvironment(processingEnvironment)
                                          .eventBusTypeElement((TypeElement) eventBusAnnotationElement)
                                          .eventBusMetaModel(model)
                                          .build()
                                          .scan(roundEnvironment);
          // handle Event-annotation
          model = EventAnnotationScanner.builder()
                                        .processingEnvironment(processingEnvironment)
                                        .eventBusTypeElement((TypeElement) eventBusAnnotationElement)
                                        .eventBusMetaModel(model)
                                        .build()
                                        .scan(roundEnvironment);

          // let's store the updated model
          this.processorUtils.store(model,
                                    this.createRelativeFileName());
        }
      }
    }
    return model;
  }

  private EventBusMetaModel restore() {
    Properties props = new Properties();
    try {
      FileObject resource = this.processingEnvironment.getFiler()
                                                      .getResource(StandardLocation.CLASS_OUTPUT,
                                                                   "",
                                                                   this.createRelativeFileName());
      props.load(resource.openInputStream());
      EventBusMetaModel    model       = new EventBusMetaModel(props);
      List<EventMetaModel> eventModels = new ArrayList<>();
      for (String eventInternalName : model.getEvents()) {
        FileObject resourceEvent = this.processingEnvironment.getFiler()
                                                             .getResource(StandardLocation.CLASS_OUTPUT,
                                                                          "",
                                                                          this.createRelativeEventModelFileName(eventInternalName));
        props.load(resource.openInputStream());
        eventModels.add(new EventMetaModel(props));
      }
      eventModels.stream()
                 .forEach(m -> model.add(m));
      return model;
    } catch (IOException e) {
      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
    }
    return null;
  }

  private TypeElement getShellTypeElement(EventBus eventBusAnnotation) {
    try {
      eventBusAnnotation.shell();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private String createRelativeFileName() {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + EventBusAnnotationScanner.EVENTBUS_PROPERTIES;
  }

  private String createRelativeEventModelFileName(String eventInternalName) {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + eventInternalName + ProcessorConstants.PROPERTIES_POSTFIX;
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

    public EventBusAnnotationScanner build() {
      return new EventBusAnnotationScanner(this);
    }
  }
}
