/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package org.bukkit;

import com.google.common.collect.ImmutableMap;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Target(value={ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Warning {
    public boolean value() default 0;

    public String reason() default "";

    public static enum WarningState {
        ON,
        OFF,
        DEFAULT;
        
        private static final Map<String, WarningState> values;

        private WarningState() {
        }

        public boolean printFor(Warning warning) {
            if (this == DEFAULT) {
                return warning == null || warning.value();
            }
            return this == ON;
        }

        public static WarningState value(String value) {
            if (value == null) {
                return DEFAULT;
            }
            WarningState state = values.get(value.toLowerCase());
            if (state == null) {
                return DEFAULT;
            }
            return state;
        }

        static {
            values = ImmutableMap.builder().put((Object)"off", (Object)OFF).put((Object)"false", (Object)OFF).put((Object)"f", (Object)OFF).put((Object)"no", (Object)OFF).put((Object)"n", (Object)OFF).put((Object)"on", (Object)ON).put((Object)"true", (Object)ON).put((Object)"t", (Object)ON).put((Object)"yes", (Object)ON).put((Object)"y", (Object)ON).put((Object)"", (Object)DEFAULT).put((Object)"d", (Object)DEFAULT).put((Object)"default", (Object)DEFAULT).build();
        }
    }

}

