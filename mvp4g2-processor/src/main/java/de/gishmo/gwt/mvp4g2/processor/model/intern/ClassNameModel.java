package de.gishmo.gwt.mvp4g2.processor.model.intern;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.Objects;

public class ClassNameModel {

  private String className;

  public ClassNameModel(String className) {
    this.className = Objects.requireNonNull(className);
  }

  public String getClassName() {
    return className;
  }

  public TypeName getTypeName() {
    switch (className) {
      case "void":
        return TypeName.VOID;
      case "boolean":
        return TypeName.BOOLEAN;
      case "byte":
        return TypeName.BYTE;
      case "short":
        return TypeName.SHORT;
      case "int":
        return TypeName.INT;
      case "long":
        return TypeName.LONG;
      case "char":
        return TypeName.CHAR;
      case "float":
        return TypeName.FLOAT;
      case "double":
        return TypeName.DOUBLE;
      case "Object":
        return TypeName.OBJECT;
      case "Void":
        return ClassName.get("java.lang",
                             "Void");
      case "Boolean":
        return ClassName.get("java.lang",
                             "Boolean");
      case "Byte":
        return ClassName.get("java.lang",
                             "Byte");
      case "Short":
        return ClassName.get("java.lang",
                             "Short");
      case "Integer":
        return ClassName.get("java.lang",
                             "Integer");
      case "Long":
        return ClassName.get("java.lang",
                             "Long");
      case "Character":
        return ClassName.get("java.lang",
                             "Character");
      case "Float":
        return ClassName.get("java.lang",
                             "Float");
      case "Double":
        return ClassName.get("java.lang",
                             "Double");
      default:
        return ClassName.get(this.getPackage(),
                             this.getSimpleName());
    }
  }

  public String getPackage() {
    return className.contains(".") ? className.substring(0,
                                                         className.lastIndexOf(".")) : "";
  }

  public String getSimpleName() {
    return className.contains(".") ? className.substring(className.lastIndexOf(".") + 1) : className;
  }

  public String normalized() {
    return className.replace(".",
                             "_");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ClassNameModel)) {
      return false;
    }
    ClassNameModel that = (ClassNameModel) o;
    return Objects.equals(className,
                          that.className);
  }

  @Override
  public int hashCode() {

    return Objects.hash(className);
  }
}
