/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;

public class CraftJukebox
extends CraftBlockEntityState<arp.a>
implements Jukebox {
    public CraftJukebox(Block block) {
        super(block, arp.a.class);
    }

    public CraftJukebox(Material material, arp.a te2) {
        super(material, te2);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);
        if (result && this.isPlaced() && this.getType() == Material.JUKEBOX) {
            CraftWorld world = (CraftWorld)this.getWorld();
            Material record = this.getPlaying();
            if (record == Material.AIR) {
                world.getHandle().a(new et(this.getX(), this.getY(), this.getZ()), aox.aN.t().a(arp.a, false), 3);
            } else {
                world.getHandle().a(new et(this.getX(), this.getY(), this.getZ()), aox.aN.t().a(arp.a, true), 3);
            }
            world.playEffect(this.getLocation(), Effect.RECORD_PLAY, record.getId());
        }
        return result;
    }

    @Override
    public Material getPlaying() {
        aip record = ((arp.a)this.getSnapshot()).a();
        if (record.b()) {
            return Material.AIR;
        }
        return CraftMagicNumbers.getMaterial(record.c());
    }

    @Override
    public void setPlaying(Material record) {
        if (record == null || CraftMagicNumbers.getItem(record) == null) {
            record = Material.AIR;
        }
        ((arp.a)this.getSnapshot()).a(new aip(CraftMagicNumbers.getItem(record), 1));
        if (record == Material.AIR) {
            this.setRawData(0);
        } else {
            this.setRawData(1);
        }
    }

    @Override
    public boolean isPlaying() {
        return this.getRawData() == 1;
    }

    @Override
    public boolean eject() {
        this.requirePlaced();
        avj tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof arp.a)) {
            return false;
        }
        arp.a jukebox = (arp.a)tileEntity;
        boolean result = !jukebox.a().b();
        CraftWorld world = (CraftWorld)this.getWorld();
        ((arp)aox.aN).e(world.getHandle(), new et(this.getX(), this.getY(), this.getZ()), null);
        return result;
    }
}

