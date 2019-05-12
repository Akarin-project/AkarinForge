/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.metadata;

import java.util.UUID;
import org.bukkit.World;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;

public class WorldMetadataStore
extends MetadataStoreBase<World>
implements MetadataStore<World> {
    @Override
    protected String disambiguate(World world, String metadataKey) {
        return world.getUID().toString() + ":" + metadataKey;
    }
}

