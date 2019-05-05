package net.minecraft.world;

import com.conversantmedia.util.concurrent.NoLockDisruptorBlockingQueue;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import co.aikar.timings.WorldTimingsHandler;
import io.akarin.forge.AkarinForge;
import io.akarin.forge.WorldCapture;
import io.akarin.forge.utils.HopperTask;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.SpigotTimings;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.generator.ChunkGenerator;
import org.spigotmc.ActivationRange;
import org.spigotmc.SpigotWorldConfig;
import org.spigotmc.TickLimiter;

import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.pathfinding.PathWorldListener;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.v1_12_R1.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class World implements IBlockAccess, net.minecraftforge.common.capabilities.ICapabilityProvider
{
    /**
     * Used in the getEntitiesWithinAABB functions to expand the search area for entities.
     * Modders should change this variable to a higher value if it is less then the radius
     * of one of there entities.
     */
    public static double MAX_ENTITY_RADIUS = 2.0D;

    private int seaLevel = 63;
    protected boolean scheduledUpdatesAreImmediate;
    public final List<Entity> loadedEntityList = Lists.<Entity>newArrayList();
    protected final List<Entity> unloadedEntityList = Lists.<Entity>newArrayList();
    public final List<TileEntity> loadedTileEntityList = Lists.<TileEntity>newArrayList();
    public final List<TileEntity> tickableTileEntities = Lists.<TileEntity>newArrayList();
    private final List<TileEntity> addedTileEntityList = Lists.<TileEntity>newArrayList();
    private final List<TileEntity> tileEntitiesToBeRemoved = Lists.<TileEntity>newArrayList();
    public final List<EntityPlayer> playerEntities = Lists.<EntityPlayer>newArrayList();
    public final List<Entity> weatherEffects = Lists.<Entity>newArrayList();
    protected final IntHashMap<Entity> entitiesById = new IntHashMap<Entity>();
    private final long cloudColour = 16777215L;
    private int skylightSubtracted;
    protected int updateLCG = (new Random()).nextInt();
    protected final int DIST_HASH_MAGIC = 1013904223;
    public float prevRainingStrength;
    public float rainingStrength;
    public float prevThunderingStrength;
    public float thunderingStrength;
    private int lastLightningBolt;
    public final Random rand = new Random();
    public final WorldProvider provider;
    protected PathWorldListener pathListener = new PathWorldListener();
    protected List<IWorldEventListener> eventListeners;
    protected IChunkProvider chunkProvider;
    protected final ISaveHandler saveHandler;
    protected WorldInfo worldInfo;
    protected boolean findingSpawnPoint;
    protected MapStorage mapStorage;
    public VillageCollection villageCollection;
    protected LootTableManager lootTable;
    protected AdvancementManager advancementManager;
    protected FunctionManager functionManager;
    public final Profiler profiler;
    private final Calendar calendar;
    protected Scoreboard worldScoreboard;
    public final boolean isRemote;
    protected boolean spawnHostileMobs;
    protected boolean spawnPeacefulMobs;
    private boolean processingLoadedTiles;
    private final WorldBorder worldBorder;
    int[] lightUpdateBlockList;

    public boolean restoringBlockSnapshots = false;
    public boolean captureBlockSnapshots = false;
    public java.util.ArrayList<net.minecraftforge.common.util.BlockSnapshot> capturedBlockSnapshots = new java.util.ArrayList<net.minecraftforge.common.util.BlockSnapshot>();
    private net.minecraftforge.common.capabilities.CapabilityDispatcher capabilities;
    private net.minecraftforge.common.util.WorldCapabilityData capabilityData;

    // Akarin start
    private LinkedBlockingQueue<HopperTask> hopperQueue = new LinkedBlockingQueue<>();
    private final ExecutorService entityThreadPool = new ThreadPoolExecutor(1, AkarinForge.entityPoolNum, 30, TimeUnit.SECONDS, new NoLockDisruptorBlockingQueue<Runnable>(100000));
    private final CraftWorld world;
    public boolean pvpMode;
    public boolean keepSpawnInMemory = true;
    public ChunkGenerator generator;
    public boolean captureTreeGeneration = false;
    public List<ItemStack> captureDrops;
    public long ticksPerAnimalSpawns;
    public long ticksPerMonsterSpawns;
    public boolean populating;
    private int tickPosition;
    public final SpigotWorldConfig spigotConfig;
    public final WorldTimingsHandler timings;
    public static boolean haveWeSilencedAPhysicsCrash;
    public static String blockLocation;
    private TickLimiter entityLimiter;
    private TickLimiter tileLimiter;
    private int tileTickPosition;
    public WorldCapture worldCapture;

    public CraftWorld getWorld() {
        return this.world;
    }

    public CraftServer getServer() {
        return (CraftServer)Bukkit.getServer();
    }

    public Chunk getChunkIfLoaded(BlockPos blockposition) {
        return ((ChunkProviderServer) this.chunkProvider).getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }
    
    public Chunk getChunkIfLoaded(int x, int z) {
        return ((ChunkProviderServer) this.chunkProvider).getChunkIfLoaded(x, z);
    }
    
    public void checkSleepStatus() {
        this.updateAllPlayersSleepingFlag();
    }
    
    protected World(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client, ChunkGenerator gen, Environment env)
    {
        // Akarin start
        this.spigotConfig = new SpigotWorldConfig(info.getWorldName());
        this.generator = gen;
        this.world = new CraftWorld((WorldServer) this, gen, env);
        this.ticksPerAnimalSpawns = this.getServer().getTicksPerAnimalSpawns();
        this.ticksPerMonsterSpawns = this.getServer().getTicksPerMonsterSpawns();
        
        timings = new co.aikar.timings.WorldTimingsHandler(this);
        this.keepSpawnInMemory = this.paperConfig.keepSpawnInMemory;
        this.entityLimiter = new org.spigotmc.TickLimiter(spigotConfig.entityMaxTickTime);
        this.tileLimiter = new org.spigotmc.TickLimiter(spigotConfig.tileMaxTickTime);
        // Akarin end
        this.eventListeners = Lists.newArrayList(this.pathListener);
        this.calendar = Calendar.getInstance();
        this.worldScoreboard = new Scoreboard();
        this.spawnHostileMobs = true;
        this.spawnPeacefulMobs = true;
        this.lightUpdateBlockList = new int[32768];
        this.saveHandler = saveHandlerIn;
        this.profiler = profilerIn;
        this.worldInfo = info;
        this.provider = providerIn;
        this.isRemote = client;
        this.worldBorder = providerIn.createWorldBorder();
        perWorldStorage = new MapStorage((ISaveHandler)null);
        // Akarin start
        getWorldBorder().world = (WorldServer) this;
        // From PlayerList.setPlayerFileData
        getWorldBorder().addListener(new IBorderListener() {
            public void onSizeChanged(WorldBorder worldborder, double d0) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_SIZE), worldborder.world);
            }

            public void onTransitionStarted(WorldBorder worldborder, double d0, double d1, long i) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.LERP_SIZE), worldborder.world);
            }

            public void onCenterChanged(WorldBorder worldborder, double d0, double d1) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_CENTER), worldborder.world);
            }

            public void onWarningTimeChanged(WorldBorder worldborder, int i) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_TIME), worldborder.world);
            }

            public void onWarningDistanceChanged(WorldBorder worldborder, int i) {
                getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_BLOCKS), worldborder.world);
            }

            public void onDamageAmountChanged(WorldBorder worldborder, double d0) {}

            public void onDamageBufferChanged(WorldBorder worldborder, double d0) {}
        });
        this.getServer().addWorld(this.world);
        // Akarin end
    }

    public World init()
    {
        return this;
    }

    public Biome getBiome(final BlockPos pos)
    {
        return this.provider.getBiomeForCoords(pos);
    }

    public Biome getBiomeForCoordsBody(final BlockPos pos)
    {
        if (this.isBlockLoaded(pos))
        {
            Chunk chunk = this.getChunkFromBlockCoords(pos);

            try
            {
                return chunk.getBiome(pos, this.provider.getBiomeProvider());
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting biome");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Coordinates of biome request");
                crashreportcategory.addDetail("Location", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return CrashReportCategory.getCoordinateInfo(pos);
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        else
        {
            return this.provider.getBiomeProvider().getBiome(pos, Biomes.PLAINS);
        }
    }

    public BiomeProvider getBiomeProvider()
    {
        return this.provider.getBiomeProvider();
    }

    protected abstract IChunkProvider createChunkProvider();

    public void initialize(WorldSettings settings)
    {
        this.worldInfo.setServerInitialized(true);
    }

    @Nullable
    public MinecraftServer getMinecraftServer()
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void setInitialSpawnLocation()
    {
        this.setSpawnPoint(new BlockPos(8, 64, 8));
    }

    public IBlockState getGroundAboveSeaLevel(BlockPos pos)
    {
        BlockPos blockpos;

        for (blockpos = new BlockPos(pos.getX(), this.getSeaLevel(), pos.getZ()); !this.isAirBlock(blockpos.up()); blockpos = blockpos.up())
        {
            ;
        }

        return this.getBlockState(blockpos);
    }

    public boolean isValid(BlockPos pos)
    {
        return !this.isOutsideBuildHeight(pos) && pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000;
    }

    public boolean isOutsideBuildHeight(BlockPos pos)
    {
        return pos.getY() < 0 || pos.getY() >= 256;
    }

    public boolean isAirBlock(BlockPos pos)
    {
        return this.getBlockState(pos).getBlock().isAir(this.getBlockState(pos), this, pos);
    }

    public boolean isBlockLoaded(BlockPos pos)
    {
        return this.isBlockLoaded(pos, true);
    }

    public boolean isBlockLoaded(BlockPos pos, boolean allowEmpty)
    {
        return this.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, allowEmpty);
    }

    public boolean isAreaLoaded(BlockPos center, int radius)
    {
        return this.isAreaLoaded(center, radius, true);
    }

    public boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty)
    {
        return this.isAreaLoaded(center.getX() - radius, center.getY() - radius, center.getZ() - radius, center.getX() + radius, center.getY() + radius, center.getZ() + radius, allowEmpty);
    }

    public boolean isAreaLoaded(BlockPos from, BlockPos to)
    {
        return this.isAreaLoaded(from, to, true);
    }

    public boolean isAreaLoaded(BlockPos from, BlockPos to, boolean allowEmpty)
    {
        return this.isAreaLoaded(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), allowEmpty);
    }

    public boolean isAreaLoaded(StructureBoundingBox box)
    {
        return this.isAreaLoaded(box, true);
    }

    public boolean isAreaLoaded(StructureBoundingBox box, boolean allowEmpty)
    {
        return this.isAreaLoaded(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, allowEmpty);
    }

    private boolean isAreaLoaded(int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd, boolean allowEmpty)
    {
        if (yEnd >= 0 && yStart < 256)
        {
            xStart = xStart >> 4;
            zStart = zStart >> 4;
            xEnd = xEnd >> 4;
            zEnd = zEnd >> 4;

            for (int i = xStart; i <= xEnd; ++i)
            {
                for (int j = zStart; j <= zEnd; ++j)
                {
                    if (!this.isChunkLoaded(i, j, allowEmpty))
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public abstract boolean isChunkLoaded(int x, int z, boolean allowEmpty); // Akarin

    public Chunk getChunkFromBlockCoords(BlockPos pos)
    {
        return this.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4);
    }

    public Chunk getChunkFromChunkCoords(int chunkX, int chunkZ)
    {
        return this.chunkProvider.provideChunk(chunkX, chunkZ);
    }

    public boolean isChunkGeneratedAt(int x, int z)
    {
        return this.isChunkLoaded(x, z, false) ? true : this.chunkProvider.isChunkGeneratedAt(x, z);
    }

    public boolean setBlockState(BlockPos pos, IBlockState newState, int flags)
    {
        // Akarin start
        if (this.captureTreeGeneration) {
            BlockSnapshot blocksnapshot = null;
            for (BlockSnapshot previous : this.capturedBlockSnapshots) {
                if (!previous.getPos().equals(pos)) continue;
                blocksnapshot = previous;
                break;
            }
            if (blocksnapshot != null) {
                this.capturedBlockSnapshots.remove(blocksnapshot);
            }
            this.capturedBlockSnapshots.add(new BlockSnapshot(this, pos, newState, flags));
            return true;
        }
        // Akarin end
        if (this.isOutsideBuildHeight(pos))
        {
            return false;
        }
        else if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            return false;
        }
        else
        {
            Chunk chunk = this.getChunkFromBlockCoords(pos);

            pos = pos.toImmutable(); // Forge - prevent mutable BlockPos leaks
            net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;
            if (this.captureBlockSnapshots && !this.isRemote)
            {
                blockSnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(this, pos, flags);
                this.capturedBlockSnapshots.add(blockSnapshot);
            }
            IBlockState oldState = getBlockState(pos);
            int oldLight = oldState.getLightValue(this, pos);
            int oldOpacity = oldState.getLightOpacity(this, pos);

            IBlockState iblockstate = chunk.setBlockState(pos, newState);

            if (iblockstate == null)
            {
                if (blockSnapshot != null) this.capturedBlockSnapshots.remove(blockSnapshot);
                return false;
            }
            else
            {
                if (newState.getLightOpacity(this, pos) != oldOpacity || newState.getLightValue(this, pos) != oldLight)
                {
                    this.profiler.startSection("checkLight");
                    this.checkLight(pos);
                    this.profiler.endSection();
                }

                if (blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
                {
                    this.markAndNotifyBlock(pos, chunk, iblockstate, newState, flags);
                }
                return true;
            }
        }
    }

    // Split off from original setBlockState(BlockPos, IBlockState, int) method in order to directly send client and physic updates
    public void markAndNotifyBlock(BlockPos pos, @Nullable Chunk chunk, IBlockState iblockstate, IBlockState newState, int flags)
    {
        Block block = newState.getBlock();
        {
            {
                if ((flags & 2) != 0 && (!this.isRemote || (flags & 4) == 0) && (chunk == null || chunk.isPopulated()))
                {
                    this.notifyBlockUpdate(pos, iblockstate, newState, flags);
                }

                if (!this.isRemote && (flags & 1) != 0)
                {
                    this.notifyNeighborsRespectDebug(pos, iblockstate.getBlock(), true);

                    if (newState.hasComparatorInputOverride())
                    {
                        this.updateComparatorOutputLevel(pos, block);
                    }
                }
                else if (!this.isRemote && (flags & 16) == 0)
                {
                    this.updateObservingBlocksAt(pos, block);
                }
            }
        }
    }

    public boolean setBlockToAir(BlockPos pos)
    {
        return this.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }

    public boolean destroyBlock(BlockPos pos, boolean dropBlock)
    {
        IBlockState iblockstate = this.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block.isAir(iblockstate, this, pos))
        {
            return false;
        }
        else
        {
            this.playEvent(2001, pos, Block.getStateId(iblockstate));

            if (dropBlock)
            {
                block.dropBlockAsItem(this, pos, iblockstate, 0);
            }

            return this.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    public boolean setBlockState(BlockPos pos, IBlockState state)
    {
        return this.setBlockState(pos, state, 3);
    }

    public void notifyBlockUpdate(BlockPos pos, IBlockState oldState, IBlockState newState, int flags)
    {
        for (int i = 0; i < this.eventListeners.size(); ++i)
        {
            ((IWorldEventListener)this.eventListeners.get(i)).notifyBlockUpdate(this, pos, oldState, newState, flags);
        }
    }

    public void notifyNeighborsRespectDebug(BlockPos pos, Block blockType, boolean p_175722_3_)
    {
        if (this.worldInfo.getTerrainType() != WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            if (populating) return; // Akarin
            this.notifyNeighborsOfStateChange(pos, blockType, p_175722_3_);
        }
    }

    public void markBlocksDirtyVertical(int x, int z, int y1, int y2)
    {
        if (y1 > y2)
        {
            int i = y2;
            y2 = y1;
            y1 = i;
        }

        if (this.provider.hasSkyLight())
        {
            for (int j = y1; j <= y2; ++j)
            {
                this.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, j, z));
            }
        }

        this.markBlockRangeForRenderUpdate(x, y1, z, x, y2, z);
    }

    public void markBlockRangeForRenderUpdate(BlockPos rangeMin, BlockPos rangeMax)
    {
        this.markBlockRangeForRenderUpdate(rangeMin.getX(), rangeMin.getY(), rangeMin.getZ(), rangeMax.getX(), rangeMax.getY(), rangeMax.getZ());
    }

    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        for (int i = 0; i < this.eventListeners.size(); ++i)
        {
            ((IWorldEventListener)this.eventListeners.get(i)).markBlockRangeForRenderUpdate(x1, y1, z1, x2, y2, z2);
        }
    }

    public void updateObservingBlocksAt(BlockPos pos, Block blockType)
    {
        this.observedNeighborChanged(pos.west(), blockType, pos);
        this.observedNeighborChanged(pos.east(), blockType, pos);
        this.observedNeighborChanged(pos.down(), blockType, pos);
        this.observedNeighborChanged(pos.up(), blockType, pos);
        this.observedNeighborChanged(pos.north(), blockType, pos);
        this.observedNeighborChanged(pos.south(), blockType, pos);
    }

    public void notifyNeighborsOfStateChange(BlockPos pos, Block blockType, boolean updateObservers)
    {
        if(net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(this, pos, this.getBlockState(pos), java.util.EnumSet.allOf(EnumFacing.class), updateObservers).isCanceled())
            return;

        this.neighborChanged(pos.west(), blockType, pos);
        this.neighborChanged(pos.east(), blockType, pos);
        this.neighborChanged(pos.down(), blockType, pos);
        this.neighborChanged(pos.up(), blockType, pos);
        this.neighborChanged(pos.north(), blockType, pos);
        this.neighborChanged(pos.south(), blockType, pos);

        if (updateObservers)
        {
            this.updateObservingBlocksAt(pos, blockType);
        }
    }

    public void notifyNeighborsOfStateExcept(BlockPos pos, Block blockType, EnumFacing skipSide)
    {
        java.util.EnumSet<EnumFacing> directions = java.util.EnumSet.allOf(EnumFacing.class);
        directions.remove(skipSide);
        if(net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(this, pos, this.getBlockState(pos), directions, false).isCanceled())
            return;

        if (skipSide != EnumFacing.WEST)
        {
            this.neighborChanged(pos.west(), blockType, pos);
        }

        if (skipSide != EnumFacing.EAST)
        {
            this.neighborChanged(pos.east(), blockType, pos);
        }

        if (skipSide != EnumFacing.DOWN)
        {
            this.neighborChanged(pos.down(), blockType, pos);
        }

        if (skipSide != EnumFacing.UP)
        {
            this.neighborChanged(pos.up(), blockType, pos);
        }

        if (skipSide != EnumFacing.NORTH)
        {
            this.neighborChanged(pos.north(), blockType, pos);
        }

        if (skipSide != EnumFacing.SOUTH)
        {
            this.neighborChanged(pos.south(), blockType, pos);
        }
    }

    public void neighborChanged(BlockPos pos, final Block blockIn, BlockPos fromPos)
    {
        if (!this.isRemote)
        {
            IBlockState iblockstate = this.getBlockState(pos);

            try
            {
                // Akarin start
                CraftWorld world = ((WorldServer) this).getWorld();
                if (world != null) {
                    BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(pos.getX(), pos.getY(), pos.getZ()), CraftMagicNumbers.getId(blockIn));
                    this.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                }
                // Akarin end
                iblockstate.neighborChanged(this, pos, blockIn, fromPos);
            }
            // Akarin start
            catch (StackOverflowError stackoverflowerror) {
                haveWeSilencedAPhysicsCrash = true;
                blockLocation = pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
            }
            // Akarin end
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
                crashreportcategory.addDetail("Source block type", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        try
                        {
                            return String.format("ID #%d (%s // %s // %s)", Block.getIdFromBlock(blockIn), blockIn.getUnlocalizedName(), blockIn.getClass().getName(), blockIn.getRegistryName());
                        }
                        catch (Throwable var2)
                        {
                            return "ID #" + Block.getIdFromBlock(blockIn);
                        }
                    }
                });
                CrashReportCategory.addBlockInfo(crashreportcategory, pos, iblockstate);
                throw new ReportedException(crashreport);
            }
        }
    }

    public void observedNeighborChanged(BlockPos pos, final Block p_190529_2_, BlockPos p_190529_3_)
    {
        if (!this.isRemote)
        {
            IBlockState iblockstate = this.getBlockState(pos);

            if (true)
            {
                try
                {
                    iblockstate.getBlock().observedNeighborChange(iblockstate, this, pos, p_190529_2_, p_190529_3_);
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
                    crashreportcategory.addDetail("Source block type", new ICrashReportDetail<String>()
                    {
                        public String call() throws Exception
                        {
                            try
                            {
                                return String.format("ID #%d (%s // %s // %s)", Block.getIdFromBlock(p_190529_2_), p_190529_2_.getUnlocalizedName(), p_190529_2_.getClass().getName(), p_190529_2_.getRegistryName());
                            }
                            catch (Throwable var2)
                            {
                                return "ID #" + Block.getIdFromBlock(p_190529_2_);
                            }
                        }
                    });
                    CrashReportCategory.addBlockInfo(crashreportcategory, pos, iblockstate);
                    throw new ReportedException(crashreport);
                }
            }
        }
    }

    public boolean isBlockTickPending(BlockPos pos, Block blockType)
    {
        return false;
    }

    public boolean canSeeSky(BlockPos pos)
    {
        return this.getChunkFromBlockCoords(pos).canSeeSky(pos);
    }

    public boolean canBlockSeeSky(BlockPos pos)
    {
        if (pos.getY() >= this.getSeaLevel())
        {
            return this.canSeeSky(pos);
        }
        else
        {
            BlockPos blockpos = new BlockPos(pos.getX(), this.getSeaLevel(), pos.getZ());

            if (!this.canSeeSky(blockpos))
            {
                return false;
            }
            else
            {
                for (BlockPos blockpos1 = blockpos.down(); blockpos1.getY() > pos.getY(); blockpos1 = blockpos1.down())
                {
                    IBlockState iblockstate = this.getBlockState(blockpos1);

                    if (iblockstate.getBlock().getLightOpacity(iblockstate, this, blockpos) > 0 && !iblockstate.getMaterial().isLiquid())
                    {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public int getLight(BlockPos pos)
    {
        if (pos.getY() < 0)
        {
            return 0;
        }
        else
        {
            if (pos.getY() >= 256)
            {
                pos = new BlockPos(pos.getX(), 255, pos.getZ());
            }

            return this.getChunkFromBlockCoords(pos).getLightSubtracted(pos, 0);
        }
    }

    public int getLightFromNeighbors(BlockPos pos)
    {
        return this.getLight(pos, true);
    }

    public int getLight(BlockPos pos, boolean checkNeighbors)
    {
        if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000)
        {
            if (checkNeighbors && this.getBlockState(pos).useNeighborBrightness())
            {
                int i1 = this.getLight(pos.up(), false);
                int i = this.getLight(pos.east(), false);
                int j = this.getLight(pos.west(), false);
                int k = this.getLight(pos.south(), false);
                int l = this.getLight(pos.north(), false);

                if (i > i1)
                {
                    i1 = i;
                }

                if (j > i1)
                {
                    i1 = j;
                }

                if (k > i1)
                {
                    i1 = k;
                }

                if (l > i1)
                {
                    i1 = l;
                }

                return i1;
            }
            else if (pos.getY() < 0)
            {
                return 0;
            }
            else
            {
                if (pos.getY() >= 256)
                {
                    pos = new BlockPos(pos.getX(), 255, pos.getZ());
                }

                Chunk chunk = this.getChunkFromBlockCoords(pos);
                return chunk.getLightSubtracted(pos, this.skylightSubtracted);
            }
        }
        else
        {
            return 15;
        }
    }

    public BlockPos getHeight(BlockPos pos)
    {
        return new BlockPos(pos.getX(), this.getHeight(pos.getX(), pos.getZ()), pos.getZ());
    }

    public int getHeight(int x, int z)
    {
        int i;

        if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000)
        {
            if (this.isChunkLoaded(x >> 4, z >> 4, true))
            {
                i = this.getChunkFromChunkCoords(x >> 4, z >> 4).getHeightValue(x & 15, z & 15);
            }
            else
            {
                i = 0;
            }
        }
        else
        {
            i = this.getSeaLevel() + 1;
        }

        return i;
    }

    @Deprecated
    public int getChunksLowestHorizon(int x, int z)
    {
        if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000)
        {
            if (!this.isChunkLoaded(x >> 4, z >> 4, true))
            {
                return 0;
            }
            else
            {
                Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                return chunk.getLowestHeight();
            }
        }
        else
        {
            return this.getSeaLevel() + 1;
        }
    }

    @SideOnly(Side.CLIENT)
    public int getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos)
    {
        if (!this.provider.hasSkyLight() && type == EnumSkyBlock.SKY)
        {
            return 0;
        }
        else
        {
            if (pos.getY() < 0)
            {
                pos = new BlockPos(pos.getX(), 0, pos.getZ());
            }

            if (!this.isValid(pos))
            {
                return type.defaultLightValue;
            }
            else if (!this.isBlockLoaded(pos))
            {
                return type.defaultLightValue;
            }
            else if (this.getBlockState(pos).useNeighborBrightness())
            {
                int i1 = this.getLightFor(type, pos.up());
                int i = this.getLightFor(type, pos.east());
                int j = this.getLightFor(type, pos.west());
                int k = this.getLightFor(type, pos.south());
                int l = this.getLightFor(type, pos.north());

                if (i > i1)
                {
                    i1 = i;
                }

                if (j > i1)
                {
                    i1 = j;
                }

                if (k > i1)
                {
                    i1 = k;
                }

                if (l > i1)
                {
                    i1 = l;
                }

                return i1;
            }
            else
            {
                Chunk chunk = this.getChunkFromBlockCoords(pos);
                return chunk.getLightFor(type, pos);
            }
        }
    }

    public int getLightFor(EnumSkyBlock type, BlockPos pos)
    {
        if (pos.getY() < 0)
        {
            pos = new BlockPos(pos.getX(), 0, pos.getZ());
        }

        if (!this.isValid(pos))
        {
            return type.defaultLightValue;
        }
        else if (!this.isBlockLoaded(pos))
        {
            return type.defaultLightValue;
        }
        else
        {
            Chunk chunk = this.getChunkFromBlockCoords(pos);
            return chunk.getLightFor(type, pos);
        }
    }

    public void setLightFor(EnumSkyBlock type, BlockPos pos, int lightValue)
    {
        if (this.isValid(pos))
        {
            if (this.isBlockLoaded(pos))
            {
                Chunk chunk = this.getChunkFromBlockCoords(pos);
                chunk.setLightFor(type, pos, lightValue);
                this.notifyLightSet(pos);
            }
        }
    }

    public void notifyLightSet(BlockPos pos)
    {
        for (int i = 0; i < this.eventListeners.size(); ++i)
        {
            ((IWorldEventListener)this.eventListeners.get(i)).notifyLightSet(pos);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getCombinedLight(BlockPos pos, int lightValue)
    {
        int i = this.getLightFromNeighborsFor(EnumSkyBlock.SKY, pos);
        int j = this.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, pos);

        if (j < lightValue)
        {
            j = lightValue;
        }

        return i << 20 | j << 4;
    }

    public float getLightBrightness(BlockPos pos)
    {
        return this.provider.getLightBrightnessTable()[this.getLightFromNeighbors(pos)];
    }

    public IBlockState getBlockState(BlockPos pos)
    {
        // Akarin start
        if (this.captureTreeGeneration && Bukkit.isPrimaryThread()) {
            for (BlockSnapshot blocksnapshot : this.capturedBlockSnapshots) {
                if (!blocksnapshot.getPos().equals(pos)) continue;
                return blocksnapshot.getReplacedBlock();
            }
        }
        // Akarin end
        if (this.isOutsideBuildHeight(pos))
        {
            return Blocks.AIR.getDefaultState();
        }
        else
        {
            Chunk chunk = this.getChunkFromBlockCoords(pos);
            return chunk.getBlockState(pos);
        }
    }

    public boolean isDaytime()
    {
        return this.provider.isDaytime();
    }

    @Nullable
    public RayTraceResult rayTraceBlocks(Vec3d start, Vec3d end)
    {
        return this.rayTraceBlocks(start, end, false, false, false);
    }

    @Nullable
    public RayTraceResult rayTraceBlocks(Vec3d start, Vec3d end, boolean stopOnLiquid)
    {
        return this.rayTraceBlocks(start, end, stopOnLiquid, false, false);
    }

    @Nullable
    public RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
    {
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z))
        {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z))
            {
                int i = MathHelper.floor(vec32.x);
                int j = MathHelper.floor(vec32.y);
                int k = MathHelper.floor(vec32.z);
                int l = MathHelper.floor(vec31.x);
                int i1 = MathHelper.floor(vec31.y);
                int j1 = MathHelper.floor(vec31.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = this.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(this, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid))
                {
                    RayTraceResult raytraceresult = iblockstate.collisionRayTrace(this, blockpos, vec31, vec32);

                    if (raytraceresult != null)
                    {
                        return raytraceresult;
                    }
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z))
                    {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2)
                    {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = this.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(this, blockpos) != Block.NULL_AABB)
                    {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid))
                        {
                            RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(this, blockpos, vec31, vec32);

                            if (raytraceresult1 != null)
                            {
                                return raytraceresult1;
                            }
                        }
                        else
                        {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    public void playSound(@Nullable EntityPlayer player, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch)
    {
        this.playSound(player, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, soundIn, category, volume, pitch);
    }

    public void playSound(@Nullable EntityPlayer player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch)
    {
        net.minecraftforge.event.entity.PlaySoundAtEntityEvent event = net.minecraftforge.event.ForgeEventFactory.onPlaySoundAtEntity(player, soundIn, category, volume, pitch);
        if (event.isCanceled() || event.getSound() == null) return;
        soundIn = event.getSound();
        category = event.getCategory();
        volume = event.getVolume();
        pitch = event.getPitch();

        for (int i = 0; i < this.eventListeners.size(); ++i)
        {
            ((IWorldEventListener)this.eventListeners.get(i)).playSoundToAllNearExcept(player, soundIn, category, x, y, z, volume, pitch);
        }
    }

    public void playSound(double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay)
    {
    }

    public void playRecord(BlockPos blockPositionIn, @Nullable SoundEvent soundEventIn)
    {
        for (int i = 0; i < this.eventListeners.size(); ++i)
        {
            ((IWorldEventListener)this.eventListeners.get(i)).playRecord(soundEventIn, blockPositionIn);
        }
    }

    public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters)
    {
        this.spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
    }

    public void spawnAlwaysVisibleParticle(int p_190523_1_, double p_190523_2_, double p_190523_4_, double p_190523_6_, double p_190523_8_, double p_190523_10_, double p_190523_12_, int... p_190523_14_)
    {
        for (int i = 0; i < this.eventListeners.size(); ++i)
        {
            ((IWorldEventListener)this.eventListeners.get(i)).spawnParticle(p_190523_1_, false, true, p_190523_2_, p_190523_4_, p_190523_6_, p_190523_8_, p_190523_10_, p_190523_12_, p_190523_14_);
        }
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticle(EnumParticleTypes particleType, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters)
    {
        this.spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange() || ignoreRange, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
    }

    private void spawnParticle(int particleID, boolean ignoreRange, double xCood, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters)
    {
        for (int i = 0; i < this.eventListeners.size(); ++i)
        {
            ((IWorldEventListener)this.eventListeners.get(i)).spawnParticle(particleID, ignoreRange, xCood, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
        }
    }

    public boolean addWeatherEffect(Entity entityIn)
    {
        this.weatherEffects.add(entityIn);
        return true;
    }

    public boolean spawnEntity(Entity entityIn)
    {
        // Akarin start
        return this.addEntity(entityIn, CreatureSpawnEvent.SpawnReason.DEFAULT);
    }

    public boolean addEntity(Entity entityIn, CreatureSpawnEvent.SpawnReason spawnReason) {
        if (entityIn == null) {
            return false;
        }
        if (this.restoringBlockSnapshots) {
            return true;
        }
        if (this.worldCapture.isCapture() && this.captureBlockSnapshots && !(entityIn instanceof EntityPlayer)) {
            this.worldCapture.addEntitySnap(entityIn, spawnReason);
            return true;
        }
        Cancellable event = null;
        if (entityIn instanceof EntityLivingBase && !(entityIn instanceof EntityPlayerMP)) {
            boolean isAnimal = entityIn instanceof EntityAnimal || entityIn instanceof EntityWaterMob || entityIn instanceof EntityGolem;
            boolean isMonster = entityIn instanceof EntityMob || entityIn instanceof EntityGhast || entityIn instanceof EntitySlime;
            boolean isNpc = entityIn instanceof INpc;
            
            if (spawnReason != SpawnReason.CUSTOM) {
                if (isAnimal && !spawnPeacefulMobs || isMonster && !spawnHostileMobs || isNpc && !getServer().getServer().getCanSpawnNPCs()) {
                    entityIn.isDead = true;
                    return false;
                }
            }
            
            event = CraftEventFactory.callCreatureSpawnEvent((EntityLivingBase) entityIn, spawnReason);
        } else if (entityIn instanceof EntityItem) {
            event = CraftEventFactory.callItemSpawnEvent((EntityItem) entityIn);
        } else if (entityIn.getBukkitEntity() instanceof org.bukkit.entity.Projectile) {
            // Not all projectiles extend EntityProjectile, so check for Bukkit interface instead
            event = CraftEventFactory.callProjectileLaunchEvent(entityIn);
        } else if (entityIn.getBukkitEntity() instanceof org.bukkit.entity.Vehicle){
            event = CraftEventFactory.callVehicleCreateEvent(entityIn);
        } else if (entityIn instanceof EntityXPOrb) {
            EntityXPOrb xp = (EntityXPOrb) entityIn;
            double radius = this.spigotConfig.expMerge;
            if (radius > 0.0) {
                final int maxValue = paperConfig.expMergeMaxValue;
                final boolean mergeUnconditionally = paperConfig.expMergeMaxValue <= 0;
                if (mergeUnconditionally || xp.xpValue < maxValue) {
                    
                    List<Entity> entities = this.getEntitiesWithinAABBExcludingEntity(entityIn, entityIn.getEntityBoundingBox().grow(radius, radius, radius));
                    for (Entity e : entities) {
                        if (e instanceof EntityXPOrb) {
                            EntityXPOrb loopItem = (EntityXPOrb) e;
                            if (!loopItem.isDead && !(maxValue > 0 && loopItem.xpValue >= maxValue) && new com.destroystokyo.paper.event.entity.ExperienceOrbMergeEvent((org.bukkit.entity.ExperienceOrb) entityIn.getBukkitEntity(), (org.bukkit.entity.ExperienceOrb) loopItem.getBukkitEntity()).callEvent()) {
                                long newTotal = (long)xp.xpValue + (long)loopItem.xpValue;
                                if ((int) newTotal < 0) continue; // Overflow
                                if (maxValue > 0 && newTotal > (long)maxValue) {
                                    loopItem.xpValue = (int) (newTotal - maxValue);
                                    xp.xpValue = maxValue;
                                } else {
                                    xp.xpValue += loopItem.xpValue;
                                    loopItem.setDead();
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (event != null && (event.isCancelled() || entityIn.isDead)) {
            entityIn.isDead = true;
            return false;
        }
        // Akarin end
        // do not drop any items while restoring blocksnapshots. Prevents dupes
        if (!this.isRemote && (entityIn == null || (entityIn instanceof net.minecraft.entity.item.EntityItem && this.restoringBlockSnapshots))) return false;

        int i = MathHelper.floor(entityIn.posX / 16.0D);
        int j = MathHelper.floor(entityIn.posZ / 16.0D);
        boolean flag = entityIn.forceSpawn;

        if (entityIn instanceof EntityPlayer)
        {
            flag = true;
        }

        if (!flag && !this.isChunkLoaded(i, j, false))
        {
            return false;
        }
        else
        {
            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                this.playerEntities.add(entityplayer);
                this.updateAllPlayersSleepingFlag();
            }

            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.EntityJoinWorldEvent(entityIn, this)) && !flag) return false;

            this.getChunkFromChunkCoords(i, j).addEntity(entityIn);
            this.loadedEntityList.add(entityIn);
            this.onEntityAdded(entityIn);
            return true;
        }
    }

    public void onEntityAdded(Entity entityIn)
    {
        for (int i = 0; i < this.eventListeners.size(); ++i)
        {
            ((IWorldEventListener)this.eventListeners.get(i)).onEntityAdded(entityIn);
        }
        entityIn.onAddedToWorld();
        entityIn.valid = true;
    }

    public void onEntityRemoved(Entity entityIn)
    {
        for (int i = 0; i < this.eventListeners.size(); ++i)
        {
            ((IWorldEventListener)this.eventListeners.get(i)).onEntityRemoved(entityIn);
        }
        entityIn.onRemovedFromWorld();
        entityIn.valid = false;
    }

    public void removeEntity(Entity entityIn)
    {
        if (entityIn.isBeingRidden())
        {
            entityIn.removePassengers();
        }

        if (entityIn.isRiding())
        {
            entityIn.dismountRidingEntity();
        }

        entityIn.setDead();

        if (entityIn instanceof EntityPlayer)
        {
            this.playerEntities.remove(entityIn);
            // Akarin start
            for ( WorldSavedData o : mapStorage.loadedDataList )
            {
                if ( o instanceof MapData )
                {
                    MapData map = (MapData) o;
                    map.playersHashMap.remove( entityIn );
                    for ( Iterator<MapData.MapInfo> iter = (Iterator<MapData.MapInfo>) map.playersArrayList.iterator(); iter.hasNext(); )
                    {
                        if ( iter.next().player == entityIn )
                        {
                            map.mapDecorations.remove(entityIn.getName());
                            iter.remove();
                        }
                    }
                }
            }
            // Akarin end
            this.updateAllPlayersSleepingFlag();
            this.onEntityRemoved(entityIn);
        }
    }

    public void removeEntityDangerously(Entity entityIn)
    {
        entityIn.setDropItemsWhenDead(false);
        entityIn.setDead();

        if (entityIn instanceof EntityPlayer)
        {
            this.playerEntities.remove(entityIn);
            this.updateAllPlayersSleepingFlag();
        }

        int i = entityIn.chunkCoordX;
        int j = entityIn.chunkCoordZ;

        if (entityIn.addedToChunk && this.isChunkLoaded(i, j, true))
        {
            this.getChunkFromChunkCoords(i, j).removeEntity(entityIn);
        }

        // Akarin start
        int index;
        if ((index = this.loadedEntityList.indexOf(entityIn)) != -1) {
            if (index <= this.tickPosition) {
                --this.tickPosition;
            }
            this.loadedEntityList.remove(index);
        }
        // Akarin end
        this.onEntityRemoved(entityIn);
    }

    public void addEventListener(IWorldEventListener listener)
    {
        this.eventListeners.add(listener);
    }

    private boolean getCollisionBoxes(@Nullable Entity entityIn, AxisAlignedBB aabb, boolean p_191504_3_, @Nullable List<AxisAlignedBB> outList)
    {
        int i = MathHelper.floor(aabb.minX) - 1;
        int j = MathHelper.ceil(aabb.maxX) + 1;
        int k = MathHelper.floor(aabb.minY) - 1;
        int l = MathHelper.ceil(aabb.maxY) + 1;
        int i1 = MathHelper.floor(aabb.minZ) - 1;
        int j1 = MathHelper.ceil(aabb.maxZ) + 1;
        WorldBorder worldborder = this.getWorldBorder();
        boolean flag = entityIn != null && entityIn.isOutsideBorder();
        boolean flag1 = entityIn != null && this.isInsideWorldBorder(entityIn);
        IBlockState iblockstate = Blocks.STONE.getDefaultState();
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        if (p_191504_3_ && !net.minecraftforge.event.ForgeEventFactory.gatherCollisionBoxes(this, entityIn, aabb, outList)) return true;
        try
        {
            for (int k1 = i; k1 < j; ++k1)
            {
                for (int l1 = i1; l1 < j1; ++l1)
                {
                    boolean flag2 = k1 == i || k1 == j - 1;
                    boolean flag3 = l1 == i1 || l1 == j1 - 1;

                    if ((!flag2 || !flag3) && this.isBlockLoaded(blockpos$pooledmutableblockpos.setPos(k1, 64, l1)))
                    {
                        for (int i2 = k; i2 < l; ++i2)
                        {
                            if (!flag2 && !flag3 || i2 != l - 1)
                            {
                                if (p_191504_3_)
                                {
                                    if (k1 < -30000000 || k1 >= 30000000 || l1 < -30000000 || l1 >= 30000000)
                                    {
                                        boolean lvt_21_2_ = true;
                                        return lvt_21_2_;
                                    }
                                }
                                else if (entityIn != null && flag == flag1)
                                {
                                    entityIn.setOutsideBorder(!flag1);
                                }

                                blockpos$pooledmutableblockpos.setPos(k1, i2, l1);
                                IBlockState iblockstate1;

                                if (!p_191504_3_ && !worldborder.contains(blockpos$pooledmutableblockpos) && flag1)
                                {
                                    iblockstate1 = iblockstate;
                                }
                                else
                                {
                                    iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos);
                                }

                                iblockstate1.addCollisionBoxToList(this, blockpos$pooledmutableblockpos, aabb, outList, entityIn, false);

                                if (p_191504_3_ && !net.minecraftforge.event.ForgeEventFactory.gatherCollisionBoxes(this, entityIn, aabb, outList))
                                {
                                    boolean flag5 = true;
                                    return flag5;
                                }
                            }
                        }
                    }
                }
            }
        }
        finally
        {
            blockpos$pooledmutableblockpos.release();
        }

        return !outList.isEmpty();
    }

    public List<AxisAlignedBB> getCollisionBoxes(@Nullable Entity entityIn, AxisAlignedBB aabb)
    {
        List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();
        this.getCollisionBoxes(entityIn, aabb, false, list);

        if (entityIn != null)
        {
            List<Entity> list1 = this.getEntitiesWithinAABBExcludingEntity(entityIn, aabb.grow(0.25D));

            for (int i = 0; i < list1.size(); ++i)
            {
                Entity entity = list1.get(i);

                if (!entityIn.isRidingSameEntity(entity))
                {
                    AxisAlignedBB axisalignedbb = entity.getCollisionBoundingBox();

                    if (axisalignedbb != null && axisalignedbb.intersects(aabb))
                    {
                        list.add(axisalignedbb);
                    }

                    axisalignedbb = entityIn.getCollisionBox(entity);

                    if (axisalignedbb != null && axisalignedbb.intersects(aabb))
                    {
                        list.add(axisalignedbb);
                    }
                }
            }
        }
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.GetCollisionBoxesEvent(this, entityIn, aabb, list));
        return list;
    }

    public void removeEventListener(IWorldEventListener listener)
    {
        this.eventListeners.remove(listener);
    }

    public boolean isInsideWorldBorder(Entity p_191503_1_)
    {
        double d0 = this.worldBorder.minX();
        double d1 = this.worldBorder.minZ();
        double d2 = this.worldBorder.maxX();
        double d3 = this.worldBorder.maxZ();

        if (p_191503_1_.isOutsideBorder())
        {
            ++d0;
            ++d1;
            --d2;
            --d3;
        }
        else
        {
            --d0;
            --d1;
            ++d2;
            ++d3;
        }

        return p_191503_1_.posX > d0 && p_191503_1_.posX < d2 && p_191503_1_.posZ > d1 && p_191503_1_.posZ < d3;
    }

    public boolean collidesWithAnyBlock(AxisAlignedBB bbox)
    {
        return this.getCollisionBoxes((Entity)null, bbox, true, Lists.newArrayList());
    }

    public int calculateSkylightSubtracted(float partialTicks)
    {
        float f = provider.getSunBrightnessFactor(partialTicks);
        f = 1 - f;
        return (int)(f * 11);
    }

    /**
     * The current sun brightness factor for this dimension.
     * 0.0f means no light at all, and 1.0f means maximum sunlight.
     * Highly recommended for sunlight detection like solar panel.
     *
     * @return The current brightness factor
     * */
    public float getSunBrightnessFactor(float partialTicks)
    {
        float f = this.getCelestialAngle(partialTicks);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float)((double)f1 * (1.0D - (double)(this.getRainStrength(partialTicks) * 5.0F) / 16.0D));
        f1 = (float)((double)f1 * (1.0D - (double)(this.getThunderStrength(partialTicks) * 5.0F) / 16.0D));
        return f1;
    }

    @SideOnly(Side.CLIENT)
    public float getSunBrightness(float partialTicks)
    {
        return this.provider.getSunBrightness(partialTicks);
    }

    @SideOnly(Side.CLIENT)
    public float getSunBrightnessBody(float partialTicks)
    {
        float f = this.getCelestialAngle(partialTicks);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.2F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float)((double)f1 * (1.0D - (double)(this.getRainStrength(partialTicks) * 5.0F) / 16.0D));
        f1 = (float)((double)f1 * (1.0D - (double)(this.getThunderStrength(partialTicks) * 5.0F) / 16.0D));
        return f1 * 0.8F + 0.2F;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(Entity entityIn, float partialTicks)
    {
        return this.provider.getSkyColor(entityIn, partialTicks);
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColorBody(Entity entityIn, float partialTicks)
    {
        float f = this.getCelestialAngle(partialTicks);
        float f1 = MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        int i = MathHelper.floor(entityIn.posX);
        int j = MathHelper.floor(entityIn.posY);
        int k = MathHelper.floor(entityIn.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        int l = net.minecraftforge.client.ForgeHooksClient.getSkyBlendColour(this, blockpos);
        float f3 = (float)(l >> 16 & 255) / 255.0F;
        float f4 = (float)(l >> 8 & 255) / 255.0F;
        float f5 = (float)(l & 255) / 255.0F;
        f3 = f3 * f1;
        f4 = f4 * f1;
        f5 = f5 * f1;
        float f6 = this.getRainStrength(partialTicks);

        if (f6 > 0.0F)
        {
            float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
            float f8 = 1.0F - f6 * 0.75F;
            f3 = f3 * f8 + f7 * (1.0F - f8);
            f4 = f4 * f8 + f7 * (1.0F - f8);
            f5 = f5 * f8 + f7 * (1.0F - f8);
        }

        float f10 = this.getThunderStrength(partialTicks);

        if (f10 > 0.0F)
        {
            float f11 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
            float f9 = 1.0F - f10 * 0.75F;
            f3 = f3 * f9 + f11 * (1.0F - f9);
            f4 = f4 * f9 + f11 * (1.0F - f9);
            f5 = f5 * f9 + f11 * (1.0F - f9);
        }

        if (this.lastLightningBolt > 0)
        {
            float f12 = (float)this.lastLightningBolt - partialTicks;

            if (f12 > 1.0F)
            {
                f12 = 1.0F;
            }

            f12 = f12 * 0.45F;
            f3 = f3 * (1.0F - f12) + 0.8F * f12;
            f4 = f4 * (1.0F - f12) + 0.8F * f12;
            f5 = f5 * (1.0F - f12) + 1.0F * f12;
        }

        return new Vec3d((double)f3, (double)f4, (double)f5);
    }

    public float getCelestialAngle(float partialTicks)
    {
        return this.provider.calculateCelestialAngle(this.getWorldTime(), partialTicks);
    }

    @SideOnly(Side.CLIENT)
    public int getMoonPhase()
    {
        return this.provider.getMoonPhase(this.getWorldTime());
    }

    public float getCurrentMoonPhaseFactor()
    {
        return provider.getCurrentMoonPhaseFactor();
    }

    public float getCurrentMoonPhaseFactorBody()
    {
        return WorldProvider.MOON_PHASE_FACTORS[this.provider.getMoonPhase(this.getWorldTime())];
    }

    public float getCelestialAngleRadians(float partialTicks)
    {
        float f = this.getCelestialAngle(partialTicks);
        return f * ((float)Math.PI * 2F);
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getCloudColour(float partialTicks)
    {
        return this.provider.getCloudColor(partialTicks);
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getCloudColorBody(float partialTicks)
    {
        float f = this.getCelestialAngle(partialTicks);
        float f1 = MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        float f2 = 1.0F;
        float f3 = 1.0F;
        float f4 = 1.0F;
        float f5 = this.getRainStrength(partialTicks);

        if (f5 > 0.0F)
        {
            float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
            float f7 = 1.0F - f5 * 0.95F;
            f2 = f2 * f7 + f6 * (1.0F - f7);
            f3 = f3 * f7 + f6 * (1.0F - f7);
            f4 = f4 * f7 + f6 * (1.0F - f7);
        }

        f2 = f2 * (f1 * 0.9F + 0.1F);
        f3 = f3 * (f1 * 0.9F + 0.1F);
        f4 = f4 * (f1 * 0.85F + 0.15F);
        float f9 = this.getThunderStrength(partialTicks);

        if (f9 > 0.0F)
        {
            float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
            float f8 = 1.0F - f9 * 0.95F;
            f2 = f2 * f8 + f10 * (1.0F - f8);
            f3 = f3 * f8 + f10 * (1.0F - f8);
            f4 = f4 * f8 + f10 * (1.0F - f8);
        }

        return new Vec3d((double)f2, (double)f3, (double)f4);
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float partialTicks)
    {
        float f = this.getCelestialAngle(partialTicks);
        return this.provider.getFogColor(f, partialTicks);
    }

    public BlockPos getPrecipitationHeight(BlockPos pos)
    {
        return this.getChunkFromBlockCoords(pos).getPrecipitationHeight(pos);
    }

    public BlockPos getTopSolidOrLiquidBlock(BlockPos pos)
    {
        Chunk chunk = this.getChunkFromBlockCoords(pos);
        BlockPos blockpos;
        BlockPos blockpos1;

        for (blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos.getY() >= 0; blockpos = blockpos1)
        {
            blockpos1 = blockpos.down();
            IBlockState state = chunk.getBlockState(blockpos1);

            if (state.getMaterial().blocksMovement() && !state.getBlock().isLeaves(state, this, blockpos1) && !state.getBlock().isFoliage(this, blockpos1))
            {
                break;
            }
        }

        return blockpos;
    }

    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float partialTicks)
    {
        return this.provider.getStarBrightness(partialTicks);
    }

    @SideOnly(Side.CLIENT)
    public float getStarBrightnessBody(float partialTicks)
    {
        float f = this.getCelestialAngle(partialTicks);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.25F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        return f1 * f1 * 0.5F;
    }

    public boolean isUpdateScheduled(BlockPos pos, Block blk)
    {
        return true;
    }

    public void scheduleUpdate(BlockPos pos, Block blockIn, int delay)
    {
    }

    public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority)
    {
    }

    public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority)
    {
    }

    public void updateEntities()
    {
        this.profiler.startSection("entities");
        this.profiler.startSection("global");

        for (int i = 0; i < this.weatherEffects.size(); ++i)
        {
            Entity entity = this.weatherEffects.get(i);
            if (entity == null) continue;

            try
            {
                if(entity.updateBlocked) continue;
                ++entity.ticksExisted;
                entity.onUpdate();
            }
            catch (Throwable throwable2)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");

                if (entity == null)
                {
                    crashreportcategory.addCrashSection("Entity", "~~NULL~~");
                }
                else
                {
                    entity.addEntityCrashInfo(crashreportcategory);
                }

                if (net.minecraftforge.common.ForgeModContainer.removeErroringEntities)
                {
                    net.minecraftforge.fml.common.FMLLog.log.fatal("{}", crashreport.getCompleteReport());
                    removeEntity(entity);
                }
                else
                throw new ReportedException(crashreport);
            }

            if (entity.isDead)
            {
                this.weatherEffects.remove(i--);
            }
        }

        this.profiler.endStartSection("remove");
        this.loadedEntityList.removeAll(this.unloadedEntityList);

        for (int k = 0; k < this.unloadedEntityList.size(); ++k)
        {
            Entity entity1 = this.unloadedEntityList.get(k);
            int j = entity1.chunkCoordX;
            int k1 = entity1.chunkCoordZ;

            if (entity1.addedToChunk && this.isChunkLoaded(j, k1, true))
            {
                this.getChunkFromChunkCoords(j, k1).removeEntity(entity1);
            }
        }

        for (int l = 0; l < this.unloadedEntityList.size(); ++l)
        {
            this.onEntityRemoved(this.unloadedEntityList.get(l));
        }

        this.unloadedEntityList.clear();
        this.tickPlayers();
        this.profiler.endStartSection("regular");
        // Akarin
        ActivationRange.activateEntities(this);
        int entitiesThisCycle = 0;
        if (this.tickPosition < 0) {
            this.tickPosition = 0;
        }
        this.entityLimiter.initTick();
        while (entitiesThisCycle < this.loadedEntityList.size() && (entitiesThisCycle % 10 != 0 || this.entityLimiter.shouldContinue())) {
            this.tickPosition = this.tickPosition < this.loadedEntityList.size() ? this.tickPosition : 0;
            Entity entity2 = this.loadedEntityList.get(this.tickPosition);
            // Akarin end
            Entity entity3 = entity2.getRidingEntity();

            if (entity3 != null)
            {
                if (!entity3.isDead && entity3.isPassenger(entity2))
                {
                    continue;
                }

                entity2.dismountRidingEntity();
            }

            this.profiler.startSection("tick");

            if (!entity2.isDead && !(entity2 instanceof EntityPlayerMP))
            {
                try
                {
                    net.minecraftforge.server.timings.TimeTracker.ENTITY_UPDATE.trackStart(entity2);
                    this.updateEntity(entity2);
                    net.minecraftforge.server.timings.TimeTracker.ENTITY_UPDATE.trackEnd(entity2);
                }
                catch (Throwable throwable1)
                {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Ticking entity");
                    CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Entity being ticked");
                    entity2.addEntityCrashInfo(crashreportcategory1);
                    if (net.minecraftforge.common.ForgeModContainer.removeErroringEntities)
                    {
                        net.minecraftforge.fml.common.FMLLog.log.fatal("{}", crashreport1.getCompleteReport());
                        removeEntity(entity2);
                    }
                    else
                    throw new ReportedException(crashreport1);
                }
            }

            this.profiler.endSection();
            this.profiler.startSection("remove");

            if (entity2.isDead)
            {
                int l1 = entity2.chunkCoordX;
                int i2 = entity2.chunkCoordZ;

                if (entity2.addedToChunk && this.isChunkLoaded(l1, i2, true))
                {
                    this.getChunkFromChunkCoords(l1, i2).removeEntity(entity2);
                }

                this.loadedEntityList.remove(this.tickPosition--); // Akarin
                this.onEntityRemoved(entity2);
            }

            this.profiler.endSection();
        }

        this.profiler.endStartSection("blockEntities");

        this.processingLoadedTiles = true; //FML Move above remove to prevent CMEs

        if (!this.tileEntitiesToBeRemoved.isEmpty())
        {
            for (Object tile : tileEntitiesToBeRemoved)
            {
               ((TileEntity)tile).onChunkUnload();
            }

            // forge: faster "contains" makes this removal much more efficient
            java.util.Set<TileEntity> remove = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
            remove.addAll(tileEntitiesToBeRemoved);
            this.tickableTileEntities.removeAll(remove);
            this.loadedTileEntityList.removeAll(remove);
            this.tileEntitiesToBeRemoved.clear();
        }

        Iterator<TileEntity> iterator = this.tickableTileEntities.iterator();

        while (iterator.hasNext())
        {
            TileEntity tileentity = iterator.next();

            if (!tileentity.isInvalid() && tileentity.hasWorld())
            {
                BlockPos blockpos = tileentity.getPos();

                if (this.isBlockLoaded(blockpos, false) && this.worldBorder.contains(blockpos)) //Forge: Fix TE's getting an extra tick on the client side....
                {
                    try
                    {
                        this.profiler.func_194340_a(() ->
                        {
                            return String.valueOf((Object)TileEntity.getKey(tileentity.getClass()));
                        });
                        net.minecraftforge.server.timings.TimeTracker.TILE_ENTITY_UPDATE.trackStart(tileentity);
                        ((ITickable)tileentity).update();
                        net.minecraftforge.server.timings.TimeTracker.TILE_ENTITY_UPDATE.trackEnd(tileentity);
                        this.profiler.endSection();
                    }
                    catch (Throwable throwable)
                    {
                        CrashReport crashreport2 = CrashReport.makeCrashReport(throwable, "Ticking block entity");
                        CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Block entity being ticked");
                        tileentity.addInfoToCrashReport(crashreportcategory2);
                        if (net.minecraftforge.common.ForgeModContainer.removeErroringTileEntities)
                        {
                            net.minecraftforge.fml.common.FMLLog.log.fatal("{}", crashreport2.getCompleteReport());
                            tileentity.invalidate();
                            this.removeTileEntity(tileentity.getPos());
                        }
                        else
                        throw new ReportedException(crashreport2);
                    }
                }
            }

            if (tileentity.isInvalid())
            {
                iterator.remove();
                this.loadedTileEntityList.remove(tileentity);

                if (this.isBlockLoaded(tileentity.getPos()))
                {
                    //Forge: Bugfix: If we set the tile entity it immediately sets it in the chunk, so we could be desyned
                    Chunk chunk = this.getChunkFromBlockCoords(tileentity.getPos());
                    if (chunk.getTileEntity(tileentity.getPos(), net.minecraft.world.chunk.Chunk.EnumCreateEntityType.CHECK) == tileentity)
                        chunk.removeTileEntity(tileentity.getPos());
                }
            }
        }

        this.processingLoadedTiles = false;
        this.profiler.endStartSection("pendingBlockEntities");

        if (!this.addedTileEntityList.isEmpty())
        {
            for (int j1 = 0; j1 < this.addedTileEntityList.size(); ++j1)
            {
                TileEntity tileentity1 = this.addedTileEntityList.get(j1);

                if (!tileentity1.isInvalid())
                {
                    // Akarin start
                    /*
                    if (!this.loadedTileEntityList.contains(tileentity1))
                    {
                        this.addTileEntity(tileentity1);
                    }
                    */
                    // Akarin end

                    if (this.isBlockLoaded(tileentity1.getPos()))
                    {
                        Chunk chunk = this.getChunkFromBlockCoords(tileentity1.getPos());
                        IBlockState iblockstate = chunk.getBlockState(tileentity1.getPos());
                        if (!this.loadedTileEntityList.contains(tileentity1)) // Akarin
                        chunk.addTileEntity(tileentity1.getPos(), tileentity1);
                        this.notifyBlockUpdate(tileentity1.getPos(), iblockstate, iblockstate, 3);
                    }
                }
            }

            this.addedTileEntityList.clear();
        }

        this.profiler.endSection();
        this.profiler.endSection();
    }

    protected void tickPlayers()
    {
    }

    public boolean addTileEntity(TileEntity tile)
    {
        // Forge - set the world early as vanilla doesn't set it until next tick
        if (tile.getWorld() != this) tile.setWorld(this);
        // Forge: wait to add new TE if we're currently processing existing ones
        if (processingLoadedTiles) return addedTileEntityList.add(tile);

        boolean flag = this.loadedTileEntityList.add(tile);

        if (flag && tile instanceof ITickable)
        {
            this.tickableTileEntities.add(tile);
        }
        tile.onLoad();

        if (this.isRemote)
        {
            BlockPos blockpos1 = tile.getPos();
            IBlockState iblockstate1 = this.getBlockState(blockpos1);
            this.notifyBlockUpdate(blockpos1, iblockstate1, iblockstate1, 2);
        }

        return flag;
    }

    public void addTileEntities(Collection<TileEntity> tileEntityCollection)
    {
        if (this.processingLoadedTiles)
        {
            for (TileEntity te : tileEntityCollection)
            {
                if (te.getWorld() != this) // Forge - set the world early as vanilla doesn't set it until next tick
                    te.setWorld(this);
            }
            this.addedTileEntityList.addAll(tileEntityCollection);
        }
        else
        {
            for (TileEntity tileentity2 : tileEntityCollection)
            {
                this.addTileEntity(tileentity2);
            }
        }
    }

    public void updateEntity(Entity ent)
    {
        this.updateEntityWithOptionalForce(ent, true);
    }

    public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate)
    {
        if (!(entityIn instanceof EntityPlayer))
        {
            int j2 = MathHelper.floor(entityIn.posX);
            int k2 = MathHelper.floor(entityIn.posZ);

            boolean isForced = getPersistentChunks().containsKey(new net.minecraft.util.math.ChunkPos(j2 >> 4, k2 >> 4));
            int range = isForced ? 0 : 32;
            boolean canUpdate = !forceUpdate || this.isAreaLoaded(j2 - range, 0, k2 - range, j2 + range, 0, k2 + range, true);
            if (!canUpdate) canUpdate = net.minecraftforge.event.ForgeEventFactory.canEntityUpdate(entityIn);

            if (!canUpdate)
            {
                return;
            }
        }
        // Akarin start
        if (forceUpdate && !ActivationRange.checkIfActive(entityIn)) {
            ++entityIn.ticksExisted;
            entityIn.inactiveTick();
            return;
        }
        // Akarin end

        entityIn.lastTickPosX = entityIn.posX;
        entityIn.lastTickPosY = entityIn.posY;
        entityIn.lastTickPosZ = entityIn.posZ;
        entityIn.prevRotationYaw = entityIn.rotationYaw;
        entityIn.prevRotationPitch = entityIn.rotationPitch;

        if (forceUpdate && entityIn.addedToChunk)
        {
            ++entityIn.ticksExisted;

            if (entityIn.isRiding())
            {
                entityIn.updateRidden();
            }
            else
            {
                if(!entityIn.updateBlocked)
                entityIn.onUpdate();
                entityIn.postTick();
            }
        }

        this.profiler.startSection("chunkCheck");

        if (Double.isNaN(entityIn.posX) || Double.isInfinite(entityIn.posX))
        {
            entityIn.posX = entityIn.lastTickPosX;
        }

        if (Double.isNaN(entityIn.posY) || Double.isInfinite(entityIn.posY))
        {
            entityIn.posY = entityIn.lastTickPosY;
        }

        if (Double.isNaN(entityIn.posZ) || Double.isInfinite(entityIn.posZ))
        {
            entityIn.posZ = entityIn.lastTickPosZ;
        }

        if (Double.isNaN((double)entityIn.rotationPitch) || Double.isInfinite((double)entityIn.rotationPitch))
        {
            entityIn.rotationPitch = entityIn.prevRotationPitch;
        }

        if (Double.isNaN((double)entityIn.rotationYaw) || Double.isInfinite((double)entityIn.rotationYaw))
        {
            entityIn.rotationYaw = entityIn.prevRotationYaw;
        }

        int i3 = MathHelper.floor(entityIn.posX / 16.0D);
        int j3 = MathHelper.floor(entityIn.posY / 16.0D);
        int k3 = MathHelper.floor(entityIn.posZ / 16.0D);

        if (!entityIn.addedToChunk || entityIn.chunkCoordX != i3 || entityIn.chunkCoordY != j3 || entityIn.chunkCoordZ != k3)
        {
            if (entityIn.addedToChunk && this.isChunkLoaded(entityIn.chunkCoordX, entityIn.chunkCoordZ, true))
            {
                this.getChunkFromChunkCoords(entityIn.chunkCoordX, entityIn.chunkCoordZ).removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
            }

            if (!entityIn.setPositionNonDirty() && !this.isChunkLoaded(i3, k3, true))
            {
                entityIn.addedToChunk = false;
            }
            else
            {
                this.getChunkFromChunkCoords(i3, k3).addEntity(entityIn);
            }
        }

        this.profiler.endSection();

        if (forceUpdate && entityIn.addedToChunk)
        {
            for (Entity entity4 : entityIn.getPassengers())
            {
                if (!entity4.isDead && entity4.getRidingEntity() == entityIn)
                {
                    this.updateEntity(entity4);
                }
                else
                {
                    entity4.dismountRidingEntity();
                }
            }
        }
    }

    public boolean checkNoEntityCollision(AxisAlignedBB bb)
    {
        return this.checkNoEntityCollision(bb, (Entity)null);
    }

    public boolean checkNoEntityCollision(AxisAlignedBB bb, @Nullable Entity entityIn)
    {
        List<Entity> list = this.getEntitiesWithinAABBExcludingEntity((Entity)null, bb);

        for (int j2 = 0; j2 < list.size(); ++j2)
        {
            Entity entity4 = list.get(j2);

            if (!entity4.isDead && entity4.preventEntitySpawning && entity4 != entityIn && (entityIn == null || !entity4.isRidingSameEntity(entityIn))) // Forge: fix MC-103516
            {
                return false;
            }
        }

        return true;
    }

    public boolean checkBlockCollision(AxisAlignedBB bb)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    IBlockState iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));

                    if (iblockstate1.getMaterial() != Material.AIR)
                    {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return false;
    }

    public boolean containsAnyLiquid(AxisAlignedBB bb)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    IBlockState iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));

                    Boolean result = iblockstate1.getBlock().isAABBInsideLiquid(this, blockpos$pooledmutableblockpos, bb);
                    if (result != null) {
                        if (!result) continue;
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                    if (iblockstate1.getMaterial().isLiquid())
                    {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return false;
    }

    public boolean isFlammableWithin(AxisAlignedBB bb)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);

        if (this.isAreaLoaded(j2, l2, j3, k2, i3, k3, true))
        {
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

            for (int l3 = j2; l3 < k2; ++l3)
            {
                for (int i4 = l2; i4 < i3; ++i4)
                {
                    for (int j4 = j3; j4 < k3; ++j4)
                    {
                        Block block = this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4)).getBlock();

                        if (block == Blocks.FIRE || block == Blocks.FLOWING_LAVA || block == Blocks.LAVA)
                        {
                            blockpos$pooledmutableblockpos.release();
                            return true;
                        }
                        else if (block.isBurning(this, new BlockPos(l3, i4, j4)))
                        {
                            blockpos$pooledmutableblockpos.release();
                            return true;
                        }
                    }
                }
            }

            blockpos$pooledmutableblockpos.release();
        }

        return false;
    }

    public boolean handleMaterialAcceleration(AxisAlignedBB bb, Material materialIn, Entity entityIn)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);

        if (!this.isAreaLoaded(j2, l2, j3, k2, i3, k3, true))
        {
            return false;
        }
        else
        {
            boolean flag = false;
            Vec3d vec3d = Vec3d.ZERO;
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

            for (int l3 = j2; l3 < k2; ++l3)
            {
                for (int i4 = l2; i4 < i3; ++i4)
                {
                    for (int j4 = j3; j4 < k3; ++j4)
                    {
                        blockpos$pooledmutableblockpos.setPos(l3, i4, j4);
                        IBlockState iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos);
                        Block block = iblockstate1.getBlock();

                        Boolean result = block.isEntityInsideMaterial(this, blockpos$pooledmutableblockpos, iblockstate1, entityIn, (double)i3, materialIn, false);
                        if (result != null && result == true)
                        {
                            // Forge: When requested call blocks modifyAcceleration method, and more importantly cause this method to return true, which results in an entity being "inWater"
                            flag = true;
                            vec3d = block.modifyAcceleration(this, blockpos$pooledmutableblockpos, entityIn, vec3d);
                            continue;
                        }
                        else if (result != null && result == false) continue;

                        if (iblockstate1.getMaterial() == materialIn)
                        {
                            double d0 = (double)((float)(i4 + 1) - BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate1.getValue(BlockLiquid.LEVEL)).intValue()));

                            if ((double)i3 >= d0)
                            {
                                flag = true;
                                vec3d = block.modifyAcceleration(this, blockpos$pooledmutableblockpos, entityIn, vec3d);
                            }
                        }
                    }
                }
            }

            blockpos$pooledmutableblockpos.release();

            if (vec3d.lengthVector() > 0.0D && entityIn.isPushedByWater())
            {
                vec3d = vec3d.normalize();
                double d1 = 0.014D;
                entityIn.motionX += vec3d.x * 0.014D;
                entityIn.motionY += vec3d.y * 0.014D;
                entityIn.motionZ += vec3d.z * 0.014D;
            }

            return flag;
        }
    }

    public boolean isMaterialInBB(AxisAlignedBB bb, Material materialIn)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    IBlockState iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));
                    Boolean result = iblockstate1.getBlock().isAABBInsideMaterial(this, blockpos$pooledmutableblockpos, bb, materialIn);
                    if (result != null) {
                        if (!result) continue;
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                    if (iblockstate1.getMaterial() == materialIn)
                    {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return false;
    }

    public Explosion createExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isSmoking)
    {
        return this.newExplosion(entityIn, x, y, z, strength, false, isSmoking);
    }

    public Explosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking)
    {
        Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this, explosion)) return explosion;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

    public float getBlockDensity(Vec3d vec, AxisAlignedBB bb)
    {
        double d0 = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;

        if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D)
        {
            int j2 = 0;
            int k2 = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0))
            {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1))
                {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2))
                    {
                        double d5 = bb.minX + (bb.maxX - bb.minX) * (double)f;
                        double d6 = bb.minY + (bb.maxY - bb.minY) * (double)f1;
                        double d7 = bb.minZ + (bb.maxZ - bb.minZ) * (double)f2;

                        if (this.rayTraceBlocks(new Vec3d(d5 + d3, d6, d7 + d4), vec) == null)
                        {
                            ++j2;
                        }

                        ++k2;
                    }
                }
            }

            return (float)j2 / (float)k2;
        }
        else
        {
            return 0.0F;
        }
    }

    public boolean extinguishFire(@Nullable EntityPlayer player, BlockPos pos, EnumFacing side)
    {
        pos = pos.offset(side);

        if (this.getBlockState(pos).getBlock() == Blocks.FIRE)
        {
            this.playEvent(player, 1009, pos, 0);
            this.setBlockToAir(pos);
            return true;
        }
        else
        {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    public String getDebugLoadedEntities()
    {
        return "All: " + this.loadedEntityList.size();
    }

    @SideOnly(Side.CLIENT)
    public String getProviderName()
    {
        return this.chunkProvider.makeString();
    }

    @Nullable
    public TileEntity getTileEntity(BlockPos pos)
    {
        if (this.isOutsideBuildHeight(pos))
        {
            return null;
        }
        else
        {
            TileEntity tileentity2 = null;

            if (this.processingLoadedTiles)
            {
                tileentity2 = this.getPendingTileEntityAt(pos);
            }

            if (tileentity2 == null)
            {
                tileentity2 = this.getChunkFromBlockCoords(pos).getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
            }

            if (tileentity2 == null)
            {
                tileentity2 = this.getPendingTileEntityAt(pos);
            }

            return tileentity2;
        }
    }

    @Nullable
    private TileEntity getPendingTileEntityAt(BlockPos pos)
    {
        for (int j2 = 0; j2 < this.addedTileEntityList.size(); ++j2)
        {
            TileEntity tileentity2 = this.addedTileEntityList.get(j2);

            if (!tileentity2.isInvalid() && tileentity2.getPos().equals(pos))
            {
                return tileentity2;
            }
        }

        return null;
    }

    public void setTileEntity(BlockPos pos, @Nullable TileEntity tileEntityIn)
    {
        pos = pos.toImmutable(); // Forge - prevent mutable BlockPos leaks
        if (!this.isOutsideBuildHeight(pos))
        {
            if (tileEntityIn != null && !tileEntityIn.isInvalid())
            {
                if (this.processingLoadedTiles)
                {
                    tileEntityIn.setPos(pos);
                    if (tileEntityIn.getWorld() != this)
                        tileEntityIn.setWorld(this); // Forge - set the world early as vanilla doesn't set it until next tick
                    Iterator<TileEntity> iterator1 = this.addedTileEntityList.iterator();

                    while (iterator1.hasNext())
                    {
                        TileEntity tileentity2 = iterator1.next();

                        if (tileentity2.getPos().equals(pos))
                        {
                            tileentity2.invalidate();
                            iterator1.remove();
                        }
                    }

                    this.addedTileEntityList.add(tileEntityIn);
                }
                else
                {
                    Chunk chunk = this.getChunkFromBlockCoords(pos);
                    if (chunk != null) chunk.addTileEntity(pos, tileEntityIn);
                    this.addTileEntity(tileEntityIn);
                }
            }
        }
    }

    public void removeTileEntity(BlockPos pos)
    {
        TileEntity tileentity2 = this.getTileEntity(pos);

        if (tileentity2 != null && this.processingLoadedTiles)
        {
            tileentity2.invalidate();
            this.addedTileEntityList.remove(tileentity2);
            if (!(tileentity2 instanceof ITickable)) //Forge: If they are not tickable they wont be removed in the update loop.
                this.loadedTileEntityList.remove(tileentity2);
        }
        else
        {
            if (tileentity2 != null)
            {
                this.addedTileEntityList.remove(tileentity2);
                this.loadedTileEntityList.remove(tileentity2);
                this.tickableTileEntities.remove(tileentity2);
            }

            this.getChunkFromBlockCoords(pos).removeTileEntity(pos);
        }
        this.updateComparatorOutputLevel(pos, getBlockState(pos).getBlock()); //Notify neighbors of changes
    }

    public void markTileEntityForRemoval(TileEntity tileEntityIn)
    {
        this.tileEntitiesToBeRemoved.add(tileEntityIn);
    }

    public boolean isBlockFullCube(BlockPos pos)
    {
        AxisAlignedBB axisalignedbb = this.getBlockState(pos).getCollisionBoundingBox(this, pos);
        return axisalignedbb != Block.NULL_AABB && axisalignedbb.getAverageEdgeLength() >= 1.0D;
    }

    public boolean isBlockNormalCube(BlockPos pos, boolean _default)
    {
        if (this.isOutsideBuildHeight(pos))
        {
            return false;
        }
        else
        {
            Chunk chunk1 = this.chunkProvider.getLoadedChunk(pos.getX() >> 4, pos.getZ() >> 4);

            if (chunk1 != null && !chunk1.isEmpty())
            {
                IBlockState iblockstate1 = this.getBlockState(pos);
                return iblockstate1.getBlock().isNormalCube(iblockstate1, this, pos);
            }
            else
            {
                return _default;
            }
        }
    }

    public void calculateInitialSkylight()
    {
        int j2 = this.calculateSkylightSubtracted(1.0F);

        if (j2 != this.skylightSubtracted)
        {
            this.skylightSubtracted = j2;
        }
    }

    public void setAllowedSpawnTypes(boolean hostile, boolean peaceful)
    {
        this.spawnHostileMobs = hostile;
        this.spawnPeacefulMobs = peaceful;
        this.provider.setAllowedSpawnTypes(hostile, peaceful);
    }

    public void tick()
    {
        this.updateWeather();
    }

    protected void calculateInitialWeather()
    {
        this.provider.calculateInitialWeather();
    }

    public void calculateInitialWeatherBody()
    {
        if (this.worldInfo.isRaining())
        {
            this.rainingStrength = 1.0F;

            if (this.worldInfo.isThundering())
            {
                this.thunderingStrength = 1.0F;
            }
        }
    }

    protected void updateWeather()
    {
        this.provider.updateWeather();
    }

    public void updateWeatherBody()
    {
        if (this.provider.hasSkyLight())
        {
            if (!this.isRemote)
            {
                boolean flag = this.getGameRules().getBoolean("doWeatherCycle");

                if (flag)
                {
                    int j2 = this.worldInfo.getCleanWeatherTime();

                    if (j2 > 0)
                    {
                        --j2;
                        this.worldInfo.setCleanWeatherTime(j2);
                        this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
                        this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
                    }

                    int k2 = this.worldInfo.getThunderTime();

                    if (k2 <= 0)
                    {
                        if (this.worldInfo.isThundering())
                        {
                            this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
                        }
                        else
                        {
                            this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
                        }
                    }
                    else
                    {
                        --k2;
                        this.worldInfo.setThunderTime(k2);

                        if (k2 <= 0)
                        {
                            this.worldInfo.setThundering(!this.worldInfo.isThundering());
                        }
                    }

                    int l2 = this.worldInfo.getRainTime();

                    if (l2 <= 0)
                    {
                        if (this.worldInfo.isRaining())
                        {
                            this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
                        }
                        else
                        {
                            this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
                        }
                    }
                    else
                    {
                        --l2;
                        this.worldInfo.setRainTime(l2);

                        if (l2 <= 0)
                        {
                            this.worldInfo.setRaining(!this.worldInfo.isRaining());
                        }
                    }
                }

                this.prevThunderingStrength = this.thunderingStrength;

                if (this.worldInfo.isThundering())
                {
                    this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01D);
                }
                else
                {
                    this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01D);
                }

                this.thunderingStrength = MathHelper.clamp(this.thunderingStrength, 0.0F, 1.0F);
                this.prevRainingStrength = this.rainingStrength;

                if (this.worldInfo.isRaining())
                {
                    this.rainingStrength = (float)((double)this.rainingStrength + 0.01D);
                }
                else
                {
                    this.rainingStrength = (float)((double)this.rainingStrength - 0.01D);
                }

                this.rainingStrength = MathHelper.clamp(this.rainingStrength, 0.0F, 1.0F);
                // Akarin start
                for (int idx = 0; idx < this.playerEntities.size(); ++idx) {
                    if (((EntityPlayerMP) this.playerEntities.get(idx)).world == this) {
                        ((EntityPlayerMP) this.playerEntities.get(idx)).tickWeather();
                    }
                }
                // Akarin end
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void playMoodSoundAndCheckLight(int p_147467_1_, int p_147467_2_, Chunk chunkIn)
    {
        chunkIn.enqueueRelightChecks();
    }

    protected void updateBlocks()
    {
    }

    public void immediateBlockTick(BlockPos pos, IBlockState state, Random random)
    {
        this.scheduledUpdatesAreImmediate = true;
        state.getBlock().updateTick(this, pos, state, random);
        this.scheduledUpdatesAreImmediate = false;
    }

    public boolean canBlockFreezeWater(BlockPos pos)
    {
        return this.canBlockFreeze(pos, false);
    }

    public boolean canBlockFreezeNoWater(BlockPos pos)
    {
        return this.canBlockFreeze(pos, true);
    }

    public boolean canBlockFreeze(BlockPos pos, boolean noWaterAdj)
    {
        return this.provider.canBlockFreeze(pos, noWaterAdj);
    }

    public boolean canBlockFreezeBody(BlockPos pos, boolean noWaterAdj)
    {
        Biome biome = this.getBiome(pos);
        float f = biome.getTemperature(pos);

        if (f >= 0.15F)
        {
            return false;
        }
        else
        {
            if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
            {
                IBlockState iblockstate1 = this.getBlockState(pos);
                Block block = iblockstate1.getBlock();

                if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && ((Integer)iblockstate1.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                {
                    if (!noWaterAdj)
                    {
                        return true;
                    }

                    boolean flag = this.isWater(pos.west()) && this.isWater(pos.east()) && this.isWater(pos.north()) && this.isWater(pos.south());

                    if (!flag)
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private boolean isWater(BlockPos pos)
    {
        return this.getBlockState(pos).getMaterial() == Material.WATER;
    }

    public boolean canSnowAt(BlockPos pos, boolean checkLight)
    {
        return this.provider.canSnowAt(pos, checkLight);
    }

    public boolean canSnowAtBody(BlockPos pos, boolean checkLight)
    {
        Biome biome = this.getBiome(pos);
        float f = biome.getTemperature(pos);

        if (f >= 0.15F)
        {
            return false;
        }
        else if (!checkLight)
        {
            return true;
        }
        else
        {
            if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
            {
                IBlockState iblockstate1 = this.getBlockState(pos);

                if (iblockstate1.getBlock().isAir(iblockstate1, this, pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(this, pos))
                {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean checkLight(BlockPos pos)
    {
        boolean flag = false;

        if (this.provider.hasSkyLight())
        {
            flag |= this.checkLightFor(EnumSkyBlock.SKY, pos);
        }

        flag = flag | this.checkLightFor(EnumSkyBlock.BLOCK, pos);
        return flag;
    }

    private int getRawLight(BlockPos pos, EnumSkyBlock lightType)
    {
        if (lightType == EnumSkyBlock.SKY && this.canSeeSky(pos))
        {
            return 15;
        }
        else
        {
            IBlockState iblockstate1 = this.getBlockState(pos);
            int j2 = lightType == EnumSkyBlock.SKY ? 0 : iblockstate1.getBlock().getLightValue(iblockstate1, this, pos);
            int k2 = iblockstate1.getBlock().getLightOpacity(iblockstate1, this, pos);

            if (false) // Forge: fix MC-119932
            {
                k2 = 1;
            }

            if (k2 < 1)
            {
                k2 = 1;
            }

            if (k2 >= 15)
            {
                return j2; // Forge: fix MC-119932
            }
            else if (j2 >= 14)
            {
                return j2;
            }
            else
            {
                BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

                try
                {
                    for (EnumFacing enumfacing : EnumFacing.values())
                    {
                        blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing);
                        int l2 = this.getLightFor(lightType, blockpos$pooledmutableblockpos) - k2;

                        if (l2 > j2)
                        {
                            j2 = l2;
                        }

                        if (j2 >= 14)
                        {
                            int i3 = j2;
                            return i3;
                        }
                    }

                    return j2;
                }
                finally
                {
                    blockpos$pooledmutableblockpos.release();
                }
            }
        }
    }

    public boolean checkLightFor(EnumSkyBlock lightType, BlockPos pos)
    {
        // Akarin start
        Chunk chunk = this.getChunkIfLoaded(pos.getX() >> 4, pos.getZ() >> 4);
        if (chunk == null || !chunk.areNeighborsLoaded(1)) {
            return false;
        }
        // Akarin end
        else
        {
            int updateRange = this.isAreaLoaded(pos, 18, false) ? 17 : 15;
            int j2 = 0;
            int k2 = 0;
            this.profiler.startSection("getBrightness");
            int l2 = this.getLightFor(lightType, pos);
            int i3 = this.getRawLight(pos, lightType);
            int j3 = pos.getX();
            int k3 = pos.getY();
            int l3 = pos.getZ();

            if (i3 > l2)
            {
                this.lightUpdateBlockList[k2++] = 133152;
            }
            else if (i3 < l2)
            {
                this.lightUpdateBlockList[k2++] = 133152 | l2 << 18;

                while (j2 < k2)
                {
                    int i4 = this.lightUpdateBlockList[j2++];
                    int j4 = (i4 & 63) - 32 + j3;
                    int k4 = (i4 >> 6 & 63) - 32 + k3;
                    int l4 = (i4 >> 12 & 63) - 32 + l3;
                    int i5 = i4 >> 18 & 15;
                    BlockPos blockpos1 = new BlockPos(j4, k4, l4);
                    int j5 = this.getLightFor(lightType, blockpos1);

                    if (j5 == i5)
                    {
                        this.setLightFor(lightType, blockpos1, 0);

                        if (i5 > 0)
                        {
                            int k5 = MathHelper.abs(j4 - j3);
                            int l5 = MathHelper.abs(k4 - k3);
                            int i6 = MathHelper.abs(l4 - l3);

                            if (k5 + l5 + i6 < updateRange)
                            {
                                BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

                                for (EnumFacing enumfacing : EnumFacing.values())
                                {
                                    int j6 = j4 + enumfacing.getFrontOffsetX();
                                    int k6 = k4 + enumfacing.getFrontOffsetY();
                                    int l6 = l4 + enumfacing.getFrontOffsetZ();
                                    blockpos$pooledmutableblockpos.setPos(j6, k6, l6);
                                    IBlockState bs = this.getBlockState(blockpos$pooledmutableblockpos);
                                    int i7 = Math.max(1, bs.getBlock().getLightOpacity(bs, this, blockpos$pooledmutableblockpos));
                                    j5 = this.getLightFor(lightType, blockpos$pooledmutableblockpos);

                                    if (j5 == i5 - i7 && k2 < this.lightUpdateBlockList.length)
                                    {
                                        this.lightUpdateBlockList[k2++] = j6 - j3 + 32 | k6 - k3 + 32 << 6 | l6 - l3 + 32 << 12 | i5 - i7 << 18;
                                    }
                                }

                                blockpos$pooledmutableblockpos.release();
                            }
                        }
                    }
                }

                j2 = 0;
            }

            this.profiler.endSection();
            this.profiler.startSection("checkedPosition < toCheckCount");

            while (j2 < k2)
            {
                int j7 = this.lightUpdateBlockList[j2++];
                int k7 = (j7 & 63) - 32 + j3;
                int l7 = (j7 >> 6 & 63) - 32 + k3;
                int i8 = (j7 >> 12 & 63) - 32 + l3;
                BlockPos blockpos2 = new BlockPos(k7, l7, i8);
                int j8 = this.getLightFor(lightType, blockpos2);
                int k8 = this.getRawLight(blockpos2, lightType);

                if (k8 != j8)
                {
                    this.setLightFor(lightType, blockpos2, k8);

                    if (k8 > j8)
                    {
                        int l8 = Math.abs(k7 - j3);
                        int i9 = Math.abs(l7 - k3);
                        int j9 = Math.abs(i8 - l3);
                        boolean flag = k2 < this.lightUpdateBlockList.length - 6;

                        if (l8 + i9 + j9 < updateRange && flag)
                        {
                            if (this.getLightFor(lightType, blockpos2.west()) < k8)
                            {
                                this.lightUpdateBlockList[k2++] = k7 - 1 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (this.getLightFor(lightType, blockpos2.east()) < k8)
                            {
                                this.lightUpdateBlockList[k2++] = k7 + 1 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (this.getLightFor(lightType, blockpos2.down()) < k8)
                            {
                                this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - 1 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (this.getLightFor(lightType, blockpos2.up()) < k8)
                            {
                                this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 + 1 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (this.getLightFor(lightType, blockpos2.north()) < k8)
                            {
                                this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - 1 - l3 + 32 << 12);
                            }

                            if (this.getLightFor(lightType, blockpos2.south()) < k8)
                            {
                                this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 + 1 - l3 + 32 << 12);
                            }
                        }
                    }
                }
            }

            this.profiler.endSection();
            return true;
        }
    }

    public boolean tickUpdates(boolean runAllPending)
    {
        return false;
    }

    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunkIn, boolean remove)
    {
        return null;
    }

    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(StructureBoundingBox structureBB, boolean remove)
    {
        return null;
    }

    public List<Entity> getEntitiesWithinAABBExcludingEntity(@Nullable Entity entityIn, AxisAlignedBB bb)
    {
        return this.getEntitiesInAABBexcluding(entityIn, bb, EntitySelectors.NOT_SPECTATING);
    }

    public List<Entity> getEntitiesInAABBexcluding(@Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate <? super Entity > predicate)
    {
        List<Entity> list = Lists.<Entity>newArrayList();
        int j2 = MathHelper.floor((boundingBox.minX - MAX_ENTITY_RADIUS) / 16.0D);
        int k2 = MathHelper.floor((boundingBox.maxX + MAX_ENTITY_RADIUS) / 16.0D);
        int l2 = MathHelper.floor((boundingBox.minZ - MAX_ENTITY_RADIUS) / 16.0D);
        int i3 = MathHelper.floor((boundingBox.maxZ + MAX_ENTITY_RADIUS) / 16.0D);

        for (int j3 = j2; j3 <= k2; ++j3)
        {
            for (int k3 = l2; k3 <= i3; ++k3)
            {
                if (this.isChunkLoaded(j3, k3, true))
                {
                    this.getChunkFromChunkCoords(j3, k3).getEntitiesWithinAABBForEntity(entityIn, boundingBox, list, predicate);
                }
            }
        }

        return list;
    }

    public <T extends Entity> List<T> getEntities(Class <? extends T > entityType, Predicate <? super T > filter)
    {
        List<T> list = Lists.<T>newArrayList();

        for (Entity entity4 : this.loadedEntityList)
        {
            if (entityType.isAssignableFrom(entity4.getClass()) && filter.apply((T)entity4))
            {
                list.add((T)entity4);
            }
        }

        return list;
    }

    public <T extends Entity> List<T> getPlayers(Class <? extends T > playerType, Predicate <? super T > filter)
    {
        List<T> list = Lists.<T>newArrayList();

        for (Entity entity4 : this.playerEntities)
        {
            if (playerType.isAssignableFrom(entity4.getClass()) && filter.apply((T)entity4))
            {
                list.add((T)entity4);
            }
        }

        return list;
    }

    public <T extends Entity> List<T> getEntitiesWithinAABB(Class <? extends T > classEntity, AxisAlignedBB bb)
    {
        return this.<T>getEntitiesWithinAABB(classEntity, bb, EntitySelectors.NOT_SPECTATING);
    }

    public <T extends Entity> List<T> getEntitiesWithinAABB(Class <? extends T > clazz, AxisAlignedBB aabb, @Nullable Predicate <? super T > filter)
    {
        int j2 = MathHelper.floor((aabb.minX - MAX_ENTITY_RADIUS) / 16.0D);
        int k2 = MathHelper.ceil((aabb.maxX + MAX_ENTITY_RADIUS) / 16.0D);
        int l2 = MathHelper.floor((aabb.minZ - MAX_ENTITY_RADIUS) / 16.0D);
        int i3 = MathHelper.ceil((aabb.maxZ + MAX_ENTITY_RADIUS) / 16.0D);
        List<T> list = Lists.<T>newArrayList();

        for (int j3 = j2; j3 < k2; ++j3)
        {
            for (int k3 = l2; k3 < i3; ++k3)
            {
                if (this.isChunkLoaded(j3, k3, true))
                {
                    this.getChunkFromChunkCoords(j3, k3).getEntitiesOfTypeWithinAABB(clazz, aabb, list, filter);
                }
            }
        }

        return list;
    }

    @Nullable
    public <T extends Entity> T findNearestEntityWithinAABB(Class <? extends T > entityType, AxisAlignedBB aabb, T closestTo)
    {
        List<T> list = this.<T>getEntitiesWithinAABB(entityType, aabb);
        T t = null;
        double d0 = Double.MAX_VALUE;

        for (int j2 = 0; j2 < list.size(); ++j2)
        {
            T t1 = list.get(j2);

            if (t1 != closestTo && EntitySelectors.NOT_SPECTATING.apply(t1))
            {
                double d1 = closestTo.getDistanceSq(t1);

                if (d1 <= d0)
                {
                    t = t1;
                    d0 = d1;
                }
            }
        }

        return t;
    }

    @Nullable
    public Entity getEntityByID(int id)
    {
        return this.entitiesById.lookup(id);
    }

    @SideOnly(Side.CLIENT)
    public List<Entity> getLoadedEntityList()
    {
        return this.loadedEntityList;
    }

    public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity)
    {
        if (this.isBlockLoaded(pos))
        {
            this.getChunkFromBlockCoords(pos).markDirty();
        }
    }

    public int countEntities(Class<?> entityType)
    {
        int j2 = 0;

        for (Entity entity4 : this.loadedEntityList)
        {
            if ((!(entity4 instanceof EntityLiving) || !((EntityLiving)entity4).isNoDespawnRequired()) && entityType.isAssignableFrom(entity4.getClass()))
            {
                ++j2;
            }
        }

        return j2;
    }

    public void loadEntities(Collection<Entity> entityCollection)
    {
        for (Entity entity4 : entityCollection)
        {
            if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.EntityJoinWorldEvent(entity4, this)))
            {
                loadedEntityList.add(entity4);
                this.onEntityAdded(entity4);
            }
        }
    }

    public void unloadEntities(Collection<Entity> entityCollection)
    {
        this.unloadedEntityList.addAll(entityCollection);
    }

    public boolean mayPlace(Block blockIn, BlockPos pos, boolean skipCollisionCheck, EnumFacing sidePlacedOn, @Nullable Entity placer)
    {
        IBlockState iblockstate1 = this.getBlockState(pos);
        AxisAlignedBB axisalignedbb = skipCollisionCheck ? null : blockIn.getDefaultState().getCollisionBoundingBox(this, pos);

        if (!((placer instanceof EntityPlayer) || !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(placer, new net.minecraftforge.common.util.BlockSnapshot(this, pos, blockIn.getDefaultState()), sidePlacedOn).isCanceled())) return false;
        if (axisalignedbb != Block.NULL_AABB && !this.checkNoEntityCollision(axisalignedbb.offset(pos))) // Forge: Remove second parameter, we patch placer to be non-null, passing it here skips collision checks for the placer
        {
            // Akarin start
            BlockCanBuildEvent event = new BlockCanBuildEvent(this.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), CraftMagicNumbers.getId(blockIn), false);
            this.getServer().getPluginManager().callEvent(event);

            return event.isBuildable();
        }
        else if (iblockstate1.getMaterial() == Material.CIRCUITS && blockIn == Blocks.ANVIL)
        {
            BlockCanBuildEvent event = new BlockCanBuildEvent(this.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), CraftMagicNumbers.getId(blockIn), true);
            this.getServer().getPluginManager().callEvent(event);

            return event.isBuildable();
        }
        else
        {
            BlockCanBuildEvent event = new BlockCanBuildEvent(this.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), CraftMagicNumbers.getId(blockIn), iblockstate1.getBlock().isReplaceable(this, pos) && blockIn.canPlaceBlockOnSide(this, pos, sidePlacedOn));
            this.getServer().getPluginManager().callEvent(event);

            return event.isBuildable();
            // Akarin end
        }
    }

    public int getSeaLevel()
    {
        return this.seaLevel;
    }

    public void setSeaLevel(int seaLevelIn)
    {
        this.seaLevel = seaLevelIn;
    }

    public int getStrongPower(BlockPos pos, EnumFacing direction)
    {
        return this.getBlockState(pos).getStrongPower(this, pos, direction);
    }

    public WorldType getWorldType()
    {
        return this.worldInfo.getTerrainType();
    }

    public int getStrongPower(BlockPos pos)
    {
        int j2 = 0;
        j2 = Math.max(j2, this.getStrongPower(pos.down(), EnumFacing.DOWN));

        if (j2 >= 15)
        {
            return j2;
        }
        else
        {
            j2 = Math.max(j2, this.getStrongPower(pos.up(), EnumFacing.UP));

            if (j2 >= 15)
            {
                return j2;
            }
            else
            {
                j2 = Math.max(j2, this.getStrongPower(pos.north(), EnumFacing.NORTH));

                if (j2 >= 15)
                {
                    return j2;
                }
                else
                {
                    j2 = Math.max(j2, this.getStrongPower(pos.south(), EnumFacing.SOUTH));

                    if (j2 >= 15)
                    {
                        return j2;
                    }
                    else
                    {
                        j2 = Math.max(j2, this.getStrongPower(pos.west(), EnumFacing.WEST));

                        if (j2 >= 15)
                        {
                            return j2;
                        }
                        else
                        {
                            j2 = Math.max(j2, this.getStrongPower(pos.east(), EnumFacing.EAST));
                            return j2 >= 15 ? j2 : j2;
                        }
                    }
                }
            }
        }
    }

    public boolean isSidePowered(BlockPos pos, EnumFacing side)
    {
        return this.getRedstonePower(pos, side) > 0;
    }

    public int getRedstonePower(BlockPos pos, EnumFacing facing)
    {
        IBlockState iblockstate1 = this.getBlockState(pos);
        return iblockstate1.getBlock().shouldCheckWeakPower(iblockstate1, this, pos, facing) ? this.getStrongPower(pos) : iblockstate1.getWeakPower(this, pos, facing);
    }

    public boolean isBlockPowered(BlockPos pos)
    {
        if (this.getRedstonePower(pos.down(), EnumFacing.DOWN) > 0)
        {
            return true;
        }
        else if (this.getRedstonePower(pos.up(), EnumFacing.UP) > 0)
        {
            return true;
        }
        else if (this.getRedstonePower(pos.north(), EnumFacing.NORTH) > 0)
        {
            return true;
        }
        else if (this.getRedstonePower(pos.south(), EnumFacing.SOUTH) > 0)
        {
            return true;
        }
        else if (this.getRedstonePower(pos.west(), EnumFacing.WEST) > 0)
        {
            return true;
        }
        else
        {
            return this.getRedstonePower(pos.east(), EnumFacing.EAST) > 0;
        }
    }

    public int isBlockIndirectlyGettingPowered(BlockPos pos)
    {
        int j2 = 0;

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            int k2 = this.getRedstonePower(pos.offset(enumfacing), enumfacing);

            if (k2 >= 15)
            {
                return 15;
            }

            if (k2 > j2)
            {
                j2 = k2;
            }
        }

        return j2;
    }

    @Nullable
    public EntityPlayer getClosestPlayerToEntity(Entity entityIn, double distance)
    {
        return this.getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance, false);
    }

    @Nullable
    public EntityPlayer getNearestPlayerNotCreative(Entity entityIn, double distance)
    {
        return this.getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance, true);
    }

    @Nullable
    public EntityPlayer getClosestPlayer(double posX, double posY, double posZ, double distance, boolean spectator)
    {
        Predicate<Entity> predicate = spectator ? EntitySelectors.CAN_AI_TARGET : EntitySelectors.NOT_SPECTATING;
        return this.getClosestPlayer(posX, posY, posZ, distance, predicate);
    }

    @Nullable
    public EntityPlayer getClosestPlayer(double x, double y, double z, double p_190525_7_, Predicate<Entity> p_190525_9_)
    {
        double d0 = -1.0D;
        EntityPlayer entityplayer = null;

        for (int j2 = 0; j2 < this.playerEntities.size(); ++j2)
        {
            EntityPlayer entityplayer1 = this.playerEntities.get(j2);
            if (entityplayer1 == null || entityplayer1.isDead) continue; // Akarin

            if (p_190525_9_.apply(entityplayer1))
            {
                double d1 = entityplayer1.getDistanceSq(x, y, z);

                if ((p_190525_7_ < 0.0D || d1 < p_190525_7_ * p_190525_7_) && (d0 == -1.0D || d1 < d0))
                {
                    d0 = d1;
                    entityplayer = entityplayer1;
                }
            }
        }

        return entityplayer;
    }

    public boolean isAnyPlayerWithinRangeAt(double x, double y, double z, double range)
    {
        for (int j2 = 0; j2 < this.playerEntities.size(); ++j2)
        {
            EntityPlayer entityplayer = this.playerEntities.get(j2);

            if (EntitySelectors.NOT_SPECTATING.apply(entityplayer))
            {
                double d0 = entityplayer.getDistanceSq(x, y, z);

                if (range < 0.0D || d0 < range * range)
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    public EntityPlayer getNearestAttackablePlayer(Entity entityIn, double maxXZDistance, double maxYDistance)
    {
        return this.getNearestAttackablePlayer(entityIn.posX, entityIn.posY, entityIn.posZ, maxXZDistance, maxYDistance, (Function)null, (Predicate)null);
    }

    @Nullable
    public EntityPlayer getNearestAttackablePlayer(BlockPos pos, double maxXZDistance, double maxYDistance)
    {
        return this.getNearestAttackablePlayer((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), maxXZDistance, maxYDistance, (Function)null, (Predicate)null);
    }

    @Nullable
    public EntityPlayer getNearestAttackablePlayer(double posX, double posY, double posZ, double maxXZDistance, double maxYDistance, @Nullable Function<EntityPlayer, Double> playerToDouble, @Nullable Predicate<EntityPlayer> p_184150_12_)
    {
        double d0 = -1.0D;
        EntityPlayer entityplayer = null;

        for (int j2 = 0; j2 < this.playerEntities.size(); ++j2)
        {
            EntityPlayer entityplayer1 = this.playerEntities.get(j2);

            if (!entityplayer1.capabilities.disableDamage && entityplayer1.isEntityAlive() && !entityplayer1.isSpectator() && (p_184150_12_ == null || p_184150_12_.apply(entityplayer1)))
            {
                double d1 = entityplayer1.getDistanceSq(posX, entityplayer1.posY, posZ);
                double d2 = maxXZDistance;

                if (entityplayer1.isSneaking())
                {
                    d2 = maxXZDistance * 0.800000011920929D;
                }

                if (entityplayer1.isInvisible())
                {
                    float f = entityplayer1.getArmorVisibility();

                    if (f < 0.1F)
                    {
                        f = 0.1F;
                    }

                    d2 *= (double)(0.7F * f);
                }

                if (playerToDouble != null)
                {
                    d2 *= ((Double)MoreObjects.firstNonNull(playerToDouble.apply(entityplayer1), Double.valueOf(1.0D))).doubleValue();
                }

                d2 = net.minecraftforge.common.ForgeHooks.getPlayerVisibilityDistance(entityplayer1, d2, maxYDistance);

                if ((maxYDistance < 0.0D || Math.abs(entityplayer1.posY - posY) < maxYDistance * maxYDistance) && (maxXZDistance < 0.0D || d1 < d2 * d2) && (d0 == -1.0D || d1 < d0))
                {
                    d0 = d1;
                    entityplayer = entityplayer1;
                }
            }
        }

        return entityplayer;
    }

    @Nullable
    public EntityPlayer getPlayerEntityByName(String name)
    {
        for (int j2 = 0; j2 < this.playerEntities.size(); ++j2)
        {
            EntityPlayer entityplayer = this.playerEntities.get(j2);

            if (name.equals(entityplayer.getName()))
            {
                return entityplayer;
            }
        }

        return null;
    }

    @Nullable
    public EntityPlayer getPlayerEntityByUUID(UUID uuid)
    {
        for (int j2 = 0; j2 < this.playerEntities.size(); ++j2)
        {
            EntityPlayer entityplayer = this.playerEntities.get(j2);

            if (uuid.equals(entityplayer.getUniqueID()))
            {
                return entityplayer;
            }
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public void sendQuittingDisconnectingPacket()
    {
    }

    public void checkSessionLock() throws MinecraftException
    {
        this.saveHandler.checkSessionLock();
    }

    @SideOnly(Side.CLIENT)
    public void setTotalWorldTime(long worldTime)
    {
        this.worldInfo.setWorldTotalTime(worldTime);
    }

    public long getSeed()
    {
        return this.provider.getSeed();
    }

    public long getTotalWorldTime()
    {
        return this.worldInfo.getWorldTotalTime();
    }

    public long getWorldTime()
    {
        return this.provider.getWorldTime();
    }

    public void setWorldTime(long time)
    {
        this.provider.setWorldTime(time);
    }

    public BlockPos getSpawnPoint()
    {
        BlockPos blockpos1 = this.provider.getSpawnPoint();

        if (!this.getWorldBorder().contains(blockpos1))
        {
            blockpos1 = this.getHeight(new BlockPos(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
        }

        return blockpos1;
    }

    public void setSpawnPoint(BlockPos pos)
    {
        this.provider.setSpawnPoint(pos);
    }

    @SideOnly(Side.CLIENT)
    public void joinEntityInSurroundings(Entity entityIn)
    {
        int j2 = MathHelper.floor(entityIn.posX / 16.0D);
        int k2 = MathHelper.floor(entityIn.posZ / 16.0D);
        int l2 = 2;

        for (int i3 = -2; i3 <= 2; ++i3)
        {
            for (int j3 = -2; j3 <= 2; ++j3)
            {
                this.getChunkFromChunkCoords(j2 + i3, k2 + j3);
            }
        }

        if (!this.loadedEntityList.contains(entityIn))
        {
            if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.EntityJoinWorldEvent(entityIn, this)))
            this.loadedEntityList.add(entityIn);
        }
    }

    public boolean isBlockModifiable(EntityPlayer player, BlockPos pos)
    {
        return this.provider.canMineBlock(player, pos);
    }

    public boolean canMineBlockBody(EntityPlayer player, BlockPos pos)
    {
        return true;
    }

    public void setEntityState(Entity entityIn, byte state)
    {
    }

    public IChunkProvider getChunkProvider()
    {
        return this.chunkProvider;
    }

    public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam)
    {
        this.getBlockState(pos).onBlockEventReceived(this, pos, eventID, eventParam);
    }

    public ISaveHandler getSaveHandler()
    {
        return this.saveHandler;
    }

    public WorldInfo getWorldInfo()
    {
        return this.worldInfo;
    }

    public GameRules getGameRules()
    {
        return this.worldInfo.getGameRulesInstance();
    }

    public void updateAllPlayersSleepingFlag()
    {
    }

    public float getThunderStrength(float delta)
    {
        return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * delta) * this.getRainStrength(delta);
    }

    @SideOnly(Side.CLIENT)
    public void setThunderStrength(float strength)
    {
        this.prevThunderingStrength = strength;
        this.thunderingStrength = strength;
    }

    public float getRainStrength(float delta)
    {
        return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * delta;
    }

    @SideOnly(Side.CLIENT)
    public void setRainStrength(float strength)
    {
        this.prevRainingStrength = strength;
        this.rainingStrength = strength;
    }

    public boolean isThundering()
    {
        return (double)this.getThunderStrength(1.0F) > 0.9D;
    }

    public boolean isRaining()
    {
        return (double)this.getRainStrength(1.0F) > 0.2D;
    }

    public boolean isRainingAt(BlockPos position)
    {
        if (!this.isRaining())
        {
            return false;
        }
        else if (!this.canSeeSky(position))
        {
            return false;
        }
        else if (this.getPrecipitationHeight(position).getY() > position.getY())
        {
            return false;
        }
        else
        {
            Biome biome = this.getBiome(position);

            if (biome.getEnableSnow())
            {
                return false;
            }
            else
            {
                return this.canSnowAt(position, false) ? false : biome.canRain();
            }
        }
    }

    public boolean isBlockinHighHumidity(BlockPos pos)
    {
        return this.provider.isBlockHighHumidity(pos);
    }

    @Nullable
    public MapStorage getMapStorage()
    {
        return this.mapStorage;
    }

    public void setData(String dataID, WorldSavedData worldSavedDataIn)
    {
        this.mapStorage.setData(dataID, worldSavedDataIn);
    }

    @Nullable
    public WorldSavedData loadData(Class <? extends WorldSavedData > clazz, String dataID)
    {
        return this.mapStorage.getOrLoadData(clazz, dataID);
    }

    public int getUniqueDataId(String key)
    {
        return this.mapStorage.getUniqueDataId(key);
    }

    public void playBroadcastSound(int id, BlockPos pos, int data)
    {
        for (int j2 = 0; j2 < this.eventListeners.size(); ++j2)
        {
            ((IWorldEventListener)this.eventListeners.get(j2)).broadcastSound(id, pos, data);
        }
    }

    public void playEvent(int type, BlockPos pos, int data)
    {
        this.playEvent((EntityPlayer)null, type, pos, data);
    }

    public void playEvent(@Nullable EntityPlayer player, int type, BlockPos pos, int data)
    {
        try
        {
            for (int j2 = 0; j2 < this.eventListeners.size(); ++j2)
            {
                ((IWorldEventListener)this.eventListeners.get(j2)).playEvent(player, type, pos, data);
            }
        }
        catch (Throwable throwable3)
        {
            CrashReport crashreport3 = CrashReport.makeCrashReport(throwable3, "Playing level event");
            CrashReportCategory crashreportcategory3 = crashreport3.makeCategory("Level event being played");
            crashreportcategory3.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(pos));
            crashreportcategory3.addCrashSection("Event source", player);
            crashreportcategory3.addCrashSection("Event type", Integer.valueOf(type));
            crashreportcategory3.addCrashSection("Event data", Integer.valueOf(data));
            throw new ReportedException(crashreport3);
        }
    }

    public int getHeight()
    {
        return this.provider.getHeight();
    }

    public int getActualHeight()
    {
        return this.provider.getActualHeight();
    }

    public Random setRandomSeed(int p_72843_1_, int p_72843_2_, int p_72843_3_)
    {
        long j2 = (long)p_72843_1_ * 341873128712L + (long)p_72843_2_ * 132897987541L + this.getWorldInfo().getSeed() + (long)p_72843_3_;
        this.rand.setSeed(j2);
        return this.rand;
    }

    public CrashReportCategory addWorldInfoToCrashReport(CrashReport report)
    {
        CrashReportCategory crashreportcategory3 = report.makeCategoryDepth("Affected level", 1);
        crashreportcategory3.addCrashSection("Level name", this.worldInfo == null ? "????" : this.worldInfo.getWorldName());
        crashreportcategory3.addDetail("All players", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return World.this.playerEntities.size() + " total; " + World.this.playerEntities;
            }
        });
        crashreportcategory3.addDetail("Chunk stats", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return World.this.chunkProvider.makeString();
            }
        });

        try
        {
            this.worldInfo.addToCrashReport(crashreportcategory3);
        }
        catch (Throwable throwable3)
        {
            crashreportcategory3.addCrashSectionThrowable("Level Data Unobtainable", throwable3);
        }

        return crashreportcategory3;
    }

    @SideOnly(Side.CLIENT)
    public double getHorizon()
    {
        return provider.getHorizon();
    }

    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
    {
        for (int j2 = 0; j2 < this.eventListeners.size(); ++j2)
        {
            IWorldEventListener iworldeventlistener = this.eventListeners.get(j2);
            iworldeventlistener.sendBlockBreakProgress(breakerId, pos, progress);
        }
    }

    public Calendar getCurrentDate()
    {
        if (this.getTotalWorldTime() % 600L == 0L)
        {
            this.calendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
        }

        return this.calendar;
    }

    @SideOnly(Side.CLIENT)
    public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, @Nullable NBTTagCompound compound)
    {
    }

    public Scoreboard getScoreboard()
    {
        return this.worldScoreboard;
    }

    public void updateComparatorOutputLevel(BlockPos pos, Block blockIn)
    {
        for (EnumFacing enumfacing : EnumFacing.VALUES)
        {
            BlockPos blockpos1 = pos.offset(enumfacing);

            if (this.isBlockLoaded(blockpos1))
            {
                IBlockState iblockstate1 = this.getBlockState(blockpos1);

                iblockstate1.getBlock().onNeighborChange(this, blockpos1, pos);
                if (iblockstate1.getBlock().isNormalCube(iblockstate1, this, blockpos1))
                {
                    blockpos1 = blockpos1.offset(enumfacing);
                    iblockstate1 = this.getBlockState(blockpos1);

                    if (iblockstate1.getBlock().getWeakChanges(this, blockpos1))
                    {
                        iblockstate1.getBlock().onNeighborChange(this, blockpos1, pos);
                    }
                }
            }
        }
    }

    public DifficultyInstance getDifficultyForLocation(BlockPos pos)
    {
        long j2 = 0L;
        float f = 0.0F;

        if (this.isBlockLoaded(pos))
        {
            f = this.getCurrentMoonPhaseFactor();
            j2 = this.getChunkFromBlockCoords(pos).getInhabitedTime();
        }

        return new DifficultyInstance(this.getDifficulty(), this.getWorldTime(), j2, f);
    }

    public EnumDifficulty getDifficulty()
    {
        return this.getWorldInfo().getDifficulty();
    }

    public int getSkylightSubtracted()
    {
        return this.skylightSubtracted;
    }

    public void setSkylightSubtracted(int newSkylightSubtracted)
    {
        this.skylightSubtracted = newSkylightSubtracted;
    }

    @SideOnly(Side.CLIENT)
    public int getLastLightningBolt()
    {
        return this.lastLightningBolt;
    }

    public void setLastLightningBolt(int lastLightningBoltIn)
    {
        this.lastLightningBolt = lastLightningBoltIn;
    }

    public VillageCollection getVillageCollection()
    {
        return this.villageCollection;
    }

    public WorldBorder getWorldBorder()
    {
        return this.worldBorder;
    }

    public boolean isSpawnChunk(int x, int z)
    {
        BlockPos blockpos1 = this.getSpawnPoint();
        int j2 = x * 16 + 8 - blockpos1.getX();
        int k2 = z * 16 + 8 - blockpos1.getZ();
        int l2 = 128;
        return j2 >= -128 && j2 <= 128 && k2 >= -128 && k2 <= 128 && this.keepSpawnInMemory;
    }

    /* ======================================== FORGE START =====================================*/
    /**
     * Determine if the given block is considered solid on the
     * specified side.  Used by placement logic.
     *
     * @param pos Block Position
     * @param side The Side in question
     * @return True if the side is solid
    */
    public boolean isSideSolid(BlockPos pos, EnumFacing side)
    {
       return isSideSolid(pos, side, false);
    }

    /**
     * Determine if the given block is considered solid on the
     * specified side.  Used by placement logic.
     *
     * @param pos Block Position
     * @param side The Side in question
     * @param _default The default to return if the block doesn't exist.
     * @return True if the side is solid
     */
    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default)
    {
        if (!this.isValid(pos)) return _default;

        Chunk chunk = getChunkFromBlockCoords(pos);
        if (chunk == null || chunk.isEmpty()) return _default;
        return getBlockState(pos).isSideSolid(this, pos, side);
    }

    /**
     * Get the persistent chunks for this world
     *
     * @return
     */
    public com.google.common.collect.ImmutableSetMultimap<net.minecraft.util.math.ChunkPos, net.minecraftforge.common.ForgeChunkManager.Ticket> getPersistentChunks()
    {
        return net.minecraftforge.common.ForgeChunkManager.getPersistentChunksFor(this);
    }

    public Iterator<Chunk> getPersistentChunkIterable(Iterator<Chunk> chunkIterator)
    {
        return net.minecraftforge.common.ForgeChunkManager.getPersistentChunksIterableFor(this, chunkIterator);
    }
    /**
     * Readded as it was removed, very useful helper function
     *
     * @param pos Block position
     * @return The blocks light opacity
     */
    public int getBlockLightOpacity(BlockPos pos)
    {
        if (!this.isValid(pos)) return 0;
        return getChunkFromBlockCoords(pos).getBlockLightOpacity(pos);
    }

    /**
     * Returns a count of entities that classify themselves as the specified creature type.
     */
    public int countEntities(net.minecraft.entity.EnumCreatureType type, boolean forSpawnCount)
    {
        int count = 0;
        for (int x = 0; x < loadedEntityList.size(); x++)
        {
            if (((Entity)loadedEntityList.get(x)).isCreatureType(type, forSpawnCount))
            {
                count++;
            }
        }
        return count;
    }

    @Deprecated // remove in 1.13
    public void markTileEntitiesInChunkForRemoval(Chunk chunk)
    {
        for (TileEntity tileentity : chunk.getTileEntityMap().values())
        {
            markTileEntityForRemoval(tileentity);
        }
    }

    protected void initCapabilities()
    {
        net.minecraftforge.common.capabilities.ICapabilityProvider parent = provider.initCapabilities();
        capabilities = net.minecraftforge.event.ForgeEventFactory.gatherCapabilities(this, parent);
        net.minecraftforge.common.util.WorldCapabilityData data = (net.minecraftforge.common.util.WorldCapabilityData)perWorldStorage.getOrLoadData(net.minecraftforge.common.util.WorldCapabilityData.class, net.minecraftforge.common.util.WorldCapabilityData.ID);
        if (data == null)
        {
            capabilityData = new net.minecraftforge.common.util.WorldCapabilityData(capabilities);
            perWorldStorage.setData(capabilityData.mapName, capabilityData);
        }
        else
        {
            capabilityData = data;
            capabilityData.setCapabilities(provider, capabilities);
        }
    }
    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capabilities == null ? false : capabilities.hasCapability(capability, facing);
    }
    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capabilities == null ? null : capabilities.getCapability(capability, facing);
    }

    protected MapStorage perWorldStorage; //Moved to a getter to simulate final without being final so we can load in subclasses.
    public MapStorage getPerWorldStorage()
    {
        return perWorldStorage;
    }

    public void sendPacketToServer(Packet<?> packetIn)
    {
        throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
    }

    public LootTableManager getLootTableManager()
    {
        return this.lootTable;
    }

    @Nullable
    public BlockPos findNearestStructure(String p_190528_1_, BlockPos p_190528_2_, boolean p_190528_3_)
    {
        return null;
    }
}