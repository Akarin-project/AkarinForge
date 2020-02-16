package io.akarin.forge.server;

import java.io.File;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.Main;
import org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_12_R1.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginLoadOrder;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import io.akarin.forge.server.layers.reflection.ReflectionTransformer;
import io.netty.util.concurrent.GenericFutureListener;
import joptsimple.OptionSet;
import net.minecraft.block.BlockSand;
import net.minecraft.crash.CrashReport;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PendingCommand;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.ReportedException;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class AkarinHooks {
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	static {
		// Resolves trace level for unknown reason
		java.util.logging.LogManager.getLogManager().reset();
	}
	
	private static boolean initalizedConnection;
	private static int initalizeConnectionSteps = 2;
	
	public static boolean verboseMissingMods() {
		if (initalizedConnection)
			// faster access after initalization
			return true;
		
		else if (--initalizeConnectionSteps < 0)
			// after initalized
			return (initalizedConnection = true);
		
		else
			// haven't finish initalization
			return false;
	}
	
	public static void registerBukkitEnchantments() {
        for (Object enchantment : Enchantment.REGISTRY) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment((Enchantment) enchantment));
        }
	}
	
	public static Environment lookupEnvironment(int dim) {
		// Lookup from bukkit
		Environment environment = Environment.getEnvironment(dim);
		
		if (environment == null)
			environment = Environment.getEnvironment(DimensionManager.getProviderType(dim).getId());
        
        // Figure out type then lookup from bukkit
        return environment;
	}
	
	public static void loadWorlds(MinecraftServer server, String saveName, String worldNameIn, long seed, WorldType type, String generatorOptions) {
        WorldSettings overworldSettings = new WorldSettings(seed, server.getGameType(), server.canStructuresSpawn(), server.isHardcore(), type);
        overworldSettings.setGeneratorOptions(generatorOptions);
        
        Integer[] dimIds = DimensionManager.getStaticDimensionIDs();
        Arrays.sort(dimIds, new Comparator<Integer>(){
            @Override
            public int compare(Integer dim1, Integer dim2) {
                return dim1 == 0 ? -1 : Math.max(dim1, dim2); // Always set dim 0 to the first
            }
        });
        
        // Prepare worlds array with same size
        server.worlds = new WorldServer[dimIds.length];
        
        for (int index = 0; index < dimIds.length; index++) {
            int dim = dimIds[index];
            
            // Skip not allowed nether or end
            if (dim != 0 && (dim == -1 && !server.getAllowNether() || dim == 1 && !server.server.getAllowEnd()))
                continue;
            
            Environment environment = lookupEnvironment(dim);
            // Register dimension to forge
            if (!DimensionManager.isDimensionRegistered(dim))
                DimensionManager.registerDimension(dim, DimensionType.getById(environment.getId()));
            
            // Make up world name by dimension
            String worldName = dim == 0 ? saveName : (dim == 1 ? "world_the_end" : (dim == -1 ? "world_nether" : "DIM" + dim));
            ChunkGenerator generator = server.server.getGenerator(worldName);
            
            ISaveHandler saver = new AnvilSaveHandler(new File("."), worldName, true, server.dataFixer);
            server.setResourcePackFromWorld(server.getFolderName(), saver);
            
        	WorldInfo info = saver.loadWorldInfo();
            if (info == null)
            	// Workaround: This can be null when manually delete etc,.
            	info = new WorldInfo(overworldSettings, worldName);
            
            WorldServer world;
            if (dim == 0) {
                world = (WorldServer) new WorldServer(server, saver, info, dim, server.profiler, environment, generator).init();
                world.initialize(overworldSettings);
                // Initialize server scoreboard
                server.server.scoreboardManager = new CraftScoreboardManager(server, world.getScoreboard());
            } else {
                world = (WorldServer) new WorldServerMulti(server, saver, dim, server.worlds[0], server.profiler, info, environment, generator).init();
            }
            
            // Sync data after create world instance
            world.worldInfo.dimension = dim;
            world.worldInfo.levelName = worldName;
            MinecraftServer.instance().server.addWorld(world.getWorld()); // Akarin
            
            // Put world into vanilla worlds
            server.worlds[index] = (WorldServer) world;
            
            world.addEventListener(new ServerWorldEventHandler(server, (WorldServer) world));
            world.getWorldInfo().setGameType(server.getGameType());
            
            // Events
            server.server.getPluginManager().callEvent(new WorldInitEvent(world.getWorld()));
            MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
        }
        
        server.getPlayerList().setPlayerManager(server.worlds);
        server.setDifficultyForAllWorlds(server.getDifficulty());
        server.initialWorldChunkLoad();
	}
	
	public static void initializeChunks(MinecraftServer server) {
        for (int index = 0; index < server.worlds.length; index++) {
            WorldServer world = server.worlds[index];
            
            LOGGER.info("Preparing start region for level " + world.dimension + " (Seed: " + world.getSeed() + ")");
            // Skip specificed worlds
            if (!world.getWorld().getKeepSpawnInMemory())
            	continue;
            
            BlockPos spawn = world.getSpawnPoint();
            long start = MinecraftServer.getCurrentTimeMillis();
            
            int chunks = 0;
            for (int x = -192; x <= 192 && server.isServerRunning(); x += 16) {
                for (int z = -192; z <= 192 && server.isServerRunning(); z += 16) {
                    world.getChunkProvider().provideChunk(spawn.getX() + x >> 4, spawn.getZ() + z >> 4);
                    
                    long current = MinecraftServer.getCurrentTimeMillis();
                    if (start - current > 1000) {
                        server.outputPercentRemaining("Preparing spawn area", ++chunks * 100 / 625);
                        start = current;
                    }
                }
            }
        }
        
        for (int index = 0; index < server.worlds.length; index++) {
        	server.server.getPluginManager().callEvent(new WorldLoadEvent(server.worlds[index].getWorld()));
        }
	}
	
	public static void preStopServer(MinecraftServer server) {
        synchronized (server.stopLock) {
            if (server.hasStopped) {
                return;
            }
            server.hasStopped = true;
        }
        
        //WatchdogThread.doStop();
        
        if (server.server != null) {
            server.server.disablePlugins();
        }
	}
	
	public static void stopServer(MinecraftServer server) {
        //if (SpigotConfig.saveUserCacheOnStopOnly)
            server.profileCache.save();
        
        MinecraftServer.LOGGER.info("Stopped server");
	}
	
    private static final int TPS = 20;
    private static final long SEC_IN_NANO = 1000000000;
    public static final long TICK_TIME = SEC_IN_NANO / TPS;
    public static final long MAX_CATCHUP_BUFFER = TICK_TIME * TPS * 60L; // Akarin
    public static final int SAMPLE_INTERVAL = 20; // Akarin
    public final static RollingAverage TPS_1 = new RollingAverage(60);
    public final static RollingAverage TPS_5 = new RollingAverage(60 * 5);
    public final static RollingAverage TPS_15 = new RollingAverage(60 * 15);
	
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
	
	public static void tickServer(MinecraftServer server) throws InterruptedException {
        Arrays.fill(server.recentTps, 20);
        long start = System.nanoTime(), lastTick = start - TICK_TIME, catchupTime = 0, curTime, wait, tickSection = start;
        
        while (server.serverRunning) {
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
            	long park = System.nanoTime();
            	while ((System.nanoTime() - park) < wait);
                curTime = System.nanoTime();
                wait = TICK_TIME - (curTime - lastTick);
            }
            
            catchupTime = Math.min(MAX_CATCHUP_BUFFER, catchupTime - wait);
            if (++MinecraftServer.currentTick % SAMPLE_INTERVAL == 0) {
                final long diff = curTime - tickSection;
                double currentTps = 1E9 / diff * SAMPLE_INTERVAL;
                
                TPS_1.add(currentTps, diff);
                TPS_5.add(currentTps, diff);
                TPS_15.add(currentTps, diff);
                
                // Backwards compat with bad plugins
                server.recentTps[0] = TPS_1.getAverage();
                server.recentTps[1] = TPS_5.getAverage();
                server.recentTps[2] = TPS_15.getAverage();
                tickSection = curTime;
            }
            lastTick = curTime;

            server.tick();
            server.serverIsRunning = true;
        }
	}
	
	public static void preTick(MinecraftServer server) {
		server.server.getScheduler().mainThreadHeartbeat(server.tickCounter);
        
        while (!server.processQueue.isEmpty()) {
        	server.processQueue.remove().run();
        }
        
        // Send time updates to everyone, it will get the right time from the world the player is in.
        if (server.tickCounter % 20 == 0) {
            for (int i = 0; i < server.getPlayerList().playerEntityList.size(); ++i) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) server.getPlayerList().playerEntityList.get(i);
                entityplayer.connection.sendPacket(new SPacketTimeUpdate(entityplayer.world.getTotalWorldTime(), entityplayer.getPlayerTime(), entityplayer.world.getGameRules().getBoolean("doDaylightCycle")));
            }
        }
	}
	
	public static void tickWorlds(MinecraftServer server) {
		for (int index = 0; index < server.worlds.length; index++) {
			WorldServer world = server.worlds[index];
			net.minecraftforge.fml.common.FMLCommonHandler.instance().onPreWorldTick(world);
			
			try {
				world.tick();
			} catch (Throwable t) {
				CrashReport crashreport = CrashReport.makeCrashReport(t, "Exception ticking world");
				world.addWorldInfoToCrashReport(crashreport);
				throw new ReportedException(crashreport);
			}
			
			try {
				world.updateEntities();
			} catch (Throwable t) {
				CrashReport crashreport1 = CrashReport.makeCrashReport(t, "Exception ticking world entities");
				world.addWorldInfoToCrashReport(crashreport1);
				throw new ReportedException(crashreport1);
			}

			net.minecraftforge.fml.common.FMLCommonHandler.instance().onPostWorldTick(world);
			world.getEntityTracker().tick();
		}
	}
	
	public static void initalizeServer(String[] arguments) {
		OptionSet options = Main.main(arguments);
        if (options == null) {
            return;
        }
        
        try {
            String root = ".";
            
            YggdrasilAuthenticationService yggdrasil = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
            MinecraftSessionService session = yggdrasil.createMinecraftSessionService();
            GameProfileRepository profile = yggdrasil.createProfileRepository();
            PlayerProfileCache usercache = new PlayerProfileCache(profile, new File(root, MinecraftServer.USER_CACHE_FILE.getName()));
            DedicatedServer server = new DedicatedServer(options, DataFixesManager.createFixer(), yggdrasil, session, profile, usercache);
            
            if (options.has("port")) {
                int port = (Integer) options.valueOf("port");
                if (port > 0) {
                    server.setServerPort(port);
                }
            }
            
            if (options.has("universe")) {
                server.anvilFile = (File) options.valueOf("universe");
            }
            
            if (options.has("world")) {
                server.setFolderName((String) options.valueOf("world"));
            }
            
            server.primaryThread.start();
        } catch (Throwable throwable) {
            LOGGER.fatal("Failed to start the minecraft server", throwable);
        }
	}
	
	public static void initalizePlugins(CraftServer server) {
		// try for NMS mapping
		ReflectionTransformer.init();
		
        server.loadPlugins();
        server.enablePlugins(PluginLoadOrder.STARTUP);
	}

	public static void initalizeConfiguration(DedicatedServer dedicatedServer) {
		
	}

	public static void handleServerCommandEvent(MinecraftServer server, PendingCommand command) {
        ServerCommandEvent event = new ServerCommandEvent(server.console, command.command);
        server.server.getPluginManager().callEvent(event);
        
        if (event.isCancelled())
        	return;
        
        command = new PendingCommand(event.getCommand(), command.sender);
        server.server.dispatchServerCommand(server.console, command);
	}

	public static String handleRemoteServerCommandEvent(DedicatedServer server, String command) {
		Waitable<String> waitable = new Waitable<String>() {
            @Override
            protected String evaluate() {
            	server.rconConsoleSource.resetLog();
                RemoteServerCommandEvent event = new RemoteServerCommandEvent(server.remoteConsole, command);
                server.server.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return "";
                }
                PendingCommand serverCommand = new PendingCommand(event.getCommand(), server.rconConsoleSource);
                server.server.dispatchServerCommand(server.remoteConsole, serverCommand);
                return server.rconConsoleSource.getLogContents();
            }
        };
        server.processQueue.add(waitable);
        try {
            return waitable.get();
        } catch (java.util.concurrent.ExecutionException e) {
            throw new RuntimeException("Exception processing rcon command " + command, e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Maintain interrupted state
            throw new RuntimeException("Interrupted processing rcon command " + command, e);
        }
	}
	
	public static WorldServer initalizeWorld(int dim) {
		WorldServer overworld = DimensionManager.getWorld(0);
        if (overworld == null) {
            throw new RuntimeException("Cannot Hotload Dim: Overworld is not Loaded!");
        }
        
        WorldSettings overworldSettings = new WorldSettings(overworld.getWorldInfo());
        Environment environment = lookupEnvironment(dim);
        
        // Make up world name by dimension
        String worldName = dim == 0 ? overworld.worldInfo.levelName : (dim == 1 ? "world_the_end" : (dim == -1 ? "world_nether" : "DIM" + dim));
        
        return initalizeWorld(dim, worldName, environment, overworldSettings);
	}
	
	public static WorldServer initalizeWorld(WorldCreator worldCreator) {
		MinecraftServer server = MinecraftServer.instance();
		
        WorldType type = WorldType.parseWorldType(worldCreator.type().getName());
        boolean generateStructures = worldCreator.generateStructures();
		WorldSettings worldSettings = new WorldSettings(worldCreator.seed(), GameType.getByID(server.server.getDefaultGameMode().getValue()), generateStructures, false, type);
		
        return initalizeWorld(worldCreator, worldSettings);
	}
	
	public static WorldServer initalizeWorld(WorldCreator worldCreator, WorldSettings worldSettings) {
		MinecraftServer server = MinecraftServer.instance();
        String worldName = worldCreator.name();
		
        SaveHandler saver = new AnvilSaveHandler(server.server.getWorldContainer(), worldName, true, server.getDataFixer());
        WorldInfo info = saver.loadWorldInfo();
        
        int dim = info != null ? info.dimension : 0;
            dim = dim == -1 || dim == 0 || dim == 1 ? DimensionManager.getNextFreeDimId() : dim;
        
        return initalizeWorld(dim, worldName, worldCreator.environment(), worldSettings);
	}
	
	public static WorldServer initalizeWorld(int dim, String worldName, Environment environment, WorldSettings worldSettings) {
		// ----------- World Initalization ----------
		
		WorldServer overworld = DimensionManager.getWorld(0);
        if (overworld == null) {
            throw new RuntimeException("Cannot Hotload Dim: Overworld is not Loaded!");
        }
		
        try {
            DimensionManager.getProviderType(dim);
        } catch (Exception e) {
            FMLLog.log.error("Cannot Hotload Dim: {}", dim, e);
            return overworld; // If a provider hasn't been registered then we can't hotload the dim
        }
        
        MinecraftServer server = overworld.getMinecraftServer();
        
        // Skip not allowed nether or end
        if (dim != 0 && (dim == -1 && !server.getAllowNether() || dim == 1 && !server.server.getAllowEnd()))
            return overworld;
        
        // Register dimension to forge
        if (!DimensionManager.isDimensionRegistered(dim))
            DimensionManager.registerDimension(dim, DimensionType.getById(environment.getId()));
        
        ISaveHandler saver = new AnvilSaveHandler(new File("."), worldName, true, server.dataFixer);
        ChunkGenerator generator = server.server.getGenerator(worldName);
        worldSettings.setGeneratorOptions(((DedicatedServer) server).getStringProperty("generator-settings", ""));
        
        WorldInfo worldInfo = new WorldInfo(worldSettings, worldName);
        WorldServer world = (WorldServer) new WorldServerMulti(server, saver, dim, overworld, server.profiler, worldInfo, environment, generator).init();
        
        // Sync data after create world instance
        world.worldInfo.dimension = dim;
        world.worldInfo.levelName = worldName;
        MinecraftServer.instance().server.addWorld(world.getWorld()); // Akarin
        
        // Put vanilla worlds
        List<WorldServer> worldServers = Lists.newArrayList(server.worlds);
        worldServers.add(world);
        server.worlds = worldServers.toArray(new WorldServer[0]);
        server.getPlayerList().setPlayerManager(server.worlds);
        
        world.addEventListener(new ServerWorldEventHandler(server, world));
        
        world.getWorldInfo().setGameType(server.getGameType());
        server.setDifficultyForAllWorlds(server.getDifficulty());
        
        // Events
        server.server.getPluginManager().callEvent(new WorldInitEvent(world.getWorld()));
        MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
        
		// ----------- Chunk Initalization ----------
        
        LOGGER.info("Preparing start region for level " + world.dimension + " (Seed: " + world.getSeed() + ")");
        
        if (world.getWorld().getKeepSpawnInMemory()) {
            BlockPos spawn = world.getSpawnPoint();
            long start = MinecraftServer.getCurrentTimeMillis();
            
            int chunks = 0;
            for (int x = -192; x <= 192 && server.isServerRunning(); x += 16) {
                for (int z = -192; z <= 192 && server.isServerRunning(); z += 16) {
                    world.getChunkProvider().provideChunk(spawn.getX() + x >> 4, spawn.getZ() + z >> 4);
                    
                    long current = MinecraftServer.getCurrentTimeMillis();
                    if (start - current > 1000) {
                        server.outputPercentRemaining("Preparing spawn area", ++chunks * 100 / 625);
                        start = current;
                    }
                }
            }
        }
        
        // Events
        server.server.getPluginManager().callEvent(new WorldLoadEvent(world.getWorld()));
        
        return world;
	}

	public static boolean handlePlayerKickEvent(NetHandlerPlayServer connection, String s) {
        // CraftBukkit start - fire PlayerKickEvent
        if (connection.processedDisconnect) {
            return false;
        }
        String leaveMessage = TextFormatting.YELLOW + connection.player.getName() + " left the game.";

        PlayerKickEvent event = new PlayerKickEvent(connection.server.getPlayer(connection.player), s, leaveMessage);

        if (connection.server.getServer().isServerRunning()) {
        	connection.server.getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            // Do not kick the player
            return false;
        }
        // Send the possibly modified leave message
        s = event.getReason();
        
        final TextComponentString chatcomponenttext = new TextComponentString(s);
        connection.netManager.sendPacket(new SPacketDisconnect(chatcomponenttext), new GenericFutureListener<io.netty.util.concurrent.Future<? super Void>>() {
            public void operationComplete(io.netty.util.concurrent.Future<? super Void> future) throws Exception {
            	connection.netManager.closeChannel(chatcomponenttext);
            }
        }, new GenericFutureListener[0]);
        
        connection.onDisconnect(chatcomponenttext);
        
        return true;
	}
	
	public static boolean handlePlayerMoveEvent(NetHandlerPlayServer connection, CPacketVehicleMove packetIn) {
        Player player = connection.getPlayer();
        // Get the Players previous Event location.
        Location from = new Location(player.getWorld(), connection.lastPosX, connection.lastPosY, connection.lastPosZ, connection.lastYaw, connection.lastPitch);
        Location to = player.getLocation().clone(); // Start off the To location as the Players current location.

        // If the packet contains movement information then we update the To location with the correct XYZ.
        to.setX(packetIn.getX());
        to.setY(packetIn.getY());
        to.setZ(packetIn.getZ());

        // If the packet contains look information then we update the To location with the correct Yaw & Pitch.
        to.setYaw(packetIn.getYaw());
        to.setPitch(packetIn.getPitch());

        // Prevent 40 event-calls for less than a single pixel of movement >.>
        double delta = Math.pow(connection.lastPosX - to.getX(), 2) + Math.pow(connection.lastPosY - to.getY(), 2) + Math.pow(connection.lastPosZ - to.getZ(), 2);
        float deltaAngle = Math.abs(connection.lastYaw - to.getYaw()) + Math.abs(connection.lastPitch - to.getPitch());

        if ((delta > 1f / 256 || deltaAngle > 10f) && !connection.player.isMovementBlocked()) {
            connection.lastPosX = to.getX();
            connection.lastPosY = to.getY();
            connection.lastPosZ = to.getZ();
            connection.lastYaw = to.getYaw();
            connection.lastPitch = to.getPitch();

            Location oldTo = to.clone();
            PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);
            connection.server.getPluginManager().callEvent(event);

            // If the event is cancelled we move the player back to their old location.
            if (event.isCancelled()) {
                connection.teleport(from);
                return false;
            }

            // If a Plugin has changed the To destination then we teleport the Player
            // there to avoid any 'Moved wrongly' or 'Moved too quickly' errors.
            // We only do this if the Event was not cancelled.
            if (!oldTo.equals(event.getTo()) && !event.isCancelled()) {
                connection.player.getBukkitEntity().teleport(event.getTo(), TeleportCause.PLUGIN);
                return false;
            }

            // Check to see if the Players Location has some how changed during the call of the event.
            // This can happen due to a plugin teleporting the player instead of using .setTo()
            if (!from.equals(connection.getPlayer().getLocation()) && connection.justTeleported) {
                connection.justTeleported = false;
                return false;
            }
        }
        
        return true;
	}

	public static void populateChunk(Chunk chunk, IChunkGenerator generator) {
        BlockSand.fallInstantly = true;
        
        try {
            // Populate vanilla terrain
    		generator.populate(chunk.x, chunk.z);
        	
            Random random = new Random();
            random.setSeed(chunk.world.getSeed());
            long xRand = random.nextLong() / 2 * 2 + 1;
            long zRand = random.nextLong() / 2 * 2 + 1;
            random.setSeed((long) chunk.x * xRand + (long) chunk.z * zRand ^ chunk.world.getSeed());
            
            // Populate bukkit
            org.bukkit.World world = chunk.world.getWorld();
            if (world != null) {
            	chunk.world.populating = true;
                try {
                    for (BlockPopulator populator : world.getPopulators()) {
                        populator.populate(world, random, chunk.bukkitChunk);
                    }
                } finally {
                	chunk.world.populating = false;
                }
            }
        } finally {
        	// Handle this safer
            BlockSand.fallInstantly = false;
		}
        
        MinecraftServer.instance().server.getPluginManager().callEvent(new ChunkPopulateEvent(chunk.bukkitChunk));
        
        // Populate forge
        GameRegistry.generateWorld(chunk.x, chunk.z, chunk.world, generator, chunk.world.getChunkProvider());
	}
}
