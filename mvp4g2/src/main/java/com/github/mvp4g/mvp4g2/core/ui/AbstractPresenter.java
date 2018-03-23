package com.github.mvp4g.mvp4g2.core.ui;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;

public abstract class AbstractPresenter<E extends IsEventBus, V extends IsLazyReverseView<?>>
  extends AbstractHandler<E>
  implements IsPresenter<E, V> {

  protected V view = null;

  /*
   * (non-Javadoc)
   * @see com.mvp4g.core.presenter.PresenterInterface#getView()
   */
  public final V getView() {
    return view;
  }

  /*
   * (non-Javadoc)
   * @see com.mvp4g.core.presenter.PresenterInterface#setView(java.lang.Object)
   */
  public final void setView(V view) {
    this.view = view;
  }

}
