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

package de.gishmo.gwt.mvp4g2.processor;

import de.gishmo.gwt.mvp4g2.processor.application.ApplicationTest;
import de.gishmo.gwt.mvp4g2.processor.eventBus.DebugTest;
import de.gishmo.gwt.mvp4g2.processor.event.StartEventTest;
import de.gishmo.gwt.mvp4g2.processor.eventBus.EventbusTest;
import de.gishmo.gwt.mvp4g2.processor.eventBus.FilterTest;
import de.gishmo.gwt.mvp4g2.processor.eventhandler.EventHandlerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ApplicationTest.class,
                     DebugTest.class,
                     EventbusTest.class,
                     EventHandlerTest.class,
                     FilterTest.class,
                     StartEventTest.class})
public class AllTests {
}
