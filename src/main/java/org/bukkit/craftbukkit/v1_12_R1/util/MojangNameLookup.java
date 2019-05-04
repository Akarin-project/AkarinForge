/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.gson.Gson
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MojangNameLookup {
    private static final Logger logger = LogManager.getFormatterLogger(MojangNameLookup.class);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String lookupName(UUID id2) {
        URLConnection connection;
        if (id2 == null) {
            return null;
        }
        InputStream inputStream = null;
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id2.toString().replace("-", ""));
            connection = url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            inputStream = connection.getInputStream();
            String result = IOUtils.toString((InputStream)inputStream, (Charset)Charsets.UTF_8);
            Gson gson = new Gson();
            Response response = (Response)gson.fromJson(result, Response.class);
            if (response == null || response.name == null) {
                logger.warn("Failed to lookup name from UUID");
                String string = null;
                return string;
            }
            if (response.cause != null && response.cause.length() > 0) {
                logger.warn("Failed to lookup name from UUID: %s", (Object)response.errorMessage);
                String string = null;
                return string;
            }
            String string = response.name;
            return string;
        }
        catch (MalformedURLException ex2) {
            logger.warn("Malformed URL in UUID lookup");
            connection = null;
            return connection;
        }
        catch (IOException ex3) {
            IOUtils.closeQuietly((InputStream)inputStream);
        }
        finally {
            IOUtils.closeQuietly((InputStream)inputStream);
        }
        return null;
    }

    private class Response {
        String errorMessage;
        String cause;
        String name;

        private Response() {
        }
    }

}

