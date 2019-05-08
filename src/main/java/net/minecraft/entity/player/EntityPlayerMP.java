package net.minecraft.entity.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.stats.RecipeBookServer;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.CooldownTrackerServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.FoodStats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.GameType;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.MainHand;

public class EntityPlayerMP extends EntityPlayer implements IContainerListener
{
    private static final Logger LOGGER = LogManager.getLogger();
    public String language = "en_us"; // Akarin
    public NetHandlerPlayServer connection;
    public final MinecraftServer mcServer;
    public final PlayerInteractionManager interactionManager;
    public double managedPosX;
    public double managedPosZ;
    private final List<Integer> entityRemoveQueue = Lists.<Integer>newLinkedList();
    private final PlayerAdvancements advancements;
    private final StatisticsManagerServer statsFile;
    private float lastHealthScore = Float.MIN_VALUE;
    private int lastFoodScore = Integer.MIN_VALUE;
    private int lastAirScore = Integer.MIN_VALUE;
    private int lastArmorScore = Integer.MIN_VALUE;
    private int lastLevelScore = Integer.MIN_VALUE;
    private int lastExperienceScore = Integer.MIN_VALUE;
    private float lastHealth = -1.0E8F;
    private int lastFoodLevel = -99999999;
    private boolean wasHungry = true;
    public int lastExperience = -99999999;
    public int respawnInvulnerabilityTicks = 60; // Akarin
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private boolean chatColours = true;
    private long playerLastActiveTime = System.currentTimeMillis();
    private Entity spectatingEntity;
    public boolean invulnerableDimensionChange; // Akarin
    private boolean seenCredits;
    private final RecipeBookServer recipeBook = new RecipeBookServer();
    private Vec3d levitationStartPos;
    private int levitatingSince;
    private boolean disconnected;
    private Vec3d enteredNetherPosition;
    public int currentWindowId;
    public boolean isChangingQuantityOnly;
    public int ping;
    public boolean queuedEndExit;
    // Akarin start
    public String displayName;
    public ITextComponent listName;
    public Location compassTarget;
    public int newExp = 0;
    public int newLevel = 0;
    public int newTotalExp = 0;
    public boolean keepLevel = false;
    public double maxHealthCache;
    public boolean joining = true;
    public boolean sentListPacket = false;
    public long timeOffset = 0;
    public boolean relativeTime = true;
    public WeatherType weather;
    private float pluginRainPosition;
    private float pluginRainPositionPrevious;
    
    public final BlockPos getSpawnPoint(MinecraftServer minecraftserver, WorldServer worldserver) {
        BlockPos blockposition = worldserver.getSpawnPoint();

        if (worldserver.provider.hasSkyLight() && worldserver.getWorldInfo().getGameType() != GameType.ADVENTURE) {
            int i = Math.max(0, minecraftserver.getSpawnRadius(worldserver));
            int j = MathHelper.floor(worldserver.getWorldBorder().getClosestDistance((double) blockposition.getX(), (double) blockposition.getZ()));

            if (j < i) {
                i = j;
            }

            if (j <= 1) {
                i = 1;
            }

            blockposition = worldserver.getTopSolidOrLiquidBlock(blockposition.add(this.rand.nextInt(i * 2 + 1) - i, 0, this.rand.nextInt(i * 2 + 1) - i));
        }

        return blockposition;
    }
    
    public void setWorld(World world) {
        super.setWorld(world);
        if (world == null) {
            this.isDead = false;
            BlockPos position = null;
            if (this.spawnWorld != null && !this.spawnWorld.equals("")) {
                CraftWorld cworld = (CraftWorld) Bukkit.getServer().getWorld(this.spawnWorld);
                if (cworld != null && this.getBedLocation() != null) {
                    world = cworld.getHandle();
                    position = EntityPlayer.getBedSpawnLocation(cworld.getHandle(), this.getBedLocation(), false);
                }
            }
            if (world == null || position == null) {
                world = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
                position = world.getSpawnPoint();
            }
            this.world = world;
            this.setPosition(position.getX() + 0.5, position.getY(), position.getZ() + 0.5);
        }
        this.dimension = ((WorldServer) this.world).dimension;
        this.interactionManager.setWorld((WorldServer) world);
    }

    public long getPlayerTime() {
        if (this.relativeTime) {
            // Adds timeOffset to the current server time.
            return this.world.getWorldTime() + this.timeOffset;
        } else {
            // Adds timeOffset to the beginning of this day.
            return this.world.getWorldTime() - (this.world.getWorldTime() % 24000) + this.timeOffset;
        }
    }

    public WeatherType getPlayerWeather() {
        return this.weather;
    }

    public void setPlayerWeather(WeatherType type, boolean plugin) {
        if (!plugin && this.weather != null) {
            return;
        }

        if (plugin) {
            this.weather = type;
        }

        if (type == WeatherType.DOWNFALL) {
            this.connection.sendPacket(new SPacketChangeGameState(2, 0));
        } else {
            this.connection.sendPacket(new SPacketChangeGameState(1, 0));
        }
    }

    public void updateWeather(float oldRain, float newRain, float oldThunder, float newThunder) {
        if (this.weather == null) {
            // Vanilla
            if (oldRain != newRain) {
                this.connection.sendPacket(new SPacketChangeGameState(7, newRain));
            }
        } else {
            // Plugin
            if (pluginRainPositionPrevious != pluginRainPosition) {
                this.connection.sendPacket(new SPacketChangeGameState(7, pluginRainPosition));
            }
        }

        if (oldThunder != newThunder) {
            if (weather == WeatherType.DOWNFALL || weather == null) {
                this.connection.sendPacket(new SPacketChangeGameState(8, newThunder));
            } else {
                this.connection.sendPacket(new SPacketChangeGameState(8, 0));
            }
        }
    }

    public void tickWeather() {
        if (this.weather == null) return;

        pluginRainPositionPrevious = pluginRainPosition;
        if (weather == WeatherType.DOWNFALL) {
            pluginRainPosition += 0.01;
        } else {
            pluginRainPosition -= 0.01;
        }

        pluginRainPosition = MathHelper.clamp(pluginRainPosition, 0.0F, 1.0F);
    }

    public void resetPlayerWeather() {
        this.weather = null;
        this.setPlayerWeather(this.world.getWorldInfo().isRaining() ? WeatherType.DOWNFALL : WeatherType.CLEAR, false);
    }

    @Override
    public String toString() {
        return super.toString() + "(" + this.getName() + " at " + this.posX + "," + this.posY + "," + this.posZ + ")";
    }

    // SPIGOT-1903, MC-98153
    public void forceSetPositionRotation(double x, double y, double z, float yaw, float pitch) {
        this.setLocationAndAngles(x, y, z, yaw, pitch);
        this.connection.captureCurrentPosition();
    }

    @Override
    public boolean isMovementBlocked() { // Akarin
        return super.isMovementBlocked() || this.getBukkitEntity().isOnline();
    }

    @Override
    public Scoreboard getWorldScoreboard() {
        return getBukkitEntity().getScoreboard().getHandle();
    }

    public void reset() {
        float exp = 0;
        boolean keepInventory = this.world.getGameRules().getBoolean("keepInventory");

        if (this.keepLevel || keepInventory) {
            exp = this.experience;
            this.newTotalExp = this.experienceTotal;
            this.newLevel = this.experienceLevel;
        }

        this.setHealth(this.getMaxHealth());
        this.fire = 0;
        this.fallDistance = 0;
        this.foodStats = new FoodStats(this);
        this.experienceLevel = this.newLevel;
        this.experienceTotal = this.newTotalExp;
        this.experience = 0;
        this.deathTime = 0;
        this.setArrowCountInEntity(0);
        this.clearActivePotions();
        this.potionsNeedUpdate = true;
        this.openContainer = this.inventoryContainer;
        this.attackingPlayer = null;
        this.revengeTarget = null;
        this._combatTracker = new CombatTracker(this);
        this.lastExperience = -1;
        if (this.keepLevel || keepInventory) {
            this.experience = exp;
        } else {
            this.addExperience(this.newExp);
        }
        this.keepLevel = false;
    }

    @Override
    public CraftPlayer getBukkitEntity() {
        return (CraftPlayer) super.getBukkitEntity();
    }
    // Akarin end

    public EntityPlayerMP(MinecraftServer server, WorldServer worldIn, GameProfile profile, PlayerInteractionManager interactionManagerIn)
    {
        super(worldIn, profile);
        interactionManagerIn.player = this;
        this.interactionManager = interactionManagerIn;
        BlockPos blockpos = worldIn.provider.getSpawnPoint(); // Akarin

        if (false && worldIn.provider.hasSkyLight() && worldIn.getWorldInfo().getGameType() != GameType.ADVENTURE)
        {
            int i = Math.max(0, server.getSpawnRadius(worldIn));
            int j = MathHelper.floor(worldIn.getWorldBorder().getClosestDistance((double)blockpos.getX(), (double)blockpos.getZ()));

            if (j < i)
            {
                i = j;
            }

            if (j <= 1)
            {
                i = 1;
            }

            blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos.add(this.rand.nextInt(i * 2 + 1) - i, 0, this.rand.nextInt(i * 2 + 1) - i));
        }

        this.mcServer = server;
        this.statsFile = server.getPlayerList().getPlayerStatsFile(this);
        this.advancements = server.getPlayerList().getPlayerAdvancements(this);
        this.stepHeight = 1.0F;
        this.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);

        while (!worldIn.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.posY < 255.0D)
        {
            this.setPosition(this.posX, this.posY + 1.0D, this.posZ);
        }
        // Akarin start
        this.displayName = this.getName();
        this.canPickUpLoot = true;
        this.maxHealthCache = this.getMaxHealth();
        // Akarin end
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("playerGameType", 99))
        {
            if (this.getServer().getForceGamemode())
            {
                this.interactionManager.setGameType(this.getServer().getGameType());
            }
            else
            {
                this.interactionManager.setGameType(GameType.getByID(compound.getInteger("playerGameType")));
            }
        }

        if (compound.hasKey("enteredNetherPosition", 10))
        {
            NBTTagCompound nbttagcompound = compound.getCompoundTag("enteredNetherPosition");
            this.enteredNetherPosition = new Vec3d(nbttagcompound.getDouble("x"), nbttagcompound.getDouble("y"), nbttagcompound.getDouble("z"));
        }

        this.seenCredits = compound.getBoolean("seenCredits");

        if (compound.hasKey("recipeBook", 10))
        {
            this.recipeBook.read(compound.getCompoundTag("recipeBook"));
        }
        this.getBukkitEntity().readExtraData(compound); // Akarin
    }

    public static void registerFixesPlayerMP(DataFixer p_191522_0_)
    {
        p_191522_0_.registerWalker(FixTypes.PLAYER, new IDataWalker()
        {
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn)
            {
                if (compound.hasKey("RootVehicle", 10))
                {
                    NBTTagCompound nbttagcompound = compound.getCompoundTag("RootVehicle");

                    if (nbttagcompound.hasKey("Entity", 10))
                    {
                        nbttagcompound.setTag("Entity", fixer.process(FixTypes.ENTITY, nbttagcompound.getCompoundTag("Entity"), versionIn));
                    }
                }

                return compound;
            }
        });
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("playerGameType", this.interactionManager.getGameType().getID());
        compound.setBoolean("seenCredits", this.seenCredits);

        if (this.enteredNetherPosition != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setDouble("x", this.enteredNetherPosition.x);
            nbttagcompound.setDouble("y", this.enteredNetherPosition.y);
            nbttagcompound.setDouble("z", this.enteredNetherPosition.z);
            compound.setTag("enteredNetherPosition", nbttagcompound);
        }

        Entity entity1 = this.getLowestRidingEntity();
        Entity entity = this.getRidingEntity();

        if (entity != null && entity1 != this && entity1.getRecursivePassengersByType(EntityPlayerMP.class).size() == 1)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            entity1.writeToNBTOptional(nbttagcompound2);
            nbttagcompound1.setUniqueId("Attach", entity.getUniqueID());
            nbttagcompound1.setTag("Entity", nbttagcompound2);
            compound.setTag("RootVehicle", nbttagcompound1);
        }

        compound.setTag("recipeBook", this.recipeBook.write());
        this.getBukkitEntity().setExtraData(compound); // Akarin
    }

    public void addExperienceLevel(int levels)
    {
        super.addExperienceLevel(levels);
        this.lastExperience = -1;
    }

    public void onEnchant(ItemStack enchantedItem, int cost)
    {
        super.onEnchant(enchantedItem, cost);
        this.lastExperience = -1;
    }

    public void addSelfToInternalCraftingInventory()
    {
        this.openContainer.addListener(this);
    }

    public void sendEnterCombat()
    {
        super.sendEnterCombat();
        this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.ENTER_COMBAT));
    }

    public void sendEndCombat()
    {
        super.sendEndCombat();
        this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.END_COMBAT));
    }

    protected void onInsideBlock(IBlockState p_191955_1_)
    {
        CriteriaTriggers.ENTER_BLOCK.trigger(this, p_191955_1_);
    }

    protected CooldownTracker createCooldownTracker()
    {
        return new CooldownTrackerServer(this);
    }

    public void onUpdate()
    {
        if (this.joining) this.joining = false; // Akarin
        this.interactionManager.updateBlockRemoving();
        --this.respawnInvulnerabilityTicks;

        if (this.hurtResistantTime > 0)
        {
            --this.hurtResistantTime;
        }

        this.openContainer.detectAndSendChanges();

        if (!this.world.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this))
        {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }

        while (!this.entityRemoveQueue.isEmpty())
        {
            int i = Math.min(this.entityRemoveQueue.size(), Integer.MAX_VALUE);
            int[] aint = new int[i];
            Iterator<Integer> iterator = this.entityRemoveQueue.iterator();
            int j = 0;

            while (iterator.hasNext() && j < i)
            {
                aint[j++] = ((Integer)iterator.next()).intValue();
                iterator.remove();
            }

            this.connection.sendPacket(new SPacketDestroyEntities(aint));
        }

        Entity entity = this.getSpectatingEntity();

        if (entity != this)
        {
            if (entity.isEntityAlive())
            {
                this.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                this.mcServer.getPlayerList().serverUpdateMovingPlayer(this);

                if (this.isSneaking())
                {
                    this.setSpectatingEntity(this);
                }
            }
            else
            {
                this.setSpectatingEntity(this);
            }
        }

        CriteriaTriggers.TICK.trigger(this);

        if (this.levitationStartPos != null)
        {
            CriteriaTriggers.LEVITATION.trigger(this, this.levitationStartPos, this.ticksExisted - this.levitatingSince);
        }

        this.advancements.flushDirty(this);
    }

    public void onUpdateEntity()
    {
        try
        {
            super.onUpdate();

            for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = this.inventory.getStackInSlot(i);

                if (!itemstack.isEmpty() && itemstack.getItem().isMap())
                {
                    Packet<?> packet = ((ItemMapBase)itemstack.getItem()).createMapDataPacket(itemstack, this.world, this);

                    if (packet != null)
                    {
                        this.connection.sendPacket(packet);
                    }
                }
            }

            if (this.getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel() || this.foodStats.getSaturationLevel() == 0.0F != this.wasHungry)
            {
                this.connection.sendPacket(new SPacketUpdateHealth(this.getBukkitEntity().getScaledHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel())); // CraftBukkit
                this.lastHealth = this.getHealth();
                this.lastFoodLevel = this.foodStats.getFoodLevel();
                this.wasHungry = this.foodStats.getSaturationLevel() == 0.0F;
            }

            if (this.getHealth() + this.getAbsorptionAmount() != this.lastHealthScore)
            {
                this.lastHealthScore = this.getHealth() + this.getAbsorptionAmount();
                this.updateScorePoints(IScoreCriteria.HEALTH, MathHelper.ceil(this.lastHealthScore));
            }

            if (this.foodStats.getFoodLevel() != this.lastFoodScore)
            {
                this.lastFoodScore = this.foodStats.getFoodLevel();
                this.updateScorePoints(IScoreCriteria.FOOD, MathHelper.ceil((float)this.lastFoodScore));
            }

            if (this.getAir() != this.lastAirScore)
            {
                this.lastAirScore = this.getAir();
                this.updateScorePoints(IScoreCriteria.AIR, MathHelper.ceil((float)this.lastAirScore));
            }
            // Akarin start
            if (this.maxHealthCache != this.getMaxHealth()) {
                this.getBukkitEntity().updateScaledHealth();
            }
            // Akarin end

            if (this.getTotalArmorValue() != this.lastArmorScore)
            {
                this.lastArmorScore = this.getTotalArmorValue();
                this.updateScorePoints(IScoreCriteria.ARMOR, MathHelper.ceil((float)this.lastArmorScore));
            }

            if (this.experienceTotal != this.lastExperienceScore)
            {
                this.lastExperienceScore = this.experienceTotal;
                this.updateScorePoints(IScoreCriteria.XP, MathHelper.ceil((float)this.lastExperienceScore));
            }

            if (this.experienceLevel != this.lastLevelScore)
            {
                this.lastLevelScore = this.experienceLevel;
                this.updateScorePoints(IScoreCriteria.LEVEL, MathHelper.ceil((float)this.lastLevelScore));
            }

            if (this.experienceTotal != this.lastExperience)
            {
                this.lastExperience = this.experienceTotal;
                this.connection.sendPacket(new SPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
            }

            if (this.ticksExisted % 20 == 0)
            {
                CriteriaTriggers.LOCATION.trigger(this);
            }
            // Akarin start
            if (this.oldLevel == -1) {
                this.oldLevel = this.experienceLevel;
            }

            if (this.oldLevel != this.experienceLevel) {
                CraftEventFactory.callPlayerLevelChangeEvent(this.world.getServer().getPlayer((EntityPlayerMP) this), this.oldLevel, this.experienceLevel);
                this.oldLevel = this.experienceLevel;
            }
            // Akarin end
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    private void updateScorePoints(IScoreCriteria criteria, int points)
    {
        // Akarin start
        for (Score score : this.world.getServer().getScoreboardManager().getScoreboardScores(criteria, this.getName(), Lists.newArrayList()))
        {
            //Score score = this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective);
            score.setScorePoints(points);
        }
        // Akarin end
    }

    public void onDeath(DamageSource cause)
    {
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, cause)) return;
        boolean flag = this.world.getGameRules().getBoolean("showDeathMessages");
        this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.ENTITY_DIED, flag));

        // Akarin start
        if (this.isDead) {
            return;
        }
        ArrayList<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>(this.inventory.getSizeInventory());
        boolean keepInventory = this.world.getGameRules().getBoolean("keepInventory") || this.isSpectator();

        if (!keepInventory) {
            for (ItemStack item : this.inventory.getContents()) {
                if (!item.isEmpty() && !EnchantmentHelper.hasVanishingCurse(item)) {
                    loot.add(CraftItemStack.asCraftMirror(item));
                }
            }
        }
        
        ITextComponent chatmessage = this.getCombatTracker().getDeathMessage();
        String deathmessage = chatmessage.getUnformattedText();
        
        this.captureDrops = true;
        PlayerDeathEvent deathEvent = CraftEventFactory.callPlayerDeathEvent(this, loot, deathmessage, keepInventory);
        this.captureDrops = false;
        
        String deathMessage = deathEvent.getDeathMessage();
        if (deathMessage != null && deathMessage.length() > 0 && flag) {
            if (deathMessage.equals(deathmessage)) {
                Team scoreboardteambase = this.getTeam();

                if (scoreboardteambase != null && scoreboardteambase.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
                    if (scoreboardteambase.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
                        this.mcServer.getPlayerList().sendMessageToAllTeamMembers((EntityPlayer) this, chatmessage);
                    } else if (scoreboardteambase.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
                        this.mcServer.getPlayerList().sendMessageToTeamOrAllPlayers((EntityPlayer) this, chatmessage);
                    }
                } else {
                    this.mcServer.getPlayerList().sendMessage(chatmessage);
                }
            } else {
                this.mcServer.getPlayerList().sendMessage(CraftChatMessage.fromString(deathMessage));
            }
        }
        
        this.spawnShoulderEntities();
        if (!deathEvent.getKeepInventory()) {
            this.inventory.clear();
        }
        
        if (!(keepInventory || this.queuedEndExit || MinecraftForge.EVENT_BUS.post(new PlayerDropsEvent(this, cause, this.capturedDrops, this.recentlyHit > 0)))) {
            for (net.minecraft.entity.item.EntityItem item : capturedDrops) {
                this.world.spawnEntity(item);
            }
        }
        
        this.closeContainer();
        this.setSpectatingEntity(this); // Remove spectated target
        
        Collection<Score> collection = this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.DEATH_COUNT, this.getName(), Lists.newArrayList());
        for (Score score : collection) {
            score.incrementScore();
        }
        
        /*
        if (flag)
        {
            Team team = this.getTeam();

            if (team != null && team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS)
            {
                if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS)
                {
                    this.mcServer.getPlayerList().sendMessageToAllTeamMembers(this, this.getCombatTracker().getDeathMessage());
                }
                else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM)
                {
                    this.mcServer.getPlayerList().sendMessageToTeamOrAllPlayers(this, this.getCombatTracker().getDeathMessage());
                }
            }
            else
            {
                this.mcServer.getPlayerList().sendMessage(this.getCombatTracker().getDeathMessage());
            }
        }

        this.spawnShoulderEntities();

        if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator())
        {
            captureDrops = true;
            capturedDrops.clear();
            this.destroyVanishingCursedItems();
            this.inventory.dropAllItems();

            captureDrops = false;
            net.minecraftforge.event.entity.player.PlayerDropsEvent event = new net.minecraftforge.event.entity.player.PlayerDropsEvent(this, cause, capturedDrops, recentlyHit > 0);
            if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
            {
                for (net.minecraft.entity.item.EntityItem item : capturedDrops)
                {
                    this.world.spawnEntity(item);
                }
            }
        }

        for (ScoreObjective scoreobjective : this.world.getScoreboard().getObjectivesFromCriteria(IScoreCriteria.DEATH_COUNT))
        {
            Score score = this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective);
            score.incrementScore();
        }
        */
        // Akarin end

        EntityLivingBase entitylivingbase = this.getAttackingEntity();

        if (entitylivingbase != null)
        {
            EntityList.EntityEggInfo entitylist$entityegginfo = EntityList.ENTITY_EGGS.get(EntityList.getKey(entitylivingbase));

            if (entitylist$entityegginfo != null)
            {
                this.addStat(entitylist$entityegginfo.entityKilledByStat);
            }

            entitylivingbase.awardKillScore(this, this.scoreValue, cause);
        }

        this.addStat(StatList.DEATHS);
        this.takeStat(StatList.TIME_SINCE_DEATH);
        this.extinguish();
        this.setFlag(0, false);
        this.getCombatTracker().reset();
    }

    public void awardKillScore(Entity p_191956_1_, int p_191956_2_, DamageSource p_191956_3_)
    {
        if (p_191956_1_ != this)
        {
            super.awardKillScore(p_191956_1_, p_191956_2_, p_191956_3_);
            this.addScore(p_191956_2_);
            Collection<Score> collection = this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.TOTAL_KILL_COUNT, this.getName(), Lists.newArrayList()); // Akarin

            if (p_191956_1_ instanceof EntityPlayer)
            {
                this.addStat(StatList.PLAYER_KILLS);
                this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.PLAYER_KILL_COUNT, this.getName(), collection); // Akarin
            }
            else
            {
                this.addStat(StatList.MOB_KILLS);
            }

            collection.addAll(this.awardTeamKillScores(p_191956_1_));

            for (Score scoreobjective : collection) // Akarin
            {
                scoreobjective.incrementScore(); // Akarin
            }

            CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(this, p_191956_1_, p_191956_3_);
        }
    }

    private Collection<Score> awardTeamKillScores(Entity p_192038_1_) // Akarin
    {
        String s = p_192038_1_ instanceof EntityPlayer ? p_192038_1_.getName() : p_192038_1_.getCachedUniqueIdString();
        ScorePlayerTeam scoreplayerteam = this.getWorldScoreboard().getPlayersTeam(this.getName());

        if (scoreplayerteam != null)
        {
            int i = scoreplayerteam.getColor().getColorIndex();

            if (i >= 0 && i < IScoreCriteria.KILLED_BY_TEAM.length)
            {
                for (ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.KILLED_BY_TEAM[i]))
                {
                    Score score = this.getWorldScoreboard().getOrCreateScore(s, scoreobjective);
                    score.incrementScore();
                }
            }
        }

        ScorePlayerTeam scoreplayerteam1 = this.getWorldScoreboard().getPlayersTeam(s);

        if (scoreplayerteam1 != null)
        {
            int j = scoreplayerteam1.getColor().getColorIndex();

            if (j >= 0 && j < IScoreCriteria.TEAM_KILL.length)
            {
                return this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.TEAM_KILL[j], this.getName(), Lists.newArrayList()); // Akarin
            }
        }

        return Lists.newArrayList(); // Akarin
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            boolean flag = this.mcServer.isDedicatedServer() && this.canPlayersAttack() && "fall".equals(source.damageType);

            if (!flag && this.respawnInvulnerabilityTicks > 0 && source != DamageSource.OUT_OF_WORLD)
            {
                return false;
            }
            else
            {
                if (source instanceof EntityDamageSource)
                {
                    Entity entity = source.getTrueSource();

                    if (entity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)entity))
                    {
                        return false;
                    }

                    if (entity instanceof EntityArrow)
                    {
                        EntityArrow entityarrow = (EntityArrow)entity;

                        if (entityarrow.shootingEntity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)entityarrow.shootingEntity))
                        {
                            return false;
                        }
                    }
                }

                return super.attackEntityFrom(source, amount);
            }
        }
    }

    public boolean canAttackPlayer(EntityPlayer other)
    {
        return !this.canPlayersAttack() ? false : super.canAttackPlayer(other);
    }

    private boolean canPlayersAttack()
    {
        return this.world.pvpMode; // Akarin
    }

    @Nullable
    public Entity changeDimension(int dimensionIn, net.minecraftforge.common.util.ITeleporter teleporter)
    {
        if (this.isPlayerSleeping()) return this; // Akarin
        if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(this, dimensionIn)) return this;
        //this.invulnerableDimensionChange = true; // Akarin

        if (this.dimension == 0 && dimensionIn == -1)
        {
            this.enteredNetherPosition = new Vec3d(this.posX, this.posY, this.posZ);
        }
        else if (this.dimension != -1 && dimensionIn != 0)
        {
            this.enteredNetherPosition = null;
        }

        if (this.dimension == 1 && dimensionIn == 1 && teleporter.isVanilla())
        {
            this.invulnerableDimensionChange = true; // Akarin
            this.world.removeEntity(this);

            if (!this.queuedEndExit)
            {
                this.queuedEndExit = true;
                this.connection.sendPacket(new SPacketChangeGameState(4, this.seenCredits ? 0.0F : 1.0F));
                this.seenCredits = true;
            }

            return this;
        }
        else
        {
            if (this.dimension == 0 && dimensionIn == 1)
            {
                dimensionIn = 1;
            }

            PlayerTeleportEvent.TeleportCause cause = this.dimension == 1 || dimensionIn == 1 ? PlayerTeleportEvent.TeleportCause.END_PORTAL : (this.dimension == -1 || dimensionIn == -1 ? PlayerTeleportEvent.TeleportCause.NETHER_PORTAL : PlayerTeleportEvent.TeleportCause.MOD); // Akarin
            this.mcServer.getPlayerList().transferPlayerToDimension(this, dimensionIn, teleporter); // Akarin
            this.connection.sendPacket(new SPacketEffect(1032, BlockPos.ORIGIN, 0, false));
            this.lastExperience = -1;
            this.lastHealth = -1.0F;
            this.lastFoodLevel = -1;
            return this;
        }
    }

    public boolean isSpectatedByPlayer(EntityPlayerMP player)
    {
        if (player.isSpectator())
        {
            return this.getSpectatingEntity() == this;
        }
        else
        {
            return this.isSpectator() ? false : super.isSpectatedByPlayer(player);
        }
    }

    private void sendTileEntityUpdate(TileEntity p_147097_1_)
    {
        if (p_147097_1_ != null)
        {
            SPacketUpdateTileEntity spacketupdatetileentity = p_147097_1_.getUpdatePacket();

            if (spacketupdatetileentity != null)
            {
                this.connection.sendPacket(spacketupdatetileentity);
            }
        }
    }

    public void onItemPickup(Entity entityIn, int quantity)
    {
        super.onItemPickup(entityIn, quantity);
        this.openContainer.detectAndSendChanges();
    }

    public EntityPlayer.SleepResult trySleep(BlockPos bedLocation)
    {
        EntityPlayer.SleepResult entityplayer$sleepresult = super.trySleep(bedLocation);

        if (entityplayer$sleepresult == EntityPlayer.SleepResult.OK)
        {
            this.addStat(StatList.SLEEP_IN_BED);
            Packet<?> packet = new SPacketUseBed(this, bedLocation);
            this.getServerWorld().getEntityTracker().sendToTracking(this, packet);
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.connection.sendPacket(packet);
            CriteriaTriggers.SLEPT_IN_BED.trigger(this);
        }

        return entityplayer$sleepresult;
    }

    public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn)
    {
        if (!this.sleeping) return; // Akarin
        if (this.isPlayerSleeping())
        {
            this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(this, 2));
        }

        super.wakeUpPlayer(immediately, updateWorldFlag, setSpawn);

        if (this.connection != null)
        {
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    public boolean startRiding(Entity entityIn, boolean force)
    {
        Entity entity = this.getRidingEntity();

        if (!super.startRiding(entityIn, force))
        {
            return false;
        }
        else
        {
            Entity entity1 = this.getRidingEntity();

            if (entity1 != entity && this.connection != null)
            {
                this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            }

            return true;
        }
    }

    public void dismountRidingEntity()
    {
        Entity entity = this.getRidingEntity();
        super.dismountRidingEntity();
        Entity entity1 = this.getRidingEntity();

        if (entity1 != entity && this.connection != null)
        {
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    public boolean isEntityInvulnerable(DamageSource source)
    {
        return super.isEntityInvulnerable(source) || this.isInvulnerableDimensionChange();
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
    }

    protected void frostWalk(BlockPos pos)
    {
        if (!this.isSpectator())
        {
            super.frostWalk(pos);
        }
    }

    public void handleFalling(double y, boolean onGroundIn)
    {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY - 0.20000000298023224D);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        IBlockState iblockstate = this.world.getBlockState(blockpos);

        if (iblockstate.getBlock().isAir(iblockstate, this.world, blockpos))
        {
            BlockPos blockpos1 = blockpos.down();
            IBlockState iblockstate1 = this.world.getBlockState(blockpos1);
            Block block = iblockstate1.getBlock();

            if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate)
            {
                blockpos = blockpos1;
                iblockstate = iblockstate1;
            }
        }

        super.updateFallState(y, onGroundIn, iblockstate, blockpos);
    }

    public void openEditSign(TileEntitySign signTile)
    {
        signTile.setPlayer(this);
        this.connection.sendPacket(new SPacketSignEditorOpen(signTile.getPos()));
    }

    public int getNextWindowId() // Akarin
    {
        this.currentWindowId = this.currentWindowId % 100 + 1;
        return currentWindowId; // Akarin
    }

    public void displayGui(IInteractionObject guiOwner)
    {
        // Akarin start
        boolean cancelled = guiOwner instanceof ILootContainer && ((ILootContainer) guiOwner).getLootTable() != null && this.isSpectator();
        Container container = CraftEventFactory.callInventoryOpenEvent(this, guiOwner.createContainer(this.inventory, this), cancelled);
        if (container == null) {
            return;
        }
        this.getNextWindowId();
        this.openContainer = container;
        this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, guiOwner.getGuiID(), guiOwner.getDisplayName()));
        
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.addListener(this);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(this, this.openContainer));
        
        /*
        if (guiOwner instanceof ILootContainer && ((ILootContainer)guiOwner).getLootTable() != null && this.isSpectator())
        {
            this.sendStatusMessage((new TextComponentTranslation("container.spectatorCantOpen", new Object[0])).setStyle((new Style()).setColor(TextFormatting.RED)), true);
        }
        else
        {
            this.getNextWindowId();
            this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, guiOwner.getGuiID(), guiOwner.getDisplayName()));
            this.openContainer = guiOwner.createContainer(this.inventory, this);
            this.openContainer.windowId = this.currentWindowId;
            this.openContainer.addListener(this);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(this, this.openContainer));
        }
        */
        // Akarin end
    }

    public void displayGUIChest(IInventory chestInventory)
    {
        // Akarin start
        boolean cancelled = false;
        if (chestInventory instanceof ILockableContainer) {
            ILockableContainer itileinventory = (ILockableContainer) chestInventory;
            cancelled = itileinventory.isLocked() && !this.canOpen(itileinventory.getLockCode()) && !this.isSpectator();
        }

        Container container;
        if (chestInventory instanceof IInteractionObject) {
            if (chestInventory instanceof TileEntity) {
                Preconditions.checkArgument(((TileEntity) chestInventory).getWorld() != null, "Container must have world to be opened");
            }
            container = ((IInteractionObject) chestInventory).createContainer(this.inventory, this);
        } else {
            container = new ContainerChest(this.inventory, chestInventory, this);
        }
        container = CraftEventFactory.callInventoryOpenEvent(this, container, cancelled);
        if (container == null && !cancelled) { // Let pre-cancelled events fall through
            chestInventory.closeInventory(this);
            return;
        }
        // Akarin end
        if (chestInventory instanceof ILootContainer && ((ILootContainer)chestInventory).getLootTable() != null && this.isSpectator())
        {
            this.sendStatusMessage((new TextComponentTranslation("container.spectatorCantOpen", new Object[0])).setStyle((new Style()).setColor(TextFormatting.RED)), true);
        }
        else
        {
            if (this.openContainer != this.inventoryContainer)
            {
                this.closeScreen();
            }

            if (chestInventory instanceof ILockableContainer)
            {
                ILockableContainer ilockablecontainer = (ILockableContainer)chestInventory;

                if (ilockablecontainer.isLocked() && !this.canOpen(ilockablecontainer.getLockCode()) && !this.isSpectator())
                {
                    this.connection.sendPacket(new SPacketChat(new TextComponentTranslation("container.isLocked", new Object[] {chestInventory.getDisplayName()}), ChatType.GAME_INFO));
                    this.connection.sendPacket(new SPacketSoundEffect(SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, this.posX, this.posY, this.posZ, 1.0F, 1.0F));
                    chestInventory.closeInventory(this); // Akarin
                    return;
                }
            }

            this.getNextWindowId();

            if (chestInventory instanceof IInteractionObject)
            {
                this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, ((IInteractionObject)chestInventory).getGuiID(), chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
                this.openContainer = container; // Akarin
            }
            else
            {
                this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "minecraft:container", chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
                this.openContainer = container; // Akarin
            }

            this.openContainer.windowId = this.currentWindowId;
            this.openContainer.addListener(this);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(this, this.openContainer));
        }
    }

    public void displayVillagerTradeGui(IMerchant villager)
    {
        // Akarin start
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerMerchant(this.inventory, villager, this.world));
        if (container == null) {
            return;
        }
        // Akarin end
        this.getNextWindowId();
        this.openContainer = container; // Akarin
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.addListener(this);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(this, this.openContainer));
        IInventory iinventory = ((ContainerMerchant)this.openContainer).getMerchantInventory();
        ITextComponent itextcomponent = villager.getDisplayName();
        this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "minecraft:villager", itextcomponent, iinventory.getSizeInventory()));
        MerchantRecipeList merchantrecipelist = villager.getRecipes(this);

        if (merchantrecipelist != null)
        {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.currentWindowId);
            merchantrecipelist.writeToBuf(packetbuffer);
            this.connection.sendPacket(new SPacketCustomPayload("MC|TrList", packetbuffer));
        }
    }

    public void openGuiHorseInventory(AbstractHorse horse, IInventory inventoryIn)
    {
        // Akarin start
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerHorseInventory(this.inventory, inventoryIn, horse, this));
        if (container == null) {
            inventoryIn.closeInventory(this);
            return;
        }
        // Akarin end
        if (this.openContainer != this.inventoryContainer)
        {
            this.closeScreen();
        }

        this.getNextWindowId();
        this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "EntityHorse", inventoryIn.getDisplayName(), inventoryIn.getSizeInventory(), horse.getEntityId()));
        this.openContainer = container; // Akarin
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.addListener(this);
    }

    public void openBook(ItemStack stack, EnumHand hand)
    {
        Item item = stack.getItem();

        if (item == Items.WRITTEN_BOOK)
        {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeEnumValue(hand);
            this.connection.sendPacket(new SPacketCustomPayload("MC|BOpen", packetbuffer));
        }
    }

    public void displayGuiCommandBlock(TileEntityCommandBlock commandBlock)
    {
        commandBlock.setSendToClient(true);
        this.sendTileEntityUpdate(commandBlock);
    }

    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
    {
        if (!(containerToSend.getSlot(slotInd) instanceof SlotCrafting))
        {
            if (containerToSend == this.inventoryContainer)
            {
                CriteriaTriggers.INVENTORY_CHANGED.trigger(this, this.inventory);
            }

            if (!this.isChangingQuantityOnly)
            {
                this.connection.sendPacket(new SPacketSetSlot(containerToSend.windowId, slotInd, stack));
            }
        }
    }

    public void sendContainerToPlayer(Container containerIn)
    {
        this.sendAllContents(containerIn, containerIn.getInventory());
    }

    public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList)
    {
        this.connection.sendPacket(new SPacketWindowItems(containerToSend.windowId, itemsList));
        this.connection.sendPacket(new SPacketSetSlot(-1, -1, this.inventory.getItemStack()));
        // Akarin start
        if (java.util.EnumSet.of(InventoryType.CRAFTING,InventoryType.WORKBENCH).contains(containerToSend.getBukkitView().getType())) {
            this.connection.sendPacket(new SPacketSetSlot(containerToSend.windowId, 0, containerToSend.getSlot(0).getStack()));
        }
        // Akarin end
    }

    public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue)
    {
        this.connection.sendPacket(new SPacketWindowProperty(containerIn.windowId, varToUpdate, newValue));
    }

    public void sendAllWindowProperties(Container containerIn, IInventory inventory)
    {
        for (int i = 0; i < inventory.getFieldCount(); ++i)
        {
            this.connection.sendPacket(new SPacketWindowProperty(containerIn.windowId, i, inventory.getField(i)));
        }
    }

    public void closeScreen()
    {
        CraftEventFactory.handleInventoryCloseEvent(this); // Akarin
        this.connection.sendPacket(new SPacketCloseWindow(this.openContainer.windowId));
        this.closeContainer();
    }

    public void updateHeldItem()
    {
        if (!this.isChangingQuantityOnly)
        {
            this.connection.sendPacket(new SPacketSetSlot(-1, -1, this.inventory.getItemStack()));
        }
    }

    public void closeContainer()
    {
        this.openContainer.onContainerClosed(this);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Close(this, this.openContainer));
        this.openContainer = this.inventoryContainer;
    }

    public void setEntityActionState(float strafe, float forward, boolean jumping, boolean sneaking)
    {
        if (this.isRiding())
        {
            if (strafe >= -1.0F && strafe <= 1.0F)
            {
                this.moveStrafing = strafe;
            }

            if (forward >= -1.0F && forward <= 1.0F)
            {
                this.moveForward = forward;
            }

            this.isJumping = jumping;
            this.setSneaking(sneaking);
        }
    }

    public void addStat(StatBase stat, int amount)
    {
        if (stat != null)
        {
            this.statsFile.increaseStat(this, stat, amount);

            for (ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(stat.getCriteria()))
            {
                this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective).increaseScore(amount);
            }
        }
    }

    public void takeStat(StatBase stat)
    {
        if (stat != null)
        {
            this.statsFile.unlockAchievement(this, stat, 0);

            for (ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(stat.getCriteria()))
            {
                this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreobjective).setScorePoints(0);
            }
        }
    }

    public void unlockRecipes(List<IRecipe> p_192021_1_)
    {
        this.recipeBook.add(p_192021_1_, this);
    }

    public void unlockRecipes(ResourceLocation[] p_193102_1_)
    {
        List<IRecipe> list = Lists.<IRecipe>newArrayList();

        for (ResourceLocation resourcelocation : p_193102_1_)
        {
            // Akarin start
            if (CraftingManager.getRecipe(resourcelocation) == null) {
                Bukkit.getLogger().warning("Ignoring grant of non existent recipe " + resourcelocation);
                continue;
            }
            // Akarin end
            list.add(CraftingManager.getRecipe(resourcelocation));
        }

        this.unlockRecipes(list);
    }

    public void resetRecipes(List<IRecipe> p_192022_1_)
    {
        this.recipeBook.remove(p_192022_1_, this);
    }

    public void mountEntityAndWakeUp()
    {
        this.disconnected = true;
        this.removePassengers();

        if (this.sleeping)
        {
            this.wakeUpPlayer(true, false, false);
        }
    }

    public boolean hasDisconnected()
    {
        return this.disconnected;
    }

    public void setPlayerHealthUpdated()
    {
        this.lastHealth = -1.0E8F;
        // Akarin start
        this.lastExperience = -1;
    }

    public void sendMessage(ITextComponent[] ichatbasecomponent) {
        for (ITextComponent component : ichatbasecomponent) {
            this.sendMessage(component);
        }
    }
    // Akarin end

    public void sendStatusMessage(ITextComponent chatComponent, boolean actionBar)
    {
        this.connection.sendPacket(new SPacketChat(chatComponent, actionBar ? ChatType.GAME_INFO : ChatType.CHAT));
    }

    protected void onItemUseFinish()
    {
        if (!this.activeItemStack.isEmpty() && this.isHandActive())
        {
            this.connection.sendPacket(new SPacketEntityStatus(this, (byte)9));
            super.onItemUseFinish();
        }
    }

    public void copyFrom(EntityPlayerMP that, boolean keepEverything)
    {
        if (keepEverything)
        {
            this.inventory.copyInventory(that.inventory);
            this.setHealth(that.getHealth());
            this.foodStats = that.foodStats;
            this.experienceLevel = that.experienceLevel;
            this.experienceTotal = that.experienceTotal;
            this.experience = that.experience;
            this.setScore(that.getScore());
            this.lastPortalPos = that.lastPortalPos;
            this.lastPortalVec = that.lastPortalVec;
            this.teleportDirection = that.teleportDirection;
        }
        else if (this.world.getGameRules().getBoolean("keepInventory") || that.isSpectator())
        {
            this.inventory.copyInventory(that.inventory);
            this.experienceLevel = that.experienceLevel;
            this.experienceTotal = that.experienceTotal;
            this.experience = that.experience;
            this.setScore(that.getScore());
        }

        this.xpSeed = that.xpSeed;
        this.enderChest = that.enderChest;
        this.getDataManager().set(PLAYER_MODEL_FLAG, that.getDataManager().get(PLAYER_MODEL_FLAG));
        this.lastExperience = -1;
        this.lastHealth = -1.0F;
        this.lastFoodLevel = -1;
        this.recipeBook.copyFrom(that.recipeBook);
        this.entityRemoveQueue.addAll(that.entityRemoveQueue);
        this.seenCredits = that.seenCredits;
        this.enteredNetherPosition = that.enteredNetherPosition;
        this.setLeftShoulderEntity(that.getLeftShoulderEntity());
        this.setRightShoulderEntity(that.getRightShoulderEntity());

        this.spawnChunkMap = that.spawnChunkMap;
        this.spawnForcedMap = that.spawnForcedMap;
        if(that.dimension != 0)
        {
            this.spawnPos = that.spawnPos;
            this.spawnForced = that.spawnForced;
        }

        //Copy over a section of the Entity Data from the old player.
        //Allows mods to specify data that persists after players respawn.
        NBTTagCompound old = that.getEntityData();
        if (old.hasKey(PERSISTED_NBT_TAG))
        {
            getEntityData().setTag(PERSISTED_NBT_TAG, old.getCompoundTag(PERSISTED_NBT_TAG));
        }
        net.minecraftforge.event.ForgeEventFactory.onPlayerClone(this, that, !keepEverything);
    }

    protected void onNewPotionEffect(PotionEffect id)
    {
        super.onNewPotionEffect(id);
        this.connection.sendPacket(new SPacketEntityEffect(this.getEntityId(), id));

        if (id.getPotion() == MobEffects.LEVITATION)
        {
            this.levitatingSince = this.ticksExisted;
            this.levitationStartPos = new Vec3d(this.posX, this.posY, this.posZ);
        }

        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }

    protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_)
    {
        super.onChangedPotionEffect(id, p_70695_2_);
        this.connection.sendPacket(new SPacketEntityEffect(this.getEntityId(), id));
        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }

    protected void onFinishedPotionEffect(PotionEffect effect)
    {
        super.onFinishedPotionEffect(effect);
        this.connection.sendPacket(new SPacketRemoveEntityEffect(this.getEntityId(), effect.getPotion()));

        if (effect.getPotion() == MobEffects.LEVITATION)
        {
            this.levitationStartPos = null;
        }

        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }

    public void setPositionAndUpdate(double x, double y, double z)
    {
        this.connection.setPlayerLocation(x, y, z, this.rotationYaw, this.rotationPitch);
    }

    public void onCriticalHit(Entity entityHit)
    {
        this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(entityHit, 4));
    }

    public void onEnchantmentCritical(Entity entityHit)
    {
        this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(entityHit, 5));
    }

    public void sendPlayerAbilities()
    {
        if (this.connection != null)
        {
            this.connection.sendPacket(new SPacketPlayerAbilities(this.capabilities));
            this.updatePotionMetadata();
        }
    }

    public WorldServer getServerWorld()
    {
        return (WorldServer)this.world;
    }

    public void setGameType(GameType gameType)
    {
        // Akarin start
        if (gameType == this.interactionManager.getGameType()) {
            return;
        }

        PlayerGameModeChangeEvent event = new PlayerGameModeChangeEvent(getBukkitEntity(), GameMode.getByValue(gameType.getID()));
        world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        // Akarin end
        this.interactionManager.setGameType(gameType);
        this.connection.sendPacket(new SPacketChangeGameState(3, (float)gameType.getID()));

        if (gameType == GameType.SPECTATOR)
        {
            this.spawnShoulderEntities();
            this.dismountRidingEntity();
        }
        else
        {
            this.setSpectatingEntity(this);
        }

        this.sendPlayerAbilities();
        this.markPotionsDirty();
    }

    public boolean isSpectator()
    {
        return this.interactionManager.getGameType() == GameType.SPECTATOR;
    }

    public boolean isCreative()
    {
        return this.interactionManager.getGameType() == GameType.CREATIVE;
    }

    public void sendMessage(ITextComponent component)
    {
        this.connection.sendPacket(new SPacketChat(component));
    }

    public boolean canUseCommand(int permLevel, String commandName)
    {
        // Akarin start
        if ("@".equals(commandName)) {
            return getBukkitEntity().hasPermission("minecraft.command.selector");
        }
        if ("".equals(commandName)) {
            return getBukkitEntity().isOp();
        }
        return getBukkitEntity().hasPermission("minecraft.command." + commandName);
        
        /*
        if ("seed".equals(commandName) && !this.mcServer.isDedicatedServer())
        {
            return true;
        }
        else if (!"tell".equals(commandName) && !"help".equals(commandName) && !"me".equals(commandName) && !"trigger".equals(commandName))
        {
            if (this.mcServer.getPlayerList().canSendCommands(this.getGameProfile()))
            {
                UserListOpsEntry userlistopsentry = (UserListOpsEntry)this.mcServer.getPlayerList().getOppedPlayers().getEntry(this.getGameProfile());

                if (userlistopsentry != null)
                {
                    return userlistopsentry.getPermissionLevel() >= permLevel;
                }
                else
                {
                    return this.mcServer.getOpPermissionLevel() >= permLevel;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
        */
        // Akarin end
    }

    public String getPlayerIP()
    {
        String s = this.connection.netManager.getRemoteAddress().toString();
        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void handleClientSettings(CPacketClientSettings packetIn)
    {
        // Akarin start
        if (getPrimaryHand() != packetIn.getMainHand()) {
            PlayerChangedMainHandEvent event = new PlayerChangedMainHandEvent(this.getBukkitEntity(), this.getPrimaryHand() == EnumHandSide.LEFT ? MainHand.LEFT : MainHand.RIGHT);
            this.mcServer.server.getPluginManager().callEvent(event);
        }
        if (!this.language.equals(packetIn.getLang())) {
            PlayerLocaleChangeEvent event = new PlayerLocaleChangeEvent(this.getBukkitEntity(), packetIn.getLang());
            this.mcServer.server.getPluginManager().callEvent(event);
        }
        // Akarin end
        this.language = packetIn.getLang();
        this.chatVisibility = packetIn.getChatVisibility();
        this.chatColours = packetIn.isColorsEnabled();
        this.getDataManager().set(PLAYER_MODEL_FLAG, Byte.valueOf((byte)packetIn.getModelPartFlags()));
        this.getDataManager().set(MAIN_HAND, Byte.valueOf((byte)(packetIn.getMainHand() == EnumHandSide.LEFT ? 0 : 1)));
    }

    public EntityPlayer.EnumChatVisibility getChatVisibility()
    {
        return this.chatVisibility;
    }

    public void loadResourcePack(String url, String hash)
    {
        this.connection.sendPacket(new SPacketResourcePackSend(url, hash));
    }

    public BlockPos getPosition()
    {
        return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
    }

    public void markPlayerActive()
    {
        this.playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
    }

    public StatisticsManagerServer getStatFile()
    {
        return this.statsFile;
    }

    public RecipeBookServer getRecipeBook()
    {
        return this.recipeBook;
    }

    public void removeEntity(Entity entityIn)
    {
        if (entityIn instanceof EntityPlayer)
        {
            this.connection.sendPacket(new SPacketDestroyEntities(new int[] {entityIn.getEntityId()}));
        }
        else
        {
            this.entityRemoveQueue.add(Integer.valueOf(entityIn.getEntityId()));
        }
    }

    public void addEntity(Entity entityIn)
    {
        this.entityRemoveQueue.remove(Integer.valueOf(entityIn.getEntityId()));
    }

    protected void updatePotionMetadata()
    {
        if (this.isSpectator())
        {
            this.resetPotionEffectMetadata();
            this.setInvisible(true);
        }
        else
        {
            super.updatePotionMetadata();
        }

        this.getServerWorld().getEntityTracker().updateVisibility(this);
    }

    public Entity getSpectatingEntity()
    {
        return (Entity)(this.spectatingEntity == null ? this : this.spectatingEntity);
    }

    public void setSpectatingEntity(Entity entityToSpectate)
    {
        Entity entity = this.getSpectatingEntity();
        this.spectatingEntity = (Entity)(entityToSpectate == null ? this : entityToSpectate);

        if (entity != this.spectatingEntity)
        {
            this.connection.sendPacket(new SPacketCamera(this.spectatingEntity));
            this.connection.setPlayerLocation(this.spectatingEntity.posX, this.spectatingEntity.posY, this.spectatingEntity.posZ, this.rotationYaw, this.rotationPitch, TeleportCause.SPECTATE); // Akarin
        }
    }

    protected void decrementTimeUntilPortal()
    {
        if (this.timeUntilPortal > 0 && !this.invulnerableDimensionChange)
        {
            --this.timeUntilPortal;
        }
    }

    public void attackTargetEntityWithCurrentItem(Entity targetEntity)
    {
        if (this.interactionManager.getGameType() == GameType.SPECTATOR)
        {
            this.setSpectatingEntity(targetEntity);
        }
        else
        {
            super.attackTargetEntityWithCurrentItem(targetEntity);
        }
    }

    public long getLastActiveTime()
    {
        return this.playerLastActiveTime;
    }

    @Nullable
    public ITextComponent getTabListDisplayName()
    {
        return this.listName; // Akarin
    }

    public void swingArm(EnumHand hand)
    {
        super.swingArm(hand);
        this.resetCooldown();
    }

    public boolean isInvulnerableDimensionChange()
    {
        return this.invulnerableDimensionChange;
    }

    public void clearInvulnerableDimensionChange()
    {
        this.invulnerableDimensionChange = false;
    }

    public void setElytraFlying()
    {
        if (CraftEventFactory.callToggleGlideEvent(this, true).isCancelled()) return; // Akarin
        this.setFlag(7, true);
    }

    public void clearElytraFlying()
    {
        if (CraftEventFactory.callToggleGlideEvent(this, false).isCancelled()) return; // Akarin
        this.setFlag(7, true);
        this.setFlag(7, false);
    }

    public PlayerAdvancements getAdvancements()
    {
        return this.advancements;
    }

    @Nullable
    public Vec3d getEnteredNetherPosition()
    {
        return this.enteredNetherPosition;
    }
}