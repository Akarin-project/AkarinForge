/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.block.BlockState;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockVector;

public interface Structure
extends BlockState {
    public String getStructureName();

    public void setStructureName(String var1);

    public String getAuthor();

    public void setAuthor(String var1);

    public void setAuthor(LivingEntity var1);

    public BlockVector getRelativePosition();

    public void setRelativePosition(BlockVector var1);

    public BlockVector getStructureSize();

    public void setStructureSize(BlockVector var1);

    public void setMirror(Mirror var1);

    public Mirror getMirror();

    public void setRotation(StructureRotation var1);

    public StructureRotation getRotation();

    public void setUsageMode(UsageMode var1);

    public UsageMode getUsageMode();

    public void setIgnoreEntities(boolean var1);

    public boolean isIgnoreEntities();

    public void setShowAir(boolean var1);

    public boolean isShowAir();

    public void setBoundingBoxVisible(boolean var1);

    public boolean isBoundingBoxVisible();

    public void setIntegrity(float var1);

    public float getIntegrity();

    public void setSeed(long var1);

    public long getSeed();

    public void setMetadata(String var1);

    public String getMetadata();
}

