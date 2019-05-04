/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

@Deprecated
public class SpawnEgg
extends MaterialData {
    public SpawnEgg() {
        super(Material.MONSTER_EGG);
    }

    @Deprecated
    public SpawnEgg(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public SpawnEgg(byte data) {
        super(Material.MONSTER_EGG, data);
    }

    public SpawnEgg(EntityType type) {
        this();
        this.setSpawnedType(type);
    }

    @Deprecated
    public EntityType getSpawnedType() {
        return EntityType.fromId(this.getData());
    }

    @Deprecated
    public void setSpawnedType(EntityType type) {
        this.setData((byte)type.getTypeId());
    }

    @Override
    public String toString() {
        return "SPAWN EGG{" + (Object)((Object)this.getSpawnedType()) + "}";
    }

    @Override
    public SpawnEgg clone() {
        return (SpawnEgg)super.clone();
    }
}

