/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SpawnEggMeta;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
public class CraftMetaSpawnEgg
extends CraftMetaItem
implements SpawnEggMeta {
    static final CraftMetaItem.ItemMetaKey ENTITY_TAG = new CraftMetaItem.ItemMetaKey("EntityTag", "entity-tag");
    static final CraftMetaItem.ItemMetaKey ENTITY_ID = new CraftMetaItem.ItemMetaKey("id");
    private EntityType spawnedType;
    private fy entityTag;

    CraftMetaSpawnEgg(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSpawnEgg)) {
            return;
        }
        CraftMetaSpawnEgg egg = (CraftMetaSpawnEgg)meta;
        this.spawnedType = egg.spawnedType;
    }

    CraftMetaSpawnEgg(fy tag) {
        super(tag);
        if (tag.e(CraftMetaSpawnEgg.ENTITY_TAG.NBT)) {
            this.entityTag = tag.p(CraftMetaSpawnEgg.ENTITY_TAG.NBT);
            if (this.entityTag.e(CraftMetaSpawnEgg.ENTITY_ID.NBT)) {
                this.spawnedType = EntityType.fromName(new nf(this.entityTag.l(CraftMetaSpawnEgg.ENTITY_ID.NBT)).a());
            }
        }
    }

    CraftMetaSpawnEgg(Map<String, Object> map) {
        super(map);
        String entityType = CraftMetaItem.SerializableMeta.getString(map, CraftMetaSpawnEgg.ENTITY_ID.BUKKIT, true);
        this.setSpawnedType(EntityType.fromName(entityType));
    }

    @Override
    void deserializeInternal(fy tag) {
        super.deserializeInternal(tag);
        if (tag.e(CraftMetaSpawnEgg.ENTITY_TAG.NBT)) {
            this.entityTag = tag.p(CraftMetaSpawnEgg.ENTITY_TAG.NBT);
            MinecraftServer.getServerInst().getDataFixer().a((rv)rw.e, this.entityTag);
            if (this.entityTag.e(CraftMetaSpawnEgg.ENTITY_ID.NBT)) {
                this.spawnedType = EntityType.fromName(new nf(this.entityTag.l(CraftMetaSpawnEgg.ENTITY_ID.NBT)).a());
            }
        }
    }

    @Override
    void serializeInternal(Map<String, gn> internalTags) {
        if (this.entityTag != null) {
            internalTags.put(CraftMetaSpawnEgg.ENTITY_TAG.NBT, this.entityTag);
        }
    }

    @Override
    void applyToItem(fy tag) {
        super.applyToItem(tag);
        if (!this.isSpawnEggEmpty() && this.entityTag == null) {
            this.entityTag = new fy();
        }
        if (this.hasSpawnedType()) {
            this.entityTag.a(CraftMetaSpawnEgg.ENTITY_ID.NBT, new nf(this.spawnedType.getName()).toString());
        }
        if (this.entityTag != null) {
            tag.a(CraftMetaSpawnEgg.ENTITY_TAG.NBT, this.entityTag);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case MONSTER_EGG: {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isSpawnEggEmpty();
    }

    boolean isSpawnEggEmpty() {
        return !this.hasSpawnedType() && this.entityTag == null;
    }

    boolean hasSpawnedType() {
        return this.spawnedType != null;
    }

    @Override
    public EntityType getSpawnedType() {
        return this.spawnedType;
    }

    @Override
    public void setSpawnedType(EntityType type) {
        Preconditions.checkArgument((boolean)(type == null || type.getName() != null), (String)"Spawn egg type must have name (%s)", (Object)((Object)type));
        this.spawnedType = type;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSpawnEgg) {
            CraftMetaSpawnEgg that = (CraftMetaSpawnEgg)meta;
            return this.hasSpawnedType() ? that.hasSpawnedType() && this.spawnedType.equals((Object)that.spawnedType) : (!that.hasSpawnedType() && this.entityTag != null ? that.entityTag != null && this.entityTag.equals(that.entityTag) : this.entityTag == null);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSpawnEgg || this.isSpawnEggEmpty());
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.hasSpawnedType()) {
            hash = 73 * hash + this.spawnedType.hashCode();
        }
        if (this.entityTag != null) {
            hash = 73 * hash + this.entityTag.hashCode();
        }
        return original != hash ? CraftMetaSpawnEgg.class.hashCode() ^ hash : hash;
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasSpawnedType()) {
            builder.put((Object)CraftMetaSpawnEgg.ENTITY_ID.BUKKIT, (Object)this.spawnedType.getName());
        }
        return builder;
    }

    @Override
    public CraftMetaSpawnEgg clone() {
        CraftMetaSpawnEgg clone = (CraftMetaSpawnEgg)super.clone();
        clone.spawnedType = this.spawnedType;
        if (this.entityTag != null) {
            clone.entityTag = this.entityTag.g();
        }
        return clone;
    }

}

