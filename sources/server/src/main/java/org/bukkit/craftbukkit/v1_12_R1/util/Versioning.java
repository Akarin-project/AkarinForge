package org.bukkit.craftbukkit.v1_12_R1.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public final class Versioning {
    public static String getBukkitVersion() {
        String result = "1.12.2-R0.1";
        InputStream stream = Bukkit.class.getClassLoader().getResourceAsStream("META-INF/maven/org.bukkit/bukkit/pom.properties");
        Properties properties = new Properties();
        if (stream != null) {
            try {
                properties.load(stream);
                result = properties.getProperty("version");
            }
            catch (IOException ex2) {
                Logger.getLogger(Versioning.class.getName()).log(Level.SEVERE, "Could not get Bukkit version!", ex2);
            }
        }
        return result;
    }
}

