/*
 * Akarin Forge
 */
package org.bukkit.metadata;

import org.bukkit.plugin.Plugin;

public interface MetadataValue {
    public Object value();

    public int asInt();

    public float asFloat();

    public double asDouble();

    public long asLong();

    public short asShort();

    public byte asByte();

    public boolean asBoolean();

    public String asString();

    public Plugin getOwningPlugin();

    public void invalidate();
}

