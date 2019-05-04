/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.FlowerPot;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class CraftFlowerPot
extends CraftBlockEntityState<avt>
implements FlowerPot {
    private MaterialData contents;

    public CraftFlowerPot(Block block) {
        super(block, avt.class);
    }

    public CraftFlowerPot(Material material, avt te2) {
        super(material, te2);
    }

    @Override
    public void load(avt pot) {
        super.load(pot);
        this.contents = pot.e() == null ? null : CraftItemStack.asBukkitCopy(pot.a()).getData();
    }

    @Override
    public MaterialData getContents() {
        return this.contents;
    }

    @Override
    public void setContents(MaterialData item) {
        this.contents = item;
    }

    @Override
    public void applyTo(avt pot) {
        super.applyTo(pot);
        pot.a(this.contents == null ? aip.a : CraftItemStack.asNMSCopy(this.contents.toItemStack(1)));
    }
}

