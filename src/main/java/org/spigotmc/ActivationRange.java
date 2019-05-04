/*
 * Akarin Forge
 */
package org.spigotmc;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.SpigotTimings;
import org.spigotmc.CustomTimingsHandler;
import org.spigotmc.SpigotWorldConfig;

public class ActivationRange {
    static bhb maxBB = new bhb(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    static bhb miscBB = new bhb(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    static bhb animalBB = new bhb(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    static bhb monsterBB = new bhb(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

    public static byte initializeEntityActivationType(vg entity) {
        if (entity instanceof ade || entity instanceof adl) {
            return 1;
        }
        if (entity instanceof vx || entity instanceof zs) {
            return 2;
        }
        return 3;
    }

    public static boolean initializeEntityActivationState(vg entity, SpigotWorldConfig config) {
        if (config != null || DimensionManager.getWorld(0) == null) {
            return true;
        }
        config = DimensionManager.getWorld((int)0).spigotConfig;
        if (entity.activationType == 3 && config.miscActivationRange == 0 || entity.activationType == 2 && config.animalActivationRange == 0 || entity.activationType == 1 && config.monsterActivationRange == 0 || entity instanceof aed || entity instanceof aev || entity instanceof abb || entity instanceof abx || entity instanceof ael || entity instanceof ack || entity instanceof ach || entity instanceof acm || entity instanceof abc || entity instanceof aem || entity.getClass().getSuperclass() == vg.class && !entity.isCreatureType(vr.b, false) && !entity.isCreatureType(vr.c, false) && !entity.isCreatureType(vr.a, false) && !entity.isCreatureType(vr.d, false)) {
            return true;
        }
        return false;
    }

    public static void activateEntities(amu world) {
        SpigotTimings.entityActivationCheckTimer.startTiming();
        int miscActivationRange = world.spigotConfig.miscActivationRange;
        int animalActivationRange = world.spigotConfig.animalActivationRange;
        int monsterActivationRange = world.spigotConfig.monsterActivationRange;
        int maxRange = Math.max(monsterActivationRange, animalActivationRange);
        maxRange = Math.max(maxRange, miscActivationRange);
        maxRange = Math.min((world.spigotConfig.viewDistance << 4) - 8, maxRange);
        for (aed player : world.i) {
            player.activatedTick = MinecraftServer.currentTick;
            maxBB = player.bw().c((double)maxRange, 256.0, maxRange);
            miscBB = player.bw().c((double)miscActivationRange, 256.0, miscActivationRange);
            animalBB = player.bw().c((double)animalActivationRange, 256.0, animalActivationRange);
            monsterBB = player.bw().c((double)monsterActivationRange, 256.0, monsterActivationRange);
            int i2 = rk.c(ActivationRange.maxBB.a / 16.0);
            int j2 = rk.c(ActivationRange.maxBB.d / 16.0);
            int k2 = rk.c(ActivationRange.maxBB.c / 16.0);
            int l2 = rk.c(ActivationRange.maxBB.f / 16.0);
            for (int i1 = i2; i1 <= j2; ++i1) {
                for (int j1 = k2; j1 <= l2; ++j1) {
                    if (!world.getWorld().isChunkLoaded(i1, j1)) continue;
                    ActivationRange.activateChunkEntities(world.a(i1, j1));
                }
            }
        }
        SpigotTimings.entityActivationCheckTimer.stopTiming();
    }

    private static void activateChunkEntities(axw chunk) {
        for (qx<vg> slice : chunk.o) {
            block5 : for (vg entity : slice) {
                if (entity == null || (long)MinecraftServer.currentTick <= entity.activatedTick) continue;
                if (entity.defaultActivationState) {
                    entity.activatedTick = MinecraftServer.currentTick;
                    continue;
                }
                switch (entity.activationType) {
                    case 1: {
                        if (!monsterBB.c(entity.bw())) continue block5;
                        entity.activatedTick = MinecraftServer.currentTick;
                        continue block5;
                    }
                    case 2: {
                        if (!animalBB.c(entity.bw())) continue block5;
                        entity.activatedTick = MinecraftServer.currentTick;
                        continue block5;
                    }
                }
                if (!miscBB.c(entity.bw())) continue;
                entity.activatedTick = MinecraftServer.currentTick;
            }
        }
    }

    public static boolean checkEntityImmunities(vg entity) {
        if (entity.ao() || entity.az > 0) {
            return true;
        }
        if (!(entity instanceof aeh) ? !entity.z || !entity.at.isEmpty() || entity.aS() : !((aeh)entity).a) {
            return true;
        }
        if (entity instanceof vq) {
            vq living = (vq)entity;
            if (living.ay > 0 || living.bu.size() > 0) {
                return true;
            }
            if (entity instanceof vx && ((vx)entity).z() != null) {
                return true;
            }
            if (entity instanceof ady && ((ady)entity).dm()) {
                return true;
            }
            if (entity instanceof zv) {
                zv animal = (zv)entity;
                if (animal.l_() || animal.dr()) {
                    return true;
                }
                if (entity instanceof aag && ((aag)entity).dm()) {
                    return true;
                }
            }
            if (entity instanceof acs && ((acs)entity).dn()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIfActive(vg entity) {
        boolean isActive;
        SpigotTimings.checkIfActiveTimer.startTiming();
        if (!entity.aa || entity instanceof aem) {
            SpigotTimings.checkIfActiveTimer.stopTiming();
            return true;
        }
        boolean bl2 = isActive = entity.activatedTick >= (long)MinecraftServer.currentTick || entity.defaultActivationState;
        if (!isActive) {
            if (((long)MinecraftServer.currentTick - entity.activatedTick - 1) % 20 == 0) {
                if (ActivationRange.checkEntityImmunities(entity)) {
                    entity.activatedTick = MinecraftServer.currentTick + 20;
                }
                isActive = true;
            }
        } else if (!entity.defaultActivationState && entity.T % 4 == 0 && !ActivationRange.checkEntityImmunities(entity)) {
            isActive = false;
        }
        int x2 = rk.c(entity.p);
        int z2 = rk.c(entity.r);
        axw chunk = entity.l.getChunkIfLoaded(x2 >> 4, z2 >> 4);
        if (isActive && (chunk == null || !chunk.areNeighborsLoaded(1))) {
            isActive = false;
        }
        SpigotTimings.checkIfActiveTimer.stopTiming();
        return isActive;
    }
}

