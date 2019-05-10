/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 */
package org.bukkit.plugin.messaging;

import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.messaging.ChannelNameTooLongException;
import org.bukkit.plugin.messaging.ChannelNotRegisteredException;
import org.bukkit.plugin.messaging.MessageTooLargeException;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.messaging.PluginMessageListenerRegistration;
import org.bukkit.plugin.messaging.ReservedChannelException;

public class StandardMessenger
implements Messenger {
    private final Map<String, Set<PluginMessageListenerRegistration>> incomingByChannel = new HashMap<String, Set<PluginMessageListenerRegistration>>();
    private final Map<Plugin, Set<PluginMessageListenerRegistration>> incomingByPlugin = new HashMap<Plugin, Set<PluginMessageListenerRegistration>>();
    private final Map<String, Set<Plugin>> outgoingByChannel = new HashMap<String, Set<Plugin>>();
    private final Map<Plugin, Set<String>> outgoingByPlugin = new HashMap<Plugin, Set<String>>();
    private final Object incomingLock = new Object();
    private final Object outgoingLock = new Object();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void addToOutgoing(Plugin plugin, String channel) {
        Object object = this.outgoingLock;
        synchronized (object) {
            Set<Plugin> plugins = this.outgoingByChannel.get(channel);
            Set<String> channels = this.outgoingByPlugin.get(plugin);
            if (plugins == null) {
                plugins = new HashSet<Plugin>();
                this.outgoingByChannel.put(channel, plugins);
            }
            if (channels == null) {
                channels = new HashSet<String>();
                this.outgoingByPlugin.put(plugin, channels);
            }
            plugins.add(plugin);
            channels.add(channel);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void removeFromOutgoing(Plugin plugin, String channel) {
        Object object = this.outgoingLock;
        synchronized (object) {
            Set<Plugin> plugins = this.outgoingByChannel.get(channel);
            Set<String> channels = this.outgoingByPlugin.get(plugin);
            if (plugins != null) {
                plugins.remove(plugin);
                if (plugins.isEmpty()) {
                    this.outgoingByChannel.remove(channel);
                }
            }
            if (channels != null) {
                channels.remove(channel);
                if (channels.isEmpty()) {
                    this.outgoingByChannel.remove(channel);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void removeFromOutgoing(Plugin plugin) {
        Object object = this.outgoingLock;
        synchronized (object) {
            Set<String> channels = this.outgoingByPlugin.get(plugin);
            if (channels != null) {
                String[] toRemove = channels.toArray(new String[channels.size()]);
                this.outgoingByPlugin.remove(plugin);
                for (String channel : toRemove) {
                    this.removeFromOutgoing(plugin, channel);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void addToIncoming(PluginMessageListenerRegistration registration) {
        Object object = this.incomingLock;
        synchronized (object) {
            Set<PluginMessageListenerRegistration> registrations = this.incomingByChannel.get(registration.getChannel());
            if (registrations == null) {
                registrations = new HashSet<PluginMessageListenerRegistration>();
                this.incomingByChannel.put(registration.getChannel(), registrations);
            } else if (registrations.contains(registration)) {
                throw new IllegalArgumentException("This registration already exists");
            }
            registrations.add(registration);
            registrations = this.incomingByPlugin.get(registration.getPlugin());
            if (registrations == null) {
                registrations = new HashSet<PluginMessageListenerRegistration>();
                this.incomingByPlugin.put(registration.getPlugin(), registrations);
            } else if (registrations.contains(registration)) {
                throw new IllegalArgumentException("This registration already exists");
            }
            registrations.add(registration);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void removeFromIncoming(PluginMessageListenerRegistration registration) {
        Object object = this.incomingLock;
        synchronized (object) {
            Set<PluginMessageListenerRegistration> registrations = this.incomingByChannel.get(registration.getChannel());
            if (registrations != null) {
                registrations.remove(registration);
                if (registrations.isEmpty()) {
                    this.incomingByChannel.remove(registration.getChannel());
                }
            }
            if ((registrations = this.incomingByPlugin.get(registration.getPlugin())) != null) {
                registrations.remove(registration);
                if (registrations.isEmpty()) {
                    this.incomingByPlugin.remove(registration.getPlugin());
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void removeFromIncoming(Plugin plugin, String channel) {
        Object object = this.incomingLock;
        synchronized (object) {
            Set<PluginMessageListenerRegistration> registrations = this.incomingByPlugin.get(plugin);
            if (registrations != null) {
                PluginMessageListenerRegistration[] toRemove;
                for (PluginMessageListenerRegistration registration : toRemove = registrations.toArray(new PluginMessageListenerRegistration[registrations.size()])) {
                    if (!registration.getChannel().equals(channel)) continue;
                    this.removeFromIncoming(registration);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void removeFromIncoming(Plugin plugin) {
        Object object = this.incomingLock;
        synchronized (object) {
            Set<PluginMessageListenerRegistration> registrations = this.incomingByPlugin.get(plugin);
            if (registrations != null) {
                PluginMessageListenerRegistration[] toRemove = registrations.toArray(new PluginMessageListenerRegistration[registrations.size()]);
                this.incomingByPlugin.remove(plugin);
                for (PluginMessageListenerRegistration registration : toRemove) {
                    this.removeFromIncoming(registration);
                }
            }
        }
    }

    @Override
    public boolean isReservedChannel(String channel) {
        StandardMessenger.validateChannel(channel);
        return channel.equals("REGISTER") || channel.equals("UNREGISTER");
    }

    @Override
    public void registerOutgoingPluginChannel(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        StandardMessenger.validateChannel(channel);
        if (this.isReservedChannel(channel)) {
            throw new ReservedChannelException(channel);
        }
        this.addToOutgoing(plugin, channel);
    }

    @Override
    public void unregisterOutgoingPluginChannel(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        StandardMessenger.validateChannel(channel);
        this.removeFromOutgoing(plugin, channel);
    }

    @Override
    public void unregisterOutgoingPluginChannel(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        this.removeFromOutgoing(plugin);
    }

    @Override
    public PluginMessageListenerRegistration registerIncomingPluginChannel(Plugin plugin, String channel, PluginMessageListener listener) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        StandardMessenger.validateChannel(channel);
        if (this.isReservedChannel(channel)) {
            throw new ReservedChannelException(channel);
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        PluginMessageListenerRegistration result = new PluginMessageListenerRegistration(this, plugin, channel, listener);
        this.addToIncoming(result);
        return result;
    }

    @Override
    public void unregisterIncomingPluginChannel(Plugin plugin, String channel, PluginMessageListener listener) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        StandardMessenger.validateChannel(channel);
        this.removeFromIncoming(new PluginMessageListenerRegistration(this, plugin, channel, listener));
    }

    @Override
    public void unregisterIncomingPluginChannel(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        StandardMessenger.validateChannel(channel);
        this.removeFromIncoming(plugin, channel);
    }

    @Override
    public void unregisterIncomingPluginChannel(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        this.removeFromIncoming(plugin);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Set<String> getOutgoingChannels() {
        Object object = this.outgoingLock;
        synchronized (object) {
            Set<String> keys = this.outgoingByChannel.keySet();
            return ImmutableSet.copyOf(keys);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Set<String> getOutgoingChannels(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        Object object = this.outgoingLock;
        synchronized (object) {
            Set<String> channels = this.outgoingByPlugin.get(plugin);
            if (channels != null) {
                return ImmutableSet.copyOf(channels);
            }
            return ImmutableSet.of();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Set<String> getIncomingChannels() {
        Object object = this.incomingLock;
        synchronized (object) {
            Set<String> keys = this.incomingByChannel.keySet();
            return ImmutableSet.copyOf(keys);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Set<String> getIncomingChannels(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        Object object = this.incomingLock;
        synchronized (object) {
            Set<PluginMessageListenerRegistration> registrations = this.incomingByPlugin.get(plugin);
            if (registrations != null) {
                ImmutableSet.Builder builder = ImmutableSet.builder();
                for (PluginMessageListenerRegistration registration : registrations) {
                    builder.add((Object)registration.getChannel());
                }
                return builder.build();
            }
            return ImmutableSet.of();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        Object object = this.incomingLock;
        synchronized (object) {
            Set<PluginMessageListenerRegistration> registrations = this.incomingByPlugin.get(plugin);
            if (registrations != null) {
                return ImmutableSet.copyOf(registrations);
            }
            return ImmutableSet.of();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(String channel) {
        StandardMessenger.validateChannel(channel);
        Object object = this.incomingLock;
        synchronized (object) {
            Set<PluginMessageListenerRegistration> registrations = this.incomingByChannel.get(channel);
            if (registrations != null) {
                return ImmutableSet.copyOf(registrations);
            }
            return ImmutableSet.of();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Set<PluginMessageListenerRegistration> getIncomingChannelRegistrations(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        StandardMessenger.validateChannel(channel);
        Object object = this.incomingLock;
        synchronized (object) {
            Set<PluginMessageListenerRegistration> registrations = this.incomingByPlugin.get(plugin);
            if (registrations != null) {
                ImmutableSet.Builder builder = ImmutableSet.builder();
                for (PluginMessageListenerRegistration registration : registrations) {
                    if (!registration.getChannel().equals(channel)) continue;
                    builder.add((Object)registration);
                }
                return builder.build();
            }
            return ImmutableSet.of();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isRegistrationValid(PluginMessageListenerRegistration registration) {
        if (registration == null) {
            throw new IllegalArgumentException("Registration cannot be null");
        }
        Object object = this.incomingLock;
        synchronized (object) {
            Set<PluginMessageListenerRegistration> registrations = this.incomingByPlugin.get(registration.getPlugin());
            if (registrations != null) {
                return registrations.contains(registration);
            }
            return false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isIncomingChannelRegistered(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        StandardMessenger.validateChannel(channel);
        Object object = this.incomingLock;
        synchronized (object) {
            Set<PluginMessageListenerRegistration> registrations = this.incomingByPlugin.get(plugin);
            if (registrations != null) {
                for (PluginMessageListenerRegistration registration : registrations) {
                    if (!registration.getChannel().equals(channel)) continue;
                    return true;
                }
            }
            return false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isOutgoingChannelRegistered(Plugin plugin, String channel) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        StandardMessenger.validateChannel(channel);
        Object object = this.outgoingLock;
        synchronized (object) {
            Set<String> channels = this.outgoingByPlugin.get(plugin);
            if (channels != null) {
                return channels.contains(channel);
            }
            return false;
        }
    }

    @Override
    public void dispatchIncomingMessage(Player source, String channel, byte[] message) {
        if (source == null) {
            throw new IllegalArgumentException("Player source cannot be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        StandardMessenger.validateChannel(channel);
        Set<PluginMessageListenerRegistration> registrations = this.getIncomingChannelRegistrations(channel);
        for (PluginMessageListenerRegistration registration : registrations) {
            try {
                registration.getListener().onPluginMessageReceived(channel, source, message);
            }
            catch (Throwable t2) {
                registration.getPlugin().getLogger().log(Level.WARNING, String.format("Plugin %s generated an exception whilst handling plugin message", registration.getPlugin().getDescription().getFullName()), t2);
            }
        }
    }

    public static void validateChannel(String channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null");
        }
        if (channel.length() > 20) {
            throw new ChannelNameTooLongException(channel);
        }
    }

    public static void validatePluginMessage(Messenger messenger, Plugin source, String channel, byte[] message) {
        if (messenger == null) {
            throw new IllegalArgumentException("Messenger cannot be null");
        }
        if (source == null) {
            throw new IllegalArgumentException("Plugin source cannot be null");
        }
        if (!source.isEnabled()) {
            throw new IllegalArgumentException("Plugin must be enabled to send messages");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        if (!messenger.isOutgoingChannelRegistered(source, channel)) {
            throw new ChannelNotRegisteredException(channel);
        }
        if (message.length > 32766) {
            throw new MessageTooLargeException(message);
        }
        StandardMessenger.validateChannel(channel);
    }
}

