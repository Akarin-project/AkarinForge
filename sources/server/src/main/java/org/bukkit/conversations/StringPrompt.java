/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

public abstract class StringPrompt
implements Prompt {
    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }
}

