/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.base.Function
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Lists
 *  com.google.common.collect.MapMaker
 *  com.mojang.authlib.GameProfile
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.ByteBufOutputStream
 *  io.netty.buffer.Unpooled
 *  io.netty.handler.codec.base64.Base64
 *  jline.console.ConsoleReader
 *  joptsimple.OptionSet
 *  net.md_5.bungee.api.chat.BaseComponent
 *  org.apache.commons.lang3.Validate
 *  org.yaml.snakeyaml.Yaml
 *  org.yaml.snakeyaml.constructor.BaseConstructor
 *  org.yaml.snakeyaml.constructor.SafeConstructor
 *  org.yaml.snakeyaml.error.MarkedYAMLException
 */
package org.bukkit.craftbukkit.v1_12_R1;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.mojang.authlib.GameProfile;

import io.akarin.forge.command.CraftSimpleCommandMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.apache.commons.lang3.Validate;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;
import org.bukkit.Warning;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
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
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.conversations.Conversable;
import org.bukkit.craftbukkit.v1_12_R1.CraftIpBanList;
import org.bukkit.craftbukkit.v1_12_R1.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.CraftProfileBanList;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.Main;
import org.bukkit.craftbukkit.v1_12_R1.boss.CraftBossBar;
import org.bukkit.craftbukkit.v1_12_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.generator.CraftChunkData;
import org.bukkit.craftbukkit.v1_12_R1.help.SimpleHelpMap;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftFurnaceRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMerchantCustom;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapelessRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.RecipeIterator;
import org.bukkit.craftbukkit.v1_12_R1.map.CraftMapView;
import org.bukkit.craftbukkit.v1_12_R1.metadata.EntityMetadataStore;
import org.bukkit.craftbukkit.v1_12_R1.metadata.PlayerMetadataStore;
import org.bukkit.craftbukkit.v1_12_R1.metadata.WorldMetadataStore;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionBrewer;
import org.bukkit.craftbukkit.v1_12_R1.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftIconCache;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.util.DatFileFilter;
import org.bukkit.craftbukkit.v1_12_R1.util.Versioning;
import org.bukkit.craftbukkit.v1_12_R1.util.permissions.CraftDefaultPermissions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.Potion;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.bukkit.util.StringUtil;
import org.bukkit.util.permissions.DefaultPermissions;
import org.spigotmc.RestartCommand;
import org.spigotmc.SpigotConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.MarkedYAMLException;

public final class CraftServer
implements Server {
    private final String serverName = "CatServer";
    private final String serverVersion;
    private final String bukkitVersion = Versioning.getBukkitVersion();
    private final Logger logger = Logger.getLogger("Minecraft");
    private final ServicesManager servicesManager = new SimpleServicesManager();
    private final CraftScheduler scheduler = new CraftScheduler();
    private final CraftSimpleCommandMap craftCommandMap;
    private final SimpleCommandMap commandMap;
    private final SimpleHelpMap helpMap;
    private final StandardMessenger messenger;
    private final SimplePluginManager pluginManager;
    protected final MinecraftServer console;
    protected final PlayerList playerList;
    private final Map<String, World> worlds;
    private YamlConfiguration configuration;
    private YamlConfiguration commandsConfiguration;
    private final Yaml yaml;
    private final Map<UUID, OfflinePlayer> offlinePlayers;
    private final EntityMetadataStore entityMetadata;
    private final PlayerMetadataStore playerMetadata;
    private final WorldMetadataStore worldMetadata;
    private int monsterSpawn;
    private int animalSpawn;
    private int waterAnimalSpawn;
    private int ambientSpawn;
    public int chunkGCPeriod;
    public int chunkGCLoadThresh;
    private File container;
    private Warning.WarningState warningState;
    private final BooleanWrapper online;
    public CraftScoreboardManager scoreboardManager;
    public boolean playerCommandState;
    private boolean printSaveWarning;
    private CraftIconCache icon;
    private boolean overrideAllCommandBlockCommands;
    private boolean unrestrictedAdvancements;
    private final List<CraftPlayer> playerView;
    public int reloadCount;
    private final Server.Spigot spigot;

    public CraftSimpleCommandMap getCraftCommandMap() {
        return this.craftCommandMap;
    }

    public CraftServer(MinecraftServer console, PlayerList playerList) {
        this.craftCommandMap = new CraftSimpleCommandMap(this);
        this.commandMap = new SimpleCommandMap(this);
        this.helpMap = new SimpleHelpMap(this);
        this.messenger = new StandardMessenger();
        this.pluginManager = new SimplePluginManager(this, this.commandMap);
        this.worlds = new LinkedHashMap<String, World>();
        this.yaml = new Yaml((BaseConstructor)new SafeConstructor());
        this.offlinePlayers = new MapMaker().weakValues().makeMap();
        this.entityMetadata = new EntityMetadataStore();
        this.playerMetadata = new PlayerMetadataStore();
        this.worldMetadata = new WorldMetadataStore();
        this.monsterSpawn = -1;
        this.animalSpawn = -1;
        this.waterAnimalSpawn = -1;
        this.ambientSpawn = -1;
        this.chunkGCPeriod = -1;
        this.chunkGCLoadThresh = 0;
        this.warningState = Warning.WarningState.DEFAULT;
        this.online = new BooleanWrapper();
        this.overrideAllCommandBlockCommands = false;
        this.spigot = new Server.Spigot(){

            @Override
            public YamlConfiguration getConfig() {
                return SpigotConfig.config;
            }

            @Override
            public void restart() {
                RestartCommand.restart();
            }

            @Override
            public void broadcast(BaseComponent component) {
                for (Player player : CraftServer.this.getOnlinePlayers()) {
                    player.spigot().sendMessage(component);
                }
            }

            @Override
            public /* varargs */ void broadcast(BaseComponent ... components) {
                for (Player player : CraftServer.this.getOnlinePlayers()) {
                    player.spigot().sendMessage(components);
                }
            }
        };
        this.console = console;
        this.playerList = playerList;
        this.playerView = Collections.unmodifiableList(Lists.transform(playerList.v(), (Function)new Function<oq, CraftPlayer>(){

            public CraftPlayer apply(oq player) {
                return player.getBukkitEntity();
            }
        }));
        this.serverVersion = CraftServer.class.getPackage().getImplementationVersion();
        this.online.value = console.getPropertyManager().a("online-mode", true);
        Bukkit.setServer(this);
        alo.l.getClass();
        Potion.setPotionBrewer(new CraftPotionBrewer());
        vb.o.getClass();
        if (!Main.useConsole) {
            this.getLogger().info("Console input is disabled due to --noconsole command argument");
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.getConfigFile());
        this.configuration.options().copyDefaults(true);
        this.configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("configurations/bukkit.yml"), Charsets.UTF_8)));
        ConfigurationSection legacyAlias = null;
        if (!this.configuration.isString("aliases")) {
            legacyAlias = this.configuration.getConfigurationSection("aliases");
            this.configuration.set("aliases", "now-in-commands.yml");
        }
        this.saveConfig();
        if (this.getCommandsConfigFile().isFile()) {
            legacyAlias = null;
        }
        this.commandsConfiguration = YamlConfiguration.loadConfiguration(this.getCommandsConfigFile());
        this.commandsConfiguration.options().copyDefaults(true);
        this.commandsConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("configurations/commands.yml"), Charsets.UTF_8)));
        this.saveCommandsConfig();
        if (legacyAlias != null) {
            ConfigurationSection aliases = this.commandsConfiguration.createSection("aliases");
            for (String key : legacyAlias.getKeys(false)) {
                ArrayList<String> commands = new ArrayList<String>();
                if (legacyAlias.isList(key)) {
                    for (String command : legacyAlias.getStringList(key)) {
                        commands.add(command + " $1-");
                    }
                } else {
                    commands.add(legacyAlias.getString(key) + " $1-");
                }
                aliases.set(key, commands);
            }
        }
        this.saveCommandsConfig();
        this.overrideAllCommandBlockCommands = this.commandsConfiguration.getStringList("command-block-overrides").contains("*");
        this.unrestrictedAdvancements = this.commandsConfiguration.getBoolean("unrestricted-advancements");
        this.pluginManager.useTimings(this.configuration.getBoolean("settings.plugin-profiling"));
        this.monsterSpawn = this.configuration.getInt("spawn-limits.monsters");
        this.animalSpawn = this.configuration.getInt("spawn-limits.animals");
        this.waterAnimalSpawn = this.configuration.getInt("spawn-limits.water-animals");
        this.ambientSpawn = this.configuration.getInt("spawn-limits.ambient");
        console.autosavePeriod = this.configuration.getInt("ticks-per.autosave");
        this.warningState = Warning.WarningState.value(this.configuration.getString("settings.deprecated-verbose"));
        this.chunkGCPeriod = this.configuration.getInt("chunk-gc.period-in-ticks");
        this.chunkGCLoadThresh = this.configuration.getInt("chunk-gc.load-threshold");
        this.loadIcon();
        CatServer.loadConfig();
    }

    public boolean getPermissionOverride(bn listener) {
        while (listener instanceof bo) {
            listener = ((bo)listener).a;
        }
        return this.unrestrictedAdvancements && listener instanceof l.AdvancementCommandListener;
    }

    public boolean getCommandBlockOverride(String command) {
        return this.overrideAllCommandBlockCommands || this.commandsConfiguration.getStringList("command-block-overrides").contains(command);
    }

    private File getConfigFile() {
        return (File)this.console.options.valueOf("bukkit-settings");
    }

    private File getCommandsConfigFile() {
        return (File)this.console.options.valueOf("commands-settings");
    }

    private void saveConfig() {
        try {
            this.configuration.save(this.getConfigFile());
        }
        catch (IOException ex2) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + this.getConfigFile(), ex2);
        }
    }

    private void saveCommandsConfig() {
        try {
            this.commandsConfiguration.save(this.getCommandsConfigFile());
        }
        catch (IOException ex2) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + this.getCommandsConfigFile(), ex2);
        }
    }

    public void loadPlugins() {
        ReflectionTransformer.init();
        this.pluginManager.registerInterface(JavaPluginLoader.class);
        File pluginFolder = (File)this.console.options.valueOf("plugins");
        if (pluginFolder.exists()) {
            Plugin[] plugins;
            for (Plugin plugin : plugins = this.pluginManager.loadPlugins(pluginFolder)) {
                try {
                    String message = String.format("Loading %s", plugin.getDescription().getFullName());
                    plugin.getLogger().info(message);
                    plugin.onLoad();
                    continue;
                }
                catch (Throwable ex2) {
                    Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex2.getMessage() + " initializing " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex2);
                }
            }
        } else {
            pluginFolder.mkdir();
        }
    }

    public void enablePlugins(PluginLoadOrder type) {
        Plugin[] plugins;
        if (type == PluginLoadOrder.STARTUP) {
            this.helpMap.clear();
            this.helpMap.initializeGeneralTopics();
        }
        for (Plugin plugin : plugins = this.pluginManager.getPlugins()) {
            if (plugin.isEnabled() || plugin.getDescription().getLoad() != type) continue;
            this.enablePlugin(plugin);
        }
        if (type == PluginLoadOrder.POSTWORLD) {
            this.setVanillaCommands(true);
            this.commandMap.setFallbackCommands();
            this.setVanillaCommands(false);
            this.commandMap.registerServerAliases();
            this.loadCustomPermissions();
            DefaultPermissions.registerCorePermissions();
            CraftDefaultPermissions.registerCorePermissions();
            this.helpMap.initializeCommands();
        }
    }

    public void disablePlugins() {
        this.pluginManager.disablePlugins();
    }

    private void setVanillaCommands(boolean first) {
        Map<String, bk> commands = this.console.N().b();
        for (bk cmd : commands.values()) {
            VanillaCommandWrapper wrapper = new VanillaCommandWrapper((bi)cmd, ft.a(cmd.b(null)));
            if (SpigotConfig.replaceCommands.contains(wrapper.getName())) {
                if (!first) continue;
                this.commandMap.register("minecraft", wrapper);
                continue;
            }
            if (first) continue;
            this.commandMap.register("minecraft", wrapper);
        }
    }

    private void enablePlugin(Plugin plugin) {
        try {
            List<Permission> perms = plugin.getDescription().getPermissions();
            for (Permission perm : perms) {
                try {
                    this.pluginManager.addPermission(perm, false);
                }
                catch (IllegalArgumentException ex2) {
                    this.getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName() + " tried to register permission '" + perm.getName() + "' but it's already registered", ex2);
                }
            }
            this.pluginManager.dirtyPermissibles();
            this.pluginManager.enablePlugin(plugin);
        }
        catch (Throwable ex3) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex3.getMessage() + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex3);
        }
    }

    @Override
    public String getName() {
        return "CatServer";
    }

    @Override
    public String getVersion() {
        return this.serverVersion + " (MC: " + this.console.G() + ")";
    }

    @Override
    public String getBukkitVersion() {
        return this.bukkitVersion;
    }

    public List<CraftPlayer> getOnlinePlayers() {
        return this.playerView;
    }

    @Override
    public Player[] getOnlinePlayers_1710() {
        return this.getOnlinePlayers().toArray(new Player[0]);
    }

    @Deprecated
    @Override
    public Player getPlayer(String name) {
        Validate.notNull((Object)name, (String)"Name cannot be null", (Object[])new Object[0]);
        Player found = this.getPlayerExact(name);
        if (found != null) {
            return found;
        }
        String lowerName = name.toLowerCase(Locale.ENGLISH);
        int delta = Integer.MAX_VALUE;
        for (Player player : this.getOnlinePlayers()) {
            if (!player.getName().toLowerCase(Locale.ENGLISH).startsWith(lowerName)) continue;
            int curDelta = Math.abs(player.getName().length() - lowerName.length());
            if (curDelta < delta) {
                found = player;
                delta = curDelta;
            }
            if (curDelta != 0) continue;
            break;
        }
        return found;
    }

    @Deprecated
    @Override
    public Player getPlayerExact(String name) {
        Validate.notNull((Object)name, (String)"Name cannot be null", (Object[])new Object[0]);
        oq player = this.playerList.a(name);
        return player != null ? player.getBukkitEntity() : null;
    }

    @Override
    public Player getPlayer(UUID id2) {
        oq player = this.playerList.a(id2);
        if (player != null) {
            return player.getBukkitEntity();
        }
        return null;
    }

    @Override
    public int broadcastMessage(String message) {
        return this.broadcast(message, "bukkit.broadcast.user");
    }

    public Player getPlayer(oq entity) {
        return entity.getBukkitEntity();
    }

    @Deprecated
    @Override
    public List<Player> matchPlayer(String partialName) {
        Validate.notNull((Object)partialName, (String)"PartialName cannot be null", (Object[])new Object[0]);
        ArrayList<Player> matchedPlayers = new ArrayList<Player>();
        for (Player iterPlayer : this.getOnlinePlayers()) {
            String iterPlayerName = iterPlayer.getName();
            if (partialName.equalsIgnoreCase(iterPlayerName)) {
                matchedPlayers.clear();
                matchedPlayers.add(iterPlayer);
                break;
            }
            if (!iterPlayerName.toLowerCase(Locale.ENGLISH).contains(partialName.toLowerCase(Locale.ENGLISH))) continue;
            matchedPlayers.add(iterPlayer);
        }
        return matchedPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return this.playerList.p();
    }

    @Override
    public int getPort() {
        return this.getConfigInt("server-port", 25565);
    }

    @Override
    public int getViewDistance() {
        return this.getConfigInt("view-distance", 10);
    }

    @Override
    public String getIp() {
        return this.getConfigString("server-ip", "");
    }

    @Override
    public String getServerName() {
        return this.getConfigString("server-name", "Unknown Server");
    }

    @Override
    public String getServerId() {
        return this.getConfigString("server-id", "unnamed");
    }

    @Override
    public String getWorldType() {
        return this.getConfigString("level-type", "DEFAULT");
    }

    @Override
    public boolean getGenerateStructures() {
        return this.getConfigBoolean("generate-structures", true);
    }

    @Override
    public boolean getAllowEnd() {
        return this.configuration.getBoolean("settings.allow-end");
    }

    @Override
    public boolean getAllowNether() {
        return this.getConfigBoolean("allow-nether", true);
    }

    public boolean getWarnOnOverload() {
        return this.configuration.getBoolean("settings.warn-on-overload");
    }

    public boolean getQueryPlugins() {
        return this.configuration.getBoolean("settings.query-plugins");
    }

    @Override
    public boolean hasWhitelist() {
        return this.getConfigBoolean("white-list", false);
    }

    private String getConfigString(String variable, String defaultValue) {
        return this.console.getPropertyManager().a(variable, defaultValue);
    }

    private int getConfigInt(String variable, int defaultValue) {
        return this.console.getPropertyManager().a(variable, defaultValue);
    }

    private boolean getConfigBoolean(String variable, boolean defaultValue) {
        return this.console.getPropertyManager().a(variable, defaultValue);
    }

    @Override
    public String getUpdateFolder() {
        return this.configuration.getString("settings.update-folder", "update");
    }

    @Override
    public File getUpdateFolderFile() {
        return new File((File)this.console.options.valueOf("plugins"), this.configuration.getString("settings.update-folder", "update"));
    }

    @Override
    public long getConnectionThrottle() {
        if (SpigotConfig.bungee) {
            return -1;
        }
        return this.configuration.getInt("settings.connection-throttle");
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        return this.configuration.getInt("ticks-per.animal-spawns");
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        return this.configuration.getInt("ticks-per.monster-spawns");
    }

    @Override
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    public CraftScheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public ServicesManager getServicesManager() {
        return this.servicesManager;
    }

    @Override
    public List<World> getWorlds() {
        return new ArrayList<World>(this.worlds.values());
    }

    public PlayerList getHandle() {
        return this.playerList;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean dispatchServerCommand(CommandSender sender, nk serverCommand) {
        Conversable conversable2;
        if (sender instanceof Conversable && (conversable2 = (Conversable)((Object)sender)).isConversing()) {
            conversable2.acceptConversationInput(serverCommand.a);
            return true;
        }
        try {
            this.playerCommandState = true;
            boolean conversable2 = this.dispatchCommand(sender, serverCommand.a);
            return conversable2;
        }
        catch (Exception ex2) {
            this.getLogger().log(Level.WARNING, "Unexpected exception while parsing console command \"" + serverCommand.a + '\"', ex2);
            boolean bl2 = false;
            return bl2;
        }
        finally {
            this.playerCommandState = false;
        }
    }

    @Override
    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        Validate.notNull((Object)sender, (String)"Sender cannot be null", (Object[])new Object[0]);
        Validate.notNull((Object)commandLine, (String)"CommandLine cannot be null", (Object[])new Object[0]);
        if (this.commandMap.dispatch(sender, commandLine)) {
            return true;
        }
        if (sender instanceof ConsoleCommandSender) {
            this.craftCommandMap.setVanillaConsoleSender(this.console);
        }
        return this.dispatchVanillaCommand(sender, commandLine);
    }

    public boolean dispatchVanillaCommand(CommandSender sender, String commandLine) {
        if (this.craftCommandMap.dispatch(sender, commandLine)) {
            return true;
        }
        sender.sendMessage(SpigotConfig.unknownCommandMessage);
        return false;
    }

    @Override
    public void reload() {
    }

    @Override
    public void reloadData() {
        this.console.aM();
    }

    private void loadIcon() {
        this.icon = new CraftIconCache(null);
        try {
            File file = new File(new File("."), "server-icon.png");
            if (file.isFile()) {
                this.icon = CraftServer.loadServerIcon0(file);
            }
        }
        catch (Exception ex2) {
            this.getLogger().log(Level.WARNING, "Couldn't load server icon", ex2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void loadCustomPermissions() {
        Map perms;
        FileInputStream stream;
        File file = new File(this.configuration.getString("settings.permissions-file"));
        try {
            stream = new FileInputStream(file);
        }
        catch (FileNotFoundException ex2) {
            try {
                file.createNewFile();
                return;
            }
            catch (Throwable throwable) {
                return;
            }
        }
        try {
            perms = (Map)this.yaml.load((InputStream)stream);
        }
        catch (MarkedYAMLException ex3) {
            this.getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML: " + ex3.toString());
            return;
        }
        catch (Throwable ex4) {
            try {
                this.getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML.", ex4);
            }
            catch (Throwable throwable) {}
            try {
                stream.close();
                return;
            }
            catch (IOException iOException) {
                // empty catch block
            }
            return;
            throw throwable;
            finally {
                try {
                    stream.close();
                }
                catch (IOException iOException) {}
            }
        }
        if (perms == null) {
            this.getLogger().log(Level.INFO, "Server permissions file " + file + " is empty, ignoring it");
            return;
        }
        List<Permission> permsList = Permission.loadPermissions(perms, "Permission node '%s' in " + file + " is invalid", Permission.DEFAULT_PERMISSION);
        Iterator<Permission> iterator = permsList.iterator();
        while (iterator.hasNext()) {
            Permission perm = iterator.next();
            try {
                this.pluginManager.addPermission(perm);
            }
            catch (IllegalArgumentException ex5) {
                this.getLogger().log(Level.SEVERE, "Permission in " + file + " was already defined", ex5);
                continue;
            }
        }
    }

    public String toString() {
        return "CraftServer{serverName=CatServer,serverVersion=" + this.serverVersion + ",minecraftVersion=" + this.console.G() + '}';
    }

    public World createWorld(String name, World.Environment environment) {
        return WorldCreator.name(name).environment(environment).createWorld();
    }

    public World createWorld(String name, World.Environment environment, long seed) {
        return WorldCreator.name(name).environment(environment).seed(seed).createWorld();
    }

    public World createWorld(String name, World.Environment environment, ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).generator(generator).createWorld();
    }

    public World createWorld(String name, World.Environment environment, long seed, ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).seed(seed).generator(generator).createWorld();
    }

    @Override
    public World createWorld(WorldCreator creator) {
        Validate.notNull((Object)creator, (String)"Creator may not be null", (Object[])new Object[0]);
        String name = creator.name();
        ChunkGenerator generator = creator.generator();
        File folder = new File(this.getWorldContainer(), name);
        World world = this.getWorld(name);
        amz type = amz.a(creator.type().getName());
        boolean generateStructures = creator.generateStructures();
        if (folder.exists() && !folder.isDirectory()) {
            throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
        }
        if (world != null) {
            return world;
        }
        boolean hardcore = false;
        amx worldSettings = new amx(creator.seed(), amx.a(this.getDefaultGameMode().getValue()), generateStructures, hardcore, type);
        oo worldserver = DimensionManager.initDimension(creator, worldSettings);
        this.pluginManager.callEvent(new WorldInitEvent(worldserver.getWorld()));
        this.logger.info("Preparing start region for level " + (this.console.worldServerList.size() - 1) + " (Dimension: " + worldserver.s.getDimension() + ", Seed: " + worldserver.Q() + ")");
        if (worldserver.getWorld().getKeepSpawnInMemory()) {
            int short1 = 196;
            long i2 = System.currentTimeMillis();
            for (int j2 = - short1; j2 <= short1; j2 += 16) {
                for (int k2 = - short1; k2 <= short1; k2 += 16) {
                    long l2 = System.currentTimeMillis();
                    if (l2 < i2) {
                        i2 = l2;
                    }
                    if (l2 > i2 + 1000) {
                        int i1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                        int j1 = (j2 + short1) * (short1 * 2 + 1) + k2 + 1;
                        this.logger.info("Preparing spawn area for " + worldserver.getWorld().getName() + ", " + j1 * 100 / i1 + "%");
                        i2 = l2;
                    }
                    et pos = worldserver.T();
                    worldserver.r().b(pos.p() + j2 >> 4, pos.r() + k2 >> 4);
                }
            }
        }
        this.pluginManager.callEvent(new WorldLoadEvent(worldserver.getWorld()));
        return worldserver.getWorld();
    }

    @Override
    public boolean unloadWorld(String name, boolean save) {
        return this.unloadWorld(this.getWorld(name), save);
    }

    @Override
    public boolean unloadWorld(World world, boolean save) {
        if (world == null) {
            return false;
        }
        oo handle = ((CraftWorld)world).getHandle();
        if (!this.console.worldServerList.contains(handle)) {
            return false;
        }
        if (handle.dimension == 0) {
            return false;
        }
        if (handle.i.size() > 0) {
            return false;
        }
        WorldUnloadEvent e2 = new WorldUnloadEvent(handle.getWorld());
        this.pluginManager.callEvent(e2);
        if (e2.isCancelled()) {
            return false;
        }
        if (save) {
            try {
                handle.a(true, null);
                handle.s();
            }
            catch (amv ex2) {
                this.getLogger().log(Level.SEVERE, null, ex2);
            }
        }
        MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(handle));
        this.worlds.remove(world.getName().toLowerCase(Locale.ENGLISH));
        DimensionManager.setWorld(handle.s.getDimension(), null, FMLCommonHandler.instance().getMinecraftServerInstance());
        return true;
    }

    public MinecraftServer getServer() {
        return this.console;
    }

    @Override
    public World getWorld(String name) {
        Validate.notNull((Object)name, (String)"Name cannot be null", (Object[])new Object[0]);
        World world = this.worlds.get(name.toLowerCase(Locale.ENGLISH));
        if (world == null && name.toUpperCase().startsWith("DIM")) {
            try {
                int dimension = Integer.valueOf(name.substring(3));
                oo worldserver = this.console.a(dimension);
                if (worldserver != null) {
                    world = worldserver.getWorld();
                }
            }
            catch (NumberFormatException worldserver) {
                // empty catch block
            }
        }
        return world;
    }

    @Override
    public World getWorld(UUID uid) {
        for (World world : this.worlds.values()) {
            if (!world.getUID().equals(uid)) continue;
            return world;
        }
        return null;
    }

    public void addWorld(World world) {
        if (this.getWorld(world.getUID()) != null) {
            System.out.println("World " + world.getName() + " is a duplicate of another world and has been prevented from loading. Please delete the uid.dat file from " + world.getName() + "'s world directory if you want to be able to load the duplicate world.");
            return;
        }
        this.worlds.put(world.getName().toLowerCase(Locale.ENGLISH), world);
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    public ConsoleReader getReader() {
        return this.console.reader;
    }

    @Override
    public PluginCommand getPluginCommand(String name) {
        Command command = this.commandMap.getCommand(name);
        if (command instanceof PluginCommand) {
            return (PluginCommand)command;
        }
        return null;
    }

    @Override
    public void savePlayers() {
        this.checkSaveState();
        this.playerList.j();
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        CraftRecipe toAdd;
        if (recipe instanceof CraftRecipe) {
            toAdd = (CraftRecipe)recipe;
        } else if (recipe instanceof ShapedRecipe) {
            toAdd = CraftShapedRecipe.fromBukkitRecipe((ShapedRecipe)recipe);
        } else if (recipe instanceof ShapelessRecipe) {
            toAdd = CraftShapelessRecipe.fromBukkitRecipe((ShapelessRecipe)recipe);
        } else if (recipe instanceof FurnaceRecipe) {
            toAdd = CraftFurnaceRecipe.fromBukkitRecipe((FurnaceRecipe)recipe);
        } else {
            return false;
        }
        toAdd.addToCraftingManager();
        return true;
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack result) {
        Validate.notNull((Object)result, (String)"Result cannot be null", (Object[])new Object[0]);
        ArrayList<Recipe> results = new ArrayList<Recipe>();
        Iterator<Recipe> iter = this.recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            ItemStack stack = recipe.getResult();
            if (stack.getType() != result.getType() || result.getDurability() != -1 && result.getDurability() != stack.getDurability()) continue;
            results.add(recipe);
        }
        return results;
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        return new RecipeIterator();
    }

    @Override
    public void clearRecipes() {
        aku.a = new fh();
        akp.a().b.clear();
        akp.a().customRecipes.clear();
        akp.a().customExperience.clear();
    }

    @Override
    public void resetRecipes() {
        aku.a = new fh();
        aku.a();
        akp.a().b = new akp().b;
        akp.a().customRecipes.clear();
        akp.a().customExperience.clear();
    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        ConfigurationSection section = this.commandsConfiguration.getConfigurationSection("aliases");
        LinkedHashMap<String, String[]> result = new LinkedHashMap<String, String[]>();
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ImmutableList commands = section.isList(key) ? section.getStringList(key) : ImmutableList.of((Object)section.getString(key));
                result.put(key, commands.toArray(new String[commands.size()]));
            }
        }
        return result;
    }

    public void removeBukkitSpawnRadius() {
        this.configuration.set("settings.spawn-radius", null);
        this.saveConfig();
    }

    public int getBukkitSpawnRadius() {
        return this.configuration.getInt("settings.spawn-radius", -1);
    }

    @Override
    public String getShutdownMessage() {
        return this.configuration.getString("settings.shutdown-message");
    }

    @Override
    public int getSpawnRadius() {
        return ((nz)this.console).q.a("spawn-protection", 16);
    }

    @Override
    public void setSpawnRadius(int value) {
        this.configuration.set("settings.spawn-radius", value);
        this.saveConfig();
    }

    @Override
    public boolean getOnlineMode() {
        return this.online.value;
    }

    @Override
    public boolean getAllowFlight() {
        return this.console.ah();
    }

    @Override
    public boolean isHardcore() {
        return this.console.p();
    }

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

    @Deprecated
    @Override
    public CraftMapView getMap(short id2) {
        bfm collection = this.console.d[0].z;
        bev worldmap = (bev)collection.a(bev.class, "map_" + id2);
        if (worldmap == null) {
            return null;
        }
        return worldmap.mapView;
    }

    @Override
    public CraftMapView createMap(World world) {
        Validate.notNull((Object)world, (String)"World cannot be null", (Object[])new Object[0]);
        aip stack = new aip(air.cg, 1, -1);
        bev worldmap = air.bl.a(stack, ((CraftWorld)world).getHandle());
        return worldmap.mapView;
    }

    @Override
    public void shutdown() {
        this.console.x();
    }

    @Override
    public int broadcast(String message, String permission) {
        HashSet<CommandSender> recipients = new HashSet<CommandSender>();
        for (Permissible permissible : this.getPluginManager().getPermissionSubscriptions(permission)) {
            if (!(permissible instanceof CommandSender) || !permissible.hasPermission(permission)) continue;
            recipients.add((CommandSender)permissible);
        }
        BroadcastMessageEvent broadcastMessageEvent = new BroadcastMessageEvent(message, recipients);
        this.getPluginManager().callEvent(broadcastMessageEvent);
        if (broadcastMessageEvent.isCancelled()) {
            return 0;
        }
        message = broadcastMessageEvent.getMessage();
        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }
        return recipients.size();
    }

    @Deprecated
    @Override
    public OfflinePlayer getOfflinePlayer(String name) {
        Validate.notNull((Object)name, (String)"Name cannot be null", (Object[])new Object[0]);
        OfflinePlayer result = this.getPlayerExact(name);
        if (result == null) {
            GameProfile profile = this.console.aB().a(name);
            result = profile == null ? this.getOfflinePlayer(new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)), name)) : this.getOfflinePlayer(profile);
        } else {
            this.offlinePlayers.remove(result.getUniqueId());
        }
        return result;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID id2) {
        Validate.notNull((Object)id2, (String)"UUID cannot be null", (Object[])new Object[0]);
        OfflinePlayer result = this.getPlayer(id2);
        if (result == null) {
            result = this.offlinePlayers.get(id2);
            if (result == null) {
                result = new CraftOfflinePlayer(this, new GameProfile(id2, null));
                this.offlinePlayers.put(id2, result);
            }
        } else {
            this.offlinePlayers.remove(id2);
        }
        return result;
    }

    public OfflinePlayer getOfflinePlayer(GameProfile profile) {
        CraftOfflinePlayer player = new CraftOfflinePlayer(this, profile);
        this.offlinePlayers.put(profile.getId(), player);
        return player;
    }

    @Override
    public Set<String> getIPBans() {
        return new HashSet<String>(Arrays.asList(this.playerList.i().a()));
    }

    @Override
    public void banIP(String address) {
        Validate.notNull((Object)address, (String)"Address cannot be null.", (Object[])new Object[0]);
        this.getBanList(BanList.Type.IP).addBan(address, null, null, null);
    }

    @Override
    public void unbanIP(String address) {
        Validate.notNull((Object)address, (String)"Address cannot be null.", (Object[])new Object[0]);
        this.getBanList(BanList.Type.IP).pardon(address);
    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        HashSet<OfflinePlayer> result = new HashSet<OfflinePlayer>();
        for (po entry : this.playerList.h().getValuesCB()) {
            result.add(this.getOfflinePlayer((GameProfile)entry.f()));
        }
        return result;
    }

    @Override
    public BanList getBanList(BanList.Type type) {
        Validate.notNull((Object)((Object)type), (String)"Type cannot be null", (Object[])new Object[0]);
        switch (type) {
            case IP: {
                return new CraftIpBanList(this.playerList.i());
            }
        }
        return new CraftProfileBanList(this.playerList.h());
    }

    @Override
    public void setWhitelist(boolean value) {
        this.playerList.a(value);
        this.console.getPropertyManager().a("white-list", (Object)value);
    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        LinkedHashSet<OfflinePlayer> result = new LinkedHashSet<OfflinePlayer>();
        for (po entry : this.playerList.k().getValuesCB()) {
            result.add(this.getOfflinePlayer((GameProfile)entry.f()));
        }
        return result;
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        HashSet<OfflinePlayer> result = new HashSet<OfflinePlayer>();
        for (po entry : this.playerList.m().getValuesCB()) {
            result.add(this.getOfflinePlayer((GameProfile)entry.f()));
        }
        return result;
    }

    @Override
    public void reloadWhitelist() {
        this.playerList.a();
    }

    @Override
    public GameMode getDefaultGameMode() {
        return GameMode.getByValue(this.console.d[0].V().q().a());
    }

    @Override
    public void setDefaultGameMode(GameMode mode) {
        Validate.notNull((Object)((Object)mode), (String)"Mode cannot be null", (Object[])new Object[0]);
        for (World world : this.getWorlds()) {
            ((CraftWorld)world).getHandle().x.a(ams.a(mode.getValue()));
        }
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return this.console.console;
    }

    public EntityMetadataStore getEntityMetadata() {
        return this.entityMetadata;
    }

    public PlayerMetadataStore getPlayerMetadata() {
        return this.playerMetadata;
    }

    public WorldMetadataStore getWorldMetadata() {
        return this.worldMetadata;
    }

    @Override
    public File getWorldContainer() {
        if (DimensionManager.getWorld(0) != null) {
            return ((bfb)DimensionManager.getWorld(0).U()).b();
        }
        if (this.getServer().n != null) {
            return this.getServer().n;
        }
        if (this.container == null) {
            this.container = new File(this.configuration.getString("settings.world-container", "."));
        }
        return this.container;
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        bfb storage = (bfb)this.console.d[0].U();
        String[] files = storage.getPlayerDir().list(new DatFileFilter());
        HashSet<OfflinePlayer> players = new HashSet<OfflinePlayer>();
        for (String file : files) {
            try {
                players.add(this.getOfflinePlayer(UUID.fromString(file.substring(0, file.length() - 4))));
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        players.addAll(this.getOnlinePlayers());
        return players.toArray(new OfflinePlayer[players.size()]);
    }

    @Override
    public Messenger getMessenger() {
        return this.messenger;
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(this.getMessenger(), source, channel, message);
        for (Player player : this.getOnlinePlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        HashSet<String> result = new HashSet<String>();
        for (Player player : this.getOnlinePlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }
        return result;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return new CraftInventoryCustom(owner, type);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        return new CraftInventoryCustom(owner, type, title);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        Validate.isTrue((boolean)(size % 9 == 0), (String)"Chests must have a size that is a multiple of 9!", (Object[])new Object[0]);
        return new CraftInventoryCustom(owner, size);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        Validate.isTrue((boolean)(size % 9 == 0), (String)"Chests must have a size that is a multiple of 9!", (Object[])new Object[0]);
        return new CraftInventoryCustom(owner, size, title);
    }

    @Override
    public Merchant createMerchant(String title) {
        return new CraftMerchantCustom(title == null ? InventoryType.MERCHANT.getDefaultTitle() : title);
    }

    @Override
    public HelpMap getHelpMap() {
        return this.helpMap;
    }

    public SimpleCommandMap getCommandMap() {
        return this.commandMap;
    }

    @Override
    public int getMonsterSpawnLimit() {
        return this.monsterSpawn;
    }

    @Override
    public int getAnimalSpawnLimit() {
        return this.animalSpawn;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return this.waterAnimalSpawn;
    }

    @Override
    public int getAmbientSpawnLimit() {
        return this.ambientSpawn;
    }

    @Override
    public boolean isPrimaryThread() {
        return Thread.currentThread().equals(this.console.primaryThread);
    }

    @Override
    public String getMotd() {
        return this.console.aj();
    }

    @Override
    public Warning.WarningState getWarningState() {
        return this.warningState;
    }

    public List<String> tabComplete(bn sender, String message, et pos, boolean forceCommand) {
        if (!(SpigotConfig.tabComplete >= 0 && message.length() > SpigotConfig.tabComplete || message.contains(" "))) {
            return ImmutableList.of();
        }
        if (!(sender instanceof oq)) {
            return ImmutableList.of();
        }
        CraftPlayer player = ((oq)sender).getBukkitEntity();
        List<String> offers = message.startsWith("/") || forceCommand ? this.tabCompleteCommand(player, message, pos) : this.tabCompleteChat(player, message);
        TabCompleteEvent tabEvent = new TabCompleteEvent(player, message, offers);
        this.getPluginManager().callEvent(tabEvent);
        return tabEvent.isCancelled() ? Collections.EMPTY_LIST : tabEvent.getCompletions();
    }

    public List<String> tabCompleteCommand(Player player, String message, et pos) {
        ImmutableList completions = null;
        try {
            if (message.startsWith("/")) {
                message = message.substring(1);
            }
            completions = pos == null ? this.getCommandMap().tabComplete(player, message) : this.getCommandMap().tabComplete(player, message, new Location(player.getWorld(), pos.p(), pos.q(), pos.r()));
        }
        catch (CommandException ex2) {
            player.sendMessage((Object)((Object)ChatColor.RED) + "An internal error occurred while attempting to tab-complete this command");
            this.getLogger().log(Level.SEVERE, "Exception when " + player.getName() + " attempted to tab complete " + message, ex2);
        }
        return completions == null ? ImmutableList.of() : completions;
    }

    public List<String> tabCompleteChat(Player player, String message) {
        ArrayList<String> completions = new ArrayList<String>();
        PlayerChatTabCompleteEvent event = new PlayerChatTabCompleteEvent(player, message, completions);
        String token = event.getLastToken();
        for (Player p2 : this.getOnlinePlayers()) {
            if (!player.canSee(p2) || !StringUtil.startsWithIgnoreCase(p2.getName(), token)) continue;
            completions.add(p2.getName());
        }
        this.pluginManager.callEvent(event);
        Iterator<String> it2 = completions.iterator();
        while (it2.hasNext()) {
            String current = it2.next();
            if (current instanceof String) continue;
            it2.remove();
        }
        Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
        return completions;
    }

    @Override
    public CraftItemFactory getItemFactory() {
        return CraftItemFactory.instance();
    }

    @Override
    public CraftScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    public void checkSaveState() {
        if (this.playerCommandState || this.printSaveWarning || this.console.autosavePeriod <= 0) {
            return;
        }
        this.printSaveWarning = true;
        this.getLogger().log(Level.WARNING, "A manual (plugin-induced) save has been detected while server is configured to auto-save. This may affect performance.", this.warningState == Warning.WarningState.ON ? new Throwable() : null);
    }

    @Override
    public CraftIconCache getServerIcon() {
        return this.icon;
    }

    @Override
    public CraftIconCache loadServerIcon(File file) throws Exception {
        Validate.notNull((Object)file, (String)"File cannot be null", (Object[])new Object[0]);
        if (!file.isFile()) {
            throw new IllegalArgumentException(file + " is not a file");
        }
        return CraftServer.loadServerIcon0(file);
    }

    static CraftIconCache loadServerIcon0(File file) throws Exception {
        return CraftServer.loadServerIcon0(ImageIO.read(file));
    }

    @Override
    public CraftIconCache loadServerIcon(BufferedImage image) throws Exception {
        Validate.notNull((Object)image, (String)"Image cannot be null", (Object[])new Object[0]);
        return CraftServer.loadServerIcon0(image);
    }

    static CraftIconCache loadServerIcon0(BufferedImage image) throws Exception {
        ByteBuf bytebuf = Unpooled.buffer();
        Validate.isTrue((boolean)(image.getWidth() == 64), (String)"Must be 64 pixels wide", (Object[])new Object[0]);
        Validate.isTrue((boolean)(image.getHeight() == 64), (String)"Must be 64 pixels high", (Object[])new Object[0]);
        ImageIO.write((RenderedImage)image, "PNG", (OutputStream)new ByteBufOutputStream(bytebuf));
        ByteBuf bytebuf1 = Base64.encode((ByteBuf)bytebuf);
        return new CraftIconCache("data:image/png;base64," + bytebuf1.toString(Charsets.UTF_8));
    }

    @Override
    public void setIdleTimeout(int threshold) {
        this.console.d(threshold);
    }

    @Override
    public int getIdleTimeout() {
        return this.console.ax();
    }

    @Override
    public ChunkGenerator.ChunkData createChunkData(World world) {
        return new CraftChunkData(world);
    }

    @Override
    public /* varargs */ BossBar createBossBar(String title, BarColor color, BarStyle style, BarFlag ... flags) {
        return new CraftBossBar(title, color, style, flags);
    }

    @Override
    public Entity getEntity(UUID uuid) {
        Validate.notNull((Object)uuid, (String)"UUID cannot be null", (Object[])new Object[0]);
        vg entity = this.console.a(uuid);
        return entity == null ? null : entity.getBukkitEntity();
    }

    @Override
    public Advancement getAdvancement(NamespacedKey key) {
        Preconditions.checkArgument((boolean)(key != null), (Object)"key");
        i advancement = this.console.aK().a(CraftNamespacedKey.toMinecraft(key));
        return advancement == null ? null : advancement.bukkit;
    }

    @Override
    public Iterator<Advancement> advancementIterator() {
        return Iterators.unmodifiableIterator((Iterator)Iterators.transform(this.console.aK().c().iterator(), (Function)new Function<i, Advancement>(){

            public Advancement apply(i advancement) {
                return advancement.bukkit;
            }
        }));
    }

    @Deprecated
    @Override
    public UnsafeValues getUnsafe() {
        return CraftMagicNumbers.INSTANCE;
    }

    @Override
    public Server.Spigot spigot() {
        return this.spigot;
    }

    static {
        ConfigurationSerialization.registerClass(CraftOfflinePlayer.class);
        CraftItemFactory.instance();
    }

    private final class BooleanWrapper {
        private boolean value;

        private BooleanWrapper() {
            this.value = true;
        }
    }

}

