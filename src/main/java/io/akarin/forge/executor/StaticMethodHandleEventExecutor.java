/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package io.akarin.forge.executor;

import com.google.common.base.Preconditions;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

public class StaticMethodHandleEventExecutor
implements EventExecutor {
    private final Class<? extends Event> eventClass;
    private final MethodHandle handle;

    public StaticMethodHandleEventExecutor(Class<? extends Event> eventClass, Method m2) {
        Preconditions.checkArgument((boolean)Modifier.isStatic(m2.getModifiers()), (String)"Not a static method: %s", (Object)m2);
        this.eventClass = eventClass;
        try {
            m2.setAccessible(true);
            this.handle = MethodHandles.lookup().unreflect(m2);
        }
        catch (IllegalAccessException e2) {
            throw new AssertionError("Unable to set accessible", e2);
        }
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        if (!this.eventClass.isInstance(event)) {
            return;
        }
        try {
            this.handle.invoke(event);
        }
        catch (Throwable t2) {
            throw new EventException(t2);
        }
    }
}

