/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
class CraftMetaBookSigned
extends CraftMetaBook
implements BookMeta {
    CraftMetaBookSigned(CraftMetaItem meta) {
        super(meta);
    }

    CraftMetaBookSigned(fy tag) {
        super(tag, false);
        boolean resolved = true;
        if (tag.e(CraftMetaBookSigned.RESOLVED.NBT)) {
            resolved = tag.q(CraftMetaBookSigned.RESOLVED.NBT);
        }
        if (tag.e(CraftMetaBookSigned.BOOK_PAGES.NBT)) {
            ge pages = tag.c(CraftMetaBookSigned.BOOK_PAGES.NBT, 8);
            for (int i2 = 0; i2 < Math.min(pages.c(), 50); ++i2) {
                String page = pages.h(i2);
                if (resolved) {
                    try {
                        this.pages.add(hh.a.a(page));
                        continue;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                this.addPage(page);
            }
        }
    }

    CraftMetaBookSigned(Map<String, Object> map) {
        super(map);
    }

    @Override
    void applyToItem(fy itemData) {
        super.applyToItem(itemData, false);
        if (this.hasTitle()) {
            itemData.a(CraftMetaBookSigned.BOOK_TITLE.NBT, this.title);
        } else {
            itemData.a(CraftMetaBookSigned.BOOK_TITLE.NBT, " ");
        }
        if (this.hasAuthor()) {
            itemData.a(CraftMetaBookSigned.BOOK_AUTHOR.NBT, this.author);
        } else {
            itemData.a(CraftMetaBookSigned.BOOK_AUTHOR.NBT, " ");
        }
        if (this.hasPages()) {
            ge list = new ge();
            for (hh page : this.pages) {
                list.a(new gm(hh.a.a(page)));
            }
            itemData.a(CraftMetaBookSigned.BOOK_PAGES.NBT, list);
        }
        itemData.a(CraftMetaBookSigned.RESOLVED.NBT, true);
        if (this.generation != null) {
            itemData.a(CraftMetaBookSigned.GENERATION.NBT, this.generation);
        } else {
            itemData.a(CraftMetaBookSigned.GENERATION.NBT, 0);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case WRITTEN_BOOK: 
            case BOOK_AND_QUILL: {
                return true;
            }
        }
        return false;
    }

    @Override
    public CraftMetaBookSigned clone() {
        CraftMetaBookSigned meta = (CraftMetaBookSigned)super.clone();
        return meta;
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        return original != hash ? CraftMetaBookSigned.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        return super.equalsCommon(meta);
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBookSigned || this.isBookEmpty());
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        return builder;
    }

}

