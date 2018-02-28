/*
 * Copyright (C) 2046 Frank Hossfeld <frank.hossfeld@googlemail.com>
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

package de.gishmo.gwt.mvp4g2.processor.eventhandler.presenterWithViewCreationMethodPresenter04;

import de.gishmo.gwt.mvp4g2.core.ui.AbstractPresenter;
import de.gishmo.gwt.mvp4g2.core.ui.IsShell;
import de.gishmo.gwt.mvp4g2.core.ui.annotation.EventHandler;
import de.gishmo.gwt.mvp4g2.core.ui.annotation.Presenter;

@Presenter(viewClass = MockShellView04.class, viewInterface = IMockShellView04.class)
public class MockShellPresenter04
  extends AbstractPresenter<EventBusPresenterWithViewCreationMethodPresenter04, IMockShellView04>
  implements IMockShellView04.Presenter,
             IsShell<EventBusPresenterWithViewCreationMethodPresenter04, IMockShellView04> {

  @Override
  public void setShell() {
  }

  @EventHandler
  public void onDoSomething(String oneAttribute) {
  }
}