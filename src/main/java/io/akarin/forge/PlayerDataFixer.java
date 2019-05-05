/*
 * Decompiled with CFR 0_119.
 */
package io.akarin.forge;

import org.bukkit.util.NumberConversions;

import net.minecraft.entity.Entity;

public class PlayerDataFixer {
    public static void checkVector(Entity entity) {
        if (!NumberConversions.isFinite(entity.x)) {
            entity.s = 0.0;
        }
        if (!NumberConversions.isFinite(entity.t)) {
            entity.t = 0.0;
        }
        if (!NumberConversions.isFinite(entity.u)) {
            entity.u = 0.0;
        }
    }

    public static void checkLocation(aed entity) {
        if (!(NumberConversions.isFinite(entity.p) && NumberConversions.isFinite(entity.q) && NumberConversions.isFinite(entity.r))) {
            et pos = entity.e().T();
            entity.b(pos.p(), pos.q(), pos.r());
        }
    }
}

