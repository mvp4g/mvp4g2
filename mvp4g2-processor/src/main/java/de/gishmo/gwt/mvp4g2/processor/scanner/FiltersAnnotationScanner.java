package de.gishmo.gwt.mvp4g2.processor.scanner;

import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Filters;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.EventBusMetaModel;
import de.gishmo.gwt.mvp4g2.processor.scanner.validation.FilterAnnotationValidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class FiltersAnnotationScanner {

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;
  private TypeElement           eventBusTypeElement;
  private EventBusMetaModel     eventBusMetaModel;

  @SuppressWarnings("unused")
  private FiltersAnnotationScanner(Builder builder) {
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
    // do validation
    FilterAnnotationValidator.builder()
                             .roundEnvironment(roundEnvironment)
                             .processingEnvironment(processingEnvironment)
                             .eventBusTypeElement(this.eventBusTypeElement)
                             .build()
                             .validate();
    // handle filters-annotation
    Filters filtersAnnotation = eventBusTypeElement.getAnnotation(Filters.class);
    if (isNull(filtersAnnotation)) {
      this.eventBusMetaModel.setHasFiltersAnnotation("false");
    } else {
      this.eventBusMetaModel.setHasFiltersAnnotation("true");
      this.eventBusMetaModel.setFilters(this.getEventFiltersAsList((TypeElement) eventBusTypeElement));
    }
    return this.eventBusMetaModel;
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

    public FiltersAnnotationScanner build() {
      return new FiltersAnnotationScanner(this);
    }
  }
}
