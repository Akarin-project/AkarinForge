/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;

public interface ConversationCanceller
extends Cloneable {
    public void setConversation(Conversation var1);

    public boolean cancelBasedOnInput(ConversationContext var1, String var2);

    public ConversationCanceller clone();
}

