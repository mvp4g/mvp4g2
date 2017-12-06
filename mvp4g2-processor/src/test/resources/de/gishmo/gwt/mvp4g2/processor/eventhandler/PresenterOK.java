/*
 * Copyright (C) 2016 Frank Hossfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.gishmo.gwt.mvp4g2.processor.eventhandler;

import de.gishmo.gwt.mvp4g2.client.ui.AbstractPresenter;
import de.gishmo.gwt.mvp4g2.client.ui.annotation.Presenter;

import de.gishmo.gwt.mvp4g2.processor.mock.IMockOneView;
import de.gishmo.gwt.mvp4g2.processor.mock.MockEventBus;
import de.gishmo.gwt.mvp4g2.processor.mock.MockOneView;

@Presenter(viewClass = MockOneView.class, viewInterface = IMockOneView.class)
public class PresenterOK
  extends AbstractPresenter<MockEventBus, IMockOneView> {
}