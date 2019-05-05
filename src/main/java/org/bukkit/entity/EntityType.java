/*
 * Akarin Forge
 */
package org.bukkit.entity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Boat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.ComplexEntityPart;
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
import org.bukkit.entity.Evoker;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Mule;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
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
import org.bukkit.entity.Spider;
import org.bukkit.entity.SplashPotion;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Stray;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownExpBottle;
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
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;

import io.akarin.forge.entity.CraftCustomEntity;

public enum EntityType {
    DROPPED_ITEM("item", Item.class, 1, false),
    EXPERIENCE_ORB("xp_orb", ExperienceOrb.class, 2),
    AREA_EFFECT_CLOUD("area_effect_cloud", AreaEffectCloud.class, 3),
    ELDER_GUARDIAN("elder_guardian", ElderGuardian.class, 4),
    WITHER_SKELETON("wither_skeleton", WitherSkeleton.class, 5),
    STRAY("stray", Stray.class, 6),
    EGG("egg", Egg.class, 7),
    LEASH_HITCH("leash_knot", LeashHitch.class, 8),
    PAINTING("painting", Painting.class, 9),
    ARROW("arrow", Arrow.class, 10),
    SNOWBALL("snowball", Snowball.class, 11),
    FIREBALL("fireball", LargeFireball.class, 12),
    SMALL_FIREBALL("small_fireball", SmallFireball.class, 13),
    ENDER_PEARL("ender_pearl", EnderPearl.class, 14),
    ENDER_SIGNAL("eye_of_ender_signal", EnderSignal.class, 15),
    SPLASH_POTION("potion", SplashPotion.class, 16, false),
    THROWN_EXP_BOTTLE("xp_bottle", ThrownExpBottle.class, 17),
    ITEM_FRAME("item_frame", ItemFrame.class, 18),
    WITHER_SKULL("wither_skull", WitherSkull.class, 19),
    PRIMED_TNT("tnt", TNTPrimed.class, 20),
    FALLING_BLOCK("falling_block", FallingBlock.class, 21, false),
    FIREWORK("fireworks_rocket", Firework.class, 22, false),
    HUSK("husk", Husk.class, 23),
    SPECTRAL_ARROW("spectral_arrow", SpectralArrow.class, 24),
    SHULKER_BULLET("shulker_bullet", ShulkerBullet.class, 25),
    DRAGON_FIREBALL("dragon_fireball", DragonFireball.class, 26),
    ZOMBIE_VILLAGER("zombie_villager", ZombieVillager.class, 27),
    SKELETON_HORSE("skeleton_horse", SkeletonHorse.class, 28),
    ZOMBIE_HORSE("zombie_horse", ZombieHorse.class, 29),
    ARMOR_STAND("armor_stand", ArmorStand.class, 30),
    DONKEY("donkey", Donkey.class, 31),
    MULE("mule", Mule.class, 32),
    EVOKER_FANGS("evocation_fangs", EvokerFangs.class, 33),
    EVOKER("evocation_illager", Evoker.class, 34),
    VEX("vex", Vex.class, 35),
    VINDICATOR("vindication_illager", Vindicator.class, 36),
    ILLUSIONER("illusion_illager", Illusioner.class, 37),
    MINECART_COMMAND("commandblock_minecart", CommandMinecart.class, 40),
    BOAT("boat", Boat.class, 41),
    MINECART("minecart", RideableMinecart.class, 42),
    MINECART_CHEST("chest_minecart", StorageMinecart.class, 43),
    MINECART_FURNACE("furnace_minecart", PoweredMinecart.class, 44),
    MINECART_TNT("tnt_minecart", ExplosiveMinecart.class, 45),
    MINECART_HOPPER("hopper_minecart", HopperMinecart.class, 46),
    MINECART_MOB_SPAWNER("spawner_minecart", SpawnerMinecart.class, 47),
    CREEPER("creeper", Creeper.class, 50),
    SKELETON("skeleton", Skeleton.class, 51),
    SPIDER("spider", Spider.class, 52),
    GIANT("giant", Giant.class, 53),
    ZOMBIE("zombie", Zombie.class, 54),
    SLIME("slime", Slime.class, 55),
    GHAST("ghast", Ghast.class, 56),
    PIG_ZOMBIE("zombie_pigman", PigZombie.class, 57),
    ENDERMAN("enderman", Enderman.class, 58),
    CAVE_SPIDER("cave_spider", CaveSpider.class, 59),
    SILVERFISH("silverfish", Silverfish.class, 60),
    BLAZE("blaze", Blaze.class, 61),
    MAGMA_CUBE("magma_cube", MagmaCube.class, 62),
    ENDER_DRAGON("ender_dragon", EnderDragon.class, 63),
    WITHER("wither", Wither.class, 64),
    BAT("bat", Bat.class, 65),
    WITCH("witch", Witch.class, 66),
    ENDERMITE("endermite", Endermite.class, 67),
    GUARDIAN("guardian", Guardian.class, 68),
    SHULKER("shulker", Shulker.class, 69),
    PIG("pig", Pig.class, 90),
    SHEEP("sheep", Sheep.class, 91),
    COW("cow", Cow.class, 92),
    CHICKEN("chicken", Chicken.class, 93),
    SQUID("squid", Squid.class, 94),
    WOLF("wolf", Wolf.class, 95),
    MUSHROOM_COW("mooshroom", MushroomCow.class, 96),
    SNOWMAN("snowman", Snowman.class, 97),
    OCELOT("ocelot", Ocelot.class, 98),
    IRON_GOLEM("villager_golem", IronGolem.class, 99),
    HORSE("horse", Horse.class, 100),
    RABBIT("rabbit", Rabbit.class, 101),
    POLAR_BEAR("polar_bear", PolarBear.class, 102),
    LLAMA("llama", Llama.class, 103),
    LLAMA_SPIT("llama_spit", LlamaSpit.class, 104),
    PARROT("parrot", Parrot.class, 105),
    VILLAGER("villager", Villager.class, 120),
    ENDER_CRYSTAL("ender_crystal", EnderCrystal.class, 200),
    LINGERING_POTION(null, LingeringPotion.class, -1, false),
    FISHING_HOOK(null, FishHook.class, -1, false),
    LIGHTNING(null, LightningStrike.class, -1, false),
    WEATHER(null, Weather.class, -1, false),
    PLAYER(null, Player.class, -1, false),
    COMPLEX_PART(null, ComplexEntityPart.class, -1, false),
    TIPPED_ARROW(null, TippedArrow.class, -1),
    UNKNOWN(null, null, -1, false),
    MOD_CUSTOM("mod_custom", CraftCustomEntity.class, -1, false);
    
    private String name;
    private Class<? extends Entity> clazz;
    private short typeId;
    private boolean independent;
    private boolean living;
    private static final Map<String, EntityType> NAME_MAP;
    private static final Map<Short, EntityType> ID_MAP;

    private EntityType(String name, Class<? extends Entity> clazz, int typeId) {
        this(name, clazz, typeId, true);
    }

    private EntityType(String name, Class<? extends Entity> clazz, int typeId, boolean independent) {
        this.name = name;
        this.clazz = clazz;
        this.typeId = (short)typeId;
        this.independent = independent;
        if (clazz != null) {
            this.living = LivingEntity.class.isAssignableFrom(clazz);
        }
    }

    @Deprecated
    public String getName() {
        return this.name;
    }

    public Class<? extends Entity> getEntityClass() {
        return this.clazz;
    }

    @Deprecated
    public short getTypeId() {
        return this.typeId;
    }

    @Deprecated
    public static EntityType fromName(String name) {
        if (name == null) {
            return null;
        }
        return NAME_MAP.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Deprecated
    public static EntityType fromId(int id2) {
        if (id2 > 32767) {
            return null;
        }
        return ID_MAP.get((short)id2);
    }

    public boolean isSpawnable() {
        return this.independent;
    }

    public boolean isAlive() {
        return this.living;
    }

    static {
        NAME_MAP = new HashMap<String, EntityType>();
        ID_MAP = new HashMap<Short, EntityType>();
        for (EntityType type : EntityType.values()) {
            if (type.name != null) {
                NAME_MAP.put(type.name.toLowerCase(Locale.ENGLISH), type);
            }
            if (type.typeId <= 0) continue;
            ID_MAP.put(type.typeId, type);
        }
    }
}

