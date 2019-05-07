package io.akarin.forge.entity;

import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public class AkarinEntityRegistry<V> extends RegistryNamespaced<ResourceLocation, V> {
    @Override
    public void register(int id2, ResourceLocation key, V value) {
        if (key == null) {
            key = (ResourceLocation) CraftNamespacedKey.toMinecraft(NamespacedKey.randomKey());
        }
        try {
            GameData.registerEntity(id2, (ResourceLocation)key, (Class)value, ((ResourceLocation)key).getResourcePath());
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Nullable
    @Override
    public V getObject(@Nullable ResourceLocation name) {
        EntityEntry entry = ForgeRegistries.ENTITIES.getValue((ResourceLocation)name);
        return (V)(entry == null ? null : entry.getEntityClass());
    }

    @Nullable
    @Override
    public ResourceLocation getNameForObject(V value) {
        EntityEntry entry = EntityRegistry.getEntry((Class)value);
        return (ResourceLocation)(entry == null ? null : entry.getRegistryName());
    }

    @Override
    public boolean containsKey(ResourceLocation key) {
        return ForgeRegistries.ENTITIES.getValue((ResourceLocation)key) != null;
    }

    @Override
    public int getIDForObject(@Nullable V value) {
        EntityEntry entry = EntityRegistry.getEntry((Class)value);
        return entry == null ? -1 : GameData.getEntityRegistry().getID(entry);
    }

    @Nullable
    @Override
    public V getObjectById(int id2) {
        EntityEntry entry = GameData.getEntityRegistry().getValue(id2);
        return (V)(entry == null ? null : entry);
    }

    @Override
    public Iterator<V> iterator() {
        return (Iterator<V>) GameData.getEntityRegistry().getValues().iterator();
    }
}

