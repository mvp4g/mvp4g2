package de.gishmo.gwt.mvp4g2.processor.handler.eventbus;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.eventbus.AbstractEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.handler.eventbus.generator.DebugAnnotationGenerator;
import de.gishmo.gwt.mvp4g2.processor.handler.eventbus.generator.EventAnnotationMetaDataGenerator;
import de.gishmo.gwt.mvp4g2.processor.handler.eventbus.generator.EventHandlerRegristrationGenerator;
import de.gishmo.gwt.mvp4g2.processor.handler.eventbus.generator.EventHandlingMethodGenerator;
import de.gishmo.gwt.mvp4g2.processor.handler.eventbus.generator.FilterAnnotationGenerator;
import de.gishmo.gwt.mvp4g2.processor.handler.eventbus.validation.ShellAttributeValidator;
import de.gishmo.gwt.mvp4g2.processor.handler.eventbus.validation.StartAnnotationValidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import java.io.IOException;
import java.util.Set;

// TODO Start annotation validation!
// TODO Filter annotation
public class EventBusAnnotationHandler {

  private ProcessorUtils processorUtils;

  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment      roundEnvironment;

  @SuppressWarnings("unused")
  private EventBusAnnotationHandler(Builder builder) {
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

  public void process()
    throws ProcessorException, IOException {
    // check, whether we have o do something ...
    if (roundEnvironment.getElementsAnnotatedWith(EventBus.class)
                        .size() == 0) {
      return;
    }
    // valildate @Application annotation
    this.validateEventBus();
    // generateEventbus code
    for (Element element : this.roundEnvironment.getElementsAnnotatedWith(EventBus.class)) {
      // cast element -> typeElement
      TypeElement eventBusTypeElement = (TypeElement) element;

      // create TypeSpec
      TypeSpec.Builder typeSpec = this.createTypeSpecEventBus(eventBusTypeElement);
      typeSpec = this.createConstructorForEventBusClass(typeSpec,
                                                        eventBusTypeElement);

      // handling Debug annotation ...
      DebugAnnotationGenerator.builder()
                              .roundEnvironment(this.roundEnvironment)
                              .processingEnvironment(this.processingEnvironment)
                              .eventBusTypeElement(eventBusTypeElement)
                              .typeSpec(typeSpec)
                              .build()
                              .generate();
      // handling Filter annotation ...
      FilterAnnotationGenerator.builder()
                               .roundEnvironment(this.roundEnvironment)
                               .processingEnvironment(this.processingEnvironment)
                               .eventBusTypeElement(eventBusTypeElement)
                               .typeSpec(typeSpec)
                               .build()
                               .generate();
      // handling events!
      EventAnnotationMetaDataGenerator.builder()
                                      .processingEnvironment(this.processingEnvironment)
                                      .eventBusTypeElement(eventBusTypeElement)
                                      .typeSpec(typeSpec)
                                      .build()
                                      .generate();
      // handling eventbus!
      EventHandlerRegristrationGenerator.builder()
                                        .roundEnvironment(this.roundEnvironment)
                                        .processingEnvironment(this.processingEnvironment)
                                        .eventBusTypeElement(eventBusTypeElement)
                                        .typeSpec(typeSpec)
                                        .build()
                                        .generate();
      // handling events in eventbus!
      EventHandlingMethodGenerator.builder()
                                  .roundEnvironment(this.roundEnvironment)
                                  .processingEnvironment(this.processingEnvironment)
                                  .eventBusTypeElement(eventBusTypeElement)
                                  .typeSpec(typeSpec)
                                  .build()
                                  .generate();

      // write eventBus
      JavaFile javaFile = JavaFile.builder(this.processorUtils.getPackageAsString(eventBusTypeElement),
                                           typeSpec.build())
                                  .build();
      System.out.println(javaFile.toString());

      javaFile.writeTo(this.processingEnvironment.getFiler());
    }
  }

  private void validateEventBus()
    throws ProcessorException {
    // create validator instances
    ShellAttributeValidator shellAttributeValidator = ShellAttributeValidator.builder()
                                                                             .processingEnvironment(this.processingEnvironment)
                                                                             .roundEnvironment(this.roundEnvironment)
                                                                             .build();
    StartAnnotationValidator startAnnotationValidator = StartAnnotationValidator.builder()
                                                                                .processingEnvironment(this.processingEnvironment)
                                                                                .roundEnvironment(this.roundEnvironment)
                                                                                .build();


    // TODO

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
    // annotated element has to be a interface
    for (Element element : elementsWithEventBusAnnotation) {
      if (element instanceof TypeElement) {
        TypeElement typeElement = (TypeElement) element;
        // Eventbus must be an interface!
        if (!typeElement.getKind()
                        .isInterface()) {
          throw new ProcessorException("@Eventbus can only be used with an interface");
        }
        // EventBus must extends IsEventBus

        // Eventbus: validate shellPresenter
        shellAttributeValidator.validate(typeElement);

        // validate @Start-annotation
        startAnnotationValidator.validate(typeElement);
      } else {
        throw new ProcessorException("@Eventbus can only be used on a type (interface)");
      }
    }


  }

  private TypeSpec.Builder createTypeSpecEventBus(TypeElement eventBusTypeElement) {
    return TypeSpec.classBuilder(eventBusTypeElement.getSimpleName() + ProcessorConstants.IMPL_NAME)
                   .superclass(ParameterizedTypeName.get(ClassName.get(AbstractEventBus.class),
                                                         ClassName.get(this.processorUtils.getPackageAsString(eventBusTypeElement),
                                                                       eventBusTypeElement.getSimpleName()
                                                                                          .toString())))
                   .addModifiers(Modifier.PUBLIC,
                                 Modifier.FINAL)
                   .addSuperinterface(ClassName.get(this.processorUtils.getPackageAsString(eventBusTypeElement),
                                                    eventBusTypeElement.getSimpleName()
                                                                       .toString()));
  }

  private TypeSpec.Builder createConstructorForEventBusClass(TypeSpec.Builder typeSpec,
                                                             TypeElement eventBusTypeElement) {
    TypeElement shellTypeElement = this.getShellTypeElement(eventBusTypeElement.getAnnotation(EventBus.class));
    MethodSpec constructor = MethodSpec.constructorBuilder()
                                       .addModifiers(Modifier.PUBLIC)
                                       .addStatement("super($S, $L)",
                                                     shellTypeElement.getQualifiedName()
                                                                     .toString(),
                                                     eventBusTypeElement.getAnnotation(EventBus.class)
                                                                        .historyOnStart())
                                       .build();
    return typeSpec.addMethod(constructor);
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

    public EventBusAnnotationHandler build() {
      return new EventBusAnnotationHandler(this);
    }

  }
}
