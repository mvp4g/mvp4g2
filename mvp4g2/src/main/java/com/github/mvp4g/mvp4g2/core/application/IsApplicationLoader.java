/*
 * Copyright (c) 2018 - Frank Hossfeld
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 *
 */

package com.github.mvp4g.mvp4g2.core.application;

/**
 * MVP4G2 application loader
 * <br>
 * <p>The Loader is executed during the start sequence of the application.
 * The loader can be used to load meta-informations at the the start of the applilcation</p>
 * <p>Once the work is done call finishLoadCommand.finishLoad() to resume with the normal processing.</p>
 */
public interface IsApplicationLoader {

  void load(FinishLoadCommand finishLoadCommand);

  interface FinishLoadCommand {

    void finishLoading();

  }
}
