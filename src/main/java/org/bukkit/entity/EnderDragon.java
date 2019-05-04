/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.ComplexLivingEntity;

public interface EnderDragon
extends ComplexLivingEntity {
    public Phase getPhase();

    public void setPhase(Phase var1);

    public static enum Phase {
        CIRCLING,
        STRAFING,
        FLY_TO_PORTAL,
        LAND_ON_PORTAL,
        LEAVE_PORTAL,
        BREATH_ATTACK,
        SEARCH_FOR_BREATH_ATTACK_TARGET,
        ROAR_BEFORE_ATTACK,
        CHARGE_PLAYER,
        DYING,
        HOVER;
        

        private Phase() {
        }
    }

}

