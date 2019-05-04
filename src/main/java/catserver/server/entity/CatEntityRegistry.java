/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package catserver.server.entity;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

public class CatEntityRegistry<K, V>
extends fh<K, V> {
    @Override
    public void a(int id2, K key, V value) {
        if (key == null) {
            key = CraftNamespacedKey.toMinecraft(NamespacedKey.randomKey());
        }
        try {
            GameData.registerEntity(id2, (nf)key, (Class)value, ((nf)key).a());
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Nullable
    @Override
    public V c(@Nullable K name) {
        EntityEntry entry = ForgeRegistries.ENTITIES.getValue((nf)name);
        return (V)(entry == null ? null : entry.getEntityClass());
    }

    @Nullable
    @Override
    public K b(V value) {
        EntityEntry entry = EntityRegistry.getEntry((Class)value);
        return (K)(entry == null ? null : entry.getRegistryName());
    }

    @Override
    public boolean d(K key) {
        return ForgeRegistries.ENTITIES.getValue((nf)key) != null;
    }

    @Override
    public int a(@Nullable V value) {
        EntityEntry entry = EntityRegistry.getEntry((Class)value);
        return entry == null ? -1 : GameData.getEntityRegistry().getID(entry);
    }

    @Nullable
    @Override
    public V a(int id2) {
        EntityEntry entry = GameData.getEntityRegistry().getValue(id2);
        return (V)(entry == null ? null : entry);
    }

    @Override
    public Iterator<V> iterator() {
        return GameData.getEntityRegistry().getValues().iterator();
    }
}

