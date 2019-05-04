/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

public final class CraftDamageSource
extends ur {
    public static ur copyOf(ur original) {
        CraftDamageSource newSource = new CraftDamageSource(original.u);
        if (original.e()) {
            newSource.k();
        }
        if (original.s()) {
            newSource.t();
        }
        if (original.c()) {
            newSource.d();
        }
        return newSource;
    }

    private CraftDamageSource(String identifier) {
        super(identifier);
    }
}

