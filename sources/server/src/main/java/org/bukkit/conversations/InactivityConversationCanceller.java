/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import org.bukkit.Server;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class InactivityConversationCanceller
implements ConversationCanceller {
    protected Plugin plugin;
    protected int timeoutSeconds;
    protected Conversation conversation;
    private int taskId = -1;

    public InactivityConversationCanceller(Plugin plugin, int timeoutSeconds) {
        this.plugin = plugin;
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
        this.startTimer();
    }

    @Override
    public boolean cancelBasedOnInput(ConversationContext context, String input) {
        this.stopTimer();
        this.startTimer();
        return false;
    }

    @Override
    public ConversationCanceller clone() {
        return new InactivityConversationCanceller(this.plugin, this.timeoutSeconds);
    }

    private void startTimer() {
        this.taskId = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable(){

            @Override
            public void run() {
                if (InactivityConversationCanceller.this.conversation.getState() == Conversation.ConversationState.UNSTARTED) {
                    InactivityConversationCanceller.this.startTimer();
                } else if (InactivityConversationCanceller.this.conversation.getState() == Conversation.ConversationState.STARTED) {
                    InactivityConversationCanceller.this.cancelling(InactivityConversationCanceller.this.conversation);
                    InactivityConversationCanceller.this.conversation.abandon(new ConversationAbandonedEvent(InactivityConversationCanceller.this.conversation, InactivityConversationCanceller.this));
                }
            }
        }, (long)(this.timeoutSeconds * 20));
    }

    private void stopTimer() {
        if (this.taskId != -1) {
            this.plugin.getServer().getScheduler().cancelTask(this.taskId);
            this.taskId = -1;
        }
    }

    protected void cancelling(Conversation conversation) {
    }

}

