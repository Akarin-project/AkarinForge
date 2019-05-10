/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

public abstract class ValidatingPrompt
implements Prompt {
    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        if (this.isInputValid(context, input)) {
            return this.acceptValidatedInput(context, input);
        }
        String failPrompt = this.getFailedValidationText(context, input);
        if (failPrompt != null) {
            context.getForWhom().sendRawMessage((Object)((Object)ChatColor.RED) + failPrompt);
        }
        return this;
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

    protected abstract boolean isInputValid(ConversationContext var1, String var2);

    protected abstract Prompt acceptValidatedInput(ConversationContext var1, String var2);

    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return null;
    }
}

