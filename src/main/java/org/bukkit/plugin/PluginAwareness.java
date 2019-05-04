/*
 * Akarin Forge
 */
package org.bukkit.plugin;

public interface PluginAwareness {

    public static enum Flags implements PluginAwareness
    {
        UTF8;
        

        private Flags() {
        }
    }

}

