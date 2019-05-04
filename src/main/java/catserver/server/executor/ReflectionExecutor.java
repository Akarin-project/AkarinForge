/*
 * Decompiled with CFR 0_119.
 */
package catserver.server.executor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.spigotmc.CustomTimingsHandler;

public class ReflectionExecutor
implements EventExecutor {
    private final Method method;
    private final Class<? extends Event> eventClass;
    private final CustomTimingsHandler timings;

    public ReflectionExecutor(Method method, Class<? extends Event> eventClass, CustomTimingsHandler timings) {
        this.method = method;
        this.eventClass = eventClass;
        this.timings = timings;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        try {
            if (!this.eventClass.isAssignableFrom(event.getClass())) {
                return;
            }
            boolean isAsync = event.isAsynchronous();
            if (!isAsync) {
                this.timings.startTiming();
            }
            this.method.invoke(listener, event);
            if (!isAsync) {
                this.timings.stopTiming();
            }
        }
        catch (InvocationTargetException ex2) {
            throw new EventException(ex2.getCause());
        }
        catch (Throwable t2) {
            throw new EventException(t2);
        }
    }
}

