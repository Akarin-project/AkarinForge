/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;

public class ExactMatchConversationCanceller
implements ConversationCanceller {
    private String escapeSequence;

    public ExactMatchConversationCanceller(String escapeSequence) {
        this.escapeSequence = escapeSequence;
    }

    @Override
    public void setConversation(Conversation conversation) {
    }

    @Override
    public boolean cancelBasedOnInput(ConversationContext context, String input) {
        return input.equals(this.escapeSequence);
    }

    @Override
    public ConversationCanceller clone() {
        return new ExactMatchConversationCanceller(this.escapeSequence);
    }
}

