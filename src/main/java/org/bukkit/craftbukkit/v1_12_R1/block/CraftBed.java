/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import com.google.common.base.Preconditions;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Bed;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;

public class CraftBed
extends CraftBlockEntityState<avi>
implements Bed {
    private DyeColor color;

    public CraftBed(Block block) {
        super(block, avi.class);
    }

    public CraftBed(Material material, avi te2) {
        super(material, te2);
    }

    @Override
    public void load(avi bed2) {
        super.load(bed2);
        this.color = DyeColor.getByWoolData((byte)bed2.a().a());
    }

    @Override
    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public void setColor(DyeColor color) {
        Preconditions.checkArgument((boolean)(color != null), (Object)"color");
        this.color = color;
    }

    @Override
    public void applyTo(avi bed2) {
        super.applyTo(bed2);
        bed2.a(ahs.b(this.color.getWoolData()));
    }
}

