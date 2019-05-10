/*
 * Akarin reference
 */
package net.minecraft.server.management;

import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PlayerInteractionManager
{
    public World world;
    public EntityPlayerMP player;
    private GameType gameType = GameType.NOT_SET;
    private boolean isDestroyingBlock;
    private int initialDamage;
    private BlockPos destroyPos = BlockPos.ORIGIN;
    private int curblockDamage;
    private boolean receivedFinishDiggingPacket;
    private BlockPos delayedDestroyPos = BlockPos.ORIGIN;
    private int initialBlockDamage;
    private int durabilityRemainingOnBlock = -1;
    // Akarin start
    public boolean interactResult = false;
    public boolean firedInteract = false;
    // Akarin end

    public PlayerInteractionManager(World worldIn)
    {
        this.world = worldIn;
    }

    public void setGameType(GameType type)
    {
        this.gameType = type;
        type.configurePlayerCapabilities(this.player.capabilities);
        this.player.sendPlayerAbilities();
        this.player.mcServer.getPlayerList().sendAll(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] {this.player}), this.player); // CraftBukkit
        this.world.updateAllPlayersSleepingFlag();
    }

    public GameType getGameType()
    {
        return this.gameType;
    }

    public boolean survivalOrAdventure()
    {
        return this.gameType.isSurvivalOrAdventure();
    }

    public boolean isCreative()
    {
        return this.gameType.isCreative();
    }

    public void initializeGameType(GameType type)
    {
        if (this.gameType == GameType.NOT_SET)
        {
            this.gameType = type;
        }

        this.setGameType(this.gameType);
    }

    public void updateBlockRemoving()
    {
        this.curblockDamage = MinecraftServer.currentTick; // CraftBukkit;

        if (this.receivedFinishDiggingPacket)
        {
            int i = this.curblockDamage - this.initialBlockDamage;
            IBlockState iblockstate = this.world.getBlockState(this.delayedDestroyPos);

            if (iblockstate.getBlock().isAir(iblockstate, world, delayedDestroyPos))
            {
                this.receivedFinishDiggingPacket = false;
            }
            else
            {
                float f = iblockstate.getPlayerRelativeBlockHardness(this.player, this.player.world, this.delayedDestroyPos) * (float)(i + 1);
                int j = (int)(f * 10.0F);

                if (j != this.durabilityRemainingOnBlock)
                {
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), this.delayedDestroyPos, j);
                    this.durabilityRemainingOnBlock = j;
                }

                if (f >= 1.0F)
                {
                    this.receivedFinishDiggingPacket = false;
                    this.tryHarvestBlock(this.delayedDestroyPos);
                }
            }
        }
        else if (this.isDestroyingBlock)
        {
            IBlockState iblockstate1 = this.world.getBlockState(this.destroyPos);

            if (iblockstate1.getBlock().isAir(iblockstate1, world, destroyPos))
            {
                this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            }
            else
            {
                int k = this.curblockDamage - this.initialDamage;
                float f1 = iblockstate1.getPlayerRelativeBlockHardness(this.player, this.player.world, this.destroyPos) * (float)(k + 1); // Forge: Fix network break progress using wrong position
                int l = (int)(f1 * 10.0F);

                if (l != this.durabilityRemainingOnBlock)
                {
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, l);
                    this.durabilityRemainingOnBlock = l;
                }
            }
        }
    }

    public void onBlockClicked(BlockPos pos, EnumFacing side)
    {
        // CraftBukkit start
        PlayerInteractEvent bevent = CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, pos, side, this.player.inventory.getCurrentItem(), EnumHand.MAIN_HAND);
        if (bevent.isCancelled()) {
            // Let the client know the block still exists
            ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, pos));
            // Update any tile entity data for this block
            TileEntity tileentity = this.world.getTileEntity(pos);
            if (tileentity != null) {
                this.player.connection.sendPacket(tileentity.getUpdatePacket());
            }
            return;
        }
        // CraftBukkit end
        double reachDist = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock event = net.minecraftforge.common.ForgeHooks.onLeftClickBlock(player, pos, side, net.minecraftforge.common.ForgeHooks.rayTraceEyeHitVec(player, reachDist + 1));
        if (event.isCanceled())
        {
            // Restore block and te data
            player.connection.sendPacket(new SPacketBlockChange(world, pos));
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            return;
        }

        if (this.isCreative())
        {
            if (!this.world.extinguishFire((EntityPlayer)null, pos, side))
            {
                this.tryHarvestBlock(pos);
            }
        }
        else
        {
            IBlockState iblockstate = this.world.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (this.gameType.hasLimitedInteractions())
            {
                if (this.gameType == GameType.SPECTATOR)
                {
                    return;
                }

                if (!this.player.isAllowEdit())
                {
                    ItemStack itemstack = this.player.getHeldItemMainhand();

                    if (itemstack.isEmpty())
                    {
                        return;
                    }

                    if (!itemstack.canDestroy(block))
                    {
                        return;
                    }
                }
            }

            this.initialDamage = this.curblockDamage;
            float f = 1.0F;

            // CraftBukkit start - Swings at air do *NOT* exist.
            if (bevent.useInteractedBlock() == Event.Result.DENY) {
                // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                IBlockState data = this.world.getBlockState(pos);
                if (block == Blocks.OAK_DOOR) {
                    // For some reason *BOTH* the bottom/top part have to be marked updated.
                    boolean bottom = data.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER;
                    ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, pos));
                    ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, bottom ? pos.up() : pos.down()));
                } else if (block == Blocks.TRAPDOOR) {
                    ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, pos));
                }
            } else if (iblockstate.getMaterial() != Material.AIR) {
                if (event.getUseBlock() != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
                {
                    block.onBlockClicked(this.world, pos, this.player);
                    this.world.extinguishFire((EntityPlayer)null, pos, side);
                }
                else
                {
                    // Restore block and te data
                    player.connection.sendPacket(new SPacketBlockChange(world, pos));
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                }
                f = iblockstate.getPlayerRelativeBlockHardness(this.player, this.player.world, pos);
            }

            if (bevent.useItemInHand() == Event.Result.DENY) {
                // If we 'insta destroyed' then the client needs to be informed.
                if (f > 1.0f) {
                    ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, pos));
                }
                return;
            }
            org.bukkit.event.block.BlockDamageEvent blockEvent = CraftEventFactory.callBlockDamageEvent(this.player, pos.getX(), pos.getY(), pos.getZ(), this.player.inventory.getCurrentItem(), f >= 1.0f);

            if (blockEvent.isCancelled()) {
                // Let the client know the block still exists
                ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, pos));
                return;
            }

            if (blockEvent.getInstaBreak()) {
                f = 2.0f;
            }
            // CraftBukkit end
            if (event.getUseItem() == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
            {
                if (f >= 1.0F)
                {
                    // Restore block and te data
                    player.connection.sendPacket(new SPacketBlockChange(world, pos));
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                }
                return;
            }

            if (!iblockstate.getBlock().isAir(iblockstate, world, pos) && f >= 1.0F)
            {
                this.tryHarvestBlock(pos);
            }
            else
            {
                this.isDestroyingBlock = true;
                this.destroyPos = pos;
                int i = (int)(f * 10.0F);
                this.world.sendBlockBreakProgress(this.player.getEntityId(), pos, i);
                this.durabilityRemainingOnBlock = i;
            }
        }
    }

    public void blockRemoving(BlockPos pos)
    {
        if (pos.equals(this.destroyPos))
        {
            this.curblockDamage = MinecraftServer.currentTick; // CraftBukkit
            int i = this.curblockDamage - this.initialDamage;
            IBlockState iblockstate = this.world.getBlockState(pos);

            if (!iblockstate.getBlock().isAir(iblockstate, world, pos))
            {
                float f = iblockstate.getPlayerRelativeBlockHardness(this.player, this.player.world, pos) * (float)(i + 1);

                if (f >= 0.7F)
                {
                    this.isDestroyingBlock = false;
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), pos, -1);
                    this.tryHarvestBlock(pos);
                }
                else if (!this.receivedFinishDiggingPacket)
                {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.delayedDestroyPos = pos;
                    this.initialBlockDamage = this.initialDamage;
                }
            }
        // Akarin start
        } else {
            this.player.connection.sendPacket(new SPacketBlockChange(this.world, pos));
            TileEntity tileentity = this.world.getTileEntity(pos);
            if (tileentity != null) {
                this.player.connection.sendPacket(tileentity.getUpdatePacket());
            }
        }
        // Akarin end
    }

    public void cancelDestroyingBlock()
    {
        this.isDestroyingBlock = false;
        this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, -1);
    }

    private boolean removeBlock(BlockPos pos)
    {
        return removeBlock(pos, false);
    }

    private boolean removeBlock(BlockPos pos, boolean canHarvest)
    {
        IBlockState iblockstate = this.world.getBlockState(pos);
        boolean flag = iblockstate.getBlock().removedByPlayer(iblockstate, world, pos, player, canHarvest);

        if (flag)
        {
            iblockstate.getBlock().onBlockDestroyedByPlayer(this.world, pos, iblockstate);
        }

        return flag;
    }

    public boolean tryHarvestBlock(BlockPos pos)
    {
        int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(world, gameType, player, pos);
        if (exp == -1)
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = this.world.getBlockState(pos);
            TileEntity tileentity = this.world.getTileEntity(pos);
            Block block = iblockstate.getBlock();

            if ((block instanceof BlockCommandBlock || block instanceof BlockStructure) && !this.player.canUseCommandBlock())
            {
                this.world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
                return false;
            }
            else
            {
                ItemStack stack = player.getHeldItemMainhand();
                if (!stack.isEmpty() && stack.getItem().onBlockStartBreak(stack, pos, player)) return false;

                this.world.playEvent(this.player, 2001, pos, Block.getStateId(iblockstate));
                boolean flag1 = false;

                if (this.isCreative())
                {
                    flag1 = this.removeBlock(pos);
                    this.player.connection.sendPacket(new SPacketBlockChange(this.world, pos));
                }
                else
                {
                    ItemStack itemstack1 = this.player.getHeldItemMainhand();
                    ItemStack itemstack2 = itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy();
                    boolean flag = iblockstate.getBlock().canHarvestBlock(world, pos, player);

                    if (!itemstack1.isEmpty())
                    {
                        itemstack1.onBlockDestroyed(this.world, iblockstate, pos, this.player);
                        if (itemstack1.isEmpty()) net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(this.player, itemstack2, EnumHand.MAIN_HAND);
                    }

                    flag1 = this.removeBlock(pos, flag);
                    if (flag1 && flag)
                    {
                        iblockstate.getBlock().harvestBlock(this.world, this.player, pos, iblockstate, tileentity, itemstack2);
                    }
                }

                // Drop experience
                if (!this.isCreative() && flag1 && exp > 0)
                {
                    iblockstate.getBlock().dropXpOnBlockBreak(world, pos, exp);
                }
                return flag1;
            }
        }
    }

    public EnumActionResult processRightClick(EntityPlayer player, World worldIn, ItemStack stack, EnumHand hand)
    {
        if (this.gameType == GameType.SPECTATOR)
        {
            return EnumActionResult.PASS;
        }
        else if (player.getCooldownTracker().hasCooldown(stack.getItem()))
        {
            return EnumActionResult.PASS;
        }
        else
        {
            EnumActionResult cancelResult = net.minecraftforge.common.ForgeHooks.onItemRightClick(player, hand);
            if (cancelResult != null) return cancelResult;
            int i = stack.getCount();
            int j = stack.getMetadata();
            ItemStack copyBeforeUse = stack.copy();
            ActionResult<ItemStack> actionresult = stack.useItemRightClick(worldIn, player, hand);
            ItemStack itemstack = actionresult.getResult();

            if (itemstack == stack && itemstack.getCount() == i && itemstack.getMaxItemUseDuration() <= 0 && itemstack.getMetadata() == j)
            {
                return actionresult.getType();
            }
            else if (actionresult.getType() == EnumActionResult.FAIL && itemstack.getMaxItemUseDuration() > 0 && !player.isHandActive())
            {
                return actionresult.getType();
            }
            else
            {
                player.setHeldItem(hand, itemstack);

                if (this.isCreative())
                {
                    itemstack.setCount(i);

                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(j);
                    }
                }

                if (itemstack.isEmpty())
                {
                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, hand);
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }

                if (!player.isHandActive())
                {
                    ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                }

                return actionresult.getType();
            }
        }
    }

    public EnumActionResult processRightClickBlock(EntityPlayer player, World worldIn, ItemStack stack, EnumHand hand, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        // Akarin start
        IBlockState blockdata = world.getBlockState(pos);
        if (blockdata.getBlock() != Blocks.AIR) {
            boolean bypass;
            boolean cancelledBlock = false;
            if (this.gameType == GameType.SPECTATOR) {
                TileEntity tileentity = world.getTileEntity(pos);
                cancelledBlock = !(tileentity instanceof ILockableContainer || tileentity instanceof IInventory);
            }
            if (!player.getBukkitEntity().isOp() && stack != null && stack.getItem() instanceof ItemBlock) {
                cancelledBlock = true;
            }
            org.bukkit.event.player.PlayerInteractEvent cbEvent = CraftEventFactory.callPlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, pos, facing, stack, cancelledBlock, hand);
            this.firedInteract = true;
            boolean bl3 = this.interactResult = cbEvent.useItemInHand() == Event.Result.DENY;
            if (cbEvent.useInteractedBlock() == Event.Result.DENY) {
                if (blockdata.getBlock() instanceof BlockDoor) {
                    boolean bottom = blockdata.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER;
                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketBlockChange(world, bottom ? pos.up() : pos.down()));
                } else if (blockdata.getBlock() instanceof BlockCake) {
                    ((EntityPlayerMP) player).getBukkitEntity().sendHealthUpdate(); // SPIGOT-1341 - reset health for cake
                }
                ((EntityPlayerMP) player).getBukkitEntity().updateInventory(); // SPIGOT-2867
                return cbEvent.useItemInHand() != Event.Result.ALLOW ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
            }
        } else if (this.gameType == GameType.SPECTATOR) {
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity instanceof ILockableContainer) {
                Block block = world.getBlockState(pos).getBlock();
                ILockableContainer itileinventory = (ILockableContainer) tileentity;

                if (itileinventory instanceof TileEntityChest && block instanceof BlockChest) {
                    itileinventory = ((BlockChest) block).getLockableContainer(world, pos);
                }

                if (itileinventory != null) {
                    player.displayGUIChest(itileinventory);
                    return EnumActionResult.SUCCESS;
                }
            } else if (tileentity instanceof IInventory) {
                player.displayGUIChest((IInventory) tileentity);
                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.PASS;
            }
            double reachDist = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
            net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event = net.minecraftforge.common.ForgeHooks.onRightClickBlock(player, hand, pos, facing, net.minecraftforge.common.ForgeHooks.rayTraceEyeHitVec(player, reachDist + 1));
            if (event.isCanceled()) {
                return event.getCancellationResult();
            }
            EnumActionResult result = EnumActionResult.PASS;
            if (event.getUseItem() != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
            {
                result = stack.onItemUseFirst(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
                if (result != EnumActionResult.PASS) return result ;
            }
            boolean bypass = player.getHeldItemMainhand().doesSneakBypassUse(worldIn, pos, player) && player.getHeldItemOffhand().doesSneakBypassUse(worldIn, pos, player);

            if (!player.isSneaking() || bypass || event.getUseBlock() == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW)
            {
                IBlockState iblockstate = worldIn.getBlockState(pos);
                if(event.getUseBlock() != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
                if (iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, hand, facing, hitX, hitY, hitZ))
                {
                    result = EnumActionResult.SUCCESS;
                }
            }
            if (stack.isEmpty())
            {
                return EnumActionResult.PASS;
            }
            else if (player.getCooldownTracker().hasCooldown(stack.getItem()))
            {
                return EnumActionResult.PASS;
            }
            else
            {
                if (stack.getItem() instanceof ItemBlock && !player.canUseCommandBlock())
                {
                    Block block = ((ItemBlock)stack.getItem()).getBlock();

                    if (block instanceof BlockCommandBlock || block instanceof BlockStructure)
                    {
                        return EnumActionResult.FAIL;
                    }
                }

                if (this.isCreative())
                {
                    int j = stack.getMetadata();
                    int i = stack.getCount();
                    if (result != EnumActionResult.SUCCESS && event.getUseItem() != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY
                            || result == EnumActionResult.SUCCESS && event.getUseItem() == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW) {
                    EnumActionResult enumactionresult = stack.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
                    stack.setItemDamage(j);
                    stack.setCount(i);
                    return enumactionresult;
                    } else return result;
                }
                else
                {
                    if (result != EnumActionResult.SUCCESS && event.getUseItem() != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY
                            || result == EnumActionResult.SUCCESS && event.getUseItem() == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW) {
                        ItemStack copyBeforeUse = stack.copy();
                        result = stack.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
                        if (stack.isEmpty()) net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, hand);
                    }
                    return result;
                }
            }
    }

    public void setWorld(WorldServer serverWorld)
    {
        this.world = serverWorld;
    }

    @Deprecated // use the attribute directly
    public double getBlockReachDistance()
    {
        return player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
    }

    @Deprecated // use an attribute modifier
    public void setBlockReachDistance(double distance)
    {
        player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(distance);
    }
}