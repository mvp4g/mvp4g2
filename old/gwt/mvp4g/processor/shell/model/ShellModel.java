package gwt.mvp4g.processor.shell.model;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import gwt.mvp4g.client.annotations.Shell;
import gwt.mvp4g.client.mvp.AbstractPresenter;
import gwt.mvp4g.processor.AbstractModel;
import gwt.mvp4g.processor.ProcessorException;
import gwt.mvp4g.processor.ProcessorUtils;

/**
 * Created by hoss on 17.09.16.
 */
public class ShellModel
  extends AbstractModel {

  /* TypeElement of the annotated interface */
  private TypeElement shell;
  /* applciaiton interface */
  private TypeElement application;
  /* view */
  private TypeElement view;

  public ShellModel(Builder builder)
    throws ProcessorException {
    super(builder.messager,
          builder.types,
          builder.elements);

    this.shell = builder.annotatedShell;

    try {
      this.shell.getAnnotation(Shell.class)
                .application();
    } catch (MirroredTypeException e) {
      this.application = elements.getTypeElement(e.getTypeMirror()
                                                  .toString());
    }

    List<? extends TypeMirror> params = ProcessorUtils.findParameterizationOf(types,
                                                                              elements.getTypeElement(AbstractPresenter.class.getCanonicalName())
                                                                                      .asType(),
                                                                              shell.asType());
    if (params == null) {
      throw new ProcessorException("AbstractPresenter is not parameterized!");
    }
    view = elements.getTypeElement(params.get(0)
                                         .toString());
  }

  public static Builder modelBuilder() {
    return new Builder();
  }

  public TypeElement getShell() {
    return shell;
  }

  public TypeElement getApplication() {
    return application;
  }

  public TypeElement getView() {
    return view;
  }

  public static final class Builder {
    private Messager    messager;
    private Types       types;
    private Elements    elements;
    private TypeElement annotatedShell;
    private TypeElement view;

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

    public Builder setShell(TypeElement annotatedShell) {
      this.annotatedShell = annotatedShell;
      return this;
    }

    public ShellModel build()
      throws ProcessorException {
      return new ShellModel(this);
    }
  }
}
