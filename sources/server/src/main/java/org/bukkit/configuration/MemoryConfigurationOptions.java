/*
 * Akarin Forge
 */
package org.bukkit.configuration;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.MemoryConfiguration;

public class MemoryConfigurationOptions
extends ConfigurationOptions {
    protected MemoryConfigurationOptions(MemoryConfiguration configuration) {
        super(configuration);
    }

    @Override
    public MemoryConfiguration configuration() {
        return (MemoryConfiguration)super.configuration();
    }

    @Override
    public MemoryConfigurationOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @Override
    public MemoryConfigurationOptions pathSeparator(char value) {
        super.pathSeparator(value);
        return this;
    }
}

