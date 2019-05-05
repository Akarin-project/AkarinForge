package org.spigotmc;

import org.spigotmc.SpigotWorldConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayerMP;

public class TrackingRange {
    public static int getEntityTrackingRange(Entity entity, int defaultRange) {
        SpigotWorldConfig config = entity.l.spigotConfig;
        if (entity instanceof EntityPlayerMP) {
            return config.playerTrackingRange;
        }
        if (entity.activationType == 1) {
            return config.monsterTrackingRange;
        }
        if (entity instanceof EntityGhast) {
            if (config.monsterTrackingRange > config.monsterActivationRange) {
                return config.monsterTrackingRange;
            }
            return config.monsterActivationRange;
        }
        if (entity.activationType == 2) {
            return config.animalTrackingRange;
        }
        if (entity instanceof EntityItemFrame || entity instanceof EntityPainting || entity instanceof EntityItem || entity instanceof EntityXPOrb) {
            return config.miscTrackingRange;
        }
        return config.otherTrackingRange;
    }
}

