package de.gishmo.gwt.mvp4g2.processor.handler.eventhandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.squareup.javapoet.TypeSpec;

import de.gishmo.gwt.mvp4g2.client.ui.annotation.EventHandlingMethod;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.processor.ProcessorConstants;
import de.gishmo.gwt.mvp4g2.processor.ProcessorException;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

public class PresenterUtils {

  private ProcessorUtils processorUtils;

  private ProcessingEnvironment processingEnvironment;
  private TypeElement           eventBusTypeElement;
  private TypeSpec.Builder      typeSpec;

  private PresenterUtils() {
  }

  private PresenterUtils(Builder builder) {
    this.processingEnvironment = builder.processingEnvironment;
    //      this.eventBusTypeElement = builder.eventBusTypeElement;
    //      this.typeSpec = builder.typeSpec;

    this.processorUtils = ProcessorUtils.builder()
                                        .processingEnvironment(this.processingEnvironment)
                                        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public TypeElement getViewClassTypeElement(Presenter presenterAnnotation) {
    try {
      presenterAnnotation.viewClass();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  public TypeElement getViewInterfaceTypeElement(Presenter presenterAnnotation) {
    try {
      presenterAnnotation.viewInterface();
    } catch (MirroredTypeException exception) {
      return (TypeElement) this.processingEnvironment.getTypeUtils()
                                                     .asElement(exception.getTypeMirror());
    }
    return null;
  }

  public Presenter.VIEW_CREATION_METHOD getCreator(TypeElement element) {
    return element.getAnnotation(Presenter.class)
                  .viewCreator();
  }

  public void createMetaFile(TypeElement typeElement,
                             boolean isPresenter)
    throws ProcessorException {
    List<Element> annotatedMethods = this.processorUtils.getMethodFromTypeElementAnnotatedWith(this.processingEnvironment,
                                                                                               typeElement,
                                                                                               EventHandlingMethod.class);
    String fileName = ProcessorConstants.META_FILE_EVENTHANDLER_PREFIX + this.processorUtils.createFullClassName(typeElement.getSimpleName()
                                                                                                                            .toString());
    try {
      FileObject resource = processingEnvironment.getFiler()
                                                 .createResource(StandardLocation.CLASS_OUTPUT,
                                                                 "",
                                                                 "META-INF/" + ProcessorConstants.MVP4G2_FOLDER_NAME + "/" + fileName);
      PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(resource.openOutputStream()));
      printWriter.println(typeElement.asType()
                                     .toString());
      printWriter.println(isPresenter ? ProcessorConstants.PRESENTER : ProcessorConstants.EVENT_HANDLER);
      annotatedMethods.stream()
                      .map(element -> (ExecutableElement) element)
                      .map(executableElement -> this.processorUtils.createInternalEventName(executableElement) + "!")
                      .forEach(printWriter::println);
      printWriter.close();
    } catch (IOException ex) {
      throw new ProcessorException("Unable to write file: >>" + fileName + "<< -> exception: " + ex.getMessage());
    }
  }

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;
    //      TypeElement           eventBusTypeElement;
    //      TypeSpec.Builder      typeSpec;
    //

    /**
     * Set the processing envirement
     *
     * @param processingEnvirement
     *   the processing envirement
     *
     * @return the Builder
     */
    public Builder processingEnvironment(ProcessingEnvironment processingEnvirement) {
      this.processingEnvironment = processingEnvirement;
      return this;
    }

    public PresenterUtils build() {
      return new PresenterUtils(this);
    }
  }
}
