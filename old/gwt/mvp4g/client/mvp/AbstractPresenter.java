/*
 * Copyright 2009 Pierre-Laurent Coirier
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwt.mvp4g.client.mvp;

import gwt.mvp4g.client.event.IsEventBus;
import gwt.mvp4g.client.handler.AbstractEventHandler;
import gwt.mvp4g.client.handler.IsPresenter;

/**
 * Default implementation of <code>IsPresenter</code> interface.<br>
 * <br>
 * This implementation has one attribute:
 * <ul>
 * <li>a view</li>
 * </ul>
 * You should extend this class to create a presenter.<br>
 * <br>
 *
 * @param <V> Type of the view injected into the presenter
 * @param <E> Type of the event bus used by the presenter.
 * @author plcoirier
 */
public abstract class AbstractPresenter<V, E extends IsEventBus>
  extends AbstractEventHandler<E>
  implements IsPresenter<V, E> {

  protected V view = null;

  /*
   * (non-Javadoc)
   * @see de.gishmo.gwt.mvp4g.client.handler.IsPresenter#getView()
   */
  @Override
  public V getView() {
    return view;
  }

  /*
   * (non-Javadoc)
   * @see de.gishmo.gwt.mvp4g.client.handler.IsPresenter#setView(java.lang.Object)
   */
  @Override
  public void setView(V view) {
    this.view = view;
  }


}
