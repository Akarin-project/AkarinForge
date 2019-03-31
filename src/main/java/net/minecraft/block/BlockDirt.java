package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDirt extends Block
{
    public static final PropertyEnum<BlockDirt.DirtType> VARIANT = PropertyEnum.<BlockDirt.DirtType>create("variant", BlockDirt.DirtType.class);
    public static final PropertyBool SNOWY = PropertyBool.create("snowy");

    protected BlockDirt()
    {
        super(Material.GROUND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockDirt.DirtType.DIRT).withProperty(SNOWY, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return ((BlockDirt.DirtType)state.getValue(VARIANT)).getColor();
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (state.getValue(VARIANT) == BlockDirt.DirtType.PODZOL)
        {
            Block block = worldIn.getBlockState(pos.up()).getBlock();
            state = state.withProperty(SNOWY, Boolean.valueOf(block == Blocks.SNOW || block == Blocks.SNOW_LAYER));
        }

        return state;
    }

    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, BlockDirt.DirtType.DIRT.getMetadata()));
        items.add(new ItemStack(this, 1, BlockDirt.DirtType.COARSE_DIRT.getMetadata()));
        items.add(new ItemStack(this, 1, BlockDirt.DirtType.PODZOL.getMetadata()));
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, ((BlockDirt.DirtType)state.getValue(VARIANT)).getMetadata());
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, BlockDirt.DirtType.byMetadata(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((BlockDirt.DirtType)state.getValue(VARIANT)).getMetadata();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT, SNOWY});
    }

    public int damageDropped(IBlockState state)
    {
        BlockDirt.DirtType blockdirt$dirttype = (BlockDirt.DirtType)state.getValue(VARIANT);

        if (blockdirt$dirttype == BlockDirt.DirtType.PODZOL)
        {
            blockdirt$dirttype = BlockDirt.DirtType.DIRT;
        }

        return blockdirt$dirttype.getMetadata();
    }

    public static enum DirtType implements IStringSerializable
    {
        DIRT(0, "dirt", "default", MapColor.DIRT),
        COARSE_DIRT(1, "coarse_dirt", "coarse", MapColor.DIRT),
        PODZOL(2, "podzol", MapColor.OBSIDIAN);

        private static final BlockDirt.DirtType[] METADATA_LOOKUP = new BlockDirt.DirtType[values().length];
        private final int metadata;
        private final String name;
        private final String unlocalizedName;
        private final MapColor color;

        private DirtType(int metadataIn, String nameIn, MapColor color)
        {
            this(metadataIn, nameIn, nameIn, color);
        }

        private DirtType(int metadataIn, String nameIn, String unlocalizedNameIn, MapColor color)
        {
            this.metadata = metadataIn;
            this.name = nameIn;
            this.unlocalizedName = unlocalizedNameIn;
            this.color = color;
        }

        public int getMetadata()
        {
            return this.metadata;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        public MapColor getColor()
        {
            return this.color;
        }

        public String toString()
        {
            return this.name;
        }

        public static BlockDirt.DirtType byMetadata(int metadata)
        {
            if (metadata < 0 || metadata >= METADATA_LOOKUP.length)
            {
                metadata = 0;
            }

            return METADATA_LOOKUP[metadata];
        }

        public String getName()
        {
            return this.name;
        }

        static
        {
            for (BlockDirt.DirtType blockdirt$dirttype : values())
            {
                METADATA_LOOKUP[blockdirt$dirttype.getMetadata()] = blockdirt$dirttype;
            }
        }
    }
}