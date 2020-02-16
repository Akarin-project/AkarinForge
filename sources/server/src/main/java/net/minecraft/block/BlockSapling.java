package net.minecraft.block;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockSapling extends BlockBush implements IGrowable
{
    public static final PropertyEnum<BlockPlanks.EnumType> TYPE = PropertyEnum.<BlockPlanks.EnumType>create("type", BlockPlanks.EnumType.class);
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
    public static TreeType treeType; // CraftBukkit

    protected BlockSapling()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockPlanks.EnumType.OAK).withProperty(STAGE, Integer.valueOf(0)));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SAPLING_AABB;
    }

    public String getLocalizedName()
    {
        return I18n.translateToLocal(this.getUnlocalizedName() + "." + BlockPlanks.EnumType.OAK.getUnlocalizedName() + ".name");
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
            {
                worldIn.captureTreeGeneration = true; // CraftBukkit
                this.grow(worldIn, pos, state, rand);
                // CraftBukkit start
                worldIn.captureTreeGeneration = false;
                if (worldIn.capturedBlockSnapshots.size() > 0) {
                    TreeType treeType = BlockSapling.treeType;
                    BlockSapling.treeType = null;
                    Location location = new Location(worldIn.getWorld(), pos.getX(), pos.getY(), pos.getZ());
                    List<BlockState> blocks = (List<BlockState>) worldIn.capturedBlockSnapshots.clone();
                    worldIn.capturedBlockSnapshots.clear();
                    StructureGrowEvent event = null;
                    if (treeType != null) {
                        event = new StructureGrowEvent(location, treeType, false, null, blocks);
                        org.bukkit.Bukkit.getPluginManager().callEvent(event);
                    }
                    if (event == null || !event.isCancelled()) {
                        for (BlockState blockstate : blocks) {
                            blockstate.update(true);
                        }
                    }
                }
                // CraftBukkit end
            }
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (((Integer)state.getValue(STAGE)).intValue() == 0)
        {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        }
        else
        {
            this.generateTree(worldIn, pos, state, rand);
        }
    }

    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
        // CraftBukkit start - Turn ternary operator into if statement to set treeType
        // WorldGenerator worldgenerator = (WorldGenerator)(rand.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true));
        WorldGenerator worldgenerator;
        if (rand.nextInt(10) == 0) {
            treeType = TreeType.BIG_TREE;
            worldgenerator = new WorldGenBigTree(true);
        } else {
            treeType = TreeType.TREE;
            worldgenerator = new WorldGenTrees(true);
        }
        // CraftBukkit end
        int i = 0;
        int j = 0;
        boolean flag = false;

        switch ((BlockPlanks.EnumType)state.getValue(TYPE))
        {
            case SPRUCE:
                label68:

                for (i = 0; i >= -1; --i)
                {
                    for (j = 0; j >= -1; --j)
                    {
                        if (this.isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE))
                        {
                        	treeType = TreeType.MEGA_REDWOOD; // CraftBukkit
                            worldgenerator = new WorldGenMegaPineTree(false, rand.nextBoolean());
                            flag = true;
                            break label68;
                        }
                    }
                }

                if (!flag)
                {
                    i = 0;
                    j = 0;
                    treeType = TreeType.REDWOOD; // CraftBukkit
                    worldgenerator = new WorldGenTaiga2(true);
                }

                break;
            case BIRCH:
            	treeType = TreeType.BIRCH; // CraftBukkit
                worldgenerator = new WorldGenBirchTree(true, false);
                break;
            case JUNGLE:
                IBlockState iblockstate = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
                IBlockState iblockstate1 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
                label82:

                for (i = 0; i >= -1; --i)
                {
                    for (j = 0; j >= -1; --j)
                    {
                        if (this.isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE))
                        {
                        	treeType = TreeType.JUNGLE; // CraftBukkit
                            worldgenerator = new WorldGenMegaJungle(true, 10, 20, iblockstate, iblockstate1);
                            flag = true;
                            break label82;
                        }
                    }
                }

                if (!flag)
                {
                    i = 0;
                    j = 0;
                    treeType = TreeType.SMALL_JUNGLE; // CraftBukkit
                    worldgenerator = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockstate, iblockstate1, false);
                }

                break;
            case ACACIA:
            	treeType = TreeType.ACACIA; // CraftBukkit
                worldgenerator = new WorldGenSavannaTree(true);
                break;
            case DARK_OAK:
                label96:

                for (i = 0; i >= -1; --i)
                {
                    for (j = 0; j >= -1; --j)
                    {
                        if (this.isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK))
                        {
                        	treeType = TreeType.DARK_OAK; // CraftBukkit
                            worldgenerator = new WorldGenCanopyTree(true);
                            flag = true;
                            break label96;
                        }
                    }
                }

                if (!flag)
                {
                    return;
                }

            case OAK:
        }

        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        if (flag)
        {
            worldIn.setBlockState(pos.add(i, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate2, 4);
        }
        else
        {
            worldIn.setBlockState(pos, iblockstate2, 4);
        }

        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j)))
        {
            if (flag)
            {
                worldIn.setBlockState(pos.add(i, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
            }
            else
            {
                worldIn.setBlockState(pos, state, 4);
            }
        }
    }

    private boolean isTwoByTwoOfType(World worldIn, BlockPos pos, int p_181624_3_, int p_181624_4_, BlockPlanks.EnumType type)
    {
        return this.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_), type) && this.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_), type) && this.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_ + 1), type) && this.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), type);
    }

    public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() == this && iblockstate.getValue(TYPE) == type;
    }

    public int damageDropped(IBlockState state)
    {
        return ((BlockPlanks.EnumType)state.getValue(TYPE)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (BlockPlanks.EnumType blockplanks$enumtype : BlockPlanks.EnumType.values())
        {
            items.add(new ItemStack(this, 1, blockplanks$enumtype.getMetadata()));
        }
    }

    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return (double)worldIn.rand.nextFloat() < 0.45D;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.grow(worldIn, pos, state, rand);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TYPE, BlockPlanks.EnumType.byMetadata(meta & 7)).withProperty(STAGE, Integer.valueOf((meta & 8) >> 3));
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((BlockPlanks.EnumType)state.getValue(TYPE)).getMetadata();
        i = i | ((Integer)state.getValue(STAGE)).intValue() << 3;
        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {TYPE, STAGE});
    }
}