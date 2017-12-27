package de.gishmo.gwt.mvp4g2.core.internal.application;

import de.gishmo.gwt.mvp4g2.core.annotation.internal.ForInternalUseOnly;
import de.gishmo.gwt.mvp4g2.core.application.IsApplicationLoader;

/**
 * Default applilcation loader
 * <p>
 * <p>does nothing</p>
 * <p>
 * <p>Used by the framework</p>
 */
@ForInternalUseOnly
public final class NoApplicationLoader
  implements IsApplicationLoader {

  @Override
  public void load(FinishLoadCommand finishLoadCommand) {
    finishLoadCommand.finishLoading();
  }
}
