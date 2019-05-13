package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;

import net.minecraft.entity.Entity;

public abstract class CraftEntity implements org.bukkit.entity.Entity {

	public static CraftEntity getEntity(CraftServer server, Entity entity) {
		return null;
	}

	public boolean canSee(CraftEntity bukkitEntity) {
		// TODO Auto-generated method stub
		return true;
	}
    
}
