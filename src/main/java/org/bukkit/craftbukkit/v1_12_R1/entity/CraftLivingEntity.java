/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftEntityEquipment;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Fish;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class CraftLivingEntity
extends CraftEntity
implements LivingEntity {
    private CraftEntityEquipment equipment;
    public String entityName;

    public CraftLivingEntity(CraftServer server, vp entity) {
        super(server, entity);
        if (entity instanceof vq || entity instanceof abz) {
            this.equipment = new CraftEntityEquipment(this);
        }
        this.entityName = EntityRegistry.entityTypeMap.get(entity.getClass());
        if (this.entityName == null) {
            this.entityName = entity.h_();
        }
    }

    @Override
    public double getHealth() {
        return Math.min((double)Math.max(0.0f, this.getHandle().cd()), this.getMaxHealth());
    }

    @Override
    public void setHealth(double health) {
        if ((health = (double)((float)health)) < 0.0 || health > this.getMaxHealth()) {
            throw new IllegalArgumentException("Health must be between 0 and " + this.getMaxHealth() + "(" + health + ")");
        }
        this.getHandle().c((float)health);
        if (health == 0.0) {
            this.getHandle().a(ur.n);
        }
    }

    @Override
    public double getMaxHealth() {
        return this.getHandle().cj();
    }

    @Override
    public void setMaxHealth(double amount) {
        Validate.isTrue((boolean)(amount > 0.0), (String)"Max health must be greater than 0", (Object[])new Object[0]);
        this.getHandle().a(adh.a).a(amount);
        if (this.getHealth() > amount) {
            this.setHealth(amount);
        }
    }

    @Override
    public void resetMaxHealth() {
        this.setMaxHealth(this.getHandle().a(adh.a).a().b());
    }

    @Override
    public double getEyeHeight() {
        return this.getHandle().by();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return this.getEyeHeight();
    }

    private List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<Block>();
        BlockIterator itr = new BlockIterator(this, maxDistance);
        while (itr.hasNext()) {
            Block block = (Block)itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            Material material = block.getType();
            if (!(transparent == null ? !material.equals((Object)Material.AIR) : !transparent.contains((Object)material))) continue;
            break;
        }
        return blocks;
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 0);
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        List<Block> blocks = this.getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 2);
    }

    @Override
    public int getRemainingAir() {
        return this.getHandle().aZ();
    }

    @Override
    public void setRemainingAir(int ticks) {
        this.getHandle().l(ticks);
    }

    @Override
    public int getMaximumAir() {
        return this.getHandle().maxAirTicks;
    }

    @Override
    public void setMaximumAir(int ticks) {
        this.getHandle().maxAirTicks = ticks;
    }

    @Override
    public void damage(double amount) {
        this.damage(amount, null);
    }

    @Override
    public void damage(double amount, Entity source) {
        ur reason = ur.n;
        if (source instanceof HumanEntity) {
            reason = ur.a(((CraftHumanEntity)source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = ur.a(((CraftLivingEntity)source).getHandle());
        }
        this.entity.a(reason, (float)amount);
    }

    @Override
    public Location getEyeLocation() {
        Location loc = this.getLocation();
        loc.setY(loc.getY() + this.getEyeHeight());
        return loc;
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return this.getHandle().aI;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        this.getHandle().aI = ticks;
    }

    @Override
    public double getLastDamage() {
        return this.getHandle().bc;
    }

    @Override
    public void setLastDamage(double damage) {
        this.getHandle().bc = (float)damage;
    }

    @Override
    public int getNoDamageTicks() {
        return this.getHandle().V;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        this.getHandle().V = ticks;
    }

    @Override
    public vp getHandle() {
        return (vp)this.entity;
    }

    public void setHandle(vq entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftLivingEntity{id=" + this.getEntityId() + ", name=" + this.entityName + "}";
    }

    @Override
    public Player getKiller() {
        return this.getHandle().aS == null ? null : (Player)((Object)this.getHandle().aS.getBukkitEntity());
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        return this.addPotionEffect(effect, false);
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        if (this.hasPotionEffect(effect.getType())) {
            if (!force) {
                return false;
            }
            this.removePotionEffect(effect.getType());
        }
        this.getHandle().c(new va(uz.a(effect.getType().getId()), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()));
        return true;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = true;
        for (PotionEffect effect : effects) {
            success &= this.addPotionEffect(effect);
        }
        return success;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        return this.getHandle().a(uz.a(type.getId()));
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        va handle = this.getHandle().b(uz.a(type.getId()));
        return handle == null ? null : new PotionEffect(PotionEffectType.getById(uz.a(handle.a())), handle.b(), handle.c(), handle.d(), handle.e());
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        this.getHandle().d(uz.a(type.getId()));
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (va handle : this.getHandle().cb().values()) {
            effects.add(new PotionEffect(PotionEffectType.getById(uz.a(handle.a())), handle.b(), handle.c(), handle.d(), handle.e()));
        }
        return effects;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return this.launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        oo world = ((CraftWorld)this.getWorld()).getHandle();
        vg launch = null;
        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new aet(world, this.getHandle());
            ((aev)launch).a(this.getHandle(), this.getHandle().w, this.getHandle().v, 0.0f, 1.5f, 1.0f);
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new aew(world, this.getHandle());
            ((aev)launch).a(this.getHandle(), this.getHandle().w, this.getHandle().v, 0.0f, 1.5f, 1.0f);
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new aex(world, this.getHandle());
            ((aev)launch).a(this.getHandle(), this.getHandle().w, this.getHandle().v, 0.0f, 1.5f, 1.0f);
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new afa(world, this.getHandle());
                ((afa)launch).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
            } else {
                launch = SpectralArrow.class.isAssignableFrom(projectile) ? new aeu(world, this.getHandle()) : new afa(world, this.getHandle());
            }
            ((aeh)launch).a(this.getHandle(), this.getHandle().w, this.getHandle().v, 0.0f, 3.0f, 1.0f);
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            launch = LingeringPotion.class.isAssignableFrom(projectile) ? new aez(world, this.getHandle(), CraftItemStack.asNMSCopy(new ItemStack(Material.LINGERING_POTION, 1))) : new aez(world, this.getHandle(), CraftItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
            ((aev)launch).a(this.getHandle(), this.getHandle().w, this.getHandle().v, -20.0f, 0.5f, 1.0f);
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new aey(world, this.getHandle());
            ((aev)launch).a(this.getHandle(), this.getHandle().w, this.getHandle().v, -20.0f, 0.7f, 1.0f);
        } else if (Fish.class.isAssignableFrom(projectile) && this.getHandle() instanceof aed) {
            launch = new acf(world, (aed)this.getHandle());
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();
            Vector direction = location.getDirection().multiply(10);
            launch = SmallFireball.class.isAssignableFrom(projectile) ? new aes(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ()) : (WitherSkull.class.isAssignableFrom(projectile) ? new afb(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ()) : (DragonFireball.class.isAssignableFrom(projectile) ? new aei(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ()) : new aen(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ())));
            ((ael)launch).projectileSource = this;
            launch.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (LlamaSpit.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();
            Vector direction = location.getDirection();
            launch = new aeo(world);
            ((aeo)launch).a = this.getHandle();
            ((aeo)launch).c(direction.getX(), direction.getY(), direction.getZ(), 1.5f, 10.0f);
            launch.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (ShulkerBullet.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();
            launch = new aer(world, this.getHandle(), null, null);
            launch.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }
        Validate.notNull((Object)launch, (String)"Projectile not supported", (Object[])new Object[0]);
        if (velocity != null) {
            ((Projectile)((Object)launch.getBukkitEntity())).setVelocity(velocity);
        }
        world.a(launch);
        return (T)((Projectile)((Object)launch.getBukkitEntity()));
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        }
        return EntityType.MOD_CUSTOM;
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        return this.getHandle().D(((CraftEntity)other).getHandle());
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return this.getHandle() instanceof vq && !((vq)this.getHandle()).bA;
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        if (this.getHandle() instanceof vq) {
            ((vq)this.getHandle()).bA = !remove;
        }
    }

    @Override
    public EntityEquipment getEquipment() {
        return this.equipment;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        this.getHandle().canPickUpLoot = pickup;
    }

    @Override
    public boolean getCanPickupItems() {
        return this.getHandle().canPickUpLoot;
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        if (this.getHealth() == 0.0) {
            return false;
        }
        return super.teleport(location, cause);
    }

    @Override
    public boolean isLeashed() {
        if (!(this.getHandle() instanceof vq)) {
            return false;
        }
        return ((vq)this.getHandle()).db() != null;
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        if (!this.isLeashed()) {
            throw new IllegalStateException("Entity not leashed");
        }
        return ((vq)this.getHandle()).db().getBukkitEntity();
    }

    private boolean unleash() {
        if (!this.isLeashed()) {
            return false;
        }
        ((vq)this.getHandle()).a(true, false);
        return true;
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        if (this.getHandle() instanceof abx || !(this.getHandle() instanceof vq)) {
            return false;
        }
        if (holder == null) {
            return this.unleash();
        }
        if (holder.isDead()) {
            return false;
        }
        this.unleash();
        ((vq)this.getHandle()).b(((CraftEntity)holder).getHandle(), true);
        return true;
    }

    @Override
    public boolean isGliding() {
        return this.getHandle().k(7);
    }

    @Override
    public void setGliding(boolean gliding) {
        this.getHandle().b(7, gliding);
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        return this.getHandle().craftAttributes.getAttribute(attribute);
    }

    @Override
    public void setAI(boolean ai2) {
        if (this.getHandle() instanceof vq) {
            ((vq)this.getHandle()).n(!ai2);
        }
    }

    @Override
    public boolean hasAI() {
        return this.getHandle() instanceof vq ? !((vq)this.getHandle()).dc() : false;
    }

    @Override
    public void setCollidable(boolean collidable) {
        this.getHandle().collides = collidable;
    }

    @Override
    public boolean isCollidable() {
        return this.getHandle().collides;
    }
}

