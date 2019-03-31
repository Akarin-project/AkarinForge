package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartHopper extends EntityMinecartContainer implements IHopper
{
    private boolean isBlocked = true;
    private int transferTicker = -1;
    private final BlockPos lastPosition = BlockPos.ORIGIN;

    public EntityMinecartHopper(World worldIn)
    {
        super(worldIn);
    }

    public EntityMinecartHopper(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public EntityMinecart.Type getType()
    {
        return EntityMinecart.Type.HOPPER;
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.HOPPER.getDefaultState();
    }

    public int getDefaultDisplayTileOffset()
    {
        return 1;
    }

    public int getSizeInventory()
    {
        return 5;
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        if (super.processInitialInteract(player, hand)) return true;
        if (!this.world.isRemote)
        {
            player.displayGUIChest(this);
        }

        return true;
    }

    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
    {
        boolean flag = !receivingPower;

        if (flag != this.getBlocked())
        {
            this.setBlocked(flag);
        }
    }

    public boolean getBlocked()
    {
        return this.isBlocked;
    }

    public void setBlocked(boolean p_96110_1_)
    {
        this.isBlocked = p_96110_1_;
    }

    public World getWorld()
    {
        return this.world;
    }

    public double getXPos()
    {
        return this.posX;
    }

    public double getYPos()
    {
        return this.posY + 0.5D;
    }

    public double getZPos()
    {
        return this.posZ;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (!this.world.isRemote && this.isEntityAlive() && this.getBlocked())
        {
            BlockPos blockpos = new BlockPos(this);

            if (blockpos.equals(this.lastPosition))
            {
                --this.transferTicker;
            }
            else
            {
                this.setTransferTicker(0);
            }

            if (!this.canTransfer())
            {
                this.setTransferTicker(0);

                if (this.captureDroppedItems())
                {
                    this.setTransferTicker(4);
                    this.markDirty();
                }
            }
        }
    }

    public boolean captureDroppedItems()
    {
        if (TileEntityHopper.pullItems(this))
        {
            return true;
        }
        else
        {
            List<EntityItem> list = this.world.<EntityItem>getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(0.25D, 0.0D, 0.25D), EntitySelectors.IS_ALIVE);

            if (!list.isEmpty())
            {
                TileEntityHopper.putDropInInventoryAllSlots((IInventory)null, this, list.get(0));
            }

            return false;
        }
    }

    public void killMinecart(DamageSource source)
    {
        super.killMinecart(source);

        if (this.world.getGameRules().getBoolean("doEntityDrops"))
        {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.HOPPER), 1, 0.0F);
        }
    }

    public static void registerFixesMinecartHopper(DataFixer fixer)
    {
        EntityMinecartContainer.addDataFixers(fixer, EntityMinecartHopper.class);
    }

    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("TransferCooldown", this.transferTicker);
        compound.setBoolean("Enabled", this.isBlocked);
    }

    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.transferTicker = compound.getInteger("TransferCooldown");
        this.isBlocked = compound.hasKey("Enabled") ? compound.getBoolean("Enabled") : true;
    }

    public void setTransferTicker(int p_98042_1_)
    {
        this.transferTicker = p_98042_1_;
    }

    public boolean canTransfer()
    {
        return this.transferTicker > 0;
    }

    public String getGuiID()
    {
        return "minecraft:hopper";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerHopper(playerInventory, this, playerIn);
    }
}