/*
 * Decompiled with CFR 0_119.
 */
package io.akarin.forge.utils;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NMSUtils {
    public static oo toNMS(World world) {
        return ((CraftWorld)world).getHandle();
    }

    public static oq toNMS(Player player) {
        return ((CraftPlayer)player).getHandle();
    }

    public static vg toNMS(Entity entity) {
        return ((CraftEntity)entity).getHandle();
    }
}

