/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftFirework
extends CraftEntity
implements Firework {
    private final Random random = new Random();
    private final CraftItemStack item;

    public CraftFirework(CraftServer server, aem entity) {
        super(server, entity);
        aip item = this.getHandle().V().a(aem.a);
        if (item.b()) {
            item = new aip(air.cm);
            this.getHandle().V().b(aem.a, item);
        }
        this.item = CraftItemStack.asCraftMirror(item);
        if (this.item.getType() != Material.FIREWORK) {
            this.item.setType(Material.FIREWORK);
        }
    }

    @Override
    public aem getHandle() {
        return (aem)this.entity;
    }

    @Override
    public String toString() {
        return "CraftFirework";
    }

    @Override
    public EntityType getType() {
        return EntityType.FIREWORK;
    }

    @Override
    public FireworkMeta getFireworkMeta() {
        return (FireworkMeta)this.item.getItemMeta();
    }

    @Override
    public void setFireworkMeta(FireworkMeta meta) {
        this.item.setItemMeta(meta);
        this.getHandle().d = 10 * (1 + meta.getPower()) + this.random.nextInt(6) + this.random.nextInt(7);
        this.getHandle().V().b(aem.a);
    }

    @Override
    public void detonate() {
        this.getHandle().d = 0;
    }
}

