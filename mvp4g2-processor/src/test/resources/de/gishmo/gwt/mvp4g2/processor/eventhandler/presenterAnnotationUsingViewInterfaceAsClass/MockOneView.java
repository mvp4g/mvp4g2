package  de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterAnnotationUsingViewInterfaceAsClass;

import de.gishmo.gwt.mvp4g2.client.ui.IsLazyReverseView;
import de.gishmo.gwt.mvp4g2.client.ui.LazyReverseView;

public interface MockOneView
  extends IsLazyReverseView<IMockOneView.Presenter>, IMockOneView {

  public void bind();

  public void createView();

}
