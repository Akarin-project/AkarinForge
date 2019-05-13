package org.bukkit.craftbukkit.v1_12_R1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.BanList.Type;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;
import org.bukkit.Warning.WarningState;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.advancement.Advancement;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.conversations.Conversable;
import org.bukkit.craftbukkit.v1_12_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_12_R1.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import io.akarin.forge.misc.LogWrapper;
import io.akarin.forge.server.command.ForgeCommandWrapper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.PendingCommand;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.DimensionManager;

/*
 * Temp class dummy impl
 */
public final class CraftServer implements Server {

	private static final Logger LOGGER = LogWrapper.getLogger("Minecraft");
	public CraftScoreboardManager scoreboardManager;

	public CraftServer(MinecraftServer server, PlayerList playerList) {
        Bukkit.setServer(this);
        this.console = MinecraftServer.instance();
        this.commandMap = new SimpleCommandMap(this);
        this.configuration = YamlConfiguration.loadConfiguration(this.getConfigFile());
        this.pluginManager = new SimplePluginManager(this, this.commandMap);
	}
	
    public void registerCommand(ICommand command) {
    	Command wrapper = new ForgeCommandWrapper(command);
        commandMap.register(command.getName().equals("forge") ? "forge" : "mod", wrapper);
    }
    
    public void registerVanillaCommands(ICommandManager manager) {
        Map<String, ICommand> commands = manager.getCommands();
        for (ICommand cmd : commands.values()) {
        	if (cmd.getName().equals("forge"))
        		continue; // We already handled this through internal command registry
        	
            VanillaCommandWrapper wrapper = new VanillaCommandWrapper(cmd, I18n.translateToLocal(cmd.getUsage(null)));
            commandMap.register("minecraft", wrapper);
        }
    }

	@Override
	public void sendPluginMessage(Plugin source, String channel, byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getListeningPluginChannels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBukkitVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends Player> getOnlinePlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getViewDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getIp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWorldType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getGenerateStructures() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getAllowEnd() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getAllowNether() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasWhitelist() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWhitelist(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<OfflinePlayer> getWhitelistedPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reloadWhitelist() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int broadcastMessage(String message) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getUpdateFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getUpdateFolderFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getConnectionThrottle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTicksPerAnimalSpawns() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTicksPerMonsterSpawns() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Player getPlayer(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player getPlayerExact(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Player> matchPlayer(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player getPlayer(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PluginManager getPluginManager() {
		return pluginManager;
	}
	
    private final CraftScheduler scheduler = new CraftScheduler();

    @Override
    public CraftScheduler getScheduler() {
        return this.scheduler;
    }

	@Override
	public ServicesManager getServicesManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<World> getWorlds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public World createWorld(WorldCreator creator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean unloadWorld(String name, boolean save) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unloadWorld(World world, boolean save) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public World getWorld(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public World getWorld(UUID uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapView getMap(short id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapView createMap(World world) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reloadData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public PluginCommand getPluginCommand(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void savePlayers() {
		// TODO Auto-generated method stub
		
	}
	
    protected final MinecraftServer console;

	@Override
	public boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException {
        Validate.notNull((Object)sender, (String)"Sender cannot be null", (Object[])new Object[0]);
        Validate.notNull((Object)commandLine, (String)"CommandLine cannot be null", (Object[])new Object[0]);
        
        if (commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        if (StringUtils.isNotEmpty("unknown"))
        	sender.sendMessage("unknown");

        return false;
	}

	@Override
	public boolean addRecipe(Recipe recipe) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Recipe> getRecipesFor(ItemStack result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Recipe> recipeIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearRecipes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetRecipes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, String[]> getCommandAliases() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSpawnRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSpawnRadius(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getOnlineMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getAllowFlight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHardcore() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int broadcast(String message, String permission) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OfflinePlayer getOfflinePlayer(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OfflinePlayer getOfflinePlayer(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getIPBans() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void banIP(String address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unbanIP(String address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<OfflinePlayer> getBannedPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BanList getBanList(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OfflinePlayer> getOperators() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameMode getDefaultGameMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultGameMode(GameMode mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ConsoleCommandSender getConsoleSender() {
		// TODO Auto-generated method stub
		return null;
	}
	
    private File container;

	@Override
	public File getWorldContainer() {
        if (DimensionManager.getWorld(0) != null) {
            return ((SaveHandler) DimensionManager.getWorld(0).getSaveHandler()).getWorldDirectory();
        }
        
        if (MinecraftServer.instance().anvilFile != null) {
            return MinecraftServer.instance().anvilFile;
        }

        if (container == null) {
            container = new File(configuration.getString("settings.world-container", "."));
        }

        return container;
	}

	@Override
	public OfflinePlayer[] getOfflinePlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Messenger getMessenger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HelpMap getHelpMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory createInventory(InventoryHolder owner, InventoryType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Merchant createMerchant(String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMonsterSpawnLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAnimalSpawnLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWaterAnimalSpawnLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAmbientSpawnLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isPrimaryThread() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getMotd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShutdownMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WarningState getWarningState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemFactory getItemFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScoreboardManager getScoreboardManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CachedServerIcon getServerIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CachedServerIcon loadServerIcon(BufferedImage image) throws IllegalArgumentException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIdleTimeout(int threshold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getIdleTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ChunkData createChunkData(World world) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BossBar createBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity getEntity(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Advancement getAdvancement(NamespacedKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Advancement> advancementIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UnsafeValues getUnsafe() {
		// TODO Auto-generated method stub
		return null;
	}
	
    private File getConfigFile() {
        return (File) MinecraftServer.instance().options.valueOf("bukkit-settings");
    }
    
    private SimplePluginManager pluginManager;
    private YamlConfiguration configuration;
    private SimpleCommandMap commandMap;

	public ChunkGenerator getGenerator(String world) {
		
        String name;
        ConfigurationSection section = this.configuration.getConfigurationSection("worlds");
        ChunkGenerator result = null;
        if (section != null && (section = section.getConfigurationSection(world)) != null && (name = section.getString("generator")) != null && !name.equals("")) {
            String[] split = name.split(":", 2);
            String id2 = split.length > 1 ? split[1] : null;
            Plugin plugin = this.pluginManager.getPlugin(split[0]);
            if (plugin == null) {
                this.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + split[0] + "' does not exist");
            } else if (!plugin.isEnabled()) {
                this.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled yet (is it load:STARTUP?)");
            } else {
                try {
                    result = plugin.getDefaultWorldGenerator(world, id2);
                    if (result == null) {
                        this.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' lacks a default world generator");
                    }
                }
                catch (Throwable t2) {
                    plugin.getLogger().log(Level.SEVERE, "Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName(), t2);
                }
            }
        }
        return result;
	}

	public void enablePlugins(PluginLoadOrder postworld) {
		// TODO Auto-generated method stub
		
	}

	public void disablePlugins() {
		// TODO Auto-generated method stub
		
	}
	
    public boolean playerCommandState;

    public boolean dispatchServerCommand(CommandSender sender, PendingCommand serverCommand) {
        Conversable conversable2;
        if (sender instanceof Conversable && (conversable2 = (Conversable)((Object)sender)).isConversing()) {
            conversable2.acceptConversationInput(serverCommand.command);
            return true;
        }
        try {
            this.playerCommandState = true;
            boolean result = this.dispatchCommand(sender, serverCommand.command);
            return result;
        }
        catch (Exception ex2) {
            this.getLogger().log(Level.WARNING, "Unexpected exception while parsing console command \"" + serverCommand.command + '\"', ex2);
            boolean bl2 = false;
            return bl2;
        }
        finally {
            this.playerCommandState = false;
        }
    }

	@Override
	public Player[] getOnlinePlayers_1710() {
		// TODO Auto-generated method stub
		return null;
	}

	public ICommandSender getServer() {
		return console;
	}

	public void addWorld(CraftWorld world) {
		// TODO Auto-generated method stub
		
	}

	public PlayerList getHandle() {
		// TODO Auto-generated method stub
		return MinecraftServer.instance().getPlayerList();
	}
    
}