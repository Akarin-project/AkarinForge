package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIHarvestFarmland extends EntityAIMoveToBlock
{
    private final EntityVillager villager;
    private boolean hasFarmItem;
    private boolean wantsToReapStuff;
    private int currentTask;

    public EntityAIHarvestFarmland(EntityVillager villagerIn, double speedIn)
    {
        super(villagerIn, speedIn, 16);
        this.villager = villagerIn;
    }

    public boolean shouldExecute()
    {
        if (this.runDelay <= 0)
        {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.villager.world, this.villager))
            {
                return false;
            }

            this.currentTask = -1;
            this.hasFarmItem = this.villager.isFarmItemInInventory();
            this.wantsToReapStuff = this.villager.wantsMoreFood();
        }

        return super.shouldExecute();
    }

    public boolean shouldContinueExecuting()
    {
        return this.currentTask >= 0 && super.shouldContinueExecuting();
    }

    public void updateTask()
    {
        super.updateTask();
        this.villager.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.villager.getVerticalFaceSpeed());

        if (this.getIsAboveDestination())
        {
            World world = this.villager.world;
            BlockPos blockpos = this.destinationBlock.up();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (this.currentTask == 0 && block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate))
            {
                // CraftBukkit start
                if (!org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory.callEntityChangeBlockEvent(this.villager, blockpos, Blocks.AIR, 0).isCancelled()) {
                    world.destroyBlock(blockpos, true);
                }
                // CraftBukkit end
            }
            else if (this.currentTask == 1 && iblockstate.getMaterial() == Material.AIR)
            {
                InventoryBasic inventorybasic = this.villager.getVillagerInventory();

                for (int i = 0; i < inventorybasic.getSizeInventory(); ++i)
                {
                    ItemStack itemstack = inventorybasic.getStackInSlot(i);
                    boolean flag = false;

                    if (!itemstack.isEmpty())
                    {
                        // CraftBukkit start
                        Block planted = null;
                        if (itemstack.getItem() == Items.WHEAT_SEEDS)
                        {
                            planted = Blocks.WHEAT;
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.POTATO)
                        {
                            planted = Blocks.POTATOES;
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.CARROT)
                        {
                            planted = Blocks.CARROTS;
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.BEETROOT_SEEDS)
                        {
                            planted = Blocks.BEETROOTS;
                            flag = true;
                        }
                        else if (itemstack.getItem() instanceof net.minecraftforge.common.IPlantable) {
                            if(((net.minecraftforge.common.IPlantable)itemstack.getItem()).getPlantType(world,blockpos) == net.minecraftforge.common.EnumPlantType.Crop) {
                                world.setBlockState(blockpos, ((net.minecraftforge.common.IPlantable)itemstack.getItem()).getPlant(world,blockpos),3);
                                flag = true;
                            }
                        }
                        if (planted != null && !org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory.callEntityChangeBlockEvent(this.villager, blockpos, planted, 0).isCancelled()) {
                            world.setBlockState(blockpos, planted.getDefaultState(), 3);
                        } else {
                            flag = false;
                        }
                        // CraftBukkit end
                    }

                    if (flag)
                    {
                        itemstack.shrink(1);

                        if (itemstack.isEmpty())
                        {
                            inventorybasic.setInventorySlotContents(i, ItemStack.EMPTY);
                        }

                        break;
                    }
                }
            }

            this.currentTask = -1;
            this.runDelay = 10;
        }
    }

    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == Blocks.FARMLAND)
        {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate) && this.wantsToReapStuff && (this.currentTask == 0 || this.currentTask < 0))
            {
                this.currentTask = 0;
                return true;
            }

            if (iblockstate.getMaterial() == Material.AIR && this.hasFarmItem && (this.currentTask == 1 || this.currentTask < 0))
            {
                this.currentTask = 1;
                return true;
            }
        }

        return false;
    }
}