/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  joptsimple.ArgumentAcceptingOptionSpec
 *  joptsimple.NonOptionArgumentSpec
 *  joptsimple.OptionParser
 *  joptsimple.OptionSet
 *  joptsimple.OptionSpecBuilder
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.akarin.forge;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.akarin.forge.AkarinForge;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpecBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AkarinGradleStart {
    protected static Logger LOGGER = LogManager.getLogger((String)"GradleStart");
    Map<String, String> argMap = Maps.newHashMap();
    List<String> extras = Lists.newArrayList();
    static final File SRG_SRG_MCP = new File(AkarinGradleStart.class.getClassLoader().getResource("mappings/" + AkarinForge.getNativeVersion() + "/srg2mcp.srg").getFile());

    public static void main(String[] args) throws Throwable {
        new AkarinGradleStart().launch(args);
    }

    protected void launch(String[] args) throws Throwable {
        System.setProperty("net.minecraftforge.gradle.GradleStart.srg.srg-mcp", SRG_SRG_MCP.getCanonicalPath());
        this.parseArgs(args);
        System.setProperty("fml.ignoreInvalidMinecraftCertificates", "true");
        args = this.getArgs();
        this.argMap = null;
        this.extras = null;
        System.gc();
        Class.forName("net.minecraft.launchwrapper.Launch").getDeclaredMethod("main", String[].class).invoke(null, args);
    }

    private String[] getArgs() {
        ArrayList<String> list = new ArrayList<String>(22);
        for (Map.Entry<String, String> e2 : this.argMap.entrySet()) {
            String val = e2.getValue();
            if (Strings.isNullOrEmpty((String)val)) continue;
            list.add("--" + e2.getKey());
            list.add(val);
        }
        if (!Strings.isNullOrEmpty((String)"net.minecraftforge.fml.common.launcher.FMLServerTweaker")) {
            list.add("--tweakClass");
            list.add("net.minecraftforge.fml.common.launcher.FMLServerTweaker");
        }
        if (this.extras != null) {
            list.addAll(this.extras);
        }
        String[] out = list.toArray(new String[list.size()]);
        StringBuilder b2 = new StringBuilder();
        b2.append('[');
        for (int x2 = 0; x2 < out.length; ++x2) {
            b2.append(out[x2]);
            if ("--accessToken".equalsIgnoreCase(out[x2])) {
                b2.append("{REDACTED}");
                ++x2;
            }
            if (x2 >= out.length - 1) continue;
            b2.append(", ");
        }
        b2.append(']');
        LOGGER.info("Running with arguments: " + b2.toString());
        return out;
    }

    private void parseArgs(String[] args) {
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        for (String key : this.argMap.keySet()) {
            parser.accepts(key).withRequiredArg().ofType(String.class);
        }
        NonOptionArgumentSpec nonOption = parser.nonOptions();
        OptionSet options = parser.parse(args);
        for (String key : this.argMap.keySet()) {
            if (!options.hasArgument(key)) continue;
            String value = (String)options.valueOf(key);
            this.argMap.put(key, value);
            if ("password".equalsIgnoreCase(key) || "accessToken".equalsIgnoreCase(key)) continue;
            LOGGER.info(key + ": " + value);
        }
        this.extras = Lists.newArrayList((Iterable)nonOption.values(options));
        LOGGER.info("Extra: " + this.extras);
    }
}

