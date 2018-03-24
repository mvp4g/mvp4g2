package com.github.mvp4g.mvp4g2.core.internal.application;

import com.github.mvp4g.mvp4g2.core.application.IsApplicationLoader;
import com.github.mvp4g.mvp4g2.core.internal.ForInternalUseOnly;

/**
 * Default applilcation loader
 * <p>does nothing</p>
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
