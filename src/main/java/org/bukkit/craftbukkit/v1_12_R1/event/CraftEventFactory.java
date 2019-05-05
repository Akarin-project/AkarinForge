/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.base.Functions
 *  javax.annotation.Nullable
 */
package org.bukkit.craftbukkit.v1_12_R1.event;

import com.google.common.base.Function;
import com.google.common.base.Functions;

import io.akarin.forge.AkarinForge;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraftforge.common.util.FakePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftStatistic;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftDamageSource;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.PluginManager;

public class CraftEventFactory {
    public static final ur MELTING = CraftDamageSource.copyOf(ur.c);
    public static final ur POISON = CraftDamageSource.copyOf(ur.o);
    public static Block blockDamage;
    public static vg entityDamage;
    private static final Function<? super Double, Double> ZERO;

    private static boolean canBuild(CraftWorld world, Player player, int x2, int z2) {
        oo worldServer = world.getHandle();
        int spawnSize = Bukkit.getServer().getSpawnRadius();
        if (world.getHandle().dimension != 0) {
            return true;
        }
        if (spawnSize <= 0) {
            return true;
        }
        if (((CraftServer)Bukkit.getServer()).getHandle().m().d()) {
            return true;
        }
        if (player.isOp()) {
            return true;
        }
        et chunkcoordinates = worldServer.T();
        int distanceFromSpawn = Math.max(Math.abs(x2 - chunkcoordinates.p()), Math.abs(z2 - chunkcoordinates.r()));
        return distanceFromSpawn > spawnSize;
    }

    public static <T extends Event> T callEvent(T event) {
        Bukkit.getServer().getPluginManager().callEvent((Event)event);
        return event;
    }

    public static BlockMultiPlaceEvent callBlockMultiPlaceEvent(amu world, aed who, ub hand, List<BlockState> blockStates, int clickedX, int clickedY, int clickedZ) {
        CraftWorld craftWorld = world.getWorld();
        CraftServer craftServer = world.getServer();
        Player player = (Player)((Object)who.getBukkitEntity());
        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        boolean canBuild = true;
        for (int i2 = 0; i2 < blockStates.size(); ++i2) {
            if (CraftEventFactory.canBuild(craftWorld, player, blockStates.get(i2).getX(), blockStates.get(i2).getZ())) continue;
            canBuild = false;
            break;
        }
        ItemStack item = hand == ub.a ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
        BlockMultiPlaceEvent event = new BlockMultiPlaceEvent(blockStates, blockClicked, item, player, canBuild);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static BlockPlaceEvent callBlockPlaceEvent(amu world, aed who, ub hand, BlockState replacedBlockState, int clickedX, int clickedY, int clickedZ) {
        ItemStack item;
        EquipmentSlot equipmentSlot;
        CraftWorld craftWorld = world.getWorld();
        CraftServer craftServer = world.getServer();
        Player player = (Player)((Object)who.getBukkitEntity());
        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        Block placedBlock = replacedBlockState.getBlock();
        boolean canBuild = CraftEventFactory.canBuild(craftWorld, player, placedBlock.getX(), placedBlock.getZ());
        if (hand == ub.a) {
            item = player.getInventory().getItemInMainHand();
            equipmentSlot = EquipmentSlot.HAND;
        } else {
            item = player.getInventory().getItemInOffHand();
            equipmentSlot = EquipmentSlot.OFF_HAND;
        }
        BlockPlaceEvent event = new BlockPlaceEvent(placedBlock, replacedBlockState, blockClicked, item, player, canBuild, equipmentSlot);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerBucketEmptyEvent callPlayerBucketEmptyEvent(aed who, int clickedX, int clickedY, int clickedZ, fa clickedFace, aip itemInHand) {
        return (PlayerBucketEmptyEvent)CraftEventFactory.getPlayerBucketEvent(false, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, air.az);
    }

    public static PlayerBucketFillEvent callPlayerBucketFillEvent(aed who, int clickedX, int clickedY, int clickedZ, fa clickedFace, aip itemInHand, ain bucket) {
        return (PlayerBucketFillEvent)CraftEventFactory.getPlayerBucketEvent(true, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, bucket);
    }

    private static PlayerEvent getPlayerBucketEvent(boolean isFilling, aed who, int clickedX, int clickedY, int clickedZ, fa clickedFace, aip itemstack, ain item) {
        Player player = who == null ? null : (Player)((Object)who.getBukkitEntity());
        CraftItemStack itemInHand = CraftItemStack.asNewCraftStack(item);
        Material bucket = CraftMagicNumbers.getMaterial(itemstack.c());
        CraftWorld craftWorld = (CraftWorld)player.getWorld();
        CraftServer craftServer = (CraftServer)player.getServer();
        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        BlockFace blockFace = CraftBlock.notchToBlockFace(clickedFace);
        PlayerBucketEvent event = null;
        if (isFilling) {
            event = new PlayerBucketFillEvent(player, blockClicked, blockFace, bucket, itemInHand);
            event.setCancelled(!CraftEventFactory.canBuild(craftWorld, player, clickedX, clickedZ));
        } else {
            event = new PlayerBucketEmptyEvent(player, blockClicked, blockFace, bucket, itemInHand);
            ((PlayerBucketEmptyEvent)event).setCancelled(!CraftEventFactory.canBuild(craftWorld, player, clickedX, clickedZ));
        }
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerInteractEvent callPlayerInteractEvent(aed who, Action action, aip itemstack, ub hand) {
        if (action != Action.LEFT_CLICK_AIR && action != Action.RIGHT_CLICK_AIR) {
            throw new AssertionError((Object)String.format("%s performing %s with %s", new Object[]{who, action, itemstack}));
        }
        return CraftEventFactory.callPlayerInteractEvent(who, action, null, fa.d, itemstack, hand);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(aed who, Action action, et position, fa direction, aip itemstack, ub hand) {
        return CraftEventFactory.callPlayerInteractEvent(who, action, position, direction, itemstack, false, hand);
    }

    public static PlayerInteractEvent callPlayerInteractEvent(aed who, Action action, et position, fa direction, aip itemstack, boolean cancelledBlock, ub hand) {
        Player player = who == null ? null : (Player)((Object)who.getBukkitEntity());
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
        CraftWorld craftWorld = (CraftWorld)player.getWorld();
        CraftServer craftServer = (CraftServer)player.getServer();
        Block blockClicked = null;
        if (position != null) {
            blockClicked = craftWorld.getBlockAt(position.p(), position.q(), position.r());
        } else {
            switch (action) {
                case LEFT_CLICK_BLOCK: {
                    action = Action.LEFT_CLICK_AIR;
                    break;
                }
                case RIGHT_CLICK_BLOCK: {
                    action = Action.RIGHT_CLICK_AIR;
                }
            }
        }
        BlockFace blockFace = CraftBlock.notchToBlockFace(direction);
        if (itemInHand.getType() == Material.AIR || itemInHand.getAmount() == 0) {
            itemInHand = null;
        }
        PlayerInteractEvent event = new PlayerInteractEvent(player, action, itemInHand, blockClicked, blockFace, hand == null ? null : (hand == ub.b ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND));
        if (cancelledBlock) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static EntityShootBowEvent callEntityShootBowEvent(vp who, aip itemstack, aeh entityArrow, float force) {
        LivingEntity shooter = (LivingEntity)((Object)who.getBukkitEntity());
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
        Arrow arrow = (Arrow)((Object)entityArrow.getBukkitEntity());
        if (itemInHand != null && (itemInHand.getType() == Material.AIR || itemInHand.getAmount() == 0)) {
            itemInHand = null;
        }
        EntityShootBowEvent event = new EntityShootBowEvent(shooter, itemInHand, arrow, force);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static BlockDamageEvent callBlockDamageEvent(aed who, int x2, int y2, int z2, aip itemstack, boolean instaBreak) {
        Player player = who == null ? null : (Player)((Object)who.getBukkitEntity());
        CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
        CraftWorld craftWorld = (CraftWorld)player.getWorld();
        CraftServer craftServer = (CraftServer)player.getServer();
        Block blockClicked = craftWorld.getBlockAt(x2, y2, z2);
        BlockDamageEvent event = new BlockDamageEvent(player, blockClicked, itemInHand, instaBreak);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static CreatureSpawnEvent callCreatureSpawnEvent(vp entityliving, CreatureSpawnEvent.SpawnReason spawnReason) {
        LivingEntity entity = (LivingEntity)((Object)entityliving.getBukkitEntity());
        CraftServer craftServer = (CraftServer)entity.getServer();
        CreatureSpawnEvent event = new CreatureSpawnEvent(entity, spawnReason);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTameEvent callEntityTameEvent(vq entity, aed tamer) {
        CraftEntity bukkitEntity = entity.getBukkitEntity();
        CraftHumanEntity bukkitTamer = tamer != null ? tamer.getBukkitEntity() : null;
        CraftServer craftServer = (CraftServer)bukkitEntity.getServer();
        entity.bA = true;
        EntityTameEvent event = new EntityTameEvent((LivingEntity)((Object)bukkitEntity), bukkitTamer);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static ItemSpawnEvent callItemSpawnEvent(acl entityitem) {
        Item entity = (Item)((Object)entityitem.getBukkitEntity());
        CraftServer craftServer = (CraftServer)entity.getServer();
        ItemSpawnEvent event = new ItemSpawnEvent(entity, entity.getLocation());
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    public static ItemDespawnEvent callItemDespawnEvent(acl entityitem) {
        Item entity = (Item)((Object)entityitem.getBukkitEntity());
        ItemDespawnEvent event = new ItemDespawnEvent(entity, entity.getLocation());
        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static ItemMergeEvent callItemMergeEvent(acl merging, acl mergingWith) {
        Item entityMerging = (Item)((Object)merging.getBukkitEntity());
        Item entityMergingWith = (Item)((Object)mergingWith.getBukkitEntity());
        ItemMergeEvent event = new ItemMergeEvent(entityMerging, entityMergingWith);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PotionSplashEvent callPotionSplashEvent(aez potion, Map<LivingEntity, Double> affectedEntities) {
        ThrownPotion thrownPotion = (ThrownPotion)((Object)potion.getBukkitEntity());
        PotionSplashEvent event = new PotionSplashEvent(thrownPotion, affectedEntities);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static LingeringPotionSplashEvent callLingeringPotionSplashEvent(aez potion, ve cloud) {
        ThrownPotion thrownPotion = (ThrownPotion)((Object)potion.getBukkitEntity());
        AreaEffectCloud effectCloud = (AreaEffectCloud)((Object)cloud.getBukkitEntity());
        LingeringPotionSplashEvent event = new LingeringPotionSplashEvent(thrownPotion, effectCloud);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static BlockFadeEvent callBlockFadeEvent(Block block, aow type) {
        BlockState state = block.getState();
        state.setTypeId(aow.a(type));
        BlockFadeEvent event = new BlockFadeEvent(block, state);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static boolean handleBlockSpreadEvent(Block block, Block source, aow type, int data) {
        BlockState state = block.getState();
        state.setTypeId(aow.a(type));
        state.setRawData((byte)data);
        BlockSpreadEvent event = new BlockSpreadEvent(block, source, state);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            state.update(true);
        }
        return !event.isCancelled();
    }

    public static EntityDeathEvent callEntityDeathEvent(vp victim) {
        return CraftEventFactory.callEntityDeathEvent(victim, new ArrayList<ItemStack>(0));
    }

    public static EntityDeathEvent callEntityDeathEvent(vp victim, List<ItemStack> drops) {
        CraftLivingEntity entity = (CraftLivingEntity)victim.getBukkitEntity();
        EntityDeathEvent event = new EntityDeathEvent(entity, drops, victim.getExpReward());
        Bukkit.getServer().getPluginManager().callEvent(event);
        victim.expToDrop = event.getDroppedExp();
        victim.capturedDrops.clear();
        for (ItemStack stack : event.getDrops()) {
            acl entityitem = new acl(victim.l, entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), CraftItemStack.asNMSCopy(stack));
            if (entityitem == null) continue;
            victim.capturedDrops.add(entityitem);
        }
        return event;
    }

    public static PlayerDeathEvent callPlayerDeathEvent(oq victim, List<ItemStack> drops, String deathMessage, boolean keepInventory) {
        CraftPlayer entity = victim.getBukkitEntity();
        PlayerDeathEvent event = new PlayerDeathEvent(entity, drops, victim.getExpReward(), 0, deathMessage);
        event.setKeepInventory(keepInventory);
        Bukkit.getServer().getPluginManager().callEvent(event);
        victim.keepLevel = event.getKeepLevel();
        victim.newLevel = event.getNewLevel();
        victim.newTotalExp = event.getNewTotalExp();
        victim.expToDrop = event.getDroppedExp();
        victim.newExp = event.getNewExp();
        if (event.getKeepInventory()) {
            return event;
        }
        victim.capturedDrops.clear();
        for (ItemStack stack : event.getDrops()) {
            acl entityitem;
            if (stack == null || stack.getType() == Material.AIR || !victim.captureDrops || (entityitem = new acl(victim.l, entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), CraftItemStack.asNMSCopy(stack))) == null) continue;
            victim.capturedDrops.add(entityitem);
        }
        return event;
    }

    public static ServerListPingEvent callServerListPingEvent(Server craftServer, InetAddress address, String motd, int numPlayers, int maxPlayers) {
        ServerListPingEvent event = new ServerListPingEvent(address, motd, numPlayers, maxPlayers);
        craftServer.getPluginManager().callEvent(event);
        return event;
    }

    private static EntityDamageEvent handleEntityDamageEvent(vg entity, ur source, Map<EntityDamageEvent.DamageModifier, Double> modifiers, Map<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> modifierFunctions) {
        if (source.c()) {
            EntityDamageEvent event2;
            EntityDamageEvent event2;
            vg damager = entityDamage;
            entityDamage = null;
            if (damager == null) {
                event2 = new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, modifiers, modifierFunctions);
            } else {
                if (entity instanceof abd) {
                    // empty if block
                }
                EntityDamageEvent.DamageCause damageCause = damager instanceof TNTPrimed ? EntityDamageEvent.DamageCause.BLOCK_EXPLOSION : EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
                event2 = new EntityDamageByEntityEvent(damager.getBukkitEntity(), entity.getBukkitEntity(), damageCause, modifiers, modifierFunctions);
            }
            CraftEventFactory.callEvent(event2);
            if (!event2.isCancelled()) {
                event2.getEntity().setLastDamageCause(event2);
            }
            return event2;
        }
        if (source instanceof us) {
            EntityDamageEvent.DamageCause cause;
            vg damager = source.j();
            EntityDamageEvent.DamageCause damageCause = cause = source.isSweep() ? EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK : EntityDamageEvent.DamageCause.ENTITY_ATTACK;
            if (source instanceof ut) {
                damager = ((ut)source).getProximateDamageSource();
                if (damager != null) {
                    if (damager.getBukkitEntity() instanceof ThrownPotion) {
                        cause = EntityDamageEvent.DamageCause.MAGIC;
                    } else if (damager.getBukkitEntity() instanceof Projectile) {
                        cause = EntityDamageEvent.DamageCause.PROJECTILE;
                    }
                }
            } else if ("thorns".equals(source.u)) {
                cause = EntityDamageEvent.DamageCause.THORNS;
            }
            return CraftEventFactory.callEntityDamageEvent(damager, entity, cause, modifiers, modifierFunctions);
        }
        if (source == ur.m) {
            EntityDamageEvent event = CraftEventFactory.callEvent(new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.VOID, modifiers, modifierFunctions));
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        }
        if (source == ur.d) {
            EntityDamageEvent event = CraftEventFactory.callEvent(new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.LAVA, modifiers, modifierFunctions));
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        }
        if (blockDamage != null) {
            EntityDamageEvent.DamageCause cause = null;
            Block damager = blockDamage;
            blockDamage = null;
            cause = source == ur.j ? EntityDamageEvent.DamageCause.CONTACT : (source == ur.e ? EntityDamageEvent.DamageCause.HOT_FLOOR : (source == ur.k ? EntityDamageEvent.DamageCause.FALL : (source == ur.q || source == ur.r ? EntityDamageEvent.DamageCause.FALLING_BLOCK : (source == ur.a ? EntityDamageEvent.DamageCause.FIRE : (source == ur.c ? EntityDamageEvent.DamageCause.FIRE_TICK : (source == ur.d ? EntityDamageEvent.DamageCause.LAVA : (damager instanceof LightningStrike ? EntityDamageEvent.DamageCause.LIGHTNING : (source == ur.o ? EntityDamageEvent.DamageCause.MAGIC : (source == MELTING ? EntityDamageEvent.DamageCause.MELTING : (source == POISON ? EntityDamageEvent.DamageCause.POISON : (source == ur.n ? EntityDamageEvent.DamageCause.CUSTOM : EntityDamageEvent.DamageCause.CUSTOM)))))))))));
            EntityDamageEvent event = CraftEventFactory.callEvent(new EntityDamageByBlockEvent(damager, entity.getBukkitEntity(), cause, modifiers, modifierFunctions));
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        }
        if (entityDamage != null) {
            EntityDamageEvent.DamageCause cause = null;
            CraftEntity damager = entityDamage.getBukkitEntity();
            entityDamage = null;
            cause = source == ur.q || source == ur.r ? EntityDamageEvent.DamageCause.FALLING_BLOCK : (damager instanceof LightningStrike ? EntityDamageEvent.DamageCause.LIGHTNING : (source == ur.k ? EntityDamageEvent.DamageCause.FALL : (source == ur.s ? EntityDamageEvent.DamageCause.DRAGON_BREATH : (source == ur.o ? EntityDamageEvent.DamageCause.MAGIC : (source == ur.j ? EntityDamageEvent.DamageCause.CONTACT : (source == ur.a ? EntityDamageEvent.DamageCause.FIRE : (source == ur.c ? EntityDamageEvent.DamageCause.FIRE_TICK : (source == ur.d ? EntityDamageEvent.DamageCause.LAVA : (source == MELTING ? EntityDamageEvent.DamageCause.MELTING : (source == POISON ? EntityDamageEvent.DamageCause.POISON : EntityDamageEvent.DamageCause.CUSTOM))))))))));
            EntityDamageEvent event = CraftEventFactory.callEvent(new EntityDamageByEntityEvent(damager, entity.getBukkitEntity(), cause, modifiers, modifierFunctions));
            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
            }
            return event;
        }
        EntityDamageEvent.DamageCause cause = null;
        if (source == ur.a) {
            cause = EntityDamageEvent.DamageCause.FIRE;
        } else if (source == ur.i) {
            cause = EntityDamageEvent.DamageCause.STARVATION;
        } else if (source == ur.p) {
            cause = EntityDamageEvent.DamageCause.WITHER;
        } else if (source == ur.f) {
            cause = EntityDamageEvent.DamageCause.SUFFOCATION;
        } else if (source == ur.h) {
            cause = EntityDamageEvent.DamageCause.DROWNING;
        } else if (source == ur.c) {
            cause = EntityDamageEvent.DamageCause.FIRE_TICK;
        } else if (source == MELTING) {
            cause = EntityDamageEvent.DamageCause.MELTING;
        } else if (source == POISON) {
            cause = EntityDamageEvent.DamageCause.POISON;
        } else if (source == ur.o) {
            cause = EntityDamageEvent.DamageCause.MAGIC;
        } else if (source == ur.k) {
            cause = EntityDamageEvent.DamageCause.FALL;
        } else if (source == ur.l) {
            cause = EntityDamageEvent.DamageCause.FLY_INTO_WALL;
        } else if (source == ur.g) {
            cause = EntityDamageEvent.DamageCause.CRAMMING;
        } else if (source == ur.n) {
            cause = EntityDamageEvent.DamageCause.CUSTOM;
        }
        if (cause != null) {
            return CraftEventFactory.callEntityDamageEvent(null, entity, cause, modifiers, modifierFunctions);
        }
        return new EntityDamageEvent(entity.getBukkitEntity(), EntityDamageEvent.DamageCause.CUSTOM, modifiers, modifierFunctions);
    }

    private static EntityDamageEvent callEntityDamageEvent(vg damager, vg damagee, EntityDamageEvent.DamageCause cause, Map<EntityDamageEvent.DamageModifier, Double> modifiers, Map<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> modifierFunctions) {
        EntityDamageEvent event = damager != null ? new EntityDamageByEntityEvent(damager.getBukkitEntity(), damagee.getBukkitEntity(), cause, modifiers, modifierFunctions) : new EntityDamageEvent(damagee.getBukkitEntity(), cause, modifiers, modifierFunctions);
        CraftEventFactory.callEvent(event);
        if (!event.isCancelled()) {
            event.getEntity().setLastDamageCause(event);
        }
        return event;
    }

    public static EntityDamageEvent handleLivingEntityDamageEvent(vg damagee, ur source, double rawDamage, double hardHatModifier, double blockingModifier, double armorModifier, double resistanceModifier, double magicModifier, double absorptionModifier, Function<Double, Double> hardHat, Function<Double, Double> blocking, Function<Double, Double> armor, Function<Double, Double> resistance, Function<Double, Double> magic, Function<Double, Double> absorption) {
        EnumMap<EntityDamageEvent.DamageModifier, Double> modifiers = new EnumMap<EntityDamageEvent.DamageModifier, Double>(EntityDamageEvent.DamageModifier.class);
        EnumMap<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> modifierFunctions = new EnumMap<EntityDamageEvent.DamageModifier, Function<? super Double, Double>>(EntityDamageEvent.DamageModifier.class);
        modifiers.put(EntityDamageEvent.DamageModifier.BASE, rawDamage);
        modifierFunctions.put(EntityDamageEvent.DamageModifier.BASE, ZERO);
        if (source == ur.r || source == ur.q) {
            modifiers.put(EntityDamageEvent.DamageModifier.HARD_HAT, hardHatModifier);
            modifierFunctions.put(EntityDamageEvent.DamageModifier.HARD_HAT, hardHat);
        }
        if (damagee instanceof aed) {
            modifiers.put(EntityDamageEvent.DamageModifier.BLOCKING, blockingModifier);
            modifierFunctions.put(EntityDamageEvent.DamageModifier.BLOCKING, blocking);
        }
        modifiers.put(EntityDamageEvent.DamageModifier.ARMOR, armorModifier);
        modifierFunctions.put(EntityDamageEvent.DamageModifier.ARMOR, armor);
        modifiers.put(EntityDamageEvent.DamageModifier.RESISTANCE, resistanceModifier);
        modifierFunctions.put(EntityDamageEvent.DamageModifier.RESISTANCE, resistance);
        modifiers.put(EntityDamageEvent.DamageModifier.MAGIC, magicModifier);
        modifierFunctions.put(EntityDamageEvent.DamageModifier.MAGIC, magic);
        modifiers.put(EntityDamageEvent.DamageModifier.ABSORPTION, absorptionModifier);
        modifierFunctions.put(EntityDamageEvent.DamageModifier.ABSORPTION, absorption);
        return CraftEventFactory.handleEntityDamageEvent(damagee, source, modifiers, modifierFunctions);
    }

    public static boolean handleNonLivingEntityDamageEvent(vg entity, ur source, double damage) {
        return CraftEventFactory.handleNonLivingEntityDamageEvent(entity, source, damage, true);
    }

    public static boolean handleNonLivingEntityDamageEvent(vg entity, ur source, double damage, boolean cancelOnZeroDamage) {
        if (entity instanceof abc && !(source instanceof us)) {
            return false;
        }
        EnumMap<EntityDamageEvent.DamageModifier, Double> modifiers = new EnumMap<EntityDamageEvent.DamageModifier, Double>(EntityDamageEvent.DamageModifier.class);
        EnumMap<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> functions = new EnumMap<EntityDamageEvent.DamageModifier, Function<? super Double, Double>>(EntityDamageEvent.DamageModifier.class);
        modifiers.put(EntityDamageEvent.DamageModifier.BASE, damage);
        functions.put(EntityDamageEvent.DamageModifier.BASE, ZERO);
        EntityDamageEvent event = CraftEventFactory.handleEntityDamageEvent(entity, source, modifiers, functions);
        if (event == null) {
            return false;
        }
        return event.isCancelled() || cancelOnZeroDamage && event.getDamage() == 0.0;
    }

    public static PlayerLevelChangeEvent callPlayerLevelChangeEvent(Player player, int oldLevel, int newLevel) {
        PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(player, oldLevel, newLevel);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerExpChangeEvent callPlayerExpChangeEvent(aed entity, int expAmount) {
        Player player = (Player)((Object)entity.getBukkitEntity());
        PlayerExpChangeEvent event = new PlayerExpChangeEvent(player, expAmount);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerItemMendEvent callPlayerItemMendEvent(aed entity, vm orb, aip nmsMendedItem, int repairAmount) {
        Player player = (Player)((Object)entity.getBukkitEntity());
        CraftItemStack bukkitStack = CraftItemStack.asCraftMirror(nmsMendedItem);
        PlayerItemMendEvent event = new PlayerItemMendEvent(player, bukkitStack, (ExperienceOrb)((Object)orb.getBukkitEntity()), repairAmount);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static boolean handleBlockGrowEvent(amu world, int x2, int y2, int z2, aow type, int data) {
        Block block = world.getWorld().getBlockAt(x2, y2, z2);
        CraftBlockState state = (CraftBlockState)block.getState();
        state.setTypeId(aow.a(type));
        state.setRawData((byte)data);
        BlockGrowEvent event = new BlockGrowEvent(block, state);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            state.update(true);
        }
        return !event.isCancelled();
    }

    public static FoodLevelChangeEvent callFoodLevelChangeEvent(aed entity, int level) {
        FoodLevelChangeEvent event = new FoodLevelChangeEvent(entity.getBukkitEntity(), level);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PigZapEvent callPigZapEvent(vg pig, vg lightning, vg pigzombie) {
        PigZapEvent event = new PigZapEvent((Pig)((Object)pig.getBukkitEntity()), (LightningStrike)((Object)lightning.getBukkitEntity()), (PigZombie)((Object)pigzombie.getBukkitEntity()));
        pig.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static HorseJumpEvent callHorseJumpEvent(vg horse, float power) {
        HorseJumpEvent event = new HorseJumpEvent((AbstractHorse)((Object)horse.getBukkitEntity()), power);
        horse.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(Entity entity, Block block, Material material) {
        return CraftEventFactory.callEntityChangeBlockEvent(entity, block, material, 0);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(vg entity, Block block, Material material) {
        return CraftEventFactory.callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, 0);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(vg entity, Block block, Material material, boolean cancelled) {
        return CraftEventFactory.callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, 0, cancelled);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(vg entity, et position, aow type, int data) {
        Block block = entity.l.getWorld().getBlockAt(position.p(), position.q(), position.r());
        Material material = CraftMagicNumbers.getMaterial(type);
        return CraftEventFactory.callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, data);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(Entity entity, Block block, Material material, int data) {
        return CraftEventFactory.callEntityChangeBlockEvent(entity, block, material, data, false);
    }

    public static EntityChangeBlockEvent callEntityChangeBlockEvent(Entity entity, Block block, Material material, int data, boolean cancelled) {
        EntityChangeBlockEvent event = new EntityChangeBlockEvent(entity, block, material, (byte)data);
        event.setCancelled(cancelled);
        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static CreeperPowerEvent callCreeperPowerEvent(vg creeper, vg lightning, CreeperPowerEvent.PowerCause cause) {
        CreeperPowerEvent event = new CreeperPowerEvent((Creeper)((Object)creeper.getBukkitEntity()), (LightningStrike)((Object)lightning.getBukkitEntity()), cause);
        creeper.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTargetEvent callEntityTargetEvent(vg entity, vg target, EntityTargetEvent.TargetReason reason) {
        EntityTargetEvent event = new EntityTargetEvent(entity.getBukkitEntity(), target == null ? null : target.getBukkitEntity(), reason);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityTargetLivingEntityEvent callEntityTargetLivingEvent(vg entity, vp target, EntityTargetEvent.TargetReason reason) {
        EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(entity.getBukkitEntity(), (LivingEntity)((Object)target.getBukkitEntity()), reason);
        entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityBreakDoorEvent callEntityBreakDoorEvent(vg entity, int x2, int y2, int z2) {
        CraftEntity entity1 = entity.getBukkitEntity();
        Block block = entity1.getWorld().getBlockAt(x2, y2, z2);
        EntityBreakDoorEvent event = new EntityBreakDoorEvent((LivingEntity)((Object)entity1), block);
        entity1.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static afr callInventoryOpenEvent(oq player, afr container) {
        return CraftEventFactory.callInventoryOpenEvent(player, container, false);
    }

    public static afr callInventoryOpenEvent(oq player, afr container, boolean cancelled) {
        if (player.by != player.bx) {
            player.a.a(new lg(player.by.d));
        }
        CraftServer server = player.l.getServer();
        CraftPlayer craftPlayer = player.getBukkitEntity();
        try {
            player.by.transferTo(container, craftPlayer);
        }
        catch (AbstractMethodError abstractMethodError) {
            // empty catch block
        }
        InventoryOpenEvent event = new InventoryOpenEvent(container.getBukkitView());
        event.setCancelled(cancelled);
        if (container.getBukkitView() != null) {
            server.getPluginManager().callEvent(event);
        }
        if (event.isCancelled()) {
            container.transferTo(player.by, craftPlayer);
            return null;
        }
        return container;
    }

    public static aip callPreCraftEvent(afy matrix, aip result, InventoryView lastCraftView, boolean isRepair) {
        CraftInventoryCrafting inventory = new CraftInventoryCrafting(matrix, matrix.resultInventory);
        inventory.setResult(CraftItemStack.asCraftMirror(result));
        PrepareItemCraftEvent event = new PrepareItemCraftEvent(inventory, lastCraftView, isRepair);
        Bukkit.getPluginManager().callEvent(event);
        ItemStack bitem = event.getInventory().getResult();
        return CraftItemStack.asNMSCopy(bitem);
    }

    public static ProjectileLaunchEvent callProjectileLaunchEvent(vg entity) {
        Projectile bukkitEntity = (Projectile)((Object)entity.getBukkitEntity());
        ProjectileLaunchEvent event = new ProjectileLaunchEvent(bukkitEntity);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static ProjectileHitEvent callProjectileHitEvent(vg entity, bhc position) {
        Block hitBlock = null;
        if (position.a == bhc.a.b) {
            et blockposition = position.a();
            hitBlock = entity.getBukkitEntity().getWorld().getBlockAt(blockposition.p(), blockposition.q(), blockposition.r());
        }
        ProjectileHitEvent event = new ProjectileHitEvent((Projectile)((Object)entity.getBukkitEntity()), position.d == null ? null : position.d.getBukkitEntity(), hitBlock);
        entity.l.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static ExpBottleEvent callExpBottleEvent(vg entity, int exp) {
        ThrownExpBottle bottle = (ThrownExpBottle)((Object)entity.getBukkitEntity());
        ExpBottleEvent event = new ExpBottleEvent(bottle, exp);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static BlockRedstoneEvent callRedstoneChange(amu world, int x2, int y2, int z2, int oldCurrent, int newCurrent) {
        BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(x2, y2, z2), oldCurrent, newCurrent);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static NotePlayEvent callNotePlayEvent(amu world, int x2, int y2, int z2, byte instrument, byte note) {
        NotePlayEvent event = new NotePlayEvent(world.getWorld().getBlockAt(x2, y2, z2), Instrument.getByType(instrument), new Note(note));
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static void callPlayerItemBreakEvent(aed human, aip brokenItem) {
        CraftItemStack item = CraftItemStack.asCraftMirror(brokenItem);
        PlayerItemBreakEvent event = new PlayerItemBreakEvent((Player)((Object)human.getBukkitEntity()), item);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static BlockIgniteEvent callBlockIgniteEvent(amu world, int x2, int y2, int z2, int igniterX, int igniterY, int igniterZ) {
        BlockIgniteEvent.IgniteCause cause;
        CraftWorld bukkitWorld = world.getWorld();
        Block igniter = bukkitWorld.getBlockAt(igniterX, igniterY, igniterZ);
        switch (igniter.getType()) {
            case LAVA: 
            case STATIONARY_LAVA: {
                cause = BlockIgniteEvent.IgniteCause.LAVA;
                break;
            }
            case DISPENSER: {
                cause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
                break;
            }
            default: {
                cause = BlockIgniteEvent.IgniteCause.SPREAD;
            }
        }
        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x2, y2, z2), cause, igniter);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(amu world, int x2, int y2, int z2, vg igniter) {
        BlockIgniteEvent.IgniteCause cause;
        CraftWorld bukkitWorld = world.getWorld();
        CraftEntity bukkitIgniter = igniter.getBukkitEntity();
        switch (bukkitIgniter.getType()) {
            case ENDER_CRYSTAL: {
                cause = BlockIgniteEvent.IgniteCause.ENDER_CRYSTAL;
                break;
            }
            case LIGHTNING: {
                cause = BlockIgniteEvent.IgniteCause.LIGHTNING;
                break;
            }
            case SMALL_FIREBALL: 
            case FIREBALL: {
                cause = BlockIgniteEvent.IgniteCause.FIREBALL;
                break;
            }
            default: {
                cause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
            }
        }
        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x2, y2, z2), cause, bukkitIgniter);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(amu world, int x2, int y2, int z2, amp explosion) {
        CraftWorld bukkitWorld = world.getWorld();
        CraftEntity igniter = explosion.h == null ? null : explosion.h.getBukkitEntity();
        BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x2, y2, z2), BlockIgniteEvent.IgniteCause.EXPLOSION, igniter);
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockIgniteEvent callBlockIgniteEvent(amu world, int x2, int y2, int z2, BlockIgniteEvent.IgniteCause cause, vg igniter) {
        BlockIgniteEvent event = new BlockIgniteEvent(world.getWorld().getBlockAt(x2, y2, z2), cause, igniter.getBukkitEntity());
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static void handleInventoryCloseEvent(aed human) {
        InventoryCloseEvent event = new InventoryCloseEvent(human.by.getBukkitView());
        if (human.by.getBukkitView() != null) {
            human.l.getServer().getPluginManager().callEvent(event);
        }
        human.by.transferTo(human.bx, human.getBukkitEntity());
    }

    public static void handleEditBookEvent(oq player, aip newBookItem) {
        int itemInHandIndex = player.bv.d;
        PlayerEditBookEvent editBookEvent = new PlayerEditBookEvent(player.getBukkitEntity(), player.bv.d, (BookMeta)CraftItemStack.getItemMeta(player.bv.i()), (BookMeta)CraftItemStack.getItemMeta(newBookItem), newBookItem.c() == air.bY);
        player.l.getServer().getPluginManager().callEvent(editBookEvent);
        aip itemInHand = player.bv.a(itemInHandIndex);
        if (itemInHand != null && itemInHand.c() == air.bX) {
            if (!editBookEvent.isCancelled()) {
                if (editBookEvent.isSigning()) {
                    itemInHand.setItem(air.bY);
                }
                CraftMetaBook meta = (CraftMetaBook)editBookEvent.getNewBookMeta();
                List<hh> pages = meta.pages;
                for (int i2 = 0; i2 < pages.size(); ++i2) {
                    pages.set(i2, CraftEventFactory.stripEvents(pages.get(i2)));
                }
                CraftItemStack.setItemMeta(itemInHand, meta);
            }
            agr slot = player.by.a(player.bv, itemInHandIndex);
            player.a.a(new iu(player.by.d, slot.e, itemInHand));
        }
    }

    private static hh stripEvents(hh c2) {
        List<hh> ls2;
        hn modi = c2.b();
        if (modi != null) {
            modi.a((hg)null);
            modi.a((hj)null);
        }
        c2.a(modi);
        if (c2 instanceof hp) {
            hp cm2 = (hp)c2;
            Object[] oo2 = cm2.j();
            for (int i2 = 0; i2 < oo2.length; ++i2) {
                Object o2 = oo2[i2];
                if (!(o2 instanceof hh)) continue;
                oo2[i2] = CraftEventFactory.stripEvents((hh)o2);
            }
        }
        if ((ls2 = c2.a()) != null) {
            for (int i3 = 0; i3 < ls2.size(); ++i3) {
                ls2.set(i3, CraftEventFactory.stripEvents(ls2.get(i3)));
            }
        }
        return c2;
    }

    public static PlayerUnleashEntityEvent callPlayerUnleashEntityEvent(vq entity, aed player) {
        PlayerUnleashEntityEvent event = new PlayerUnleashEntityEvent(entity.getBukkitEntity(), (Player)((Object)player.getBukkitEntity()));
        entity.l.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PlayerLeashEntityEvent callPlayerLeashEntityEvent(vq entity, vg leashHolder, aed player) {
        PlayerLeashEntityEvent event = new PlayerLeashEntityEvent(entity.getBukkitEntity(), leashHolder.getBukkitEntity(), (Player)((Object)player.getBukkitEntity()));
        entity.l.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static Cancellable handleStatisticsIncrease(aed entityHuman, qo statistic, int current, int incrementation) {
        PlayerStatisticIncrementEvent event;
        CraftPlayer player = ((oq)entityHuman).getBukkitEntity();
        Statistic stat = CraftStatistic.getBukkitStatistic(statistic);
        if (stat == null) {
            return null;
        }
        switch (stat) {
            case FALL_ONE_CM: 
            case BOAT_ONE_CM: 
            case CLIMB_ONE_CM: 
            case DIVE_ONE_CM: 
            case FLY_ONE_CM: 
            case HORSE_ONE_CM: 
            case MINECART_ONE_CM: 
            case PIG_ONE_CM: 
            case PLAY_ONE_TICK: 
            case SWIM_ONE_CM: 
            case WALK_ONE_CM: 
            case SPRINT_ONE_CM: 
            case CROUCH_ONE_CM: 
            case TIME_SINCE_DEATH: 
            case SNEAK_TIME: {
                return null;
            }
        }
        if (stat.getType() == Statistic.Type.UNTYPED) {
            event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation);
        } else if (stat.getType() == Statistic.Type.ENTITY) {
            EntityType entityType = CraftStatistic.getEntityTypeFromStatistic(statistic);
            event = new PlayerStatisticIncrementEvent((Player)player, stat, current, current + incrementation, entityType);
        } else {
            Material material = CraftStatistic.getMaterialFromStatistic(statistic);
            event = new PlayerStatisticIncrementEvent((Player)player, stat, current, current + incrementation, material);
        }
        entityHuman.l.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static FireworkExplodeEvent callFireworkExplodeEvent(aem firework) {
        FireworkExplodeEvent event = new FireworkExplodeEvent((Firework)((Object)firework.getBukkitEntity()));
        firework.l.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static PrepareAnvilEvent callPrepareAnvilEvent(InventoryView view, aip item) {
        PrepareAnvilEvent event = new PrepareAnvilEvent(view, CraftItemStack.asCraftMirror(item).clone());
        event.getView().getPlayer().getServer().getPluginManager().callEvent(event);
        event.getInventory().setItem(2, event.getResult());
        return event;
    }

    public static SpawnerSpawnEvent callSpawnerSpawnEvent(vg spawnee, et pos) {
        CraftEntity entity = spawnee.getBukkitEntity();
        BlockState state = entity.getWorld().getBlockAt(pos.p(), pos.q(), pos.r()).getState();
        if (!(state instanceof CreatureSpawner)) {
            state = null;
        }
        SpawnerSpawnEvent event = new SpawnerSpawnEvent(entity, (CreatureSpawner)state);
        entity.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static EntityToggleGlideEvent callToggleGlideEvent(vp entity, boolean gliding) {
        EntityToggleGlideEvent event = new EntityToggleGlideEvent((LivingEntity)((Object)entity.getBukkitEntity()), gliding);
        entity.l.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static AreaEffectCloudApplyEvent callAreaEffectCloudApplyEvent(ve cloud, List<LivingEntity> entities) {
        AreaEffectCloudApplyEvent event = new AreaEffectCloudApplyEvent((AreaEffectCloud)((Object)cloud.getBukkitEntity()), entities);
        cloud.l.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static VehicleCreateEvent callVehicleCreateEvent(vg entity) {
        Vehicle bukkitEntity = (Vehicle)((Object)entity.getBukkitEntity());
        VehicleCreateEvent event = new VehicleCreateEvent(bukkitEntity);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static EntityBreedEvent callEntityBreedEvent(vp child, vp mother, vp father, vp breeder, aip bredWith, int experience) {
        LivingEntity breederEntity = (LivingEntity)((Object)(breeder == null ? null : breeder.getBukkitEntity()));
        CraftItemStack bredWithStack = bredWith == null ? null : CraftItemStack.asCraftMirror(bredWith).clone();
        EntityBreedEvent event = new EntityBreedEvent((LivingEntity)((Object)child.getBukkitEntity()), (LivingEntity)((Object)mother.getBukkitEntity()), (LivingEntity)((Object)father.getBukkitEntity()), breederEntity, bredWithStack, experience);
        child.l.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static BlockPhysicsEvent callBlockPhysicsEvent(amu world, et blockposition) {
        Block block = world.getWorld().getBlockAt(blockposition.p(), blockposition.q(), blockposition.r());
        BlockPhysicsEvent event = new BlockPhysicsEvent(block, block.getTypeId());
        world.getServer().getPluginManager().callEvent(event);
        return event;
    }

    public static boolean handleBlockFormEvent(amu world, et pos, awt block, @Nullable vg entity) {
        BlockState blockState = world.getWorld().getBlockAt(pos.p(), pos.q(), pos.r()).getState();
        blockState.setType(CraftMagicNumbers.getMaterial(block.u()));
        blockState.setRawData((byte)block.u().e(block));
        BlockFormEvent event = entity == null ? new BlockFormEvent(blockState.getBlock(), blockState) : new EntityBlockFormEvent(entity.getBukkitEntity(), blockState.getBlock(), blockState);
        world.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            blockState.update(true);
        }
        return !event.isCancelled();
    }

    public static BlockBreakEvent callBlockBreakEvent(amu world, et pos, awt iBlockState, oq player) {
        Block bukkitBlock = world.getWorld().getBlockAt(pos.p(), pos.q(), pos.r());
        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(bukkitBlock, player.getBukkitEntity());
        oq playermp = player;
        aow block = iBlockState.u();
        if (!(playermp instanceof FakePlayer)) {
            boolean isSwordNoBreak;
            boolean bl2 = isSwordNoBreak = playermp.c.b().d() && !playermp.co().b() && playermp.co().c() instanceof ajy;
            if (!isSwordNoBreak) {
                int exp = 0;
                if (block != null && player.c(block.t()) && (!block.canSilkHarvest(world, pos, block.s().b(), player) || alm.a(alo.t, player.co()) <= 0)) {
                    int meta = block.e(block.s().b());
                    int bonusLevel = alm.a(alo.v, player.co());
                    exp = block.getExpDrop(iBlockState, world, pos, bonusLevel);
                }
                blockBreakEvent.setExpToDrop(exp);
            } else {
                blockBreakEvent.setCancelled(true);
            }
        }
        world.getServer().getPluginManager().callEvent(blockBreakEvent);
        return blockBreakEvent;
    }

    static {
        ZERO = Functions.constant((Object)-0.0);
    }

}

