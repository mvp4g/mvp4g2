package de.gishmo.gwt.mvp4g2.processor;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

  public boolean implementsInterface(ProcessingEnvironment processingEnvironment,
                                     TypeElement typeElement,
                                     TypeMirror implementedInterface) {
    return processingEnvironment.getTypeUtils()
                                .isAssignable(typeElement.asType(),
                                              implementedInterface);
  }

  public String getCanonicalClassName(Element element) {
    return this.getPackageAsString(element) +
           "." + element.getSimpleName()
                        .toString();
  }

  public String getPackageAsString(Element type) {
    return this.getPackage(type)
               .getQualifiedName()
               .toString();
  }

  public PackageElement getPackage(Element type) {
    while (type.getKind() != ElementKind.PACKAGE) {
      type = type.getEnclosingElement();
    }
    return (PackageElement) type;
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

  public String createNameWithleadingUpperCase(String name) {
    return name.substring(0,
                          1)
               .toUpperCase() + name.substring(1);
  }

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

  public String createFullClassName(String packageName,
                                    String className) {
    return packageName.replace(".",
                               "_") + "_" + className;
  }

  public String createFullClassName(String className) {
    return className.replace(".",
                             "_");
  }

  public String setFirstCharacterToUpperCase(String className) {
    return className.substring(0,
                               1)
                    .toUpperCase() + className.substring(1);
  }

  public <T> boolean isSuperClass(Types typeUtils,
                                  TypeElement typeElement,
                                  Class<T> superClazz) {
    for (TypeMirror tm : typeUtils.directSupertypes(typeElement.asType())) {
      String canonicalNameTM = this.getCanonicalClassName((TypeElement) typeUtils.asElement(tm));
      if (superClazz.getCanonicalName()
                    .equals(canonicalNameTM)) {
        return true;
      } else {
        return this.isSuperClass(typeUtils,
                                 (TypeElement) typeUtils.asElement(tm),
                                 superClazz);
      }
    }
    return false;
  }

  public List<TypeElement> getListOfSuperClasses(TypeElement typeElement,
                                                 Class<?> implementingSuperClass) {
    List<TypeElement> listOfTypeMirror = new ArrayList<>();
    TypeMirror implementingSuperClassTypeMirror = this.getTypeMirror(implementingSuperClass.getCanonicalName());
    Set<TypeMirror> list = this.getFlattenedSupertypeHierarchy(this.processingEnvironment.getTypeUtils(),
                                                               typeElement.asType());
    list.stream()
        .filter(mirror -> !implementingSuperClassTypeMirror.toString()
                                                           .equals(mirror.toString()))
        .filter(mirror -> !typeElement.asType()
                                      .toString()
                                      .equals(mirror.toString()))
        .filter(mirror -> this.processingEnvironment.getTypeUtils()
                                                    .isAssignable(mirror,
                                                                  implementingSuperClassTypeMirror))
        .forEachOrdered(mirror -> {
          listOfTypeMirror.add(this.getTypeElement(mirror));
        });
    return listOfTypeMirror;
  }

  public TypeMirror getTypeMirror(String className) {
    return this.getTypeElement(className)
               .asType();
  }

  public TypeElement getTypeElement(TypeMirror mirror) {
    return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                   .asElement(mirror);
  }

  public TypeElement getTypeElement(String className) {
    return this.processingEnvironment.getElementUtils()
                                     .getTypeElement(className);
  }

  protected void createErrorMessage(String errorMessage) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    pw.println(errorMessage);
    pw.close();
    messager.printMessage(Diagnostic.Kind.ERROR,
                          sw.toString());

  }

  public String createInternalEventName(ExecutableElement executableElement) {
    String internalEventname = executableElement.getSimpleName().toString();
    for (VariableElement variableElement : executableElement.getParameters())   {
      internalEventname += "_";
      internalEventname += variableElement.asType()
                                          .toString().replace(".", "_");
    }
    return internalEventname;
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
