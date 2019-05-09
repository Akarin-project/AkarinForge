/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.ItemMeta;

public interface FireworkEffectMeta
extends ItemMeta {
    public void setEffect(FireworkEffect var1);

    public boolean hasEffect();

    public FireworkEffect getEffect();

    @Override
    public FireworkEffectMeta clone();
}

