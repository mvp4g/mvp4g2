package de.gishmo.gwt.mvp4g2.processor.eventbus.eventBusNotExtendingIsEventBus;

import de.gishmo.gwt.mvp4g2.client.eventbus.annotation.EventBus;
import de.gishmo.gwt.mvp4g2.processor.mock.MockShellPresenter;

@EventBus(shell = MockShellPresenter.class)
public interface EventBusNotExtendingIsEventBus {
}
