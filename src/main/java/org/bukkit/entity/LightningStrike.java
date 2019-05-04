/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Weather;

public interface LightningStrike
extends Weather {
    public boolean isEffect();

    @Override
    public Spigot org_bukkit_entity_LightningStrike$Spigot_spigot();

    public static class Spigot
    extends Entity.Spigot {
        public boolean isSilent() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}

