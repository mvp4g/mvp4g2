package de.gishmo.gwt.mvp4g2.processor.scanner;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.scanner.validation.EventAnnotationValidator;

public class EventAnnotationScanner {
  //
  //  private final static String EVENT_HANDLER_PROPERTIES = "eventHandler.properties";

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
    for (Element element : roundEnvironment.getElementsAnnotatedWith(Event.class)) {
    // do validation
    EventAnnotationValidator.builder()
                            .roundEnvironment(roundEnvironment)
                            .processingEnvironment(processingEnvironment)
                            .eventBusTypeElement(eventBusTypeElement)
                            .eventElement(element)
                            .build()
                            .validate();

    }


    // handle event-annotation

    return this.eventBusMetaModel;
  }

  //
  //  private EventHandlerMetaModel restore() {
  //    Properties props = new Properties();
  //    try {
  //      FileObject resource = this.processingEnvironment.getFiler()
  //                                                      .getResource(StandardLocation.CLASS_OUTPUT,
  //                                                                   "",
  //                                                                   this.createRelativeFileName());
  //      props.load(resource.openInputStream());
  //      return new EventHandlerMetaModel(props);
  //    } catch (IOException e) {
  //      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
  //    }
  //    return new EventHandlerMetaModel();
  //  }
  //
  //  private String createRelativeFileName() {
  //    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + EventAnnotationScanner.EVENT_HANDLER_PROPERTIES;
  //  }

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
