package de.gishmo.gwt.mvp4g2.processor.handler.eventhandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import de.gishmo.gwt.mvp4g2.client.internal.ui.EventHandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.EventHandler;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.handler.eventhandler.validation.EventHandlerAnnotationValidator;

// TODO check, that @Eventhandler is annoted at a class that extends AbstractEventHandler!
public class EventHandlerAnnotationHandler {

  private final static String IMPL_NAME = "MetaData";

  private ProcessorUtils processorUtils;

  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;
  private PresenterUtils        presenterUtils;

  @SuppressWarnings("unused")
  private EventHandlerAnnotationHandler(Builder builder) {
    super();

    this.processingEnvironment = builder.processingEnvironment;
    this.roundEnvironment = builder.roundEnvironment;

    setUp();
  }

  private void setUp() {
    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
    this.presenterUtils = PresenterUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void process()
    throws ProcessorException, IOException {
    // check, whether we have o do something ...
    if (roundEnvironment.getElementsAnnotatedWith(EventHandler.class)
                        .size() == 0) {
      return;
    }
    // valildate @Application annotation
    EventHandlerAnnotationValidator eventHandlerAnnotationValidator = EventHandlerAnnotationValidator.builder()
                                                                                                     .processingEnvironment(this.processingEnvironment)
                                                                                                     .roundEnvironment(this.roundEnvironment)
                                                                                                     .build();
    // valildate @Application annotation
    eventHandlerAnnotationValidator.validate();
    Map<String, List<String>> eventHandlingMethods = new HashMap<>();
    // validate and generate
    for (Element element : this.roundEnvironment.getElementsAnnotatedWith(EventHandler.class)) {
      eventHandlerAnnotationValidator.validate(element);
      this.generate(element);
      // handlng the annotated event handling metohds
      this.presenterUtils.createMetaFile((TypeElement) element,
                                         false);
    }
  }

  private void generate(Element element)
    throws IOException {
    EventHandler presenter   = element.getAnnotation(EventHandler.class);
    TypeElement  typeElement = (TypeElement) element;

    String className = this.processorUtils.createFullClassName(this.processorUtils.getPackageAsString(element),
                                                               element.getSimpleName()
                                                                      .toString()) + EventHandlerAnnotationHandler.IMPL_NAME;
    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.processorUtils.setFirstCharacterToUpperCase(className))
                                        .superclass(ParameterizedTypeName.get(ClassName.get(EventHandlerMetaData.class),
                                                                              ClassName.get(this.processorUtils.getPackageAsString(typeElement),
                                                                                            typeElement.getSimpleName()
                                                                                                       .toString())))
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL);

    // constructor ...
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                               .addModifiers(Modifier.PUBLIC)
                                               .addStatement("super($S, $T.EVENT_HANDLER, new $T())",
                                                             this.processorUtils.getCanonicalClassName(typeElement),
                                                             ClassName.get(EventHandlerMetaData.Kind.class),
                                                             ClassName.get(this.processorUtils.getPackageAsString(typeElement),
                                                                           typeElement.getSimpleName()
                                                                                      .toString()));
    typeSpec.addMethod(constructor.build());
    JavaFile javaFile = JavaFile.builder(this.processorUtils.getPackageAsString(element),
                                         typeSpec.build())
                                .build();
    javaFile.writeTo(this.processingEnvironment.getFiler());
    //    System.out.println(javaFile.toString());
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

    public EventHandlerAnnotationHandler build() {
      return new EventHandlerAnnotationHandler(this);
    }

  }
}
