/*
 * Decompiled with CFR 0_119.
 */
package io.akarin.forge.utils;

import java.util.Set;

import net.minecraft.entity.Entity;

public class EntityMoveTask
implements Runnable {
    public final Entity entity;
    public final vv moverType;
    public final double x;
    public final double y;
    public final double z;
    public final long time;

    public EntityMoveTask(Entity entity, vv moverType, double x2, double y2, double z2, long time) {
        this.entity = entity;
        this.moverType = moverType;
        this.x = x2;
        this.y = y2;
        this.z = z2;
        this.time = time;
    }

    @Override
    public void run() {
        try {
            if (System.currentTimeMillis() - this.time > 100) {
                return;
            }
            if (this.entity.l.unloadedEntitySet.contains(this.entity)) {
                return;
            }
            this.entity.move0(this.moverType, this.x, this.y, this.z, true);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}

