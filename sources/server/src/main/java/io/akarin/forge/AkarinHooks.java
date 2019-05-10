package io.akarin.forge;

import java.io.File;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.Main;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_12_R1.util.Waitable;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginLoadOrder;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.SpigotConfig;
import org.spigotmc.WatchdogThread;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import io.akarin.forge.api.bukkit.I18nManager;
import joptsimple.OptionSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PendingCommand;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class AkarinHooks {
	private static final Logger LOGGER = LogManager.getRootLogger();
	
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
	
	public static Environment lookupEnvironment(int dim) {
		// Lookup from bukkit
        for (Environment env : Environment.values())
        	if (env.getId() == dim)
        		return env;
        
        // Figure out type then lookup from bukkit
        return Environment.getEnvironment(DimensionManager.getProviderType(dim).getId());
	}
	
	public static Environment lookupEnvironment(Map<Integer, Environment> lookup, int dim) {
		Environment env = lookup.get(dim);
		if (env == null)
			// Figure out type then lookup
			env = lookup.get(DimensionManager.getProviderType(dim).getId());
		
		return env;
	}
	
	public static void injectBukkit() {
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
        Bukkit.getLogger().warning(Lists.newArrayList(dimIds).toString());
        
        // Prepare worlds array with same size
        server.worlds = new WorldServer[dimIds.length];
        
        for (int index = 0; index < dimIds.length; index++) {
            int dim = dimIds[index];
            
            // Skip not allowed nether or end
            if (dim != 0 && (dim == -1 && !server.getAllowNether() || dim == 1 && !server.server.getAllowEnd()))
                continue;
            
            Environment environment = Environment.getEnvironment(dim);
            // Register dimension to forge
            if (!DimensionManager.isDimensionRegistered(dim))
                DimensionManager.registerDimension(dim, DimensionType.getById(environment.getId()));
            
            // Make up world name by dimension
            String worldName = dim == 0 ? saveName : "DIM" + dim;
            ChunkGenerator generator = server.server.getGenerator(worldName);
            
            ISaveHandler saver = new AnvilSaveHandler(server.server.getWorldContainer(), worldName, true, server.dataFixer);
            server.setResourcePackFromWorld(server.getFolderName(), saver);
            
        	WorldInfo info = saver.loadWorldInfo();
            if (info == null)
            	// Workaround: This can be null when manually delete etc,.
            	info = new WorldInfo(overworldSettings, worldNameIn);
            
            info.syncWorldName(worldNameIn);
            
            WorldServer world;
            if (dim == 0) {
                world = (WorldServer) new WorldServer(server, saver, info, dim, server.profiler, environment, generator).init();
                world.initialize(overworldSettings);
                // Initialize server scoreboard
                server.server.scoreboardManager = new CraftScoreboardManager(server, world.getScoreboard());
            } else {
                world = (WorldServer) new WorldServerMulti(server, saver, dim, server.worlds[0], server.profiler, info, environment, generator).init();
            }
            
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
	
	public static WorldServer initalizeWorld(int dim) {
		WorldServer overworld = DimensionManager.getWorld(0);
        if (overworld == null) {
            throw new RuntimeException("Cannot Hotload Dim: Overworld is not Loaded!");
        }
        
        WorldSettings overworldSettings = new WorldSettings(overworld.getWorldInfo());
        
        Environment environment = Environment.getEnvironment(dim);
        
        // Make up world name by dimension
        String worldName = "DIM" + dim;
        
        return initalizeWorld(dim, worldName, environment, overworldSettings);
	}
	
	public static WorldServer initalizeWorld(WorldCreator worldCreator) {
		MinecraftServer server = MinecraftServer.getServerInst();
		
        WorldType type = WorldType.parseWorldType(worldCreator.type().getName());
        boolean generateStructures = worldCreator.generateStructures();
		WorldSettings worldSettings = new WorldSettings(worldCreator.seed(), GameType.getByID(server.server.getDefaultGameMode().getValue()), generateStructures, false, type);
		
        return initalizeWorld(worldCreator, worldSettings);
	}
	
	public static WorldServer initalizeWorld(WorldCreator worldCreator, WorldSettings worldSettings) {
		MinecraftServer server = MinecraftServer.getServerInst();
        String worldName = worldCreator.name();
		
        SaveHandler saver = new AnvilSaveHandler(server.server.getWorldContainer(), worldName, true, server.getDataFixer());
        WorldInfo info = saver.loadWorldInfo();
        
        int dim = info != null ? info.getDimension() : 0;
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
        
        ISaveHandler saver = new AnvilSaveHandler(server.server.getWorldContainer(), worldName, true, server.dataFixer);
        ChunkGenerator generator = server.server.getGenerator(worldName);
        worldSettings.setGeneratorOptions(((DedicatedServer) server).getStringProperty("generator-settings", ""));
        
        WorldInfo worldInfo = new WorldInfo(worldSettings, worldName);
        WorldServer world = (WorldServer) new WorldServerMulti(server, saver, dim, overworld, server.profiler, worldInfo, environment, generator).init();
        
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
	
	public static void preStopServer(MinecraftServer server) {
        AsyncCatcher.enabled = false;
        
        synchronized (server.stopLock) {
            if (server.hasStopped) {
                return;
            }
            server.hasStopped = true;
        }
        
        WatchdogThread.doStop();
        
        if (server.server != null) {
            server.server.disablePlugins();
        }
	}
	
	public static void saveUserCache(MinecraftServer server) {
        if (SpigotConfig.saveUserCacheOnStopOnly) {
            LOGGER.info("Saving usercache.json");
            server.profileCache.save();
        }
	}
	
	public static void tickServer(MinecraftServer server) throws InterruptedException {
        Arrays.fill(server.recentTps, 20);
        long start = System.nanoTime(), lastTick = start - MinecraftServer.TICK_TIME, catchupTime = 0, curTime, wait, tickSection = start;
        
        while (server.serverRunning) {
            curTime = System.nanoTime();
            wait = MinecraftServer.TICK_TIME - (curTime - lastTick);
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
            	long target = wait / 1000000;
            	long park = System.nanoTime();
            	while ((System.nanoTime() - park) < target);
                curTime = System.nanoTime();
                wait = MinecraftServer.TICK_TIME - (curTime - lastTick);
            }
            
            catchupTime = Math.min(MinecraftServer.MAX_CATCHUP_BUFFER, catchupTime - wait);
            if (++MinecraftServer.currentTick % MinecraftServer.SAMPLE_INTERVAL == 0) {
                final long diff = curTime - tickSection;
                double currentTps = 1E9 / diff * MinecraftServer.SAMPLE_INTERVAL;
                
                server.tps1.add(currentTps, diff);
                server.tps5.add(currentTps, diff);
                server.tps15.add(currentTps, diff);
                
                // Backwards compat with bad plugins
                server.recentTps[0] = server.tps1.getAverage();
                server.recentTps[1] = server.tps5.getAverage();
                server.recentTps[2] = server.tps15.getAverage();
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
	
	public static void initalizeConfiguration(DedicatedServer server) {
        org.spigotmc.SpigotConfig.init((File) server.options.valueOf("spigot-settings"));
        org.spigotmc.SpigotConfig.registerCommands();
        
        com.destroystokyo.paper.PaperConfig.init((File) server.options.valueOf("paper-settings"));
        com.destroystokyo.paper.PaperConfig.registerCommands();
	}
	
	public static void initalizePlugins(CraftServer server) {
        server.loadPlugins();
        server.enablePlugins(PluginLoadOrder.STARTUP);
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
	
	public static void handleBukkitSpawnRadius(DedicatedServer server) {
        if (server.server.getBukkitSpawnRadius() > -1) {
            LOGGER.debug("'settings.spawn-radius' in bukkit.yml has been moved to 'spawn-protection' in server.properties. I will move your config for you.");
           
            server.getPropertyManager().serverProperties.remove("spawn-protection");
            server.getPropertyManager().getIntProperty("spawn-protection", server.server.getBukkitSpawnRadius());
            
            server.server.removeBukkitSpawnRadius();
            server.getPropertyManager().saveProperties();
        }
	}
	
	public static String queryPlugins(DedicatedServer server) {
		StringBuilder result = new StringBuilder();
        org.bukkit.plugin.Plugin[] plugins = server.server.getPluginManager().getPlugins();

        result.append(server.getName());
        result.append(" on Bukkit ");
        result.append(server.server.getBukkitVersion());

        if (plugins.length > 0 && server.server.getQueryPlugins()) {
            result.append(": ");

            for (int i = 0; i < plugins.length; i++) {
                if (i > 0) {
                    result.append("; ");
                }

                result.append(plugins[i].getDescription().getName());
                result.append(" ");
                result.append(plugins[i].getDescription().getVersion().replaceAll(";", ","));
            }
        }

        return result.toString();
	}
	
	public static void handleWorldBorder(World world) {
		world.getWorldBorder().world = (WorldServer) world;
		world.getWorldBorder().addListener(new IBorderListener() {
            public void onSizeChanged(WorldBorder worldborder, double d0) {
            	world.getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_SIZE), worldborder.world);
            }

            public void onTransitionStarted(WorldBorder worldborder, double d0, double d1, long i) {
            	world.getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.LERP_SIZE), worldborder.world);
            }

            public void onCenterChanged(WorldBorder worldborder, double d0, double d1) {
            	world.getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_CENTER), worldborder.world);
            }

            public void onWarningTimeChanged(WorldBorder worldborder, int i) {
            	world.getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_TIME), worldborder.world);
            }

            public void onWarningDistanceChanged(WorldBorder worldborder, int i) {
            	world.getServer().getHandle().sendAll(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.SET_WARNING_BLOCKS), worldborder.world);
            }

            public void onDamageAmountChanged(WorldBorder worldborder, double d0) {}

            public void onDamageBufferChanged(WorldBorder worldborder, double d0) {}
        });
	}
	
	public static boolean capatureTreeGeneration(World world, BlockPos pos, IBlockState newState, int flags) {
        if (world.captureTreeGeneration) {
            BlockSnapshot blocksnapshot = null;
            
            // Clean previous snapshot at the pos
            for (BlockSnapshot previous : world.capturedBlockSnapshots) {
                if (!previous.getPos().equals(pos))
                	continue;
                
                blocksnapshot = previous;
                break;
            }
            if (blocksnapshot != null) {
            	world.capturedBlockSnapshots.remove(blocksnapshot);
            }
            
            world.capturedBlockSnapshots.add(new BlockSnapshot(world, pos, newState, flags));
            return true;
        }
        
        return false;
	}
	
	public static boolean handleBlockPhysicsEvent(World world, Block blockIn, BlockPos pos) {
        CraftWorld cworld = ((WorldServer) world).getWorld();
        if (cworld != null) {
            BlockPhysicsEvent event = new BlockPhysicsEvent(cworld.getBlockAt(pos.getX(), pos.getY(), pos.getZ()), CraftMagicNumbers.getId(blockIn));
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }
        }
        
        return true;
	}
	
	public static void removePlayerFromMap(World world, Entity entityIn) {
		for (WorldSavedData o : world.mapStorage.loadedDataList) {
			if (o instanceof MapData) {
				MapData map = (MapData) o;
				map.playersHashMap.remove(entityIn);
				for (Iterator<MapData.MapInfo> iter = (Iterator<MapData.MapInfo>) map.playersArrayList.iterator(); iter
						.hasNext();) {
					if (iter.next().player == entityIn) {
						map.mapDecorations.remove(entityIn.getName());
						iter.remove();
					}
				}
			}
		}
	}
	
	public static void updateEntity(Entity entityIn, boolean forceUpdate) {
        if (forceUpdate) {
            ++entityIn.ticksExisted;
            entityIn.inactiveTick();
            return;
        }
	}
	
	public static void addTileEntity(World world, TileEntity tileentity) {
        if (!world.loadedTileEntityList.contains(tileentity))
        	world.addTileEntity(tileentity);
	}
	
	public static void updatePlayersWeather(World world) {
        for (int idx = 0; idx < world.playerEntities.size(); ++idx) {
            if (((EntityPlayerMP) world.playerEntities.get(idx)).world == world) {
                ((EntityPlayerMP) world.playerEntities.get(idx)).tickWeather();
            }
        }
	}
	
	public static boolean handleBlockCanBuildEvent(World world, Block blockIn, BlockPos pos, boolean defaultReturn) {
        BlockCanBuildEvent event = new BlockCanBuildEvent(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), CraftMagicNumbers.getId(blockIn), defaultReturn);
        world.getServer().getPluginManager().callEvent(event);

        return event.isBuildable();
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
                }
                finally {
                	chunk.world.populating = false;
                }
            }
        } finally {
        	// Handle this safer
            BlockSand.fallInstantly = false;
		}
        
        // Populate forge
        chunk.world.getServer().getPluginManager().callEvent(new ChunkPopulateEvent(chunk.bukkitChunk));
        
        if (!AkarinForge.disableForgeGenWorld.contains(chunk.world.getWorldInfo().getWorldName())) {
            GameRegistry.generateWorld(chunk.x, chunk.z, chunk.world, generator, chunk.world.getChunkProvider());
        }
	}
}
