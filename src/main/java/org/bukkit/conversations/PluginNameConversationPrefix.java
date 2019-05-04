/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class PluginNameConversationPrefix
implements ConversationPrefix {
    protected String separator;
    protected ChatColor prefixColor;
    protected Plugin plugin;
    private String cachedPrefix;

    public PluginNameConversationPrefix(Plugin plugin) {
        this(plugin, " > ", ChatColor.LIGHT_PURPLE);
    }

    public PluginNameConversationPrefix(Plugin plugin, String separator, ChatColor prefixColor) {
        this.separator = separator;
        this.prefixColor = prefixColor;
        this.plugin = plugin;
        this.cachedPrefix = (Object)((Object)prefixColor) + plugin.getDescription().getName() + separator + (Object)((Object)ChatColor.WHITE);
    }

    @Override
    public String getPrefix(ConversationContext context) {
        return this.cachedPrefix;
    }
}

