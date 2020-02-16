package net.minecraft.block;

import java.util.Random;

import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.event.entity.EntityInteractEvent;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRedstoneOre extends Block
{
    private final boolean isOn;

    public BlockRedstoneOre(boolean isOn)
    {
        super(Material.ROCK);

        if (isOn)
        {
            this.setTickRandomly(true);
        }

        this.isOn = isOn;
    }

    public int tickRate(World worldIn)
    {
        return 30;
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        this.interact(worldIn, pos, playerIn); // CraftBukkit - add entityhuman
        super.onBlockClicked(worldIn, pos, playerIn);
    }

    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        // CraftBukkit start
        // this.activate(worldIn, pos);
        // super.onEntityWalk(worldIn, pos, entityIn);
        if (entityIn instanceof EntityPlayer) {
            org.bukkit.event.player.PlayerInteractEvent event = org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entityIn, org.bukkit.event.block.Action.PHYSICAL, pos, null, null, null);
            if (!event.isCancelled()) {
                this.interact(worldIn, pos, entityIn); // add entity
                super.onEntityWalk(worldIn, pos, entityIn);
            }
        } else {
            EntityInteractEvent event = new EntityInteractEvent(entityIn.getBukkitEntity(), worldIn.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()));
            worldIn.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.interact(worldIn, pos, entityIn); // add entity
                super.onEntityWalk(worldIn, pos, entityIn);
            }
        }
        // CraftBukkit end
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        this.interact(worldIn, pos, playerIn); // CraftBukkit - add entityhuman
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    // CraftBukkit start
    private void interact(World world, BlockPos blockposition, Entity entity) { // CraftBukkit - add Entity
        this.spawnParticles(world, blockposition);
        if (this == Blocks.REDSTONE_ORE) {
            if (CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition, Blocks.LIT_REDSTONE_ORE, 0).isCancelled()) {
                return;
            }
            world.setBlockState(blockposition, Blocks.LIT_REDSTONE_ORE.getDefaultState());
        }
    }
    
    @Override
    public int getExpDrop(World world, IBlockState data, int i) {
        if (this.getItemDropped(data, world.rand, i) != Item.getItemFromBlock(this)) {
            int j = 1 + world.rand.nextInt(5);
            return j;
        }
        return 0;
    }
    // CraftBukkit end
    private void activate(World worldIn, BlockPos pos)
    {
        this.spawnParticles(worldIn, pos);

        if (this == Blocks.REDSTONE_ORE)
        {
            worldIn.setBlockState(pos, Blocks.LIT_REDSTONE_ORE.getDefaultState());
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (this == Blocks.LIT_REDSTONE_ORE)
        {
            // CraftBukkit start
            if (CraftEventFactory.callBlockFadeEvent(worldIn.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), Blocks.REDSTONE_ORE).isCancelled()) {
                return;
            }
            // CraftBukkit end
            worldIn.setBlockState(pos, Blocks.REDSTONE_ORE.getDefaultState());
        }
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.REDSTONE;
    }

    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return this.quantityDropped(random) + random.nextInt(fortune + 1);
    }

    public int quantityDropped(Random random)
    {
        return 4 + random.nextInt(2);
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        if (this.getItemDropped(state, RANDOM, fortune) != Item.getItemFromBlock(this))
        {
            return 1 + RANDOM.nextInt(5);
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (this.isOn)
        {
            this.spawnParticles(worldIn, pos);
        }
    }

    private void spawnParticles(World worldIn, BlockPos pos)
    {
        Random random = worldIn.rand;
        double d0 = 0.0625D;

        for (int i = 0; i < 6; ++i)
        {
            double d1 = (double)((float)pos.getX() + random.nextFloat());
            double d2 = (double)((float)pos.getY() + random.nextFloat());
            double d3 = (double)((float)pos.getZ() + random.nextFloat());

            if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube())
            {
                d2 = (double)pos.getY() + 0.0625D + 1.0D;
            }

            if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube())
            {
                d2 = (double)pos.getY() - 0.0625D;
            }

            if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube())
            {
                d3 = (double)pos.getZ() + 0.0625D + 1.0D;
            }

            if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube())
            {
                d3 = (double)pos.getZ() - 0.0625D;
            }

            if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube())
            {
                d1 = (double)pos.getX() + 0.0625D + 1.0D;
            }

            if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube())
            {
                d1 = (double)pos.getX() - 0.0625D;
            }

            if (d1 < (double)pos.getX() || d1 > (double)(pos.getX() + 1) || d2 < 0.0D || d2 > (double)(pos.getY() + 1) || d3 < (double)pos.getZ() || d3 > (double)(pos.getZ() + 1))
            {
                worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(Blocks.REDSTONE_ORE);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(Blocks.REDSTONE_ORE), 1, this.damageDropped(state));
    }
}