package de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterAnnotationUsingViewInterfaceAsClass;

import de.gishmo.gwt.mvp4g2.core.ui.IsLazyReverseView;

public interface MockOneView
  extends IsLazyReverseView<IMockOneView.Presenter>,
          IMockOneView {

  public void createView();

  public void bind();

}
