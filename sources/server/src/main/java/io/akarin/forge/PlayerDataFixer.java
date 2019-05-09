package io.akarin.forge;

import org.bukkit.util.NumberConversions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public class PlayerDataFixer {
    public static void checkVector(Entity entity) {
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

    public static void checkLocation(EntityPlayerMP entity) {
        if (!(NumberConversions.isFinite(entity.posX) && NumberConversions.isFinite(entity.posY) && NumberConversions.isFinite(entity.posZ))) {
            BlockPos pos = entity.getEntityWorld().getSpawnPoint();
            entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
        }
    }
}
