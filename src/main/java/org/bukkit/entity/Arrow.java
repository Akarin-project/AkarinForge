/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;

public interface Arrow
extends Projectile {
    public int getKnockbackStrength();

    public void setKnockbackStrength(int var1);

    public boolean isCritical();

    public void setCritical(boolean var1);

    public boolean isInBlock();

    public Block getAttachedBlock();

    public PickupStatus getPickupStatus();

    public void setPickupStatus(PickupStatus var1);

    @Override
    public Spigot org_bukkit_entity_Arrow$Spigot_spigot();

    public static class Spigot
    extends Entity.Spigot {
        public double getDamage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setDamage(double damage) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static enum PickupStatus {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;
        

        private PickupStatus() {
        }
    }

}

