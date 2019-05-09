/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import java.util.Map;
import org.bukkit.conversations.Conversable;
import org.bukkit.plugin.Plugin;

public class ConversationContext {
    private Conversable forWhom;
    private Map<Object, Object> sessionData;
    private Plugin plugin;

    public ConversationContext(Plugin plugin, Conversable forWhom, Map<Object, Object> initialSessionData) {
        this.plugin = plugin;
        this.forWhom = forWhom;
        this.sessionData = initialSessionData;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public Conversable getForWhom() {
        return this.forWhom;
    }

    public Map<Object, Object> getAllSessionData() {
        return this.sessionData;
    }

    public Object getSessionData(Object key) {
        return this.sessionData.get(key);
    }

    public void setSessionData(Object key, Object value) {
        this.sessionData.put(key, value);
    }
}

