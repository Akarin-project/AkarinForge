/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftThrownPotion;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.inventory.ItemStack;

public class CraftLingeringPotion
extends CraftThrownPotion
implements LingeringPotion {
    public CraftLingeringPotion(CraftServer server, aez entity) {
        super(server, entity);
    }

    @Override
    public void setItem(ItemStack item) {
        Validate.notNull((Object)item, (String)"ItemStack cannot be null.", (Object[])new Object[0]);
        Validate.isTrue((boolean)(item.getType() == Material.LINGERING_POTION), (String)("ItemStack must be a lingering potion. This item stack was " + (Object)((Object)item.getType()) + "."), (Object[])new Object[0]);
        this.getHandle().a(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public aez getHandle() {
        return (aez)this.entity;
    }

    @Override
    public String toString() {
        return "CraftLingeringPotion";
    }

    @Override
    public EntityType getType() {
        return EntityType.LINGERING_POTION;
    }
}

