/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

public class NullConversationPrefix
implements ConversationPrefix {
    @Override
    public String getPrefix(ConversationContext context) {
        return "";
    }
}

