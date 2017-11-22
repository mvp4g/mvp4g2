package gwt.mvp4g.processor.application.model;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import gwt.mvp4g.processor.AbstractModel;
import gwt.mvp4g.processor.ProcessorException;

/**
 * Created by hoss on 17.09.16.
 */
public class ApplicationModel
  extends AbstractModel {

  /* TypeElement of the annotated interface */
  private TypeElement application;

  public ApplicationModel(Builder builder)
    throws ProcessorException {
    super(builder.messager,
          builder.types,
          builder.elements);

    this.application = builder.application;

//    try {
//      this.application.getAnnotation(Application.class)
//                               .shell();
//    } catch (MirroredTypeException e) {
//      shellClass = elements.getTypeElement(e.getTypeMirror().toString());
//    }
  }

  public static Builder modelBuilder() {
    return new Builder();
  }

  public TypeElement getApplication() {
    return application;
  }


  public static final class Builder {
    private Messager    messager;
    private Types       types;
    private Elements    elements;
    private TypeElement application;

    public Builder setMessager(Messager messager) {
      this.messager = messager;
      return this;
    }

    public Builder setTypes(Types types) {
      this.types = types;
      return this;
    }

    public Builder setElements(Elements elements) {
      this.elements = elements;
      return this;
    }

    public Builder setApplication(TypeElement application) {
      this.application = application;
      return this;
    }

    public ApplicationModel build()
      throws ProcessorException {
      return new ApplicationModel(this);
    }
  }
}
