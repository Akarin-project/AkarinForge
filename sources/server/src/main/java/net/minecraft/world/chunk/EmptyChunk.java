package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EmptyChunk extends Chunk
{
    public EmptyChunk(World worldIn, int x, int z)
    {
        super(worldIn, x, z);
    }

    public boolean isAtLocation(int x, int z)
    {
        return x == this.x && z == this.z;
    }

    public int getHeightValue(int x, int z)
    {
        return 0;
    }

    public void generateHeightMap()
    {
    }

    public void generateSkylightMap()
    {
    }

    public IBlockState getBlockState(BlockPos pos)
    {
        return Blocks.AIR.getDefaultState();
    }

    public int getBlockLightOpacity(BlockPos pos)
    {
        return 255;
    }

    public int getLightFor(EnumSkyBlock type, BlockPos pos)
    {
        return type.defaultLightValue;
    }

    public void setLightFor(EnumSkyBlock type, BlockPos pos, int value)
    {
    }

    public int getLightSubtracted(BlockPos pos, int amount)
    {
        return 0;
    }

    public void addEntity(Entity entityIn)
    {
    }

    public void removeEntity(Entity entityIn)
    {
    }

    public void removeEntityAtIndex(Entity entityIn, int index)
    {
    }

    public boolean canSeeSky(BlockPos pos)
    {
        return false;
    }

    @Nullable
    public TileEntity getTileEntity(BlockPos pos, Chunk.EnumCreateEntityType p_177424_2_)
    {
        return null;
    }

    public void addTileEntity(TileEntity tileEntityIn)
    {
    }

    public void addTileEntity(BlockPos pos, TileEntity tileEntityIn)
    {
    }

    public void removeTileEntity(BlockPos pos)
    {
    }

    public void onLoad()
    {
    }

    public void onUnload()
    {
    }

    public void markDirty()
    {
    }

    public void getEntitiesWithinAABBForEntity(@Nullable Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, Predicate <? super Entity > filter)
    {
    }

    public <T extends Entity> void getEntitiesOfTypeWithinAABB(Class <? extends T > entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate <? super T > filter)
    {
    }

    public boolean needsSaving(boolean p_76601_1_)
    {
        return false;
    }

    public Random getRandomWithSeed(long seed)
    {
        return new Random(this.getWorld().getSeed() + (long)(this.x * this.x * 4987142) + (long)(this.x * 5947611) + (long)(this.z * this.z) * 4392871L + (long)(this.z * 389711) ^ seed);
    }

    public boolean isEmpty()
    {
        return true;
    }

    public boolean isEmptyBetween(int startY, int endY)
    {
        return true;
    }
}