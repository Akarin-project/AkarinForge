/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Monster;

public interface Guardian
extends Monster {
    @Deprecated
    public boolean isElder();

    @Deprecated
    public void setElder(boolean var1);
}

