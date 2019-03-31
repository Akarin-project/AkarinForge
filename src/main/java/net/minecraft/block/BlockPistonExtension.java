package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPistonExtension extends BlockDirectional
{
    public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE = PropertyEnum.<BlockPistonExtension.EnumPistonType>create("type", BlockPistonExtension.EnumPistonType.class);
    public static final PropertyBool SHORT = PropertyBool.create("short");
    protected static final AxisAlignedBB PISTON_EXTENSION_EAST_AABB = new AxisAlignedBB(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
    protected static final AxisAlignedBB PISTON_EXTENSION_UP_AABB = new AxisAlignedBB(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
    protected static final AxisAlignedBB UP_ARM_AABB = new AxisAlignedBB(0.375D, -0.25D, 0.375D, 0.625D, 0.75D, 0.625D);
    protected static final AxisAlignedBB DOWN_ARM_AABB = new AxisAlignedBB(0.375D, 0.25D, 0.375D, 0.625D, 1.25D, 0.625D);
    protected static final AxisAlignedBB SOUTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, -0.25D, 0.625D, 0.625D, 0.75D);
    protected static final AxisAlignedBB NORTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 1.25D);
    protected static final AxisAlignedBB EAST_ARM_AABB = new AxisAlignedBB(-0.25D, 0.375D, 0.375D, 0.75D, 0.625D, 0.625D);
    protected static final AxisAlignedBB WEST_ARM_AABB = new AxisAlignedBB(0.25D, 0.375D, 0.375D, 1.25D, 0.625D, 0.625D);
    protected static final AxisAlignedBB SHORT_UP_ARM_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D);
    protected static final AxisAlignedBB SHORT_DOWN_ARM_AABB = new AxisAlignedBB(0.375D, 0.25D, 0.375D, 0.625D, 1.0D, 0.625D);
    protected static final AxisAlignedBB SHORT_SOUTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 0.75D);
    protected static final AxisAlignedBB SHORT_NORTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 1.0D);
    protected static final AxisAlignedBB SHORT_EAST_ARM_AABB = new AxisAlignedBB(0.0D, 0.375D, 0.375D, 0.75D, 0.625D, 0.625D);
    protected static final AxisAlignedBB SHORT_WEST_ARM_AABB = new AxisAlignedBB(0.25D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);

    public BlockPistonExtension()
    {
        super(Material.PISTON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT).withProperty(SHORT, Boolean.valueOf(false)));
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.5F);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch ((EnumFacing)state.getValue(FACING))
        {
            case DOWN:
            default:
                return PISTON_EXTENSION_DOWN_AABB;
            case UP:
                return PISTON_EXTENSION_UP_AABB;
            case NORTH:
                return PISTON_EXTENSION_NORTH_AABB;
            case SOUTH:
                return PISTON_EXTENSION_SOUTH_AABB;
            case WEST:
                return PISTON_EXTENSION_WEST_AABB;
            case EAST:
                return PISTON_EXTENSION_EAST_AABB;
        }
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getBoundingBox(worldIn, pos));
        addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getArmShape(state));
    }

    private AxisAlignedBB getArmShape(IBlockState state)
    {
        boolean flag = ((Boolean)state.getValue(SHORT)).booleanValue();

        switch ((EnumFacing)state.getValue(FACING))
        {
            case DOWN:
            default:
                return flag ? SHORT_DOWN_ARM_AABB : DOWN_ARM_AABB;
            case UP:
                return flag ? SHORT_UP_ARM_AABB : UP_ARM_AABB;
            case NORTH:
                return flag ? SHORT_NORTH_ARM_AABB : NORTH_ARM_AABB;
            case SOUTH:
                return flag ? SHORT_SOUTH_ARM_AABB : SOUTH_ARM_AABB;
            case WEST:
                return flag ? SHORT_WEST_ARM_AABB : WEST_ARM_AABB;
            case EAST:
                return flag ? SHORT_EAST_ARM_AABB : EAST_ARM_AABB;
        }
    }

    public boolean isTopSolid(IBlockState state)
    {
        return state.getValue(FACING) == EnumFacing.UP;
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode)
        {
            BlockPos blockpos = pos.offset(((EnumFacing)state.getValue(FACING)).getOpposite());
            Block block = worldIn.getBlockState(blockpos).getBlock();

            if (block == Blocks.PISTON || block == Blocks.STICKY_PISTON)
            {
                worldIn.setBlockToAir(blockpos);
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        EnumFacing enumfacing = ((EnumFacing)state.getValue(FACING)).getOpposite();
        pos = pos.offset(enumfacing);
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if ((iblockstate.getBlock() == Blocks.PISTON || iblockstate.getBlock() == Blocks.STICKY_PISTON) && ((Boolean)iblockstate.getValue(BlockPistonBase.EXTENDED)).booleanValue())
        {
            iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return false;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return false;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);

        if (iblockstate.getBlock() != Blocks.PISTON && iblockstate.getBlock() != Blocks.STICKY_PISTON)
        {
            worldIn.setBlockToAir(pos);
        }
        else
        {
            iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Nullable
    public static EnumFacing getFacing(int meta)
    {
        int i = meta & 7;
        return i > 5 ? null : EnumFacing.getFront(i);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(state.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY ? Blocks.STICKY_PISTON : Blocks.PISTON);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(TYPE, (meta & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

        if (state.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY)
        {
            i |= 8;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, TYPE, SHORT});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return face == state.getValue(FACING) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public static enum EnumPistonType implements IStringSerializable
    {
        DEFAULT("normal"),
        STICKY("sticky");

        private final String VARIANT;

        private EnumPistonType(String name)
        {
            this.VARIANT = name;
        }

        public String toString()
        {
            return this.VARIANT;
        }

        public String getName()
        {
            return this.VARIANT;
        }
    }
}