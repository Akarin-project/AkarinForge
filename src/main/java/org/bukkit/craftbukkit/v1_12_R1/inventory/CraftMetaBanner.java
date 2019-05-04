/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
public class CraftMetaBanner
extends CraftMetaItem
implements BannerMeta {
    static final CraftMetaItem.ItemMetaKey BASE = new CraftMetaItem.ItemMetaKey("Base", "base-color");
    static final CraftMetaItem.ItemMetaKey PATTERNS = new CraftMetaItem.ItemMetaKey("Patterns", "patterns");
    static final CraftMetaItem.ItemMetaKey COLOR = new CraftMetaItem.ItemMetaKey("Color", "color");
    static final CraftMetaItem.ItemMetaKey PATTERN = new CraftMetaItem.ItemMetaKey("Pattern", "pattern");
    private DyeColor base;
    private List<Pattern> patterns = new ArrayList<Pattern>();

    CraftMetaBanner(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaBanner)) {
            return;
        }
        CraftMetaBanner banner = (CraftMetaBanner)meta;
        this.base = banner.base;
        this.patterns = new ArrayList<Pattern>(banner.patterns);
    }

    CraftMetaBanner(fy tag) {
        super(tag);
        if (!tag.e("BlockEntityTag")) {
            return;
        }
        fy entityTag = tag.p("BlockEntityTag");
        DyeColor dyeColor = this.base = entityTag.e(CraftMetaBanner.BASE.NBT) ? DyeColor.getByDyeData((byte)entityTag.h(CraftMetaBanner.BASE.NBT)) : null;
        if (entityTag.e(CraftMetaBanner.PATTERNS.NBT)) {
            ge patterns = entityTag.c(CraftMetaBanner.PATTERNS.NBT, 10);
            for (int i2 = 0; i2 < Math.min(patterns.c(), 20); ++i2) {
                fy p2 = patterns.b(i2);
                this.patterns.add(new Pattern(DyeColor.getByDyeData((byte)p2.h(CraftMetaBanner.COLOR.NBT)), PatternType.getByIdentifier(p2.l(CraftMetaBanner.PATTERN.NBT))));
            }
        }
    }

    CraftMetaBanner(Map<String, Object> map) {
        Iterable rawPatternList;
        super(map);
        String baseStr = CraftMetaItem.SerializableMeta.getString(map, CraftMetaBanner.BASE.BUKKIT, true);
        if (baseStr != null) {
            this.base = DyeColor.valueOf(baseStr);
        }
        if ((rawPatternList = CraftMetaItem.SerializableMeta.getObject(Iterable.class, map, CraftMetaBanner.PATTERNS.BUKKIT, true)) == null) {
            return;
        }
        for (Object obj : rawPatternList) {
            if (!(obj instanceof Pattern)) {
                throw new IllegalArgumentException("Object in pattern list is not valid. " + obj.getClass());
            }
            this.addPattern((Pattern)obj);
        }
    }

    @Override
    void applyToItem(fy tag) {
        super.applyToItem(tag);
        fy entityTag = new fy();
        if (this.base != null) {
            entityTag.a(CraftMetaBanner.BASE.NBT, (int)this.base.getDyeData());
        }
        ge newPatterns = new ge();
        for (Pattern p2 : this.patterns) {
            fy compound = new fy();
            compound.a(CraftMetaBanner.COLOR.NBT, (int)p2.getColor().getDyeData());
            compound.a(CraftMetaBanner.PATTERN.NBT, p2.getPattern().getIdentifier());
            newPatterns.a(compound);
        }
        entityTag.a(CraftMetaBanner.PATTERNS.NBT, newPatterns);
        tag.a("BlockEntityTag", entityTag);
    }

    @Override
    public DyeColor getBaseColor() {
        return this.base;
    }

    @Override
    public void setBaseColor(DyeColor color) {
        this.base = color;
    }

    @Override
    public List<Pattern> getPatterns() {
        return new ArrayList<Pattern>(this.patterns);
    }

    @Override
    public void setPatterns(List<Pattern> patterns) {
        this.patterns = new ArrayList<Pattern>(patterns);
    }

    @Override
    public void addPattern(Pattern pattern) {
        this.patterns.add(pattern);
    }

    @Override
    public Pattern getPattern(int i2) {
        return this.patterns.get(i2);
    }

    @Override
    public Pattern removePattern(int i2) {
        return this.patterns.remove(i2);
    }

    @Override
    public void setPattern(int i2, Pattern pattern) {
        this.patterns.set(i2, pattern);
    }

    @Override
    public int numberOfPatterns() {
        return this.patterns.size();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.base != null) {
            builder.put((Object)CraftMetaBanner.BASE.BUKKIT, (Object)this.base.toString());
        }
        if (!this.patterns.isEmpty()) {
            builder.put((Object)CraftMetaBanner.PATTERNS.BUKKIT, (Object)ImmutableList.copyOf(this.patterns));
        }
        return builder;
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.base != null) {
            hash = 31 * hash + this.base.hashCode();
        }
        if (!this.patterns.isEmpty()) {
            hash = 31 * hash + this.patterns.hashCode();
        }
        return original != hash ? CraftMetaBanner.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBanner) {
            CraftMetaBanner that = (CraftMetaBanner)meta;
            return this.base == that.base && this.patterns.equals(that.patterns);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBanner || this.patterns.isEmpty() && this.base == null);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.patterns.isEmpty() && this.base == null;
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.BANNER;
    }

    @Override
    public CraftMetaBanner clone() {
        CraftMetaBanner meta = (CraftMetaBanner)super.clone();
        meta.patterns = new ArrayList<Pattern>(this.patterns);
        return meta;
    }
}

