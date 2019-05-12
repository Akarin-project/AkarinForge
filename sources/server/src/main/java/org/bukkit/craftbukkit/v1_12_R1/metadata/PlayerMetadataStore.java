/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.metadata;

import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;

public class PlayerMetadataStore
extends MetadataStoreBase<OfflinePlayer>
implements MetadataStore<OfflinePlayer> {
    @Override
    protected String disambiguate(OfflinePlayer player, String metadataKey) {
        return player.getUniqueId() + ":" + metadataKey;
    }
}

