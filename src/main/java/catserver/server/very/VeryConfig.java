/*
 * Decompiled with CFR 0_119.
 */
package catserver.server.very;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bukkit.configuration.file.YamlConfiguration;

public final class VeryConfig {
    protected static int userid;
    protected static String key;
    public static long expTime;
    public static Class cls;

    public static void load() {
        YamlConfiguration config;
        File file = new File("auth.yml");
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else {
            config = YamlConfiguration.loadConfiguration(new InputStreamReader(VeryConfig.class.getClassLoader().getResourceAsStream("auth.yml")));
            try {
                file.createNewFile();
                config.save(file);
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        userid = config.getInt("userid");
        key = config.getString("key");
    }

    static {
        expTime = 0;
        cls = null;
    }
}

