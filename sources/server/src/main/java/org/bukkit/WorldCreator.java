/*
 * Akarin Forge
 */
package org.bukkit;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class WorldCreator {
    private final String name;
    private long seed;
    private World.Environment environment = World.Environment.NORMAL;
    private ChunkGenerator generator = null;
    private WorldType type = WorldType.NORMAL;
    private boolean generateStructures = true;
    private String generatorSettings = "";

    public WorldCreator(String name) {
        if (name == null) {
            throw new IllegalArgumentException("World name cannot be null");
        }
        this.name = name;
        this.seed = new Random().nextLong();
    }

    public WorldCreator copy(World world) {
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }
        this.seed = world.getSeed();
        this.environment = world.getEnvironment();
        this.generator = world.getGenerator();
        return this;
    }

    public WorldCreator copy(WorldCreator creator) {
        if (creator == null) {
            throw new IllegalArgumentException("Creator cannot be null");
        }
        this.seed = creator.seed();
        this.environment = creator.environment();
        this.generator = creator.generator();
        return this;
    }

    public String name() {
        return this.name;
    }

    public long seed() {
        return this.seed;
    }

    public WorldCreator seed(long seed) {
        this.seed = seed;
        return this;
    }

    public World.Environment environment() {
        return this.environment;
    }

    public WorldCreator environment(World.Environment env) {
        this.environment = env;
        return this;
    }

    public WorldType type() {
        return this.type;
    }

    public WorldCreator type(WorldType type) {
        this.type = type;
        return this;
    }

    public ChunkGenerator generator() {
        return this.generator;
    }

    public WorldCreator generator(ChunkGenerator generator) {
        this.generator = generator;
        return this;
    }

    public WorldCreator generator(String generator) {
        this.generator = WorldCreator.getGeneratorForName(this.name, generator, Bukkit.getConsoleSender());
        return this;
    }

    public WorldCreator generator(String generator, CommandSender output) {
        this.generator = WorldCreator.getGeneratorForName(this.name, generator, output);
        return this;
    }

    public WorldCreator generatorSettings(String generatorSettings) {
        this.generatorSettings = generatorSettings;
        return this;
    }

    public String generatorSettings() {
        return this.generatorSettings;
    }

    public WorldCreator generateStructures(boolean generate) {
        this.generateStructures = generate;
        return this;
    }

    public boolean generateStructures() {
        return this.generateStructures;
    }

    public World createWorld() {
        return Bukkit.createWorld(this);
    }

    public static WorldCreator name(String name) {
        return new WorldCreator(name);
    }

    public static ChunkGenerator getGeneratorForName(String world, String name, CommandSender output) {
        ChunkGenerator result = null;
        if (world == null) {
            throw new IllegalArgumentException("World name must be specified");
        }
        if (output == null) {
            output = Bukkit.getConsoleSender();
        }
        if (name != null) {
            String[] split = name.split(":", 2);
            String id2 = split.length > 1 ? split[1] : null;
            Plugin plugin = Bukkit.getPluginManager().getPlugin(split[0]);
            if (plugin == null) {
                output.sendMessage("Could not set generator for world '" + world + "': Plugin '" + split[0] + "' does not exist");
            } else if (!plugin.isEnabled()) {
                output.sendMessage("Could not set generator for world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled");
            } else {
                result = plugin.getDefaultWorldGenerator(world, id2);
            }
        }
        return result;
    }
}

