/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.ItemMeta;

public interface FireworkMeta
extends ItemMeta {
    public void addEffect(FireworkEffect var1) throws IllegalArgumentException;

    public /* varargs */ void addEffects(FireworkEffect ... var1) throws IllegalArgumentException;

    public void addEffects(Iterable<FireworkEffect> var1) throws IllegalArgumentException;

    public List<FireworkEffect> getEffects();

    public int getEffectsSize();

    public void removeEffect(int var1) throws IndexOutOfBoundsException;

    public void clearEffects();

    public boolean hasEffects();

    public int getPower();

    public void setPower(int var1) throws IllegalArgumentException;

    @Override
    public FireworkMeta clone();
}

