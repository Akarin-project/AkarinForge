/*
 * Akarin Forge
 */
package io.akarin.forge.utils;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

public class NMSUtils {
    public static WorldServer toNMS(World world) {
        return ((CraftWorld)world).getHandle();
    }

    public static EntityPlayerMP toNMS(Player player) {
        return ((CraftPlayer)player).getHandle();
    }

    public static net.minecraft.entity.Entity toNMS(Entity entity) {
        return ((CraftEntity)entity).getHandle();
    }
}

