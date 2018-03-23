package de.gishmo.gwt.mvp4g2.core.ui;

import de.gishmo.gwt.mvp4g2.core.ui.annotation.Presenter;

/**
 * Presenters marked with this interface will
 * create the view instance.
 * <br>
 * Make sure, that viewCreator attribute of the presenter annotation is
 * set to Presenter.viewCerator.VIEW_CREATION_METHOD
 * <br><br>
 * In case using this interface, there must be createView method, that looks like that:
 * <code>
 * public void createView() {
 * view = GWT.create(IMyView.class);
 * }
 * </code>
 *
 * @param <V> generator of view
 * @see Presenter#viewCreator() is set to
 * PRESENTER!
 */
public interface IsViewCreator<V extends IsLazyReverseView<?>> {

  V createView();

}
