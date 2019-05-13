package io.akarin.forge.server.utility;

import org.bukkit.util.NumberConversions;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class DataFixer {
    public static void fixVector(Entity entity) {
        if (!NumberConversions.isFinite(entity.motionX)) {
            entity.motionX = 0.0;
        }
        if (!NumberConversions.isFinite(entity.motionY)) {
            entity.motionY = 0.0;
        }
        if (!NumberConversions.isFinite(entity.motionZ)) {
            entity.motionZ = 0.0;
        }
    }

    public static void fixPosition(Entity entity) {
        if (!(NumberConversions.isFinite(entity.posX) ||
              NumberConversions.isFinite(entity.posY) ||
              NumberConversions.isFinite(entity.posZ ))) {
        	
            BlockPos pos = entity.world.getSpawnPoint();
            entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
        }
    }
}
