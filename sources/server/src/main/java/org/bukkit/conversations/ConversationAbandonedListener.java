/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import java.util.EventListener;
import org.bukkit.conversations.ConversationAbandonedEvent;

public interface ConversationAbandonedListener
extends EventListener {
    public void conversationAbandoned(ConversationAbandonedEvent var1);
}

