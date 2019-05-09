/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.ArrayUtils
 *  org.apache.commons.lang.BooleanUtils
 */
package org.bukkit.conversations;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public abstract class BooleanPrompt
extends ValidatingPrompt {
    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        Object[] accepted = new String[]{"true", "false", "on", "off", "yes", "no", "y", "n", "1", "0", "right", "wrong", "correct", "incorrect", "valid", "invalid"};
        return ArrayUtils.contains((Object[])accepted, (Object)input.toLowerCase());
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        if (input.equalsIgnoreCase("y") || input.equals("1") || input.equalsIgnoreCase("right") || input.equalsIgnoreCase("correct") || input.equalsIgnoreCase("valid")) {
            input = "true";
        }
        return this.acceptValidatedInput(context, BooleanUtils.toBoolean((String)input));
    }

    protected abstract Prompt acceptValidatedInput(ConversationContext var1, boolean var2);
}

