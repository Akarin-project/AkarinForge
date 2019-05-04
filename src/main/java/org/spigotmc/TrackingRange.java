/*
 * Akarin Forge
 */
package org.spigotmc;

import org.spigotmc.SpigotWorldConfig;

public class TrackingRange {
    public static int getEntityTrackingRange(vg entity, int defaultRange) {
        SpigotWorldConfig config = entity.l.spigotConfig;
        if (entity instanceof oq) {
            return config.playerTrackingRange;
        }
        if (entity.activationType == 1) {
            return config.monsterTrackingRange;
        }
        if (entity instanceof acy) {
            if (config.monsterTrackingRange > config.monsterActivationRange) {
                return config.monsterTrackingRange;
            }
            return config.monsterActivationRange;
        }
        if (entity.activationType == 2) {
            return config.animalTrackingRange;
        }
        if (entity instanceof acb || entity instanceof acd || entity instanceof acl || entity instanceof vm) {
            return config.miscTrackingRange;
        }
        return config.otherTrackingRange;
    }
}

