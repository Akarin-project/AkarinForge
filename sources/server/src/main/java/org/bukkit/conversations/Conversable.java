/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;

public interface Conversable {
    public boolean isConversing();

    public void acceptConversationInput(String var1);

    public boolean beginConversation(Conversation var1);

    public void abandonConversation(Conversation var1);

    public void abandonConversation(Conversation var1, ConversationAbandonedEvent var2);

    public void sendRawMessage(String var1);
}

