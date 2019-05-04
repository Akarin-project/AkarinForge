/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.Repairable;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
public class CraftMetaKnowledgeBook
extends CraftMetaItem
implements KnowledgeBookMeta {
    static final CraftMetaItem.ItemMetaKey BOOK_RECIPES = new CraftMetaItem.ItemMetaKey("Recipes");
    static final int MAX_RECIPES = 32767;
    protected List<NamespacedKey> recipes = new ArrayList<NamespacedKey>();

    CraftMetaKnowledgeBook(CraftMetaItem meta) {
        super(meta);
        if (meta instanceof CraftMetaKnowledgeBook) {
            CraftMetaKnowledgeBook bookMeta = (CraftMetaKnowledgeBook)meta;
            this.recipes.addAll(bookMeta.recipes);
        }
    }

    CraftMetaKnowledgeBook(fy tag) {
        super(tag);
        if (tag.e(CraftMetaKnowledgeBook.BOOK_RECIPES.NBT)) {
            ge pages = tag.c(CraftMetaKnowledgeBook.BOOK_RECIPES.NBT, 8);
            for (int i2 = 0; i2 < pages.c(); ++i2) {
                String recipe = pages.h(i2);
                this.addRecipe(CraftNamespacedKey.fromString(recipe));
            }
        }
    }

    CraftMetaKnowledgeBook(Map<String, Object> map) {
        super(map);
        Iterable pages = CraftMetaItem.SerializableMeta.getObject(Iterable.class, map, CraftMetaKnowledgeBook.BOOK_RECIPES.BUKKIT, true);
        if (pages != null) {
            for (Object page : pages) {
                if (!(page instanceof String)) continue;
                this.addRecipe(CraftNamespacedKey.fromString((String)page));
            }
        }
    }

    @Override
    void applyToItem(fy itemData) {
        super.applyToItem(itemData);
        if (this.hasRecipes()) {
            ge list = new ge();
            for (NamespacedKey recipe : this.recipes) {
                list.a(new gm(recipe.toString()));
            }
            itemData.a(CraftMetaKnowledgeBook.BOOK_RECIPES.NBT, list);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBookEmpty();
    }

    boolean isBookEmpty() {
        return !this.hasRecipes();
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case KNOWLEDGE_BOOK: {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasRecipes() {
        return !this.recipes.isEmpty();
    }

    @Override
    public /* varargs */ void addRecipe(NamespacedKey ... recipes) {
        for (NamespacedKey recipe : recipes) {
            if (recipe == null) continue;
            if (this.recipes.size() >= 32767) {
                return;
            }
            this.recipes.add(recipe);
        }
    }

    @Override
    public List<NamespacedKey> getRecipes() {
        return Collections.unmodifiableList(this.recipes);
    }

    @Override
    public void setRecipes(List<NamespacedKey> recipes) {
        this.recipes.clear();
        for (NamespacedKey recipe : this.recipes) {
            this.addRecipe(recipe);
        }
    }

    @Override
    public CraftMetaKnowledgeBook clone() {
        CraftMetaKnowledgeBook meta = (CraftMetaKnowledgeBook)super.clone();
        meta.recipes = new ArrayList<NamespacedKey>(this.recipes);
        return meta;
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.hasRecipes()) {
            hash = 61 * hash + 17 * this.recipes.hashCode();
        }
        return original != hash ? CraftMetaKnowledgeBook.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaKnowledgeBook) {
            CraftMetaKnowledgeBook that = (CraftMetaKnowledgeBook)meta;
            return this.hasRecipes() ? that.hasRecipes() && this.recipes.equals(that.recipes) : !that.hasRecipes();
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaKnowledgeBook || this.isBookEmpty());
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasRecipes()) {
            ArrayList<String> recipesString = new ArrayList<String>();
            for (NamespacedKey recipe : this.recipes) {
                recipesString.add(recipe.toString());
            }
            builder.put((Object)CraftMetaKnowledgeBook.BOOK_RECIPES.BUKKIT, recipesString);
        }
        return builder;
    }

}

