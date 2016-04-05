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

package org.gwt4e.mvp4g.processor.context;

import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * <p>Processor context classes are containing information we will need
 * to generate the code. Getting the information is done inside fo the classes.
 * <br><br>
 * Contains base stuff of a processor.</p>
 */
public abstract class AbstractProcessorContext {

  final Messager messager;
  final Types    types;
  final Elements elements;

  AbstractProcessorContext(Messager messager,
                           Types types,
                           Elements elements) {
    super();

    this.messager = messager;
    this.types = types;
    this.elements = elements;
  }

  public Messager getMessager() {
    return messager;
  }

  public Types getTypes() {
    return types;
  }

  public Elements getElements() {
    return elements;
  }
}
