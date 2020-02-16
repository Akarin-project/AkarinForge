package io.akarin.forge.executor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

public class ReflectionExecutor
implements EventExecutor {
    private final Method method;
    private final Class<? extends Event> eventClass;

    public ReflectionExecutor(Method method, Class<? extends Event> eventClass) {
        this.method = method;
        this.eventClass = eventClass;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        try {
            if (!this.eventClass.isAssignableFrom(event.getClass())) {
                return;
            }
            boolean isAsync = event.isAsynchronous();
            this.method.invoke(listener, event);
        }
        catch (InvocationTargetException ex2) {
            throw new EventException(ex2.getCause());
        }
        catch (Throwable t2) {
            throw new EventException(t2);
        }
    }
}

