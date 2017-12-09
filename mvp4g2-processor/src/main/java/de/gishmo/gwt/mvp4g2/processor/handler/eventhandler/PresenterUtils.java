package de.gishmo.gwt.mvp4g2.processor.handler.eventhandler;

import com.squareup.javapoet.TypeSpec;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;
import de.gishmo.gwt.mvp4g2.processor.ProcessorUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;

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

  public static final class Builder {

    ProcessingEnvironment processingEnvironment;
//      TypeElement           eventBusTypeElement;
//      TypeSpec.Builder      typeSpec;
//

    /**
     * Set the processing envirement
     *
     * @param processingEnvirement the processing envirement
     * @return the Builder
     */
    public Builder processingEnvironment(ProcessingEnvironment processingEnvirement) {
      this.processingEnvironment = processingEnvirement;
      return this;
    }
//
//      /**
//       * Set the eventbus generator element
//       *
//       * @param eventBusTypeElement the eventbvus generator element
//       * @return the Builder
//       */
//      public de.gishmo.gwt.mvp4g2.processor.handler.eventbus.generator.EventHandlingMethodGenerator.Builder eventBusTypeElement(TypeElement eventBusTypeElement) {
//        this.eventBusTypeElement = eventBusTypeElement;
//        return this;
//      }
//
//      /**
//       * Set the typeSpec of the currently generated eventBus
//       *
//       * @param typeSpec ttype spec of the crruent eventbus
//       * @return the Builder
//       */
//      public de.gishmo.gwt.mvp4g2.processor.handler.eventbus.generator.EventHandlingMethodGenerator.Builder typeSpec(TypeSpec.Builder typeSpec) {
//        this.typeSpec = typeSpec;
//        return this;
//      }

    public PresenterUtils build() {
      return new PresenterUtils(this);
    }
  }
}
