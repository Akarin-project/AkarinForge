/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit;

import com.google.common.base.Preconditions;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.plugin.Plugin;

public final class NamespacedKey {
    public static final String MINECRAFT = "minecraft";
    public static final String BUKKIT = "bukkit";
    private static final Pattern VALID_NAMESPACE = Pattern.compile("[a-z0-9._-]+");
    private static final Pattern VALID_KEY = Pattern.compile("[a-z0-9/._-]+");
    private final String namespace;
    private final String key;

    @Deprecated
    public NamespacedKey(String namespace, String key) {
        Preconditions.checkArgument((boolean)(namespace != null && VALID_NAMESPACE.matcher(namespace).matches()), (Object)"namespace");
        Preconditions.checkArgument((boolean)(key != null && VALID_KEY.matcher(key).matches()), (Object)"key");
        this.namespace = namespace;
        this.key = key;
        String string = this.toString();
        Preconditions.checkArgument((boolean)(string.length() < 256), (String)"NamespacedKey must be less than 256 characters", (Object)string);
    }

    public NamespacedKey(Plugin plugin, String key) {
        Preconditions.checkArgument((boolean)(plugin != null), (Object)"plugin");
        Preconditions.checkArgument((boolean)(key != null), (Object)"key");
        this.namespace = plugin.getName().toLowerCase(Locale.ROOT);
        this.key = key.toLowerCase().toLowerCase(Locale.ROOT);
        Preconditions.checkArgument((boolean)VALID_NAMESPACE.matcher(this.namespace).matches(), (Object)"namespace");
        Preconditions.checkArgument((boolean)VALID_KEY.matcher(this.key).matches(), (Object)"key");
        String string = this.toString();
        Preconditions.checkArgument((boolean)(string.length() < 256), (String)"NamespacedKey must be less than 256 characters (%s)", (Object)string);
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getKey() {
        return this.key;
    }

    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.namespace.hashCode();
        hash = 47 * hash + this.key.hashCode();
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        NamespacedKey other = (NamespacedKey)obj;
        return this.namespace.equals(other.namespace) && this.key.equals(other.key);
    }

    public String toString() {
        return this.namespace + ":" + this.key;
    }

    @Deprecated
    public static NamespacedKey randomKey() {
        return new NamespacedKey("bukkit", UUID.randomUUID().toString());
    }

    public static NamespacedKey minecraft(String key) {
        return new NamespacedKey("minecraft", key);
    }
}

