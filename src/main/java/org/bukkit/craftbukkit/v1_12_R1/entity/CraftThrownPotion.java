/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftProjectile;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionUtil;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public abstract class CraftThrownPotion
extends CraftProjectile
implements ThrownPotion {
    public CraftThrownPotion(CraftServer server, aez entity) {
        super(server, entity);
    }

    @Override
    public Collection<PotionEffect> getEffects() {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (va effect : aki.a(this.getHandle().l())) {
            builder.add((Object)CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().l());
    }

    @Override
    public aez getHandle() {
        return (aez)this.entity;
    }
}

