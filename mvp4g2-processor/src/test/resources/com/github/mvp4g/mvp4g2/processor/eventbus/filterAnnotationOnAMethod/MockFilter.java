package com.github.mvp4g.mvp4g2.processor.eventbus.filterAnnotationOnAMethod;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventFilter;

public class MockFilter
  implements IsEventFilter<FilterAnnotationOnAMethod> {

  @Override
  public boolean filterEvent(FilterAnnotationOnAMethod eventBus,
                             String eventName,
                             Object... params) {
    return true;
  }

}
