package de.gishmo.gwt.mvp4g2.client.ui.internal;

import de.gishmo.gwt.mvp4g2.client.ui.IsLazyReverseView;
import de.gishmo.gwt.mvp4g2.client.ui.IsPresenter;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;

/**
 * Preenter meta data information.
 *
 * @param <P> the meta data presenter
 * @param <P> the meta data view
 */
public abstract class PresenterHandlerMetaData<P extends IsPresenter<?, ?>, V extends IsLazyReverseView<?>>
  extends HandlerMetaData {

  protected P                              presenter;
  protected V                              view;
  private   boolean                        multiple;
  private   Presenter.VIEW_CREATION_METHOD viewCreationMethod;

  public PresenterHandlerMetaData(String canonicalName,
                                  Kind kind,
                                  boolean multiple,
                                  Presenter.VIEW_CREATION_METHOD viewCreationMethod) {
    super(canonicalName,
          kind);
    this.multiple = multiple;
    this.viewCreationMethod = viewCreationMethod;
  }

  public P getPresenter() {
    return presenter;
  }

  public V getView() {
    return view;
  }

  public boolean isMultiple() {
    return multiple;
  }

  public Presenter.VIEW_CREATION_METHOD getViewCreationMethod() {
    return viewCreationMethod;
  }
}
