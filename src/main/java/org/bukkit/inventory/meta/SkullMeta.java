/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.ItemMeta;

public interface SkullMeta
extends ItemMeta {
    @Deprecated
    public String getOwner();

    public boolean hasOwner();

    @Deprecated
    public boolean setOwner(String var1);

    public OfflinePlayer getOwningPlayer();

    public boolean setOwningPlayer(OfflinePlayer var1);

    @Override
    public SkullMeta clone();
}

