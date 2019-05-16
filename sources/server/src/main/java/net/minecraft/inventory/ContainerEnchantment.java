package net.minecraft.inventory;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryEnchanting;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerEnchantment extends Container
{
    public IInventory tableInventory;
    private World worldPointer;
    private final BlockPos position;
    private final Random rand;
    public int xpSeed;
    public int[] enchantLevels;
    public int[] enchantClue;
    public int[] worldClue;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private Player player;
    // CraftBukkit end

    @SideOnly(Side.CLIENT)
    public ContainerEnchantment(InventoryPlayer playerInv, World worldIn)
    {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }

    public ContainerEnchantment(InventoryPlayer playerInv, World worldIn, BlockPos pos)
    {
        this.tableInventory = new InventoryBasic("Enchant", true, 2)
        {
            public int getInventoryStackLimit()
            {
                return 64;
            }
            public void markDirty()
            {
                super.markDirty();
                ContainerEnchantment.this.onCraftMatrixChanged(this);
            }
            // CraftBukkit start
            @Override
            public Location getLocation() {
                return new org.bukkit.Location(worldPointer.getWorld(), position.getX(), position.getY(), position.getZ());
            }
            // CraftBukkit end
        };
        this.rand = new Random();
        this.enchantLevels = new int[3];
        this.enchantClue = new int[] { -1, -1, -1};
        this.worldClue = new int[] { -1, -1, -1};
        this.worldPointer = worldIn;
        this.position = pos;
        this.xpSeed = playerInv.player.getXPSeed();
        this.addSlotToContainer(new Slot(this.tableInventory, 0, 15, 47)
        {
            public boolean isItemValid(ItemStack stack)
            {
                return true;
            }
            public int getSlotStackLimit()
            {
                return 1;
            }
        });
        this.addSlotToContainer(new Slot(this.tableInventory, 1, 35, 47)
        {
            java.util.List<ItemStack> ores = net.minecraftforge.oredict.OreDictionary.getOres("gemLapis");
            public boolean isItemValid(ItemStack stack)
            {
                for (ItemStack ore : ores)
                    if (net.minecraftforge.oredict.OreDictionary.itemMatches(ore, stack, false)) return true;
                return false;
            }
        });

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
        player = (Player) playerInv.player.getBukkitEntity(); // CraftBukkit
    }

    protected void broadcastData(IContainerListener crafting)
    {
        crafting.sendWindowProperty(this, 0, this.enchantLevels[0]);
        crafting.sendWindowProperty(this, 1, this.enchantLevels[1]);
        crafting.sendWindowProperty(this, 2, this.enchantLevels[2]);
        crafting.sendWindowProperty(this, 3, this.xpSeed & -16);
        crafting.sendWindowProperty(this, 4, this.enchantClue[0]);
        crafting.sendWindowProperty(this, 5, this.enchantClue[1]);
        crafting.sendWindowProperty(this, 6, this.enchantClue[2]);
        crafting.sendWindowProperty(this, 7, this.worldClue[0]);
        crafting.sendWindowProperty(this, 8, this.worldClue[1]);
        crafting.sendWindowProperty(this, 9, this.worldClue[2]);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        this.broadcastData(listener);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);
            this.broadcastData(icontainerlistener);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        if (id >= 0 && id <= 2)
        {
            this.enchantLevels[id] = data;
        }
        else if (id == 3)
        {
            this.xpSeed = data;
        }
        else if (id >= 4 && id <= 6)
        {
            this.enchantClue[id - 4] = data;
        }
        else if (id >= 7 && id <= 9)
        {
            this.worldClue[id - 7] = data;
        }
        else
        {
            super.updateProgressBar(id, data);
        }
    }

    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        if (inventoryIn == this.tableInventory)
        {
            ItemStack itemstack = inventoryIn.getStackInSlot(0);

            if (!itemstack.isEmpty()) // CraftBukkit - relax condition
            {
                if (!this.worldPointer.isRemote)
                {
                    int l = 0;
                    float power = 0;

                    for (int j = -1; j <= 1; ++j)
                    {
                        for (int k = -1; k <= 1; ++k)
                        {
                            if ((j != 0 || k != 0) && this.worldPointer.isAirBlock(this.position.add(k, 0, j)) && this.worldPointer.isAirBlock(this.position.add(k, 1, j)))
                            {
                                power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k * 2, 0, j * 2));
                                power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k * 2, 1, j * 2));
                                if (k != 0 && j != 0)
                                {
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k * 2, 0, j));
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k * 2, 1, j));
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k, 0, j * 2));
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k, 1, j * 2));
                                }
                            }
                        }
                    }

                    this.rand.setSeed((long)this.xpSeed);

                    for (int i1 = 0; i1 < 3; ++i1)
                    {
                        this.enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(this.rand, i1, (int)power, itemstack);
                        this.enchantClue[i1] = -1;
                        this.worldClue[i1] = -1;

                        if (this.enchantLevels[i1] < i1 + 1)
                        {
                            this.enchantLevels[i1] = 0;
                        }
                        this.enchantLevels[i1] = net.minecraftforge.event.ForgeEventFactory.onEnchantmentLevelSet(worldPointer, position, i1, (int)power, itemstack, enchantLevels[i1]);
                    }

                    for (int j1 = 0; j1 < 3; ++j1)
                    {
                        if (this.enchantLevels[j1] > 0)
                        {
                            List<EnchantmentData> list = this.getEnchantmentList(itemstack, j1, this.enchantLevels[j1]);

                            if (list != null && !list.isEmpty())
                            {
                                EnchantmentData enchantmentdata = list.get(this.rand.nextInt(list.size()));
                                this.enchantClue[j1] = Enchantment.getEnchantmentID(enchantmentdata.enchantment);
                                this.worldClue[j1] = enchantmentdata.enchantmentLevel;
                            }
                        }
                    }

                    // CraftBukkit start
                    CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);
                    org.bukkit.enchantments.EnchantmentOffer[] offers = new EnchantmentOffer[3];
                    for (int i = 0; i < 3; ++i) {
                        org.bukkit.enchantments.Enchantment enchantment = (this.enchantClue[i] >= 0) ? org.bukkit.enchantments.Enchantment.getById(this.enchantClue[i]) : null;
                        offers[i] = (enchantment != null) ? new EnchantmentOffer(enchantment, this.worldClue[i], this.enchantLevels[i]) : null;
                    }

                    PrepareItemEnchantEvent event = new PrepareItemEnchantEvent(player, this.getBukkitView(), this.worldPointer.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()), item, offers, (int) power);
                    event.setCancelled(!itemstack.isItemEnchantable());
                    this.worldPointer.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        for (int i = 0; i < 3; ++i) {
                            this.enchantLevels[i] = 0;
                            this.enchantClue[i] = -1;
                            this.worldClue[i] = -1;
                        }
                        return;
                    }

                    for (int i = 0; i < 3; i++) {
                        EnchantmentOffer offer = event.getOffers()[i];
                        if (offer != null) {
                            this.enchantLevels[i] = offer.getCost();
                            this.enchantClue[i] = offer.getEnchantment().getId();
                            this.worldClue[i] = offer.getEnchantmentLevel();
                        } else {
                            this.enchantLevels[i] = 0;
                            this.enchantClue[i] = -1;
                            this.worldClue[i] = -1;
                        }
                    }
                    // CraftBukkit end
                    this.detectAndSendChanges();
                }
            }
            else
            {
                for (int i = 0; i < 3; ++i)
                {
                    this.enchantLevels[i] = 0;
                    this.enchantClue[i] = -1;
                    this.worldClue[i] = -1;
                }
            }
        }
    }

    public boolean enchantItem(EntityPlayer playerIn, int id)
    {
        ItemStack itemstack = this.tableInventory.getStackInSlot(0);
        ItemStack itemstack1 = this.tableInventory.getStackInSlot(1);
        int i = id + 1;

        if ((itemstack1.isEmpty() || itemstack1.getCount() < i) && !playerIn.capabilities.isCreativeMode)
        {
            return false;
        }
        else if (this.enchantLevels[id] > 0 && !itemstack.isEmpty() && (playerIn.experienceLevel >= i && playerIn.experienceLevel >= this.enchantLevels[id] || playerIn.capabilities.isCreativeMode))
        {
            if (!this.worldPointer.isRemote)
            {
                List<EnchantmentData> list = this.getEnchantmentList(itemstack, id, this.enchantLevels[id]);

                // CraftBukkit start
                if (true || !list.isEmpty())
                {
                    // playerIn.onEnchant(itemstack, i); // Moved down
                	// CraftBukkit end
                    boolean flag = itemstack.getItem() == Items.BOOK;
                    // CraftBukkit start
                    Map<org.bukkit.enchantments.Enchantment, Integer> enchants = new java.util.HashMap<org.bukkit.enchantments.Enchantment, Integer>();
                    for (EnchantmentData instance : list) {
                        enchants.put(org.bukkit.enchantments.Enchantment.getById(Enchantment.getEnchantmentID(instance.enchantment)), instance.enchantmentLevel);
                    }
                    CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);

                    EnchantItemEvent event = new EnchantItemEvent((Player) playerIn.getBukkitEntity(), this.getBukkitView(), this.worldPointer.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()), item, this.enchantLevels[id], enchants, id);
                    this.worldPointer.getServer().getPluginManager().callEvent(event);

                    int level = event.getExpLevelCost();
                    if (event.isCancelled() || (level > playerIn.experienceLevel && !playerIn.capabilities.isCreativeMode) || event.getEnchantsToAdd().isEmpty()) {
                        return false;
                    }
                	// CraftBukkit end

                    if (flag)
                    {
                        itemstack = new ItemStack(Items.ENCHANTED_BOOK);
                        this.tableInventory.setInventorySlotContents(0, itemstack);
                    }

                    // CraftBukkit start
                    for (Map.Entry<org.bukkit.enchantments.Enchantment, Integer> entry : event.getEnchantsToAdd().entrySet()) {
                        try {
                            if (flag) {
                                int enchantId = entry.getKey().getId();
                                if (Enchantment.getEnchantmentByID(enchantId) == null) {
                                    continue;
                                }

                                EnchantmentData weightedrandomenchant = new EnchantmentData(Enchantment.getEnchantmentByID(enchantId), entry.getValue());
                                ItemEnchantedBook.addEnchantment(itemstack, weightedrandomenchant);
                            } else {
                                item.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                            }
                        } catch (IllegalArgumentException e) {
                            /* Just swallow invalid enchantments */
                        }
                    }

                    playerIn.onEnchant(itemstack, i);
                    // CraftBukkit end

                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        itemstack1.shrink(i);

                        if (itemstack1.isEmpty())
                        {
                            this.tableInventory.setInventorySlotContents(1, ItemStack.EMPTY);
                        }
                    }

                    playerIn.addStat(StatList.ITEM_ENCHANTED);

                    if (playerIn instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.ENCHANTED_ITEM.trigger((EntityPlayerMP)playerIn, itemstack, i);
                    }

                    this.tableInventory.markDirty();
                    this.xpSeed = playerIn.getXPSeed();
                    this.onCraftMatrixChanged(this.tableInventory);
                    this.worldPointer.playSound((EntityPlayer)null, this.position, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, this.worldPointer.rand.nextFloat() * 0.1F + 0.9F);
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private List<EnchantmentData> getEnchantmentList(ItemStack stack, int enchantSlot, int level)
    {
        this.rand.setSeed((long)(this.xpSeed + enchantSlot));
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(this.rand, stack, level, false);

        if (stack.getItem() == Items.BOOK && list.size() > 1)
        {
            list.remove(this.rand.nextInt(list.size()));
        }

        return list;
    }

    @SideOnly(Side.CLIENT)
    public int getLapisAmount()
    {
        ItemStack itemstack = this.tableInventory.getStackInSlot(1);
        return itemstack.isEmpty() ? 0 : itemstack.getCount();
    }

    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        // CraftBukkit Start - If an enchantable was opened from a null location, set the world to the player's world, preventing a crash
        if (this.worldPointer == null) {
            this.worldPointer = playerIn.getEntityWorld();
        }
        // CraftBukkit end

        if (!this.worldPointer.isRemote)
        {
            this.clearContainer(playerIn, playerIn.world, this.tableInventory);
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        if (!this.checkReachable) return true; // CraftBukkit
        if (this.worldPointer.getBlockState(this.position).getBlock() != Blocks.ENCHANTING_TABLE)
        {
            return false;
        }
        else
        {
            return playerIn.getDistanceSq((double)this.position.getX() + 0.5D, (double)this.position.getY() + 0.5D, (double)this.position.getZ() + 0.5D) <= 64.0D;
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index == 1)
            {
                if (!this.mergeItemStack(itemstack1, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (itemstack1.getItem() == Items.DYE && EnumDyeColor.byDyeDamage(itemstack1.getMetadata()) == EnumDyeColor.BLUE)
            {
                if (!this.mergeItemStack(itemstack1, 1, 2, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (((Slot)this.inventorySlots.get(0)).getHasStack() || !((Slot)this.inventorySlots.get(0)).isItemValid(itemstack1))
                {
                    return ItemStack.EMPTY;
                }

                if (itemstack1.hasTagCompound())// Forge: Fix MC-17431
                {
                    ((Slot)this.inventorySlots.get(0)).putStack(itemstack1.splitStack(1));
                }
                else if (!itemstack1.isEmpty())
                {
                    ((Slot)this.inventorySlots.get(0)).putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
                    itemstack1.shrink(1);
                }
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
    // CraftBukkit start
    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryEnchanting inventory = new CraftInventoryEnchanting(this.tableInventory);
        bukkitEntity = new CraftInventoryView(this.player, inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}