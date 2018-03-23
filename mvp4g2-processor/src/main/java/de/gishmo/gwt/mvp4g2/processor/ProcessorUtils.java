package de.gishmo.gwt.mvp4g2.processor;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import de.gishmo.gwt.mvp4g2.core.ui.annotation.EventHandler;
import de.gishmo.gwt.mvp4g2.processor.model.intern.ClassNameModel;
import de.gishmo.gwt.mvp4g2.processor.model.intern.IsMetaModel;

public class ProcessorUtils {

  private ProcessingEnvironment processingEnvironment;

  private Types    types;
  private Messager messager;
  private Filer    filer;
  private Elements elements;

  @SuppressWarnings("unused")
  private ProcessorUtils(Builder builder) {
    super();

    this.processingEnvironment = builder.processingEnvironment;

    this.types = this.processingEnvironment.getTypeUtils();
    this.messager = this.processingEnvironment.getMessager();
    this.filer = this.processingEnvironment.getFiler();
    this.elements = this.processingEnvironment.getElementUtils();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void store(IsMetaModel metaModel,
                    String fileName)
    throws ProcessorException {
    try {
      FileObject fileObject = processingEnvironment.getFiler()
                                                   .createResource(StandardLocation.CLASS_OUTPUT,
                                                                   "",
                                                                   fileName);
      PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(fileObject.openOutputStream()));
      metaModel.createPropertes()
               .store(printWriter,
                      "");
      printWriter.close();
    } catch (IOException ex) {
      throw new ProcessorException("Mvp4g2Processor: Unable to write file: >>" + fileName + "<< -> exception: " + ex.getMessage());
    }
  }

  public boolean implementsInterface(ProcessingEnvironment processingEnvironment,
                                     TypeElement typeElement,
                                     TypeMirror implementedInterface) {
    return processingEnvironment.getTypeUtils()
                                .isAssignable(typeElement.asType(),
                                              implementedInterface);
  }

  //  public String getCanonicalClassName(Element element) {
  //    return this.getPackageAsString(element) +
  //           "." + element.getSimpleName()
  //                        .toString();
  //  }
  //
  //  public String getPackageAsString(Element type) {
  //    return this.getPackage(type)
  //               .getQualifiedName()
  //               .toString();
  //  }
  //
  //  public PackageElement getPackage(Element type) {
  //    while (type.getKind() != ElementKind.PACKAGE) {
  //      type = type.getEnclosingElement();
  //    }
  //    return (PackageElement) type;
  //  }


  public Elements getElements() {
    return this.elements;
  }

  /**
   * Returns all of the superclasses and superinterfaces for a given generator
   * including the generator itself. The returned set maintains an internal
   * breadth-first ordering of the generator, followed by its interfaces (and their
   * super-interfaces), then the supertype and its interfaces, and so on.
   */
  public boolean extendsClassOrInterface(Types types,
                                         TypeMirror typeMirror,
                                         TypeMirror toImplement) {
    String clearedToImplement = this.removeGenericsFromClassName(toImplement.toString());
    Set<TypeMirror> setOfSuperType = this.getFlattenedSupertypeHierarchy(types,
                                                                         typeMirror);
    for (TypeMirror mirror : setOfSuperType) {
      if (clearedToImplement.equals(this.removeGenericsFromClassName(mirror.toString()))) {
        return true;
      }
    }
    return false;
  }

  private String removeGenericsFromClassName(String className) {
    if (className.contains("<")) {
      className = className.substring(0,
                                      className.indexOf("<"));
    }
    return className;
  }

  /**
   * Returns all of the superclasses and superinterfaces for a given generator
   * including the generator itself. The returned set maintains an internal
   * breadth-first ordering of the generator, followed by its interfaces (and their
   * super-interfaces), then the supertype and its interfaces, and so on.
   */
  public Set<TypeMirror> getFlattenedSupertypeHierarchy(Types types,
                                                        TypeMirror typeMirror) {
    List<TypeMirror> toAdd = new ArrayList<>();
    LinkedHashSet<TypeMirror> result = new LinkedHashSet<>();
    toAdd.add(typeMirror);
    for (int i = 0; i < toAdd.size(); i++) {
      TypeMirror type = toAdd.get(i);
      if (result.add(type)) {
        toAdd.addAll(types.directSupertypes(type));
      }
    }
    return result;
  }

  public boolean supertypeHasGeneric(Types types,
                                     TypeMirror typeMirror,
                                     TypeMirror implementsMirror) {
    TypeMirror superTypeMirror = this.getFlattenedSupertype(types,
                                                            typeMirror,
                                                            implementsMirror);
    if (superTypeMirror == null) {
      return false;
    }
    return superTypeMirror.toString()
                          .contains("<");
  }

  public TypeMirror getFlattenedSupertype(Types types,
                                          TypeMirror typeMirror,
                                          TypeMirror implementsMirror) {
    String implementsMirrorWihoutGeneric = this.removeGenericsFromClassName(implementsMirror.toString());
    Set<TypeMirror> implementedSuperTypes = this.getFlattenedSupertypeHierarchy(types,
                                                                                typeMirror);
    for (TypeMirror typeMirrorSuperType : implementedSuperTypes) {
      String tn1WithoutGenric = this.removeGenericsFromClassName(typeMirrorSuperType.toString());
      if (implementsMirrorWihoutGeneric.equals(tn1WithoutGenric)) {
        return typeMirrorSuperType;
      }
    }
    return null;
  }

  //  public String createNameWithleadingUpperCase(String name) {
  //    return name.substring(0,
  //                          1)
  //               .toUpperCase() + name.substring(1);
  //  }

  public void createErrorMessage(String errorMessage) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    pw.println(errorMessage);
    pw.close();
    messager.printMessage(Diagnostic.Kind.ERROR,
                          sw.toString());

  }

  public String createFullClassName(String packageName,
                                    String className) {
    return packageName.replace(".",
                               "_") + "_" + className;
  }

  public void createNoteMessage(String noteMessage) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    pw.println(noteMessage);
    pw.close();
    messager.printMessage(Diagnostic.Kind.NOTE,
                          sw.toString());
  }

  public void createWarningMessage(String warningMessage) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    pw.println(warningMessage);
    pw.close();
    messager.printMessage(Diagnostic.Kind.WARNING,
                          sw.toString());
  }

  public String createHandledEventArray(TypeElement typeElement)
    throws ProcessorException {
    List<String> eventHandlingMethods = new ArrayList<>();
    List<Element> annotatedMethods = this.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                typeElement,
                                                                                EventHandler.class);
    for (Element annotatedMethod : annotatedMethods) {
      ExecutableElement executableElement = (ExecutableElement) annotatedMethod;
      if (!"void".equals(executableElement.getReturnType()
                                          .toString())) {
        throw new ProcessorException("Mvp4g2Processor: >>" + typeElement.toString() + "<< -> EventElement: >>" + executableElement.toString() + "<< must return 'void'");
      }
    }

    annotatedMethods.stream()
                    .map(element -> (ExecutableElement) element)
                    .map(this::createInternalEventName)
                    .forEach(internalEventName -> {
//                      internalEventName = internalEventName.substring(2);
//                      internalEventName = internalEventName.substring(0,
//                                                                      1)
//                                                           .toLowerCase() + internalEventName.substring(1);
                      eventHandlingMethods.add(internalEventName);
                    });
    String returnValue = "";
    for (int i = 0; i < eventHandlingMethods.size(); i++) {
      returnValue += eventHandlingMethods.get(i);
      if (i < eventHandlingMethods.size() - 1) {
        returnValue += ",";
      }
    }
    return returnValue;
  }

  //  public <T> boolean isSuperClass(Types typeUtils,
  //                                  TypeElement typeElement,
  //                                  Class<T> superClazz) {
  //    for (TypeMirror tm : typeUtils.directSupertypes(typeElement.asType())) {
  //      String canonicalNameTM = this.getCanonicalClassName((TypeElement) typeUtils.asElement(tm));
  //      if (superClazz.getCanonicalName()
  //                    .equals(canonicalNameTM)) {
  //        return true;
  //      } else {
  //        return this.isSuperClass(typeUtils,
  //                                 (TypeElement) typeUtils.asElement(tm),
  //                                 superClazz);
  //      }
  //    }
  //    return false;
  //  }
  //
  //  public List<TypeElement> getListOfSuperClasses(TypeElement typeElement,
  //                                                 Class<?> implementingSuperClass) {
  //    List<TypeElement> listOfTypeMirror = new ArrayList<>();
  //    TypeMirror implementingSuperClassTypeMirror = this.getTypeMirror(implementingSuperClass.getCanonicalName());
  //    Set<TypeMirror> list = this.getFlattenedSupertypeHierarchy(this.processingEnvironment.getTypeUtils(),
  //                                                               typeElement.asType());
  //    list.stream()
  //        .filter(mirror -> !implementingSuperClassTypeMirror.toString()
  //                                                           .equals(mirror.toString()))
  //        .filter(mirror -> !typeElement.asType()
  //                                      .toString()
  //                                      .equals(mirror.toString()))
  //        .filter(mirror -> this.processingEnvironment.getTypeUtils()
  //                                                    .isAssignable(mirror,
  //                                                                  implementingSuperClassTypeMirror))
  //        .forEachOrdered(mirror -> {
  //          listOfTypeMirror.add(this.getTypeElement(mirror));
  //        });
  //    return listOfTypeMirror;
  //  }
  //
  //  public TypeMirror getTypeMirror(String className) {
  //    return this.getTypeElement(className)
  //               .asType();
  //  }
  //
  //  public TypeElement getTypeElement(TypeMirror mirror) {
  //    return (TypeElement) this.processingEnvironment.getTypeUtils()
  //                                                   .asElement(mirror);
  //  }
  //
  //  public TypeElement getTypeElement(String className) {
  //    return this.processingEnvironment.getElementUtils()
  //                                     .getTypeElement(className);
  //  }
  //
  //  public String getEventBusResourcePath() {
  //    return StandardLocation.CLASS_OUTPUT + "/" + "META-INF/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + ProcessorConstants.EVENT_BUS_FOLDER_NAME + "/EventBus";
  //  }

  public <A extends Annotation> List<Element> getMethodFromTypeElementAnnotatedWith(ProcessingEnvironment processingEnvironment,
                                                                                    TypeElement element,
                                                                                    Class<A> annotation) {
    List<Element> annotatedMethods = processingEnvironment.getElementUtils()
                                                          .getAllMembers(element)
                                                          .stream()
                                                          .filter(methodElement -> methodElement.getAnnotation(annotation) != null)
                                                          .collect(Collectors.toList());
    return annotatedMethods;
  }

  public String createInternalEventName(ExecutableElement executableElement) {
    String internalEventname = executableElement.getSimpleName()
                                                .toString();
    for (VariableElement variableElement : executableElement.getParameters()) {
      internalEventname += ProcessorConstants.PARAMETER_DELIMITER;
      internalEventname += variableElement.asType()
                                          .toString()
                                          .replace(".",
                                                   "_");
    }
    return internalEventname;
  }

  public boolean doesExist(ClassNameModel typeElementClassName) {
    return this.processingEnvironment.getElementUtils()
                                     .getTypeElement(typeElementClassName.getClassName()) != null;
  }

  public String createHistoryMetaDataClassName(String historyConverterClassName) {
    return this.setFirstCharacterToUpperCase(this.createHistoryMetaDataVariableName(historyConverterClassName)) + "_" + ProcessorConstants.META_DATA;
  }

  public String setFirstCharacterToUpperCase(String className) {
    return className.substring(0,
                               1)
                    .toUpperCase() + className.substring(1);
  }

  public String createHistoryMetaDataVariableName(String historyConverterClassName) {
    return this.createFullClassName(historyConverterClassName);
  }

  public String createFullClassName(String className) {
    return className.replace(".",
                             "_");
  }

  public String createEventHandlingMethodName(String eventName) {
    return "on" + eventName.substring(0,
                                      1)
                           .toUpperCase() + eventName.substring(1);
  }

  public String createEventNameFromHandlingMethod(String event) {
    return event.substring(2,
                           3)
                .toLowerCase() + event.substring(3);
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public ProcessorUtils build() {
      return new ProcessorUtils(this);
    }

  }
}
