/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.util.LazyHashSet;
import org.bukkit.entity.Player;

public class LazyPlayerSet
extends LazyHashSet<Player> {
    private final MinecraftServer server;

    public LazyPlayerSet(MinecraftServer server) {
        this.server = server;
    }

    @Override
    HashSet<Player> makeReference() {
        if (this.reference != null) {
            throw new IllegalStateException("Reference already created!");
        }
        List<oq> players = this.server.am().v();
        HashSet<Player> reference = new HashSet<Player>(players.size());
        for (oq player : players) {
            reference.add(player.getBukkitEntity());
        }
        return reference;
    }
}

