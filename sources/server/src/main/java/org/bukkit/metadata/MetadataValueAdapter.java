/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.metadata;

import java.lang.ref.WeakReference;
import org.apache.commons.lang.Validate;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;

public abstract class MetadataValueAdapter
implements MetadataValue {
    protected final WeakReference<Plugin> owningPlugin;

    protected MetadataValueAdapter(Plugin owningPlugin) {
        Validate.notNull((Object)owningPlugin, (String)"owningPlugin cannot be null");
        this.owningPlugin = new WeakReference<Plugin>(owningPlugin);
    }

    @Override
    public Plugin getOwningPlugin() {
        return this.owningPlugin.get();
    }

    @Override
    public int asInt() {
        return NumberConversions.toInt(this.value());
    }

    @Override
    public float asFloat() {
        return NumberConversions.toFloat(this.value());
    }

    @Override
    public double asDouble() {
        return NumberConversions.toDouble(this.value());
    }

    @Override
    public long asLong() {
        return NumberConversions.toLong(this.value());
    }

    @Override
    public short asShort() {
        return NumberConversions.toShort(this.value());
    }

    @Override
    public byte asByte() {
        return NumberConversions.toByte(this.value());
    }

    @Override
    public boolean asBoolean() {
        Object value = this.value();
        if (value instanceof Boolean) {
            return (Boolean)value;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue() != 0;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String)value);
        }
        return value != null;
    }

    @Override
    public String asString() {
        Object value = this.value();
        if (value == null) {
            return "";
        }
        return value.toString();
    }
}

