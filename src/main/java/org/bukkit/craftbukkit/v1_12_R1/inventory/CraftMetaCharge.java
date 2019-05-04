/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaFirework;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
class CraftMetaCharge
extends CraftMetaItem
implements FireworkEffectMeta {
    static final CraftMetaItem.ItemMetaKey EXPLOSION = new CraftMetaItem.ItemMetaKey("Explosion", "firework-effect");
    private FireworkEffect effect;

    CraftMetaCharge(CraftMetaItem meta) {
        super(meta);
        if (meta instanceof CraftMetaCharge) {
            this.effect = ((CraftMetaCharge)meta).effect;
        }
    }

    CraftMetaCharge(Map<String, Object> map) {
        super(map);
        this.setEffect(CraftMetaItem.SerializableMeta.getObject(FireworkEffect.class, map, CraftMetaCharge.EXPLOSION.BUKKIT, true));
    }

    CraftMetaCharge(fy tag) {
        super(tag);
        if (tag.e(CraftMetaCharge.EXPLOSION.NBT)) {
            this.effect = CraftMetaFirework.getEffect(tag.p(CraftMetaCharge.EXPLOSION.NBT));
        }
    }

    @Override
    public void setEffect(FireworkEffect effect) {
        this.effect = effect;
    }

    @Override
    public boolean hasEffect() {
        return this.effect != null;
    }

    @Override
    public FireworkEffect getEffect() {
        return this.effect;
    }

    @Override
    void applyToItem(fy itemTag) {
        super.applyToItem(itemTag);
        if (this.hasEffect()) {
            itemTag.a(CraftMetaCharge.EXPLOSION.NBT, CraftMetaFirework.getExplosion(this.effect));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case FIREWORK_CHARGE: {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && !this.hasChargeMeta();
    }

    boolean hasChargeMeta() {
        return this.hasEffect();
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaCharge) {
            CraftMetaCharge that = (CraftMetaCharge)meta;
            return this.hasEffect() ? that.hasEffect() && this.effect.equals(that.effect) : !that.hasEffect();
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCharge || !this.hasChargeMeta());
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.hasEffect()) {
            hash = 61 * hash + this.effect.hashCode();
        }
        return hash != original ? CraftMetaCharge.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaCharge clone() {
        return (CraftMetaCharge)super.clone();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasEffect()) {
            builder.put((Object)CraftMetaCharge.EXPLOSION.BUKKIT, (Object)this.effect);
        }
        return builder;
    }

}

