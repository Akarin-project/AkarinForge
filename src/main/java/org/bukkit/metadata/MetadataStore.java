/*
 * Akarin Forge
 */
package org.bukkit.metadata;

import java.util.List;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public interface MetadataStore<T> {
    public void setMetadata(T var1, String var2, MetadataValue var3);

    public List<MetadataValue> getMetadata(T var1, String var2);

    public boolean hasMetadata(T var1, String var2);

    public void removeMetadata(T var1, String var2, Plugin var3);

    public void invalidateAll(Plugin var1);
}

