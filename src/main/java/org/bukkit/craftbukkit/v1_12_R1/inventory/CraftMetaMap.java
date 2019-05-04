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
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.Repairable;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
class CraftMetaMap
extends CraftMetaItem
implements MapMeta {
    static final CraftMetaItem.ItemMetaKey MAP_SCALING = new CraftMetaItem.ItemMetaKey("map_is_scaling", "scaling");
    static final CraftMetaItem.ItemMetaKey MAP_LOC_NAME = new CraftMetaItem.ItemMetaKey("LocName", "display-loc-name");
    static final CraftMetaItem.ItemMetaKey MAP_COLOR = new CraftMetaItem.ItemMetaKey("MapColor", "display-map-color");
    static final byte SCALING_EMPTY = 0;
    static final byte SCALING_TRUE = 1;
    static final byte SCALING_FALSE = 2;
    private byte scaling = 0;
    private String locName;
    private Color color;

    CraftMetaMap(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaMap)) {
            return;
        }
        CraftMetaMap map = (CraftMetaMap)meta;
        this.scaling = map.scaling;
        this.locName = map.locName;
        this.color = map.color;
    }

    CraftMetaMap(fy tag) {
        super(tag);
        if (tag.e(CraftMetaMap.MAP_SCALING.NBT)) {
            int n2 = this.scaling = tag.q(CraftMetaMap.MAP_SCALING.NBT) ? 1 : 2;
        }
        if (tag.e(CraftMetaMap.DISPLAY.NBT)) {
            fy display = tag.p(CraftMetaMap.DISPLAY.NBT);
            if (display.e(CraftMetaMap.MAP_LOC_NAME.NBT)) {
                this.locName = display.l(CraftMetaMap.MAP_LOC_NAME.NBT);
            }
            if (display.e(CraftMetaMap.MAP_COLOR.NBT)) {
                this.color = Color.fromRGB(display.h(CraftMetaMap.MAP_COLOR.NBT));
            }
        }
    }

    CraftMetaMap(Map<String, Object> map) {
        String locName;
        Color color;
        super(map);
        Boolean scaling = CraftMetaItem.SerializableMeta.getObject(Boolean.class, map, CraftMetaMap.MAP_SCALING.BUKKIT, true);
        if (scaling != null) {
            this.setScaling(scaling);
        }
        if ((locName = CraftMetaItem.SerializableMeta.getString(map, CraftMetaMap.MAP_LOC_NAME.BUKKIT, true)) != null) {
            this.setLocationName(locName);
        }
        if ((color = CraftMetaItem.SerializableMeta.getObject(Color.class, map, CraftMetaMap.MAP_COLOR.BUKKIT, true)) != null) {
            this.setColor(color);
        }
    }

    @Override
    void applyToItem(fy tag) {
        super.applyToItem(tag);
        if (this.hasScaling()) {
            tag.a(CraftMetaMap.MAP_SCALING.NBT, this.isScaling());
        }
        if (this.hasLocationName()) {
            this.setDisplayTag(tag, CraftMetaMap.MAP_LOC_NAME.NBT, new gm(this.getLocationName()));
        }
        if (this.hasColor()) {
            this.setDisplayTag(tag, CraftMetaMap.MAP_COLOR.NBT, new gd(this.color.asRGB()));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case MAP: {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isMapEmpty();
    }

    boolean isMapEmpty() {
        return !(this.hasScaling() | this.hasLocationName()) && !this.hasColor();
    }

    boolean hasScaling() {
        return this.scaling != 0;
    }

    @Override
    public boolean isScaling() {
        return this.scaling == 1;
    }

    @Override
    public void setScaling(boolean scaling) {
        this.scaling = scaling ? 1 : 2;
    }

    @Override
    public boolean hasLocationName() {
        return this.locName != null;
    }

    @Override
    public String getLocationName() {
        return this.locName;
    }

    @Override
    public void setLocationName(String name) {
        this.locName = name;
    }

    @Override
    public boolean hasColor() {
        return this.color != null;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaMap) {
            CraftMetaMap that = (CraftMetaMap)meta;
            return this.scaling == that.scaling && (this.hasLocationName() ? that.hasLocationName() && this.locName.equals(that.locName) : !that.hasLocationName()) && (this.hasColor() ? that.hasColor() && this.color.equals(that.color) : !that.hasColor());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaMap || this.isMapEmpty());
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.hasScaling()) {
            hash ^= 572662306 << (this.isScaling() ? 1 : -1);
        }
        if (this.hasLocationName()) {
            hash = 61 * hash + this.locName.hashCode();
        }
        if (this.hasColor()) {
            hash = 61 * hash + this.color.hashCode();
        }
        return original != hash ? CraftMetaMap.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaMap clone() {
        return (CraftMetaMap)super.clone();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasScaling()) {
            builder.put((Object)CraftMetaMap.MAP_SCALING.BUKKIT, (Object)this.isScaling());
        }
        if (this.hasLocationName()) {
            builder.put((Object)CraftMetaMap.MAP_LOC_NAME.BUKKIT, (Object)this.getLocationName());
        }
        if (this.hasColor()) {
            builder.put((Object)CraftMetaMap.MAP_COLOR.BUKKIT, (Object)this.getColor());
        }
        return builder;
    }

}

