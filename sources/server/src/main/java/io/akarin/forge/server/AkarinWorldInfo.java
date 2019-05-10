package io.akarin.forge.server;

import net.minecraft.world.WorldServer;

public abstract class AkarinWorldInfo {
    public WorldServer world;
    
    public abstract void syncWorldName(String name);
    
    public abstract void setDimension(int dim);

    public abstract int getDimension();
}
