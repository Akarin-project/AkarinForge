/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Set;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMerchant;
import org.bukkit.craftbukkit.v1_12_R1.inventory.InventoryWrapper;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;

public class CraftHumanEntity
extends CraftLivingEntity
implements HumanEntity {
    private CraftInventoryPlayer inventory;
    private final CraftInventory enderChest;
    protected final PermissibleBase perm;
    private boolean op;
    private GameMode mode;

    public CraftHumanEntity(CraftServer server, aed entity) {
        super(server, entity);
        this.perm = new PermissibleBase(this);
        this.mode = server.getDefaultGameMode();
        this.inventory = new CraftInventoryPlayer(entity.bv);
        this.enderChest = new CraftInventory(entity.dl());
    }

    @Override
    public String getName() {
        return this.getHandle().h_();
    }

    @Override
    public PlayerInventory getInventory() {
        return this.inventory;
    }

    @Override
    public EntityEquipment getEquipment() {
        return this.inventory;
    }

    @Override
    public Inventory getEnderChest() {
        return this.enderChest;
    }

    @Override
    public MainHand getMainHand() {
        return this.getHandle().cF() == vo.a ? MainHand.LEFT : MainHand.RIGHT;
    }

    @Override
    public ItemStack getItemInHand() {
        return this.getInventory().getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        this.getInventory().setItemInHand(item);
    }

    @Override
    public ItemStack getItemOnCursor() {
        return CraftItemStack.asCraftMirror(this.getHandle().bv.q());
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        aip stack = CraftItemStack.asNMSCopy(item);
        this.getHandle().bv.g(stack);
        if (this instanceof CraftPlayer) {
            ((oq)this.getHandle()).q();
        }
    }

    @Override
    public boolean isSleeping() {
        return this.getHandle().bK;
    }

    @Override
    public int getSleepTicks() {
        return this.getHandle().c;
    }

    @Override
    public boolean isOp() {
        return this.op;
    }

    @Override
    public boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return this.perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return this.perm.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return this.perm.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }

    @Override
    public void setOp(boolean value) {
        this.op = value;
        this.perm.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }

    @Override
    public GameMode getGameMode() {
        return this.mode;
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }
        this.mode = mode;
    }

    @Override
    public aed getHandle() {
        return (aed)this.entity;
    }

    public void setHandle(aed entity) {
        super.setHandle(entity);
        this.inventory = new CraftInventoryPlayer(entity.bv);
    }

    @Override
    public String toString() {
        return "CraftHumanEntity{id=" + this.getEntityId() + "name=" + this.getName() + '}';
    }

    @Override
    public InventoryView getOpenInventory() {
        return this.getHandle().by.getBukkitView();
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        if (!(this.getHandle() instanceof oq)) {
            return null;
        }
        oq player = (oq)this.getHandle();
        InventoryType type = inventory.getType();
        afr formerContainer = this.getHandle().by;
        tv iinventory = inventory instanceof CraftInventory ? ((CraftInventory)inventory).getInventory() : new InventoryWrapper(inventory);
        switch (type) {
            case PLAYER: 
            case CHEST: 
            case ENDER_CHEST: {
                this.getHandle().a(iinventory);
                break;
            }
            case DISPENSER: {
                if (iinventory instanceof avp) {
                    this.getHandle().a((avp)iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:dispenser");
                break;
            }
            case DROPPER: {
                if (iinventory instanceof avq) {
                    this.getHandle().a((avq)iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:dropper");
                break;
            }
            case FURNACE: {
                if (iinventory instanceof avu) {
                    this.getHandle().a((avu)iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:furnace");
                break;
            }
            case WORKBENCH: {
                this.openCustomInventory(inventory, player, "minecraft:crafting_table");
                break;
            }
            case BREWING: {
                if (iinventory instanceof avk) {
                    this.getHandle().a((avk)iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:brewing_stand");
                break;
            }
            case ENCHANTING: {
                this.openCustomInventory(inventory, player, "minecraft:enchanting_table");
                break;
            }
            case HOPPER: {
                if (iinventory instanceof avw) {
                    this.getHandle().a((avw)iinventory);
                    break;
                }
                if (iinventory instanceof afj) {
                    this.getHandle().a((afj)iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:hopper");
                break;
            }
            case BEACON: {
                if (iinventory instanceof avh) {
                    this.getHandle().a((avh)iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:beacon");
                break;
            }
            case ANVIL: {
                if (iinventory instanceof aon.a) {
                    this.getHandle().a((aon.a)((Object)iinventory));
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:anvil");
                break;
            }
            case SHULKER_BOX: {
                if (iinventory instanceof awb) {
                    this.getHandle().a((awb)iinventory);
                    break;
                }
                this.openCustomInventory(inventory, player, "minecraft:shulker_box");
                break;
            }
            case CREATIVE: 
            case CRAFTING: {
                throw new IllegalArgumentException("Can't open a " + (Object)((Object)type) + " inventory!");
            }
        }
        if (this.getHandle().by == formerContainer) {
            return null;
        }
        this.getHandle().by.checkReachable = false;
        return this.getHandle().by.getBukkitView();
    }

    private void openCustomInventory(Inventory inventory, oq player, String windowType) {
        if (player.a == null) {
            return;
        }
        afr container = new CraftContainer(inventory, this.getHandle(), player.getNextWindowIdCB());
        if ((container = CraftEventFactory.callInventoryOpenEvent(player, container)) == null) {
            return;
        }
        String title = container.getBukkitView().getTitle();
        int size = container.getBukkitView().getTopInventory().getSize();
        if (windowType.equals("minecraft:crafting_table") || windowType.equals("minecraft:anvil") || windowType.equals("minecraft:enchanting_table")) {
            size = 0;
        }
        player.a.a(new ir(container.d, windowType, new ho(title), size));
        this.getHandle().by = container;
        this.getHandle().by.a(player);
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        Block block;
        if (!force && (block = location.getBlock()).getType() != Material.WORKBENCH) {
            return null;
        }
        if (location == null) {
            location = this.getLocation();
        }
        this.getHandle().a(new apr.a(this.getHandle().l, new et(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
        if (force) {
            this.getHandle().by.checkReachable = false;
        }
        return this.getHandle().by.getBukkitView();
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        et pos;
        avj container;
        Block block;
        if (!force && (block = location.getBlock()).getType() != Material.ENCHANTMENT_TABLE) {
            return null;
        }
        if (location == null) {
            location = this.getLocation();
        }
        if ((container = this.getHandle().l.r(pos = new et(location.getBlockX(), location.getBlockY(), location.getBlockZ()))) == null && force) {
            container = new avk();
            container.a(this.getHandle().l);
            container.a(pos);
        }
        this.getHandle().a((avx)container);
        if (force) {
            this.getHandle().by.checkReachable = false;
        }
        return this.getHandle().by.getBukkitView();
    }

    @Override
    public void openInventory(InventoryView inventory) {
        if (!(this.getHandle() instanceof oq)) {
            return;
        }
        if (((oq)this.getHandle()).a == null) {
            return;
        }
        if (this.getHandle().by != this.getHandle().bx) {
            ((oq)this.getHandle()).a.a(new lg(this.getHandle().by.d));
        }
        oq player = (oq)this.getHandle();
        afr container = inventory instanceof CraftInventoryView ? ((CraftInventoryView)inventory).getHandle() : new CraftContainer(inventory, this.getHandle(), player.getNextWindowIdCB());
        if ((container = CraftEventFactory.callInventoryOpenEvent(player, container)) == null) {
            return;
        }
        InventoryType type = inventory.getType();
        String windowType = CraftContainer.getNotchInventoryType(type);
        String title = inventory.getTitle();
        int size = inventory.getTopInventory().getSize();
        player.a.a(new ir(container.d, windowType, new ho(title), size));
        player.by = container;
        player.by.a(player);
    }

    @Override
    public InventoryView openMerchant(Villager villager, boolean force) {
        Preconditions.checkNotNull((Object)villager, (Object)"villager cannot be null");
        return this.openMerchant((Merchant)villager, force);
    }

    @Override
    public InventoryView openMerchant(Merchant merchant, boolean force) {
        amf mcMerchant2;
        amf mcMerchant2;
        Preconditions.checkNotNull((Object)merchant, (Object)"merchant cannot be null");
        if (!force && merchant.isTrading()) {
            return null;
        }
        if (merchant.isTrading()) {
            merchant.getTrader().closeInventory();
        }
        if (merchant instanceof CraftVillager) {
            mcMerchant2 = ((CraftVillager)merchant).getHandle();
        } else if (merchant instanceof CraftMerchant) {
            mcMerchant2 = ((CraftMerchant)merchant).getMerchant();
        } else {
            throw new IllegalArgumentException("Can't open merchant " + merchant.toString());
        }
        mcMerchant2.a_(this.getHandle());
        this.getHandle().a(mcMerchant2);
        return this.getHandle().by.getBukkitView();
    }

    @Override
    public void closeInventory() {
        this.getHandle().p();
    }

    @Override
    public boolean isBlocking() {
        return this.getHandle().cO();
    }

    @Override
    public boolean isHandRaised() {
        return this.getHandle().cG();
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return false;
    }

    @Override
    public int getExpToLevel() {
        return this.getHandle().dh();
    }

    @Override
    public boolean hasCooldown(Material material) {
        Preconditions.checkArgument((boolean)(material != null), (Object)"material");
        return this.getHandle().dt().a(CraftMagicNumbers.getItem(material));
    }

    @Override
    public int getCooldown(Material material) {
        Preconditions.checkArgument((boolean)(material != null), (Object)"material");
        aio.a cooldown = this.getHandle().dt().a.get(CraftMagicNumbers.getItem(material));
        return cooldown == null ? 0 : Math.max(0, cooldown.b - this.getHandle().dt().b);
    }

    @Override
    public void setCooldown(Material material, int ticks) {
        Preconditions.checkArgument((boolean)(material != null), (Object)"material");
        Preconditions.checkArgument((boolean)(ticks >= 0), (Object)"Cannot have negative cooldown");
        this.getHandle().dt().a(CraftMagicNumbers.getItem(material), ticks);
    }

    @Override
    public Entity getShoulderEntityLeft() {
        if (!this.getHandle().dp().b_()) {
            vg shoulder = vi.a(this.getHandle().dp(), this.getHandle().l);
            return shoulder == null ? null : shoulder.getBukkitEntity();
        }
        return null;
    }

    @Override
    public void setShoulderEntityLeft(Entity entity) {
        this.getHandle().h(entity == null ? new fy() : ((CraftEntity)entity).save());
        if (entity != null) {
            entity.remove();
        }
    }

    @Override
    public Entity getShoulderEntityRight() {
        if (!this.getHandle().dq().b_()) {
            vg shoulder = vi.a(this.getHandle().dq(), this.getHandle().l);
            return shoulder == null ? null : shoulder.getBukkitEntity();
        }
        return null;
    }

    @Override
    public void setShoulderEntityRight(Entity entity) {
        this.getHandle().i(entity == null ? new fy() : ((CraftEntity)entity).save());
        if (entity != null) {
            entity.remove();
        }
    }

}

