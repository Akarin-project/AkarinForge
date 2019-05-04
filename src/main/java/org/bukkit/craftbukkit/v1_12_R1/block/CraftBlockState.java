/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import java.util.List;
import net.minecraftforge.common.util.BlockSnapshot;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_12_R1.metadata.BlockMetadataStore;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class CraftBlockState
implements BlockState {
    private final CraftWorld world;
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;
    private final fy nbt;
    protected int type;
    protected MaterialData data;
    protected int flag;

    public CraftBlockState(Block block) {
        this.world = (CraftWorld)block.getWorld();
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.type = block.getTypeId();
        this.chunk = (CraftChunk)block.getChunk();
        this.flag = 3;
        this.createData(block.getData());
        avj te2 = this.world.getHandle().r(new et(this.x, this.y, this.z));
        if (te2 != null) {
            this.nbt = new fy();
            te2.b(this.nbt);
        } else {
            this.nbt = null;
        }
    }

    public CraftBlockState(Block block, int flag) {
        this(block);
        this.flag = flag;
    }

    public CraftBlockState(Material material) {
        this.world = null;
        this.type = material.getId();
        this.chunk = null;
        this.z = 0;
        this.y = 0;
        this.x = 0;
        this.nbt = null;
    }

    public CraftBlockState(BlockSnapshot blocksnapshot) {
        this.world = blocksnapshot.getWorld().getWorld();
        this.x = blocksnapshot.getPos().p();
        this.y = blocksnapshot.getPos().q();
        this.z = blocksnapshot.getPos().r();
        this.type = aow.a(blocksnapshot.getReplacedBlock().u());
        this.chunk = (CraftChunk)this.world.getBlockAt(this.x, this.y, this.z).getChunk();
        this.flag = 3;
        this.nbt = blocksnapshot.getNbt();
        this.createData((byte)blocksnapshot.getMeta());
    }

    public static CraftBlockState getBlockState(amu world, int x2, int y2, int z2) {
        return new CraftBlockState(world.getWorld().getBlockAt(x2, y2, z2));
    }

    public static CraftBlockState getBlockState(amu world, int x2, int y2, int z2, int flag) {
        return new CraftBlockState(world.getWorld().getBlockAt(x2, y2, z2), flag);
    }

    @Override
    public World getWorld() {
        this.requirePlaced();
        return this.world;
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
        this.requirePlaced();
        return this.chunk;
    }

    @Override
    public void setData(MaterialData data) {
        Material mat = this.getType();
        if (mat == null || mat.getData() == null) {
            this.data = data;
        } else if (data.getClass() == mat.getData() || data.getClass() == MaterialData.class) {
            this.data = data;
        } else {
            throw new IllegalArgumentException("Provided data is not of type " + mat.getData().getName() + ", found " + data.getClass().getName());
        }
    }

    @Override
    public MaterialData getData() {
        return this.data;
    }

    @Override
    public void setType(Material type) {
        this.setTypeId(type.getId());
    }

    @Override
    public boolean setTypeId(int type) {
        if (this.type != type) {
            this.type = type;
            this.createData(0);
        }
        return true;
    }

    @Override
    public Material getType() {
        return Material.getBlockMaterial(this.getTypeId());
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return this.flag;
    }

    @Override
    public int getTypeId() {
        return this.type;
    }

    @Override
    public byte getLightLevel() {
        return this.getBlock().getLightLevel();
    }

    @Override
    public Block getBlock() {
        this.requirePlaced();
        return this.world.getBlockAt(this.x, this.y, this.z);
    }

    @Override
    public boolean update() {
        return this.update(false);
    }

    @Override
    public boolean update(boolean force) {
        return this.update(force, true);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        avj te2;
        if (!this.isPlaced()) {
            return true;
        }
        Block block = this.getBlock();
        if (block.getType() != this.getType() && !force) {
            return false;
        }
        et pos = new et(this.x, this.y, this.z);
        awt newBlock = CraftMagicNumbers.getBlock(this.getType()).a(this.getRawData());
        block.setTypeIdAndData(this.getTypeId(), this.getRawData(), applyPhysics);
        this.world.getHandle().a(pos, CraftMagicNumbers.getBlock(block).a(block.getData()), newBlock, 3);
        if (applyPhysics && this.getData() instanceof Attachable) {
            this.world.getHandle().b(pos.a(CraftBlock.blockFaceToNotch(((Attachable)((Object)this.getData())).getAttachedFace())), newBlock.u(), false);
        }
        if (this.nbt != null && (te2 = this.world.getHandle().r(new et(this.x, this.y, this.z))) != null) {
            fy nbt2 = new fy();
            te2.b(nbt2);
            if (!nbt2.equals(this.nbt)) {
                te2.a(this.nbt);
            }
        }
        return true;
    }

    private void createData(byte data) {
        Material mat = this.getType();
        this.data = mat == null || mat.getData() == null ? new MaterialData(this.type, data) : mat.getNewData(data);
    }

    @Override
    public byte getRawData() {
        return this.data.getData();
    }

    @Override
    public Location getLocation() {
        return new Location(this.world, this.x, this.y, this.z);
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(this.world);
            loc.setX(this.x);
            loc.setY(this.y);
            loc.setZ(this.z);
            loc.setYaw(0.0f);
            loc.setPitch(0.0f);
        }
        return loc;
    }

    @Override
    public void setRawData(byte data) {
        this.data.setData(data);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        CraftBlockState other = (CraftBlockState)obj;
        if (!(this.world == other.world || this.world != null && this.world.equals(other.world))) {
            return false;
        }
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (!(this.data == other.data || this.data != null && this.data.equals(other.data))) {
            return false;
        }
        if (!(this.nbt == other.nbt || this.nbt != null && this.nbt.equals(other.nbt))) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.world != null ? this.world.hashCode() : 0);
        hash = 73 * hash + this.x;
        hash = 73 * hash + this.y;
        hash = 73 * hash + this.z;
        hash = 73 * hash + this.type;
        hash = 73 * hash + (this.data != null ? this.data.hashCode() : 0);
        hash = 73 * hash + (this.nbt != null ? this.nbt.hashCode() : 0);
        return hash;
    }

    public avj getTileEntity() {
        if (this.nbt != null) {
            return avj.a(this.world.getHandle(), this.nbt);
        }
        return null;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.requirePlaced();
        this.chunk.getCraftWorld().getBlockMetadata().setMetadata(this.getBlock(), metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        this.requirePlaced();
        return this.chunk.getCraftWorld().getBlockMetadata().getMetadata(this.getBlock(), metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        this.requirePlaced();
        return this.chunk.getCraftWorld().getBlockMetadata().hasMetadata(this.getBlock(), metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.requirePlaced();
        this.chunk.getCraftWorld().getBlockMetadata().removeMetadata(this.getBlock(), metadataKey, owningPlugin);
    }

    @Override
    public boolean isPlaced() {
        return this.world != null;
    }

    protected void requirePlaced() {
        if (!this.isPlaced()) {
            throw new IllegalStateException("The blockState must be placed to call this method");
        }
    }
}

