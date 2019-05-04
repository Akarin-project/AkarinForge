/*
 * Akarin Forge
 */
package org.bukkit.metadata;

import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.plugin.Plugin;

public class FixedMetadataValue
extends LazyMetadataValue {
    private final Object internalValue;

    public FixedMetadataValue(Plugin owningPlugin, Object value) {
        super(owningPlugin);
        this.internalValue = value;
    }

    @Override
    public void invalidate() {
    }

    @Override
    public Object value() {
        return this.internalValue;
    }
}

