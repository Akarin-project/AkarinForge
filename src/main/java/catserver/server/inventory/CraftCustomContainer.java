/*
 * Decompiled with CFR 0_119.
 */
package catserver.server.inventory;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftCustomContainer
extends CraftBlockState
implements InventoryHolder {
    private final CraftWorld world;
    private final tv inventory;

    public CraftCustomContainer(Block block) {
        super(block);
        this.world = (CraftWorld)block.getWorld();
        this.inventory = (tv)((Object)this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ()));
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.inventory);
    }
}

