/*
 * Akarin Forge
 */
package org.bukkit.plugin.messaging;

import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.messaging.PluginMessageListenerRegistration;

public interface Messenger {
    public static final int MAX_MESSAGE_SIZE = 32766;
    public static final int MAX_CHANNEL_SIZE = 20;

    public boolean isReservedChannel(String var1);

    public void registerOutgoingPluginChannel(Plugin var1, String var2);

    public void unregisterOutgoingPluginChannel(Plugin var1, String var2);

    public void unregisterOutgoingPluginChannel(Plugin var1);

    public PluginMessageListenerRegistration registerIncomingPluginChannel(Plugin var1, String var2, PluginMessageListener var3);

    public void unregisterIncomingPluginChannel(Plugin var1, String var2, PluginMessageListener var3);

    public void unregisterIncomingPluginChannel(Plugin var1, String var2);

    public void unregisterIncomingPluginChannel(Plugin var1);

    public Set<String> getOutgoingChannels();

    public Set<String> getOutgoingChannels(Plugin var1);

    public Set<String> getIncomingChannels();

    public Set<String> getIncomingChannels(Plugin var1);

    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(Plugin var1);

    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(String var1);

    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(Plugin var1, String var2);

    public boolean isRegistrationValid(PluginMessageListenerRegistration var1);

    public boolean isIncomingChannelRegistered(Plugin var1, String var2);

    public boolean isOutgoingChannelRegistered(Plugin var1, String var2);

    public void dispatchIncomingMessage(Player var1, String var2, byte[] var3);
}

