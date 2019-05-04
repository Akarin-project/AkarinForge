/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import catserver.server.inventory.CraftCustomContainer;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBanner;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBeacon;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBed;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockState;
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
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.metadata.BlockMetadataStore;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;

public class CraftBlock
implements Block {
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;

    public CraftBlock(CraftChunk chunk, int x2, int y2, int z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
        this.chunk = chunk;
    }

    private aow getNMSBlock() {
        return CraftMagicNumbers.getBlock(this);
    }

    private static aow getNMSBlock(int type) {
        return CraftMagicNumbers.getBlock(type);
    }

    @Override
    public World getWorld() {
        return this.chunk.getWorld();
    }

    @Override
    public Location getLocation() {
        return new Location(this.getWorld(), this.x, this.y, this.z);
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(this.getWorld());
            loc.setX(this.x);
            loc.setY(this.y);
            loc.setZ(this.z);
            loc.setYaw(0.0f);
            loc.setPitch(0.0f);
        }
        return loc;
    }

    public BlockVector getVector() {
        return new BlockVector(this.x, this.y, this.z);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public Chunk getChunk() {
        return this.chunk;
    }

    @Override
    public void setData(byte data) {
        this.setData(data, 3);
    }

    @Override
    public void setData(byte data, boolean applyPhysics) {
        if (applyPhysics) {
            this.setData(data, 3);
        } else {
            this.setData(data, 2);
        }
    }

    private void setData(byte data, int flag) {
        amu world = this.chunk.getHandle().q();
        et position = new et(this.x, this.y, this.z);
        awt blockData = world.o(position);
        world.a(position, blockData.u().a(data), flag);
    }

    private awt getData0() {
        return this.chunk.getHandle().a(new et(this.x, this.y, this.z));
    }

    @Override
    public byte getData() {
        awt blockData = this.chunk.getHandle().a(new et(this.x, this.y, this.z));
        return (byte)blockData.u().e(blockData);
    }

    @Override
    public void setType(Material type) {
        this.setType(type, true);
    }

    @Override
    public void setType(Material type, boolean applyPhysics) {
        this.setTypeId(type.getId(), applyPhysics);
    }

    @Override
    public boolean setTypeId(int type) {
        return this.setTypeId(type, true);
    }

    @Override
    public boolean setTypeId(int type, boolean applyPhysics) {
        aow block = CraftBlock.getNMSBlock(type);
        return this.setTypeIdAndData(type, (byte)block.e(block.t()), applyPhysics);
    }

    @Override
    public boolean setTypeIdAndData(int type, byte data, boolean applyPhysics) {
        awt blockData = CraftBlock.getNMSBlock(type).a(data);
        et position = new et(this.x, this.y, this.z);
        if (type != 0 && blockData.u() instanceof aoq && type != this.getTypeId()) {
            this.chunk.getHandle().q().a(position, aox.a.t(), 0);
        }
        if (applyPhysics) {
            return this.chunk.getHandle().q().a(position, blockData, 3);
        }
        awt old = this.chunk.getHandle().a(position);
        boolean success = this.chunk.getHandle().q().a(position, blockData, 18);
        if (success) {
            this.chunk.getHandle().q().a(position, old, blockData, 3);
        }
        return success;
    }

    @Override
    public Material getType() {
        return Material.getBlockMaterial(this.getTypeId());
    }

    @Deprecated
    @Override
    public int getTypeId() {
        return CraftMagicNumbers.getId(this.chunk.getHandle().a(new et(this.x, this.y, this.z)).u());
    }

    @Override
    public byte getLightLevel() {
        return (byte)this.chunk.getHandle().q().k(new et(this.x, this.y, this.z));
    }

    @Override
    public byte getLightFromSky() {
        return (byte)this.chunk.getHandle().q().b(ana.a, new et(this.x, this.y, this.z));
    }

    @Override
    public byte getLightFromBlocks() {
        return (byte)this.chunk.getHandle().q().b(ana.b, new et(this.x, this.y, this.z));
    }

    public Block getFace(BlockFace face) {
        return this.getRelative(face, 1);
    }

    public Block getFace(BlockFace face, int distance) {
        return this.getRelative(face, distance);
    }

    @Override
    public Block getRelative(int modX, int modY, int modZ) {
        return this.getWorld().getBlockAt(this.getX() + modX, this.getY() + modY, this.getZ() + modZ);
    }

    @Override
    public Block getRelative(BlockFace face) {
        return this.getRelative(face, 1);
    }

    @Override
    public Block getRelative(BlockFace face, int distance) {
        return this.getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    @Override
    public BlockFace getFace(Block block) {
        BlockFace[] values;
        for (BlockFace face : values = BlockFace.values()) {
            if (this.getX() + face.getModX() != block.getX() || this.getY() + face.getModY() != block.getY() || this.getZ() + face.getModZ() != block.getZ()) continue;
            return face;
        }
        return null;
    }

    public String toString() {
        return "CraftBlock{chunk=" + this.chunk + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",type=" + (Object)((Object)this.getType()) + ",data=" + this.getData() + '}';
    }

    public static BlockFace notchToBlockFace(fa notch) {
        if (notch == null) {
            return BlockFace.SELF;
        }
        switch (notch) {
            case a: {
                return BlockFace.DOWN;
            }
            case b: {
                return BlockFace.UP;
            }
            case c: {
                return BlockFace.NORTH;
            }
            case d: {
                return BlockFace.SOUTH;
            }
            case e: {
                return BlockFace.WEST;
            }
            case f: {
                return BlockFace.EAST;
            }
        }
        return BlockFace.SELF;
    }

    public static fa blockFaceToNotch(BlockFace face) {
        switch (face) {
            case DOWN: {
                return fa.a;
            }
            case UP: {
                return fa.b;
            }
            case NORTH: {
                return fa.c;
            }
            case SOUTH: {
                return fa.d;
            }
            case WEST: {
                return fa.e;
            }
            case EAST: {
                return fa.f;
            }
        }
        return null;
    }

    @Override
    public BlockState getState() {
        Material material = this.getType();
        if (material == null) {
            avj tileEntity = this.chunk.getCraftWorld().getTileEntityAt(this.x, this.y, this.z);
            if (tileEntity != null) {
                if (tileEntity instanceof tv) {
                    return new CraftCustomContainer(this);
                }
                return new CraftBlockEntityState(this, tileEntity.getClass());
            }
            return new CraftBlockState(this);
        }
        switch (material) {
            case SIGN: 
            case SIGN_POST: 
            case WALL_SIGN: {
                return new CraftSign(this);
            }
            case CHEST: 
            case TRAPPED_CHEST: {
                return new CraftChest(this);
            }
            case BURNING_FURNACE: 
            case FURNACE: {
                return new CraftFurnace(this);
            }
            case DISPENSER: {
                return new CraftDispenser(this);
            }
            case DROPPER: {
                return new CraftDropper(this);
            }
            case END_GATEWAY: {
                return new CraftEndGateway(this);
            }
            case HOPPER: {
                return new CraftHopper(this);
            }
            case MOB_SPAWNER: {
                return new CraftCreatureSpawner(this);
            }
            case NOTE_BLOCK: {
                return new CraftNoteBlock(this);
            }
            case JUKEBOX: {
                return new CraftJukebox(this);
            }
            case BREWING_STAND: {
                return new CraftBrewingStand(this);
            }
            case SKULL: {
                return new CraftSkull(this);
            }
            case COMMAND: 
            case COMMAND_CHAIN: 
            case COMMAND_REPEATING: {
                return new CraftCommandBlock(this);
            }
            case BEACON: {
                return new CraftBeacon(this);
            }
            case BANNER: 
            case WALL_BANNER: 
            case STANDING_BANNER: {
                return new CraftBanner(this);
            }
            case FLOWER_POT: {
                return new CraftFlowerPot(this);
            }
            case STRUCTURE_BLOCK: {
                return new CraftStructureBlock(this);
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
                return new CraftShulkerBox(this);
            }
            case ENCHANTMENT_TABLE: {
                return new CraftEnchantingTable(this);
            }
            case ENDER_CHEST: {
                return new CraftEnderChest(this);
            }
            case DAYLIGHT_DETECTOR: 
            case DAYLIGHT_DETECTOR_INVERTED: {
                return new CraftDaylightDetector(this);
            }
            case REDSTONE_COMPARATOR_OFF: 
            case REDSTONE_COMPARATOR_ON: {
                return new CraftComparator(this);
            }
            case BED_BLOCK: {
                return new CraftBed(this);
            }
        }
        avj tileEntity = this.chunk.getCraftWorld().getTileEntityAt(this.x, this.y, this.z);
        if (tileEntity != null) {
            if (tileEntity instanceof tv) {
                return new CraftCustomContainer(this);
            }
            return new CraftBlockEntityState(this, tileEntity.getClass());
        }
        return new CraftBlockState(this);
    }

    @Override
    public Biome getBiome() {
        return this.getWorld().getBiome(this.x, this.z);
    }

    @Override
    public void setBiome(Biome bio) {
        this.getWorld().setBiome(this.x, this.z, bio);
    }

    public static Biome biomeBaseToBiome(anh base) {
        if (base == null) {
            return null;
        }
        return Biome.valueOf(anh.p.b(base).a().toUpperCase(Locale.ENGLISH));
    }

    public static anh biomeToBiomeBase(Biome bio) {
        if (bio == null) {
            return null;
        }
        return anh.p.c(new nf(bio.name().toLowerCase(Locale.ENGLISH)));
    }

    @Override
    public double getTemperature() {
        return this.getWorld().getTemperature(this.x, this.z);
    }

    @Override
    public double getHumidity() {
        return this.getWorld().getHumidity(this.x, this.z);
    }

    @Override
    public boolean isBlockPowered() {
        return this.chunk.getHandle().q().x(new et(this.x, this.y, this.z)) > 0;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return this.chunk.getHandle().q().y(new et(this.x, this.y, this.z));
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof CraftBlock)) {
            return false;
        }
        CraftBlock other = (CraftBlock)o2;
        return this.x == other.x && this.y == other.y && this.z == other.z && this.getWorld().equals(other.getWorld());
    }

    public int hashCode() {
        return this.y << 24 ^ this.x ^ this.z ^ this.getWorld().hashCode();
    }

    @Override
    public boolean isBlockFacePowered(BlockFace face) {
        return this.chunk.getHandle().q().b(new et(this.x, this.y, this.z), CraftBlock.blockFaceToNotch(face));
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        int power = this.chunk.getHandle().q().c(new et(this.x, this.y, this.z), CraftBlock.blockFaceToNotch(face));
        Block relative = this.getRelative(face);
        if (relative.getType() == Material.REDSTONE_WIRE) {
            return Math.max(power, relative.getData()) > 0;
        }
        return power > 0;
    }

    @Override
    public int getBlockPower(BlockFace face) {
        int power = 0;
        atf wire = aox.af;
        amu world = this.chunk.getHandle().q();
        if ((face == BlockFace.DOWN || face == BlockFace.SELF) && world.b(new et(this.x, this.y - 1, this.z), fa.a)) {
            power = wire.a(world, new et(this.x, this.y - 1, this.z), power);
        }
        if ((face == BlockFace.UP || face == BlockFace.SELF) && world.b(new et(this.x, this.y + 1, this.z), fa.b)) {
            power = wire.a(world, new et(this.x, this.y + 1, this.z), power);
        }
        if ((face == BlockFace.EAST || face == BlockFace.SELF) && world.b(new et(this.x + 1, this.y, this.z), fa.f)) {
            power = wire.a(world, new et(this.x + 1, this.y, this.z), power);
        }
        if ((face == BlockFace.WEST || face == BlockFace.SELF) && world.b(new et(this.x - 1, this.y, this.z), fa.e)) {
            power = wire.a(world, new et(this.x - 1, this.y, this.z), power);
        }
        if ((face == BlockFace.NORTH || face == BlockFace.SELF) && world.b(new et(this.x, this.y, this.z - 1), fa.c)) {
            power = wire.a(world, new et(this.x, this.y, this.z - 1), power);
        }
        if ((face == BlockFace.SOUTH || face == BlockFace.SELF) && world.b(new et(this.x, this.y, this.z + 1), fa.d)) {
            power = wire.a(world, new et(this.x, this.y, this.z - 1), power);
        }
        return power > 0 ? power : ((face == BlockFace.SELF ? this.isBlockIndirectlyPowered() : this.isBlockFaceIndirectlyPowered(face)) ? 15 : 0);
    }

    @Override
    public int getBlockPower() {
        return this.getBlockPower(BlockFace.SELF);
    }

    @Override
    public boolean isEmpty() {
        return this.getType() == Material.AIR;
    }

    @Override
    public boolean isLiquid() {
        return this.getType() == Material.WATER || this.getType() == Material.STATIONARY_WATER || this.getType() == Material.LAVA || this.getType() == Material.STATIONARY_LAVA;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(this.getNMSBlock().h(this.getNMSBlock().a(this.getData())).ordinal());
    }

    private boolean itemCausesDrops(ItemStack item) {
        aow block = this.getNMSBlock();
        ain itemType = item != null ? ain.c(item.getTypeId()) : null;
        return block != null && (block.t().a().l() || itemType != null && itemType.a(block.t()));
    }

    @Override
    public boolean breakNaturally() {
        aow block = this.getNMSBlock();
        byte data = this.getData();
        boolean result = false;
        if (block != null && block != aox.a) {
            block.a(this.chunk.getHandle().q(), new et(this.x, this.y, this.z), block.a(data), 1.0f, 0);
            result = true;
        }
        this.setTypeId(Material.AIR.getId());
        return result;
    }

    @Override
    public boolean breakNaturally(ItemStack item) {
        if (this.itemCausesDrops(item)) {
            return this.breakNaturally();
        }
        return this.setTypeId(Material.AIR.getId());
    }

    @Override
    public Collection<ItemStack> getDrops() {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        aow block = this.getNMSBlock();
        if (block != aox.a) {
            awt data = this.getData0();
            int count = block.a(0, this.chunk.getHandle().q().r);
            for (int i2 = 0; i2 < count; ++i2) {
                ain item = block.a(data, this.chunk.getHandle().q().r, 0);
                if (item == air.a) continue;
                if (aox.ce == block) {
                    aip nmsStack = new aip(item, 1, block.d(data));
                    awd tileentityskull = (awd)this.chunk.getHandle().q().r(new et(this.x, this.y, this.z));
                    if (tileentityskull.f() == 3 && tileentityskull.a() != null) {
                        nmsStack.b(new fy());
                        fy nbttagcompound = new fy();
                        gj.a(nbttagcompound, tileentityskull.a());
                        nmsStack.p().a("SkullOwner", nbttagcompound);
                    }
                    drops.add(CraftItemStack.asBukkitCopy(nmsStack));
                    continue;
                }
                if (aox.bN == block) {
                    int age2 = (Integer)data.c(apm.a);
                    int dropAmount = age2 >= 2 ? 3 : 1;
                    for (int j2 = 0; j2 < dropAmount; ++j2) {
                        drops.add(new ItemStack(Material.INK_SACK, 1, 3));
                    }
                    continue;
                }
                drops.add(new ItemStack(CraftMagicNumbers.getMaterial(item), 1, (short)block.d(data)));
            }
        }
        return drops;
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack item) {
        if (this.itemCausesDrops(item)) {
            return this.getDrops();
        }
        return Collections.emptyList();
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.chunk.getCraftWorld().getBlockMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.chunk.getCraftWorld().getBlockMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.chunk.getCraftWorld().getBlockMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.chunk.getCraftWorld().getBlockMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

}

