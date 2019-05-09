/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;

public interface KnowledgeBookMeta
extends ItemMeta {
    public boolean hasRecipes();

    public List<NamespacedKey> getRecipes();

    public void setRecipes(List<NamespacedKey> var1);

    public /* varargs */ void addRecipe(NamespacedKey ... var1);

    @Override
    public KnowledgeBookMeta clone();
}

