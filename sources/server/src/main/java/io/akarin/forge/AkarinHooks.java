package io.akarin.forge;

import java.io.File;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.v1_12_R1.Main;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginLoadOrder;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.SpigotConfig;
import org.spigotmc.WatchdogThread;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import io.akarin.forge.api.bukkit.I18nManager;
import joptsimple.OptionSet;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.ReportedException;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

public abstract class AkarinHooks {
	private static final Logger LOGGER = LogManager.getLogger("AkarinHooks");
	
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
                return dim1 == 0 || dim2 == 0 ? -1 : // Always set dim 0 to the first
                	  (dim1 < dim2 ? -1 : 1); 
            }
        });
        
        // Prepare worlds array with same size
        server.worlds = new WorldServer[dimIds.length];
        
        for (int index = 0; index < dimIds.length; index++) {
            int dim = dimIds[index];
            
            // Skip not allowed nether or end
            if (dim != 0 && (dim == -1 && !server.getAllowNether() || dim == 1 && !server.server.getAllowEnd()))
                continue;
            
            Environment environment = Environment.getEnvironment(dim);
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
            
            LOGGER.info("Preparing start region for level " + world.getWorldInfo().getDimension() + " (Seed: " + world.getSeed() + ")");
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
                Thread.sleep(wait / 1000000);
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
}
