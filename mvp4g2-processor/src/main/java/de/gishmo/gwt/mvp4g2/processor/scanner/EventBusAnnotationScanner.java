package de.gishmo.gwt.mvp4g2.processor.scanner;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventFilter;
import de.gishmo.gwt.mvp4g2.client.eventbus.IsMvp4g2Logger;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Debug;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Filters;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Start;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;

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
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

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

  public EventBusMetaModel scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // First we try to read an already created resource ...
    EventBusMetaModel model = this.restore();
    // Check if we have an element annotated with @Application
    if (!roundEnvironment.getElementsAnnotatedWith(EventBus.class)
                         .isEmpty()) {
      // check, whether we have o do something ...
      validate();
      // should only be one, so we can search for the first! ...
      Optional<? extends Element> optionalElement = this.roundEnvironment.getElementsAnnotatedWith(EventBus.class)
                                                                         .stream()
                                                                         .findFirst();
      if (optionalElement.isPresent()) {
        Element eventBusAnnotationElement = optionalElement.get();
        validate(eventBusAnnotationElement);
        EventBus eventBusAnnotation = eventBusAnnotationElement.getAnnotation(EventBus.class);
        if (!isNull(eventBusAnnotation)) {
          TypeElement shellTypeElement = this.getShellTypeElement(eventBusAnnotation);
          model = new EventBusMetaModel(eventBusAnnotationElement.toString(),
                                        isNull(shellTypeElement) ? "" : shellTypeElement.toString(),
                                        String.valueOf(eventBusAnnotation.historyOnStart()));
          // DEBUG-Annotation
          this.validateDebugAnnotation(eventBusAnnotationElement);
          this.handleDebugAnnotation(eventBusAnnotationElement,
                                     model);
          // Filters-Annotation
          this.validateFiltersAnnotation(eventBusAnnotationElement);
          this.handleFilterAnnotation(eventBusAnnotationElement,
                                      model);


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
      return new EventBusMetaModel(props);
    } catch (IOException e) {
      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
    }
    return null;
  }

  private void validate()
    throws ProcessorException {
    // get elements annotated with EventBus annotation
    Set<? extends Element> elementsWithEventBusAnnotation = this.roundEnvironment.getElementsAnnotatedWith(EventBus.class);
    // at least there should exatly one Application annotation!
    if (elementsWithEventBusAnnotation.size() == 0) {
      throw new ProcessorException("Missing Mvp4g EventBus interface");
    }
    // at least there should only one Application annotation!
    if (elementsWithEventBusAnnotation.size() > 1) {
      throw new ProcessorException("There should be at least only one interface, that is annotated with @EventBus");
    }
  }

  private void validate(Element element)
    throws ProcessorException {
    // get elements annotated with EventBus annotation
    Set<? extends Element> elementsWithEventBusAnnotation = this.roundEnvironment.getElementsAnnotatedWith(EventBus.class);
    // annotated element has to be a interface
    if (element instanceof TypeElement) {
      TypeElement typeElement = (TypeElement) element;
      // Eventbus must be an interface!
      if (!typeElement.getKind()
                      .isInterface()) {
        throw new ProcessorException("@Eventbus can only be used with an interface");
      }
      // EventBus must extends IsEventBus


    } else {
      throw new ProcessorException("@Eventbus can only be used on a type (interface)");
    }
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

  private void validateDebugAnnotation(Element eventBusAnnotationElement)
    throws ProcessorException {
    // get elements annotated with Debug annotation
    Set<? extends Element> elementsWithDebugAnnotation = this.roundEnvironment.getElementsAnnotatedWith(Debug.class);
    // at least there should only one Application annotation!
    if (elementsWithDebugAnnotation.size() > 1) {
      throw new ProcessorException("There should be at least only one interface, that is annotated with @Debug");
    }
    // annotated element has to be a interface
    for (Element element : elementsWithDebugAnnotation) {
      if (element instanceof TypeElement) {
        TypeElement typeElement = (TypeElement) element;
        // @Debug can only be used on a interface
        if (!typeElement.getKind()
                        .isInterface()) {
          throw new ProcessorException("@Debug can only be used with an interface");
        }
        // @Debug can only be used on a interface that extends IsEventBus
        if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                         typeElement.asType(),
                                                         this.processingEnvironment.getElementUtils()
                                                                                   .getTypeElement(IsEventBus.class.getCanonicalName())
                                                                                   .asType())) {
          throw new ProcessorException("@Debug can only be used on interfaces that extends IsEventBus");
        }
        // the loggerinside the annotation must extends IsMvp4g2Logger!
        TypeElement loggerElement = this.getLogger(typeElement.getAnnotation(Debug.class));
        if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                         loggerElement.asType(),
                                                         this.processingEnvironment.getElementUtils()
                                                                                   .getTypeElement(IsMvp4g2Logger.class.getCanonicalName())
                                                                                   .asType())) {
          throw new ProcessorException("@Debug - the logger attribute needs class that extends IsMvp4g2Logger");
        }
      } else {
        throw new ProcessorException("@Debug can only be used on a type (interface)");
      }
    }
  }

  private void handleDebugAnnotation(Element eventBusTypeElement,
                                     EventBusMetaModel model) {
    Debug debugAnnotation = eventBusTypeElement.getAnnotation(Debug.class);
    if (!isNull(debugAnnotation)) {
      model.setHasDebugAnnotation("true");
      model.setDebugLogLevel(debugAnnotation.logLevel()
                                            .toString());
      if (!isNull(getLogger(debugAnnotation))) {
        model.setDebugLogger(getLogger(debugAnnotation).getQualifiedName()
                                                       .toString());
      }
    } else {
      model.setHasDebugAnnotation("false");
      model.setDebugLogLevel("");
      model.setDebugLogger("");
    }
  }

  private void validateFiltersAnnotation(Element eventBusTypeElement)
    throws ProcessorException {
    // get elements annotated with EventBus annotation
    Set<? extends Element> elementsWithFilterAnnotation = this.roundEnvironment.getElementsAnnotatedWith(Filters.class);
    // at least there should only one Application annotation!
    if (elementsWithFilterAnnotation.size() > 1) {
      throw new ProcessorException("There should be at least only one interface, that is annotated with @Filter");
    }
    // annotated element has to be a interface
    for (Element element : elementsWithFilterAnnotation) {
      if (element instanceof TypeElement) {
        TypeElement typeElement = (TypeElement) element;
        if (!typeElement.getKind()
                        .isInterface()) {
          throw new ProcessorException("@Filter can only be used with an interface");
        }
        // @Filter can only be used on a interface that extends IsEventBus
        if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                         typeElement.asType(),
                                                         this.processingEnvironment.getElementUtils()
                                                                                   .getTypeElement(IsEventBus.class.getCanonicalName())
                                                                                   .asType())) {
          throw new ProcessorException("@Filter can only be used on interfaces that extends IsEventBus");
        }
        // test, that all filters implement IsEventFilter!
        List<String> eventFilterAsStringList = this.getEventFiltersAsList((TypeElement) eventBusTypeElement);
        for (String eventFilterClassname : eventFilterAsStringList) {
          TypeElement filterElement = this.processingEnvironment.getElementUtils()
                                                                .getTypeElement(eventFilterClassname);
          if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                           filterElement.asType(),
                                                           this.processingEnvironment.getElementUtils()
                                                                                     .getTypeElement(IsEventFilter.class.getCanonicalName())
                                                                                     .asType())) {
            throw new ProcessorException("@Filter - the filters attribute needs class that implements IsEventFilter");
          }
        }
      } else {
        throw new ProcessorException("@Filter can only be used on a type (interface)");
      }
    }
  }

  private void handleFilterAnnotation(Element eventBusTypeElement,
                                      EventBusMetaModel model) {
    Filters filtersAnnotation = eventBusTypeElement.getAnnotation(Filters.class);
    if (isNull(filtersAnnotation)) {
      model.setHasFiltersAnnotation("false");
    } else {
      model.setHasFiltersAnnotation("true");
      model.setFilters(this.getEventFiltersAsList((TypeElement) eventBusTypeElement));
    }
  }

  private String createRelativeFileName() {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + EventBusAnnotationScanner.EVENTBUS_PROPERTIES;
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

  public List<String> getEventFiltersAsList(TypeElement typeElement) {
    Element filterAnnotation = this.processingEnvironment.getElementUtils()
                                                         .getTypeElement(Filters.class.getName());
    TypeMirror filterAnnotationAsTypeMirror = filterAnnotation.asType();
    return typeElement.getAnnotationMirrors()
                      .stream()
                      .filter(annotationMirror -> annotationMirror.getAnnotationType()
                                                                  .equals(filterAnnotationAsTypeMirror))
                      .flatMap(annotationMirror -> annotationMirror.getElementValues()
                                                                   .entrySet()
                                                                   .stream())
                      .findFirst().<List<String>>map(entry -> Arrays.stream(entry.getValue()
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
                                                                    .collect(Collectors.toList())).orElse(null);
  }

  private void validateShell(TypeElement typeElement)
    throws ProcessorException {

  }

//  private TypeElement getEventBusTypeElement(Application applicationAnnotation) {
//    try {
//      applicationAnnotation.eventBus();
//    } catch (MirroredTypeException exception) {
//      return (TypeElement) this.processingEnvironment.getTypeUtils()
//                                                     .asElement(exception.getTypeMirror());
//    }
//    return null;
//  }
//
//  private TypeElement getApplicationLoaderTypeElement(Application applicationAnnotation) {
//    try {
//      applicationAnnotation.loader();
//    } catch (MirroredTypeException exception) {
//      return (TypeElement) this.processingEnvironment.getTypeUtils()
//                                                     .asElement(exception.getTypeMirror());
//    }
//    return null;
//  }

  private void validateStart(TypeElement typeElement)
    throws ProcessorException {
    // check, if there are more than @Start annotated method
    List<Element> elementsAnnotatedWithStart = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                                         typeElement,
                                                                                                         Start.class);
    if (elementsAnnotatedWithStart.size() > 1) {
      throw new ProcessorException("@Start-annotation can only be used a single time in a eventbus interface");
    } else {
      // check, that the @Start annotation is only used on zero-argument methods!
      if (elementsAnnotatedWithStart.size() == 1) {
        ExecutableElement executableElement = (ExecutableElement) elementsAnnotatedWithStart.get(0);
        if (executableElement.getParameters()
                             .size() > 0) {
          throw new ProcessorException("@Start-annotation can only be used on zero argument methods");
        }
      }
    }
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
