/*
 * Akarin Forge
 */
package org.bukkit.plugin;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public abstract class PluginBase
implements Plugin {
    public final int hashCode() {
        return this.getName().hashCode();
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Plugin)) {
            return false;
        }
        return this.getName().equals(((Plugin)obj).getName());
    }

    @Override
    public final String getName() {
        return this.getDescription().getName();
    }
}

