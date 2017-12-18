package de.gishmo.gwt.mvp4g2.processor.scanner;

import de.gishmo.gwt.mvp4g2.client.ui.AbstractPresenter;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;
import de.gishmo.gwt.mvp4g2.processor.model.PresenterModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Properties;

public class PresenterAnnotationScanner {

  private final static String PRESENTER_PROPERTIES = "presenter.properties";

  private ProcessorUtils        processorUtils;
  private ProcessingEnvironment processingEnvironment;

  @SuppressWarnings("unused")
  private PresenterAnnotationScanner(Builder builder) {
    super();
    this.processingEnvironment = builder.processingEnvironment;
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

  public PresenterModel scan(RoundEnvironment roundEnvironment)
    throws ProcessorException {
    // read all already created model
    PresenterModel model = this.restore();
    for (Element element : roundEnvironment.getElementsAnnotatedWith(Presenter.class)) {
      TypeElement typeElement = (TypeElement) element;
      // validate the element. In case of error throw exception!
      validate(typeElement);
      // update model
      model.add(((TypeElement) element).getQualifiedName()
                                       .toString(),
                typeElement.getAnnotation(Presenter.class)
                           .multiple() ? "true" : "false",
                this.getViewClassTypeElement(element.getAnnotation(Presenter.class))
                    .getQualifiedName()
                    .toString(),
                this.getViewInterfaceTypeElement(element.getAnnotation(Presenter.class))
                    .getQualifiedName()
                    .toString(),
                typeElement.getAnnotation(Presenter.class)
                           .viewCreator()
                           .toString(),
                this.processorUtils.createHandledEventArray(typeElement));
      // let's store the updated model
      this.processorUtils.store(model,
                                this.createRelativeFileName());
    }
    return model;
  }

  private PresenterModel restore() {
    Properties props = new Properties();
    try {
      FileObject resource = this.processingEnvironment.getFiler()
                                                      .getResource(StandardLocation.CLASS_OUTPUT,
                                                                   "",
                                                                   this.createRelativeFileName());
      props.load(resource.openInputStream());
      return new PresenterModel(props);
    } catch (IOException e) {
      this.processorUtils.createNoteMessage("no resource found for : >>" + this.createRelativeFileName() + "<<");
    }
    return new PresenterModel();
  }

  private void validate(Element element)
    throws ProcessorException {
    if (element instanceof TypeElement) {
      TypeElement typeElement = (TypeElement) element;
      // check, that the presenter annotion is only used with classes
      if (!typeElement.getKind()
                      .isClass()) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @Presenter can only be used with as class!");
      }
      // check, that the view attribute is a class
      TypeElement viewClassElement = this.getViewClassTypeElement(element.getAnnotation(Presenter.class));
      TypeElement viewInterfaceElement = this.getViewInterfaceTypeElement(element.getAnnotation(Presenter.class));
      // check, that the viewClass is a class
      if (!viewClassElement.getKind()
                           .isClass()) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": the viewClass-attribute of a @Presenter must be a class!");
      }
      // chekc if the vioewInterface is a interface
      if (!viewInterfaceElement.getKind()
                               .isInterface()) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": the viewInterface-attribute of a @Presenter must be a interface!");
      }
      // check, if viewClass is implementing viewInterface
      if (!this.processorUtils.implementsInterface(this.processingEnvironment,
                                                   viewClassElement,
                                                   viewInterfaceElement.asType())) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": the viewClass-attribute of a @Presenter must implement the viewInterface!");
      }
      // check, that the typeElement extends AbstractEventHandler
      if (!this.processorUtils.extendsClassOrInterface(this.processingEnvironment.getTypeUtils(),
                                                       typeElement.asType(),
                                                       this.processingEnvironment.getElementUtils()
                                                                                 .getTypeElement(AbstractPresenter.class.getCanonicalName())
                                                                                 .asType())) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @Presenter must extend AbstractPresenter.class!");
      }
      // check if annotated class is abstract
      if (typeElement.getModifiers()
                     .contains(Modifier.ABSTRACT)) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": @Presenter can not be ABSTRACT");
      }
      // check if class attribute is not abstradt
      if (viewClassElement.getModifiers()
                          .contains(Modifier.ABSTRACT)) {
        throw new ProcessorException(typeElement.getSimpleName()
                                                .toString() + ": class-attribute of @Presenter can not be ABSTRACT");
      }
    } else {
      throw new ProcessorException("@Presenter can only be used on a type (class)");
    }
  }

  private TypeElement getViewClassTypeElement(Presenter presenterAnnotation) {
    try {
      presenterAnnotation.viewClass();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private TypeElement getViewInterfaceTypeElement(Presenter presenterAnnotation) {
    try {
      presenterAnnotation.viewInterface();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  private String createRelativeFileName() {
    return ProcessorConstants.META_INF + "/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + PresenterAnnotationScanner.PRESENTER_PROPERTIES;
  }

  public static class Builder {

    ProcessingEnvironment processingEnvironment;

    public Builder processingEnvironment(ProcessingEnvironment processingEnvironment) {
      this.processingEnvironment = processingEnvironment;
      return this;
    }

    public PresenterAnnotationScanner build() {
      return new PresenterAnnotationScanner(this);
    }
  }
}
