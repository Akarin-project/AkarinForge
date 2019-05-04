/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnchantingTable;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;

public class CraftEnchantingTable
extends CraftBlockEntityState<avr>
implements EnchantingTable {
    public CraftEnchantingTable(Block block) {
        super(block, avr.class);
    }

    public CraftEnchantingTable(Material material, avr te2) {
        super(material, te2);
    }

    @Override
    public String getCustomName() {
        avr enchant = (avr)this.getSnapshot();
        return enchant.n_() ? enchant.h_() : null;
    }

    @Override
    public void setCustomName(String name) {
        ((avr)this.getSnapshot()).a(name);
    }

    @Override
    public void applyTo(avr enchantingTable) {
        super.applyTo(enchantingTable);
        if (!((avr)this.getSnapshot()).n_()) {
            enchantingTable.a((String)null);
        }
    }
}

