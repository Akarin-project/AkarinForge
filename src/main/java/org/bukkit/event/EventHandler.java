/*
 * Akarin Forge
 */
package org.bukkit.event;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bukkit.event.EventPriority;

@Target(value={ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface EventHandler {
    public EventPriority priority() default EventPriority.NORMAL;

    public boolean ignoreCancelled() default 0;
}

