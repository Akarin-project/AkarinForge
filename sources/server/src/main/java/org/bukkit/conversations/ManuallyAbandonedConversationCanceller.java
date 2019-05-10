/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;

public class ManuallyAbandonedConversationCanceller
implements ConversationCanceller {
    @Override
    public void setConversation(Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean cancelBasedOnInput(ConversationContext context, String input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConversationCanceller clone() {
        throw new UnsupportedOperationException();
    }
}

