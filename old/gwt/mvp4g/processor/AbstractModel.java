package gwt.mvp4g.processor;

import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by hoss on 19.09.16.
 */
public abstract class AbstractModel {

  protected final Types    types;
  protected final Messager messager;
  protected final Elements elements;

  protected AbstractModel(Messager messager,
                                Types types,
                                Elements elements) {
    this.types = types;
    this.messager = messager;
    this.elements = elements;
  }
}
