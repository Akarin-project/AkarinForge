/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.Material;
import org.bukkit.block.BlockState;

public interface Jukebox
extends BlockState {
    public Material getPlaying();

    public void setPlaying(Material var1);

    public boolean isPlaying();

    public boolean eject();
}

