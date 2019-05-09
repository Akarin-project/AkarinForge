/*
 * Akarin Forge
 */
package org.bukkit.configuration;

import org.bukkit.configuration.Configuration;

public class ConfigurationOptions {
    private char pathSeparator = 46;
    private boolean copyDefaults = false;
    private final Configuration configuration;

    protected ConfigurationOptions(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration configuration() {
        return this.configuration;
    }

    public char pathSeparator() {
        return this.pathSeparator;
    }

    public ConfigurationOptions pathSeparator(char value) {
        this.pathSeparator = value;
        return this;
    }

    public boolean copyDefaults() {
        return this.copyDefaults;
    }

    public ConfigurationOptions copyDefaults(boolean value) {
        this.copyDefaults = value;
        return this;
    }
}

