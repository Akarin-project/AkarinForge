/*
 * Akarin Forge
 */
package org.bukkit.configuration.file;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemoryConfigurationOptions;
import org.bukkit.configuration.file.FileConfiguration;

public class FileConfigurationOptions
extends MemoryConfigurationOptions {
    private String header = null;
    private boolean copyHeader = true;

    protected FileConfigurationOptions(MemoryConfiguration configuration) {
        super(configuration);
    }

    @Override
    public FileConfiguration configuration() {
        return (FileConfiguration)super.configuration();
    }

    @Override
    public FileConfigurationOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @Override
    public FileConfigurationOptions pathSeparator(char value) {
        super.pathSeparator(value);
        return this;
    }

    public String header() {
        return this.header;
    }

    public FileConfigurationOptions header(String value) {
        this.header = value;
        return this;
    }

    public boolean copyHeader() {
        return this.copyHeader;
    }

    public FileConfigurationOptions copyHeader(boolean value) {
        this.copyHeader = value;
        return this;
    }
}

