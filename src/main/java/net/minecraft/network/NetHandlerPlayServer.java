package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.util.concurrent.Futures;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.ServerRecipeBookHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.util.NumberConversions;

public class NetHandlerPlayServer implements INetHandlerPlayServer, ITickable
{
    private static final Logger LOGGER = LogManager.getLogger();
    public final NetworkManager netManager;
    private final MinecraftServer serverController;
    public EntityPlayerMP player;
    private int networkTickCount;
    private long field_194402_f;
    private boolean field_194403_g;
    private long field_194404_h;
    // Akarin start
    private volatile int chatSpamThresholdCount;
    private static final AtomicIntegerFieldUpdater chatSpamField = AtomicIntegerFieldUpdater.newUpdater(NetHandlerPlayServer.class, "chatSpamThresholdCount");
    
    private final CraftServer server;
    private volatile boolean processedDisconnect;
    private int lastTick = MinecraftServer.currentTick;
    private int allowedPlayerTicks = 1;
    private int lastDropTick = MinecraftServer.currentTick;
    private int lastBookTick  = MinecraftServer.currentTick;
    private int dropCount = 0;
    private static final int SURVIVAL_PLACE_DISTANCE_SQUARED = 6 * 6;
    private static final int CREATIVE_PLACE_DISTANCE_SQUARED = 7 * 7;

    // Get position of last block hit for BlockDamageLevel.STOPPED
    private double lastPosX = Double.MAX_VALUE;
    private double lastPosY = Double.MAX_VALUE;
    private double lastPosZ = Double.MAX_VALUE;
    private float lastPitch = Float.MAX_VALUE;
    private float lastYaw = Float.MAX_VALUE;
    private boolean justTeleported = false;
    private boolean hasMoved; // Spigot

    public CraftPlayer getPlayer() {
        return (this.player == null) ? null : (CraftPlayer) this.player.getBukkitEntity();
    }
    private final static HashSet<Integer> invalidItems = new HashSet<Integer>(java.util.Arrays.asList(8, 9, 10, 11, 26, 34, 36, 43, 51, 55, 59, 62, 63, 64, 68, 71, 74, 75, 83, 90, 92, 93, 94, 104, 105, 115, 117, 118, 119, 125, 127, 132, 140, 141, 142, 144)); // TODO: Check after every update.
    // Akarin end
    private int itemDropThreshold;
    private final IntHashMap<Short> pendingTransactions = new IntHashMap<Short>();
    private double firstGoodX;
    private double firstGoodY;
    private double firstGoodZ;
    private double lastGoodX;
    private double lastGoodY;
    private double lastGoodZ;
    private Entity lowestRiddenEnt;
    private double lowestRiddenX;
    private double lowestRiddenY;
    private double lowestRiddenZ;
    private double lowestRiddenX1;
    private double lowestRiddenY1;
    private double lowestRiddenZ1;
    private Vec3d targetPos;
    private int teleportId;
    private int lastPositionUpdate;
    private boolean floating;
    private int floatingTickCount;
    private boolean vehicleFloating;
    private int vehicleFloatingTickCount;
    private int movePacketCounter;
    private int lastMovePacketCounter;
    private ServerRecipeBookHelper field_194309_H = new ServerRecipeBookHelper();

    public NetHandlerPlayServer(MinecraftServer server, NetworkManager networkManagerIn, EntityPlayerMP playerIn)
    {
        this.server = server.server; // Akarin
        this.serverController = server;
        this.netManager = networkManagerIn;
        networkManagerIn.setNetHandler(this);
        this.player = playerIn;
        playerIn.connection = this;
    }

    public void update()
    {
        this.captureCurrentPosition();
        this.player.onUpdateEntity();
        this.player.setPositionAndRotation(this.firstGoodX, this.firstGoodY, this.firstGoodZ, this.player.rotationYaw, this.player.rotationPitch);
        ++this.networkTickCount;
        this.lastMovePacketCounter = this.movePacketCounter;

        if (this.floating)
        {
            if (++this.floatingTickCount > 80)
            {
                LOGGER.warn("{} was kicked for floating too long!", (Object)this.player.getName());
                this.disconnect(new TextComponentTranslation("multiplayer.disconnect.flying", new Object[0]));
                return;
            }
        }
        else
        {
            this.floating = false;
            this.floatingTickCount = 0;
        }

        this.lowestRiddenEnt = this.player.getLowestRidingEntity();

        if (this.lowestRiddenEnt != this.player && this.lowestRiddenEnt.getControllingPassenger() == this.player)
        {
            this.lowestRiddenX = this.lowestRiddenEnt.posX;
            this.lowestRiddenY = this.lowestRiddenEnt.posY;
            this.lowestRiddenZ = this.lowestRiddenEnt.posZ;
            this.lowestRiddenX1 = this.lowestRiddenEnt.posX;
            this.lowestRiddenY1 = this.lowestRiddenEnt.posY;
            this.lowestRiddenZ1 = this.lowestRiddenEnt.posZ;

            if (this.vehicleFloating && this.player.getLowestRidingEntity().getControllingPassenger() == this.player)
            {
                if (++this.vehicleFloatingTickCount > 80)
                {
                    LOGGER.warn("{} was kicked for floating a vehicle too long!", (Object)this.player.getName());
                    this.disconnect(new TextComponentTranslation("multiplayer.disconnect.flying", new Object[0]));
                    return;
                }
            }
            else
            {
                this.vehicleFloating = false;
                this.vehicleFloatingTickCount = 0;
            }
        }
        else
        {
            this.lowestRiddenEnt = null;
            this.vehicleFloating = false;
            this.vehicleFloatingTickCount = 0;
        }

        this.serverController.profiler.startSection("keepAlive");
        long i = this.currentTimeMillis();

        if (i - this.field_194402_f >= 25000L) // Akarin
        {
            if (this.field_194403_g)
            {
                this.disconnect(new TextComponentTranslation("disconnect.timeout", new Object[0]));
            }
            else
            {
                this.field_194403_g = true;
                this.field_194402_f = i;
                this.field_194404_h = i;
                this.sendPacket(new SPacketKeepAlive(this.field_194404_h));
            }
        }

        this.serverController.profiler.endSection();

        // Akarin start
        for (int spam; (spam = this.chatSpamThresholdCount) > 0 && !chatSpamField.compareAndSet(this, spam, spam - 1); ) ;
        /*
        if (this.chatSpamThresholdCount > 0)
        {
            --this.chatSpamThresholdCount;
        }
        */
        // Akarin end

        if (this.itemDropThreshold > 0)
        {
            --this.itemDropThreshold;
        }

        if (this.player.getLastActiveTime() > 0L && this.serverController.getMaxPlayerIdleMinutes() > 0 && MinecraftServer.getCurrentTimeMillis() - this.player.getLastActiveTime() > (long)(this.serverController.getMaxPlayerIdleMinutes() * 1000 * 60))
        {
            this.player.markPlayerActive(); // Akarin - SPIGOT-854
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.idling", new Object[0]));
        }
    }

    public void captureCurrentPosition() // Akarin
    {
        this.firstGoodX = this.player.posX;
        this.firstGoodY = this.player.posY;
        this.firstGoodZ = this.player.posZ;
        this.lastGoodX = this.player.posX;
        this.lastGoodY = this.player.posY;
        this.lastGoodZ = this.player.posZ;
    }

    public NetworkManager getNetworkManager()
    {
        return this.netManager;
    }

    // Akarin start
    public void disconnect(ITextComponent ichatbasecomponent) {
        disconnect(CraftChatMessage.fromComponent(ichatbasecomponent, TextFormatting.WHITE));
    }

    public void disconnect(String s)
    {
        // Akarin start - fire PlayerKickEvent
        if (this.processedDisconnect) {
            return;
        }
        String leaveMessage = TextFormatting.YELLOW + this.player.getName() + " left the game.";

        PlayerKickEvent event = new PlayerKickEvent(this.server.getPlayer(this.player), s, leaveMessage);

        if (this.server.getServer().isServerRunning()) {
            this.server.getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            // Do not kick the player
            return;
        }
        // Send the possibly modified leave message
        s = event.getReason();
        final TextComponentString textComponent = new TextComponentString(s);

        this.netManager.sendPacket(new SPacketDisconnect(textComponent), new GenericFutureListener < Future <? super Void >> ()
        {
            public void operationComplete(Future <? super Void > p_operationComplete_1_) throws Exception
            {
                NetHandlerPlayServer.this.netManager.closeChannel(textComponent);
            }
        });
        this.onDisconnect(textComponent);
        this.netManager.disableAutoRead();
        this.serverController.addScheduledTask(new Runnable() {
            public void run() {
                NetHandlerPlayServer.this.netManager.checkDisconnected();
            }
        });
        // Akarin end
    }

    public void processInput(CPacketInput packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        this.player.setEntityActionState(packetIn.getStrafeSpeed(), packetIn.getForwardSpeed(), packetIn.isJumping(), packetIn.isSneaking());
    }

    private static boolean isMovePlayerPacketInvalid(CPacketPlayer packetIn)
    {
        if (Doubles.isFinite(packetIn.getX(0.0D)) && Doubles.isFinite(packetIn.getY(0.0D)) && Doubles.isFinite(packetIn.getZ(0.0D)) && Floats.isFinite(packetIn.getPitch(0.0F)) && Floats.isFinite(packetIn.getYaw(0.0F)))
        {
            return Math.abs(packetIn.getX(0.0D)) > 3.0E7D || Math.abs(packetIn.getY(0.0D)) > 3.0E7D || Math.abs(packetIn.getZ(0.0D)) > 3.0E7D;
        }
        else
        {
            return true;
        }
    }

    private static boolean isMoveVehiclePacketInvalid(CPacketVehicleMove packetIn)
    {
        return !Doubles.isFinite(packetIn.getX()) || !Doubles.isFinite(packetIn.getY()) || !Doubles.isFinite(packetIn.getZ()) || !Floats.isFinite(packetIn.getPitch()) || !Floats.isFinite(packetIn.getYaw());
    }

    public void processVehicleMove(CPacketVehicleMove packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());

        if (isMoveVehiclePacketInvalid(packetIn))
        {
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_vehicle_movement", new Object[0]));
        }
        else
        {
            Entity entity = this.player.getLowestRidingEntity();

            if (entity != this.player && entity.getControllingPassenger() == this.player && entity == this.lowestRiddenEnt)
            {
                WorldServer worldserver = this.player.getServerWorld();
                double d0 = entity.posX;
                double d1 = entity.posY;
                double d2 = entity.posZ;
                double d3 = packetIn.getX();
                double d4 = packetIn.getY();
                double d5 = packetIn.getZ();
                float f = packetIn.getYaw();
                float f1 = packetIn.getPitch();
                double d6 = d3 - this.lowestRiddenX;
                double d7 = d4 - this.lowestRiddenY;
                double d8 = d5 - this.lowestRiddenZ;
                double d9 = entity.motionX * entity.motionX + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ;
                double d10 = d6 * d6 + d7 * d7 + d8 * d8;

                // Akarin start - handle custom speeds and skipped ticks
                this.allowedPlayerTicks += (System.currentTimeMillis() / 50) - this.lastTick;
                this.allowedPlayerTicks = Math.max(this.allowedPlayerTicks, 1);
                this.lastTick = (int) (System.currentTimeMillis() / 50);

                ++this.movePacketCounter;
                int i = this.movePacketCounter - this.lastMovePacketCounter;
                if (i > Math.max(this.allowedPlayerTicks, 5)) {
                    NetHandlerPlayServer.LOGGER.debug(this.player.getName() + " is sending move packets too frequently (" + i + " packets since last tick)");
                    i = 1;
                }

                if (d10 > 0) {
                    allowedPlayerTicks -= 1;
                } else {
                    allowedPlayerTicks = 20;
                }
                float speed;
                if (player.capabilities.isFlying) {
                    speed = player.capabilities.flySpeed * 20f;
                } else {
                    speed = player.capabilities.walkSpeed * 10f;
                }
                speed *= 2f;

                if (d10 - d9 > Math.max(100.0D, Math.pow((double) (org.spigotmc.SpigotConfig.movedTooQuicklyMultiplier * (float) i * speed), 2)) && (!this.serverController.isSinglePlayer() || !this.serverController.getServerOwner().equals(entity.getName())))
                // Akarin end
                {
                    LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", entity.getName(), this.player.getName(), Double.valueOf(d6), Double.valueOf(d7), Double.valueOf(d8));
                    this.netManager.sendPacket(new SPacketMoveVehicle(entity));
                    return;
                }

                boolean flag = worldserver.getCollisionBoxes(entity, entity.getEntityBoundingBox().shrink(0.0625D)).isEmpty();
                d6 = d3 - this.lowestRiddenX1;
                d7 = d4 - this.lowestRiddenY1 - 1.0E-6D;
                d8 = d5 - this.lowestRiddenZ1;
                entity.move(MoverType.PLAYER, d6, d7, d8);
                double d11 = d7;
                d6 = d3 - entity.posX;
                d7 = d4 - entity.posY;

                if (d7 > -0.5D || d7 < 0.5D)
                {
                    d7 = 0.0D;
                }

                d8 = d5 - entity.posZ;
                d10 = d6 * d6 + d7 * d7 + d8 * d8;
                boolean flag1 = false;

                if (d10 > 0.0625D)
                {
                    flag1 = true;
                    LOGGER.warn("{} moved wrongly!", (Object)entity.getName());
                }

                entity.setPositionAndRotation(d3, d4, d5, f, f1);
                this.player.setPositionAndRotation(d3, d4, d5, this.player.rotationYaw, this.player.rotationPitch); // Forge - Resync player position on vehicle moving
                boolean flag2 = worldserver.getCollisionBoxes(entity, entity.getEntityBoundingBox().shrink(0.0625D)).isEmpty();

                if (flag && (flag1 || !flag2))
                {
                    entity.setPositionAndRotation(d0, d1, d2, f, f1);
                    this.player.setPositionAndRotation(d0, d1, d2, this.player.rotationYaw, this.player.rotationPitch); // Forge - Resync player position on vehicle moving
                    this.netManager.sendPacket(new SPacketMoveVehicle(entity));
                    return;
                }
                // Akarin start - fire PlayerMoveEvent
                Player player = this.getPlayer();
                if ( !hasMoved )
                {
                    Location curPos = player.getLocation();
                    lastPosX = curPos.getX();
                    lastPosY = curPos.getY();
                    lastPosZ = curPos.getZ();
                    lastYaw = curPos.getYaw();
                    lastPitch = curPos.getPitch();
                    hasMoved = true;
                }
                Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
                Location to = player.getLocation().clone(); // Start off the To location as the Players current location.

                // If the packet contains movement information then we update the To location with the correct XYZ.
                to.setX(packetIn.getX());
                to.setY(packetIn.getY());
                to.setZ(packetIn.getZ());


                // If the packet contains look information then we update the To location with the correct Yaw & Pitch.
                to.setYaw(packetIn.getYaw());
                to.setPitch(packetIn.getPitch());

                // Prevent 40 event-calls for less than a single pixel of movement >.>
                double delta = Math.pow(this.lastPosX - to.getX(), 2) + Math.pow(this.lastPosY - to.getY(), 2) + Math.pow(this.lastPosZ - to.getZ(), 2);
                float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

                if ((delta > 1f / 256 || deltaAngle > 10f) && !this.player.isMovementBlocked()) {
                    this.lastPosX = to.getX();
                    this.lastPosY = to.getY();
                    this.lastPosZ = to.getZ();
                    this.lastYaw = to.getYaw();
                    this.lastPitch = to.getPitch();

                    // Skip the first time we do this
                    if (true) { // Spigot - don't skip any move events
                        Location oldTo = to.clone();
                        PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);
                        this.server.getPluginManager().callEvent(event);

                        // If the event is cancelled we move the player back to their old location.
                        if (event.isCancelled()) {
                            teleport(from);
                            return;
                        }

                        // If a Plugin has changed the To destination then we teleport the Player
                        // there to avoid any 'Moved wrongly' or 'Moved too quickly' errors.
                        // We only do this if the Event was not cancelled.
                        if (!oldTo.equals(event.getTo()) && !event.isCancelled()) {
                            this.player.getBukkitEntity().teleport(event.getTo(), TeleportCause.PLUGIN);
                            return;
                        }

                        // Check to see if the Players Location has some how changed during the call of the event.
                        // This can happen due to a plugin teleporting the player instead of using .setTo()
                        if (!from.equals(this.getPlayer().getLocation()) && this.justTeleported) {
                            this.justTeleported = false;
                            return;
                        }
                    }
                }
                // Akarin end

                this.serverController.getPlayerList().serverUpdateMovingPlayer(this.player);
                this.player.addMovementStat(this.player.posX - d0, this.player.posY - d1, this.player.posZ - d2);
                this.vehicleFloating = d11 >= -0.03125D && !this.serverController.isFlightAllowed() && !worldserver.checkBlockCollision(entity.getEntityBoundingBox().grow(0.0625D).expand(0.0D, -0.55D, 0.0D));
                this.lowestRiddenX1 = entity.posX;
                this.lowestRiddenY1 = entity.posY;
                this.lowestRiddenZ1 = entity.posZ;
            }
        }
    }

    public void processConfirmTeleport(CPacketConfirmTeleport packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());

        if (packetIn.getTeleportId() == this.teleportId && this.targetPos != null) // Akarin
        {
            this.player.setPositionAndRotation(this.targetPos.x, this.targetPos.y, this.targetPos.z, this.player.rotationYaw, this.player.rotationPitch);

            if (this.player.isInvulnerableDimensionChange())
            {
                this.lastGoodX = this.targetPos.x;
                this.lastGoodY = this.targetPos.y;
                this.lastGoodZ = this.targetPos.z;
                this.player.clearInvulnerableDimensionChange();
            }

            this.targetPos = null;
        }
    }

    public void handleRecipeBookUpdate(CPacketRecipeInfo p_191984_1_)
    {
        PacketThreadUtil.checkThreadAndEnqueue(p_191984_1_, this, this.player.getServerWorld());

        if (p_191984_1_.getPurpose() == CPacketRecipeInfo.Purpose.SHOWN)
        {
            this.player.getRecipeBook().markSeen(p_191984_1_.getRecipe());
        }
        else if (p_191984_1_.getPurpose() == CPacketRecipeInfo.Purpose.SETTINGS)
        {
            this.player.getRecipeBook().setGuiOpen(p_191984_1_.isGuiOpen());
            this.player.getRecipeBook().setFilteringCraftable(p_191984_1_.isFilteringCraftable());
        }
    }

    public void handleSeenAdvancements(CPacketSeenAdvancements p_194027_1_)
    {
        PacketThreadUtil.checkThreadAndEnqueue(p_194027_1_, this, this.player.getServerWorld());

        if (p_194027_1_.getAction() == CPacketSeenAdvancements.Action.OPENED_TAB)
        {
            ResourceLocation resourcelocation = p_194027_1_.getTab();
            Advancement advancement = this.serverController.getAdvancementManager().getAdvancement(resourcelocation);

            if (advancement != null)
            {
                this.player.getAdvancements().setSelectedTab(advancement);
            }
        }
    }

    public void processPlayer(CPacketPlayer packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());

        if (isMovePlayerPacketInvalid(packetIn))
        {
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_player_movement", new Object[0]));
        }
        else
        {
            WorldServer worldserver = this.serverController.getWorld(this.player.dimension);

            if (!this.player.queuedEndExit && !this.player.isMovementBlocked()) // Akarin
            {
                if (this.networkTickCount == 0)
                {
                    this.captureCurrentPosition();
                }

                if (this.targetPos != null)
                {
                    if (this.networkTickCount - this.lastPositionUpdate > 20)
                    {
                        this.lastPositionUpdate = this.networkTickCount;
                        this.setPlayerLocation(this.targetPos.x, this.targetPos.y, this.targetPos.z, this.player.rotationYaw, this.player.rotationPitch);
                    }
                    this.allowedPlayerTicks = 20; // Akarin
                }
                else
                {
                    this.lastPositionUpdate = this.networkTickCount;

                    if (this.player.isRiding())
                    {
                        this.player.setPositionAndRotation(this.player.posX, this.player.posY, this.player.posZ, packetIn.getYaw(this.player.rotationYaw), packetIn.getPitch(this.player.rotationPitch));
                        this.serverController.getPlayerList().serverUpdateMovingPlayer(this.player);
                        this.allowedPlayerTicks = 20; // Akarin
                    }
                    else
                    {
                        // Akarin - Make sure the move is valid but then reset it for plugins to modify
                        double prevX = player.posX;
                        double prevY = player.posY;
                        double prevZ = player.posZ;
                        float prevYaw = player.rotationYaw;
                        float prevPitch = player.rotationPitch;
                        // Akarin end
                        double d0 = this.player.posX;
                        double d1 = this.player.posY;
                        double d2 = this.player.posZ;
                        double d3 = this.player.posY;
                        double d4 = packetIn.getX(this.player.posX);
                        double d5 = packetIn.getY(this.player.posY);
                        double d6 = packetIn.getZ(this.player.posZ);
                        float f = packetIn.getYaw(this.player.rotationYaw);
                        float f1 = packetIn.getPitch(this.player.rotationPitch);
                        double d7 = d4 - this.firstGoodX;
                        double d8 = d5 - this.firstGoodY;
                        double d9 = d6 - this.firstGoodZ;
                        double d10 = this.player.motionX * this.player.motionX + this.player.motionY * this.player.motionY + this.player.motionZ * this.player.motionZ;
                        double d11 = d7 * d7 + d8 * d8 + d9 * d9;

                        if (this.player.isPlayerSleeping())
                        {
                            if (d11 > 1.0D)
                            {
                                this.setPlayerLocation(this.player.posX, this.player.posY, this.player.posZ, packetIn.getYaw(this.player.rotationYaw), packetIn.getPitch(this.player.rotationPitch));
                            }
                        }
                        else
                        {
                            ++this.movePacketCounter;
                            int i = this.movePacketCounter - this.lastMovePacketCounter;

                            // Akarin start - handle custom speeds and skipped ticks
                            this.allowedPlayerTicks += (System.currentTimeMillis() / 50) - this.lastTick;
                            this.allowedPlayerTicks = Math.max(this.allowedPlayerTicks, 1);
                            this.lastTick = (int) (System.currentTimeMillis() / 50);

                            if (i > Math.max(this.allowedPlayerTicks, 5))
                            // Akarin end
                            {
                                LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.getName(), Integer.valueOf(i));
                                i = 1;
                            }
                            // Akarin start
                            if (packetIn.rotating || d11 > 0) {
                                allowedPlayerTicks -= 1;
                            } else {
                                allowedPlayerTicks = 20;
                            }
                            float speed;
                            if (player.capabilities.isFlying) {
                                speed = player.capabilities.flySpeed * 20f;
                            } else {
                                speed = player.capabilities.walkSpeed * 10f;
                            }
                            // Akarin end

                            if (!this.player.isInvulnerableDimensionChange() && (!this.player.getServerWorld().getGameRules().getBoolean("disableElytraMovementCheck") || !this.player.isElytraFlying()))
                            {
                                float f2 = this.player.isElytraFlying() ? 300.0F : 100.0F;

                                if (d11 - d10 > Math.max(f2, Math.pow((double) (org.spigotmc.SpigotConfig.movedTooQuicklyMultiplier * (float) i * speed), 2))) // Akarin
                                {
                                    LOGGER.warn("{} moved too quickly! {},{},{}", this.player.getName(), Double.valueOf(d7), Double.valueOf(d8), Double.valueOf(d9));
                                    this.setPlayerLocation(this.player.posX, this.player.posY, this.player.posZ, this.player.rotationYaw, this.player.rotationPitch);
                                    return;
                                }
                            }

                            boolean flag2 = worldserver.getCollisionBoxes(this.player, this.player.getEntityBoundingBox().shrink(0.0625D)).isEmpty();
                            d7 = d4 - this.lastGoodX;
                            d8 = d5 - this.lastGoodY;
                            d9 = d6 - this.lastGoodZ;

                            if (this.player.onGround && !packetIn.isOnGround() && d8 > 0.0D)
                            {
                                this.player.jump();
                            }

                            this.player.move(MoverType.PLAYER, d7, d8, d9);
                            this.player.onGround = packetIn.isOnGround();
                            double d12 = d8;
                            d7 = d4 - this.player.posX;
                            d8 = d5 - this.player.posY;

                            if (d8 > -0.5D || d8 < 0.5D)
                            {
                                d8 = 0.0D;
                            }

                            d9 = d6 - this.player.posZ;
                            d11 = d7 * d7 + d8 * d8 + d9 * d9;
                            boolean flag = false;

                            if (!this.player.isInvulnerableDimensionChange() && d11 > 0.0625D && !this.player.isPlayerSleeping() && !this.player.interactionManager.isCreative() && this.player.interactionManager.getGameType() != GameType.SPECTATOR)
                            {
                                flag = true;
                                LOGGER.warn("{} moved wrongly!", (Object)this.player.getName());
                            }

                            this.player.setPositionAndRotation(d4, d5, d6, f, f1);
                            this.player.addMovementStat(this.player.posX - d0, this.player.posY - d1, this.player.posZ - d2);

                            if (!this.player.noClip && !this.player.isPlayerSleeping())
                            {
                                boolean flag1 = worldserver.getCollisionBoxes(this.player, this.player.getEntityBoundingBox().shrink(0.0625D)).isEmpty();

                                if (flag2 && (flag || !flag1))
                                {
                                    this.setPlayerLocation(d0, d1, d2, f, f1);
                                    return;
                                }
                            }
                            // Akarint start - fire PlayerMoveEvent
                            // Rest to old location first
                            this.player.setPositionAndRotation(prevX, prevY, prevZ, prevYaw, prevPitch);

                            Player player = this.getPlayer();
                            Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
                            Location to = player.getLocation().clone(); // Start off the To location as the Players current location.

                            // If the packet contains movement information then we update the To location with the correct XYZ.
                            if (packetIn.moving) {
                                to.setX(packetIn.x);
                                to.setY(packetIn.y);
                                to.setZ(packetIn.z);
                            }

                            // If the packet contains look information then we update the To location with the correct Yaw & Pitch.
                            if (packetIn.rotating) {
                                to.setYaw(packetIn.yaw);
                                to.setPitch(packetIn.pitch);
                            }

                            // Prevent 40 event-calls for less than a single pixel of movement >.>
                            double delta = Math.pow(this.lastPosX - to.getX(), 2) + Math.pow(this.lastPosY - to.getY(), 2) + Math.pow(this.lastPosZ - to.getZ(), 2);
                            float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

                            if ((delta > 1f / 256 || deltaAngle > 10f) && !this.player.isMovementBlocked()) {
                                this.lastPosX = to.getX();
                                this.lastPosY = to.getY();
                                this.lastPosZ = to.getZ();
                                this.lastYaw = to.getYaw();
                                this.lastPitch = to.getPitch();

                                // Skip the first time we do this
                                if (from.getX() != Double.MAX_VALUE) {
                                    Location oldTo = to.clone();
                                    PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);
                                    this.server.getPluginManager().callEvent(event);

                                    // If the event is cancelled we move the player back to their old location.
                                    if (event.isCancelled()) {
                                        teleport(from);
                                        return;
                                    }

                                    // If a Plugin has changed the To destination then we teleport the Player
                                    // there to avoid any 'Moved wrongly' or 'Moved too quickly' errors.
                                    // We only do this if the Event was not cancelled.
                                    if (!oldTo.equals(event.getTo()) && !event.isCancelled()) {
                                        this.player.getBukkitEntity().teleport(event.getTo(), TeleportCause.PLUGIN);
                                        return;
                                    }

                                    // Check to see if the Players Location has some how changed during the call of the event.
                                    // This can happen due to a plugin teleporting the player instead of using .setTo()
                                    if (!from.equals(this.getPlayer().getLocation()) && this.justTeleported) {
                                        this.justTeleported = false;
                                        return;
                                    }
                                }
                            }
                            this.player.setPositionAndRotation(d4, d5, d6, f, f1); // Copied from above
                            // Akarin end

                            this.floating = d12 >= -0.03125D;
                            this.floating &= !this.serverController.isFlightAllowed() && !this.player.capabilities.allowFlying;
                            this.floating &= !this.player.isPotionActive(MobEffects.LEVITATION) && !this.player.isElytraFlying() && !worldserver.checkBlockCollision(this.player.getEntityBoundingBox().grow(0.0625D).expand(0.0D, -0.55D, 0.0D));
                            this.player.onGround = packetIn.isOnGround();
                            this.serverController.getPlayerList().serverUpdateMovingPlayer(this.player);
                            this.player.handleFalling(this.player.posY - d3, packetIn.isOnGround());
                            this.lastGoodX = this.player.posX;
                            this.lastGoodY = this.player.posY;
                            this.lastGoodZ = this.player.posZ;
                        }
                    }
                }
            }
        }
    }

    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch)
    {
        this.setPlayerLocation(x, y, z, yaw, pitch, Collections.emptySet());
    }
    // Akarin start - Delegate to teleport(Location)
    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, PlayerTeleportEvent.TeleportCause cause) {
        this.setPlayerLocation(x, y, z, yaw, pitch, Collections.emptySet(), cause);
    }

    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, Set<SPacketPlayerPosLook.EnumFlags> relativeSet)
    {
        this.setPlayerLocation(x, y, z, yaw, pitch, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }

    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, Set<SPacketPlayerPosLook.EnumFlags> set, PlayerTeleportEvent.TeleportCause cause) {
        Player player = this.getPlayer();
        Location from = player.getLocation();

        if (set.contains(SPacketPlayerPosLook.EnumFlags.X)) {
            x += from.getX();
        }
        if (set.contains(SPacketPlayerPosLook.EnumFlags.Y)) {
            y += from.getY();
        }
        if (set.contains(SPacketPlayerPosLook.EnumFlags.Z)) {
            z += from.getZ();
        }
        if (set.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
            yaw += from.getYaw();
        }
        if (set.contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) {
            pitch += from.getPitch();
        }

        Location to = new Location(this.getPlayer().getWorld(), x, y, z, yaw, pitch);
        PlayerTeleportEvent event = new PlayerTeleportEvent(player, from.clone(), to.clone(), cause);
        this.server.getPluginManager().callEvent(event);

        if (event.isCancelled() || !to.equals(event.getTo())) {
            set.clear(); // Can't relative teleport
            to = event.isCancelled() ? event.getFrom() : event.getTo();
            x = to.getX();
            y = to.getY();
            z = to.getZ();
            yaw = to.getYaw();
            pitch = to.getPitch();
        }

        this.internalTeleport(x, y, z, yaw, pitch, set);
    }

    public void teleport(Location dest) {
        internalTeleport(dest.getX(), dest.getY(), dest.getZ(), dest.getYaw(), dest.getPitch(), Collections.emptySet());
    }

    private void internalTeleport(double x, double y, double z, float yaw, float pitch, Set<SPacketPlayerPosLook.EnumFlags> relativeSet) {
        if (Float.isNaN(yaw)) {
            yaw = 0;
        }
        if (Float.isNaN(pitch)) {
            pitch = 0;
        }

        this.justTeleported = true;
        // Akarin end
        double d0 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.X) ? this.player.posX : 0.0D;
        double d1 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Y) ? this.player.posY : 0.0D;
        double d2 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Z) ? this.player.posZ : 0.0D;
        this.targetPos = new Vec3d(x + d0, y + d1, z + d2);
        float f = yaw;
        float f1 = pitch;

        if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT))
        {
            f = yaw + this.player.rotationYaw;
        }

        if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.X_ROT))
        {
            f1 = pitch + this.player.rotationPitch;
        }
        // Akarin start - update last location
        this.lastPosX = this.targetPos.x;
        this.lastPosY = this.targetPos.y;
        this.lastPosZ = this.targetPos.z;
        this.lastYaw = yaw;
        this.lastPitch = pitch;
        // Akarin end

        if (++this.teleportId == Integer.MAX_VALUE)
        {
            this.teleportId = 0;
        }

        this.lastPositionUpdate = this.networkTickCount;
        this.player.setPositionAndRotation(this.targetPos.x, this.targetPos.y, this.targetPos.z, f, f1);
        this.player.connection.sendPacket(new SPacketPlayerPosLook(x, y, z, yaw, pitch, relativeSet, this.teleportId));
    }

    public void processPlayerDigging(CPacketPlayerDigging packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // Akarin
        WorldServer worldserver = this.serverController.getWorld(this.player.dimension);
        BlockPos blockpos = packetIn.getPosition();
        this.player.markPlayerActive();

        switch (packetIn.getAction())
        {
            case SWAP_HELD_ITEMS:

                if (!this.player.isSpectator())
                {
                    ItemStack itemstack = this.player.getHeldItem(EnumHand.OFF_HAND);
                    // Akarin start
                    PlayerSwapHandItemsEvent swapItemsEvent = new PlayerSwapHandItemsEvent(getPlayer(), CraftItemStack.asBukkitCopy(itemstack), CraftItemStack.asBukkitCopy(this.player.getHeldItem(EnumHand.MAIN_HAND)));
                    this.server.getPluginManager().callEvent(swapItemsEvent);
                    if (swapItemsEvent.isCancelled()) {
                        return;
                    }
                    itemstack = CraftItemStack.asNMSCopy(swapItemsEvent.getMainHandItem());
                    this.player.setHeldItem(EnumHand.OFF_HAND, CraftItemStack.asNMSCopy(swapItemsEvent.getOffHandItem()));
                    // Akarin end
                    this.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
                }

                return;
            case DROP_ITEM:

                if (!this.player.isSpectator())
                {
                    // Akarin start - limit how quickly items can be dropped
                    // If the ticks aren't the same then the count starts from 0 and we update the lastDropTick.
                    if (this.lastDropTick != MinecraftServer.currentTick) {
                        this.dropCount = 0;
                        this.lastDropTick = MinecraftServer.currentTick;
                    } else {
                        // Else we increment the drop count and check the amount.
                        this.dropCount++;
                        if (this.dropCount >= 20) {
                            LOGGER.warn(this.player.getName() + " dropped their items too quickly!");
                            this.disconnect("You dropped your items too quickly (Hacking?)");
                            return;
                        }
                    }
                    // Akarin end
                    this.player.dropItem(false);
                }

                return;
            case DROP_ALL_ITEMS:

                if (!this.player.isSpectator())
                {
                    this.player.dropItem(true);
                }

                return;
            case RELEASE_USE_ITEM:
                this.player.stopActiveHand();
                return;
            case START_DESTROY_BLOCK:
            case ABORT_DESTROY_BLOCK:
            case STOP_DESTROY_BLOCK:
                double d0 = this.player.posX - ((double)blockpos.getX() + 0.5D);
                double d1 = this.player.posY - ((double)blockpos.getY() + 0.5D) + 1.5D;
                double d2 = this.player.posZ - ((double)blockpos.getZ() + 0.5D);
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                double dist = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + 1;
                dist *= dist;

                if (d3 > dist)
                {
                    return;
                }
                else if (blockpos.getY() >= this.serverController.getBuildLimit())
                {
                    return;
                }
                else
                {
                    if (packetIn.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK)
                    {
                        if (!this.serverController.isBlockProtected(worldserver, blockpos, this.player) && worldserver.getWorldBorder().contains(blockpos))
                        {
                            this.player.interactionManager.onBlockClicked(blockpos, packetIn.getFacing());
                        }
                        else
                        {
                            // Akarin start - fire PlayerInteractEvent
                            CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, blockpos, packetIn.getFacing(), this.player.inventory.getCurrentItem(), EnumHand.MAIN_HAND);
                            this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
                            // Update any tile entity data for this block
                            TileEntity tileentity = worldserver.getTileEntity(blockpos);
                            if (tileentity != null) {
                                this.player.connection.sendPacket(tileentity.getUpdatePacket());
                            }
                            // Akarin end
                        }
                    }
                    else
                    {
                        if (packetIn.getAction() == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)
                        {
                            this.player.interactionManager.blockRemoving(blockpos);
                        }
                        else if (packetIn.getAction() == CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK)
                        {
                            this.player.interactionManager.cancelDestroyingBlock();
                        }

                        if (worldserver.getBlockState(blockpos).getMaterial() != Material.AIR)
                        {
                            this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
                        }
                    }

                    return;
                }

            default:
                throw new IllegalArgumentException("Invalid player action");
        }
    }

    public void processTryUseItemOnBlock(CPacketPlayerTryUseItemOnBlock packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        WorldServer worldserver = this.serverController.getWorld(this.player.dimension);
        EnumHand enumhand = packetIn.getHand();
        ItemStack itemstack = this.player.getHeldItem(enumhand);
        BlockPos blockpos = packetIn.getPos();
        EnumFacing enumfacing = packetIn.getDirection();
        this.player.markPlayerActive();

        if (blockpos.getY() < this.serverController.getBuildLimit() - 1 || enumfacing != EnumFacing.UP && blockpos.getY() < this.serverController.getBuildLimit())
        {
            double dist = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + 3;
            dist *= dist;
            if (this.targetPos == null && this.player.getDistanceSq((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D) < dist && !this.serverController.isBlockProtected(worldserver, blockpos, this.player) && worldserver.getWorldBorder().contains(blockpos))
            {
                // CraftBukkit start - Check if we can actually do something over this large a distance
                Location eyeLoc = this.getPlayer().getEyeLocation();
                double reachDistance = NumberConversions.square(eyeLoc.getX() - blockpos.getX()) + NumberConversions.square(eyeLoc.getY() - blockpos.getY()) + NumberConversions.square(eyeLoc.getZ() - blockpos.getZ());
                if (reachDistance > (this.getPlayer().getGameMode() == org.bukkit.GameMode.CREATIVE ? CREATIVE_PLACE_DISTANCE_SQUARED : SURVIVAL_PLACE_DISTANCE_SQUARED)) {
                    return;
                }
                // CraftBukkit end
                this.player.interactionManager.processRightClickBlock(this.player, worldserver, itemstack, enumhand, blockpos, enumfacing, packetIn.getFacingX(), packetIn.getFacingY(), packetIn.getFacingZ());
            }
        }
        else
        {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("build.tooHigh", new Object[] {this.serverController.getBuildLimit()});
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            this.player.connection.sendPacket(new SPacketChat(textcomponenttranslation, ChatType.GAME_INFO));
        }

        this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
        this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos.offset(enumfacing)));
    }

    public void processTryUseItem(CPacketPlayerTryUseItem packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        WorldServer worldserver = this.serverController.getWorld(this.player.dimension);
        EnumHand enumhand = packetIn.getHand();
        ItemStack itemstack = this.player.getHeldItem(enumhand);
        this.player.markPlayerActive();

        if (!itemstack.isEmpty())
        {
            // Akarin start
            // Raytrace to look for 'rogue armswings'
            float f1 = this.player.rotationPitch;
            float f2 = this.player.rotationYaw;
            double d0 = this.player.posX;
            double d1 = this.player.posY + (double) this.player.getEyeHeight();
            double d2 = this.player.posZ;
            Vec3d vec3d = new Vec3d(d0, d1, d2);

            float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
            float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            float f7 = f4 * f5;
            float f8 = f3 * f5;
            double d3 = player.interactionManager.getGameType()== GameType.CREATIVE ? 5.0D : 4.5D;
            Vec3d vec3d1 = vec3d.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
            RayTraceResult movingobjectposition = this.player.world.rayTraceBlocks(vec3d, vec3d1, false);

            boolean cancelled;
            if (movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK) {
                org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_AIR, itemstack, enumhand);
                cancelled = event.useItemInHand() == Result.DENY;
            } else {
                if (player.interactionManager.firedInteract) {
                    player.interactionManager.firedInteract = false;
                    cancelled = player.interactionManager.interactResult;
                } else {
                    org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, movingobjectposition.getBlockPos(), movingobjectposition.sideHit, itemstack, true, enumhand);
                    cancelled = event.useItemInHand() == Result.DENY;
                }
            }

            if (cancelled) {
                this.player.getBukkitEntity().updateInventory(); // SPIGOT-2524
            } else {
                this.player.interactionManager.processRightClick(this.player, worldserver, itemstack, enumhand);
            }
            // Akarin end
        }
    }

    public void handleSpectate(CPacketSpectate packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());

        if (this.player.isSpectator())
        {
            Entity entity = null;

            for (WorldServer worldserver : this.serverController.worldServers)
            {
                if (worldserver != null)
                {
                    entity = packetIn.getEntity(worldserver);

                    if (entity != null)
                    {
                        break;
                    }
                }
            }

            if (entity != null)
            {
                this.player.setSpectatingEntity(this.player);
                this.player.dismountRidingEntity();

                /* Akarin start - replace with bukkit handling for multi-world
                if (entity.world == this.player.world)
                {
                    this.player.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                }
                else if (net.minecraftforge.common.ForgeHooks.onTravelToDimension(this.player, entity.dimension))
                {
                    int prevDimension = this.player.dimension;
                    WorldServer worldserver1 = this.player.getServerWorld();
                    WorldServer worldserver2 = (WorldServer)entity.world;
                    this.player.dimension = entity.dimension;
                    this.sendPacket(new SPacketRespawn(this.player.dimension, worldserver2.getDifficulty(), worldserver2.getWorldInfo().getTerrainType(), this.player.interactionManager.getGameType())); // Forge: Use new dimensions information
                    this.serverController.getPlayerList().updatePermissionLevel(this.player);
                    worldserver1.removeEntityDangerously(this.player);
                    this.player.isDead = false;
                    this.player.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);

                    if (this.player.isEntityAlive())
                    {
                        worldserver1.updateEntityWithOptionalForce(this.player, false);
                        worldserver2.spawnEntity(this.player);
                        worldserver2.updateEntityWithOptionalForce(this.player, false);
                    }

                    this.player.setWorld(worldserver2);
                    this.serverController.getPlayerList().preparePlayer(this.player, worldserver1);
                    this.player.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                    this.player.interactionManager.setWorld(worldserver2);
                    this.serverController.getPlayerList().updateTimeAndWeatherForPlayer(this.player, worldserver2);
                    this.serverController.getPlayerList().syncPlayerInventory(this.player);
                    net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerChangedDimensionEvent(this.player, prevDimension, this.player.dimension);
                }
                */
                this.player.getBukkitEntity().teleport(entity.getBukkitEntity(), PlayerTeleportEvent.TeleportCause.SPECTATE);
            }
        }
    }
    
    @Override
    public void handleResourcePackStatus(CPacketResourcePackStatus packetplayinresourcepackstatus) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinresourcepackstatus, this, this.player.getServerWorld());
        final PlayerResourcePackStatusEvent.Status status = PlayerResourcePackStatusEvent.Status.values()[packetplayinresourcepackstatus.action.ordinal()];
        this.getPlayer().setResourcePackStatus(status);
        this.server.getPluginManager().callEvent(new PlayerResourcePackStatusEvent(getPlayer(), status));
    }
    // Akarin end

    public void processSteerBoat(CPacketSteerBoat packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        Entity entity = this.player.getRidingEntity();

        if (entity instanceof EntityBoat)
        {
            ((EntityBoat)entity).setPaddleState(packetIn.getLeft(), packetIn.getRight());
        }
    }

    public void onDisconnect(ITextComponent reason)
    {
        // Akarin start - Rarely it would send a disconnect line twice
        if (this.processedDisconnect) {
            return;
        } else {
            this.processedDisconnect = true;
        }
        // Akarin end
        LOGGER.info("{} lost connection: {}", this.player.getName(), reason.getUnformattedText());
        // Akarin start
        /*
        this.serverController.refreshStatusNextTick();
        TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("multiplayer.player.left", new Object[] {this.player.getDisplayName()});
        textcomponenttranslation.getStyle().setColor(TextFormatting.YELLOW);
        this.serverController.getPlayerList().sendMessage(textcomponenttranslation);
        */
        // Akarin end
        this.player.mountEntityAndWakeUp();
        // Akarin start
        String quitMessage = this.serverController.getPlayerList().playerLoggedOut(this.player);
        if ((quitMessage != null) && (quitMessage.length() > 0)) {
            this.serverController.getPlayerList().sendMessage(CraftChatMessage.fromString(quitMessage));
        }
        // Akarin end

        if (this.serverController.isSinglePlayer() && this.player.getName().equals(this.serverController.getServerOwner()))
        {
            LOGGER.info("Stopping singleplayer server as player logged out");
            this.serverController.initiateShutdown();
        }
    }

    public void sendPacket(final Packet<?> packetIn)
    {
        if (packetIn instanceof SPacketChat)
        {
            SPacketChat spacketchat = (SPacketChat)packetIn;
            EntityPlayer.EnumChatVisibility entityplayer$enumchatvisibility = this.player.getChatVisibility();

            if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.HIDDEN && spacketchat.getType() != ChatType.GAME_INFO)
            {
                return;
            }

            if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.SYSTEM && !spacketchat.isSystem())
            {
                return;
            }
        }
        // Akarin start
        if (packetIn == null || this.processedDisconnect) { // Spigot
            return;
        } else if (packetIn instanceof SPacketSpawnPosition) {
            SPacketSpawnPosition packet6 = (SPacketSpawnPosition) packetIn;
            this.player.compassTarget = new Location(this.getPlayer().getWorld(), packet6.spawnBlockPos.getX(), packet6.spawnBlockPos.getY(), packet6.spawnBlockPos.getZ());
        }
        // Akarin end

        try
        {
            this.netManager.sendPacket(packetIn);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Sending packet");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Packet being sent");
            crashreportcategory.addDetail("Packet class", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return packetIn.getClass().getCanonicalName();
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    public void processHeldItemChange(CPacketHeldItemChange packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit

        if (packetIn.getSlotId() >= 0 && packetIn.getSlotId() < InventoryPlayer.getHotbarSize())
        {
            // Akarin start
            PlayerItemHeldEvent event = new PlayerItemHeldEvent(this.getPlayer(), this.player.inventory.currentItem, packetIn.getSlotId());
            this.server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                this.sendPacket(new SPacketHeldItemChange(this.player.inventory.currentItem));
                this.player.markPlayerActive();
                return;
            }
            // Akarin end
            this.player.inventory.currentItem = packetIn.getSlotId();
            this.player.markPlayerActive();
        }
        else
        {
            LOGGER.warn("{} tried to set an invalid carried item", (Object)this.player.getName());
        }
    }

    public void processChatMessage(CPacketChatMessage packetIn)
    {
        // Akarin start - async chat
        // SPIGOT-3638
        if (this.serverController.isServerStopped()) {
            return;
        }

        boolean isSync = packetIn.getMessage().startsWith("/");
        if (packetIn.getMessage().startsWith("/")) {
            PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        }
        if (this.player.isDead || this.player.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN)
        // Akarin end
        {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("chat.cannotSend", new Object[0]);
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            this.sendPacket(new SPacketChat(textcomponenttranslation));
        }
        else
        {
            this.player.markPlayerActive();
            String s = packetIn.getMessage();
            s = StringUtils.normalizeSpace(s);

            for (int i = 0; i < s.length(); ++i)
            {
                if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i)))
                {
                    if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i))) {
                        // Akarin start - threadsafety
                        if (!isSync) {
                            Waitable waitable = new Waitable() {
                                @Override
                                protected Object evaluate() {
                                    NetHandlerPlayServer.this.disconnect(new TextComponentTranslation("multiplayer.disconnect.illegal_characters", new Object[0]));
                                    return null;
                                }
                            };
                            
                            this.serverController.processQueue.add(waitable);
                            
                            try {
                                waitable.get();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            } catch (ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.illegal_characters", new Object[0]));
                        }
                    }
                    // Akarin end
                    return;
                }
            }

            // Akarin start
            if (isSync) {
                try {
                    this.serverController.server.playerCommandState = true;
                    this.handleSlashCommand(s);
                } finally {
                    this.serverController.server.playerCommandState = false;
                }
            } else if (s.isEmpty()) {
                LOGGER.warn(this.player.getName() + " tried to send an empty message");
            } else if (getPlayer().isConversing()) {
                // Spigot start
                final String message = s;
                this.serverController.processQueue.add( new Waitable()
                {
                    @Override
                    protected Object evaluate()
                    {
                        getPlayer().acceptConversationInput( message );
                        return null;
                    }
                } );
                // Spigot end
            } else if (this.player.getChatVisibility() == EntityPlayer.EnumChatVisibility.SYSTEM) { // Re-add "Command Only" flag check
                TextComponentTranslation chatmessage = new TextComponentTranslation("chat.cannotSend", new Object[0]);

                chatmessage.getStyle().setColor(TextFormatting.RED);
                this.sendPacket(new SPacketChat(chatmessage));
            } else if (true) {
                this.chat(s, true);
                // Akarin end - the below is for reference. :)
            }
            else
            {
                ITextComponent itextcomponent = new TextComponentTranslation("chat.type.text", this.player.getDisplayName(), net.minecraftforge.common.ForgeHooks.newChatWithLinks(s));
                itextcomponent = net.minecraftforge.common.ForgeHooks.onServerChatEvent(this, s, itextcomponent);
                if (itextcomponent == null) return;
                this.serverController.getPlayerList().sendMessage(itextcomponent, false);
            }

            // Akarin start
            boolean counted = true;
            for ( String exclude : org.spigotmc.SpigotConfig.spamExclusions )
            {
                if ( exclude != null && s.startsWith( exclude ) )
                {
                    counted = false;
                    break;
                }
            }
            
            if (counted && chatSpamField.addAndGet(this, 20) > 200 && !this.serverController.getPlayerList().canSendCommands(this.player.getGameProfile())) { // Spigot
                if (!isSync) {
                    Waitable waitable = new Waitable() {
                        @Override
                        protected Object evaluate() {
                            NetHandlerPlayServer.this.disconnect(new TextComponentTranslation("disconnect.spam", new Object[0]));
                            return null;
                        }
                    };

                    this.serverController.processQueue.add(waitable);

                    try {
                        waitable.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    this.disconnect(new TextComponentTranslation("disconnect.spam", new Object[0]));
                }
            }

        }
    }

    public void chat(String s, boolean async) {
        if (s.isEmpty() || this.player.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) {
            return;
        }

        if (!async && s.startsWith("/")) {
            if (!Bukkit.isPrimaryThread()) {
                final String fCommandLine = s;
                this.serverController.addScheduledTask(() -> chat(fCommandLine,  false));
            }
            this.handleSlashCommand(s);
        } else if (this.player.getChatVisibility() == EntityPlayer.EnumChatVisibility.SYSTEM) {
            // Do nothing, this is coming from a plugin
        } else {
            Player player = this.getPlayer();
            AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet(serverController));
            this.server.getPluginManager().callEvent(event);

            if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0) {
                // Evil plugins still listening to deprecated event
                final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(), event.getRecipients());
                queueEvent.setCancelled(event.isCancelled());
                Waitable waitable = new Waitable() {
                    @Override
                    protected Object evaluate() {
                        org.bukkit.Bukkit.getPluginManager().callEvent(queueEvent);

                        if (queueEvent.isCancelled()) {
                            return null;
                        }

                        String message = String.format(queueEvent.getFormat(), queueEvent.getPlayer().getDisplayName(), queueEvent.getMessage());
                        NetHandlerPlayServer.this.serverController.console.sendMessage(message);
                        if (((LazyPlayerSet) queueEvent.getRecipients()).isLazy()) {
                            for (Object player : NetHandlerPlayServer.this.serverController.getPlayerList().playerEntityList) {
                                ((EntityPlayerMP) player).sendMessage(CraftChatMessage.fromString(message));
                            }
                        } else {
                            for (Player player : queueEvent.getRecipients()) {
                                player.sendMessage(message);
                            }
                        }
                        return null;
                    }};
                if (async) {
                    serverController.processQueue.add(waitable);
                } else {
                    waitable.run();
                }
                try {
                    waitable.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // This is proper habit for java. If we aren't handling it, pass it on!
                } catch (ExecutionException e) {
                    throw new RuntimeException("Exception processing chat event", e.getCause());
                }
            } else {
                if (event.isCancelled()) {
                    return;
                }

                // Paper Start - (Meh) Support for vanilla world scoreboard name coloring
                String displayName = event.getPlayer().getDisplayName();
                if (this.player.getEntityWorld().paperConfig.useVanillaScoreboardColoring) {
                    displayName = ScorePlayerTeam.formatPlayerName(this.player.getTeam(), player.getDisplayName());
                }

                s = String.format(event.getFormat(), displayName, event.getMessage());
                // Paper end
                serverController.console.sendMessage(s);
                if (((LazyPlayerSet) event.getRecipients()).isLazy()) {
                    for (Object recipient : serverController.getPlayerList().playerEntityList) {
                        ((EntityPlayerMP) recipient).sendMessage(CraftChatMessage.fromString(s));
                    }
                } else {
                    for (Player recipient : event.getRecipients()) {
                        recipient.sendMessage(s);
                    }
                }
            }
        }
    }
    // Akarin end

    private void handleSlashCommand(String command)
    {
       // Akarin start - whole method
        if ( org.spigotmc.SpigotConfig.logCommands )
        this.LOGGER.info(this.player.getName() + " issued server command: " + command);

        CraftPlayer player = this.getPlayer();

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, command, new LazyPlayerSet(serverController));
        this.server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        try {
            if (this.server.dispatchCommand(event.getPlayer(), event.getMessage().substring(1))) {
                return;
            }
        } catch (org.bukkit.command.CommandException ex) {
            player.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
            java.util.logging.Logger.getLogger(NetHandlerPlayServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            return;
        }
        // Akarin end
    }

    public void handleAnimation(CPacketAnimation packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        this.player.markPlayerActive();
        // Akarin start - Raytrace to look for 'rogue armswings'
        float f1 = this.player.rotationPitch;
        float f2 = this.player.rotationYaw;
        double d0 = this.player.posX;
        double d1 = this.player.posY + (double) this.player.getEyeHeight();
        double d2 = this.player.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);

        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = player.interactionManager.getGameType()== GameType.CREATIVE ? 5.0D : 4.5D;
        Vec3d vec3d1 = vec3d.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        RayTraceResult movingobjectposition = this.player.world.rayTraceBlocks(vec3d, vec3d1, false);

        if (movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK) {
            CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_AIR, this.player.inventory.getCurrentItem(), EnumHand.MAIN_HAND);
        }

        // Arm swing animation
        PlayerAnimationEvent event = new PlayerAnimationEvent(this.getPlayer());
        this.server.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        // Akarin end
        this.player.swingArm(packetIn.getHand());
    }

    public void processEntityAction(CPacketEntityAction packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        // Akarin start
        if (this.player.isDead) return;
        switch (packetIn.getAction()) {
            case START_SNEAKING:
            case STOP_SNEAKING:
                PlayerToggleSneakEvent event = new PlayerToggleSneakEvent(this.getPlayer(), packetIn.getAction() == CPacketEntityAction.Action.START_SNEAKING);
                this.server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
                break;
            case START_SPRINTING:
            case STOP_SPRINTING:
                PlayerToggleSprintEvent e2 = new PlayerToggleSprintEvent(this.getPlayer(), packetIn.getAction() == CPacketEntityAction.Action.START_SPRINTING);
                this.server.getPluginManager().callEvent(e2);

                if (e2.isCancelled()) {
                    return;
                }
                break;
        }
        // Akarin end
        this.player.markPlayerActive();

        switch (packetIn.getAction())
        {
            case START_SNEAKING:
                this.player.setSneaking(true);
                break;
            case STOP_SNEAKING:
                this.player.setSneaking(false);
                break;
            case START_SPRINTING:
                this.player.setSprinting(true);
                break;
            case STOP_SPRINTING:
                this.player.setSprinting(false);
                break;
            case STOP_SLEEPING:

                if (this.player.isPlayerSleeping())
                {
                    this.player.wakeUpPlayer(false, true, true);
                    this.targetPos = new Vec3d(this.player.posX, this.player.posY, this.player.posZ);
                }

                break;
            case START_RIDING_JUMP:

                if (this.player.getRidingEntity() instanceof IJumpingMount)
                {
                    IJumpingMount ijumpingmount1 = (IJumpingMount)this.player.getRidingEntity();
                    int i = packetIn.getAuxData();

                    if (ijumpingmount1.canJump() && i > 0)
                    {
                        ijumpingmount1.handleStartJump(i);
                    }
                }

                break;
            case STOP_RIDING_JUMP:

                if (this.player.getRidingEntity() instanceof IJumpingMount)
                {
                    IJumpingMount ijumpingmount = (IJumpingMount)this.player.getRidingEntity();
                    ijumpingmount.handleStopJump();
                }

                break;
            case OPEN_INVENTORY:

                if (this.player.getRidingEntity() instanceof AbstractHorse)
                {
                    ((AbstractHorse)this.player.getRidingEntity()).openGUI(this.player);
                }

                break;
            case START_FALL_FLYING:

                if (!this.player.onGround && this.player.motionY < 0.0D && !this.player.isElytraFlying() && !this.player.isInWater())
                {
                    ItemStack itemstack = this.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

                    if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack))
                    {
                        this.player.setElytraFlying();
                    }
                }
                else
                {
                    this.player.clearElytraFlying();
                }

                break;
            default:
                throw new IllegalArgumentException("Invalid client command!");
        }
    }

    public void processUseEntity(CPacketUseEntity packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // Akarin
        WorldServer worldserver = this.serverController.getWorld(this.player.dimension);
        Entity entity = packetIn.getEntityFromWorld(worldserver);
        // Akarin start
        if ( entity == player && !player.isSpectator() )
        {
            disconnect( "Cannot interact with self!" );
            return;
        }
        // Akarin ed
        this.player.markPlayerActive();

        if (entity != null)
        {
            boolean flag = this.player.canEntityBeSeen(entity);
            double d0 = 36.0D;

            if (!flag)
            {
                d0 = 9.0D;
            }

            if (this.player.getDistanceSq(entity) < d0)
            {
                // Akarin start
                ItemStack itemInHand = this.player.getHeldItem(packetIn.getHand() == null ? EnumHand.MAIN_HAND : packetIn.getHand());

                if (packetIn.getAction() == CPacketUseEntity.Action.INTERACT || packetIn.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
                    boolean triggerLeashUpdate = itemInHand != null && itemInHand.getItem() == Items.LEAD && entity instanceof EntityLiving;
                    Item origItem = this.player.inventory.getCurrentItem() == null ? null : this.player.inventory.getCurrentItem().getItem();
                    PlayerInteractEntityEvent event;
                    if (packetIn.getAction() == CPacketUseEntity.Action.INTERACT) {
                        event = new PlayerInteractEntityEvent((Player) this.getPlayer(), entity.getBukkitEntity(), (packetIn.getHand() == EnumHand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
                    } else {
                        Vec3d target = packetIn.getHitVec();
                        event = new PlayerInteractAtEntityEvent((Player) this.getPlayer(), entity.getBukkitEntity(), new org.bukkit.util.Vector(target.x, target.y, target.z), (packetIn.getHand() == EnumHand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
                    }
                    this.server.getPluginManager().callEvent(event);

                    if (triggerLeashUpdate && (event.isCancelled() || this.player.inventory.getCurrentItem() == null || this.player.inventory.getCurrentItem().getItem() != Items.LEAD)) {
                        // Refresh the current leash state
                        this.sendPacket(new SPacketEntityAttach(entity, ((EntityLiving) entity).getLeashHolder()));
                    }

                    if (event.isCancelled() || this.player.inventory.getCurrentItem() == null || this.player.inventory.getCurrentItem().getItem() != origItem) {
                        // Refresh the current entity metadata
                        this.sendPacket(new SPacketEntityMetadata(entity.getEntityId(), entity.dataManager, true));
                    }

                    if (event.isCancelled()) {
                        this.player.sendContainerToPlayer(this.player.openContainer); // Refresh player inventory
                        return;
                    }
                }
                // Akarin end
                if (packetIn.getAction() == CPacketUseEntity.Action.INTERACT)
                {
                    EnumHand enumhand = packetIn.getHand();
                    this.player.interactOn(entity, enumhand);
                    // Akarin start
                    if (!itemInHand.isEmpty() && itemInHand.getCount() <= -1) {
                        this.player.sendContainerToPlayer(this.player.openContainer);
                    }
                    // Akarin end
                }
                else if (packetIn.getAction() == CPacketUseEntity.Action.INTERACT_AT)
                {
                    EnumHand enumhand1 = packetIn.getHand();
                    if(net.minecraftforge.common.ForgeHooks.onInteractEntityAt(player, entity, packetIn.getHitVec(), enumhand1) != null) return;
                    // Akarin start
                    if (!itemInHand.isEmpty() && itemInHand.getCount() <= -1) {
                        this.player.sendContainerToPlayer(this.player.openContainer);
                    }
                    // Akarin end
                    entity.applyPlayerInteraction(this.player, packetIn.getHitVec(), enumhand1);
                }
                else if (packetIn.getAction() == CPacketUseEntity.Action.ATTACK)
                {
                    if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity == this.player && !player.isSpectator()) // Akarin
                    {
                        this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_entity_attacked", new Object[0]));
                        this.serverController.logWarning("Player " + this.player.getName() + " tried to attack an invalid entity");
                        return;
                    }

                    this.player.attackTargetEntityWithCurrentItem(entity);
                    // Akarin start
                    if (!itemInHand.isEmpty() && itemInHand.getCount() <= -1) {
                        this.player.sendContainerToPlayer(this.player.openContainer);
                    }
                    // Akarin end
                }
            }
        }
    }

    public void processClientStatus(CPacketClientStatus packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        this.player.markPlayerActive();
        CPacketClientStatus.State cpacketclientstatus$state = packetIn.getStatus();

        switch (cpacketclientstatus$state)
        {
            case PERFORM_RESPAWN:

                if (this.player.queuedEndExit)
                {
                    this.player.queuedEndExit = false;
                    this.serverController.getPlayerList().changeDimension(this.player, 0, PlayerTeleportEvent.TeleportCause.END_PORTAL); // Akarin - reroute logic through custom portal management
                    CriteriaTriggers.CHANGED_DIMENSION.trigger(this.player, DimensionType.THE_END, DimensionType.OVERWORLD);
                }
                else
                {
                    if (this.player.getHealth() > 0.0F)
                    {
                        return;
                    }

                    this.player = this.serverController.getPlayerList().recreatePlayerEntity(this.player, player.dimension, false);

                    if (this.serverController.isHardcore())
                    {
                        this.player.setGameType(GameType.SPECTATOR);
                        this.player.getServerWorld().getGameRules().setOrCreateGameRule("spectatorsGenerateChunks", "false");
                    }
                }

                break;
            case REQUEST_STATS:
                this.player.getStatFile().sendStats(this.player);
        }
    }

    public void processCloseWindow(CPacketCloseWindow packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // Akarin
        CraftEventFactory.handleInventoryCloseEvent(this.player); // Akarin
        this.player.closeContainer();
    }

    public void processClickWindow(CPacketClickWindow packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        this.player.markPlayerActive();

        if (this.player.openContainer.windowId == packetIn.getWindowId() && this.player.openContainer.getCanCraft(this.player) && this.player.openContainer.canInteractWith(this.player)) { // Akarin
            boolean cancelled = this.player.isSpectator(); // Akarin - see below if
            if (false/*this.player.isSpectator()*/) // Akarin
            {
                NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>create();

                for (int i = 0; i < this.player.openContainer.inventorySlots.size(); ++i)
                {
                    nonnulllist.add(((Slot)this.player.openContainer.inventorySlots.get(i)).getStack());
                }

                this.player.sendAllContents(this.player.openContainer, nonnulllist);
            }
            else
            {
                // Akarin start - Call InventoryClickEvent
                if (packetIn.getSlotId() < -1 && packetIn.getSlotId() != -999) {
                    return;
                }

                InventoryView inventory = this.player.openContainer.getBukkitView();
                SlotType type = CraftInventoryView.getSlotType(inventory, packetIn.getSlotId());

                InventoryClickEvent event;
                ClickType click = ClickType.UNKNOWN;
                InventoryAction action = InventoryAction.UNKNOWN;

                ItemStack itemstack2 = ItemStack.EMPTY;

                switch (packetIn.getClickType()) {
                    case PICKUP:
                        if (packetIn.getUsedButton() == 0) {
                            click = ClickType.LEFT;
                        } else if (packetIn.getUsedButton() == 1) {
                            click = ClickType.RIGHT;
                        }
                        if (packetIn.getUsedButton() == 0 || packetIn.getUsedButton() == 1) {
                            action = InventoryAction.NOTHING; // Don't want to repeat ourselves
                            if (packetIn.getSlotId() == -999) {
                                if (!player.inventory.getItemStack().isEmpty()) {
                                    action = packetIn.getUsedButton() == 0 ? InventoryAction.DROP_ALL_CURSOR : InventoryAction.DROP_ONE_CURSOR;
                                }
                            } else if (packetIn.getSlotId() < 0)  {
                                action = InventoryAction.NOTHING;
                            } else {
                                Slot slot = this.player.openContainer.getSlot(packetIn.getSlotId());
                                if (slot != null) {
                                    ItemStack clickedItem = slot.getStack();
                                    ItemStack cursor = player.inventory.getItemStack();
                                    if (clickedItem.isEmpty()) {
                                        if (!cursor.isEmpty()) {
                                            action = packetIn.getUsedButton() == 0 ? InventoryAction.PLACE_ALL : InventoryAction.PLACE_ONE;
                                        }
                                    } else if (slot.canTakeStack(player)) {
                                        if (cursor.isEmpty()) {
                                            action = packetIn.getUsedButton() == 0 ? InventoryAction.PICKUP_ALL : InventoryAction.PICKUP_HALF;
                                        } else if (slot.isItemValid(cursor)) {
                                            if (clickedItem.isItemEqual(cursor) && ItemStack.areItemStackTagsEqual(clickedItem, cursor)) {
                                                int toPlace = packetIn.getUsedButton() == 0 ? cursor.getCount() : 1;
                                                toPlace = Math.min(toPlace, clickedItem.getMaxStackSize() - clickedItem.getCount());
                                                toPlace = Math.min(toPlace, slot.inventory.getInventoryStackLimit() - clickedItem.getCount());
                                                if (toPlace == 1) {
                                                    action = InventoryAction.PLACE_ONE;
                                                } else if (toPlace == cursor.getCount()) {
                                                    action = InventoryAction.PLACE_ALL;
                                                } else if (toPlace < 0) {
                                                    action = toPlace != -1 ? InventoryAction.PICKUP_SOME : InventoryAction.PICKUP_ONE; // this happens with oversized stacks
                                                } else if (toPlace != 0) {
                                                    action = InventoryAction.PLACE_SOME;
                                                }
                                            } else if (cursor.getCount() <= slot.getSlotStackLimit()) {
                                                action = InventoryAction.SWAP_WITH_CURSOR;
                                            }
                                        } else if (cursor.getItem() == clickedItem.getItem() && (!cursor.getHasSubtypes() || cursor.getMetadata() == clickedItem.getMetadata()) && ItemStack.areItemStackTagsEqual(cursor, clickedItem)) {
                                            if (clickedItem.getCount() >= 0) {
                                                if (clickedItem.getCount() + cursor.getCount() <= cursor.getMaxStackSize()) {
                                                    // As of 1.5, this is result slots only
                                                    action = InventoryAction.PICKUP_ALL;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case QUICK_MOVE:
                        if (packetIn.getUsedButton() == 0) {
                            click = ClickType.SHIFT_LEFT;
                        } else if (packetIn.getUsedButton() == 1) {
                            click = ClickType.SHIFT_RIGHT;
                        }
                        if (packetIn.getUsedButton() == 0 || packetIn.getUsedButton() == 1) {
                            if (packetIn.getSlotId() < 0) {
                                action = InventoryAction.NOTHING;
                            } else {
                                Slot slot = this.player.openContainer.getSlot(packetIn.getSlotId());
                                if (slot != null && slot.canTakeStack(this.player) && slot.getHasStack()) {
                                    action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            }
                        }
                        break;
                    case SWAP:
                        if (packetIn.getUsedButton() >= 0 && packetIn.getUsedButton() < 9) {
                            click = ClickType.NUMBER_KEY;
                            Slot clickedSlot = this.player.openContainer.getSlot(packetIn.getSlotId());
                            if (clickedSlot.canTakeStack(player)) {
                                ItemStack hotbar = this.player.inventory.getStackInSlot(packetIn.getUsedButton());
                                boolean canCleanSwap = hotbar.isEmpty() || (clickedSlot.inventory == player.inventory && clickedSlot.isItemValid(hotbar)); // the slot will accept the hotbar item
                                if (clickedSlot.getHasStack()) {
                                    if (canCleanSwap) {
                                        action = InventoryAction.HOTBAR_SWAP;
                                    } else {
                                        action = InventoryAction.HOTBAR_MOVE_AND_READD;
                                    }
                                } else if (!clickedSlot.getHasStack() && !hotbar.isEmpty() && clickedSlot.isItemValid(hotbar)) {
                                    action = InventoryAction.HOTBAR_SWAP;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            } else {
                                action = InventoryAction.NOTHING;
                            }
                        }
                        break;
                    case CLONE:
                        if (packetIn.getUsedButton() == 2) {
                            click = ClickType.MIDDLE;
                            if (packetIn.getSlotId() < 0) { // Paper - GH-404
                                action = InventoryAction.NOTHING;
                            } else {
                                Slot slot = this.player.openContainer.getSlot(packetIn.getSlotId());
                                if (slot != null && slot.getHasStack() && player.capabilities.isCreativeMode && player.inventory.getItemStack().isEmpty()) {
                                    action = InventoryAction.CLONE_STACK;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            }
                        } else {
                            click = ClickType.UNKNOWN;
                            action = InventoryAction.UNKNOWN;
                        }
                        break;
                    case THROW:
                        if (packetIn.getSlotId() >= 0) {
                            if (packetIn.getUsedButton() == 0) {
                                click = ClickType.DROP;
                                Slot slot = this.player.openContainer.getSlot(packetIn.getSlotId());
                                if (slot != null && slot.getHasStack() && slot.canTakeStack(player) && !slot.getStack().isEmpty() && slot.getStack().getItem() != Item.getItemFromBlock(Blocks.AIR)) {
                                    action = InventoryAction.DROP_ONE_SLOT;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            } else if (packetIn.getUsedButton() == 1) {
                                click = ClickType.CONTROL_DROP;
                                Slot slot = this.player.openContainer.getSlot(packetIn.getSlotId());
                                if (slot != null && slot.getHasStack() && slot.canTakeStack(player) && !slot.getStack().isEmpty() && slot.getStack().getItem() != Item.getItemFromBlock(Blocks.AIR)) {
                                    action = InventoryAction.DROP_ALL_SLOT;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            }
                        } else {
                            // Sane default (because this happens when they are holding nothing. Don't ask why.)
                            click = ClickType.LEFT;
                            if (packetIn.getUsedButton() == 1) {
                                click = ClickType.RIGHT;
                            }
                            action = InventoryAction.NOTHING;
                        }
                        break;
                    case QUICK_CRAFT:
                        itemstack2 = this.player.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getClickType(), this.player);
                        break;
                    case PICKUP_ALL:
                        click = ClickType.DOUBLE_CLICK;
                        action = InventoryAction.NOTHING;
                        if (packetIn.getSlotId() >= 0 && !this.player.inventory.getItemStack().isEmpty()) {
                            ItemStack cursor = this.player.inventory.getItemStack();
                            action = InventoryAction.NOTHING;
                            // Quick check for if we have any of the item
                            if (inventory.getTopInventory().contains(org.bukkit.Material.getMaterial(Item.getIdFromItem(cursor.getItem()))) || inventory.getBottomInventory().contains(org.bukkit.Material.getMaterial(Item.getIdFromItem(cursor.getItem())))) {
                                action = InventoryAction.COLLECT_TO_CURSOR;
                            }
                        }
                        break;
                    default:
                        break;
                }

                if (click == ClickType.NUMBER_KEY) {
                    event = new InventoryClickEvent(inventory, type, packetIn.getSlotId(), click, action, packetIn.getUsedButton());
                } else {
                    event = new InventoryClickEvent(inventory, type, packetIn.getSlotId(), click, action);
                }

                org.bukkit.inventory.Inventory top = inventory.getTopInventory();
                if (packetIn.getSlotId() == 0 && top instanceof CraftingInventory) {
                    org.bukkit.inventory.Recipe recipe = ((CraftingInventory) top).getRecipe();
                    if (recipe != null) {
                        if (click == ClickType.NUMBER_KEY) {
                            event = new CraftItemEvent(recipe, inventory, type, packetIn.getSlotId(), click, action, packetIn.getUsedButton());
                        } else {
                            event = new CraftItemEvent(recipe, inventory, type, packetIn.getSlotId(), click, action);
                        }
                    }
                }

                event.setCancelled(cancelled);
                Container oldContainer = this.player.openContainer; // SPIGOT-1224
                server.getPluginManager().callEvent(event);
                if (this.player.openContainer != oldContainer) {
                    return;
                }

                switch (event.getResult()) {
                    case ALLOW:
                    case DEFAULT:
                        itemstack2 = this.player.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getClickType(), this.player);
                        break;
                    case DENY:
                        /* Needs enum constructor in InventoryAction
                        if (action.modifiesOtherSlots()) {

                        } else {
                            if (action.modifiesCursor()) {
                                this.player.playerConnection.sendPacket(new Packet103SetSlot(-1, -1, this.player.inventory.getCarried()));
                            }
                            if (action.modifiesClicked()) {
                                this.player.playerConnection.sendPacket(new Packet103SetSlot(this.player.activeContainer.windowId, packet102windowclick.slot, this.player.activeContainer.getSlot(packet102windowclick.slot).getItem()));
                            }
                        }*/
                        switch (action) {
                            // Modified other slots
                            case PICKUP_ALL:
                            case MOVE_TO_OTHER_INVENTORY:
                            case HOTBAR_MOVE_AND_READD:
                            case HOTBAR_SWAP:
                            case COLLECT_TO_CURSOR:
                            case UNKNOWN:
                                this.player.sendContainerToPlayer(this.player.openContainer);
                                break;
                            // Modified cursor and clicked
                            case PICKUP_SOME:
                            case PICKUP_HALF:
                            case PICKUP_ONE:
                            case PLACE_ALL:
                            case PLACE_SOME:
                            case PLACE_ONE:
                            case SWAP_WITH_CURSOR:
                                this.player.connection.sendPacket(new SPacketSetSlot(-1, -1, this.player.inventory.getItemStack()));
                                this.player.connection.sendPacket(new SPacketSetSlot(this.player.openContainer.windowId, packetIn.getSlotId(), this.player.openContainer.getSlot(packetIn.getSlotId()).getStack()));
                                break;
                            // Modified clicked only
                            case DROP_ALL_SLOT:
                            case DROP_ONE_SLOT:
                                this.player.connection.sendPacket(new SPacketSetSlot(this.player.openContainer.windowId, packetIn.getSlotId(), this.player.openContainer.getSlot(packetIn.getSlotId()).getStack()));
                                break;
                            // Modified cursor only
                            case DROP_ALL_CURSOR:
                            case DROP_ONE_CURSOR:
                            case CLONE_STACK:
                                this.player.connection.sendPacket(new SPacketSetSlot(-1, -1, this.player.inventory.getItemStack()));
                                break;
                            // Nothing
                            case NOTHING:
                                break;
                        }
                        return;
                }

                if (event instanceof CraftItemEvent) {
                    // Need to update the inventory on crafting to
                    // correctly support custom recipes
                    player.sendContainerToPlayer(player.openContainer);
                }
                // Akarin end

                if (ItemStack.areItemStacksEqualUsingNBTShareTag(packetIn.getClickedItem(), itemstack2))
                {
                    this.player.connection.sendPacket(new SPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
                    this.player.isChangingQuantityOnly = true;
                    this.player.openContainer.detectAndSendChanges();
                    this.player.updateHeldItem();
                    this.player.isChangingQuantityOnly = false;
                }
                else
                {
                    this.pendingTransactions.addKey(this.player.openContainer.windowId, Short.valueOf(packetIn.getActionNumber()));
                    this.player.connection.sendPacket(new SPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
                    this.player.openContainer.setCanCraft(this.player, false);
                    NonNullList<ItemStack> nonnulllist1 = NonNullList.<ItemStack>create();

                    for (int j = 0; j < this.player.openContainer.inventorySlots.size(); ++j)
                    {
                        ItemStack itemstack = ((Slot)this.player.openContainer.inventorySlots.get(j)).getStack();
                        ItemStack itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack;
                        nonnulllist1.add(itemstack1);
                    }

                    this.player.sendAllContents(this.player.openContainer, nonnulllist1);
                }
            }
        }
    }

    public void func_194308_a(CPacketPlaceRecipe p_194308_1_)
    {
        PacketThreadUtil.checkThreadAndEnqueue(p_194308_1_, this, this.player.getServerWorld());
        this.player.markPlayerActive();

        if (!this.player.isSpectator() && this.player.openContainer.windowId == p_194308_1_.func_194318_a() && this.player.openContainer.getCanCraft(this.player))
        {
            this.field_194309_H.func_194327_a(this.player, p_194308_1_.func_194317_b(), p_194308_1_.func_194319_c());
        }
    }

    public void processEnchantItem(CPacketEnchantItem packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        this.player.markPlayerActive();

        if (this.player.openContainer.windowId == packetIn.getWindowId() && this.player.openContainer.getCanCraft(this.player) && !this.player.isSpectator())
        {
            this.player.openContainer.enchantItem(this.player, packetIn.getButton());
            this.player.openContainer.detectAndSendChanges();
        }
    }

    public void processCreativeInventoryAction(CPacketCreativeInventoryAction packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());

        if (this.player.interactionManager.isCreative())
        {
            boolean flag = packetIn.getSlotId() < 0;
            ItemStack itemstack = packetIn.getStack();

            if (!itemstack.isEmpty() && itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("BlockEntityTag", 10))
            {
                NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("BlockEntityTag");

                if (nbttagcompound.hasKey("x") && nbttagcompound.hasKey("y") && nbttagcompound.hasKey("z"))
                {
                    BlockPos blockpos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
                    TileEntity tileentity = this.player.world.getTileEntity(blockpos);

                    if (tileentity != null)
                    {
                        NBTTagCompound nbttagcompound1 = tileentity.writeToNBT(new NBTTagCompound());
                        nbttagcompound1.removeTag("x");
                        nbttagcompound1.removeTag("y");
                        nbttagcompound1.removeTag("z");
                        itemstack.setTagInfo("BlockEntityTag", nbttagcompound1);
                    }
                }
            }

            boolean flag1 = packetIn.getSlotId() >= 1 && packetIn.getSlotId() <= 45;
            // Akarin - Add invalidItems check
            boolean flag2 = itemstack.isEmpty() || itemstack.getMetadata() >= 0 && itemstack.getCount() <= 64 && !itemstack.isEmpty() && (!invalidItems.contains(Item.getIdFromItem(itemstack.getItem())) || !org.spigotmc.SpigotConfig.filterCreativeItems); // Spigot
            if (flag || (flag1 && !ItemStack.areItemStacksEqual(this.player.inventoryContainer.getSlot(packetIn.getSlotId()).getStack(), packetIn.getStack()))) { // Insist on valid slot
                // CraftBukkit start - Call click event
                InventoryView inventory = this.player.inventoryContainer.getBukkitView();
                org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(packetIn.getStack());

                SlotType type = SlotType.QUICKBAR;
                if (flag) {
                    type = SlotType.OUTSIDE;
                } else if (packetIn.getSlotId() < 36) {
                    if (packetIn.getSlotId() >= 5 && packetIn.getSlotId() < 9) {
                        type = SlotType.ARMOR;
                    } else {
                        type = SlotType.CONTAINER;
                    }
                }
                InventoryCreativeEvent event = new InventoryCreativeEvent(inventory, type, flag ? -999 : packetIn.getSlotId(), item);
                server.getPluginManager().callEvent(event);

                itemstack = CraftItemStack.asNMSCopy(event.getCursor());

                switch (event.getResult()) {
                case ALLOW:
                    // Plugin cleared the id / stacksize checks
                    flag2 = true;
                    break;
                case DEFAULT:
                    break;
                case DENY:
                    // Reset the slot
                    if (packetIn.getSlotId() >= 0) {
                        this.player.connection.sendPacket(new SPacketSetSlot(this.player.inventoryContainer.windowId, packetIn.getSlotId(), this.player.inventoryContainer.getSlot(packetIn.getSlotId()).getStack()));
                        this.player.connection.sendPacket(new SPacketSetSlot(-1, -1, ItemStack.EMPTY));
                    }
                    return;
                }
            }
            // Akarin end

            if (flag1 && flag2)
            {
                if (itemstack.isEmpty())
                {
                    this.player.inventoryContainer.putStackInSlot(packetIn.getSlotId(), ItemStack.EMPTY);
                }
                else
                {
                    this.player.inventoryContainer.putStackInSlot(packetIn.getSlotId(), itemstack);
                }

                this.player.inventoryContainer.setCanCraft(this.player, true);
            }
            else if (flag && flag2 && this.itemDropThreshold < 200)
            {
                this.itemDropThreshold += 20;
                EntityItem entityitem = this.player.dropItem(itemstack, true);

                if (entityitem != null)
                {
                    entityitem.setAgeToCreativeDespawnTime();
                }
            }
        }
    }

    public void processConfirmTransaction(CPacketConfirmTransaction packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // Akarin
        Short oshort = this.pendingTransactions.lookup(this.player.openContainer.windowId);

        if (oshort != null && packetIn.getUid() == oshort.shortValue() && this.player.openContainer.windowId == packetIn.getWindowId() && !this.player.openContainer.getCanCraft(this.player) && !this.player.isSpectator())
        {
            this.player.openContainer.setCanCraft(this.player, true);
        }
    }

    public void processUpdateSign(CPacketUpdateSign packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // Akarin
        this.player.markPlayerActive();
        WorldServer worldserver = this.serverController.getWorld(this.player.dimension);
        BlockPos blockpos = packetIn.getPosition();

        if (worldserver.isBlockLoaded(blockpos))
        {
            IBlockState iblockstate = worldserver.getBlockState(blockpos);
            TileEntity tileentity = worldserver.getTileEntity(blockpos);

            if (!(tileentity instanceof TileEntitySign))
            {
                return;
            }

            TileEntitySign tileentitysign = (TileEntitySign)tileentity;

            if (!tileentitysign.getIsEditable() || tileentitysign.getPlayer() != this.player)
            {
                this.serverController.logWarning("Player " + this.player.getName() + " just tried to change non-editable sign");
                this.sendPacket(tileentity.getUpdatePacket()); // Akarin
                return;
            }

            String[] astring = packetIn.getLines();

            // CraftBukkit start
            Player player = this.server.getPlayer(this.player);
            int x = packetIn.getPosition().getX();
            int y = packetIn.getPosition().getY();
            int z = packetIn.getPosition().getZ();
            String[] lines = new String[4];

            for (int i = 0; i < astring.length; ++i) {
                lines[i] = ChatAllowedCharacters.filterAllowedCharacters(astring[i]); // Replaced with anvil color stripping method to stop exploits that allow colored signs to be created.
            }
            SignChangeEvent event = new SignChangeEvent((org.bukkit.craftbukkit.block.CraftBlock) player.getWorld().getBlockAt(x, y, z), this.server.getPlayer(this.player), lines);
            this.server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                System.arraycopy(org.bukkit.craftbukkit.block.CraftSign.sanitizeLines(event.getLines()), 0, tileentitysign.signText, 0, 4);
                tileentitysign.isEditable = false;
             }
            // CraftBukkit end

            tileentitysign.markDirty();
            worldserver.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
        }
    }

    public void processKeepAlive(CPacketKeepAlive packetIn)
    {
        if (this.field_194403_g && packetIn.getKey() == this.field_194404_h)
        {
            int i = (int)(this.currentTimeMillis() - this.field_194402_f);
            this.player.ping = (this.player.ping * 3 + i) / 4;
            this.field_194403_g = false;
        }
        else if (!this.player.getName().equals(this.serverController.getServerOwner()))
        {
            this.disconnect(new TextComponentTranslation("disconnect.timeout", new Object[0]));
        }
    }

    private long currentTimeMillis()
    {
        return System.nanoTime() / 1000000L;
    }

    public void processPlayerAbilities(CPacketPlayerAbilities packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        // CraftBukkit start
        if (this.player.capabilities.allowFlying && this.player.capabilities.isFlying != packetIn.isFlying()) {
            PlayerToggleFlightEvent event = new PlayerToggleFlightEvent(this.server.getPlayer(this.player), packetIn.isFlying());
            this.server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.player.capabilities.isFlying = packetIn.isFlying(); // Actually set the player's flying status
            } else {
                this.player.sendPlayerAbilities(); // Tell the player their ability was reverted
            }
        }
        // CraftBukkit end
    }

    public void processTabComplete(CPacketTabComplete packetIn)
    {
        // Akarin start
        if (chatSpamField.addAndGet(this, 10) > 500 && !this.serverController.getPlayerList().canSendCommands(this.player.getGameProfile())) {
            serverController.addScheduledTask(() -> this.disconnect(new TextComponentTranslation("disconnect.spam", new Object[0])));
            return;
        }
        // Akarin end
        List<String> list = Lists.<String>newArrayList();

        for (String s : this.serverController.getTabCompletions(this.player, packetIn.getMessage(), packetIn.getTargetBlock(), packetIn.hasTargetBlock()))
        {
            list.add(s);
        }

        this.player.connection.sendPacket(new SPacketTabComplete((String[])list.toArray(new String[list.size()])));
    }

    public void processClientSettings(CPacketClientSettings packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        this.player.handleClientSettings(packetIn);
    }

    public void processCustomPayload(CPacketCustomPayload packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        String s = packetIn.getChannelName();

        if ("MC|BEdit".equals(s))
        {
            // Akarin start
            if (this.lastBookTick + 20 > MinecraftServer.currentTick) {
                this.disconnect("Book edited too quickly!");
                return;
            }
            this.lastBookTick = MinecraftServer.currentTick;
            // Akarin end
            PacketBuffer packetbuffer = packetIn.getBufferData();

            try
            {
                ItemStack itemstack = packetbuffer.readItemStack();

                if (itemstack.isEmpty())
                {
                    return;
                }

                if (!ItemWritableBook.isNBTValid(itemstack.getTagCompound()))
                {
                    throw new IOException("Invalid book tag!");
                }

                ItemStack itemstack1 = this.player.getHeldItemMainhand();

                if (itemstack1.isEmpty())
                {
                    return;
                }

                if (itemstack.getItem() == Items.WRITABLE_BOOK && itemstack.getItem() == itemstack1.getItem())
                {
                    itemstack1.setTagInfo("pages", itemstack.getTagCompound().getTagList("pages", 8));
                    CraftEventFactory.handleEditBookEvent(player, itemstack1); // Akarin
                }
            }
            catch (Exception exception6)
            {
                LOGGER.error("Couldn't handle book info", (Throwable)exception6);
                this.disconnect("Invalid book data!"); // Akarin
            }
        }
        else if ("MC|BSign".equals(s))
        {
            // Akarin start
            if (this.lastBookTick + 20 > MinecraftServer.currentTick) {
                this.disconnect("Book edited too quickly!");
                return;
            }
            this.lastBookTick = MinecraftServer.currentTick;
            // Akarin end
            PacketBuffer packetbuffer1 = packetIn.getBufferData();

            try
            {
                ItemStack itemstack3 = packetbuffer1.readItemStack();

                if (itemstack3.isEmpty())
                {
                    return;
                }

                if (!ItemWrittenBook.validBookTagContents(itemstack3.getTagCompound()))
                {
                    throw new IOException("Invalid book tag!");
                }

                ItemStack itemstack4 = this.player.getHeldItemMainhand();

                if (itemstack4.isEmpty())
                {
                    return;
                }

                if (itemstack3.getItem() == Items.WRITABLE_BOOK && itemstack4.getItem() == Items.WRITABLE_BOOK)
                {
                    ItemStack itemstack2 = new ItemStack(Items.WRITTEN_BOOK);
                    itemstack2.setTagInfo("author", new NBTTagString(this.player.getName()));
                    itemstack2.setTagInfo("title", new NBTTagString(itemstack3.getTagCompound().getString("title")));
                    NBTTagList nbttaglist = itemstack3.getTagCompound().getTagList("pages", 8);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i)
                    {
                        String s1 = nbttaglist.getStringTagAt(i);
                        ITextComponent itextcomponent = new TextComponentString(s1);
                        s1 = ITextComponent.Serializer.componentToJson(itextcomponent);
                        nbttaglist.set(i, new NBTTagString(s1));
                    }

                    itemstack2.setTagInfo("pages", nbttaglist);
                    CraftEventFactory.handleEditBookEvent(player, itemstack2); // Akarin
                }
            }
            catch (Exception exception7)
            {
                LOGGER.error("Couldn't sign book", (Throwable)exception7);
                this.disconnect("Invalid book data!"); // Akarin
            }
        }
        else if ("MC|TrSel".equals(s))
        {
            try
            {
                int k = packetIn.getBufferData().readInt();
                Container container = this.player.openContainer;

                if (container instanceof ContainerMerchant)
                {
                    ((ContainerMerchant)container).setCurrentRecipeIndex(k);
                }
            }
            catch (Exception exception5)
            {
                LOGGER.error("Couldn't select trade", (Throwable)exception5);
                this.disconnect("Invalid trade data!"); // Akarin
            }
        }
        else if ("MC|AdvCmd".equals(s))
        {
            if (!this.serverController.isCommandBlockEnabled())
            {
                this.player.sendMessage(new TextComponentTranslation("advMode.notEnabled", new Object[0]));
                return;
            }

            if (!this.player.canUseCommandBlock())
            {
                this.player.sendMessage(new TextComponentTranslation("advMode.notAllowed", new Object[0]));
                return;
            }

            PacketBuffer packetbuffer2 = packetIn.getBufferData();

            try
            {
                int l = packetbuffer2.readByte();
                CommandBlockBaseLogic commandblockbaselogic1 = null;

                if (l == 0)
                {
                    TileEntity tileentity = this.player.world.getTileEntity(new BlockPos(packetbuffer2.readInt(), packetbuffer2.readInt(), packetbuffer2.readInt()));

                    if (tileentity instanceof TileEntityCommandBlock)
                    {
                        commandblockbaselogic1 = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic();
                    }
                }
                else if (l == 1)
                {
                    Entity entity = this.player.world.getEntityByID(packetbuffer2.readInt());

                    if (entity instanceof EntityMinecartCommandBlock)
                    {
                        commandblockbaselogic1 = ((EntityMinecartCommandBlock)entity).getCommandBlockLogic();
                    }
                }

                String s6 = packetbuffer2.readString(packetbuffer2.readableBytes());
                boolean flag2 = packetbuffer2.readBoolean();

                if (commandblockbaselogic1 != null)
                {
                    commandblockbaselogic1.setCommand(s6);
                    commandblockbaselogic1.setTrackOutput(flag2);

                    if (!flag2)
                    {
                        commandblockbaselogic1.setLastOutput((ITextComponent)null);
                    }

                    commandblockbaselogic1.updateCommand();
                    this.player.sendMessage(new TextComponentTranslation("advMode.setCommand.success", new Object[] {s6}));
                }
            }
            catch (Exception exception4)
            {
                LOGGER.error("Couldn't set command block", (Throwable)exception4);
                this.disconnect("Invalid command data!"); // Akarin
            }
        }
        else if ("MC|AutoCmd".equals(s))
        {
            if (!this.serverController.isCommandBlockEnabled())
            {
                this.player.sendMessage(new TextComponentTranslation("advMode.notEnabled", new Object[0]));
                return;
            }

            if (!this.player.canUseCommandBlock())
            {
                this.player.sendMessage(new TextComponentTranslation("advMode.notAllowed", new Object[0]));
                return;
            }

            PacketBuffer packetbuffer3 = packetIn.getBufferData();

            try
            {
                CommandBlockBaseLogic commandblockbaselogic = null;
                TileEntityCommandBlock tileentitycommandblock = null;
                BlockPos blockpos1 = new BlockPos(packetbuffer3.readInt(), packetbuffer3.readInt(), packetbuffer3.readInt());
                TileEntity tileentity2 = this.player.world.getTileEntity(blockpos1);

                if (tileentity2 instanceof TileEntityCommandBlock)
                {
                    tileentitycommandblock = (TileEntityCommandBlock)tileentity2;
                    commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();
                }

                String s7 = packetbuffer3.readString(packetbuffer3.readableBytes());
                boolean flag3 = packetbuffer3.readBoolean();
                TileEntityCommandBlock.Mode tileentitycommandblock$mode = TileEntityCommandBlock.Mode.valueOf(packetbuffer3.readString(16));
                boolean flag = packetbuffer3.readBoolean();
                boolean flag1 = packetbuffer3.readBoolean();

                if (commandblockbaselogic != null)
                {
                    EnumFacing enumfacing = (EnumFacing)this.player.world.getBlockState(blockpos1).getValue(BlockCommandBlock.FACING);

                    switch (tileentitycommandblock$mode)
                    {
                        case SEQUENCE:
                            IBlockState iblockstate3 = Blocks.CHAIN_COMMAND_BLOCK.getDefaultState();
                            this.player.world.setBlockState(blockpos1, iblockstate3.withProperty(BlockCommandBlock.FACING, enumfacing).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag)), 2);
                            break;
                        case AUTO:
                            IBlockState iblockstate2 = Blocks.REPEATING_COMMAND_BLOCK.getDefaultState();
                            this.player.world.setBlockState(blockpos1, iblockstate2.withProperty(BlockCommandBlock.FACING, enumfacing).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag)), 2);
                            break;
                        case REDSTONE:
                            IBlockState iblockstate = Blocks.COMMAND_BLOCK.getDefaultState();
                            this.player.world.setBlockState(blockpos1, iblockstate.withProperty(BlockCommandBlock.FACING, enumfacing).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag)), 2);
                    }

                    tileentity2.validate();
                    this.player.world.setTileEntity(blockpos1, tileentity2);
                    commandblockbaselogic.setCommand(s7);
                    commandblockbaselogic.setTrackOutput(flag3);

                    if (!flag3)
                    {
                        commandblockbaselogic.setLastOutput((ITextComponent)null);
                    }

                    tileentitycommandblock.setAuto(flag1);
                    commandblockbaselogic.updateCommand();

                    if (!net.minecraft.util.StringUtils.isNullOrEmpty(s7))
                    {
                        this.player.sendMessage(new TextComponentTranslation("advMode.setCommand.success", new Object[] {s7}));
                    }
                }
            }
            catch (Exception exception3)
            {
                LOGGER.error("Couldn't set command block", (Throwable)exception3);
                this.disconnect("Invalid command data!"); // Akarin
            }
        }
        else if ("MC|Beacon".equals(s))
        {
            if (this.player.openContainer instanceof ContainerBeacon)
            {
                try
                {
                    PacketBuffer packetbuffer4 = packetIn.getBufferData();
                    int i1 = packetbuffer4.readInt();
                    int k1 = packetbuffer4.readInt();
                    ContainerBeacon containerbeacon = (ContainerBeacon)this.player.openContainer;
                    Slot slot = containerbeacon.getSlot(0);

                    if (slot.getHasStack())
                    {
                        slot.decrStackSize(1);
                        IInventory iinventory = containerbeacon.getTileEntity();
                        iinventory.setField(1, i1);
                        iinventory.setField(2, k1);
                        iinventory.markDirty();
                    }
                }
                catch (Exception exception2)
                {
                    LOGGER.error("Couldn't set beacon", (Throwable)exception2);
                    this.disconnect("Invalid beacon data!"); // Akarin
                }
            }
        }
        else if ("MC|ItemName".equals(s))
        {
            if (this.player.openContainer instanceof ContainerRepair)
            {
                ContainerRepair containerrepair = (ContainerRepair)this.player.openContainer;

                if (packetIn.getBufferData() != null && packetIn.getBufferData().readableBytes() >= 1)
                {
                    String s5 = ChatAllowedCharacters.filterAllowedCharacters(packetIn.getBufferData().readString(32767));

                    if (s5.length() <= 35)
                    {
                        containerrepair.updateItemName(s5);
                    }
                }
                else
                {
                    containerrepair.updateItemName("");
                }
            }
        }
        else if ("MC|Struct".equals(s))
        {
            if (!this.player.canUseCommandBlock())
            {
                return;
            }

            PacketBuffer packetbuffer5 = packetIn.getBufferData();

            try
            {
                BlockPos blockpos = new BlockPos(packetbuffer5.readInt(), packetbuffer5.readInt(), packetbuffer5.readInt());
                IBlockState iblockstate1 = this.player.world.getBlockState(blockpos);
                TileEntity tileentity1 = this.player.world.getTileEntity(blockpos);

                if (tileentity1 instanceof TileEntityStructure)
                {
                    TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity1;
                    int l1 = packetbuffer5.readByte();
                    String s8 = packetbuffer5.readString(32);
                    tileentitystructure.setMode(TileEntityStructure.Mode.valueOf(s8));
                    tileentitystructure.setName(packetbuffer5.readString(64));
                    int i2 = MathHelper.clamp(packetbuffer5.readInt(), -32, 32);
                    int j2 = MathHelper.clamp(packetbuffer5.readInt(), -32, 32);
                    int k2 = MathHelper.clamp(packetbuffer5.readInt(), -32, 32);
                    tileentitystructure.setPosition(new BlockPos(i2, j2, k2));
                    int l2 = MathHelper.clamp(packetbuffer5.readInt(), 0, 32);
                    int i3 = MathHelper.clamp(packetbuffer5.readInt(), 0, 32);
                    int j = MathHelper.clamp(packetbuffer5.readInt(), 0, 32);
                    tileentitystructure.setSize(new BlockPos(l2, i3, j));
                    String s2 = packetbuffer5.readString(32);
                    tileentitystructure.setMirror(Mirror.valueOf(s2));
                    String s3 = packetbuffer5.readString(32);
                    tileentitystructure.setRotation(Rotation.valueOf(s3));
                    tileentitystructure.setMetadata(packetbuffer5.readString(128));
                    tileentitystructure.setIgnoresEntities(packetbuffer5.readBoolean());
                    tileentitystructure.setShowAir(packetbuffer5.readBoolean());
                    tileentitystructure.setShowBoundingBox(packetbuffer5.readBoolean());
                    tileentitystructure.setIntegrity(MathHelper.clamp(packetbuffer5.readFloat(), 0.0F, 1.0F));
                    tileentitystructure.setSeed(packetbuffer5.readVarLong());
                    String s4 = tileentitystructure.getName();

                    if (l1 == 2)
                    {
                        if (tileentitystructure.save())
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.save_success", new Object[] {s4}), false);
                        }
                        else
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.save_failure", new Object[] {s4}), false);
                        }
                    }
                    else if (l1 == 3)
                    {
                        if (!tileentitystructure.isStructureLoadable())
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.load_not_found", new Object[] {s4}), false);
                        }
                        else if (tileentitystructure.load())
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.load_success", new Object[] {s4}), false);
                        }
                        else
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.load_prepare", new Object[] {s4}), false);
                        }
                    }
                    else if (l1 == 4)
                    {
                        if (tileentitystructure.detectSize())
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.size_success", new Object[] {s4}), false);
                        }
                        else
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.size_failure", new Object[0]), false);
                        }
                    }

                    tileentitystructure.markDirty();
                    this.player.world.notifyBlockUpdate(blockpos, iblockstate1, iblockstate1, 3);
                }
            }
            catch (Exception exception1)
            {
                LOGGER.error("Couldn't set structure block", (Throwable)exception1);
                this.disconnect("Invalid structure data!"); // Akarin
            }
        }
        else if ("MC|PickItem".equals(s))
        {
            PacketBuffer packetbuffer6 = packetIn.getBufferData();

            try
            {
                int j1 = packetbuffer6.readVarInt();
                this.player.inventory.pickItem(j1);
                this.player.connection.sendPacket(new SPacketSetSlot(-2, this.player.inventory.currentItem, this.player.inventory.getStackInSlot(this.player.inventory.currentItem)));
                this.player.connection.sendPacket(new SPacketSetSlot(-2, j1, this.player.inventory.getStackInSlot(j1)));
                this.player.connection.sendPacket(new SPacketHeldItemChange(this.player.inventory.currentItem));
            }
            catch (Exception exception)
            {
                LOGGER.error("Couldn't pick item", (Throwable)exception);
                this.disconnect("Invalid pick item!"); // CraftBukkit
            }
        }
        // Akarin start
        else if (packetIn.getChannelName().equals("REGISTER")) {
            try {
                String channels = packetIn.getBufferData().toString(com.google.common.base.Charsets.UTF_8);
                for (String channel : channels.split("\0")) {
                    getPlayer().addChannel(channel);
                }
            } catch (Exception ex) {
                NetHandlerPlayServer.LOGGER.error("Couldn\'t register custom payload", ex);
                this.disconnect("Invalid payload REGISTER!");
            }
        } else if (packetIn.getChannelName().equals("UNREGISTER")) {
            try {
                String channels = packetIn.getBufferData().toString(com.google.common.base.Charsets.UTF_8);
                for (String channel : channels.split("\0")) {
                    getPlayer().removeChannel(channel);
                }
            } catch (Exception ex) {
                NetHandlerPlayServer.LOGGER.error("Couldn\'t unregister custom payload", ex);
                this.disconnect("Invalid payload UNREGISTER!");
            }
        } else {
            try {
                byte[] data = new byte[packetIn.getBufferData().readableBytes()];
                packetIn.getBufferData().readBytes(data);
                server.getMessenger().dispatchIncomingMessage(player.getBukkitEntity(), packetIn.getChannelName(), data);
            } catch (Exception ex) {
                NetHandlerPlayServer.LOGGER.error("Couldn\'t dispatch custom payload", ex);
                this.disconnect("Invalid custom payload!");
            }
        }
    }

    public final boolean isDisconnected() {
        return (!this.player.joining && !this.netManager.isChannelOpen()) || this.processedDisconnect;
    }
    // Akarin end
}