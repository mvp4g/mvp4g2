package de.gishmo.gwt.mvp4g2.processor.model.intern;

public class ClassNameModel {

  private String className;

  public ClassNameModel(String className) {
    this.className = className;
  }

  public String getClassName() {
    return className;
  }

  public String getSimpleName() {
    return className.contains(".") ? className.substring(className.lastIndexOf(".") + 1) : className;
  }

  public String getPackage() {
    return className.contains(".") ? className.substring(0,
                                                         className.lastIndexOf(".")) : "";
  }

  public String normalized() {
    return className.replace(".",
                             "_");
  }
}
