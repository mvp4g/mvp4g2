package org.gwt4e.mvp4g.test.apt.application.ApplicationOK.generated;

import com.google.gwt.user.client.ui.HasWidgets;
import org.gwt4e.mvp4g.client.AbstractApplication;

public class ApplicationOKImpl extends AbstractApplication {
  public void run(HasWidgets.ForIsWidget viewPort) {
    new ApplicationOKModuleImpl(getEventBus());
  }
}