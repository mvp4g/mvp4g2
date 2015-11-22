/*
 * Copyright (C) 2015 Frank Hossfeld
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

/*
 * Copyright (C) 2015 Frank Hossfeld
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

/*
 * Copyright (C) 2015 Frank Hossfeld
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

/*
 * Copyright (C) 2015 Frank Hossfeld
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

package org.gwt4e.mvp4g2.test.apt.eventbus;


import com.google.testing.compile.JavaFileObjects;
import org.gwt4e.mvp4g2.processor.EventBusProcessor;
import org.junit.Test;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.truth0.Truth.ASSERT;


public class EventBusTest {


  /**
   * <p>Test:
   * <br>
   * EventBus is not an Interface.
   * <br><br>
   * Expected result:
   * <br>
   * error message: org.gwt4e.mvp4g2.test.apt.eventbus.EventBusIsNotAnInterface applied on a type that's not an interface; ignoring
   * </p>
   */
  @Test
  public void EventBusIsNotAnInterfaceTest() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g2/test/apt/eventbus/EventBusIsNotAnInterface.java"))
          .processedWith(new EventBusProcessor())
          .failsToCompile()
          .withErrorContaining("org.gwt4e.mvp4g2.test.apt.eventbus.EventBusIsNotAnInterface applied on a type that's not an interface; ignoring");
  }

  /**
   * <p>Test:
   * <br>
   * Implementing Mvp4g2EventBus interface.
   * <br><br>
   * Expected result:
   * <br>
   * error message: org.gwt4e.mvp4g2.test.apt.eventbus.NoMvp4g2EventBusInterface: org.gwt4e.mvp4g2.client.annotations.EventBus applied on a type that doesn't implement org.gwt4e.mvp4g2.client.Mvp4g2EventBus; ignoring
   * </p>
   */
  @Test
  public void NoMvp4g2EventBusInterfaceTest() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g2/test/apt/eventbus/NoMvp4g2EventBusInterface.java"))
          .processedWith(new EventBusProcessor())
          .failsToCompile()
          .withErrorContaining("org.gwt4e.mvp4g2.test.apt.eventbus.NoMvp4g2EventBusInterface: org.gwt4e.mvp4g2.client.annotations.EventBus applied on a type that doesn't implement org.gwt4e.mvp4g2.client.Mvp4g2EventBus; ignoring");
  }

  /**
   * <p>Test:
   * <br>
   * Event bus with no events
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithNoEventsTest() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g2/test/apt/eventbus/EventBusWithNoEvents.java"))
          .processedWith(new EventBusProcessor())
          .failsToCompile()
          .withErrorContaining("org.gwt4e.mvp4g2.test.apt.eventbus.EventBusWithNoEvents has no events defined");
  }

  /**
   * <p>Test:
   * <br>
   * Event bus with no events
   * <br><br>
   * Expected result:
   * <br>
   * error message:
   * </p>
   */
  @Test
  public void EventBusWithOneEventsTest() {
    ASSERT.about(javaSource())
          .that(JavaFileObjects.forResource("org/gwt4e/mvp4g2/test/apt/eventbus/EventBusWithOneEvents.java"))
          .processedWith(new EventBusProcessor())
          .compilesWithoutError();
  }

}
