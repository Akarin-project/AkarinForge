/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Structure;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockVector;

public class CraftStructureBlock
extends CraftBlockEntityState<awe>
implements Structure {
    private static final int MAX_SIZE = 32;

    public CraftStructureBlock(Block block) {
        super(block, awe.class);
    }

    public CraftStructureBlock(Material material, awe structure) {
        super(material, structure);
    }

    @Override
    public String getStructureName() {
        return ((awe)this.getSnapshot()).a();
    }

    @Override
    public void setStructureName(String name) {
        Preconditions.checkArgument((boolean)(name != null), (Object)"Structure Name cannot be null");
        ((awe)this.getSnapshot()).a(name);
    }

    @Override
    public String getAuthor() {
        return ((awe)this.getSnapshot()).f;
    }

    @Override
    public void setAuthor(String author) {
        Preconditions.checkArgument((boolean)(author != null && !author.isEmpty()), (Object)"Author name cannot be null nor empty");
        ((awe)this.getSnapshot()).f = author;
    }

    @Override
    public void setAuthor(LivingEntity entity) {
        Preconditions.checkArgument((boolean)(entity != null), (Object)"Structure Block author entity cannot be null");
        ((awe)this.getSnapshot()).a(((CraftLivingEntity)entity).getHandle());
    }

    @Override
    public BlockVector getRelativePosition() {
        return new BlockVector(((awe)this.getSnapshot()).h.p(), ((awe)this.getSnapshot()).h.q(), ((awe)this.getSnapshot()).h.r());
    }

    @Override
    public void setRelativePosition(BlockVector vector) {
        Validate.isTrue((boolean)CraftStructureBlock.isBetween(vector.getBlockX(), -32, 32), (String)"Structure Size (X) must be between -32 and 32", (Object[])new Object[0]);
        Validate.isTrue((boolean)CraftStructureBlock.isBetween(vector.getBlockY(), -32, 32), (String)"Structure Size (Y) must be between -32 and 32", (Object[])new Object[0]);
        Validate.isTrue((boolean)CraftStructureBlock.isBetween(vector.getBlockZ(), -32, 32), (String)"Structure Size (Z) must be between -32 and 32", (Object[])new Object[0]);
        ((awe)this.getSnapshot()).h = new et(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    @Override
    public BlockVector getStructureSize() {
        return new BlockVector(((awe)this.getSnapshot()).i.p(), ((awe)this.getSnapshot()).i.q(), ((awe)this.getSnapshot()).i.r());
    }

    @Override
    public void setStructureSize(BlockVector vector) {
        Validate.isTrue((boolean)CraftStructureBlock.isBetween(vector.getBlockX(), 0, 32), (String)"Structure Size (X) must be between 0 and 32", (Object[])new Object[0]);
        Validate.isTrue((boolean)CraftStructureBlock.isBetween(vector.getBlockY(), 0, 32), (String)"Structure Size (Y) must be between 0 and 32", (Object[])new Object[0]);
        Validate.isTrue((boolean)CraftStructureBlock.isBetween(vector.getBlockZ(), 0, 32), (String)"Structure Size (Z) must be between 0 and 32", (Object[])new Object[0]);
        ((awe)this.getSnapshot()).i = new et(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    @Override
    public void setMirror(Mirror mirror) {
        ((awe)this.getSnapshot()).j = ary.valueOf(mirror.name());
    }

    @Override
    public Mirror getMirror() {
        return Mirror.valueOf(((awe)this.getSnapshot()).j.name());
    }

    @Override
    public void setRotation(StructureRotation rotation) {
        ((awe)this.getSnapshot()).k = atm.valueOf(rotation.name());
    }

    @Override
    public StructureRotation getRotation() {
        return StructureRotation.valueOf(((awe)this.getSnapshot()).k.name());
    }

    @Override
    public void setUsageMode(UsageMode mode) {
        ((awe)this.getSnapshot()).a(awe.a.valueOf(mode.name()));
    }

    @Override
    public UsageMode getUsageMode() {
        return UsageMode.valueOf(((awe)this.getSnapshot()).k().name());
    }

    @Override
    public void setIgnoreEntities(boolean flag) {
        ((awe)this.getSnapshot()).m = flag;
    }

    @Override
    public boolean isIgnoreEntities() {
        return ((awe)this.getSnapshot()).m;
    }

    @Override
    public void setShowAir(boolean showAir) {
        ((awe)this.getSnapshot()).o = showAir;
    }

    @Override
    public boolean isShowAir() {
        return ((awe)this.getSnapshot()).o;
    }

    @Override
    public void setBoundingBoxVisible(boolean showBoundingBox) {
        ((awe)this.getSnapshot()).p = showBoundingBox;
    }

    @Override
    public boolean isBoundingBoxVisible() {
        return ((awe)this.getSnapshot()).p;
    }

    @Override
    public void setIntegrity(float integrity) {
        Validate.isTrue((boolean)CraftStructureBlock.isBetween(integrity, 0.0f, 1.0f), (String)"Integrity must be between 0.0f and 1.0f", (Object[])new Object[0]);
        ((awe)this.getSnapshot()).q = integrity;
    }

    @Override
    public float getIntegrity() {
        return ((awe)this.getSnapshot()).q;
    }

    @Override
    public void setSeed(long seed) {
        ((awe)this.getSnapshot()).r = seed;
    }

    @Override
    public long getSeed() {
        return ((awe)this.getSnapshot()).r;
    }

    @Override
    public void setMetadata(String metadata) {
        Validate.notNull((Object)metadata, (String)"Structure metadata cannot be null", (Object[])new Object[0]);
        if (this.getUsageMode() == UsageMode.DATA) {
            ((awe)this.getSnapshot()).g = metadata;
        }
    }

    @Override
    public String getMetadata() {
        return ((awe)this.getSnapshot()).g;
    }

    private static boolean isBetween(int num, int min, int max) {
        return num >= min && num <= max;
    }

    private static boolean isBetween(float num, float min, float max) {
        return num >= min && num <= max;
    }
}

