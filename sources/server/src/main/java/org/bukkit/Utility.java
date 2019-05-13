/*
 * Akarin Forge
 */
package org.bukkit;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(value=RetentionPolicy.SOURCE)
public @interface Utility {
}

