/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBanner;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBeacon;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBrewingStand;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftChest;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftCommandBlock;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftComparator;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftCreatureSpawner;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftDaylightDetector;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftDispenser;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftDropper;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftEnchantingTable;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftEndGateway;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftEnderChest;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftFlowerPot;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftFurnace;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftHopper;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftJukebox;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftNoteBlock;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftShulkerBox;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftSkull;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftStructureBlock;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
public class CraftMetaBlockState
extends CraftMetaItem
implements BlockStateMeta {
    static final CraftMetaItem.ItemMetaKey BLOCK_ENTITY_TAG = new CraftMetaItem.ItemMetaKey("BlockEntityTag");
    final Material material;
    fy blockEntityTag;

    CraftMetaBlockState(CraftMetaItem meta, Material material) {
        super(meta);
        this.material = material;
        if (!(meta instanceof CraftMetaBlockState) || ((CraftMetaBlockState)meta).material != material) {
            this.blockEntityTag = null;
            return;
        }
        CraftMetaBlockState te2 = (CraftMetaBlockState)meta;
        this.blockEntityTag = te2.blockEntityTag;
    }

    CraftMetaBlockState(fy tag, Material material) {
        super(tag);
        this.material = material;
        this.blockEntityTag = tag.b(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, 10) ? tag.p(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT) : null;
    }

    CraftMetaBlockState(Map<String, Object> map) {
        super(map);
        String matName = CraftMetaItem.SerializableMeta.getString(map, "blockMaterial", true);
        Material m2 = Material.getMaterial(matName);
        this.material = m2 != null ? m2 : Material.AIR;
    }

    @Override
    void applyToItem(fy tag) {
        super.applyToItem(tag);
        if (this.blockEntityTag != null) {
            tag.a(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, this.blockEntityTag);
        }
    }

    @Override
    void deserializeInternal(fy tag) {
        if (tag.b(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, 10)) {
            this.blockEntityTag = tag.p(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(Map<String, gn> internalTags) {
        if (this.blockEntityTag != null) {
            internalTags.put(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, this.blockEntityTag);
        }
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        builder.put((Object)"blockMaterial", (Object)this.material.name());
        return builder;
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.blockEntityTag != null) {
            hash = 61 * hash + this.blockEntityTag.hashCode();
        }
        return original != hash ? CraftMetaBlockState.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBlockState) {
            CraftMetaBlockState that = (CraftMetaBlockState)meta;
            return Objects.equal((Object)this.blockEntityTag, (Object)that.blockEntityTag);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBlockState || this.blockEntityTag == null);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.blockEntityTag == null;
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case FURNACE: 
            case CHEST: 
            case TRAPPED_CHEST: 
            case JUKEBOX: 
            case DISPENSER: 
            case DROPPER: 
            case SIGN: 
            case MOB_SPAWNER: 
            case NOTE_BLOCK: 
            case BREWING_STAND_ITEM: 
            case ENCHANTMENT_TABLE: 
            case COMMAND: 
            case COMMAND_REPEATING: 
            case COMMAND_CHAIN: 
            case BEACON: 
            case DAYLIGHT_DETECTOR: 
            case DAYLIGHT_DETECTOR_INVERTED: 
            case HOPPER: 
            case REDSTONE_COMPARATOR: 
            case FLOWER_POT_ITEM: 
            case SHIELD: 
            case STRUCTURE_BLOCK: 
            case WHITE_SHULKER_BOX: 
            case ORANGE_SHULKER_BOX: 
            case MAGENTA_SHULKER_BOX: 
            case LIGHT_BLUE_SHULKER_BOX: 
            case YELLOW_SHULKER_BOX: 
            case LIME_SHULKER_BOX: 
            case PINK_SHULKER_BOX: 
            case GRAY_SHULKER_BOX: 
            case SILVER_SHULKER_BOX: 
            case CYAN_SHULKER_BOX: 
            case PURPLE_SHULKER_BOX: 
            case BLUE_SHULKER_BOX: 
            case BROWN_SHULKER_BOX: 
            case GREEN_SHULKER_BOX: 
            case RED_SHULKER_BOX: 
            case BLACK_SHULKER_BOX: 
            case ENDER_CHEST: {
                return true;
            }
        }
        return false;
    }

    @Override
    public CraftMetaBlockState clone() {
        CraftMetaBlockState meta = (CraftMetaBlockState)super.clone();
        if (this.blockEntityTag != null) {
            meta.blockEntityTag = this.blockEntityTag.g();
        }
        return meta;
    }

    @Override
    public boolean hasBlockState() {
        return this.blockEntityTag != null;
    }

    @Override
    public BlockState getBlockState() {
        if (this.blockEntityTag != null) {
            switch (this.material) {
                case SHIELD: {
                    this.blockEntityTag.a("id", "banner");
                    break;
                }
                case WHITE_SHULKER_BOX: 
                case ORANGE_SHULKER_BOX: 
                case MAGENTA_SHULKER_BOX: 
                case LIGHT_BLUE_SHULKER_BOX: 
                case YELLOW_SHULKER_BOX: 
                case LIME_SHULKER_BOX: 
                case PINK_SHULKER_BOX: 
                case GRAY_SHULKER_BOX: 
                case SILVER_SHULKER_BOX: 
                case CYAN_SHULKER_BOX: 
                case PURPLE_SHULKER_BOX: 
                case BLUE_SHULKER_BOX: 
                case BROWN_SHULKER_BOX: 
                case GREEN_SHULKER_BOX: 
                case RED_SHULKER_BOX: 
                case BLACK_SHULKER_BOX: {
                    this.blockEntityTag.a("id", "shulker_box");
                }
            }
        }
        avj te2 = this.blockEntityTag == null ? null : avj.a(null, this.blockEntityTag);
        switch (this.material) {
            case SIGN: 
            case SIGN_POST: 
            case WALL_SIGN: {
                if (te2 == null) {
                    te2 = new awc();
                }
                return new CraftSign(this.material, (awc)te2);
            }
            case CHEST: 
            case TRAPPED_CHEST: {
                if (te2 == null) {
                    te2 = new avl();
                }
                return new CraftChest(this.material, (avl)te2);
            }
            case FURNACE: 
            case BURNING_FURNACE: {
                if (te2 == null) {
                    te2 = new avu();
                }
                return new CraftFurnace(this.material, (avu)te2);
            }
            case DISPENSER: {
                if (te2 == null) {
                    te2 = new avp();
                }
                return new CraftDispenser(this.material, (avp)te2);
            }
            case DROPPER: {
                if (te2 == null) {
                    te2 = new avq();
                }
                return new CraftDropper(this.material, (avq)te2);
            }
            case END_GATEWAY: {
                if (te2 == null) {
                    te2 = new awf();
                }
                return new CraftEndGateway(this.material, (awf)te2);
            }
            case HOPPER: {
                if (te2 == null) {
                    te2 = new avw();
                }
                return new CraftHopper(this.material, (avw)te2);
            }
            case MOB_SPAWNER: {
                if (te2 == null) {
                    te2 = new avy();
                }
                return new CraftCreatureSpawner(this.material, (avy)te2);
            }
            case NOTE_BLOCK: {
                if (te2 == null) {
                    te2 = new avz();
                }
                return new CraftNoteBlock(this.material, (avz)te2);
            }
            case JUKEBOX: {
                if (te2 == null) {
                    te2 = new arp.a();
                }
                return new CraftJukebox(this.material, (arp.a)te2);
            }
            case BREWING_STAND_ITEM: {
                if (te2 == null) {
                    te2 = new avk();
                }
                return new CraftBrewingStand(this.material, (avk)te2);
            }
            case SKULL: {
                if (te2 == null) {
                    te2 = new awd();
                }
                return new CraftSkull(this.material, (awd)te2);
            }
            case COMMAND: 
            case COMMAND_REPEATING: 
            case COMMAND_CHAIN: {
                if (te2 == null) {
                    te2 = new avm();
                }
                return new CraftCommandBlock(this.material, (avm)te2);
            }
            case BEACON: {
                if (te2 == null) {
                    te2 = new avh();
                }
                return new CraftBeacon(this.material, (avh)te2);
            }
            case SHIELD: 
            case BANNER: 
            case WALL_BANNER: 
            case STANDING_BANNER: {
                if (te2 == null) {
                    te2 = new avf();
                }
                return new CraftBanner(this.material, (avf)te2);
            }
            case FLOWER_POT_ITEM: {
                if (te2 == null) {
                    te2 = new avt();
                }
                return new CraftFlowerPot(this.material, (avt)te2);
            }
            case STRUCTURE_BLOCK: {
                if (te2 == null) {
                    te2 = new awe();
                }
                return new CraftStructureBlock(this.material, (awe)te2);
            }
            case WHITE_SHULKER_BOX: 
            case ORANGE_SHULKER_BOX: 
            case MAGENTA_SHULKER_BOX: 
            case LIGHT_BLUE_SHULKER_BOX: 
            case YELLOW_SHULKER_BOX: 
            case LIME_SHULKER_BOX: 
            case PINK_SHULKER_BOX: 
            case GRAY_SHULKER_BOX: 
            case SILVER_SHULKER_BOX: 
            case CYAN_SHULKER_BOX: 
            case PURPLE_SHULKER_BOX: 
            case BLUE_SHULKER_BOX: 
            case BROWN_SHULKER_BOX: 
            case GREEN_SHULKER_BOX: 
            case RED_SHULKER_BOX: 
            case BLACK_SHULKER_BOX: {
                if (te2 == null) {
                    te2 = new awb();
                }
                return new CraftShulkerBox(this.material, (awb)te2);
            }
            case ENCHANTMENT_TABLE: {
                if (te2 == null) {
                    te2 = new avr();
                }
                return new CraftEnchantingTable(this.material, (avr)te2);
            }
            case ENDER_CHEST: {
                if (te2 == null) {
                    te2 = new avs();
                }
                return new CraftEnderChest(this.material, (avs)te2);
            }
            case DAYLIGHT_DETECTOR: 
            case DAYLIGHT_DETECTOR_INVERTED: {
                if (te2 == null) {
                    te2 = new avo();
                }
                return new CraftDaylightDetector(this.material, (avo)te2);
            }
            case REDSTONE_COMPARATOR: {
                if (te2 == null) {
                    te2 = new avn();
                }
                return new CraftComparator(this.material, (avn)te2);
            }
        }
        throw new IllegalStateException("Missing blockState for " + (Object)((Object)this.material));
    }

    @Override
    public void setBlockState(BlockState blockState) {
        boolean valid;
        Validate.notNull((Object)blockState, (String)"blockState must not be null", (Object[])new Object[0]);
        switch (this.material) {
            case SIGN: 
            case SIGN_POST: 
            case WALL_SIGN: {
                valid = blockState instanceof CraftSign;
                break;
            }
            case CHEST: 
            case TRAPPED_CHEST: {
                valid = blockState instanceof CraftChest;
                break;
            }
            case FURNACE: 
            case BURNING_FURNACE: {
                valid = blockState instanceof CraftFurnace;
                break;
            }
            case DISPENSER: {
                valid = blockState instanceof CraftDispenser;
                break;
            }
            case DROPPER: {
                valid = blockState instanceof CraftDropper;
                break;
            }
            case END_GATEWAY: {
                valid = blockState instanceof CraftEndGateway;
                break;
            }
            case HOPPER: {
                valid = blockState instanceof CraftHopper;
                break;
            }
            case MOB_SPAWNER: {
                valid = blockState instanceof CraftCreatureSpawner;
                break;
            }
            case NOTE_BLOCK: {
                valid = blockState instanceof CraftNoteBlock;
                break;
            }
            case JUKEBOX: {
                valid = blockState instanceof CraftJukebox;
                break;
            }
            case BREWING_STAND_ITEM: {
                valid = blockState instanceof CraftBrewingStand;
                break;
            }
            case SKULL: {
                valid = blockState instanceof CraftSkull;
                break;
            }
            case COMMAND: 
            case COMMAND_REPEATING: 
            case COMMAND_CHAIN: {
                valid = blockState instanceof CraftCommandBlock;
                break;
            }
            case BEACON: {
                valid = blockState instanceof CraftBeacon;
                break;
            }
            case SHIELD: 
            case BANNER: 
            case WALL_BANNER: 
            case STANDING_BANNER: {
                valid = blockState instanceof CraftBanner;
                break;
            }
            case FLOWER_POT_ITEM: {
                valid = blockState instanceof CraftFlowerPot;
                break;
            }
            case STRUCTURE_BLOCK: {
                valid = blockState instanceof CraftStructureBlock;
                break;
            }
            case WHITE_SHULKER_BOX: 
            case ORANGE_SHULKER_BOX: 
            case MAGENTA_SHULKER_BOX: 
            case LIGHT_BLUE_SHULKER_BOX: 
            case YELLOW_SHULKER_BOX: 
            case LIME_SHULKER_BOX: 
            case PINK_SHULKER_BOX: 
            case GRAY_SHULKER_BOX: 
            case SILVER_SHULKER_BOX: 
            case CYAN_SHULKER_BOX: 
            case PURPLE_SHULKER_BOX: 
            case BLUE_SHULKER_BOX: 
            case BROWN_SHULKER_BOX: 
            case GREEN_SHULKER_BOX: 
            case RED_SHULKER_BOX: 
            case BLACK_SHULKER_BOX: {
                valid = blockState instanceof CraftShulkerBox;
                break;
            }
            case ENCHANTMENT_TABLE: {
                valid = blockState instanceof CraftEnchantingTable;
                break;
            }
            case ENDER_CHEST: {
                valid = blockState instanceof CraftEnderChest;
                break;
            }
            case DAYLIGHT_DETECTOR: 
            case DAYLIGHT_DETECTOR_INVERTED: {
                valid = blockState instanceof CraftDaylightDetector;
                break;
            }
            case REDSTONE_COMPARATOR: {
                valid = blockState instanceof CraftComparator;
                break;
            }
            default: {
                valid = false;
            }
        }
        Validate.isTrue((boolean)valid, (String)("Invalid blockState for " + (Object)((Object)this.material)), (Object[])new Object[0]);
        this.blockEntityTag = ((CraftBlockEntityState)blockState).getSnapshotNBT();
    }

}

