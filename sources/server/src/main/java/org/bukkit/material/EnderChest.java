/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.MaterialData;

public class EnderChest
extends DirectionalContainer {
    public EnderChest() {
        super(Material.ENDER_CHEST);
    }

    public EnderChest(BlockFace direction) {
        this();
        this.setFacingDirection(direction);
    }

    @Deprecated
    public EnderChest(int type) {
        super(type);
    }

    public EnderChest(Material type) {
        super(type);
    }

    @Deprecated
    public EnderChest(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public EnderChest(Material type, byte data) {
        super(type, data);
    }

    @Override
    public EnderChest clone() {
        return (EnderChest)super.clone();
    }
}

