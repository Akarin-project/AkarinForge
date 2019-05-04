/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

public abstract class MessagePrompt
implements Prompt {
    @Override
    public boolean blocksForInput(ConversationContext context) {
        return false;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        return this.getNextPrompt(context);
    }

    protected abstract Prompt getNextPrompt(ConversationContext var1);
}

