/*
 * Copyright (C) 2036 Frank Hossfeld <frank.hossfeld@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.mvp4g.mvp4g2.processor.eventhandler.presenterWithViewCreationMethodPresenter03;

import com.github.mvp4g.mvp4g2.core.ui.AbstractPresenter;
import com.github.mvp4g.mvp4g2.core.ui.IsViewCreator;
import com.github.mvp4g.mvp4g2.core.ui.annotation.EventHandler;
import com.github.mvp4g.mvp4g2.core.ui.annotation.Presenter;

@Presenter(viewClass = MockView03.class, viewInterface = IMockView03.class, viewCreator = Presenter.VIEW_CREATION_METHOD.PRESENTER)
public class MockPresenter03
  extends AbstractPresenter<EventBusPresenterWithViewCreationMethodPresenter03, IMockView03>
  implements IMockView03.Presenter,
             IsViewCreator {

  @Override
  public void set() {
  }

  @EventHandler
  public void onDoSomething(String oneAttribute) {
  }

  @Override
  public IMockView03 createView() {
    return new MockView03();
  }
}
