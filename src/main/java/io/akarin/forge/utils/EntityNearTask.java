/*
 * Decompiled with CFR 0_119.
 */
package io.akarin.forge.utils;

import java.util.Set;

import net.minecraft.entity.Entity;

public class EntityNearTask
implements Runnable {
    public final Entity entity;
    public final long time;

    public EntityNearTask(Entity entity, long time) {
        this.entity = entity;
        this.time = time;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - this.time > 100) {
            return;
        }
        if (this.entity.l.unloadedEntitySet.contains(this.entity)) {
            return;
        }
        this.entity.collideWithNearbyEntities0();
    }
}

