/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.Art;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftArt;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHanging;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class CraftPainting
extends CraftHanging
implements Painting {
    public CraftPainting(CraftServer server, acd entity) {
        super(server, entity);
    }

    @Override
    public Art getArt() {
        acd.a art2 = this.getHandle().c;
        return CraftArt.NotchToBukkit(art2);
    }

    @Override
    public boolean setArt(Art art2) {
        return this.setArt(art2, false);
    }

    @Override
    public boolean setArt(Art art2, boolean force) {
        acd painting = this.getHandle();
        acd.a oldArt = painting.c;
        painting.c = CraftArt.BukkitToNotch(art2);
        painting.a(painting.b);
        if (!force && !painting.k()) {
            painting.c = oldArt;
            painting.a(painting.b);
            return false;
        }
        this.update();
        return true;
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (super.setFacingDirection(face, force)) {
            this.update();
            return true;
        }
        return false;
    }

    private void update() {
        oo world = ((CraftWorld)this.getWorld()).getHandle();
        acd painting = new acd(world);
        painting.a = this.getHandle().q();
        painting.c = this.getHandle().c;
        painting.a(this.getHandle().b);
        this.getHandle().X();
        this.getHandle().D = true;
        world.a(painting);
        this.entity = painting;
    }

    @Override
    public acd getHandle() {
        return (acd)this.entity;
    }

    @Override
    public String toString() {
        return "CraftPainting{art=" + (Object)((Object)this.getArt()) + "}";
    }

    @Override
    public EntityType getType() {
        return EntityType.PAINTING;
    }
}

