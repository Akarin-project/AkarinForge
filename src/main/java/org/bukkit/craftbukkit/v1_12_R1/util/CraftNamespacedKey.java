/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import org.bukkit.NamespacedKey;

public final class CraftNamespacedKey {
    public static NamespacedKey fromString(String string) {
        return CraftNamespacedKey.fromMinecraft(new nf(string));
    }

    public static NamespacedKey fromMinecraft(nf minecraft) {
        return new NamespacedKey(minecraft.b(), minecraft.a());
    }

    public static nf toMinecraft(NamespacedKey key) {
        return new nf(key.getNamespace(), key.getKey());
    }
}

