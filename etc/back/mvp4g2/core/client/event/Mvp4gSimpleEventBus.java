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

import java.util.*;

/**
 * Basic implementation of {@link Mvp4gEventBus}.
 */
public final class Mvp4gSimpleEventBus
  extends Mvp4gEventBus {

  /**
   * Map of event type to map of event source to list of their handlers.
   */
  private final Map<Mvp4gEvent.Type<?>, Map<Object, List<?>>> map         = new HashMap<Mvp4gEvent.Type<?>, Map<Object, List<?>>>();
  private       int                                           firingDepth = 0;

  /**
   * Add and remove operations received during dispatch.
   */
  private List<Command> deferredDeltas;

  public Mvp4gSimpleEventBus() {
    super();
  }

  @Override
  public <H> Mvp4gHandlerRegistration addHandler(Mvp4gEvent.Type<H> type,
                                                 H handler) {
    return doAdd(type,
                 null,
                 handler);
  }

  @Override
  public <H> Mvp4gHandlerRegistration addHandlerToSource(final Mvp4gEvent.Type<H> type,
                                                         final Object source,
                                                         final H handler) {
    if (source == null) {
      throw new NullPointerException("Cannot add a handler with a null source");
    }

    return doAdd(type,
                 source,
                 handler);
  }

  @Override
  public void fireEvent(Mvp4gEvent<?> event) {
    doFire(event, null);
  }

  @Override
  public void fireEventFromSource(Mvp4gEvent<?> event,
                                  Object source) {
    if (source == null) {
      throw new NullPointerException("Cannot fire from a null source");
    }
    doFire(event,
           source);
  }


  private <H> void doFire(Mvp4gEvent<H> event,
                          Object source) {
    if (event == null) {
      throw new NullPointerException("Cannot fire null event");
    }
    try {
      firingDepth++;

      if (source != null) {
        setSourceOfEvent(event,
                         source);
      }

      List<H> handlers = getDispatchList(event.getAssociatedType(),
                                         source);
      Set<Throwable> causes = null;

      ListIterator<H> it = handlers.listIterator();
      while (it.hasNext()) {
        H handler = it.next();
        try {
          dispatchEvent(event,
                        handler);
        } catch (Throwable e) {
          if (causes == null) {
            causes = new HashSet<Throwable>();
          }
          causes.add(e);
        }
      }

      if (causes != null) {
        throw new Mvp4gUmbrellaException(causes);
      }
    } finally {
      firingDepth--;
      if (firingDepth == 0) {
        handleQueuedAddsAndRemoves();
      }
    }
  }

  private <H> List<H> getDispatchList(Mvp4gEvent.Type<H> type,
                                      Object source) {
    List<H> directHandlers = getHandlerList(type,
                                            source);
    if (source == null) {
      return directHandlers;
    }

    List<H> globalHandlers = getHandlerList(type,
                                            null);

    List<H> rtn = new ArrayList<H>(directHandlers);
    rtn.addAll(globalHandlers);
    return rtn;
  }

  private void handleQueuedAddsAndRemoves() {
    if (deferredDeltas != null) {
      try {
        for (Command c : deferredDeltas) {
          c.execute();
        }
      } finally {
        deferredDeltas = null;
      }
    }
  }

  private <H> List<H> getHandlerList(Mvp4gEvent.Type<H> type,
                                     Object source) {
    Map<Object, List<?>> sourceMap = map.get(type);
    if (sourceMap == null) {
      return Collections.emptyList();
    }

    // safe, we control the puts.
    @SuppressWarnings("unchecked") List<H> handlers = (List<H>) sourceMap.get(source);
    if (handlers == null) {
      return Collections.emptyList();
    }

    return handlers;
  }

  private <H> Mvp4gHandlerRegistration doAdd(final Mvp4gEvent.Type<H> type,
                                             final Object source,
                                             final H handler) {
    if (type == null) {
      throw new NullPointerException("Cannot add a handler with a null type");
    }
    if (handler == null) {
      throw new NullPointerException("Cannot add a null handler");
    }

    if (firingDepth > 0) {
      enqueueAdd(type,
                 source,
                 handler);
    } else {
      doAddNow(type,
               source,
               handler);
    }

    return new Mvp4gHandlerRegistration() {
      public void removeHandler() {
        doRemoveNow(type,
                    source,
                    handler);
      }
    };
  }

  private <H> void doAddNow(Mvp4gEvent.Type<H> type,
                            Object source,
                            H handler) {
    List<H> l = ensureHandlerList(type,
                                  source);
    l.add(handler);
  }

  private <H> void enqueueAdd(final Mvp4gEvent.Type<H> type,
                              final Object source,
                              final H handler) {
    defer(new Command() {
      public void execute() {
        doAddNow(type,
                 source,
                 handler);
      }
    });
  }

  private <H> void enqueueRemove(final Mvp4gEvent.Type<H> type,
                                 final Object source,
                                 final H handler) {
    defer(new Command() {
      public void execute() {
        doRemoveNow(type,
                    source,
                    handler);
      }
    });
  }

  private void defer(Command command) {
    if (deferredDeltas == null) {
      deferredDeltas = new ArrayList<Command>();
    }
    deferredDeltas.add(command);
  }

  private <H> void doRemoveNow(Mvp4gEvent.Type<H> type,
                               Object source,
                               H handler) {
    List<H> l = getHandlerList(type,
                               source);

    boolean removed = l.remove(handler);

    if (removed && l.isEmpty()) {
      prune(type,
            source);
    }
  }

  private void prune(Mvp4gEvent.Type<?> type,
                     Object source) {
    Map<Object, List<?>> sourceMap = map.get(type);

    List<?> pruned = sourceMap.remove(source);

    assert pruned != null : "Can't prune what wasn't there";
    assert pruned.isEmpty() : "Pruned unempty list!";

    if (sourceMap.isEmpty()) {
      map.remove(type);
    }
  }

  private <H> List<H> ensureHandlerList(Mvp4gEvent.Type<H> type,
                                        Object source) {
    Map<Object, List<?>> sourceMap = map.get(type);
    if (sourceMap == null) {
      sourceMap = new HashMap<Object, List<?>>();
      map.put(type,
              sourceMap);
    }

    // safe, we control the puts.
    @SuppressWarnings("unchecked") List<H> handlers = (List<H>) sourceMap.get(source);
    if (handlers == null) {
      handlers = new ArrayList<H>();
      sourceMap.put(source,
                    handlers);
    }

    return handlers;
  }

  private interface Command {
    void execute();
  }
}
