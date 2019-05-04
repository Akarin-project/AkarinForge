/*
 * Akarin Forge
 */
package org.bukkit.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

public class HandlerList {
    private volatile RegisteredListener[] handlers = null;
    private final EnumMap<EventPriority, ArrayList<RegisteredListener>> handlerslots = new EnumMap(EventPriority.class);
    private static ArrayList<HandlerList> allLists = new ArrayList();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void bakeAll() {
        ArrayList<HandlerList> arrayList = allLists;
        synchronized (arrayList) {
            for (HandlerList h2 : allLists) {
                h2.bake();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void unregisterAll() {
        ArrayList<HandlerList> arrayList = allLists;
        synchronized (arrayList) {
            Iterator<HandlerList> iterator = allLists.iterator();
            while (iterator.hasNext()) {
                HandlerList h2;
                HandlerList handlerList = h2 = iterator.next();
                synchronized (handlerList) {
                    for (List list : h2.handlerslots.values()) {
                        list.clear();
                    }
                    h2.handlers = null;
                    continue;
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void unregisterAll(Plugin plugin) {
        ArrayList<HandlerList> arrayList = allLists;
        synchronized (arrayList) {
            for (HandlerList h2 : allLists) {
                h2.unregister(plugin);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void unregisterAll(Listener listener) {
        ArrayList<HandlerList> arrayList = allLists;
        synchronized (arrayList) {
            for (HandlerList h2 : allLists) {
                h2.unregister(listener);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public HandlerList() {
        for (EventPriority o2 : EventPriority.values()) {
            this.handlerslots.put(o2, new ArrayList());
        }
        ArrayList<HandlerList> arrayList = allLists;
        synchronized (arrayList) {
            allLists.add(this);
        }
    }

    public synchronized void register(RegisteredListener listener) {
        if (this.handlerslots.get((Object)listener.getPriority()).contains(listener)) {
            throw new IllegalStateException("This listener is already registered to priority " + listener.getPriority().toString());
        }
        this.handlers = null;
        this.handlerslots.get((Object)listener.getPriority()).add(listener);
    }

    public void registerAll(Collection<RegisteredListener> listeners) {
        for (RegisteredListener listener : listeners) {
            this.register(listener);
        }
    }

    public synchronized void unregister(RegisteredListener listener) {
        if (this.handlerslots.get((Object)listener.getPriority()).remove(listener)) {
            this.handlers = null;
        }
    }

    public synchronized void unregister(Plugin plugin) {
        boolean changed = false;
        for (List list : this.handlerslots.values()) {
            ListIterator i2 = list.listIterator();
            while (i2.hasNext()) {
                if (!((RegisteredListener)i2.next()).getPlugin().equals(plugin)) continue;
                i2.remove();
                changed = true;
            }
        }
        if (changed) {
            this.handlers = null;
        }
    }

    public synchronized void unregister(Listener listener) {
        boolean changed = false;
        for (List list : this.handlerslots.values()) {
            ListIterator i2 = list.listIterator();
            while (i2.hasNext()) {
                if (!((RegisteredListener)i2.next()).getListener().equals(listener)) continue;
                i2.remove();
                changed = true;
            }
        }
        if (changed) {
            this.handlers = null;
        }
    }

    public synchronized void bake() {
        if (this.handlers != null) {
            return;
        }
        ArrayList entries = new ArrayList();
        for (Map.Entry<EventPriority, ArrayList<RegisteredListener>> entry : this.handlerslots.entrySet()) {
            entries.addAll(entry.getValue());
        }
        this.handlers = entries.toArray(new RegisteredListener[entries.size()]);
    }

    public RegisteredListener[] getRegisteredListeners() {
        RegisteredListener[] handlers;
        while ((handlers = this.handlers) == null) {
            this.bake();
        }
        return handlers;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ArrayList<RegisteredListener> getRegisteredListeners(Plugin plugin) {
        ArrayList<RegisteredListener> listeners = new ArrayList<RegisteredListener>();
        ArrayList<HandlerList> arrayList = allLists;
        synchronized (arrayList) {
            Iterator<HandlerList> iterator = allLists.iterator();
            while (iterator.hasNext()) {
                HandlerList h2;
                HandlerList handlerList = h2 = iterator.next();
                synchronized (handlerList) {
                    for (List list : h2.handlerslots.values()) {
                        for (RegisteredListener listener : list) {
                            if (!listener.getPlugin().equals(plugin)) continue;
                            listeners.add(listener);
                        }
                    }
                    continue;
                }
            }
        }
        return listeners;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ArrayList<HandlerList> getHandlerLists() {
        ArrayList<HandlerList> arrayList = allLists;
        synchronized (arrayList) {
            return (ArrayList)allLists.clone();
        }
    }
}

