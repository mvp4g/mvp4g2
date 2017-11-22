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
package de.gishmo.gwt.mvp4g2.core.client.event;

import de.gishmo.gwt.mvp4g2.core.client.annotation.NotUpdatedYet;

/**
 * Base Mvp4gInternalEvent object.
 *
 * @param <H>
 *   interface implemented by handlers of this kind of event
 */
@NotUpdatedYet
public abstract class Mvp4gEvent<H> {
  private Object source;

  /**
   * Constructor.
   */
  protected Mvp4gEvent() {
  }

  /**
   * Returns the {@link Type} used to register this event, allowing an
   * {@link Mvp4gEventBus} to find handlers of the appropriate class.
   *
   * @return the type
   */
  public abstract Type<H> getAssociatedType();

  /**
   * Returns the source for this event. The type and meaning of the
   * source is arbitrary, and is most useful as a secondary key for
   * handler registration. (See {@link Mvp4gEventBus#addHandlerToSource},
   * which allows a handler to register for events of a particular
   * type, tied to a particular source.) <p> Note that the source is
   * actually set at dispatch time, e.g. via {@link
   * Mvp4gEventBus#fireEventFromSource(Mvp4gEvent, Object)}.
   *
   * @return object representing the source of this event
   */
  public Object getSource() {
    return source;
  }

  /**
   * Set the source that triggered this event. Intended to be called
   * by the {@link Mvp4gEventBus} during dispatch.
   *
   * @param source
   *   the source of this event.
   *
   * @see Mvp4gEventBus#fireEventFromSource(Mvp4gEvent, Object)
   * @see Mvp4gEventBus#setSourceOfEvent(Mvp4gEvent, Object)
   */
  protected void setSource(Object source) {
    this.source = source;
  }

  /**
   * This is a method used primarily for debugging. It gives a string
   * representation of the event details. This does not override the
   * toString method because the compiler cannot always optimize
   * toString out correctly. Mvp4gInternalEvent types should override as desired.
   *
   * @return a string representing the event's specifics.
   */
  public String toDebugString() {
    String name = this.getClass()
                      .getName();
    name = name.substring(name.lastIndexOf(".") + 1);
    return "event: " + name + ":";
  }

  /**
   * Implemented by subclasses to to invoke their handlers in a type safe
   * manner. Intended to be called by {@link Mvp4gEventBus#fireEvent(Mvp4gEvent)} or
   * {@link Mvp4gEventBus#fireEventFromSource(Mvp4gEvent, Object)}.
   *
   * @param handler
   *   handler
   *
   * @see Mvp4gEventBus#dispatchEvent(Mvp4gEvent, Object)
   */
  protected abstract void dispatch(H handler);

  /**
   * Type class used to register events with an {@link Mvp4gEventBus}.
   *
   * @param <H>
   *   handler type
   */
  public static class Type<H> {
    private static int nextHashCode;
    private final  int index;

    /**
     * Constructor.
     */
    public Type() {
      index = ++nextHashCode;
    }

    @Override
    public final int hashCode() {
      return index;
    }

    @Override
    public String toString() {
      return "Mvp4gInternalEvent type";
    }
  }

  /**
   * The toString() for abstract event is overridden to avoid accidently
   * including class literals in the compiled output. Use {@link Mvp4gEvent}
   * #toDebugString to get more information about the event.
   */
  @Override
  public String toString() {
    return "An event type";
  }

}
