/*
 * Copyright 2010 Pierre-Laurent Coirier
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
package gwt.mvp4g.client.handler;

import gwt.mvp4g.client.event.IsEventBus;

/**
 * Interface that defines a presenter.<br>
 * <br>
 * This interface provides getter and setter for a view.<br>
 * <br>
 * It is recommended to use directly <code>BasePresenter</code>.
 *
 * @param <V> Type of the view injected into the presenter
 * @param <E> Type of the event bus used by the presenter.
 *
 * @author Frank Hossfeld
 * @since 16.09.2016
 */
public interface IsPresenter<V, E extends IsEventBus>
  extends IsEventHandler<E> {

  /**
   * Gets the view associated with the presenter.
   *
   * @return view manipulated by the presenter.
   */
  public V getView();

  /**
   * Sets the view associated with the presenter.
   *
   * @param view view to set
   */
  public void setView(V view);

}
