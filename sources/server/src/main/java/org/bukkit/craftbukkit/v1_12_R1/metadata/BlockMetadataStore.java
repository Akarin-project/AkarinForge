/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.metadata;

import java.util.List;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class BlockMetadataStore
extends MetadataStoreBase<Block>
implements MetadataStore<Block> {
    private final World owningWorld;

    public BlockMetadataStore(World owningWorld) {
        this.owningWorld = owningWorld;
    }

    @Override
    protected String disambiguate(Block block, String metadataKey) {
        return Integer.toString(block.getX()) + ":" + Integer.toString(block.getY()) + ":" + Integer.toString(block.getZ()) + ":" + metadataKey;
    }

    @Override
    public List<MetadataValue> getMetadata(Block block, String metadataKey) {
        if (block.getWorld() == this.owningWorld) {
            return super.getMetadata(block, metadataKey);
        }
        throw new IllegalArgumentException("Block does not belong to world " + this.owningWorld.getName());
    }

    @Override
    public boolean hasMetadata(Block block, String metadataKey) {
        if (block.getWorld() == this.owningWorld) {
            return super.hasMetadata(block, metadataKey);
        }
        throw new IllegalArgumentException("Block does not belong to world " + this.owningWorld.getName());
    }

    @Override
    public void removeMetadata(Block block, String metadataKey, Plugin owningPlugin) {
        if (block.getWorld() != this.owningWorld) {
            throw new IllegalArgumentException("Block does not belong to world " + this.owningWorld.getName());
        }
        super.removeMetadata(block, metadataKey, owningPlugin);
    }

    @Override
    public void setMetadata(Block block, String metadataKey, MetadataValue newMetadataValue) {
        if (block.getWorld() != this.owningWorld) {
            throw new IllegalArgumentException("Block does not belong to world " + this.owningWorld.getName());
        }
        super.setMetadata(block, metadataKey, newMetadataValue);
    }
}

