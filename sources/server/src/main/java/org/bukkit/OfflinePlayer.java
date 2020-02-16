/*
 * Akarin Forge
 */
package org.bukkit;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

public interface OfflinePlayer
extends ServerOperator,
AnimalTamer,
ConfigurationSerializable {
    public boolean isOnline();

    @Override
    public String getName();

    @Override
    public UUID getUniqueId();

    public boolean isBanned();

    public boolean isWhitelisted();

    public void setWhitelisted(boolean var1);

    public Player getPlayer();

    public long getFirstPlayed();

    public long getLastPlayed();

    public boolean hasPlayedBefore();

    public Location getBedSpawnLocation();
}

