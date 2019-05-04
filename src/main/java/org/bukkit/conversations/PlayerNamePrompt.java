/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.Server;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class PlayerNamePrompt
extends ValidatingPrompt {
    private Plugin plugin;

    public PlayerNamePrompt(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return this.plugin.getServer().getPlayer(input) != null;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        return this.acceptValidatedInput(context, this.plugin.getServer().getPlayer(input));
    }

    protected abstract Prompt acceptValidatedInput(ConversationContext var1, Player var2);
}

