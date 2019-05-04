/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Monster;

public interface Skeleton
extends Monster {
    @Deprecated
    public SkeletonType getSkeletonType();

    @Deprecated
    public void setSkeletonType(SkeletonType var1);

    @Deprecated
    public static enum SkeletonType {
        NORMAL,
        WITHER,
        STRAY;
        

        private SkeletonType() {
        }
    }

}

