package org.bukkit.craftbukkit.v1_12_R1.event;

import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class CraftEventFactory {

	public static void callProjectileHitEvent(EntityFireball entityFireball, RayTraceResult raytraceresult) {
	}

	public static boolean handleNonLivingEntityDamageEvent(EntityFireball entityFireball, DamageSource source,
			float amount) {
		return true;
	}

	public static void callRedstoneChange(World world, int x, int y, int z, int oldPower, int newPower) {
		
	}
    
}
