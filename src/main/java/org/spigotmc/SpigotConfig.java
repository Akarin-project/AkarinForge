/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Throwables
 *  gnu.trove.map.hash.TObjectIntHashMap
 *  org.apache.logging.log4j.Level
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.core.LoggerContext
 *  org.apache.logging.log4j.core.config.Configuration
 *  org.apache.logging.log4j.core.config.LoggerConfig
 */
package org.spigotmc;

import com.google.common.base.Throwables;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.craftbukkit.CraftServer;
import org.spigotmc.RestartCommand;
import org.spigotmc.SpigotCommand;
import org.spigotmc.TicksPerSecondCommand;
import org.spigotmc.WatchdogThread;

public class SpigotConfig {
    private static File CONFIG_FILE;
    private static final String HEADER = "This is the main configuration file for Spigot.\nAs you can see, there's tons to configure. Some options may impact gameplay, so use\nwith caution, and make sure you know what each option does before configuring.\nFor a reference for any variable inside this file, check out the Spigot wiki at\nhttp://www.spigotmc.org/wiki/spigot-configuration/\n\nIf you need help with the configuration or have any questions related to Spigot,\njoin us at the IRC or drop by our forums and leave a post.\n\nIRC: #spigot @ irc.spi.gt ( http://www.spigotmc.org/pages/irc/ )\nForums: http://www.spigotmc.org/\n";
    public static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    public static boolean logCommands;
    public static int tabComplete;
    public static String whitelistMessage;
    public static String unknownCommandMessage;
    public static String serverFullMessage;
    public static String outdatedClientMessage;
    public static String outdatedServerMessage;
    public static int timeoutTime;
    public static boolean restartOnCrash;
    public static String restartScript;
    public static String restartMessage;
    public static boolean bungee;
    public static boolean lateBind;
    public static boolean disableStatSaving;
    public static TObjectIntHashMap<String> forcedStats;
    public static int playerSample;
    public static int playerShuffle;
    public static List<String> spamExclusions;
    public static boolean silentCommandBlocks;
    public static boolean filterCreativeItems;
    public static Set<String> replaceCommands;
    public static int userCacheCap;
    public static boolean saveUserCacheOnStopOnly;
    public static int intCacheLimit;
    public static double movedWronglyThreshold;
    public static double movedTooQuicklyMultiplier;
    public static double maxHealth;
    public static double movementSpeed;
    public static double attackDamage;
    public static boolean debug;
    public static int itemDirtyTicks;
    public static boolean disableAdvancementSaving;
    public static List<String> disabledAdvancements;

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        }
        catch (IOException iOException) {
        }
        catch (InvalidConfigurationException ex2) {
            Bukkit.getLogger().log(java.util.logging.Level.SEVERE, "Could not load spigot.yml, please correct your syntax errors", ex2);
            throw Throwables.propagate((Throwable)ex2);
        }
        config.options().header("This is the main configuration file for Spigot.\nAs you can see, there's tons to configure. Some options may impact gameplay, so use\nwith caution, and make sure you know what each option does before configuring.\nFor a reference for any variable inside this file, check out the Spigot wiki at\nhttp://www.spigotmc.org/wiki/spigot-configuration/\n\nIf you need help with the configuration or have any questions related to Spigot,\njoin us at the IRC or drop by our forums and leave a post.\n\nIRC: #spigot @ irc.spi.gt ( http://www.spigotmc.org/pages/irc/ )\nForums: http://www.spigotmc.org/\n");
        config.options().copyDefaults(true);
        commands = new HashMap<String, Command>();
        commands.put("spigot", new SpigotCommand("spigot"));
        version = SpigotConfig.getInt("config-version", 11);
        SpigotConfig.set("config-version", 11);
        SpigotConfig.readConfig(SpigotConfig.class, null);
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            MinecraftServer.getServerInst().server.getCommandMap().register(entry.getKey(), "Spigot", entry.getValue());
        }
    }

    static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (!Modifier.isPrivate(method.getModifiers()) || method.getParameterTypes().length != 0 || method.getReturnType() != Void.TYPE) continue;
            try {
                method.setAccessible(true);
                method.invoke(instance, new Object[0]);
                continue;
            }
            catch (InvocationTargetException ex2) {
                throw Throwables.propagate((Throwable)ex2.getCause());
            }
            catch (Exception ex3) {
                Bukkit.getLogger().log(java.util.logging.Level.SEVERE, "Error invoking " + method, ex3);
            }
        }
        try {
            config.save(CONFIG_FILE);
        }
        catch (IOException ex4) {
            Bukkit.getLogger().log(java.util.logging.Level.SEVERE, "Could not save " + CONFIG_FILE, ex4);
        }
    }

    private static void set(String path, Object val) {
        config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static void logCommands() {
        logCommands = SpigotConfig.getBoolean("commands.log", true);
    }

    private static void tabComplete() {
        if (version < 6) {
            boolean oldValue = SpigotConfig.getBoolean("commands.tab-complete", true);
            if (oldValue) {
                SpigotConfig.set("commands.tab-complete", 0);
            } else {
                SpigotConfig.set("commands.tab-complete", -1);
            }
        }
        tabComplete = SpigotConfig.getInt("commands.tab-complete", 0);
    }

    private static String transform(String s2) {
        return ChatColor.translateAlternateColorCodes('&', s2).replaceAll("\\\\n", "\n");
    }

    private static void messages() {
        if (version < 8) {
            SpigotConfig.set("messages.outdated-client", outdatedClientMessage);
            SpigotConfig.set("messages.outdated-server", outdatedServerMessage);
        }
        whitelistMessage = SpigotConfig.transform(SpigotConfig.getString("messages.whitelist", "You are not whitelisted on this server!"));
        unknownCommandMessage = SpigotConfig.transform(SpigotConfig.getString("messages.unknown-command", "Unknown command. Type \"/help\" for help."));
        serverFullMessage = SpigotConfig.transform(SpigotConfig.getString("messages.server-full", "The server is full!"));
        outdatedClientMessage = SpigotConfig.transform(SpigotConfig.getString("messages.outdated-client", outdatedClientMessage));
        outdatedServerMessage = SpigotConfig.transform(SpigotConfig.getString("messages.outdated-server", outdatedServerMessage));
    }

    private static void watchdog() {
        timeoutTime = SpigotConfig.getInt("settings.timeout-time", timeoutTime);
        restartOnCrash = SpigotConfig.getBoolean("settings.restart-on-crash", restartOnCrash);
        restartScript = SpigotConfig.getString("settings.restart-script", restartScript);
        restartMessage = SpigotConfig.transform(SpigotConfig.getString("messages.restart", "Server is restarting"));
        commands.put("restart", new RestartCommand("restart"));
        WatchdogThread.doStart(timeoutTime, restartOnCrash);
    }

    private static void bungee() {
        if (version < 4) {
            SpigotConfig.set("settings.bungeecord", false);
            System.out.println("Oudated config, disabling BungeeCord support!");
        }
        bungee = SpigotConfig.getBoolean("settings.bungeecord", false);
    }

    private static void nettyThreads() {
        int count = SpigotConfig.getInt("settings.netty-threads", 4);
        System.setProperty("io.netty.eventLoopThreads", Integer.toString(count));
        Bukkit.getLogger().log(java.util.logging.Level.INFO, "Using {0} threads for Netty based IO", count);
    }

    private static void lateBind() {
        lateBind = SpigotConfig.getBoolean("settings.late-bind", false);
    }

    private static void stats() {
        disableStatSaving = SpigotConfig.getBoolean("stats.disable-saving", false);
        if (!config.contains("stats.forced-stats")) {
            config.createSection("stats.forced-stats");
        }
        ConfigurationSection section = config.getConfigurationSection("stats.forced-stats");
        for (String name : section.getKeys(true)) {
            if (!section.isInt(name)) continue;
            if ( StatList.getOneShotStat(name) == null ) {
                Bukkit.getLogger().log(java.util.logging.Level.WARNING, "Ignoring non existent stats.forced-stats " + name);
                continue;
            }
            forcedStats.put(name, section.getInt(name));
        }
    }

    private static void tpsCommand() {
        commands.put("tps", new TicksPerSecondCommand("tps"));
    }

    private static void playerSample() {
        playerSample = SpigotConfig.getInt("settings.sample-count", 12);
        System.out.println("Server Ping Player Sample Count: " + playerSample);
    }

    private static void playerShuffle() {
        playerShuffle = SpigotConfig.getInt("settings.player-shuffle", 0);
    }

    private static void spamExclusions() {
        spamExclusions = SpigotConfig.getList("commands.spam-exclusions", Arrays.asList("/skill"));
    }

    private static void silentCommandBlocks() {
        silentCommandBlocks = SpigotConfig.getBoolean("commands.silent-commandblock-console", false);
    }

    private static void filterCreativeItems() {
        filterCreativeItems = SpigotConfig.getBoolean("settings.filter-creative-items", true);
    }

    private static void replaceCommands() {
        if (config.contains("replace-commands")) {
            SpigotConfig.set("commands.replace-commands", config.getStringList("replace-commands"));
            config.set("replace-commands", null);
        }
        replaceCommands = new HashSet<String>(SpigotConfig.getList("commands.replace-commands", Arrays.asList("setblock", "summon", "testforblock", "tellraw")));
    }

    private static void userCacheCap() {
        userCacheCap = SpigotConfig.getInt("settings.user-cache-size", 1000);
    }

    private static void saveUserCacheOnStopOnly() {
        saveUserCacheOnStopOnly = SpigotConfig.getBoolean("settings.save-user-cache-on-stop-only", false);
    }

    private static void intCacheLimit() {
        intCacheLimit = SpigotConfig.getInt("settings.int-cache-limit", 1024);
    }

    private static void movedWronglyThreshold() {
        movedWronglyThreshold = SpigotConfig.getDouble("settings.moved-wrongly-threshold", 0.0625);
    }

    private static void movedTooQuicklyMultiplier() {
        movedTooQuicklyMultiplier = SpigotConfig.getDouble("settings.moved-too-quickly-multiplier", 10.0);
    }

    private static void attributeMaxes() {
        maxHealth = getDouble( "settings.attribute.maxHealth.max", maxHealth );
        ( (RangedAttribute) SharedMonsterAttributes.MAX_HEALTH ).maximumValue = maxHealth;
        movementSpeed = getDouble( "settings.attribute.movementSpeed.max", movementSpeed );
        ( (RangedAttribute) SharedMonsterAttributes.MOVEMENT_SPEED ).maximumValue = movementSpeed;
        attackDamage = getDouble( "settings.attribute.attackDamage.max", attackDamage );
        ( (RangedAttribute) SharedMonsterAttributes.ATTACK_DAMAGE ).maximumValue = attackDamage;
    }

    private static void debug() {
        debug = SpigotConfig.getBoolean("settings.debug", false);
        if (debug && !LogManager.getRootLogger().isTraceEnabled()) {
            LoggerContext ctx = (LoggerContext)LogManager.getContext((boolean)false);
            Configuration conf = ctx.getConfiguration();
            conf.getLoggerConfig("").setLevel(Level.ALL);
            ctx.updateLoggers(conf);
        }
        if (LogManager.getRootLogger().isTraceEnabled()) {
            Bukkit.getLogger().info("Debug logging is enabled");
        } else {
            Bukkit.getLogger().info("Debug logging is disabled");
        }
    }

    private static void itemDirtyTicks() {
        itemDirtyTicks = SpigotConfig.getInt("settings.item-dirty-ticks", 20);
    }

    private static void disabledAdvancements() {
        disableAdvancementSaving = SpigotConfig.getBoolean("advancements.disable-saving", false);
        disabledAdvancements = SpigotConfig.getList("advancements.disabled", Arrays.asList("minecraft:story/disabled"));
    }

    static {
        outdatedClientMessage = "Outdated client! Please use {0}";
        outdatedServerMessage = "Outdated server! I'm still on {0}";
        timeoutTime = 60;
        restartOnCrash = true;
        restartScript = "./start.sh";
        forcedStats = new TObjectIntHashMap();
        maxHealth = 2048.0;
        movementSpeed = 2048.0;
        attackDamage = 2048.0;
    }
}

