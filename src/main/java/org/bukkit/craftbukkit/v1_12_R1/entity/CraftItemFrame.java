/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.apache.commons.lang3.Validate;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHanging;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;

public class CraftItemFrame
extends CraftHanging
implements ItemFrame {
    public CraftItemFrame(CraftServer server, acb entity) {
        super(server, entity);
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (!super.setFacingDirection(face, force)) {
            return false;
        }
        this.update();
        return true;
    }

    private void update() {
        acb old = this.getHandle();
        oo world = ((CraftWorld)this.getWorld()).getHandle();
        et position = old.c();
        fa direction = old.bt();
        aip item = old.r() != null ? old.r().l() : null;
        old.X();
        acb frame = new acb(world, position, direction);
        frame.a(item);
        world.a(frame);
        this.entity = frame;
    }

    @Override
    public void setItem(ItemStack item) {
        this.getHandle().a(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().r());
    }

    @Override
    public Rotation getRotation() {
        return this.toBukkitRotation(this.getHandle().s());
    }

    Rotation toBukkitRotation(int value) {
        switch (value) {
            case 0: {
                return Rotation.NONE;
            }
            case 1: {
                return Rotation.CLOCKWISE_45;
            }
            case 2: {
                return Rotation.CLOCKWISE;
            }
            case 3: {
                return Rotation.CLOCKWISE_135;
            }
            case 4: {
                return Rotation.FLIPPED;
            }
            case 5: {
                return Rotation.FLIPPED_45;
            }
            case 6: {
                return Rotation.COUNTER_CLOCKWISE;
            }
            case 7: {
                return Rotation.COUNTER_CLOCKWISE_45;
            }
        }
        throw new AssertionError((Object)("Unknown rotation " + value + " for " + this.getHandle()));
    }

    @Override
    public void setRotation(Rotation rotation) {
        Validate.notNull((Object)((Object)rotation), (String)"Rotation cannot be null", (Object[])new Object[0]);
        this.getHandle().a(CraftItemFrame.toInteger(rotation));
    }

    static int toInteger(Rotation rotation) {
        switch (rotation) {
            case NONE: {
                return 0;
            }
            case CLOCKWISE_45: {
                return 1;
            }
            case CLOCKWISE: {
                return 2;
            }
            case CLOCKWISE_135: {
                return 3;
            }
            case FLIPPED: {
                return 4;
            }
            case FLIPPED_45: {
                return 5;
            }
            case COUNTER_CLOCKWISE: {
                return 6;
            }
            case COUNTER_CLOCKWISE_45: {
                return 7;
            }
        }
        throw new IllegalArgumentException((Object)((Object)rotation) + " is not applicable to an ItemFrame");
    }

    @Override
    public acb getHandle() {
        return (acb)this.entity;
    }

    @Override
    public String toString() {
        return "CraftItemFrame{item=" + this.getItem() + ", rotation=" + (Object)((Object)this.getRotation()) + "}";
    }

    @Override
    public EntityType getType() {
        return EntityType.ITEM_FRAME;
    }

}

