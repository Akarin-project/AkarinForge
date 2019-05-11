/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.conversations;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class ConversationTracker {
    private LinkedList<Conversation> conversationQueue = new LinkedList();

    public synchronized boolean beginConversation(Conversation conversation) {
        if (!this.conversationQueue.contains(conversation)) {
            this.conversationQueue.addLast(conversation);
            if (this.conversationQueue.getFirst() == conversation) {
                conversation.begin();
                conversation.outputNextPrompt();
                return true;
            }
        }
        return true;
    }

    public synchronized void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        if (!this.conversationQueue.isEmpty()) {
            if (this.conversationQueue.getFirst() == conversation) {
                conversation.abandon(details);
            }
            if (this.conversationQueue.contains(conversation)) {
                this.conversationQueue.remove(conversation);
            }
            if (!this.conversationQueue.isEmpty()) {
                this.conversationQueue.getFirst().outputNextPrompt();
            }
        }
    }

    public synchronized void abandonAllConversations() {
        LinkedList<Conversation> oldQueue = this.conversationQueue;
        this.conversationQueue = new LinkedList();
        for (Conversation conversation : oldQueue) {
            try {
                conversation.abandon(new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
            }
            catch (Throwable t2) {
                Bukkit.getLogger().log(Level.SEVERE, "Unexpected exception while abandoning a conversation", t2);
            }
        }
    }

    public synchronized void acceptConversationInput(String input) {
        if (this.isConversing()) {
            Conversation conversation = this.conversationQueue.getFirst();
            try {
                conversation.acceptInput(input);
            }
            catch (Throwable t2) {
                conversation.getContext().getPlugin().getLogger().log(Level.WARNING, String.format("Plugin %s generated an exception whilst handling conversation input", conversation.getContext().getPlugin().getDescription().getFullName()), t2);
            }
        }
    }

    public synchronized boolean isConversing() {
        return !this.conversationQueue.isEmpty();
    }

    public synchronized boolean isConversingModaly() {
        return this.isConversing() && this.conversationQueue.getFirst().isModal();
    }
}

