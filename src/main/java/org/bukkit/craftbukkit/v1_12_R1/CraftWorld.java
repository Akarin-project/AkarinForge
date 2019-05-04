/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Predicate
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  it.unimi.dsi.fastutil.objects.ObjectCollection
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.commons.lang3.Validate;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.CraftEffect;
import org.bukkit.craftbukkit.v1_12_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftSound;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorldBorder;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItem;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLightningStrike;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.metadata.BlockMetadataStore;
import org.bukkit.craftbukkit.v1_12_R1.metadata.WorldMetadataStore;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Boat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Illager;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Mule;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Slime;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Stray;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Weather;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import org.spigotmc.AsyncCatcher;

public class CraftWorld
implements World {
    public static final int CUSTOM_DIMENSION_OFFSET = 10;
    private final oo world;
    private WorldBorder worldBorder;
    private World.Environment environment;
    private final CraftServer server = (CraftServer)Bukkit.getServer();
    public ChunkGenerator generator;
    private final List<BlockPopulator> populators = new ArrayList<BlockPopulator>();
    private final BlockMetadataStore blockMetadata;
    private int monsterSpawn;
    private int animalSpawn;
    private int waterAnimalSpawn;
    private int ambientSpawn;
    private int chunkLoadCount;
    private int chunkGCTickCount;
    private static final Random rand = new Random();
    private final World.Spigot spigot;

    public CraftWorld(oo world, ChunkGenerator gen, World.Environment env) {
        this.blockMetadata = new BlockMetadataStore(this);
        this.monsterSpawn = -1;
        this.animalSpawn = -1;
        this.waterAnimalSpawn = -1;
        this.ambientSpawn = -1;
        this.chunkLoadCount = 0;
        this.spigot = new World.Spigot(){

            @Override
            public void playEffect(Location location, Effect effect, int id2, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius) {
                ht packet2;
                Validate.notNull((Object)location, (String)"Location cannot be null", (Object[])new Object[0]);
                Validate.notNull((Object)((Object)effect), (String)"Effect cannot be null", (Object[])new Object[0]);
                Validate.notNull((Object)location.getWorld(), (String)"World cannot be null", (Object[])new Object[0]);
                if (effect.getType() != Effect.Type.PARTICLE) {
                    int packetData = effect.getId();
                    packet2 = new jf(packetData, new et(location.getBlockX(), location.getBlockY(), location.getBlockZ()), id2, false);
                } else {
                    fj particle = null;
                    int[] extra = null;
                    for (fj p2 : fj.values()) {
                        if (!effect.getName().startsWith(p2.b().replace("_", ""))) continue;
                        particle = p2;
                        if (effect.getData() == null) break;
                        if (effect.getData().equals(Material.class)) {
                            extra = new int[]{id2};
                            break;
                        }
                        extra = new int[]{data << 12 | id2 & 4095};
                        break;
                    }
                    if (extra == null) {
                        extra = new int[]{};
                    }
                    packet2 = new jg(particle, true, (float)location.getX(), (float)location.getY(), (float)location.getZ(), offsetX, offsetY, offsetZ, speed, particleCount, extra);
                }
                radius *= radius;
                for (Player player : CraftWorld.this.getPlayers()) {
                    ht packet2;
                    int distance;
                    if (((CraftPlayer)player).getHandle().a == null || !location.getWorld().equals(player.getWorld()) || (distance = (int)player.getLocation().distanceSquared(location)) > radius) continue;
                    ((CraftPlayer)player).getHandle().a.a(packet2);
                }
            }

            @Override
            public void playEffect(Location location, Effect effect) {
                CraftWorld.this.playEffect(location, effect, 0);
            }

            @Override
            public LightningStrike strikeLightning(Location loc, boolean isSilent) {
                aci lightning = new aci(CraftWorld.this.world, loc.getX(), loc.getY(), loc.getZ(), false, isSilent);
                CraftWorld.this.world.d(lightning);
                return new CraftLightningStrike(CraftWorld.this.server, lightning);
            }

            @Override
            public LightningStrike strikeLightningEffect(Location loc, boolean isSilent) {
                aci lightning = new aci(CraftWorld.this.world, loc.getX(), loc.getY(), loc.getZ(), true, isSilent);
                CraftWorld.this.world.d(lightning);
                return new CraftLightningStrike(CraftWorld.this.server, lightning);
            }
        };
        this.world = world;
        this.generator = gen;
        this.environment = env;
        if (this.server.chunkGCPeriod > 0) {
            this.chunkGCTickCount = rand.nextInt(this.server.chunkGCPeriod);
        }
    }

    @Override
    public Block getBlockAt(int x2, int y2, int z2) {
        return this.getChunkAt(x2 >> 4, z2 >> 4).getBlock(x2 & 15, y2, z2 & 15);
    }

    @Override
    public int getBlockTypeIdAt(int x2, int y2, int z2) {
        return CraftMagicNumbers.getId(this.world.o(new et(x2, y2, z2)).u());
    }

    @Override
    public int getHighestBlockYAt(int x2, int z2) {
        if (!this.isChunkLoaded(x2 >> 4, z2 >> 4)) {
            this.loadChunk(x2 >> 4, z2 >> 4);
        }
        return this.world.l(new et(x2, 0, z2)).q();
    }

    @Override
    public Location getSpawnLocation() {
        et spawn = this.world.T();
        return new Location(this, spawn.p(), spawn.q(), spawn.r());
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        Preconditions.checkArgument((boolean)(location != null), (Object)"location");
        return this.equals(location.getWorld()) ? this.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ()) : false;
    }

    @Override
    public boolean setSpawnLocation(int x2, int y2, int z2) {
        try {
            Location previousLocation = this.getSpawnLocation();
            this.world.x.a(new et(x2, y2, z2));
            SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
            this.server.getPluginManager().callEvent(event);
            return true;
        }
        catch (Exception e2) {
            return false;
        }
    }

    @Override
    public Chunk getChunkAt(int x2, int z2) {
        return this.world.r().c((int)x2, (int)z2).bukkitChunk;
    }

    @Override
    public Chunk getChunkAt(Block block) {
        return this.getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    @Override
    public boolean isChunkLoaded(int x2, int z2) {
        return this.world.r().d(x2, z2);
    }

    @Override
    public Chunk[] getLoadedChunks() {
        Object[] chunks = this.world.r().e.values().toArray();
        Chunk[] craftChunks = new CraftChunk[chunks.length];
        for (int i2 = 0; i2 < chunks.length; ++i2) {
            axw chunk = (axw)chunks[i2];
            craftChunks[i2] = chunk.bukkitChunk;
        }
        return craftChunks;
    }

    @Override
    public void loadChunk(int x2, int z2) {
        this.loadChunk(x2, z2, true);
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        return this.unloadChunk(chunk.getX(), chunk.getZ());
    }

    @Override
    public boolean unloadChunk(int x2, int z2) {
        return this.unloadChunk(x2, z2, true);
    }

    @Override
    public boolean unloadChunk(int x2, int z2, boolean save) {
        return this.unloadChunk(x2, z2, save, false);
    }

    @Override
    public boolean unloadChunkRequest(int x2, int z2) {
        return this.unloadChunkRequest(x2, z2, true);
    }

    @Override
    public boolean unloadChunkRequest(int x2, int z2, boolean safe) {
        AsyncCatcher.catchOp("chunk unload");
        if (safe && this.isChunkInUse(x2, z2)) {
            return false;
        }
        axw chunk = this.world.r().a(x2, z2);
        if (chunk != null) {
            this.world.r().a(chunk);
        }
        return true;
    }

    @Override
    public boolean unloadChunk(int x2, int z2, boolean save, boolean safe) {
        AsyncCatcher.catchOp("chunk unload");
        if (this.isChunkInUse(x2, z2)) {
            return false;
        }
        return this.unloadChunk0(x2, z2, save);
    }

    private boolean unloadChunk0(int x2, int z2, boolean save) {
        axw chunk = this.world.r().getChunkIfLoaded(x2, z2);
        if (chunk == null) {
            return true;
        }
        this.world.r().a(chunk);
        return chunk.d;
    }

    @Override
    public boolean regenerateChunk(int x2, int z2) {
        if (!this.unloadChunk0(x2, z2, false)) {
            return false;
        }
        long chunkKey = amn.a(x2, z2);
        this.world.r().b.remove(chunkKey);
        axw chunk = null;
        chunk = this.world.r().c.a(x2, z2);
        ot playerChunk = this.world.w().b(x2, z2);
        if (playerChunk != null) {
            playerChunk.f = chunk;
        }
        if (chunk != null) {
            this.world.r().e.put(chunkKey, (Object)chunk);
            chunk.c();
            chunk.populateCB(this.world.r(), this.world.r().c, true);
            this.refreshChunk(x2, z2);
        }
        return chunk != null;
    }

    @Override
    public boolean refreshChunk(int x2, int z2) {
        if (!this.isChunkLoaded(x2, z2)) {
            return false;
        }
        int px2 = x2 << 4;
        int pz2 = z2 << 4;
        int height = this.getMaxHeight() / 16;
        for (int idx = 0; idx < 64; ++idx) {
            this.world.a(new et(px2 + idx / height, idx % height * 16, pz2), aox.a.t(), aox.b.t(), 3);
        }
        this.world.a(new et(px2 + 15, height * 16 - 1, pz2 + 15), aox.a.t(), aox.b.t(), 3);
        return true;
    }

    @Override
    public boolean isChunkInUse(int x2, int z2) {
        return this.world.w().isChunkInUse(x2, z2);
    }

    @Override
    public boolean loadChunk(int x2, int z2, boolean generate) {
        AsyncCatcher.catchOp("chunk load");
        ++this.chunkLoadCount;
        if (generate) {
            return this.world.r().c(x2, z2) != null;
        }
        return this.world.r().b(x2, z2) != null;
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        return this.isChunkLoaded(chunk.getX(), chunk.getZ());
    }

    @Override
    public void loadChunk(Chunk chunk) {
        this.loadChunk(chunk.getX(), chunk.getZ());
        ((CraftChunk)this.getChunkAt((int)chunk.getX(), (int)chunk.getZ())).getHandle().bukkitChunk = chunk;
    }

    public oo getHandle() {
        return this.world;
    }

    @Override
    public Item dropItem(Location loc, ItemStack item) {
        Validate.notNull((Object)item, (String)"Cannot drop a Null item.", (Object[])new Object[0]);
        Validate.isTrue((boolean)(item.getTypeId() != 0), (String)"Cannot drop AIR.", (Object[])new Object[0]);
        acl entity = new acl(this.world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));
        entity.e = 10;
        this.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return new CraftItem(this.world.getServer(), entity);
    }

    private static void randomLocationWithinBlock(Location loc, double xs2, double ys2, double zs2) {
        double prevX = loc.getX();
        double prevY = loc.getY();
        double prevZ = loc.getZ();
        loc.add(xs2, ys2, zs2);
        if (loc.getX() < Math.floor(prevX)) {
            loc.setX(Math.floor(prevX));
        }
        if (loc.getX() >= Math.ceil(prevX)) {
            loc.setX(Math.ceil(prevX - 0.01));
        }
        if (loc.getY() < Math.floor(prevY)) {
            loc.setY(Math.floor(prevY));
        }
        if (loc.getY() >= Math.ceil(prevY)) {
            loc.setY(Math.ceil(prevY - 0.01));
        }
        if (loc.getZ() < Math.floor(prevZ)) {
            loc.setZ(Math.floor(prevZ));
        }
        if (loc.getZ() >= Math.ceil(prevZ)) {
            loc.setZ(Math.ceil(prevZ - 0.01));
        }
    }

    @Override
    public Item dropItemNaturally(Location loc, ItemStack item) {
        double xs2 = (double)(this.world.r.nextFloat() * 0.7f) - 0.35;
        double ys2 = (double)(this.world.r.nextFloat() * 0.7f) - 0.35;
        double zs2 = (double)(this.world.r.nextFloat() * 0.7f) - 0.35;
        loc = loc.clone();
        CraftWorld.randomLocationWithinBlock(loc, xs2, ys2, zs2);
        return this.dropItem(loc, item);
    }

    @Override
    public Arrow spawnArrow(Location loc, Vector velocity, float speed, float spread) {
        return this.spawnArrow(loc, velocity, speed, spread, Arrow.class);
    }

    @Override
    public <T extends Arrow> T spawnArrow(Location loc, Vector velocity, float speed, float spread, Class<T> clazz) {
        vg arrow;
        Validate.notNull((Object)loc, (String)"Can not spawn arrow with a null location", (Object[])new Object[0]);
        Validate.notNull((Object)velocity, (String)"Can not spawn arrow with a null velocity", (Object[])new Object[0]);
        Validate.notNull(clazz, (String)"Can not spawn an arrow with no class", (Object[])new Object[0]);
        if (TippedArrow.class.isAssignableFrom(clazz)) {
            arrow = new afa(this.world);
            arrow.setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
        } else {
            arrow = SpectralArrow.class.isAssignableFrom(clazz) ? new aeu(this.world) : new afa(this.world);
        }
        arrow.b(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        arrow.c(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        this.world.a(arrow);
        return (T)((Arrow)((Object)arrow.getBukkitEntity()));
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType entityType) {
        if (EntityRegistry.entityClassMap.get(entityType.getName()) != null) {
            vg entity = null;
            entity = this.getEntity(EntityRegistry.entityClassMap.get(entityType.getName()), this.world);
            if (entity != null) {
                entity.b(loc.getX(), loc.getY(), loc.getZ(), 0.0f, 0.0f);
                this.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                return entity.getBukkitEntity();
            }
        }
        return this.spawn(loc, entityType.getEntityClass());
    }

    private vg getEntity(Class<? extends vg> aClass, oo world) {
        vq entity = null;
        try {
            entity = (vq)aClass.getConstructor(amu.class).newInstance(world);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return entity;
    }

    @Override
    public LightningStrike strikeLightning(Location loc) {
        aci lightning = new aci(this.world, loc.getX(), loc.getY(), loc.getZ(), false);
        this.world.d(lightning);
        return new CraftLightningStrike(this.server, lightning);
    }

    @Override
    public LightningStrike strikeLightningEffect(Location loc) {
        aci lightning = new aci(this.world, loc.getX(), loc.getY(), loc.getZ(), true);
        this.world.d(lightning);
        return new CraftLightningStrike(this.server, lightning);
    }

    @Override
    public boolean generateTree(Location loc, TreeType type) {
        azu gen2;
        et pos = new et(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        switch (type) {
            azu gen2;
            case BIG_TREE: {
                gen2 = new azh(true);
                break;
            }
            case BIRCH: {
                gen2 = new azi(true, false);
                break;
            }
            case REDWOOD: {
                gen2 = new bau(true);
                break;
            }
            case TALL_REDWOOD: {
                gen2 = new bam();
                break;
            }
            case JUNGLE: {
                awt iblockdata1 = aox.r.t().a(aso.b, asr.a.d);
                awt iblockdata2 = aox.t.t().a(asn.e, asr.a.d).a(arr.b, false);
                gen2 = new bag(true, 10, 20, iblockdata1, iblockdata2);
                break;
            }
            case SMALL_JUNGLE: {
                awt iblockdata1 = aox.r.t().a(aso.b, asr.a.d);
                awt iblockdata2 = aox.t.t().a(asn.e, asr.a.d).a(arr.b, false);
                gen2 = new bax(true, 4 + rand.nextInt(7), iblockdata1, iblockdata2, false);
                break;
            }
            case COCOA_TREE: {
                awt iblockdata1 = aox.r.t().a(aso.b, asr.a.d);
                awt iblockdata2 = aox.t.t().a(asn.e, asr.a.d).a(arr.b, false);
                gen2 = new bax(true, 4 + rand.nextInt(7), iblockdata1, iblockdata2, true);
                break;
            }
            case JUNGLE_BUSH: {
                awt iblockdata1 = aox.r.t().a(aso.b, asr.a.d);
                awt iblockdata2 = aox.t.t().a(asn.e, asr.a.a).a(arr.b, false);
                gen2 = new azx(iblockdata1, iblockdata2);
                break;
            }
            case RED_MUSHROOM: {
                gen2 = new bab(aox.bh);
                break;
            }
            case BROWN_MUSHROOM: {
                gen2 = new bab(aox.bg);
                break;
            }
            case SWAMP: {
                gen2 = new bav();
                break;
            }
            case ACACIA: {
                gen2 = new bar(true);
                break;
            }
            case DARK_OAK: {
                gen2 = new bap(true);
                break;
            }
            case MEGA_REDWOOD: {
                gen2 = new bah(false, rand.nextBoolean());
                break;
            }
            case TALL_BIRCH: {
                gen2 = new azi(true, true);
                break;
            }
            case CHORUS_PLANT: {
                apj.a(this.world, pos, rand, 8);
                return true;
            }
            default: {
                gen2 = new bax(true);
            }
        }
        return gen2.b(this.world, rand, pos);
    }

    @Override
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        this.world.captureTreeGeneration = true;
        this.world.captureBlockSnapshots = true;
        boolean grownTree = this.generateTree(loc, type);
        this.world.captureBlockSnapshots = false;
        this.world.captureTreeGeneration = false;
        if (grownTree) {
            for (BlockSnapshot blocksnapshot : this.world.capturedBlockSnapshots) {
                et position = blocksnapshot.getPos();
                int x2 = position.p();
                int y2 = position.q();
                int z2 = position.r();
                awt oldBlock = this.world.o(position);
                int typeId = aow.a(blocksnapshot.getReplacedBlock().u());
                int data = blocksnapshot.getMeta();
                int flag = blocksnapshot.getFlag();
                delegate.setTypeIdAndData(x2, y2, z2, typeId, data);
                awt newBlock = this.world.o(position);
                this.world.markAndNotifyBlock(position, null, oldBlock, newBlock, flag);
            }
            this.world.capturedBlockSnapshots.clear();
            return true;
        }
        this.world.capturedBlockSnapshots.clear();
        return false;
    }

    public avj getTileEntityAt(int x2, int y2, int z2) {
        return this.world.r(new et(x2, y2, z2));
    }

    @Override
    public String getName() {
        return this.world.x.j();
    }

    @Deprecated
    public long getId() {
        return this.world.x.a();
    }

    @Override
    public UUID getUID() {
        return this.world.U().getUUID();
    }

    public String toString() {
        return "CraftWorld{name=" + this.getName() + '}';
    }

    @Override
    public long getTime() {
        long time = this.getFullTime() % 24000;
        if (time < 0) {
            time += 24000;
        }
        return time;
    }

    @Override
    public void setTime(long time) {
        long margin = (time - this.getFullTime()) % 24000;
        if (margin < 0) {
            margin += 24000;
        }
        this.setFullTime(this.getFullTime() + margin);
    }

    @Override
    public long getFullTime() {
        return this.world.S();
    }

    @Override
    public void setFullTime(long time) {
        this.world.b(time);
        for (Player p2 : this.getPlayers()) {
            CraftPlayer cp2 = (CraftPlayer)p2;
            if (cp2.getHandle().a == null) continue;
            cp2.getHandle().a.a(new ko(cp2.getHandle().l.R(), cp2.getHandle().getPlayerTime(), cp2.getHandle().l.W().b("doDaylightCycle")));
        }
    }

    @Override
    public boolean createExplosion(double x2, double y2, double z2, float power) {
        return this.createExplosion(x2, y2, z2, power, false, true);
    }

    @Override
    public boolean createExplosion(double x2, double y2, double z2, float power, boolean setFire) {
        return this.createExplosion(x2, y2, z2, power, setFire, true);
    }

    @Override
    public boolean createExplosion(double x2, double y2, double z2, float power, boolean setFire, boolean breakBlocks) {
        return !this.world.a((vg)null, (double)x2, (double)y2, (double)z2, (float)power, (boolean)setFire, (boolean)breakBlocks).wasCanceled;
    }

    @Override
    public boolean createExplosion(Location loc, float power) {
        return this.createExplosion(loc, power, false);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return this.createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
    }

    @Override
    public World.Environment getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(World.Environment env) {
        if (this.environment != env) {
            this.environment = env;
            this.world.s = aym.getProviderForDimension(this.environment.getId());
        }
    }

    @Override
    public Block getBlockAt(Location location) {
        return this.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public int getBlockTypeIdAt(Location location) {
        return this.getBlockTypeIdAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        return this.getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public Chunk getChunkAt(Location location) {
        return this.getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    @Override
    public ChunkGenerator getGenerator() {
        return this.generator;
    }

    @Override
    public List<BlockPopulator> getPopulators() {
        return this.populators;
    }

    @Override
    public Block getHighestBlockAt(int x2, int z2) {
        return this.getBlockAt(x2, this.getHighestBlockYAt(x2, z2), z2);
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        return this.getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public Biome getBiome(int x2, int z2) {
        return CraftBlock.biomeBaseToBiome(this.world.b(new et(x2, 0, z2)));
    }

    @Override
    public void setBiome(int x2, int z2, Biome bio) {
        axw chunk;
        anh bb2 = CraftBlock.biomeToBiomeBase(bio);
        if (this.world.e(new et(x2, 0, z2)) && (chunk = this.world.f(new et(x2, 0, z2))) != null) {
            byte[] biomevals = chunk.l();
            biomevals[(z2 & 15) << 4 | x2 & 15] = (byte)anh.p.a(bb2);
            chunk.e();
        }
    }

    @Override
    public double getTemperature(int x2, int z2) {
        return this.world.b(new et(x2, 0, z2)).n();
    }

    @Override
    public double getHumidity(int x2, int z2) {
        return this.world.b(new et(x2, 0, z2)).k();
    }

    @Override
    public List<Entity> getEntities() {
        ArrayList<Entity> list = new ArrayList<Entity>();
        for (Object o2 : this.world.e) {
            vg mcEnt;
            CraftEntity bukkitEntity;
            if (!(o2 instanceof vg) || (bukkitEntity = (mcEnt = (vg)o2).getBukkitEntity()) == null) continue;
            list.add(bukkitEntity);
        }
        return list;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        ArrayList<LivingEntity> list = new ArrayList<LivingEntity>();
        for (Object o2 : this.world.e) {
            vg mcEnt;
            CraftEntity bukkitEntity;
            if (!(o2 instanceof vg) || (bukkitEntity = (mcEnt = (vg)o2).getBukkitEntity()) == null || !(bukkitEntity instanceof LivingEntity)) continue;
            list.add((LivingEntity)((Object)bukkitEntity));
        }
        return list;
    }

    @Deprecated
    @Override
    public /* varargs */ <T extends Entity> Collection<T> getEntitiesByClass(Class<T> ... classes) {
        return this.getEntitiesByClasses(classes);
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz) {
        ArrayList<CraftEntity> list = new ArrayList<CraftEntity>();
        for (Object entity : this.world.e) {
            Class bukkitClass;
            CraftEntity bukkitEntity;
            if (!(entity instanceof vg) || (bukkitEntity = ((vg)entity).getBukkitEntity()) == null || !clazz.isAssignableFrom(bukkitClass = bukkitEntity.getClass())) continue;
            list.add(bukkitEntity);
        }
        return list;
    }

    @Override
    public /* varargs */ Collection<Entity> getEntitiesByClasses(Class<?> ... classes) {
        ArrayList<Entity> list = new ArrayList<Entity>();
        block0 : for (Object entity : this.world.e) {
            CraftEntity bukkitEntity;
            if (!(entity instanceof vg) || (bukkitEntity = ((vg)entity).getBukkitEntity()) == null) continue;
            Class bukkitClass = bukkitEntity.getClass();
            for (Class clazz : classes) {
                if (!clazz.isAssignableFrom(bukkitClass)) continue;
                list.add(bukkitEntity);
                continue block0;
            }
        }
        return list;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x2, double y2, double z2) {
        if (location == null || !location.getWorld().equals(this)) {
            return Collections.emptyList();
        }
        bhb bb2 = new bhb(location.getX() - x2, location.getY() - y2, location.getZ() - z2, location.getX() + x2, location.getY() + y2, location.getZ() + z2);
        List<vg> entityList = this.getHandle().a((vg)null, bb2, (Predicate<? super vg>)null);
        ArrayList<Entity> bukkitEntityList = new ArrayList<Entity>(entityList.size());
        for (vg entity : entityList) {
            bukkitEntityList.add(entity.getBukkitEntity());
        }
        return bukkitEntityList;
    }

    @Override
    public List<Player> getPlayers() {
        ArrayList<Player> list = new ArrayList<Player>(this.world.i.size());
        for (aed human : this.world.i) {
            CraftHumanEntity bukkitEntity = human.getBukkitEntity();
            if (bukkitEntity == null || !(bukkitEntity instanceof Player)) continue;
            list.add((Player)((Object)bukkitEntity));
        }
        return list;
    }

    @Override
    public void save() {
        this.server.checkSaveState();
        try {
            boolean oldSave = this.world.b;
            this.world.b = false;
            this.world.a(true, null);
            this.world.b = oldSave;
        }
        catch (amv ex2) {
            ex2.printStackTrace();
        }
    }

    @Override
    public boolean isAutoSave() {
        return !this.world.b;
    }

    @Override
    public void setAutoSave(boolean value) {
        this.world.b = !value;
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.getHandle().x.a(tz.a(difficulty.getValue()));
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().ag().ordinal());
    }

    public BlockMetadataStore getBlockMetadata() {
        return this.blockMetadata;
    }

    @Override
    public boolean hasStorm() {
        return this.world.x.o();
    }

    @Override
    public void setStorm(boolean hasStorm) {
        this.world.x.b(hasStorm);
        this.setWeatherDuration(0);
    }

    @Override
    public int getWeatherDuration() {
        return this.world.x.p();
    }

    @Override
    public void setWeatherDuration(int duration) {
        this.world.x.g(duration);
    }

    @Override
    public boolean isThundering() {
        return this.world.x.m();
    }

    @Override
    public void setThundering(boolean thundering) {
        this.world.x.a(thundering);
        this.setThunderDuration(0);
    }

    @Override
    public int getThunderDuration() {
        return this.world.x.n();
    }

    @Override
    public void setThunderDuration(int duration) {
        this.world.x.f(duration);
    }

    @Override
    public long getSeed() {
        return this.world.x.a();
    }

    @Override
    public boolean getPVP() {
        return this.world.pvpMode;
    }

    @Override
    public void setPVP(boolean pvp) {
        this.world.pvpMode = pvp;
    }

    public void playEffect(Player player, Effect effect, int data) {
        this.playEffect(player.getLocation(), effect, data, 0);
    }

    @Override
    public void playEffect(Location location, Effect effect, int data) {
        this.playEffect(location, effect, data, 64);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        this.playEffect(loc, effect, data, 64);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data, int radius) {
        if (data != null) {
            Validate.isTrue((boolean)(effect.getData() != null && effect.getData().isAssignableFrom(data.getClass())), (String)"Wrong kind of data for this effect!", (Object[])new Object[0]);
        } else {
            Validate.isTrue((boolean)(effect.getData() == null), (String)"Wrong kind of data for this effect!", (Object[])new Object[0]);
        }
        if (data != null && data.getClass().equals(MaterialData.class)) {
            MaterialData materialData = (MaterialData)data;
            Validate.isTrue((boolean)materialData.getItemType().isBlock(), (String)"Material must be block", (Object[])new Object[0]);
            this.spigot().playEffect(loc, effect, materialData.getItemType().getId(), materialData.getData(), 0.0f, 0.0f, 0.0f, 1.0f, 1, radius);
        } else {
            int dataValue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
            this.playEffect(loc, effect, dataValue, radius);
        }
    }

    @Override
    public void playEffect(Location location, Effect effect, int data, int radius) {
        this.spigot().playEffect(location, effect, data, 0, 0.0f, 0.0f, 0.0f, 1.0f, 1, radius);
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        return this.spawn(location, clazz, null, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function) throws IllegalArgumentException {
        return this.spawn(location, clazz, function, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, MaterialData data) throws IllegalArgumentException {
        Validate.notNull((Object)data, (String)"MaterialData cannot be null", (Object[])new Object[0]);
        return this.spawnFallingBlock(location, data.getItemType(), data.getData());
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, Material material, byte data) throws IllegalArgumentException {
        Validate.notNull((Object)location, (String)"Location cannot be null", (Object[])new Object[0]);
        Validate.notNull((Object)((Object)material), (String)"Material cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)material.isBlock(), (String)"Material must be a block", (Object[])new Object[0]);
        ack entity = new ack(this.world, location.getX(), location.getY(), location.getZ(), CraftMagicNumbers.getBlock(material).a(data));
        entity.T = 1;
        this.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (FallingBlock)((Object)entity.getBukkitEntity());
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, int blockId, byte blockData) throws IllegalArgumentException {
        return this.spawnFallingBlock(location, Material.getBlockMaterial(blockId), blockData);
    }

    public vg createEntity(Location location, Class<? extends Entity> clazz) throws IllegalArgumentException {
        if (location == null || clazz == null) {
            throw new IllegalArgumentException("Location or entity class cannot be null");
        }
        vg entity = null;
        double x2 = location.getX();
        double y2 = location.getY();
        double z2 = location.getZ();
        float pitch = location.getPitch();
        float yaw = location.getYaw();
        if (Boat.class.isAssignableFrom(clazz)) {
            entity = new afd(this.world, x2, y2, z2);
            entity.b(x2, y2, z2, yaw, pitch);
        } else if (FallingBlock.class.isAssignableFrom(clazz)) {
            entity = new ack(this.world, x2, y2, z2, this.world.o(new et(x2, y2, z2)));
        } else if (Projectile.class.isAssignableFrom(clazz)) {
            if (Snowball.class.isAssignableFrom(clazz)) {
                entity = new aet(this.world, x2, y2, z2);
            } else if (Egg.class.isAssignableFrom(clazz)) {
                entity = new aew(this.world, x2, y2, z2);
            } else if (Arrow.class.isAssignableFrom(clazz)) {
                if (TippedArrow.class.isAssignableFrom(clazz)) {
                    entity = new afa(this.world);
                    ((afa)entity).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
                } else {
                    entity = SpectralArrow.class.isAssignableFrom(clazz) ? new aeu(this.world) : new afa(this.world);
                }
                entity.b(x2, y2, z2, 0.0f, 0.0f);
            } else if (ThrownExpBottle.class.isAssignableFrom(clazz)) {
                entity = new aey(this.world);
                entity.b(x2, y2, z2, 0.0f, 0.0f);
            } else if (EnderPearl.class.isAssignableFrom(clazz)) {
                entity = new aex(this.world);
                entity.b(x2, y2, z2, 0.0f, 0.0f);
            } else if (ThrownPotion.class.isAssignableFrom(clazz)) {
                entity = LingeringPotion.class.isAssignableFrom(clazz) ? new aez(this.world, x2, y2, z2, CraftItemStack.asNMSCopy(new ItemStack(Material.LINGERING_POTION, 1))) : new aez(this.world, x2, y2, z2, CraftItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
            } else if (Fireball.class.isAssignableFrom(clazz)) {
                entity = SmallFireball.class.isAssignableFrom(clazz) ? new aes(this.world) : (WitherSkull.class.isAssignableFrom(clazz) ? new afb(this.world) : (DragonFireball.class.isAssignableFrom(clazz) ? new aei(this.world) : new aen(this.world)));
                entity.b(x2, y2, z2, yaw, pitch);
                Vector direction = location.getDirection().multiply(10);
                ((ael)entity).setDirection(direction.getX(), direction.getY(), direction.getZ());
            } else if (ShulkerBullet.class.isAssignableFrom(clazz)) {
                entity = new aer(this.world);
                entity.b(x2, y2, z2, yaw, pitch);
            } else if (LlamaSpit.class.isAssignableFrom(clazz)) {
                entity = new aeo(this.world);
                entity.b(x2, y2, z2, yaw, pitch);
            }
        } else if (Minecart.class.isAssignableFrom(clazz)) {
            entity = PoweredMinecart.class.isAssignableFrom(clazz) ? new afi(this.world, x2, y2, z2) : (StorageMinecart.class.isAssignableFrom(clazz) ? new aff(this.world, x2, y2, z2) : (ExplosiveMinecart.class.isAssignableFrom(clazz) ? new afm(this.world, x2, y2, z2) : (HopperMinecart.class.isAssignableFrom(clazz) ? new afj(this.world, x2, y2, z2) : (SpawnerMinecart.class.isAssignableFrom(clazz) ? new afl(this.world, x2, y2, z2) : (CommandMinecart.class.isAssignableFrom(clazz) ? new afg(this.world, x2, y2, z2) : new afk(this.world, x2, y2, z2))))));
        } else if (EnderSignal.class.isAssignableFrom(clazz)) {
            entity = new aek(this.world, x2, y2, z2);
        } else if (EnderCrystal.class.isAssignableFrom(clazz)) {
            entity = new abc(this.world);
            entity.b(x2, y2, z2, 0.0f, 0.0f);
        } else if (LivingEntity.class.isAssignableFrom(clazz)) {
            if (Chicken.class.isAssignableFrom(clazz)) {
                entity = new zw(this.world);
            } else if (Cow.class.isAssignableFrom(clazz)) {
                entity = MushroomCow.class.isAssignableFrom(clazz) ? new aaa(this.world) : new zx(this.world);
            } else if (Golem.class.isAssignableFrom(clazz)) {
                if (Snowman.class.isAssignableFrom(clazz)) {
                    entity = new aai(this.world);
                } else if (IronGolem.class.isAssignableFrom(clazz)) {
                    entity = new aak(this.world);
                } else if (Shulker.class.isAssignableFrom(clazz)) {
                    entity = new adi(this.world);
                }
            } else if (Creeper.class.isAssignableFrom(clazz)) {
                entity = new acs(this.world);
            } else if (Ghast.class.isAssignableFrom(clazz)) {
                entity = new acy(this.world);
            } else if (Pig.class.isAssignableFrom(clazz)) {
                entity = new aad(this.world);
            } else if (!Player.class.isAssignableFrom(clazz)) {
                if (Sheep.class.isAssignableFrom(clazz)) {
                    entity = new aag(this.world);
                } else if (AbstractHorse.class.isAssignableFrom(clazz)) {
                    if (ChestedHorse.class.isAssignableFrom(clazz)) {
                        if (Donkey.class.isAssignableFrom(clazz)) {
                            entity = new aap(this.world);
                        } else if (Mule.class.isAssignableFrom(clazz)) {
                            entity = new aat(this.world);
                        } else if (Llama.class.isAssignableFrom(clazz)) {
                            entity = new aas(this.world);
                        }
                    } else {
                        entity = SkeletonHorse.class.isAssignableFrom(clazz) ? new aau(this.world) : (ZombieHorse.class.isAssignableFrom(clazz) ? new aaw(this.world) : new aaq(this.world));
                    }
                } else if (Skeleton.class.isAssignableFrom(clazz)) {
                    entity = Stray.class.isAssignableFrom(clazz) ? new ado(this.world) : (WitherSkeleton.class.isAssignableFrom(clazz) ? new ads(this.world) : new adk(this.world));
                } else if (Slime.class.isAssignableFrom(clazz)) {
                    entity = MagmaCube.class.isAssignableFrom(clazz) ? new add(this.world) : new adl(this.world);
                } else if (Spider.class.isAssignableFrom(clazz)) {
                    entity = CaveSpider.class.isAssignableFrom(clazz) ? new acr(this.world) : new adn(this.world);
                } else if (Squid.class.isAssignableFrom(clazz)) {
                    entity = new aaj(this.world);
                } else if (Tameable.class.isAssignableFrom(clazz)) {
                    if (Wolf.class.isAssignableFrom(clazz)) {
                        entity = new aam(this.world);
                    } else if (Ocelot.class.isAssignableFrom(clazz)) {
                        entity = new aab(this.world);
                    } else if (Parrot.class.isAssignableFrom(clazz)) {
                        entity = new aac(this.world);
                    }
                } else if (PigZombie.class.isAssignableFrom(clazz)) {
                    entity = new adf(this.world);
                } else if (Zombie.class.isAssignableFrom(clazz)) {
                    entity = Husk.class.isAssignableFrom(clazz) ? new adb(this.world) : (ZombieVillager.class.isAssignableFrom(clazz) ? new adu(this.world) : new adt(this.world));
                } else if (Giant.class.isAssignableFrom(clazz)) {
                    entity = new acz(this.world);
                } else if (Silverfish.class.isAssignableFrom(clazz)) {
                    entity = new adj(this.world);
                } else if (Enderman.class.isAssignableFrom(clazz)) {
                    entity = new acu(this.world);
                } else if (Blaze.class.isAssignableFrom(clazz)) {
                    entity = new acq(this.world);
                } else if (Villager.class.isAssignableFrom(clazz)) {
                    entity = new ady(this.world);
                } else if (Witch.class.isAssignableFrom(clazz)) {
                    entity = new adr(this.world);
                } else if (Wither.class.isAssignableFrom(clazz)) {
                    entity = new abx(this.world);
                } else if (ComplexLivingEntity.class.isAssignableFrom(clazz)) {
                    if (EnderDragon.class.isAssignableFrom(clazz)) {
                        entity = new abd(this.world);
                    }
                } else if (Ambient.class.isAssignableFrom(clazz)) {
                    if (Bat.class.isAssignableFrom(clazz)) {
                        entity = new zt(this.world);
                    }
                } else if (Rabbit.class.isAssignableFrom(clazz)) {
                    entity = new aaf(this.world);
                } else if (Endermite.class.isAssignableFrom(clazz)) {
                    entity = new acv(this.world);
                } else if (Guardian.class.isAssignableFrom(clazz)) {
                    entity = ElderGuardian.class.isAssignableFrom(clazz) ? new act(this.world) : new ada(this.world);
                } else if (ArmorStand.class.isAssignableFrom(clazz)) {
                    entity = new abz(this.world, x2, y2, z2);
                } else if (PolarBear.class.isAssignableFrom(clazz)) {
                    entity = new aae(this.world);
                } else if (Vex.class.isAssignableFrom(clazz)) {
                    entity = new adp(this.world);
                } else if (Illager.class.isAssignableFrom(clazz)) {
                    if (Spellcaster.class.isAssignableFrom(clazz)) {
                        if (Evoker.class.isAssignableFrom(clazz)) {
                            entity = new acx(this.world);
                        } else if (Illusioner.class.isAssignableFrom(clazz)) {
                            entity = new adc(this.world);
                        }
                    } else if (Vindicator.class.isAssignableFrom(clazz)) {
                        entity = new adq(this.world);
                    }
                }
            }
            if (entity != null) {
                entity.a(x2, y2, z2, yaw, pitch);
                entity.g(yaw);
            }
        } else if (Hanging.class.isAssignableFrom(clazz)) {
            Block block = this.getBlockAt(location);
            BlockFace face = BlockFace.SELF;
            int width = 16;
            int height = 16;
            if (ItemFrame.class.isAssignableFrom(clazz)) {
                width = 12;
                height = 12;
            } else if (LeashHitch.class.isAssignableFrom(clazz)) {
                width = 9;
                height = 9;
            }
            BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
            et pos = new et((int)x2, (int)y2, (int)z2);
            for (BlockFace dir : faces) {
                aow nmsBlock = CraftMagicNumbers.getBlock(block.getRelative(dir));
                if (!nmsBlock.t().a().a() && !apw.C(nmsBlock.t())) continue;
                boolean taken = false;
                bhb bb2 = aca.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).d(), width, height);
                List<vg> list = this.world.b(null, bb2);
                Iterator<vg> it2 = list.iterator();
                while (!taken && it2.hasNext()) {
                    vg e2 = it2.next();
                    if (!(e2 instanceof aca)) continue;
                    taken = true;
                }
                if (taken) continue;
                face = dir;
                break;
            }
            if (LeashHitch.class.isAssignableFrom(clazz)) {
                entity = new acc(this.world, new et((int)x2, (int)y2, (int)z2));
                entity.k = true;
            } else {
                Preconditions.checkArgument((boolean)(face != BlockFace.SELF), (String)"Cannot spawn hanging entity for %s at %s (no free face)", (Object)clazz.getName(), (Object)location);
                fa dir = CraftBlock.blockFaceToNotch(face).d();
                if (Painting.class.isAssignableFrom(clazz)) {
                    entity = new acd(this.world, new et((int)x2, (int)y2, (int)z2), dir);
                } else if (ItemFrame.class.isAssignableFrom(clazz)) {
                    entity = new acb(this.world, new et((int)x2, (int)y2, (int)z2), dir);
                }
            }
            if (entity != null && !((aca)entity).k()) {
                throw new IllegalArgumentException("Cannot spawn hanging entity for " + clazz.getName() + " at " + location);
            }
        } else if (TNTPrimed.class.isAssignableFrom(clazz)) {
            entity = new acm(this.world, x2, y2, z2, null);
        } else if (ExperienceOrb.class.isAssignableFrom(clazz)) {
            entity = new vm(this.world, x2, y2, z2, 0);
        } else if (Weather.class.isAssignableFrom(clazz)) {
            if (LightningStrike.class.isAssignableFrom(clazz)) {
                entity = new aci(this.world, x2, y2, z2, false);
            }
        } else if (Firework.class.isAssignableFrom(clazz)) {
            entity = new aem(this.world, x2, y2, z2, aip.a);
        } else if (AreaEffectCloud.class.isAssignableFrom(clazz)) {
            entity = new ve(this.world, x2, y2, z2);
        } else if (EvokerFangs.class.isAssignableFrom(clazz)) {
            entity = new aej(this.world, x2, y2, z2, (float)Math.toRadians(yaw), 0, null);
        }
        if (entity != null) {
            return entity;
        }
        throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
    }

    public <T extends Entity> T addEntity(vg entity, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        return this.addEntity(entity, reason, null);
    }

    public <T extends Entity> T addEntity(vg entity, CreatureSpawnEvent.SpawnReason reason, Consumer<T> function) throws IllegalArgumentException {
        Preconditions.checkArgument((boolean)(entity != null), (Object)"Cannot spawn null entity");
        if (entity instanceof vq) {
            ((vq)entity).a(this.getHandle().D(new et(entity)), null);
        }
        if (function != null) {
            function.accept(entity.getBukkitEntity());
        }
        this.world.addEntity(entity, reason);
        return (T)entity.getBukkitEntity();
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        vg entity = this.createEntity(location, clazz);
        return this.addEntity(entity, reason, function);
    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x2, int z2, boolean includeBiome, boolean includeBiomeTempRain) {
        return CraftChunk.getEmptyChunkSnapshot(x2, z2, this, includeBiome, includeBiomeTempRain);
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        this.world.a(allowMonsters, allowAnimals);
    }

    @Override
    public boolean getAllowAnimals() {
        return this.world.I;
    }

    @Override
    public boolean getAllowMonsters() {
        return this.world.H;
    }

    @Override
    public int getMaxHeight() {
        return this.world.aa();
    }

    @Override
    public int getSeaLevel() {
        return this.world.M();
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return this.world.keepSpawnInMemory;
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        this.world.keepSpawnInMemory = keepLoaded;
        et chunkcoordinates = this.world.T();
        int chunkCoordX = chunkcoordinates.p() >> 4;
        int chunkCoordZ = chunkcoordinates.r() >> 4;
        for (int x2 = -12; x2 <= 12; ++x2) {
            for (int z2 = -12; z2 <= 12; ++z2) {
                if (keepLoaded) {
                    this.loadChunk(chunkCoordX + x2, chunkCoordZ + z2);
                    continue;
                }
                if (!this.isChunkLoaded(chunkCoordX + x2, chunkCoordZ + z2)) continue;
                this.unloadChunk(chunkCoordX + x2, chunkCoordZ + z2);
            }
        }
    }

    public int hashCode() {
        return this.getUID().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        CraftWorld other = (CraftWorld)obj;
        return this.getUID() == other.getUID();
    }

    @Override
    public File getWorldFolder() {
        return this.world.U().b();
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(this.server.getMessenger(), source, channel, message);
        for (Player player : this.getPlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        HashSet<String> result = new HashSet<String>();
        for (Player player : this.getPlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }
        return result;
    }

    @Override
    public WorldType getWorldType() {
        return WorldType.getByName(this.world.V().t().a());
    }

    @Override
    public boolean canGenerateStructures() {
        return this.world.V().r();
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        return this.world.ticksPerAnimalSpawns;
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        this.world.ticksPerAnimalSpawns = ticksPerAnimalSpawns;
    }

    @Override
    public long getTicksPerMonsterSpawns() {
        return this.world.ticksPerMonsterSpawns;
    }

    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        this.world.ticksPerMonsterSpawns = ticksPerMonsterSpawns;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getWorldMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getWorldMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.server.getWorldMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getWorldMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public int getMonsterSpawnLimit() {
        if (this.monsterSpawn < 0) {
            return this.server.getMonsterSpawnLimit();
        }
        return this.monsterSpawn;
    }

    @Override
    public void setMonsterSpawnLimit(int limit) {
        this.monsterSpawn = limit;
    }

    @Override
    public int getAnimalSpawnLimit() {
        if (this.animalSpawn < 0) {
            return this.server.getAnimalSpawnLimit();
        }
        return this.animalSpawn;
    }

    @Override
    public void setAnimalSpawnLimit(int limit) {
        this.animalSpawn = limit;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        if (this.waterAnimalSpawn < 0) {
            return this.server.getWaterAnimalSpawnLimit();
        }
        return this.waterAnimalSpawn;
    }

    @Override
    public void setWaterAnimalSpawnLimit(int limit) {
        this.waterAnimalSpawn = limit;
    }

    @Override
    public int getAmbientSpawnLimit() {
        if (this.ambientSpawn < 0) {
            return this.server.getAmbientSpawnLimit();
        }
        return this.ambientSpawn;
    }

    @Override
    public void setAmbientSpawnLimit(int limit) {
        this.ambientSpawn = limit;
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        this.playSound(loc, sound, SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        this.playSound(loc, sound, SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, Sound sound, SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null) {
            return;
        }
        double x2 = loc.getX();
        double y2 = loc.getY();
        double z2 = loc.getZ();
        this.getHandle().a(null, x2, y2, z2, CraftSound.getSoundEffect(CraftSound.getSound(sound)), qg.valueOf(category.name()), volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null) {
            return;
        }
        double x2 = loc.getX();
        double y2 = loc.getY();
        double z2 = loc.getZ();
        ix packet = new ix(sound, qg.valueOf(category.name()), x2, y2, z2, volume, pitch);
        this.world.u().am().a(null, x2, y2, z2, volume > 1.0f ? (double)(16.0f * volume) : 16.0, this.world.dimension, packet);
    }

    @Override
    public String getGameRuleValue(String rule) {
        return this.getHandle().W().a(rule);
    }

    @Override
    public boolean setGameRuleValue(String rule, String value) {
        if (rule == null || value == null) {
            return false;
        }
        if (!this.isGameRule(rule)) {
            return false;
        }
        this.getHandle().W().a(rule, value);
        return true;
    }

    @Override
    public String[] getGameRules() {
        return this.getHandle().W().b();
    }

    @Override
    public boolean isGameRule(String rule) {
        return this.getHandle().W().e(rule);
    }

    @Override
    public WorldBorder getWorldBorder() {
        if (this.worldBorder == null) {
            this.worldBorder = new CraftWorldBorder(this);
        }
        return this.worldBorder;
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    @Override
    public void spawnParticle(Particle particle, double x2, double y2, double z2, int count) {
        this.spawnParticle(particle, x2, y2, z2, count, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x2, double y2, double z2, int count, T data) {
        this.spawnParticle(particle, x2, y2, z2, count, 0.0, 0.0, 0.0, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double x2, double y2, double z2, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, x2, y2, z2, count, offsetX, offsetY, offsetZ, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x2, double y2, double z2, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawnParticle(particle, x2, y2, z2, count, offsetX, offsetY, offsetZ, 1.0, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(Particle particle, double x2, double y2, double z2, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, x2, y2, z2, count, offsetX, offsetY, offsetZ, extra, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x2, double y2, double z2, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("data should be " + particle.getDataType() + " got " + data.getClass());
        }
        this.getHandle().sendParticles(null, CraftParticle.toNMS(particle), true, x2, y2, z2, count, offsetX, offsetY, offsetZ, extra, CraftParticle.toData(particle, data));
    }

    public void processChunkGC() {
        ++this.chunkGCTickCount;
        if (this.chunkLoadCount >= this.server.chunkGCLoadThresh && this.server.chunkGCLoadThresh > 0) {
            this.chunkLoadCount = 0;
        } else if (this.chunkGCTickCount >= this.server.chunkGCPeriod && this.server.chunkGCPeriod > 0) {
            this.chunkGCTickCount = 0;
        } else {
            return;
        }
        on cps = this.world.r();
        for (axw chunk : cps.e.values()) {
            if (this.isChunkInUse(chunk.b, chunk.c) || cps.b.contains(amn.a(chunk.b, chunk.c))) continue;
            cps.a(chunk);
        }
    }

    @Override
    public World.Spigot spigot() {
        return this.spigot;
    }

}

