/*
 * Akarin Forge
 */
package org.bukkit.metadata;

import java.util.List;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public interface Metadatable {
    public void setMetadata(String var1, MetadataValue var2);

    public List<MetadataValue> getMetadata(String var1);

    public boolean hasMetadata(String var1);

    public void removeMetadata(String var1, Plugin var2);
}

