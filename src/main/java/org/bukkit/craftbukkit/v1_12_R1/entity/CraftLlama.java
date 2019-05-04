/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftChestedHorse;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryLlama;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.LlamaInventory;

public class CraftLlama
extends CraftChestedHorse
implements Llama {
    public CraftLlama(CraftServer server, aas entity) {
        super(server, entity);
    }

    @Override
    public aas getHandle() {
        return (aas)super.getHandle();
    }

    @Override
    public Llama.Color getColor() {
        return Llama.Color.values()[this.getHandle().dR()];
    }

    @Override
    public void setColor(Llama.Color color) {
        Preconditions.checkArgument((boolean)(color != null), (Object)"color");
        this.getHandle().o(color.ordinal());
    }

    @Override
    public LlamaInventory getInventory() {
        return new CraftInventoryLlama(this.getHandle().bC);
    }

    @Override
    public int getStrength() {
        return this.getHandle().dQ();
    }

    @Override
    public void setStrength(int strength) {
        Preconditions.checkArgument((boolean)(1 <= strength && strength <= 5), (Object)"strength must be [1,5]");
        if (strength == this.getStrength()) {
            return;
        }
        this.getHandle().p(strength);
        this.getHandle().dC();
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.LLAMA;
    }

    @Override
    public String toString() {
        return "CraftLlama";
    }

    @Override
    public EntityType getType() {
        return EntityType.LLAMA;
    }
}

