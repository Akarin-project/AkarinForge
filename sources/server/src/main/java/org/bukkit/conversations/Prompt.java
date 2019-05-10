/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.conversations.ConversationContext;

public interface Prompt
extends Cloneable {
    public static final Prompt END_OF_CONVERSATION = null;

    public String getPromptText(ConversationContext var1);

    public boolean blocksForInput(ConversationContext var1);

    public Prompt acceptInput(ConversationContext var1, String var2);
}

