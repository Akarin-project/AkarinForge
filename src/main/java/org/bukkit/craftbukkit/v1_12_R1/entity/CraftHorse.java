/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;

public class CraftHorse
extends CraftAbstractHorse
implements Horse {
    public CraftHorse(CraftServer server, aaq entity) {
        super(server, entity);
    }

    @Override
    public aaq getHandle() {
        return (aaq)super.getHandle();
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.HORSE;
    }

    @Override
    public Horse.Color getColor() {
        return Horse.Color.values()[this.getHandle().dl() & 255];
    }

    @Override
    public void setColor(Horse.Color color) {
        Validate.notNull((Object)((Object)color), (String)"Color cannot be null", (Object[])new Object[0]);
        this.getHandle().o(color.ordinal() & 255 | this.getStyle().ordinal() << 8);
    }

    @Override
    public Horse.Style getStyle() {
        return Horse.Style.values()[this.getHandle().dl() >>> 8];
    }

    @Override
    public void setStyle(Horse.Style style) {
        Validate.notNull((Object)((Object)style), (String)"Style cannot be null", (Object[])new Object[0]);
        this.getHandle().o(this.getColor().ordinal() & 255 | style.ordinal() << 8);
    }

    @Override
    public boolean isCarryingChest() {
        return false;
    }

    @Override
    public void setCarryingChest(boolean chest) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public HorseInventory getInventory() {
        return new CraftInventoryHorse(this.getHandle().bC);
    }

    @Override
    public String toString() {
        return "CraftHorse{variant=" + (Object)((Object)this.getVariant()) + ", owner=" + this.getOwner() + '}';
    }

    @Override
    public EntityType getType() {
        return EntityType.HORSE;
    }
}

