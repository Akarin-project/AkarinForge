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
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaSkull;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Repairable;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
class CraftMetaLeatherArmor
extends CraftMetaItem
implements LeatherArmorMeta {
    static final CraftMetaItem.ItemMetaKey COLOR = new CraftMetaItem.ItemMetaKey("color");
    private Color color = CraftItemFactory.DEFAULT_LEATHER_COLOR;

    CraftMetaLeatherArmor(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaLeatherArmor)) {
            return;
        }
        CraftMetaLeatherArmor armorMeta = (CraftMetaLeatherArmor)meta;
        this.color = armorMeta.color;
    }

    CraftMetaLeatherArmor(fy tag) {
        fy display;
        super(tag);
        if (tag.e(CraftMetaLeatherArmor.DISPLAY.NBT) && (display = tag.p(CraftMetaLeatherArmor.DISPLAY.NBT)).e(CraftMetaLeatherArmor.COLOR.NBT)) {
            try {
                this.color = Color.fromRGB(display.h(CraftMetaLeatherArmor.COLOR.NBT));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
    }

    CraftMetaLeatherArmor(Map<String, Object> map) {
        super(map);
        this.setColor(CraftMetaItem.SerializableMeta.getObject(Color.class, map, CraftMetaLeatherArmor.COLOR.BUKKIT, true));
    }

    @Override
    void applyToItem(fy itemTag) {
        super.applyToItem(itemTag);
        if (this.hasColor()) {
            this.setDisplayTag(itemTag, CraftMetaLeatherArmor.COLOR.NBT, new gd(this.color.asRGB()));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isLeatherArmorEmpty();
    }

    boolean isLeatherArmorEmpty() {
        return !this.hasColor();
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case LEATHER_HELMET: 
            case LEATHER_CHESTPLATE: 
            case LEATHER_LEGGINGS: 
            case LEATHER_BOOTS: {
                return true;
            }
        }
        return false;
    }

    @Override
    public CraftMetaLeatherArmor clone() {
        return (CraftMetaLeatherArmor)super.clone();
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color == null ? CraftItemFactory.DEFAULT_LEATHER_COLOR : color;
    }

    boolean hasColor() {
        return !CraftItemFactory.DEFAULT_LEATHER_COLOR.equals(this.color);
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasColor()) {
            builder.put((Object)CraftMetaLeatherArmor.COLOR.BUKKIT, (Object)this.color);
        }
        return builder;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaLeatherArmor) {
            CraftMetaLeatherArmor that = (CraftMetaLeatherArmor)meta;
            return this.color.equals(that.color);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaLeatherArmor || this.isLeatherArmorEmpty());
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.hasColor()) {
            hash ^= this.color.hashCode();
        }
        return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
    }

}

