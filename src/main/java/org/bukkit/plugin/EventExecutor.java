/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.plugin;

import com.google.common.base.Preconditions;

import io.akarin.forge.executor.MethodHandleEventExecutor;
import io.akarin.forge.executor.StaticMethodHandleEventExecutor;
import io.akarin.forge.executor.asm.ASMEventExecutorGenerator;
import io.akarin.forge.executor.asm.ClassDefiner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;

public interface EventExecutor {
    public static final ConcurrentMap<Method, Class<? extends EventExecutor>> eventExecutorMap = new ConcurrentHashMap<Method, Class<? extends EventExecutor>>(){

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Class<? extends EventExecutor> computeIfAbsent(Method key, Function<? super Method, ? extends Class<? extends EventExecutor>> mappingFunction) {
            Class executorClass = (Class)this.get(key);
            if (executorClass != null) {
                return executorClass;
            }
            Method method = key;
            synchronized (method) {
                executorClass = (Class)this.get(key);
                if (executorClass != null) {
                    return executorClass;
                }
                return super.computeIfAbsent(key, mappingFunction);
            }
        }
    };

    public void execute(Listener var1, Event var2) throws EventException;

    default public static EventExecutor create(Method m2, final Class<? extends Event> eventClass) {
        Preconditions.checkNotNull((Object)m2, (Object)"Null method");
        Preconditions.checkArgument((boolean)(m2.getParameterCount() != 0), (String)"Incorrect number of arguments %s", (int)m2.getParameterCount());
        Preconditions.checkArgument((boolean)(m2.getParameterTypes()[0] == eventClass), (String)"First parameter %s doesn't match event class %s", m2.getParameterTypes()[0], eventClass);
        ClassDefiner definer = ClassDefiner.getInstance();
        if (Modifier.isStatic(m2.getModifiers())) {
            return new StaticMethodHandleEventExecutor(eventClass, m2);
        }
        if (definer.isBypassAccessChecks() || Modifier.isPublic(m2.getDeclaringClass().getModifiers()) && Modifier.isPublic(m2.getModifiers())) {
            Class executorClass = eventExecutorMap.computeIfAbsent(m2, __ -> {
                String name = ASMEventExecutorGenerator.generateName();
                byte[] classData = ASMEventExecutorGenerator.generateEventExecutor(m2, name);
                return definer.defineClass(m2.getDeclaringClass().getClassLoader(), name, classData).asSubclass(EventExecutor.class);
            }
            );
            try {
                final EventExecutor asmExecutor = (EventExecutor)executorClass.newInstance();
                return new EventExecutor(){

                    @Override
                    public void execute(Listener listener, Event event) throws EventException {
                        if (!eventClass.isInstance(event)) {
                            return;
                        }
                        try {
                            asmExecutor.execute(listener, event);
                        }
                        catch (Exception e2) {
                            throw new EventException(e2);
                        }
                    }
                };
            }
            catch (IllegalAccessException | InstantiationException e2) {
                throw new AssertionError("Unable to initialize generated event executor", e2);
            }
        }
        return new MethodHandleEventExecutor(eventClass, m2);
    }

}

