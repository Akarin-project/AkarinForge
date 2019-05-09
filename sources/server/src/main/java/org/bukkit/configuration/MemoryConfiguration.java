/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.configuration;

import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfigurationOptions;
import org.bukkit.configuration.MemorySection;

public class MemoryConfiguration
extends MemorySection
implements Configuration {
    protected Configuration defaults;
    protected MemoryConfigurationOptions options;

    public MemoryConfiguration() {
    }

    public MemoryConfiguration(Configuration defaults) {
        this.defaults = defaults;
    }

    @Override
    public void addDefault(String path, Object value) {
        Validate.notNull((Object)path, (String)"Path may not be null");
        if (this.defaults == null) {
            this.defaults = new MemoryConfiguration();
        }
        this.defaults.set(path, value);
    }

    @Override
    public void addDefaults(Map<String, Object> defaults) {
        Validate.notNull(defaults, (String)"Defaults may not be null");
        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            this.addDefault(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void addDefaults(Configuration defaults) {
        Validate.notNull((Object)defaults, (String)"Defaults may not be null");
        this.addDefaults(defaults.getValues(true));
    }

    @Override
    public void setDefaults(Configuration defaults) {
        Validate.notNull((Object)defaults, (String)"Defaults may not be null");
        this.defaults = defaults;
    }

    @Override
    public Configuration getDefaults() {
        return this.defaults;
    }

    @Override
    public ConfigurationSection getParent() {
        return null;
    }

    @Override
    public MemoryConfigurationOptions options() {
        if (this.options == null) {
            this.options = new MemoryConfigurationOptions(this);
        }
        return this.options;
    }
}

