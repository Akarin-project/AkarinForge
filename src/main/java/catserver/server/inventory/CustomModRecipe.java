/*
 * Decompiled with CFR 0_119.
 */
package catserver.server.inventory;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class CustomModRecipe
implements Recipe,
Keyed {
    private final akt iRecipe;
    private final ItemStack output;
    private NamespacedKey key;

    public CustomModRecipe(akt iRecipe) {
        this(iRecipe, null);
    }

    public CustomModRecipe(akt iRecipe, nf key) {
        this.iRecipe = iRecipe;
        this.output = CraftItemStack.asCraftMirror(iRecipe.b());
        try {
            this.key = key != null ? CraftNamespacedKey.fromMinecraft(key) : NamespacedKey.randomKey();
        }
        catch (Exception e2) {
            this.key = NamespacedKey.randomKey();
        }
    }

    @Override
    public ItemStack getResult() {
        return this.output.clone();
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    public akt getHandle() {
        return this.iRecipe;
    }
}

