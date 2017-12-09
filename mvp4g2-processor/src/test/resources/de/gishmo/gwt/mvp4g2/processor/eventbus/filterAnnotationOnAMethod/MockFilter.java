package de.gishmo.gwt.mvp4g2.processor.eventbus.filterAnnotationOnAMethod;

import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventBus;
import de.gishmo.gwt.mvp4g2.client.eventbus.IsEventFilter;

public class MockFilter
  implements IsEventFilter<FilterAnnotationOnAMethod> {

  @Override
  public boolean filterEvent(FilterAnnotationOnAMethod eventBus,
                             String eventName,
                             Object... params) {
    return true;
  }

}
