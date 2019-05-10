/*
 * Akarin Forge
 */
package org.bukkit.generator;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;

public abstract class BlockPopulator {
    public abstract void populate(World var1, Random var2, Chunk var3);
}

