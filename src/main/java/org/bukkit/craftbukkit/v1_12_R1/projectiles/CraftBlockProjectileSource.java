/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.projectiles;

import java.util.Random;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class CraftBlockProjectileSource
implements BlockProjectileSource {
    private final avp dispenserBlock;

    public CraftBlockProjectileSource(avp dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return this.dispenserBlock.D().getWorld().getBlockAt(this.dispenserBlock.w().p(), this.dispenserBlock.w().q(), this.dispenserBlock.w().r());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return this.launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Validate.isTrue((boolean)(this.getBlock().getType() == Material.DISPENSER), (String)"Block is no longer dispenser", (Object[])new Object[0]);
        ev isourceblock = new ev(this.dispenserBlock.D(), this.dispenserBlock.w());
        fk iposition = apz.a(isourceblock);
        fa enumdirection = (fa)isourceblock.e().c(apz.a);
        amu world = this.dispenserBlock.D();
        vg launch = null;
        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new aet(world, iposition.a(), iposition.b(), iposition.c());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new aew(world, iposition.a(), iposition.b(), iposition.c());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new aex(world, null);
            launch.b(iposition.a(), iposition.b(), iposition.c());
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new aey(world, iposition.a(), iposition.b(), iposition.c());
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            launch = LingeringPotion.class.isAssignableFrom(projectile) ? new aez(world, iposition.a(), iposition.b(), iposition.c(), CraftItemStack.asNMSCopy(new ItemStack(Material.LINGERING_POTION, 1))) : new aez(world, iposition.a(), iposition.b(), iposition.c(), CraftItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new afa(world, iposition.a(), iposition.b(), iposition.c());
                ((afa)launch).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
            } else {
                launch = SpectralArrow.class.isAssignableFrom(projectile) ? new aeu(world, iposition.a(), iposition.b(), iposition.c()) : new afa(world, iposition.a(), iposition.b(), iposition.c());
            }
            ((aeh)launch).c = aeh.a.b;
            ((aeh)launch).projectileSource = this;
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            double d0 = iposition.a() + (double)((float)enumdirection.g() * 0.3f);
            double d1 = iposition.b() + (double)((float)enumdirection.h() * 0.3f);
            double d2 = iposition.c() + (double)((float)enumdirection.i() * 0.3f);
            Random random = world.r;
            double d3 = random.nextGaussian() * 0.05 + (double)enumdirection.g();
            double d4 = random.nextGaussian() * 0.05 + (double)enumdirection.h();
            double d5 = random.nextGaussian() * 0.05 + (double)enumdirection.i();
            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new aes(world, null, d0, d1, d2);
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new afb(world);
                launch.b(d0, d1, d2);
                double d6 = rk.a(d3 * d3 + d4 * d4 + d5 * d5);
                ((ael)launch).b = d3 / d6 * 0.1;
                ((ael)launch).c = d4 / d6 * 0.1;
                ((ael)launch).d = d5 / d6 * 0.1;
            } else {
                launch = new aen(world);
                launch.b(d0, d1, d2);
                double d6 = rk.a(d3 * d3 + d4 * d4 + d5 * d5);
                ((ael)launch).b = d3 / d6 * 0.1;
                ((ael)launch).c = d4 / d6 * 0.1;
                ((ael)launch).d = d5 / d6 * 0.1;
            }
            ((ael)launch).projectileSource = this;
        }
        Validate.notNull((Object)launch, (String)"Projectile not supported", (Object[])new Object[0]);
        if (launch instanceof aep) {
            if (launch instanceof aev) {
                ((aev)launch).projectileSource = this;
            }
            float a2 = 6.0f;
            float b2 = 1.1f;
            if (launch instanceof aez || launch instanceof ThrownExpBottle) {
                a2 *= 0.5f;
                b2 *= 1.25f;
            }
            ((aep)((Object)launch)).c(enumdirection.g(), (float)enumdirection.h() + 0.1f, enumdirection.i(), b2, a2);
        }
        if (velocity != null) {
            ((Projectile)((Object)launch.getBukkitEntity())).setVelocity(velocity);
        }
        world.a(launch);
        return (T)((Projectile)((Object)launch.getBukkitEntity()));
    }
}

