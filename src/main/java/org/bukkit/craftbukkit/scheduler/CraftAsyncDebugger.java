/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

class CraftAsyncDebugger {
    private CraftAsyncDebugger next = null;
    private final int expiry;
    private final Plugin plugin;
    private final Class<? extends Runnable> clazz;

    CraftAsyncDebugger(int expiry, Plugin plugin, Class<? extends Runnable> clazz) {
        this.expiry = expiry;
        this.plugin = plugin;
        this.clazz = clazz;
    }

    final CraftAsyncDebugger getNextHead(int time) {
        CraftAsyncDebugger next;
        CraftAsyncDebugger current = this;
        while (time > current.expiry && (next = current.next) != null) {
            current = next;
        }
        return current;
    }

    final CraftAsyncDebugger setNext(CraftAsyncDebugger next) {
        this.next = next;
        return this.next;
    }

    StringBuilder debugTo(StringBuilder string) {
        CraftAsyncDebugger next = this;
        while (next != null) {
            string.append(next.plugin.getDescription().getName()).append(':').append(next.clazz.getName()).append('@').append(next.expiry).append(',');
            next = next.next;
        }
        return string;
    }
}

