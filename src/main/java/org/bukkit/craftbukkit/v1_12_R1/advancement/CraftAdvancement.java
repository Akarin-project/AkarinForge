/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.advancement;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

public class CraftAdvancement
implements Advancement {
    private final i handle;

    public CraftAdvancement(i handle) {
        this.handle = handle;
    }

    public i getHandle() {
        return this.handle;
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(this.handle.h());
    }

    @Override
    public Collection<String> getCriteria() {
        return Collections.unmodifiableCollection(this.handle.f().keySet());
    }
}

