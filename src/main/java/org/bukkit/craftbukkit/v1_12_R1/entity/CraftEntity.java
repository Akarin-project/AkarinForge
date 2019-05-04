/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Lists
 *  com.mojang.authlib.GameProfile
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import catserver.server.PlayerDataFixer;
import catserver.server.entity.CraftCustomEntity;
import catserver.server.entity.CraftCustomProjectile;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAmbient;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAnimals;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAreaEffectCloud;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftBat;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftBlaze;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftBoat;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCaveSpider;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftComplexPart;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreeper;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftDonkey;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftDragonFireball;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEgg;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftElderGuardian;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderCrystal;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderDragon;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderDragonPart;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderPearl;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderSignal;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderman;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEndermite;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEvoker;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEvokerFangs;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftExperienceOrb;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFallingBlock;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFirework;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFish;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFlying;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFuckPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftGhast;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftGiant;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftGuardian;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHanging;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHusk;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftIllager;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftIllusioner;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftIronGolem;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItem;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItemFrame;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLargeFireball;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLeash;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLightningStrike;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLingeringPotion;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLlama;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLlamaSpit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMinecart;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMinecartChest;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMinecartFurnace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMinecartHopper;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMinecartMobSpawner;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMinecartRideable;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMinecartTNT;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMonster;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMule;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMushroomCow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftOcelot;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPainting;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftParrot;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPig;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPigZombie;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPolarBear;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftProjectile;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftRabbit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSheep;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftShulker;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftShulkerBullet;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSilverfish;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSkeleton;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSkeletonHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSlime;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSmallFireball;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSnowball;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSnowman;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSpectralArrow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSpellcaster;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSpider;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSplashPotion;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSquid;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftStray;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftTNTPrimed;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftTameableAnimal;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftThrownExpBottle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftTippedArrow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVex;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVillagerZombie;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVindicator;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWaterMob;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWeather;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWitch;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWither;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWitherSkeleton;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWitherSkull;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWolf;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftZombie;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftZombieHorse;
import org.bukkit.craftbukkit.v1_12_R1.metadata.EntityMetadataStore;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public abstract class CraftEntity
implements Entity {
    private static PermissibleBase perm;
    protected final CraftServer server;
    protected vg entity;
    private EntityDamageEvent lastDamageEvent;
    private final Entity.Spigot spigot;

    public CraftEntity(CraftServer server, vg entity) {
        this.spigot = new Entity.Spigot(){

            @Override
            public boolean isInvulnerable() {
                return CraftEntity.this.getHandle().b(ur.n);
            }
        };
        this.server = server;
        this.entity = entity;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static CraftEntity getEntity(CraftServer server, vg entity) {
        if (entity instanceof vp) {
            if (entity instanceof aed) {
                if (!(entity instanceof oq)) return new CraftFuckPlayer(server, FakePlayerFactory.get(DimensionManager.getWorld(entity.l.s.getDimension()), ((aed)entity).da()));
                if (!(entity instanceof FakePlayer)) return new CraftPlayer(server, (oq)entity);
                return new CraftFuckPlayer(server, (FakePlayer)entity);
            }
            if (entity instanceof aal) {
                if (!(entity instanceof aaj)) return new CraftWaterMob(server, (aal)entity);
                return new CraftSquid(server, (aaj)entity);
            }
            if (entity instanceof vx) {
                if (entity instanceof zv) {
                    if (entity instanceof zw) {
                        return new CraftChicken(server, (zw)entity);
                    }
                    if (entity instanceof zx) {
                        if (!(entity instanceof aaa)) return new CraftCow(server, (zx)entity);
                        return new CraftMushroomCow(server, (aaa)entity);
                    }
                    if (entity instanceof aad) {
                        return new CraftPig(server, (aad)entity);
                    }
                    if (entity instanceof wb) {
                        if (entity instanceof aam) {
                            return new CraftWolf(server, (aam)entity);
                        }
                        if (entity instanceof aab) {
                            return new CraftOcelot(server, (aab)entity);
                        }
                        if (!(entity instanceof aac)) return new CraftTameableAnimal(server, (wb)entity);
                        return new CraftParrot(server, (aac)entity);
                    }
                    if (entity instanceof aag) {
                        return new CraftSheep(server, (aag)entity);
                    }
                    if (entity instanceof aao) {
                        if (entity instanceof aan) {
                            if (entity instanceof aap) {
                                return new CraftDonkey(server, (aap)entity);
                            }
                            if (entity instanceof aat) {
                                return new CraftMule(server, (aat)entity);
                            }
                            if (!(entity instanceof aas)) throw new AssertionError((Object)("Unknown entity " + (entity == null ? " is null" : entity.getClass() + ": " + entity)));
                            return new CraftLlama(server, (aas)entity);
                        }
                        if (entity instanceof aaq) {
                            return new CraftHorse(server, (aaq)entity);
                        }
                        if (entity instanceof aau) {
                            return new CraftSkeletonHorse(server, (aau)entity);
                        }
                        if (!(entity instanceof aaw)) throw new AssertionError((Object)("Unknown entity " + (entity == null ? " is null" : entity.getClass() + ": " + entity)));
                        return new CraftZombieHorse(server, (aaw)entity);
                    }
                    if (entity instanceof aaf) {
                        return new CraftRabbit(server, (aaf)entity);
                    }
                    if (!(entity instanceof aae)) return new CraftAnimals(server, (zv)entity);
                    return new CraftPolarBear(server, (aae)entity);
                }
                if (entity instanceof ade) {
                    if (entity instanceof adt) {
                        if (entity instanceof adf) {
                            return new CraftPigZombie(server, (adf)entity);
                        }
                        if (entity instanceof adb) {
                            return new CraftHusk(server, (adb)entity);
                        }
                        if (!(entity instanceof adu)) return new CraftZombie(server, (adt)entity);
                        return new CraftVillagerZombie(server, (adu)entity);
                    }
                    if (entity instanceof acs) {
                        return new CraftCreeper(server, (acs)entity);
                    }
                    if (entity instanceof acu) {
                        return new CraftEnderman(server, (acu)entity);
                    }
                    if (entity instanceof adj) {
                        return new CraftSilverfish(server, (adj)entity);
                    }
                    if (entity instanceof acz) {
                        return new CraftGiant(server, (acz)entity);
                    }
                    if (entity instanceof acp) {
                        if (entity instanceof ado) {
                            return new CraftStray(server, (ado)entity);
                        }
                        if (!(entity instanceof ads)) return new CraftSkeleton(server, (acp)entity);
                        return new CraftWitherSkeleton(server, (ads)entity);
                    }
                    if (entity instanceof acq) {
                        return new CraftBlaze(server, (acq)entity);
                    }
                    if (entity instanceof adr) {
                        return new CraftWitch(server, (adr)entity);
                    }
                    if (entity instanceof abx) {
                        return new CraftWither(server, (abx)entity);
                    }
                    if (entity instanceof adn) {
                        if (!(entity instanceof acr)) return new CraftSpider(server, (adn)entity);
                        return new CraftCaveSpider(server, (acr)entity);
                    }
                    if (entity instanceof acv) {
                        return new CraftEndermite(server, (acv)entity);
                    }
                    if (entity instanceof ada) {
                        if (!(entity instanceof act)) return new CraftGuardian(server, (ada)entity);
                        return new CraftElderGuardian(server, (act)entity);
                    }
                    if (entity instanceof adp) {
                        return new CraftVex(server, (adp)entity);
                    }
                    if (!(entity instanceof aco)) return new CraftMonster(server, (ade)entity);
                    if (entity instanceof adm) {
                        if (entity instanceof acx) {
                            return new CraftEvoker(server, (acx)entity);
                        }
                        if (!(entity instanceof adc)) return new CraftSpellcaster(server, (adm)entity);
                        return new CraftIllusioner(server, (adc)entity);
                    }
                    if (!(entity instanceof adq)) return new CraftIllager(server, (aco)entity);
                    return new CraftVindicator(server, (adq)entity);
                }
                if (entity instanceof zz) {
                    if (entity instanceof aai) {
                        return new CraftSnowman(server, (aai)entity);
                    }
                    if (entity instanceof aak) {
                        return new CraftIronGolem(server, (aak)entity);
                    }
                    if (!(entity instanceof adi)) return new CraftLivingEntity(server, (vp)entity);
                    return new CraftShulker(server, (adi)entity);
                }
                if (!(entity instanceof ady)) return new CraftCreature(server, (vx)entity);
                return new CraftVillager(server, (ady)entity);
            }
            if (entity instanceof adl) {
                if (!(entity instanceof add)) return new CraftSlime(server, (adl)entity);
                return new CraftMagmaCube(server, (add)entity);
            }
            if (entity instanceof vn) {
                if (!(entity instanceof acy)) return new CraftFlying(server, (vn)entity);
                return new CraftGhast(server, (acy)entity);
            }
            if (entity instanceof abd) {
                return new CraftEnderDragon(server, (abd)entity);
            }
            if (entity instanceof zs) {
                if (!(entity instanceof zt)) return new CraftAmbient(server, (zs)entity);
                return new CraftBat(server, (zt)entity);
            }
            if (!(entity instanceof abz)) return new CraftLivingEntity(server, (vp)entity);
            return new CraftArmorStand(server, (abz)entity);
        }
        if (entity instanceof abb) {
            abb part = (abb)entity;
            if (!(part.a instanceof abd)) return new CraftComplexPart(server, (abb)entity);
            return new CraftEnderDragonPart(server, (abb)entity);
        }
        if (entity instanceof vm) {
            return new CraftExperienceOrb(server, (vm)entity);
        }
        if (entity instanceof afa) {
            if (!((afa)entity).isTipped()) return new CraftArrow(server, (aeh)entity);
            return new CraftTippedArrow(server, (afa)entity);
        }
        if (entity instanceof aeu) {
            return new CraftSpectralArrow(server, (aeu)entity);
        }
        if (entity instanceof aeh) {
            return new CraftArrow(server, (aeh)entity);
        }
        if (entity instanceof afd) {
            return new CraftBoat(server, (afd)entity);
        }
        if (entity instanceof aev) {
            if (entity instanceof aew) {
                return new CraftEgg(server, (aew)entity);
            }
            if (entity instanceof aet) {
                return new CraftSnowball(server, (aet)entity);
            }
            if (entity instanceof aez) {
                if (((aez)entity).p()) return new CraftLingeringPotion(server, (aez)entity);
                return new CraftSplashPotion(server, (aez)entity);
            }
            if (entity instanceof aex) {
                return new CraftEnderPearl(server, (aex)entity);
            }
            if (!(entity instanceof aey)) return new CraftProjectile(server, entity);
            return new CraftThrownExpBottle(server, (aey)entity);
        }
        if (entity instanceof ack) {
            return new CraftFallingBlock(server, (ack)entity);
        }
        if (entity instanceof ael) {
            if (entity instanceof aes) {
                return new CraftSmallFireball(server, (aes)entity);
            }
            if (entity instanceof aen) {
                return new CraftLargeFireball(server, (aen)entity);
            }
            if (entity instanceof afb) {
                return new CraftWitherSkull(server, (afb)entity);
            }
            if (!(entity instanceof aei)) return new CraftFireball(server, (ael)entity);
            return new CraftDragonFireball(server, (aei)entity);
        }
        if (entity instanceof aek) {
            return new CraftEnderSignal(server, (aek)entity);
        }
        if (entity instanceof abc) {
            return new CraftEnderCrystal(server, (abc)entity);
        }
        if (entity instanceof acf) {
            return new CraftFish(server, (acf)entity);
        }
        if (entity instanceof acl) {
            return new CraftItem(server, (acl)entity);
        }
        if (entity instanceof ach) {
            if (!(entity instanceof aci)) return new CraftWeather(server, (ach)entity);
            return new CraftLightningStrike(server, (aci)entity);
        }
        if (entity instanceof afe) {
            if (entity instanceof afi) {
                return new CraftMinecartFurnace(server, (afi)entity);
            }
            if (entity instanceof aff) {
                return new CraftMinecartChest(server, (aff)entity);
            }
            if (entity instanceof afm) {
                return new CraftMinecartTNT(server, (afm)entity);
            }
            if (entity instanceof afj) {
                return new CraftMinecartHopper(server, (afj)entity);
            }
            if (entity instanceof afl) {
                return new CraftMinecartMobSpawner(server, (afl)entity);
            }
            if (entity instanceof afk) {
                return new CraftMinecartRideable(server, (afk)entity);
            }
            if (!(entity instanceof afg)) return new CraftMinecart(server, (afe)entity);
            return new CraftMinecartCommand(server, (afg)entity);
        }
        if (entity instanceof aca) {
            if (entity instanceof acd) {
                return new CraftPainting(server, (acd)entity);
            }
            if (entity instanceof acb) {
                return new CraftItemFrame(server, (acb)entity);
            }
            if (!(entity instanceof acc)) return new CraftHanging(server, (aca)entity);
            return new CraftLeash(server, (acc)entity);
        }
        if (entity instanceof acm) {
            return new CraftTNTPrimed(server, (acm)entity);
        }
        if (entity instanceof aem) {
            return new CraftFirework(server, (aem)entity);
        }
        if (entity instanceof aer) {
            return new CraftShulkerBullet(server, (aer)entity);
        }
        if (entity instanceof ve) {
            return new CraftAreaEffectCloud(server, (ve)entity);
        }
        if (entity instanceof aej) {
            return new CraftEvokerFangs(server, (aej)entity);
        }
        if (entity instanceof aeo) {
            return new CraftLlamaSpit(server, (aeo)entity);
        }
        if (entity == null) throw new AssertionError((Object)("Unknown entity " + (entity == null ? " is null" : entity.getClass() + ": " + entity)));
        if (!(entity instanceof aep)) return new CraftCustomEntity(server, entity);
        return new CraftCustomProjectile(server, entity);
    }

    @Override
    public Location getLocation() {
        return new Location(this.getWorld(), this.entity.p, this.entity.q, this.entity.r, this.entity.getBukkitYaw(), this.entity.w);
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(this.getWorld());
            loc.setX(this.entity.p);
            loc.setY(this.entity.q);
            loc.setZ(this.entity.r);
            loc.setYaw(this.entity.getBukkitYaw());
            loc.setPitch(this.entity.w);
        }
        return loc;
    }

    @Override
    public Vector getVelocity() {
        PlayerDataFixer.checkVector(this.entity);
        return new Vector(this.entity.s, this.entity.t, this.entity.u);
    }

    @Override
    public void setVelocity(Vector velocity) {
        Preconditions.checkArgument((boolean)(velocity != null), (Object)"velocity");
        velocity.checkFinite();
        this.entity.s = velocity.getX();
        this.entity.t = velocity.getY();
        this.entity.u = velocity.getZ();
        this.entity.D = true;
    }

    @Override
    public double getHeight() {
        return this.getHandle().H;
    }

    @Override
    public double getWidth() {
        return this.getHandle().G;
    }

    @Override
    public boolean isOnGround() {
        if (this.entity instanceof aeh) {
            return ((aeh)this.entity).z;
        }
        return this.entity.z;
    }

    @Override
    public World getWorld() {
        return this.entity.l.getWorld();
    }

    @Override
    public boolean teleport(Location location) {
        return this.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        Preconditions.checkArgument((boolean)(location != null), (Object)"location");
        location.checkFinite();
        if (this.entity.aT() || this.entity.F) {
            return false;
        }
        this.entity.o();
        this.entity.l = ((CraftWorld)location.getWorld()).getHandle();
        this.entity.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.entity.g(location.getYaw());
        this.entity.l.a(this.entity, false);
        return true;
    }

    @Override
    public boolean teleport(Entity destination) {
        return this.teleport(destination.getLocation());
    }

    @Override
    public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
        return this.teleport(destination.getLocation(), cause);
    }

    @Override
    public List<Entity> getNearbyEntities(double x2, double y2, double z2) {
        List<vg> notchEntityList = this.entity.l.a(this.entity, this.entity.bw().c(x2, y2, z2), null);
        ArrayList<Entity> bukkitEntityList = new ArrayList<Entity>(notchEntityList.size());
        for (vg e2 : notchEntityList) {
            bukkitEntityList.add(e2.getBukkitEntity());
        }
        return bukkitEntityList;
    }

    @Override
    public int getEntityId() {
        return this.entity.S();
    }

    @Override
    public int getFireTicks() {
        return this.entity.az;
    }

    @Override
    public int getMaxFireTicks() {
        return this.entity.bL();
    }

    @Override
    public void setFireTicks(int ticks) {
        this.entity.az = ticks;
    }

    @Override
    public void remove() {
        this.entity.X();
    }

    @Override
    public boolean isDead() {
        return !this.entity.aC();
    }

    @Override
    public boolean isValid() {
        return this.entity.aC() && this.entity.valid;
    }

    @Override
    public Server getServer() {
        return this.server;
    }

    public Vector getMomentum() {
        return this.getVelocity();
    }

    public void setMomentum(Vector value) {
        this.setVelocity(value);
    }

    @Override
    public Entity getPassenger() {
        return this.isEmpty() ? null : this.getHandle().bF().get(0).getBukkitEntity();
    }

    @Override
    public boolean setPassenger(Entity passenger) {
        Preconditions.checkArgument((boolean)(!this.equals(passenger)), (Object)"Entity cannot ride itself.");
        if (passenger instanceof CraftEntity) {
            this.eject();
            return ((CraftEntity)passenger).getHandle().m(this.getHandle());
        }
        return false;
    }

    @Override
    public List<Entity> getPassengers() {
        return Lists.newArrayList((Iterable)Lists.transform(this.getHandle().bF(), (Function)new Function<vg, Entity>(){

            public Entity apply(vg input) {
                return input.getBukkitEntity();
            }
        }));
    }

    @Override
    public boolean addPassenger(Entity passenger) {
        Preconditions.checkArgument((boolean)(passenger != null), (Object)"passenger == null");
        return ((CraftEntity)passenger).getHandle().a(this.getHandle(), true);
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        Preconditions.checkArgument((boolean)(passenger != null), (Object)"passenger == null");
        ((CraftEntity)passenger).getHandle().o();
        return true;
    }

    @Override
    public boolean isEmpty() {
        return !this.getHandle().aT();
    }

    @Override
    public boolean eject() {
        if (this.isEmpty()) {
            return false;
        }
        this.getHandle().aH();
        return true;
    }

    @Override
    public float getFallDistance() {
        return this.getHandle().L;
    }

    @Override
    public void setFallDistance(float distance) {
        this.getHandle().L = distance;
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        this.lastDamageEvent = event;
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return this.lastDamageEvent;
    }

    @Override
    public UUID getUniqueId() {
        return this.getHandle().bm();
    }

    @Override
    public int getTicksLived() {
        return this.getHandle().T;
    }

    @Override
    public void setTicksLived(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Age must be at least 1 tick");
        }
        this.getHandle().T = value;
    }

    public vg getHandle() {
        return this.entity;
    }

    @Override
    public void playEffect(EntityEffect type) {
        Preconditions.checkArgument((boolean)(type != null), (Object)"type");
        if (type.getApplicable().isInstance(this)) {
            this.getHandle().l.a(this.getHandle(), type.getData());
        }
    }

    public void setHandle(vg entity) {
        this.entity = entity;
    }

    public String toString() {
        return "CraftEntity{id=" + this.getEntityId() + '}';
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        CraftEntity other = (CraftEntity)obj;
        return this.getEntityId() == other.getEntityId();
    }

    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.getEntityId();
        return hash;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getEntityMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.server.getEntityMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean isInsideVehicle() {
        return this.getHandle().aS();
    }

    @Override
    public boolean leaveVehicle() {
        if (!this.isInsideVehicle()) {
            return false;
        }
        this.getHandle().o();
        return true;
    }

    @Override
    public Entity getVehicle() {
        if (!this.isInsideVehicle()) {
            return null;
        }
        return this.getHandle().bJ().getBukkitEntity();
    }

    @Override
    public void setCustomName(String name) {
        if (name == null) {
            name = "";
        }
        this.getHandle().c(name);
    }

    @Override
    public String getCustomName() {
        String name = this.getHandle().bq();
        if (name == null || name.length() == 0) {
            if (this.getType().getEntityClass() == CraftCustomEntity.class && this instanceof CraftLivingEntity) {
                return ((CraftLivingEntity)this).entity.h_();
            }
            return null;
        }
        return name;
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        this.getHandle().j(flag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.getHandle().br();
    }

    @Override
    public void sendMessage(String message) {
    }

    @Override
    public void sendMessage(String[] messages) {
    }

    @Override
    public String getName() {
        return this.getHandle().h_();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return CraftEntity.getPermissibleBase().isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return CraftEntity.getPermissibleBase().isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return CraftEntity.getPermissibleBase().hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return CraftEntity.getPermissibleBase().hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return CraftEntity.getPermissibleBase().addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return CraftEntity.getPermissibleBase().addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return CraftEntity.getPermissibleBase().addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return CraftEntity.getPermissibleBase().addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        CraftEntity.getPermissibleBase().removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        CraftEntity.getPermissibleBase().recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return CraftEntity.getPermissibleBase().getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return CraftEntity.getPermissibleBase().isOp();
    }

    @Override
    public void setOp(boolean value) {
        CraftEntity.getPermissibleBase().setOp(value);
    }

    @Override
    public void setGlowing(boolean flag) {
        this.getHandle().as = flag;
        vg e2 = this.getHandle();
        if (e2.k(6) != flag) {
            e2.b(6, flag);
        }
    }

    @Override
    public boolean isGlowing() {
        return this.getHandle().as;
    }

    @Override
    public void setInvulnerable(boolean flag) {
        this.getHandle().i(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return this.getHandle().b(ur.n);
    }

    @Override
    public boolean isSilent() {
        return this.getHandle().ai();
    }

    @Override
    public void setSilent(boolean flag) {
        this.getHandle().c(flag);
    }

    @Override
    public boolean hasGravity() {
        return !this.getHandle().aj();
    }

    @Override
    public void setGravity(boolean gravity) {
        this.getHandle().d(!gravity);
    }

    @Override
    public int getPortalCooldown() {
        return this.getHandle().aj;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        this.getHandle().aj = cooldown;
    }

    @Override
    public Set<String> getScoreboardTags() {
        return this.getHandle().T();
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        return this.getHandle().a(tag);
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        return this.getHandle().b(tag);
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(this.getHandle().o_().ordinal());
    }

    protected fy save() {
        fy nbttagcompound = new fy();
        nbttagcompound.a("id", this.getHandle().aB());
        this.getHandle().e(nbttagcompound);
        return nbttagcompound;
    }

    private static PermissibleBase getPermissibleBase() {
        if (perm == null) {
            perm = new PermissibleBase(new ServerOperator(){

                @Override
                public boolean isOp() {
                    return false;
                }

                @Override
                public void setOp(boolean value) {
                }
            });
        }
        return perm;
    }

    @Override
    public Entity.Spigot org_bukkit_entity_Entity$Spigot_spigot() {
        return this.spigot;
    }

}

