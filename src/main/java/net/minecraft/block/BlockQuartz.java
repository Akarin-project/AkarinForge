package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockQuartz extends Block
{
    public static final PropertyEnum<BlockQuartz.EnumType> VARIANT = PropertyEnum.<BlockQuartz.EnumType>create("variant", BlockQuartz.EnumType.class);

    public BlockQuartz()
    {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockQuartz.EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if (meta == BlockQuartz.EnumType.LINES_Y.getMetadata())
        {
            switch (facing.getAxis())
            {
                case Z:
                    return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_Z);
                case X:
                    return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_X);
                case Y:
                    return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_Y);
            }
        }

        return meta == BlockQuartz.EnumType.CHISELED.getMetadata() ? this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.CHISELED) : this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.DEFAULT);
    }

    public int damageDropped(IBlockState state)
    {
        BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType)state.getValue(VARIANT);
        return blockquartz$enumtype != BlockQuartz.EnumType.LINES_X && blockquartz$enumtype != BlockQuartz.EnumType.LINES_Z ? blockquartz$enumtype.getMetadata() : BlockQuartz.EnumType.LINES_Y.getMetadata();
    }

    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType)state.getValue(VARIANT);
        return blockquartz$enumtype != BlockQuartz.EnumType.LINES_X && blockquartz$enumtype != BlockQuartz.EnumType.LINES_Z ? super.getSilkTouchDrop(state) : new ItemStack(Item.getItemFromBlock(this), 1, BlockQuartz.EnumType.LINES_Y.getMetadata());
    }

    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, BlockQuartz.EnumType.DEFAULT.getMetadata()));
        items.add(new ItemStack(this, 1, BlockQuartz.EnumType.CHISELED.getMetadata()));
        items.add(new ItemStack(this, 1, BlockQuartz.EnumType.LINES_Y.getMetadata()));
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.QUARTZ;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.byMetadata(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((BlockQuartz.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:

                switch ((BlockQuartz.EnumType)state.getValue(VARIANT))
                {
                    case LINES_X:
                        return state.withProperty(VARIANT, BlockQuartz.EnumType.LINES_Z);
                    case LINES_Z:
                        return state.withProperty(VARIANT, BlockQuartz.EnumType.LINES_X);
                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }

    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        IBlockState state = world.getBlockState(pos);
        for (IProperty prop : state.getProperties().keySet())
        {
            if (prop.getName().equals("variant") && prop.getValueClass() == EnumType.class)
            {
                EnumType current = (EnumType)state.getValue(prop);
                EnumType next = current == EnumType.LINES_X ? EnumType.LINES_Y :
                                current == EnumType.LINES_Y ? EnumType.LINES_Z :
                                current == EnumType.LINES_Z ? EnumType.LINES_X : current;
                if (next == current)
                    return false;
                world.setBlockState(pos, state.withProperty(prop, next));
                return true;
            }
        }
        return false;
    }

    public static enum EnumType implements IStringSerializable
    {
        DEFAULT(0, "default", "default"),
        CHISELED(1, "chiseled", "chiseled"),
        LINES_Y(2, "lines_y", "lines"),
        LINES_X(3, "lines_x", "lines"),
        LINES_Z(4, "lines_z", "lines");

        private static final BlockQuartz.EnumType[] META_LOOKUP = new BlockQuartz.EnumType[values().length];
        private final int meta;
        private final String serializedName;
        private final String unlocalizedName;

        private EnumType(int meta, String name, String unlocalizedName)
        {
            this.meta = meta;
            this.serializedName = name;
            this.unlocalizedName = unlocalizedName;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.unlocalizedName;
        }

        public static BlockQuartz.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.serializedName;
        }

        static
        {
            for (BlockQuartz.EnumType blockquartz$enumtype : values())
            {
                META_LOOKUP[blockquartz$enumtype.getMetadata()] = blockquartz$enumtype;
            }
        }
    }
}