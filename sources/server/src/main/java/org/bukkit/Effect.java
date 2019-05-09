/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package org.bukkit;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;

public enum Effect {
    CLICK2(1000, Type.SOUND),
    CLICK1(1001, Type.SOUND),
    BOW_FIRE(1002, Type.SOUND),
    DOOR_TOGGLE(1006, Type.SOUND),
    IRON_DOOR_TOGGLE(1005, Type.SOUND),
    TRAPDOOR_TOGGLE(1007, Type.SOUND),
    IRON_TRAPDOOR_TOGGLE(1037, Type.SOUND),
    FENCE_GATE_TOGGLE(1008, Type.SOUND),
    DOOR_CLOSE(1012, Type.SOUND),
    IRON_DOOR_CLOSE(1011, Type.SOUND),
    TRAPDOOR_CLOSE(1013, Type.SOUND),
    IRON_TRAPDOOR_CLOSE(1036, Type.SOUND),
    FENCE_GATE_CLOSE(1014, Type.SOUND),
    EXTINGUISH(1009, Type.SOUND),
    RECORD_PLAY(1010, Type.SOUND, Material.class),
    GHAST_SHRIEK(1015, Type.SOUND),
    GHAST_SHOOT(1016, Type.SOUND),
    BLAZE_SHOOT(1018, Type.SOUND),
    ZOMBIE_CHEW_WOODEN_DOOR(1019, Type.SOUND),
    ZOMBIE_CHEW_IRON_DOOR(1020, Type.SOUND),
    ZOMBIE_DESTROY_DOOR(1021, Type.SOUND),
    SMOKE(2000, Type.VISUAL, BlockFace.class),
    STEP_SOUND(2001, Type.SOUND, Material.class),
    POTION_BREAK(2002, Type.VISUAL, Potion.class),
    ENDER_SIGNAL(2003, Type.VISUAL),
    MOBSPAWNER_FLAMES(2004, Type.VISUAL),
    BREWING_STAND_BREW(1035, Type.SOUND),
    CHORUS_FLOWER_GROW(1033, Type.SOUND),
    CHORUS_FLOWER_DEATH(1034, Type.SOUND),
    PORTAL_TRAVEL(1032, Type.SOUND),
    ENDEREYE_LAUNCH(1003, Type.SOUND),
    FIREWORK_SHOOT(1004, Type.SOUND),
    VILLAGER_PLANT_GROW(2005, Type.VISUAL, Integer.class),
    DRAGON_BREATH(2006, Type.VISUAL),
    ANVIL_BREAK(1029, Type.SOUND),
    ANVIL_USE(1030, Type.SOUND),
    ANVIL_LAND(1031, Type.SOUND),
    ENDERDRAGON_SHOOT(1017, Type.SOUND),
    WITHER_BREAK_BLOCK(1022, Type.SOUND),
    WITHER_SHOOT(1024, Type.SOUND),
    ZOMBIE_INFECT(1026, Type.SOUND),
    ZOMBIE_CONVERTED_VILLAGER(1027, Type.SOUND),
    BAT_TAKEOFF(1025, Type.SOUND),
    END_GATEWAY_SPAWN(3000, Type.VISUAL),
    ENDERDRAGON_GROWL(3001, Type.SOUND),
    FIREWORKS_SPARK("fireworksSpark", Type.PARTICLE),
    CRIT("crit", Type.PARTICLE),
    MAGIC_CRIT("magicCrit", Type.PARTICLE),
    POTION_SWIRL("mobSpell", Type.PARTICLE),
    POTION_SWIRL_TRANSPARENT("mobSpellAmbient", Type.PARTICLE),
    SPELL("spell", Type.PARTICLE),
    INSTANT_SPELL("instantSpell", Type.PARTICLE),
    WITCH_MAGIC("witchMagic", Type.PARTICLE),
    NOTE("note", Type.PARTICLE),
    PORTAL("portal", Type.PARTICLE),
    FLYING_GLYPH("enchantmenttable", Type.PARTICLE),
    FLAME("flame", Type.PARTICLE),
    LAVA_POP("lava", Type.PARTICLE),
    FOOTSTEP("footstep", Type.PARTICLE),
    SPLASH("splash", Type.PARTICLE),
    PARTICLE_SMOKE("smoke", Type.PARTICLE),
    EXPLOSION_HUGE("hugeexplosion", Type.PARTICLE),
    EXPLOSION_LARGE("largeexplode", Type.PARTICLE),
    EXPLOSION("explode", Type.PARTICLE),
    VOID_FOG("depthsuspend", Type.PARTICLE),
    SMALL_SMOKE("townaura", Type.PARTICLE),
    CLOUD("cloud", Type.PARTICLE),
    COLOURED_DUST("reddust", Type.PARTICLE),
    SNOWBALL_BREAK("snowballpoof", Type.PARTICLE),
    WATERDRIP("dripWater", Type.PARTICLE),
    LAVADRIP("dripLava", Type.PARTICLE),
    SNOW_SHOVEL("snowshovel", Type.PARTICLE),
    SLIME("slime", Type.PARTICLE),
    HEART("heart", Type.PARTICLE),
    VILLAGER_THUNDERCLOUD("angryVillager", Type.PARTICLE),
    HAPPY_VILLAGER("happyVillager", Type.PARTICLE),
    LARGE_SMOKE("largesmoke", Type.PARTICLE),
    ITEM_BREAK("iconcrack", Type.PARTICLE, Material.class),
    TILE_BREAK("blockcrack", Type.PARTICLE, MaterialData.class),
    TILE_DUST("blockdust", Type.PARTICLE, MaterialData.class);
    
    private final int id;
    private final Type type;
    private final Class<?> data;
    private static final Map<Integer, Effect> BY_ID;
    private static final Map<String, Effect> BY_NAME;
    private final String particleName;

    private Effect(int id2, Type type) {
        this(id2, type, null);
    }

    private Effect(int id2, Type type, Class<?> data) {
        this.id = id2;
        this.type = type;
        this.data = data;
        this.particleName = null;
    }

    private Effect(String particleName, Type type, Class<?> data) {
        this.particleName = particleName;
        this.type = type;
        this.id = 0;
        this.data = data;
    }

    private Effect(String particleName, Type type) {
        this.particleName = particleName;
        this.type = type;
        this.id = 0;
        this.data = null;
    }

    @Deprecated
    public int getId() {
        return this.id;
    }

    @Deprecated
    public String getName() {
        return this.particleName;
    }

    public Type getType() {
        return this.type;
    }

    public Class<?> getData() {
        return this.data;
    }

    @Deprecated
    public static Effect getById(int id2) {
        return BY_ID.get(id2);
    }

    @Deprecated
    public static Effect getByName(String name) {
        return BY_NAME.get(name);
    }

    static {
        BY_ID = Maps.newHashMap();
        BY_NAME = Maps.newHashMap();
        for (Effect effect : Effect.values()) {
            if (effect.type == Type.PARTICLE) continue;
            BY_ID.put(effect.id, effect);
        }
        for (Effect effect : Effect.values()) {
            if (effect.type != Type.PARTICLE) continue;
            BY_NAME.put(effect.particleName, effect);
        }
    }

    public static enum Type {
        SOUND,
        VISUAL,
        PARTICLE;
        

        private Type() {
        }
    }

}

