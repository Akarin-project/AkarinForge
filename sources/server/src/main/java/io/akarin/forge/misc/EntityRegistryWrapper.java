package io.akarin.forge.misc;

import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.registries.GameData;

public class EntityRegistryWrapper extends RegistryNamespaced<ResourceLocation, Class<? extends Entity>> {
	
	public static EntityRegistryWrapper create() {
		return new EntityRegistryWrapper();
	}

	@Override
	public void register(int id, ResourceLocation key, Class<? extends Entity> value) {
		GameData.registerEntity(id, key, value, key.getResourcePath());
	}

	@Override
	public Class<? extends Entity> getObject(@Nullable ResourceLocation name) {
		return GameData.getWrapper(EntityEntry.class).getObject(name).getEntityClass();
	}

	@Override
	public ResourceLocation getNameForObject(Class<? extends Entity> value) {
		return EntityRegistry.getEntry(value).getRegistryName();
	}

	@Override
	public boolean containsKey(ResourceLocation key) {
		return GameData.getWrapper(EntityEntry.class).containsKey(key);
	}

	@Override
	public int getIDForObject(@Nullable Class<? extends Entity> value) {
		return GameData.getWrapper(EntityEntry.class).getIDForObject(EntityRegistry.getEntry(value));
	}

	@Override
	public Class<? extends Entity> getObjectById(int id) {
		return GameData.getWrapper(EntityEntry.class).getObjectById(id).getEntityClass();
	}

	@Override
	public Iterator<Class<? extends Entity>> iterator() {
		return EntityRegistry.entityClassMap.values().iterator();
	}
}
