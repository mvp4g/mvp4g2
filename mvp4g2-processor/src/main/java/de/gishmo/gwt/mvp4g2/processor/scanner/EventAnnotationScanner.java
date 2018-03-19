package de.gishmo.gwt.mvp4g2.processor.scanner;

import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.core.eventbus.annotation.Start;
import de.gishmo.gwt.mvp4g2.core.history.annotation.InitHistory;
import de.gishmo.gwt.mvp4g2.core.history.annotation.NotFoundHistory;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.model.EventMetaModel;
import de.gishmo.gwt.mvp4g2.processor.scanner.validation.EventAnnotationValidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class EventAnnotationScanner {

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private TypeElement           eventBusTypeElement;
  private EventBusMetaModel     eventBusMetaModel;

  @SuppressWarnings("unused")
  private EventAnnotationScanner(Builder builder) {
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
    EventAnnotationValidator eventAnnotationValidator = EventAnnotationValidator.builder()
                                                                                .roundEnvironment(roundEnvironment)
                                                                                .processingEnvironment(processingEnvironment)
                                                                                .eventBusMetaModel(this.eventBusMetaModel)
                                                                                .eventBusTypeElement(eventBusTypeElement)
                                                                                .build();
    // validate event bus
    eventAnnotationValidator.validate();
    // handle events
    for (Element element : roundEnvironment.getElementsAnnotatedWith(Event.class)) {
      // do validation
      EventAnnotationValidator.builder()
                              .roundEnvironment(roundEnvironment)
                              .processingEnvironment(processingEnvironment)
                              .eventBusMetaModel(this.eventBusMetaModel)
                              .eventBusTypeElement(eventBusTypeElement)
                              .build()
                              .validate(element);
      ExecutableElement executableElement = (ExecutableElement) element;
      // restore meta data (if there is one)
      // First we try to read an already created resource ...
      EventMetaModel model = this.restore(element);
      // get event annoation
      Event eventAnnotation = element.getAnnotation(Event.class);
      // scan for data
      // internal event name
      model.setEventInternalName(this.processorUtils.createInternalEventName(executableElement));
      // event name
      model.setEventName(executableElement.getSimpleName()
                                          .toString());
      // history name of hte event
      model.setHistoryEventName(eventAnnotation.historyName());
      // navigation event
      model.setNavigationEvent(Boolean.toString(eventAnnotation.navigationEvent()));
      // passive
      model.setPassive(Boolean.toString(eventAnnotation.passive()));
      // handlers
      model.setHandlers(this.getElementsFromAnnotationAsList(executableElement,
                                                             "handlers"));
      // List of binding handlers (full class names as String)
      model.setBindings(this.getElementsFromAnnotationAsList(executableElement,
                                                             "bind"));
      // List of activate handlers (full class names as String)
      model.setActivateHandlers(this.getElementsFromAnnotationAsList(executableElement,
                                                                     "activate"));
      // List of deactivate handlers (full class names as String)
      model.setDeactivateHandlers(this.getElementsFromAnnotationAsList(executableElement,
                                                                       "deactivate"));
      // history converter
      model.setHistoryConverter(Objects.requireNonNull(this.getHistoryConverterTypeElement(eventAnnotation))
                                       .getQualifiedName()
                                       .toString());
      // start event?
      model.setStartEvent(isNull(executableElement.getAnnotation(Start.class)) ? "false" : "true");
      // initHistory event?
      model.setInitHistory(isNull(executableElement.getAnnotation(InitHistory.class)) ? "false" : "true");
      // start event?
      model.setNotFoundHistory(isNull(executableElement.getAnnotation(NotFoundHistory.class)) ? "false" : "true");
      // parameters
      executableElement.getParameters()
                       .forEach(v -> model.addParameter(v.getSimpleName()
                                                         .toString(),
                                                        v.asType()
                                                         .toString()));
      // let's store the updated model
      this.processorUtils.store(model,
                                this.createRelativeFileName(element));
      // store the event meta data model in the eventbus meta data model
      this.eventBusMetaModel.add(model);
    }
    return this.eventBusMetaModel;
  }

  private EventMetaModel restore(Element element) {
    Properties props = new Properties();
    try {
      FileObject resource = this.processingEnvironment.getFiler()
                                                      .getResource(StandardLocation.CLASS_OUTPUT,
                                                                   "",
                                                                   this.createRelativeFileName(element));
      props.load(resource.openInputStream());
      return new EventMetaModel(props);
    } catch (IOException e) {
      // every thing is ok -> no operation
//      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName(element) + "<<");
    }
    return new EventMetaModel();
  }

  private List<String> getElementsFromAnnotationAsList(ExecutableElement executableElement,
                                                       String attribute) {
    Element eventAnnotation = this.processingEnvironment.getElementUtils()
                                                        .getTypeElement(Event.class.getName());
    TypeMirror eventAnnotationAsTypeMirror = eventAnnotation.asType();
    return executableElement.getAnnotationMirrors()
                            .stream()
                            .filter(annotationMirror -> annotationMirror.getAnnotationType()
                                                                        .equals(eventAnnotationAsTypeMirror))
                            .flatMap(annotationMirror -> annotationMirror.getElementValues()
                                                                         .entrySet()
                                                                         .stream())
                            .filter(entry -> attribute.equals(entry.getKey()
                                                                   .getSimpleName()
                                                                   .toString()))
                            .findFirst()
                            .map(entry -> Arrays.stream(entry.getValue()
                                                             .toString()
                                                             .replace("{",
                                                                      "")
                                                             .replace("}",
                                                                      "")
                                                             .replace(" ",
                                                                      "")
                                                             .split(","))
                                                .map((v) -> v.substring(0,
                                                                        v.indexOf(".class")))
                                                .collect(Collectors.toList()))
                            .orElse(null);
  }

  private TypeElement getHistoryConverterTypeElement(Event eventAnnotation) {
    try {
      eventAnnotation.historyConverter();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private String createRelativeFileName(Element element) {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + this.processorUtils.createInternalEventName((ExecutableElement) element) + ProcessorConstants.PROPERTIES_POSTFIX;
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

    public EventAnnotationScanner build() {
      return new EventAnnotationScanner(this);
    }
  }
}
