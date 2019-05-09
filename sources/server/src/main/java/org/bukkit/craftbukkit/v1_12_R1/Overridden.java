/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Overridden {
}

