package de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterAnnotationUsingViewAbstractClass;

import de.gishmo.gwt.mvp4g2.client.ui.LazyReverseView;

public abstract class MockOneView
  extends LazyReverseView<IMockOneView.Presenter>
  implements IMockOneView {

  public void bind();

  public void createView();

}
