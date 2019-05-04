/*
 * Akarin Forge
 */
package org.bukkit.conversations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.conversations.NullConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.plugin.Plugin;

public class Conversation {
    private Prompt firstPrompt;
    private boolean abandoned;
    protected Prompt currentPrompt;
    protected ConversationContext context;
    protected boolean modal;
    protected boolean localEchoEnabled;
    protected ConversationPrefix prefix;
    protected List<ConversationCanceller> cancellers;
    protected List<ConversationAbandonedListener> abandonedListeners;

    public Conversation(Plugin plugin, Conversable forWhom, Prompt firstPrompt) {
        this(plugin, forWhom, firstPrompt, new HashMap<Object, Object>());
    }

    public Conversation(Plugin plugin, Conversable forWhom, Prompt firstPrompt, Map<Object, Object> initialSessionData) {
        this.firstPrompt = firstPrompt;
        this.context = new ConversationContext(plugin, forWhom, initialSessionData);
        this.modal = true;
        this.localEchoEnabled = true;
        this.prefix = new NullConversationPrefix();
        this.cancellers = new ArrayList<ConversationCanceller>();
        this.abandonedListeners = new ArrayList<ConversationAbandonedListener>();
    }

    public Conversable getForWhom() {
        return this.context.getForWhom();
    }

    public boolean isModal() {
        return this.modal;
    }

    void setModal(boolean modal) {
        this.modal = modal;
    }

    public boolean isLocalEchoEnabled() {
        return this.localEchoEnabled;
    }

    public void setLocalEchoEnabled(boolean localEchoEnabled) {
        this.localEchoEnabled = localEchoEnabled;
    }

    public ConversationPrefix getPrefix() {
        return this.prefix;
    }

    void setPrefix(ConversationPrefix prefix) {
        this.prefix = prefix;
    }

    void addConversationCanceller(ConversationCanceller canceller) {
        canceller.setConversation(this);
        this.cancellers.add(canceller);
    }

    public List<ConversationCanceller> getCancellers() {
        return this.cancellers;
    }

    public ConversationContext getContext() {
        return this.context;
    }

    public void begin() {
        if (this.currentPrompt == null) {
            this.abandoned = false;
            this.currentPrompt = this.firstPrompt;
            this.context.getForWhom().beginConversation(this);
        }
    }

    public ConversationState getState() {
        if (this.currentPrompt != null) {
            return ConversationState.STARTED;
        }
        if (this.abandoned) {
            return ConversationState.ABANDONED;
        }
        return ConversationState.UNSTARTED;
    }

    public void acceptInput(String input) {
        if (this.currentPrompt != null) {
            if (this.localEchoEnabled) {
                this.context.getForWhom().sendRawMessage(this.prefix.getPrefix(this.context) + input);
            }
            for (ConversationCanceller canceller : this.cancellers) {
                if (!canceller.cancelBasedOnInput(this.context, input)) continue;
                this.abandon(new ConversationAbandonedEvent(this, canceller));
                return;
            }
            this.currentPrompt = this.currentPrompt.acceptInput(this.context, input);
            this.outputNextPrompt();
        }
    }

    public synchronized void addConversationAbandonedListener(ConversationAbandonedListener listener) {
        this.abandonedListeners.add(listener);
    }

    public synchronized void removeConversationAbandonedListener(ConversationAbandonedListener listener) {
        this.abandonedListeners.remove(listener);
    }

    public void abandon() {
        this.abandon(new ConversationAbandonedEvent(this, new ManuallyAbandonedConversationCanceller()));
    }

    public synchronized void abandon(ConversationAbandonedEvent details) {
        if (!this.abandoned) {
            this.abandoned = true;
            this.currentPrompt = null;
            this.context.getForWhom().abandonConversation(this);
            for (ConversationAbandonedListener listener : this.abandonedListeners) {
                listener.conversationAbandoned(details);
            }
        }
    }

    public void outputNextPrompt() {
        if (this.currentPrompt == null) {
            this.abandon(new ConversationAbandonedEvent(this));
        } else {
            this.context.getForWhom().sendRawMessage(this.prefix.getPrefix(this.context) + this.currentPrompt.getPromptText(this.context));
            if (!this.currentPrompt.blocksForInput(this.context)) {
                this.currentPrompt = this.currentPrompt.acceptInput(this.context, null);
                this.outputNextPrompt();
            }
        }
    }

    public static enum ConversationState {
        UNSTARTED,
        STARTED,
        ABANDONED;
        

        private ConversationState() {
        }
    }

}

