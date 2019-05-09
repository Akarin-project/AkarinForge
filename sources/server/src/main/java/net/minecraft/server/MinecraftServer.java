package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import io.akarin.forge.BukkitInjector;
import io.akarin.forge.Metrics;
import io.akarin.forge.api.bukkit.I18nManager;
import io.akarin.forge.threads.AkarinWatcher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import joptsimple.OptionSet;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Bootstrap;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.profiler.Profiler;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ITickable;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.Main;
import org.bukkit.craftbukkit.v1_12_R1.SpigotTimings;
import org.bukkit.craftbukkit.v1_12_R1.chunkio.ChunkIOExecutor;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_12_R1.util.ServerShutdownThread;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginLoadOrder;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.CustomTimingsHandler;
import org.spigotmc.SlackActivityAccountant;
import org.spigotmc.SpigotConfig;
import org.spigotmc.WatchdogThread;

public abstract class MinecraftServer implements ICommandSender, Runnable, IThreadListener, ISnooperInfo
{
    public static final Logger LOGGER = LogManager.getLogger(); // Akarin
    public static final File USER_CACHE_FILE = new File("usercache.json");
    protected ISaveFormat anvilConverterForAnvilFile; // Akarin
    private final Snooper usageSnooper = new Snooper("server", this, getCurrentTimeMillis());
    public File anvilFile; // Akarin
    private final List<ITickable> tickables = Lists.<ITickable>newArrayList();
    public final ICommandManager commandManager;
    public final Profiler profiler = new Profiler();
    private final NetworkSystem networkSystem;
    private final ServerStatusResponse statusResponse = new ServerStatusResponse();
    private final Random random = new Random();
    public final DataFixer dataFixer; // Akarin
    @SideOnly(Side.SERVER)
    private String hostname;
    private int serverPort = -1;
    public WorldServer[] worlds = new WorldServer[0];
    private PlayerList playerList;
    private boolean serverRunning = true;
    private boolean serverStopped;
    private int tickCounter;
    protected final Proxy serverProxy;
    public String currentTask;
    public int percentDone;
    private boolean onlineMode;
    private boolean preventProxyConnections;
    private boolean canSpawnAnimals;
    private boolean canSpawnNPCs;
    private boolean pvpEnabled;
    private boolean allowFlight;
    private String motd;
    private int buildLimit;
    private int maxPlayerIdleMinutes;
    public final long[] tickTimeArray = new long[100];
    //public long[][] timeOfLastDimensionTick;
    public java.util.Hashtable<Integer, long[]> worldTickTimes = new java.util.Hashtable<Integer, long[]>();
    private KeyPair serverKeyPair;
    private String serverOwner;
    private String folderName;
    @SideOnly(Side.CLIENT)
    private String worldName;
    private boolean isDemo;
    private boolean enableBonusChest;
    private String resourcePackUrl = "";
    private String resourcePackHash = "";
    private boolean serverIsRunning;
    private long timeOfLastWarning;
    private String userMessage;
    private boolean startProfiling;
    private boolean isGamemodeForced;
    private final YggdrasilAuthenticationService authService;
    private final MinecraftSessionService sessionService;
    private final GameProfileRepository profileRepo;
    private final PlayerProfileCache profileCache;
    private long nanoTimeSinceStatusRefresh;
    public final Queue < FutureTask<? >> futureTaskQueue = Queues. < FutureTask<? >> newConcurrentLinkedQueue();
    private Thread serverThread;
    protected long currentTime = getCurrentTimeMillis();
    @SideOnly(Side.CLIENT)
    private boolean worldIconSet;
    // Akarin start
    public List<WorldServer> worldServers = Lists.newArrayList();
    public ConsoleCommandSender console;
    public RemoteConsoleCommandSender remoteConsole;
    public CraftServer server;
    public OptionSet options;
    public Thread primaryThread;
    public Queue<Runnable> processQueue = Queues.newConcurrentLinkedQueue();
    public int autosavePeriod;
    public final SlackActivityAccountant slackActivityAccountant;
    private boolean hasStopped;
    private final Object stopLock;
    public static int currentTick = (int) (System.currentTimeMillis() / 50);
    
    private static final int TPS = 20;
    private static final long SEC_IN_NANO = 1000000000;
    public static final long TICK_TIME = SEC_IN_NANO / TPS;
    private static final long MAX_CATCHUP_BUFFER = TICK_TIME * TPS * 60L;
    private static final int SAMPLE_INTERVAL = 20;
    public final RollingAverage tps1 = new RollingAverage(60);
    public final RollingAverage tps5 = new RollingAverage(60 * 5);
    public final RollingAverage tps15 = new RollingAverage(60 * 15);
    public double[] recentTps = new double[3];
    
    private static MinecraftServer SERVER;
    
    public static class RollingAverage {
        private final int size;
        private long time;
        private double total;
        private int index = 0;
        private final double[] samples;
        private final long[] times;

        RollingAverage(int size) {
            this.size = size;
            this.time = size * SEC_IN_NANO;
            this.total = TPS * SEC_IN_NANO * size;
            this.samples = new double[size];
            this.times = new long[size];
            for (int i = 0; i < size; i++) {
                this.samples[i] = TPS;
                this.times[i] = SEC_IN_NANO;
            }
        }

        public void add(double x, long t) {
            time -= times[index];
            total -= samples[index] * times[index];
            samples[index] = x;
            times[index] = t;
            time += t;
            total += x * t;
            if (++index == size) {
                index = 0;
            }
        }

        public double getAverage() {
            return total / time;
        }
    }
    
    public static MinecraftServer getServerInst() {
        return SERVER;
    }
    
    public abstract PropertyManager getPropertyManager();

    public MinecraftServer(OptionSet options, Proxy proxyIn, DataFixer dataFixerIn, YggdrasilAuthenticationService authServiceIn, MinecraftSessionService sessionServiceIn, GameProfileRepository profileRepoIn, PlayerProfileCache profileCacheIn)
    {
        SERVER = this;
        this.slackActivityAccountant = new SlackActivityAccountant();
        this.stopLock = new Object();
        this.recentTps = new double[3];
        
        if (System.console() == null && System.getProperty("jline.terminal") == null) {
            System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
            Main.useJline = false;
        }
        
        Runtime.getRuntime().addShutdownHook(new ServerShutdownThread(this));
        this.serverThread = this.primaryThread = new Thread(SidedThreadGroups.SERVER, this, "Server thread");
        // Akarin end
        this.serverProxy = proxyIn;
        this.authService = authServiceIn;
        this.sessionService = sessionServiceIn;
        this.profileRepo = profileRepoIn;
        this.profileCache = profileCacheIn;
        this.options = options; // Akarin
        this.networkSystem = new NetworkSystem(this);
        this.commandManager = this.createCommandManager();
        this.anvilConverterForAnvilFile = this.getActiveAnvilConverter(); // Akarin
        this.dataFixer = dataFixerIn;
    }

    public ServerCommandManager createCommandManager()
    {
        return new ServerCommandManager(this);
    }

    public abstract boolean init() throws IOException;

    public void convertMapIfNeeded(String worldNameIn)
    {
        if (this.getActiveAnvilConverter().isOldMapFormat(worldNameIn))
        {
            LOGGER.info("Converting map!");
            this.setUserMessage("menu.convertingLevel");
            this.getActiveAnvilConverter().convertMapFormat(worldNameIn, new IProgressUpdate()
            {
                private long startTime = System.currentTimeMillis();
                public void displaySavingString(String message)
                {
                }
                public void setLoadingProgress(int progress)
                {
                    if (System.currentTimeMillis() - this.startTime >= 1000L)
                    {
                        this.startTime = System.currentTimeMillis();
                        MinecraftServer.LOGGER.info("Converting... {}%", (int)progress);
                    }
                }
                @SideOnly(Side.CLIENT)
                public void resetProgressAndMessage(String message)
                {
                }
                @SideOnly(Side.CLIENT)
                public void setDoneWorking()
                {
                }
                public void displayLoadingString(String message)
                {
                }
            });
        }
    }

    protected synchronized void setUserMessage(String message)
    {
        this.userMessage = message;
    }

    @Nullable
    @SideOnly(Side.CLIENT)

    public synchronized String getUserMessage()
    {
        return this.userMessage;
    }

    public void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String generatorOptions)
    {
        // Akarin start
        ServerCommandManager commandManager = this.createCommandManager();
        // Akarin end
        this.convertMapIfNeeded(saveName);
        this.setUserMessage("menu.loadingLevel");
        ISaveHandler isavehandler = this.anvilConverterForAnvilFile.getSaveLoader(saveName, true);
        this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        // Akarin start
        this.worlds = new WorldServer[3];
        
        WorldSettings worldsettings = new WorldSettings(seed, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), type);
        worldsettings.setGeneratorOptions(generatorOptions);
        Integer[] dimIds = DimensionManager.getStaticDimensionIDs();
        
        Arrays.sort(dimIds, new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 == 0 ? -1 : Math.max(o1, o2);
            }
        });
        int worldConunter = 0;
        
        Integer[] arrinteger = dimIds;
        int n2 = arrinteger.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            WorldInfo worlddata;
            ISaveHandler idatamanager;
            World world;
            String worldType;
            int dim = arrinteger[i2];
            
            if (dim != 0 && (dim == -1 && !this.getAllowNether() || dim == 1 && !this.server.getAllowEnd()))
                continue;
            
            Environment worldEnvironment = Environment.getEnvironment(dim);
            if (worldEnvironment == null) {
                WorldProvider provider = DimensionManager.createProviderFor(dim);
                worldType = provider.getClass().getSimpleName().toLowerCase();
                worldType = worldType.replace("worldprovider", "");
                worldType = worldType.replace("provider", "");
                worldEnvironment = Environment.getEnvironment(DimensionManager.getProviderType(dim).getId());
            } else {
                worldType = worldEnvironment.toString().toLowerCase();
            }
            
            String name = dim == 0 ? saveName : "DIM" + dim;
            ChunkGenerator gen = null;
            
            if (dim == 0) {
                idatamanager = new net.minecraft.world.chunk.storage.AnvilSaveHandler(this.server.getWorldContainer(), worldNameIn, true, this.dataFixer);
                worlddata = idatamanager.loadWorldInfo();
                
                if (!BukkitInjector.initializedBukkit) {
                    BukkitInjector.injectBlockBukkitMaterials();
                    BukkitInjector.injectItemBukkitMaterials();
                    BukkitInjector.injectBiomes();
                    BukkitInjector.injectEntityType();
                    BukkitInjector.registerEnchantments();
                    BukkitInjector.registerPotions();
                    BukkitInjector.initializedBukkit = true;
                    I18nManager.loadI18n();
                }
                
                if (worlddata == null) {
                    worlddata = new WorldInfo(worldsettings, worldNameIn);
                }
                
                worlddata.checkName(worldNameIn);
                world = new WorldServer(this, idatamanager, worlddata, dim, this.profiler, worldEnvironment, gen).init();
                worlds[0] = (WorldServer) world;
                world.initialize(worldsettings);
                
                this.server.scoreboardManager = new CraftScoreboardManager(this, world.getScoreboard());
            } else {
                gen = this.server.getGenerator(name);
                idatamanager = new net.minecraft.world.chunk.storage.AnvilSaveHandler(this.server.getWorldContainer(), name, true, this.dataFixer);
                worlddata = idatamanager.loadWorldInfo();
                
                if (worlddata == null) {
                    worlddata = new WorldInfo(worldsettings, name);
                }
                
                worlddata.checkName(name);
                world = new WorldServerMulti(this, idatamanager, dim, this.worlds[0], this.profiler, worlddata, Environment.getEnvironment(dim), gen).init();
                
                if (++worldConunter < 3)
                    worlds[worldConunter] = (WorldServer) world;
            }
            
            world.addEventListener(new ServerWorldEventHandler(this, (WorldServer) world));
            world.getWorldInfo().setGameType(this.getGameType());
            worldServers.add((WorldServer) world);
            
            this.server.getPluginManager().callEvent(new WorldInitEvent(world.getWorld()));
            MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
        }
        
        getPlayerList().setPlayerManager(worldServers.toArray(new WorldServer[worldServers.size()]));
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();

        /*
        WorldSettings worldsettings;

        if (worldinfo == null)
        {
            if (this.isDemo())
            {
                worldsettings = WorldServerDemo.DEMO_WORLD_SETTINGS;
            }
            else
            {
                worldsettings = new WorldSettings(seed, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), type);
                worldsettings.setGeneratorOptions(generatorOptions);

                if (this.enableBonusChest)
                {
                    worldsettings.enableBonusChest();
                }
            }

            worldinfo = new WorldInfo(worldsettings, worldNameIn);
        }
        else
        {
            worldinfo.setWorldName(worldNameIn);
            worldsettings = new WorldSettings(worldinfo);
        }

        if (false) { //Forge Dead code, reimplemented below
        for (int i = 0; i < this.worlds.length; ++i)
        {
            int j = 0;

            if (i == 1)
            {
                j = -1;
            }

            if (i == 2)
            {
                j = 1;
            }

            if (i == 0)
            {
                if (this.isDemo())
                {
                    this.worlds[i] = (WorldServer)(new WorldServerDemo(this, isavehandler, worldinfo, j, this.profiler)).init();
                }
                else
                {
                    this.worlds[i] = (WorldServer)(new WorldServer(this, isavehandler, worldinfo, j, this.profiler)).init();
                }

                this.worlds[i].initialize(worldsettings);
            }
            else
            {
                this.worlds[i] = (WorldServer)(new WorldServerMulti(this, isavehandler, j, this.worlds[0], this.profiler)).init();
            }

            this.worlds[i].addEventListener(new ServerWorldEventHandler(this, this.worlds[i]));

            if (!this.isSinglePlayer())
            {
                this.worlds[i].getWorldInfo().setGameType(this.getGameType());
            }
        }
        } //Forge: End dead code

        WorldServer overWorld = (WorldServer)(isDemo() ? new WorldServerDemo(this, isavehandler, worldinfo, 0, profiler).init() : new WorldServer(this, isavehandler, worldinfo, 0, profiler).init());
        overWorld.initialize(worldsettings);
        for (int dim : net.minecraftforge.common.DimensionManager.getStaticDimensionIDs())
        {
            WorldServer world = (dim == 0 ? overWorld : (WorldServer)new WorldServerMulti(this, isavehandler, dim, overWorld, profiler).init());
            world.addEventListener(new ServerWorldEventHandler(this, world));

            if (!this.isSinglePlayer())
            {
                world.getWorldInfo().setGameType(this.getGameType());
            }
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.WorldEvent.Load(world));
        }

        this.playerList.setPlayerManager(new WorldServer[]{ overWorld });
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();
        */
        // Akarin end
    }

    public void initialWorldChunkLoad()
    {
        int i = 16;
        int j = 4;
        int k = 192;
        int l = 625;
        int i1 = 0;
        this.setUserMessage("menu.generatingTerrain");
        // Akarin start
        
        for (int m2 = 0; m2 < this.worldServers.size(); ++m2) {
            WorldServer worldserver = this.worldServers.get(m2);
            
            LOGGER.info("Preparing start region for level " + m2 + " (Seed: " + worldserver.getSeed() + ")");
            if (!worldserver.getWorld().getKeepSpawnInMemory()) continue;
            BlockPos blockposition = worldserver.getSpawnPoint();
            
            long jk2 = MinecraftServer.getCurrentTimeMillis();
            i = 0;
            for (int l1 = -192; l1 <= 192 && this.isServerRunning(); l1 += 16) {
                for (int i22 = -192; i22 <= 192 && this.isServerRunning(); i22 += 16) {
                    long j22 = MinecraftServer.getCurrentTimeMillis();
                    if (j22 - jk2 > 1000) {
                        this.outputPercentRemaining("Preparing spawn area", i * 100 / 625);
                        jk2 = j22;
                    }
                    ++i;
                    worldserver.getChunkProvider().provideChunk(blockposition.getX() + k >> 4, blockposition.getZ() + l >> 4);
                }
            }
        }
        
        for (WorldServer world : this.worldServers) {
            this.server.getPluginManager().callEvent(new WorldLoadEvent(world.getWorld()));
        }
        
        this.server.enablePlugins(PluginLoadOrder.POSTWORLD);
        AkarinWatcher.startThread();
        new Metrics();
        // Akarin end

        this.clearCurrentTask();
    }

    public void setResourcePackFromWorld(String worldNameIn, ISaveHandler saveHandlerIn)
    {
        File file1 = new File(saveHandlerIn.getWorldDirectory(), "resources.zip");

        if (file1.isFile())
        {
            try
            {
                this.setResourcePack("level://" + URLEncoder.encode(worldNameIn, StandardCharsets.UTF_8.toString()) + "/" + "resources.zip", "");
            }
            catch (UnsupportedEncodingException var5)
            {
                LOGGER.warn("Something went wrong url encoding {}", (Object)worldNameIn);
            }
        }
    }

    public abstract boolean canStructuresSpawn();

    public abstract GameType getGameType();

    public abstract EnumDifficulty getDifficulty();

    public abstract boolean isHardcore();

    public abstract int getOpPermissionLevel();

    public abstract boolean shouldBroadcastRconToOps();

    public abstract boolean shouldBroadcastConsoleToOps();

    protected void outputPercentRemaining(String message, int percent)
    {
        this.currentTask = message;
        this.percentDone = percent;
        LOGGER.info("{}: {}%", message, Integer.valueOf(percent));
    }

    protected void clearCurrentTask()
    {
        this.currentTask = null;
        this.percentDone = 0;
    }

    public void saveAllWorlds(boolean isSilent)
    {
        for (WorldServer worldserver : this.worldServers) // Akarin
        {
            if (worldserver != null)
            {
                if (!isSilent)
                {
                    LOGGER.info("Saving chunks for level '{}'/{}", worldserver.getWorldInfo().getWorldName(), worldserver.provider.getDimensionType().getName());
                }

                try
                {
                    worldserver.saveAllChunks(true, (IProgressUpdate)null);
                }
                catch (MinecraftException minecraftexception)
                {
                    LOGGER.warn(minecraftexception.getMessage());
                }
            }
        }
    }

    public void stopServer()
    {
        LOGGER.info("Stopping server");
        // Akarin start
        AsyncCatcher.enabled = false;
        
        synchronized (this.stopLock) {
            if (this.hasStopped) {
                return;
            }
            this.hasStopped = true;
        }
        
        WatchdogThread.doStop();
        AkarinWatcher.stopThread();
        
        if (this.server != null) {
            this.server.disablePlugins();
        }
        // Akarin end

        if (this.getNetworkSystem() != null)
        {
            this.getNetworkSystem().terminateEndpoints();
        }

        if (this.playerList != null)
        {
            LOGGER.info("Saving players");
            this.playerList.saveAllPlayerData();
            this.playerList.removeAllPlayers();
            try { Thread.sleep(100); } catch (InterruptedException ex) {} // Akarin
        }

        if (this.worlds != null)
        {
            LOGGER.info("Saving worlds");

            for (WorldServer worldserver : this.worlds)
            {
                if (worldserver != null)
                {
                    worldserver.disableLevelSaving = false;
                }
            }

            this.saveAllWorlds(false);

            for (WorldServer worldserver1 : this.worlds)
            {
                if (worldserver1 != null)
                {
                    net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.WorldEvent.Unload(worldserver1));
                    worldserver1.flush();
                }
            }

            WorldServer[] tmp = worlds;
            for (WorldServer world : tmp)
            {
                net.minecraftforge.common.DimensionManager.setWorld(world.provider.getDimension(), null, this);
            }
        }

        if (this.usageSnooper.isSnooperRunning())
        {
            this.usageSnooper.stopSnooper();
        }

        CommandBase.setCommandListener(null); // Forge: fix MC-128561
        // Akarin start
        if (SpigotConfig.saveUserCacheOnStopOnly) {
            LOGGER.info("Saving usercache.json");
            this.profileCache.save();
        }
        // Akarin end
    }

    public boolean isServerRunning()
    {
        return this.serverRunning;
    }

    public void initiateShutdown()
    {
        this.serverRunning = false;
    }

    public void run()
    {
        try
        {
            if (this.init())
            {
                net.minecraftforge.fml.common.FMLCommonHandler.instance().handleServerStarted();
                this.currentTime = getCurrentTimeMillis();
                long i = 0L;
                this.statusResponse.setServerDescription(new TextComponentString(this.motd));
                this.statusResponse.setVersion(new ServerStatusResponse.Version("1.12.2", 340));
                this.applyServerIconToResponse(this.statusResponse);
                // Akarin start
                Arrays.fill( recentTps, 20 );
                long start = System.nanoTime(), lastTick = start - TICK_TIME, catchupTime = 0, curTime, wait, tickSection = start;
                while (this.serverRunning) {
                    curTime = System.nanoTime();
                    wait = TICK_TIME - (curTime - lastTick);
                    if (wait > 0) {
                        if (catchupTime < 2E6) {
                            wait += Math.abs(catchupTime);
                        } else if (wait < catchupTime) {
                            catchupTime -= wait;
                            wait = 0;
                        } else {
                            wait -= catchupTime;
                            catchupTime = 0;
                        }
                    }
                    if (wait > 0) {
                        Thread.sleep(wait / 1000000);
                        curTime = System.nanoTime();
                        wait = TICK_TIME - (curTime - lastTick);
                    }

                    catchupTime = Math.min(MAX_CATCHUP_BUFFER, catchupTime - wait);
                    if ( ++MinecraftServer.currentTick % SAMPLE_INTERVAL == 0 )
                    {
                        final long diff = curTime - tickSection;
                        double currentTps = 1E9 / diff * SAMPLE_INTERVAL;
                        tps1.add(currentTps, diff);
                        tps5.add(currentTps, diff);
                        tps15.add(currentTps, diff);
                        // Backwards compat with bad plugins
                        recentTps[0] = tps1.getAverage();
                        recentTps[1] = tps5.getAverage();
                        recentTps[2] = tps15.getAverage();
                        tickSection = curTime;
                    }
                    lastTick = curTime;

                    this.tick();
                    this.serverIsRunning = true;
                }
                // Akarin end
                net.minecraftforge.fml.common.FMLCommonHandler.instance().handleServerStopping();
                net.minecraftforge.fml.common.FMLCommonHandler.instance().expectServerStopped(); // has to come before finalTick to avoid race conditions
            }
            else
            {
                net.minecraftforge.fml.common.FMLCommonHandler.instance().expectServerStopped(); // has to come before finalTick to avoid race conditions
                this.finalTick((CrashReport)null);
            }
        }
        catch (net.minecraftforge.fml.common.StartupQuery.AbortedException e)
        {
            // ignore silently
            net.minecraftforge.fml.common.FMLCommonHandler.instance().expectServerStopped(); // has to come before finalTick to avoid race conditions
        }
        catch (Throwable throwable1)
        {
            LOGGER.error("Encountered an unexpected exception", throwable1);
            CrashReport crashreport = null;

            if (throwable1 instanceof ReportedException)
            {
                crashreport = this.addServerInfoToCrashReport(((ReportedException)throwable1).getCrashReport());
            }
            else
            {
                crashreport = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", throwable1));
            }

            File file1 = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (crashreport.saveToFile(file1))
            {
                LOGGER.error("This crash report has been saved to: {}", (Object)file1.getAbsolutePath());
            }
            else
            {
                LOGGER.error("We were unable to save this crash report to disk.");
            }

            net.minecraftforge.fml.common.FMLCommonHandler.instance().expectServerStopped(); // has to come before finalTick to avoid race conditions
            this.finalTick(crashreport);
        }
        finally
        {
            try
            {
                this.stopServer();
            }
            catch (Throwable throwable)
            {
                LOGGER.error("Exception stopping the server", throwable);
            }
            finally
            {
                net.minecraftforge.fml.common.FMLCommonHandler.instance().handleServerStopped();
                this.serverStopped = true;
                this.systemExitNow();
            }
        }
    }

    public void applyServerIconToResponse(ServerStatusResponse response)
    {
        File file1 = this.getFile("server-icon.png");

        if (!file1.exists())
        {
            file1 = this.getActiveAnvilConverter().getFile(this.getFolderName(), "icon.png");
        }

        if (file1.isFile())
        {
            ByteBuf bytebuf = Unpooled.buffer();

            try
            {
                BufferedImage bufferedimage = ImageIO.read(file1);
                Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
                Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
                ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
                ByteBuf bytebuf1 = Base64.encode(bytebuf);
                response.setFavicon("data:image/png;base64," + bytebuf1.toString(StandardCharsets.UTF_8));
                bytebuf1.release(); // Forge: fix MC-122085
            }
            catch (Exception exception)
            {
                LOGGER.error("Couldn't load server icon", (Throwable)exception);
            }
            finally
            {
                bytebuf.release();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean isWorldIconSet()
    {
        this.worldIconSet = this.worldIconSet || this.getWorldIconFile().isFile();
        return this.worldIconSet;
    }

    @SideOnly(Side.CLIENT)
    public File getWorldIconFile()
    {
        return this.getActiveAnvilConverter().getFile(this.getFolderName(), "icon.png");
    }

    public File getDataDirectory()
    {
        return new File(".");
    }

    public void finalTick(CrashReport report)
    {
    }

    public void systemExitNow()
    {
    }

    public void tick()
    {
        AkarinWatcher.update(); // Akarin
        long i = System.nanoTime();
        net.minecraftforge.fml.common.FMLCommonHandler.instance().onPreServerTick();
        ++this.tickCounter;

        if (this.startProfiling)
        {
            this.startProfiling = false;
            this.profiler.profilingEnabled = true;
            this.profiler.clearProfiling();
        }

        this.profiler.startSection("root");
        this.updateTimeLightAndEntities();

        if (i - this.nanoTimeSinceStatusRefresh >= 5000000000L)
        {
            this.nanoTimeSinceStatusRefresh = i;
            this.statusResponse.setPlayers(new ServerStatusResponse.Players(this.getMaxPlayers(), this.getCurrentPlayerCount()));
            GameProfile[] agameprofile = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
            int j = MathHelper.getInt(this.random, 0, this.getCurrentPlayerCount() - agameprofile.length);

            for (int k = 0; k < agameprofile.length; ++k)
            {
                agameprofile[k] = ((EntityPlayerMP)this.playerList.getPlayers().get(j + k)).getGameProfile();
            }

            Collections.shuffle(Arrays.asList(agameprofile));
            this.statusResponse.getPlayers().setPlayers(agameprofile);
            this.statusResponse.invalidateJson();
        }

        if (this.autosavePeriod > 0 && this.tickCounter % this.autosavePeriod == 0) // Akarin
        {
            this.profiler.startSection("save");
            this.playerList.saveAllPlayerData();
            this.saveAllWorlds(true);
            this.profiler.endSection();
        }

        this.profiler.startSection("tallying");
        this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - i;
        this.profiler.endSection();
        this.profiler.startSection("snooper");

        if (!this.usageSnooper.isSnooperRunning() && this.tickCounter > 100)
        {
            this.usageSnooper.startSnooper();
        }

        if (this.tickCounter % 6000 == 0)
        {
            this.usageSnooper.addMemoryStatsToSnooper();
        }

        this.profiler.endSection();
        this.profiler.endSection();
        net.minecraftforge.fml.common.FMLCommonHandler.instance().onPostServerTick();
        WatchdogThread.tick();
        CustomTimingsHandler.tick();
    }

    public void updateTimeLightAndEntities()
    {
        // Akarin start
        SpigotTimings.schedulerTimer.startTiming();
        this.server.getScheduler().mainThreadHeartbeat(this.tickCounter);
        SpigotTimings.schedulerTimer.stopTiming();
        
        while (!this.futureTaskQueue.isEmpty())
        {
            Util.runTask(this.futureTaskQueue.poll(), LOGGER);
        }
        
        SpigotTimings.processQueueTimer.startTiming();
        while (!this.processQueue.isEmpty()) {
            this.processQueue.remove().run();
        }
        SpigotTimings.processQueueTimer.stopTiming();
        
        SpigotTimings.chunkIOTickTimer.startTiming();
        ChunkIOExecutor.tick();
        SpigotTimings.chunkIOTickTimer.stopTiming();
        
        SpigotTimings.timeUpdateTimer.startTiming();
        // Send time updates to everyone, it will get the right time from the world the player is in.
        if (this.tickCounter % 20 == 0) {
            for (int i = 0; i < this.getPlayerList().playerEntityList.size(); ++i) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) this.getPlayerList().playerEntityList.get(i);
                entityplayer.connection.sendPacket(new SPacketTimeUpdate(entityplayer.world.getTotalWorldTime(), entityplayer.getPlayerTime(), entityplayer.world.getGameRules().getBoolean("doDaylightCycle")));
            }
        }
        SpigotTimings.timeUpdateTimer.stopTiming();
        // Akarin end
        this.profiler.startSection("jobs");

        this.profiler.endStartSection("levels");
        net.minecraftforge.common.chunkio.ChunkIOExecutor.tick();

        // Akarin start
        //Integer[] ids = net.minecraftforge.common.DimensionManager.getIDs(this.tickCounter % 200 == 0);
        for (int x = 0; x < this.worldServers.size(); x++)
        {
            WorldServer worldServer = this.worldServers.get(x);
            int id = worldServer.dimension;
            // Akarin end
            long i = System.nanoTime();

            if (id == 0 || this.getAllowNether())
            {
                WorldServer worldserver = net.minecraftforge.common.DimensionManager.getWorld(id);
                this.profiler.func_194340_a(() ->
                {
                    return worldserver.getWorldInfo().getWorldName();
                });

                /*
                if (this.tickCounter % 20 == 0)
                {
                    this.profiler.startSection("timeSync");
                    this.playerList.sendPacketToAllPlayersInDimension(new SPacketTimeUpdate(worldserver.getTotalWorldTime(), worldserver.getWorldTime(), worldserver.getGameRules().getBoolean("doDaylightCycle")), worldserver.provider.getDimension());
                    this.profiler.endSection();
                }
                */

                this.profiler.startSection("tick");
                net.minecraftforge.fml.common.FMLCommonHandler.instance().onPreWorldTick(worldserver);

                try
                {
                    worldserver.tick();
                }
                catch (Throwable throwable1)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Exception ticking world");
                    worldserver.addWorldInfoToCrashReport(crashreport);
                    throw new ReportedException(crashreport);
                }

                try
                {
                    worldserver.updateEntities();
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Exception ticking world entities");
                    worldserver.addWorldInfoToCrashReport(crashreport1);
                    throw new ReportedException(crashreport1);
                }

                net.minecraftforge.fml.common.FMLCommonHandler.instance().onPostWorldTick(worldserver);
                this.profiler.endSection();
                this.profiler.startSection("tracker");
                worldserver.getEntityTracker().tick();
                this.profiler.endSection();
                this.profiler.endSection();
                worldTickTimes.get(id)[this.tickCounter % 100] = System.nanoTime() - i;
            }
        }

        this.profiler.endStartSection("dim_unloading");
        net.minecraftforge.common.DimensionManager.unloadWorlds(worldTickTimes);
        this.profiler.endStartSection("connection");
        this.getNetworkSystem().networkTick();
        this.profiler.endStartSection("players");
        this.playerList.onTick();
        this.profiler.endStartSection("commandFunctions");
        this.getFunctionManager().update();
        this.profiler.endStartSection("tickables");

        for (int k = 0; k < this.tickables.size(); ++k)
        {
            ((ITickable)this.tickables.get(k)).update();
        }

        this.profiler.endSection();
    }

    public boolean getAllowNether()
    {
        return true;
    }

    public void startServerThread()
    {
        // Akarin start
        /*
        net.minecraftforge.fml.common.StartupQuery.reset();
        this.serverThread = this.primaryThread = new Thread(net.minecraftforge.fml.common.thread.SidedThreadGroups.SERVER, this, "Server thread"); // Akarin
        this.serverThread.start();
        */
        // Akarin end
    }

    public File getFile(String fileName)
    {
        return new File(this.getDataDirectory(), fileName);
    }

    public void logWarning(String msg)
    {
        LOGGER.warn(msg);
    }

    public WorldServer getWorld(int dimension)
    {
        WorldServer ret = net.minecraftforge.common.DimensionManager.getWorld(dimension, true);
        if (ret == null)
        {
            net.minecraftforge.common.DimensionManager.initDimension(dimension);
            ret = net.minecraftforge.common.DimensionManager.getWorld(dimension);
        }
        return ret;
    }

    public String getMinecraftVersion()
    {
        return "1.12.2";
    }

    public int getCurrentPlayerCount()
    {
        return this.playerList.getCurrentPlayerCount();
    }

    public int getMaxPlayers()
    {
        return this.playerList.getMaxPlayers();
    }

    public String[] getOnlinePlayerNames()
    {
        return this.playerList.getOnlinePlayerNames();
    }

    public GameProfile[] getOnlinePlayerProfiles()
    {
        return this.playerList.getOnlinePlayerProfiles();
    }

    public String getServerModName()
    {
        return net.minecraftforge.fml.common.FMLCommonHandler.instance().getModName();
    }

    public CrashReport addServerInfoToCrashReport(CrashReport report)
    {
        report.getCategory().addDetail("Profiler Position", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return MinecraftServer.this.profiler.profilingEnabled ? MinecraftServer.this.profiler.getNameOfLastSection() : "N/A (disabled)";
            }
        });

        if (this.playerList != null)
        {
            report.getCategory().addDetail("Player Count", new ICrashReportDetail<String>()
            {
                public String call()
                {
                    return MinecraftServer.this.playerList.getCurrentPlayerCount() + " / " + MinecraftServer.this.playerList.getMaxPlayers() + "; " + MinecraftServer.this.playerList.getPlayers();
                }
            });
        }

        return report;
    }

    public List<String> getTabCompletions(ICommandSender sender, String input, @Nullable BlockPos pos, boolean hasTargetBlock)
    {
        List<String> list = Lists.<String>newArrayList();
        boolean flag = input.startsWith("/");

        if (flag)
        {
            input = input.substring(1);
        }

        if (!flag && !hasTargetBlock)
        {
            String[] astring = input.split(" ", -1);
            String s2 = astring[astring.length - 1];

            for (String s1 : this.playerList.getOnlinePlayerNames())
            {
                if (CommandBase.doesStringStartWith(s2, s1))
                {
                    list.add(s1);
                }
            }

            return list;
        }
        else
        {
            boolean flag1 = !input.contains(" ");
            List<String> list1 = this.commandManager.getTabCompletions(sender, input, pos);

            if (!list1.isEmpty())
            {
                for (String s : list1)
                {
                    if (flag1 && !hasTargetBlock)
                    {
                        list.add("/" + s);
                    }
                    else
                    {
                        list.add(s);
                    }
                }
            }

            return list;
        }
    }

    public boolean isAnvilFileSet()
    {
        return true; // Akarin
    }

    public String getName()
    {
        return "Server";
    }

    public void sendMessage(ITextComponent component)
    {
        LOGGER.info(component.getUnformattedText());
    }

    public boolean canUseCommand(int permLevel, String commandName)
    {
        return true;
    }

    public ICommandManager getCommandManager()
    {
        return this.commandManager;
    }

    public KeyPair getKeyPair()
    {
        return this.serverKeyPair;
    }

    public String getServerOwner()
    {
        return this.serverOwner;
    }

    public void setServerOwner(String owner)
    {
        this.serverOwner = owner;
    }

    public boolean isSinglePlayer()
    {
        return this.serverOwner != null;
    }

    public String getFolderName()
    {
        return this.folderName;
    }

    public void setFolderName(String name)
    {
        this.folderName = name;
    }

    @SideOnly(Side.CLIENT)
    public void setWorldName(String worldNameIn)
    {
        this.worldName = worldNameIn;
    }

    @SideOnly(Side.CLIENT)
    public String getWorldName()
    {
        return this.worldName;
    }

    public void setKeyPair(KeyPair keyPair)
    {
        this.serverKeyPair = keyPair;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty difficulty)
    {
        for (WorldServer worldserver1 : this.worldServers) // Akarin
        {
            if (worldserver1 != null)
            {
                if (worldserver1.getWorldInfo().isHardcoreModeEnabled())
                {
                    worldserver1.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
                    worldserver1.setAllowedSpawnTypes(true, true);
                }
                else if (this.isSinglePlayer())
                {
                    worldserver1.getWorldInfo().setDifficulty(difficulty);
                    worldserver1.setAllowedSpawnTypes(worldserver1.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                }
                else
                {
                    worldserver1.getWorldInfo().setDifficulty(difficulty);
                    worldserver1.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
                }
            }
        }
    }

    public boolean allowSpawnMonsters()
    {
        return true;
    }

    public boolean isDemo()
    {
        return this.isDemo;
    }

    public void setDemo(boolean demo)
    {
        this.isDemo = demo;
    }

    public void canCreateBonusChest(boolean enable)
    {
        this.enableBonusChest = enable;
    }

    public ISaveFormat getActiveAnvilConverter()
    {
        return this.anvilConverterForAnvilFile;
    }

    public String getResourcePackUrl()
    {
        return this.resourcePackUrl;
    }

    public String getResourcePackHash()
    {
        return this.resourcePackHash;
    }

    public void setResourcePack(String url, String hash)
    {
        this.resourcePackUrl = url;
        this.resourcePackHash = hash;
    }

    public void addServerStatsToSnooper(Snooper playerSnooper)
    {
        playerSnooper.addClientStat("whitelist_enabled", Boolean.valueOf(false));
        playerSnooper.addClientStat("whitelist_count", Integer.valueOf(0));

        if (this.playerList != null)
        {
            playerSnooper.addClientStat("players_current", Integer.valueOf(this.getCurrentPlayerCount()));
            playerSnooper.addClientStat("players_max", Integer.valueOf(this.getMaxPlayers()));
            playerSnooper.addClientStat("players_seen", Integer.valueOf(this.playerList.getAvailablePlayerDat().length));
        }

        playerSnooper.addClientStat("uses_auth", Boolean.valueOf(this.onlineMode));
        playerSnooper.addClientStat("gui_state", this.getGuiEnabled() ? "enabled" : "disabled");
        playerSnooper.addClientStat("run_time", Long.valueOf((getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
        playerSnooper.addClientStat("avg_tick_ms", Integer.valueOf((int)(MathHelper.average(this.tickTimeArray) * 1.0E-6D)));
        int l = 0;

        if (this.worldServers != null) // Akarin
        {
            for (WorldServer worldserver1 : this.worldServers) // Akarin
            {
                if (worldserver1 != null)
                {
                    WorldInfo worldinfo = worldserver1.getWorldInfo();
                    playerSnooper.addClientStat("world[" + l + "][dimension]", Integer.valueOf(worldserver1.provider.getDimensionType().getId()));
                    playerSnooper.addClientStat("world[" + l + "][mode]", worldinfo.getGameType());
                    playerSnooper.addClientStat("world[" + l + "][difficulty]", worldserver1.getDifficulty());
                    playerSnooper.addClientStat("world[" + l + "][hardcore]", Boolean.valueOf(worldinfo.isHardcoreModeEnabled()));
                    playerSnooper.addClientStat("world[" + l + "][generator_name]", worldinfo.getTerrainType().getName());
                    playerSnooper.addClientStat("world[" + l + "][generator_version]", Integer.valueOf(worldinfo.getTerrainType().getVersion()));
                    playerSnooper.addClientStat("world[" + l + "][height]", Integer.valueOf(this.buildLimit));
                    playerSnooper.addClientStat("world[" + l + "][chunks_loaded]", Integer.valueOf(worldserver1.getChunkProvider().getLoadedChunkCount()));
                    ++l;
                }
            }
        }

        playerSnooper.addClientStat("worlds", Integer.valueOf(l));
    }

    public void addServerTypeToSnooper(Snooper playerSnooper)
    {
        playerSnooper.addStatToSnooper("singleplayer", Boolean.valueOf(this.isSinglePlayer()));
        playerSnooper.addStatToSnooper("server_brand", this.getServerModName());
        playerSnooper.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        playerSnooper.addStatToSnooper("dedicated", Boolean.valueOf(this.isDedicatedServer()));
    }

    public boolean isSnooperEnabled()
    {
        return true;
    }

    public abstract boolean isDedicatedServer();

    public boolean isServerInOnlineMode()
    {
        return this.server.getOnlineMode(); // Akarin
    }

    public void setOnlineMode(boolean online)
    {
        this.onlineMode = online;
    }

    public boolean getPreventProxyConnections()
    {
        return this.preventProxyConnections;
    }

    public boolean getCanSpawnAnimals()
    {
        return this.canSpawnAnimals;
    }

    public void setCanSpawnAnimals(boolean spawnAnimals)
    {
        this.canSpawnAnimals = spawnAnimals;
    }

    public boolean getCanSpawnNPCs()
    {
        return this.canSpawnNPCs;
    }

    public abstract boolean shouldUseNativeTransport();

    public void setCanSpawnNPCs(boolean spawnNpcs)
    {
        this.canSpawnNPCs = spawnNpcs;
    }

    public boolean isPVPEnabled()
    {
        return this.pvpEnabled;
    }

    public void setAllowPvp(boolean allowPvp)
    {
        this.pvpEnabled = allowPvp;
    }

    public boolean isFlightAllowed()
    {
        return this.allowFlight;
    }

    public void setAllowFlight(boolean allow)
    {
        this.allowFlight = allow;
    }

    public abstract boolean isCommandBlockEnabled();

    public String getMOTD()
    {
        return this.motd;
    }

    public void setMOTD(String motdIn)
    {
        this.motd = motdIn;
    }

    public int getBuildLimit()
    {
        return this.buildLimit;
    }

    public void setBuildLimit(int maxBuildHeight)
    {
        this.buildLimit = maxBuildHeight;
    }

    public boolean isServerStopped()
    {
        return this.serverStopped;
    }

    public PlayerList getPlayerList()
    {
        return this.playerList;
    }

    public void setPlayerList(PlayerList list)
    {
        this.playerList = list;
    }

    public void setGameType(GameType gameMode)
    {
        for (WorldServer worldserver1 : this.worldServers) // Akarin
        {
            worldserver1.getWorldInfo().setGameType(gameMode);
        }
    }

    public NetworkSystem getNetworkSystem()
    {
        return this.networkSystem;
    }

    @SideOnly(Side.CLIENT)
    public boolean serverIsInRunLoop()
    {
        return this.serverIsRunning;
    }

    public boolean getGuiEnabled()
    {
        return false;
    }

    public abstract String shareToLAN(GameType type, boolean allowCheats);

    public int getTickCounter()
    {
        return this.tickCounter;
    }

    public void enableProfiling()
    {
        this.startProfiling = true;
    }

    @SideOnly(Side.CLIENT)
    public Snooper getPlayerUsageSnooper()
    {
        return this.usageSnooper;
    }

    public World getEntityWorld()
    {
        return this.worlds[0];
    }

    public boolean isBlockProtected(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        return false;
    }

    public boolean getForceGamemode()
    {
        return this.isGamemodeForced;
    }

    public Proxy getServerProxy()
    {
        return this.serverProxy;
    }

    public static long getCurrentTimeMillis()
    {
        return System.currentTimeMillis();
    }

    public int getMaxPlayerIdleMinutes()
    {
        return this.maxPlayerIdleMinutes;
    }

    public void setPlayerIdleTimeout(int idleTimeout)
    {
        this.maxPlayerIdleMinutes = idleTimeout;
    }

    public MinecraftSessionService getMinecraftSessionService()
    {
        return this.sessionService;
    }

    public GameProfileRepository getGameProfileRepository()
    {
        return this.profileRepo;
    }

    public PlayerProfileCache getPlayerProfileCache()
    {
        return this.profileCache;
    }

    public ServerStatusResponse getServerStatusResponse()
    {
        return this.statusResponse;
    }

    public void refreshStatusNextTick()
    {
        this.nanoTimeSinceStatusRefresh = 0L;
    }

    @Nullable
    public Entity getEntityFromUuid(UUID uuid)
    {
        for (WorldServer worldserver1 : this.worldServers) // Akarin
        {
            if (worldserver1 != null)
            {
                Entity entity = worldserver1.getEntityFromUuid(uuid);

                if (entity != null)
                {
                    return entity;
                }
            }
        }

        return null;
    }

    public boolean sendCommandFeedback()
    {
        return this.worldServers.get(0).getGameRules().getBoolean("sendCommandFeedback");
    }

    public MinecraftServer getServer()
    {
        return this;
    }

    public int getMaxWorldSize()
    {
        return 29999984;
    }

    public <V> ListenableFuture<V> callFromMainThread(Callable<V> callable)
    {
        Validate.notNull(callable);

        if (!this.isCallingFromMinecraftThread()) // Akarin
        {
            ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.<V>create(callable);

            this.futureTaskQueue.add(listenablefuturetask);
            return listenablefuturetask;
        }
        else
        {
            try
            {
                return Futures.<V>immediateFuture(callable.call());
            }
            catch (Exception exception)
            {
                return Futures.immediateFailedCheckedFuture(exception);
            }
        }
    }

    public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule)
    {
        Validate.notNull(runnableToSchedule);
        return this.<Object>callFromMainThread(Executors.callable(runnableToSchedule));
    }

    public boolean isCallingFromMinecraftThread()
    {
        return Thread.currentThread() == this.serverThread;
    }

    public int getNetworkCompressionThreshold()
    {
        return 256;
    }

    public int getSpawnRadius(@Nullable WorldServer worldIn)
    {
        return worldIn != null ? worldIn.getGameRules().getInt("spawnRadius") : 10;
    }

    public AdvancementManager getAdvancementManager()
    {
        return this.worlds[0].getAdvancementManager();
    }

    public FunctionManager getFunctionManager()
    {
        return this.worlds[0].getFunctionManager();
    }

    public void reload()
    {
        if (this.isCallingFromMinecraftThread())
        {
            this.getPlayerList().saveAllPlayerData();
            this.worlds[0].getLootTableManager().reloadLootTables();
            this.getAdvancementManager().reload();
            this.getFunctionManager().reload();
            this.getPlayerList().reloadResources();
        }
        else
        {
            this.addScheduledTask(this::reload);
        }
    }

    @SideOnly(Side.SERVER)
    public String getServerHostname()
    {
        return this.hostname;
    }

    @SideOnly(Side.SERVER)
    public void setHostname(String host)
    {
        this.hostname = host;
    }

    @SideOnly(Side.SERVER)
    public void registerTickable(ITickable tickable)
    {
        this.tickables.add(tickable);
    }

    @SideOnly(Side.SERVER)
    public static void main(String[] p_main_0_)
    {
        
        //Forge: Copied from DedicatedServer.init as to run as early as possible, Old code left in place intentionally.
        //Done in good faith with permission: https://github.com/MinecraftForge/MinecraftForge/issues/3659#issuecomment-390467028
        ServerEula eula = new ServerEula(new File("eula.txt"));
        if (false && !eula.hasAcceptedEULA()) // Akarin
        {
            LOGGER.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
            eula.createEULAFile();
            return;
        }

        Bootstrap.register();
        
        // Akarin start
        OptionSet options = Main.main(p_main_0_);
        if (options == null) {
            return;
        }
        
        try {
            String root = ".";
            
            YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
            MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            PlayerProfileCache usercache = new PlayerProfileCache(gameprofilerepository, new File(root, MinecraftServer.USER_CACHE_FILE.getName()));
            DedicatedServer dedicatedserver = new DedicatedServer(options, DataFixesManager.createFixer(), yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, usercache);
            
            if (options.has("port")) {
                int port = (Integer) options.valueOf("port");
                if (port > 0) {
                    dedicatedserver.setServerPort(port);
                }
            }
            
            if (options.has("universe")) {
                dedicatedserver.anvilFile = (File) options.valueOf("universe");
            }
            
            if (options.has("world")) {
                dedicatedserver.setFolderName((String) options.valueOf("world"));
            }
            
            dedicatedserver.primaryThread.start();
        } catch (Throwable throwable) {
            LOGGER.fatal("Failed to start the minecraft server", throwable);
        }

        /*
        try
        {
            boolean flag = true;
            String s = null;
            String s1 = ".";
            String s2 = null;
            boolean flag1 = false;
            boolean flag2 = false;
            int l = -1;

            for (int i1 = 0; i1 < p_main_0_.length; ++i1)
            {
                String s3 = p_main_0_[i1];
                String s4 = i1 == p_main_0_.length - 1 ? null : p_main_0_[i1 + 1];
                boolean flag3 = false;

                if (!"nogui".equals(s3) && !"--nogui".equals(s3))
                {
                    if ("--port".equals(s3) && s4 != null)
                    {
                        flag3 = true;

                        try
                        {
                            l = Integer.parseInt(s4);
                        }
                        catch (NumberFormatException var13)
                        {
                            ;
                        }
                    }
                    else if ("--singleplayer".equals(s3) && s4 != null)
                    {
                        flag3 = true;
                        s = s4;
                    }
                    else if ("--universe".equals(s3) && s4 != null)
                    {
                        flag3 = true;
                        s1 = s4;
                    }
                    else if ("--world".equals(s3) && s4 != null)
                    {
                        flag3 = true;
                        s2 = s4;
                    }
                    else if ("--demo".equals(s3))
                    {
                        flag1 = true;
                    }
                    else if ("--bonusChest".equals(s3))
                    {
                        flag2 = true;
                    }
                }
                else
                {
                    flag = false;
                }

                if (flag3)
                {
                    ++i1;
                }
            }

            YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
            MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(s1, USER_CACHE_FILE.getName()));
            final DedicatedServer dedicatedserver = new DedicatedServer(new File(s1), DataFixesManager.createFixer(), yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, playerprofilecache);

            if (s != null)
            {
                dedicatedserver.setServerOwner(s);
            }

            if (s2 != null)
            {
                dedicatedserver.setFolderName(s2);
            }

            if (l >= 0)
            {
                dedicatedserver.setServerPort(l);
            }

            if (flag1)
            {
                dedicatedserver.setDemo(true);
            }

            if (flag2)
            {
                dedicatedserver.canCreateBonusChest(true);
            }

            if (flag && !GraphicsEnvironment.isHeadless())
            {
                dedicatedserver.setGuiEnabled();
            }

            dedicatedserver.startServerThread();
            Runtime.getRuntime().addShutdownHook(new Thread("Server Shutdown Thread")
            {
                public void run()
                {
                    dedicatedserver.stopServer();
                }
            });
        }
        catch (Exception exception)
        {
            LOGGER.fatal("Failed to start the minecraft server", (Throwable)exception);
        }
        */
        // Akarin end
    }

    @SideOnly(Side.SERVER)
    public void logInfo(String msg)
    {
        LOGGER.info(msg);
    }

    @SideOnly(Side.SERVER)
    public boolean isDebuggingEnabled()
    {
        return this.getPropertyManager().getBooleanProperty("debug", false);
    }

    @SideOnly(Side.SERVER)
    public void logSevere(String msg)
    {
        LOGGER.error(msg);
    }

    @SideOnly(Side.SERVER)
    public void logDebug(String msg)
    {
        if (this.isDebuggingEnabled())
        {
            LOGGER.info(msg);
        }
    }

    @SideOnly(Side.SERVER)
    public int getServerPort()
    {
        return this.serverPort;
    }

    @SideOnly(Side.SERVER)
    public void setServerPort(int port)
    {
        this.serverPort = port;
    }

    @SideOnly(Side.SERVER)
    public void setPreventProxyConnections(boolean p_190517_1_)
    {
        this.preventProxyConnections = p_190517_1_;
    }

    @SideOnly(Side.SERVER)
    public int getSpawnProtectionSize()
    {
        return 16;
    }

    @SideOnly(Side.SERVER)
    public void setForceGamemode(boolean force)
    {
        this.isGamemodeForced = force;
    }

    @SideOnly(Side.SERVER)
    public long getCurrentTime()
    {
        return this.currentTime;
    }

    @SideOnly(Side.SERVER)
    public Thread getServerThread()
    {
        return this.serverThread;
    }

    public DataFixer getDataFixer()
    {
        return this.dataFixer;
    }
}