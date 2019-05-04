/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicates
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.common.util.concurrent.Futures
 *  com.mojang.authlib.GameProfile
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfile;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
class CraftMetaSkull
extends CraftMetaItem
implements SkullMeta {
    static final CraftMetaItem.ItemMetaKey SKULL_PROFILE = new CraftMetaItem.ItemMetaKey("SkullProfile");
    static final CraftMetaItem.ItemMetaKey SKULL_OWNER = new CraftMetaItem.ItemMetaKey("SkullOwner", "skull-owner");
    static final int MAX_OWNER_LENGTH = 16;
    private GameProfile profile;

    CraftMetaSkull(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSkull)) {
            return;
        }
        CraftMetaSkull skullMeta = (CraftMetaSkull)meta;
        this.profile = skullMeta.profile;
    }

    CraftMetaSkull(fy tag) {
        super(tag);
        if (tag.b(CraftMetaSkull.SKULL_OWNER.NBT, 10)) {
            this.profile = gj.a(tag.p(CraftMetaSkull.SKULL_OWNER.NBT));
        } else if (tag.b(CraftMetaSkull.SKULL_OWNER.NBT, 8) && !tag.l(CraftMetaSkull.SKULL_OWNER.NBT).isEmpty()) {
            this.profile = new GameProfile(null, tag.l(CraftMetaSkull.SKULL_OWNER.NBT));
        }
    }

    CraftMetaSkull(Map<String, Object> map) {
        super(map);
        if (this.profile == null) {
            this.setOwner(CraftMetaItem.SerializableMeta.getString(map, CraftMetaSkull.SKULL_OWNER.BUKKIT, true));
        }
    }

    @Override
    void deserializeInternal(fy tag) {
        if (tag.b(CraftMetaSkull.SKULL_PROFILE.NBT, 10)) {
            this.profile = gj.a(tag.p(CraftMetaSkull.SKULL_PROFILE.NBT));
        }
    }

    @Override
    void serializeInternal(Map<String, gn> internalTags) {
        if (this.profile != null) {
            fy nbtData = new fy();
            gj.a(nbtData, this.profile);
            internalTags.put(CraftMetaSkull.SKULL_PROFILE.NBT, nbtData);
        }
    }

    @Override
    void applyToItem(fy tag) {
        super.applyToItem(tag);
        if (this.profile != null) {
            this.profile = (GameProfile)Futures.getUnchecked(awd.updateGameprofile(this.profile, Predicates.alwaysTrue(), true));
            fy owner = new fy();
            gj.a(owner, this.profile);
            tag.a(CraftMetaSkull.SKULL_OWNER.NBT, owner);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isSkullEmpty();
    }

    boolean isSkullEmpty() {
        return this.profile == null;
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case SKULL_ITEM: {
                return true;
            }
        }
        return false;
    }

    @Override
    public CraftMetaSkull clone() {
        return (CraftMetaSkull)super.clone();
    }

    @Override
    public boolean hasOwner() {
        return this.profile != null && this.profile.getName() != null;
    }

    @Override
    public String getOwner() {
        return this.hasOwner() ? this.profile.getName() : null;
    }

    @Override
    public OfflinePlayer getOwningPlayer() {
        if (this.hasOwner()) {
            if (this.profile.getId() != null) {
                return Bukkit.getOfflinePlayer(this.profile.getId());
            }
            if (this.profile.getName() != null) {
                return Bukkit.getOfflinePlayer(this.profile.getName());
            }
        }
        return null;
    }

    @Override
    public boolean setOwner(String name) {
        if (name != null && name.length() > 16) {
            return false;
        }
        this.profile = name == null ? null : new GameProfile(null, name);
        return true;
    }

    @Override
    public boolean setOwningPlayer(OfflinePlayer owner) {
        this.profile = owner == null ? null : new GameProfile(owner.getUniqueId(), owner.getName());
        return true;
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.hasOwner()) {
            hash = 61 * hash + this.profile.hashCode();
        }
        return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSkull) {
            CraftMetaSkull that = (CraftMetaSkull)meta;
            return this.hasOwner() ? that.hasOwner() && this.profile.equals((Object)that.profile) : !that.hasOwner();
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSkull || this.isSkullEmpty());
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasOwner()) {
            return builder.put((Object)CraftMetaSkull.SKULL_OWNER.BUKKIT, (Object)this.profile.getName());
        }
        return builder;
    }

}

