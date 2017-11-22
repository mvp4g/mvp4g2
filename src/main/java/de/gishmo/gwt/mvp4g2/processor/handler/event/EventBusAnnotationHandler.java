package de.gishmo.gwt.mvp4g2.processor.handler.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import de.gishmo.gwt.mvp4g2.client.eventbus.AbstractEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.PresenterHandlerRegistration;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Event;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.Start;
import de.gishmo.gwt.mvp4g2.client.eventbus.internal.EventMetaData;
import de.gishmo.gwt.mvp4g2.client.ui.AbstractPresenter;
import de.gishmo.gwt.mvp4g2.client.ui.IsPresenter;
import de.gishmo.gwt.mvp4g2.client.ui.internal.EventHandlerMetaData;
import de.gishmo.gwt.mvp4g2.client.ui.internal.PresenterHandlerMetaData;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

// TODO Start annotation validation!
public class EventBusAnnotationHandler {

  private final static String IMPL_NAME       = "Impl";
  private final static String EVENT_META_DATA = "EventMetaData";
  private final static String META_DATA       = "MetaData";

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
      TypeElement eventBusTypeElement = (TypeElement) element;
      // generate the event meta data
      List<Element> events = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                       (TypeElement) element,
                                                                                       Event.class);
      for (Element event : events) {
        this.validateEvent(eventBusTypeElement,
                           element);
        this.generateEventMetaData(eventBusTypeElement,
                                   event);
      }
      // generate the eventBus
      this.generateEventbus(element);
    }
  }

  private void validateEventBus()
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
    // annotated element has to be a interface
    for (Element element : elementsWithEventBusAnnotation) {
      if (element instanceof TypeElement) {
        TypeElement typeElement = (TypeElement) element;
        if (!typeElement.getKind()
                        .isInterface()) {
          throw new ProcessorException("@Eventbus can only be used with an interface");
        }
      }
    }
  }

  private void validateEvent(Element element,
                             Element element1)
    throws ProcessorException {
    //    try {
    //      ExecutableElement executableElement = (ExecutableElement) element;
    //    } catch (Exception e) {
    //      throw new ProcessorException("@Event can only be used with a method");
    //    }
  }

  private void generateEventMetaData(TypeElement eventBusTypeElement,
                                     Element methodElement)
    throws IOException {
    ExecutableElement executableElement = (ExecutableElement) methodElement;

    // List of event handlers (full class names as String)
    List<String> handlersFromAnnotation = this.getHandlerElementsAsList(executableElement,
                                                                        "handlers");
    // List of binding handlers (full class names as String)
    List<String> bindHandlersFromAnnotation = this.getHandlerElementsAsList(executableElement,
                                                                            "bind");

    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.createEventMetaDataClassName(eventBusTypeElement,
                                                                                        executableElement))
                                        .superclass(ClassName.get(EventMetaData.class))
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL);

    // constructor ...
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                                               .addModifiers(Modifier.PUBLIC)
                                               .addStatement("super($S, $L, $L)",
                                                             executableElement.getSimpleName()
                                                                              .toString(),
                                                             executableElement.getAnnotation(Event.class)
                                                                              .passive(),
                                                             executableElement.getAnnotation(Event.class)
                                                                              .navigationEvent());
    executableElement.getParameters()
                     .stream()
                     .map(variableElement -> constructor.addStatement("super.addParameterType($S, $S)",
                                                                      variableElement.getSimpleName()
                                                                                     .toString(),
                                                                      variableElement.asType()
                                                                                     .toString()));
    if (handlersFromAnnotation != null) {
      handlersFromAnnotation.stream()
                            .forEach((s) -> constructor.addStatement("super.addHandler($S)",
                                                                     s));
    }
    if (bindHandlersFromAnnotation != null) {
      bindHandlersFromAnnotation.stream()
                                .forEach((s) -> constructor.addStatement("super.addBindHandler($S)",
                                                                         s));
    }
    typeSpec.addMethod(constructor.build());

    JavaFile javaFile = JavaFile.builder(ProcessorUtils.getPackageAsString(methodElement),
                                         typeSpec.build())
                                .build();
    javaFile.writeTo(this.processingEnvironment.getFiler());
    System.out.println(javaFile.toString());
  }

  private void generateEventbus(Element eventbusTypeElement)
    throws IOException {
    EventBus    eventBusAnnotation = eventbusTypeElement.getAnnotation(EventBus.class);
    TypeElement shellTypeElement   = this.getShellTypeElement(eventBusAnnotation);

    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(eventbusTypeElement.getSimpleName() + EventBusAnnotationHandler.IMPL_NAME)
                                        .superclass(ClassName.get(AbstractEventBus.class))
                                        .addModifiers(Modifier.PUBLIC,
                                                      Modifier.FINAL)
                                        .addSuperinterface(ClassName.get(ProcessorUtils.getPackageAsString(eventbusTypeElement),
                                                                         eventbusTypeElement.getSimpleName()
                                                                                            .toString()));

    // constructor ...
    MethodSpec constructor = MethodSpec.constructorBuilder()
                                       .addModifiers(Modifier.PUBLIC)
                                       .addStatement("super($S)",
                                                     shellTypeElement.getQualifiedName()
                                                                     .toString())
                                       .build();
    typeSpec.addMethod(constructor);

    // create loadEventHandlerMetaData-method
    //
    // this method will instantiate the meta data classes for event handlers and save them to a map
    this.generateLoadEventHandlerMetaDataMethod(typeSpec,
                                                eventbusTypeElement);
    // create loadEventMetaData-method
    //
    // this method will instantiate the meta data classes and save for events and save them to a map
    this.generateLoadEventMetaDataMethod(typeSpec,
                                         eventbusTypeElement);
    // create event ahndling methods
    this.generateEventHandlingMethods(typeSpec,
                                      eventbusTypeElement);

    JavaFile javaFile = JavaFile.builder(ProcessorUtils.getPackageAsString(eventbusTypeElement),
                                         typeSpec.build())
                                .build();
    javaFile.writeTo(this.processingEnvironment.getFiler());

    System.out.println(javaFile.toString());
  }

  private List<String> getHandlerElementsAsList(ExecutableElement executableElement,
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

  private boolean isPresenter(String eventHandlerClassName) {
    return this.processorUtils.isSuperClass(this.processingEnvironment.getTypeUtils(),
                                            this.processingEnvironment.getElementUtils()
                                                                      .getTypeElement(eventHandlerClassName),
                                            AbstractPresenter.class);
  }

  private String createEventMetaDataClassName(TypeElement element,
                                              ExecutableElement executableElement) {
    return this.processorUtils.setFirstCharacterToUpperCase(this.createEventMetaDataVariableName(element,
                                                                                                 executableElement));
  }

  private String createEventMetaDataVariableName(TypeElement element,
                                                 ExecutableElement executableElement) {
    return this.processorUtils.createFullClassName(element.asType()
                                                          .toString() + "_" + executableElement.getSimpleName()
                                                                                               .toString());
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

  private void generateLoadEventHandlerMetaDataMethod(TypeSpec.Builder typeSpec,
                                                      Element element) {
    // create loadEventHandlerMetaData-method
    //
    // this method will instantiate the meta data classes and save them to a map
    MethodSpec.Builder loadEventHandlerMethod = MethodSpec.methodBuilder("loadEventHandlerMetaData")
                                                          .addAnnotation(Override.class)
                                                          .addModifiers(Modifier.PROTECTED);
    // get all elements annotated with Event
    List<Element> events = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                     (TypeElement) element,
                                                                                     Event.class);
    // List of already created EventHandler used to avoid a second create ...
    List<String> listOfAllEventHandlersToCreate = new ArrayList<>();
    events.stream()
          .map(eventElement -> (ExecutableElement) eventElement)
          .forEach(executableElement -> {
            List<String> handlersFromAnnotation = this.getHandlerElementsAsList(executableElement,
                                                                                "handlers");
            if (handlersFromAnnotation != null) {
              for (String eventHandlerClassName : handlersFromAnnotation) {
                if (!listOfAllEventHandlersToCreate.contains(eventHandlerClassName)) {
                  // save the name of the event handler to create
                  listOfAllEventHandlersToCreate.add(eventHandlerClassName);
                }
              }
            }
            List<String> bindingsFromAnnotation = this.getHandlerElementsAsList(executableElement,
                                                                                "bind");
            if (bindingsFromAnnotation != null) {
              for (String eventHandlerClassName : bindingsFromAnnotation) {
                if (!listOfAllEventHandlersToCreate.contains(eventHandlerClassName)) {
                  // save the name of the event Handler to create
                  listOfAllEventHandlersToCreate.add(eventHandlerClassName);
                }
              }
            }
          });
    listOfAllEventHandlersToCreate.forEach(eventHandlerClassName -> this.addHandlerToMetaList(loadEventHandlerMethod,
                                                                                              eventHandlerClassName));
    typeSpec.addMethod(loadEventHandlerMethod.build());

    // generate addHandlerMethod
    MethodSpec.Builder addHandlerMethod = MethodSpec.methodBuilder("addHandler")
                                                    .addAnnotation(Override.class)
                                                    .addModifiers(Modifier.PUBLIC)
                                                    .addParameter(ParameterizedTypeName.get(ClassName.get(IsPresenter.class),
                                                                                            WildcardTypeName.subtypeOf(Object.class),
                                                                                            WildcardTypeName.subtypeOf(Object.class)),
                                                                  "presenter")
                                                    .returns(ClassName.get(PresenterHandlerRegistration.class));
    // List of already created EventHandler used to avoid a second create ...
    // check if we deal with a presenter
    // Name of the variable , class name
    // check, that multiple is false! (We can do this not here during code generation, because we don't know it ...)
    listOfAllEventHandlersToCreate.stream()
                                  .filter(this::isPresenter)
                                  .forEach(eventHandlerClassName -> {
                                    String metaDataClassName    = this.createEventHandlerMetaDataClassName(eventHandlerClassName);
                                    String metaDataVariableName = this.processorUtils.createFullClassName(eventHandlerClassName + EventBusAnnotationHandler.META_DATA);
                                    addHandlerMethod.addStatement("final $N $N = new $N()",
                                                                  metaDataClassName,
                                                                  metaDataVariableName,
                                                                  metaDataClassName);
                                    addHandlerMethod.beginControlFlow("if ($N.isMultiple())",
                                                                      metaDataVariableName);
                                    this.generatePresenterBinding(addHandlerMethod,
                                                                  eventHandlerClassName,
                                                                  metaDataVariableName);
                                    addHandlerMethod.addCode("return new $T() {\n",
                                                             ClassName.get(PresenterHandlerRegistration.class));
                                    addHandlerMethod.addCode("  @$T\n",
                                                             ClassName.get(Override.class));
                                    addHandlerMethod.addCode("  public void remove() {\n");
                                    addHandlerMethod.addCode("    removePresenterHandlerMetaData($S, $N);\n",
                                                             eventHandlerClassName,
                                                             metaDataVariableName);
                                    addHandlerMethod.addCode("  }\n");
                                    addHandlerMethod.addCode("};\n");
                                    addHandlerMethod.nextControlFlow("else");
                                    addHandlerMethod.addStatement("assert false : $S",
                                                                  eventHandlerClassName + ": is not annotated with @Presenter(...,multiple = true)");
                                    addHandlerMethod.endControlFlow();
                                  });
    addHandlerMethod.addStatement("return null");
    typeSpec.addMethod(addHandlerMethod.build());
  }

  private String createEventHandlerMetaDataClassName(String className) {
    StringBuilder sb = new StringBuilder();
    sb.append(className.substring(0,
                                  className.lastIndexOf(".")))
      .append(".")
      .append(this.processorUtils.setFirstCharacterToUpperCase(className.replace(".",
                                                                                 "_")))
      .append(EventBusAnnotationHandler.META_DATA);
    return sb.toString();
  }

  private void addHandlerToMetaList(MethodSpec.Builder methodToGenerate,
                                    String eventHandlerClassName) {
    // check if we deal with a presenter
    boolean isPresenter = this.isPresenter(eventHandlerClassName);
    // Name of the variable , class name
    String metaDataClassName    = this.createEventHandlerMetaDataClassName(eventHandlerClassName);
    String metaDataVariableName = this.processorUtils.createFullClassName(eventHandlerClassName + EventBusAnnotationHandler.META_DATA);
    // comment
    methodToGenerate.addComment("");
    methodToGenerate.addComment("----------------------------------------------------------------------");
    methodToGenerate.addComment("");
    methodToGenerate.addComment("handle $N ($N)",
                                eventHandlerClassName,
                                isPresenter ? "Presenter" : "EventHandler");
    methodToGenerate.addComment("");
    // code ...
    methodToGenerate.addStatement("$N $N = new $N()",
                                  metaDataClassName,
                                  metaDataVariableName,
                                  metaDataClassName);
    if (isPresenter) {
      // check, that multiple is false! (We can do this not here during code generation, because we don't know it ...)
      methodToGenerate.beginControlFlow("if (!$N.isMultiple())",
                                        metaDataVariableName);
      this.generatePresenterBinding(methodToGenerate,
                                    eventHandlerClassName,
                                    metaDataVariableName);
      methodToGenerate.endControlFlow();
    } else {
      methodToGenerate.addStatement("super.putEventHandlerMetaData($S, $N)",
                                    eventHandlerClassName,
                                    metaDataVariableName);
      // set eventbus statement
      methodToGenerate.addStatement("$N.getEventHandler().setEventBus(this)",
                                    metaDataVariableName);
    }
  }

  private void generatePresenterBinding(MethodSpec.Builder methodToGenerate,
                                        String eventHandlerClassName,
                                        String metaDataVariableName) {
    methodToGenerate.addStatement("super.putPresenterHandlerMetaData($S, $N)",
                                  eventHandlerClassName,
                                  metaDataVariableName);
    // set eventbus statement
    methodToGenerate.addStatement("$N.getPresenter().setEventBus(this)",
                                  metaDataVariableName);
    // set view statement in presenter
    methodToGenerate.addStatement("$N.getPresenter().setView($N.getView())",
                                  metaDataVariableName,
                                  metaDataVariableName);
    // set presenter statement in view
    methodToGenerate.addStatement("$N.getView().setPresenter($N.getPresenter())",
                                  metaDataVariableName,
                                  metaDataVariableName);
  }

  private void generateLoadEventMetaDataMethod(TypeSpec.Builder typeSpec,
                                               Element eventbusTypeElement) {
    MethodSpec.Builder loadEventMethod = MethodSpec.methodBuilder("loadEventMetaData")
                                                   .addAnnotation(Override.class)
                                                   .addModifiers(Modifier.PROTECTED);
    // get all elements annotated with Event
    List<Element> events = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                     (TypeElement) eventbusTypeElement,
                                                                                     Event.class);
    // List of already created EventHandler used to avoid a second create ...
    // cast to ExecutableElement because @Event can only be used with methods ...
    events.stream()
          .map(eventElement -> (ExecutableElement) eventElement)
          .forEach(executableElement -> {
            String metaDataClassName = this.createEventMetaDataClassName((TypeElement) eventbusTypeElement,
                                                                         executableElement);
            loadEventMethod.addComment("");
            loadEventMethod.addComment("----------------------------------------------------------------------");
            loadEventMethod.addComment("");
            loadEventMethod.addComment("handle $N",
                                       metaDataClassName);
            loadEventMethod.addComment("");
            loadEventMethod.addStatement("super.putEventMetaData($S, new $N())",
                                         executableElement.getSimpleName()
                                                          .toString(),
                                         metaDataClassName);
          });
    typeSpec.addMethod(loadEventMethod.build());
  }

  private void generateEventHandlingMethods(TypeSpec.Builder typeSpec,
                                            Element element) {
    // get all elements annotated with Event
    List<Element> events = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                     (TypeElement) element,
                                                                                     Event.class);
    // List of already created EventHandler used to avoid a second create ...
    // cast to ExecutableElement because @Event can only be used with methods ...
    // generate event handlingmethod
    events.stream()
          .map(eventElement -> (ExecutableElement) eventElement)
          .forEach(executableElement -> {
            this.generateEventHandlingMethod(typeSpec,
                                             executableElement);
            this.generateEventHandlingMethodForExecution(typeSpec,
                                                         executableElement);
          });
    // generate the code for the Start event handling
    this.generateStartEventHandlingMethods(typeSpec,
                                           element);
  }

  private void generateStartEventHandlingMethods(TypeSpec.Builder typeSpec,
                                                 Element element) {
    MethodSpec.Builder startEventHandlingMethod = MethodSpec.methodBuilder("fireStartEvent")
                                                            .addAnnotation(Override.class)
                                                            .addModifiers(Modifier.PUBLIC,
                                                                          Modifier.FINAL);
    // get all elements annotated with Start
    List<Element> startEvents = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                          (TypeElement) element,
                                                                                          Start.class);
    // get event meta data from store ...
    startEventHandlingMethod.addStatement("this.$L()",
                                          startEvents.get(0)
                                                     .getSimpleName()
                                                     .toString());
    typeSpec.addMethod(startEventHandlingMethod.build());
  }

  // TODO split method in two, tow handle navagationEvents
  private void generateEventHandlingMethod(TypeSpec.Builder typeSpec,
                                           ExecutableElement executableElement) {
    StringBuilder sb = new StringBuilder();
    MethodSpec.Builder eventHandlingMethod = MethodSpec.methodBuilder(executableElement.getSimpleName()
                                                                                       .toString())
                                                       .addAnnotation(Override.class)
                                                       .addModifiers(Modifier.PUBLIC);
    // add parametners to method signature ...
    // generate method body
    executableElement.getParameters()
                     .stream()
                     .map(variableElement -> ParameterSpec.builder(ClassName.get(variableElement.asType()),
                                                                   variableElement.getSimpleName()
                                                                                  .toString())
                                                          .build())
                     .forEach(eventHandlingMethod::addParameter);
    // TODO implement here navagation envet handling!
    sb.append("do")
      .append(executableElement.getSimpleName()
                               .toString()
                               .substring(0,
                                          1)
                               .toUpperCase())
      .append(executableElement.getSimpleName()
                               .toString()
                               .substring(1));
    eventHandlingMethod.addCode(sb.toString() + "(");
    IntStream.range(0,
                    executableElement.getParameters()
                                     .size())
             .forEachOrdered(i -> {
               eventHandlingMethod.addCode(executableElement.getParameters()
                                                            .get(i)
                                                            .getSimpleName()
                                                            .toString());
               if (i != executableElement.getParameters()
                                         .size() - 1) {
                 eventHandlingMethod.addCode(", ");
               }
             });
    eventHandlingMethod.addCode(");\n");
    typeSpec.addMethod(eventHandlingMethod.build());
  }

  private void generateEventHandlingMethodForExecution(TypeSpec.Builder typeSpec,
                                                       ExecutableElement executableElement) {
    StringBuilder sb = new StringBuilder();
    sb.append("do")
      .append(executableElement.getSimpleName()
                               .toString()
                               .substring(0,
                                          1)
                               .toUpperCase())
      .append(executableElement.getSimpleName()
                               .toString()
                               .substring(1));
    MethodSpec.Builder eventHandlingMethod = MethodSpec.methodBuilder(sb.toString())
                                                       .addModifiers(Modifier.PUBLIC);
    // add parametners to method signature ...
    // generate method body
    executableElement.getParameters()
                     .stream()
                     .map(variableElement -> ParameterSpec.builder(ClassName.get(variableElement.asType()),
                                                                   variableElement.getSimpleName()
                                                                                  .toString())
                                                          .build())
                     .forEach(eventHandlingMethod::addParameter);
    // get event meta data from store ...
    eventHandlingMethod.addStatement("$T eventMetaData = super.getEventMetaData($S)",
                                     ClassName.get(EventMetaData.class),
                                     executableElement.getSimpleName()
                                                      .toString());
    /* bind views */
    eventHandlingMethod.addStatement("super.createAndBindView(eventMetaData)");
    /* Handle Binding */
    eventHandlingMethod.addCode("super.bind(eventMetaData");
    executableElement.getParameters()
                     .forEach(variableElement -> eventHandlingMethod.addCode(", $N",
                                                                             variableElement.getSimpleName()
                                                                                            .toString()));
    eventHandlingMethod.addCode(");\n");
    // fire events
    List<String> eventHandlerClasses = this.getHandlerElementsAsList(executableElement,
                                                                     "handlers");

    eventHandlingMethod.addStatement("$T<$T<?>> eventHandlers = null",
                                     ClassName.get(List.class),
                                     ClassName.get(EventHandlerMetaData.class));
    eventHandlingMethod.addStatement("$T<$T<?, ?>> presenters = null",
                                     ClassName.get(List.class),
                                     ClassName.get(PresenterHandlerMetaData.class));
    // start presenter code
    eventHandlerClasses.forEach(eventHandlerClass -> {
      eventHandlingMethod.addComment("handling: " + eventHandlerClass);
      this.createEventHandlingMethod(eventHandlingMethod,
                                     executableElement,
                                     eventHandlerClass,
                                     "eventHandlerMetaDataMap",
                                     false);
      this.createEventHandlingMethod(eventHandlingMethod,
                                     executableElement,
                                     eventHandlerClass,
                                     "presenterHandlerMetaDataMap",
                                     true);
    });
    typeSpec.addMethod(eventHandlingMethod.build());
  }

  private void createEventHandlingMethod(MethodSpec.Builder method,
                                         ExecutableElement executableElement,
                                         String eventHandlerClass,
                                         String handlerMetaDataMapName,
                                         boolean isPresenter) {
    String getHandlerMethodName = isPresenter ? "getPresenter" : "getEventHandler";
    String variableName         = isPresenter ? "presenters" : "eventHandlers";

    Class<?> metaDataClass = isPresenter ? PresenterHandlerMetaData.class : EventHandlerMetaData.class;

    method.addStatement("$N = this.$N.get($S)",
                        variableName,
                        handlerMetaDataMapName,
                        eventHandlerClass);
    method.beginControlFlow("if ($N != null && $N.size() != 0)",
                            variableName,
                            variableName);
    method.beginControlFlow("for ($T<$N> metaData : $N)",
                            ClassName.get(metaDataClass),
                            isPresenter ? "?, ?" : "?",
                            variableName);
    method.addStatement("boolean activated = metaData.$N().isActivated()",
                        getHandlerMethodName);
    method.addCode("activated = activated && metaData.$N().pass(eventMetaData.getEventName()",
                   getHandlerMethodName);
    executableElement.getParameters()
                     .forEach(variableElement -> method.addCode(", $N",
                                                                variableElement.getSimpleName()
                                                                               .toString()));
    method.addCode(");\n");
    method.beginControlFlow("if (activated)");
    //      method.beginControlFlow("if (presenterHandlerMetaData.getPresenter().isBinded())");
    method.addStatement("metaData.$N().onBeforeEvent($S)",
                        getHandlerMethodName,
                        executableElement.getSimpleName()
                                         .toString());
    method.addCode("(($T) metaData.$N()).on$L(",
                   ClassName.get(eventHandlerClass.substring(0,
                                                             eventHandlerClass.lastIndexOf(".")),
                                 eventHandlerClass.substring(eventHandlerClass.lastIndexOf(".") + 1)),
                   getHandlerMethodName,
                   executableElement.getSimpleName()
                                    .toString()
                                    .substring(0,
                                               1)
                                    .toUpperCase() + executableElement.getSimpleName()
                                                                      .toString()
                                                                      .substring(1));
    boolean firstElement = true;
    for (VariableElement variableElement : executableElement.getParameters()) {
      if (firstElement) {
        firstElement = false;
      } else {
        method.addCode(", ");
      }
      method.addCode("$N",
                     variableElement.getSimpleName()
                                    .toString());
    }
    method.addCode(");\n");
    //      method.endControlFlow();
    method.endControlFlow();
    method.endControlFlow();
    method.endControlFlow();
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
