/*
 * Akarin Forge
 */
package org.bukkit.event.inventory;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockExpEvent;

public class FurnaceExtractEvent
extends BlockExpEvent {
    private final Player player;
    private final Material itemType;
    private final int itemAmount;

    public FurnaceExtractEvent(Player player, Block block, Material itemType, int itemAmount, int exp) {
        super(block, exp);
        this.player = player;
        this.itemType = itemType;
        this.itemAmount = itemAmount;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Material getItemType() {
        return this.itemType;
    }

    public int getItemAmount() {
        return this.itemAmount;
    }
}

