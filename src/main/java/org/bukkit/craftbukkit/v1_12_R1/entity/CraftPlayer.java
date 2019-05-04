/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.io.BaseEncoding
 *  com.mojang.authlib.GameProfile
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.Unpooled
 *  io.netty.util.internal.ConcurrentSet
 *  javax.annotation.Nullable
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.apache.commons.lang3.NotImplementedException
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.BaseEncoding;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ConcurrentSet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Validate;
import org.bukkit.Achievement;
import org.bukkit.BanList;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.craftbukkit.v1_12_R1.CraftEffect;
import org.bukkit.craftbukkit.v1_12_R1.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftSound;
import org.bukkit.craftbukkit.v1_12_R1.CraftStatistic;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.advancement.CraftAdvancement;
import org.bukkit.craftbukkit.v1_12_R1.advancement.CraftAdvancementProgress;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_12_R1.conversations.ConversationTracker;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.map.CraftMapView;
import org.bukkit.craftbukkit.v1_12_R1.map.RenderData;
import org.bukkit.craftbukkit.v1_12_R1.metadata.PlayerMetadataStore;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.SpigotConfig;

@DelegateDeserialization(value=CraftOfflinePlayer.class)
public class CraftPlayer
extends CraftHumanEntity
implements Player {
    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;
    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set<String> channels = new ConcurrentSet();
    private final Map<UUID, Set<WeakReference<Plugin>>> hiddenPlayers = new HashMap<UUID, Set<WeakReference<Plugin>>>();
    private static final WeakHashMap<Plugin, WeakReference<Plugin>> pluginWeakReferences = new WeakHashMap();
    private int hash = 0;
    private double health = 20.0;
    private boolean scaledHealth = false;
    private double healthScale = 20.0;
    private final Player.Spigot spigot;

    public CraftPlayer(CraftServer server, oq entity) {
        super(server, entity);
        this.spigot = new Player.Spigot(){

            @Override
            public InetSocketAddress getRawAddress() {
                return (InetSocketAddress)CraftPlayer.this.getHandle().a.a.getRawAddress();
            }

            @Override
            public boolean getCollidesWithEntities() {
                return CraftPlayer.this.isCollidable();
            }

            @Override
            public void setCollidesWithEntities(boolean collides) {
                CraftPlayer.this.setCollidable(collides);
            }

            @Override
            public void respawn() {
                if (CraftPlayer.this.getHealth() <= 0.0 && CraftPlayer.this.isOnline()) {
                    CraftPlayer.this.server.getServer().am().a(CraftPlayer.this.getHandle(), 0, false);
                }
            }

            @Override
            public void playEffect(Location location, Effect effect, int id2, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius) {
                ht packet2;
                Validate.notNull((Object)location, (String)"Location cannot be null", (Object[])new Object[0]);
                Validate.notNull((Object)((Object)effect), (String)"Effect cannot be null", (Object[])new Object[0]);
                Validate.notNull((Object)location.getWorld(), (String)"World cannot be null", (Object[])new Object[0]);
                if (effect.getType() != Effect.Type.PARTICLE) {
                    int packetData = effect.getId();
                    packet2 = new jf(packetData, new et(location.getBlockX(), location.getBlockY(), location.getBlockZ()), id2, false);
                } else {
                    fj particle = null;
                    int[] extra = null;
                    for (fj p2 : fj.values()) {
                        if (!effect.getName().startsWith(p2.b().replace("_", ""))) continue;
                        particle = p2;
                        if (effect.getData() == null) break;
                        if (effect.getData().equals(Material.class)) {
                            extra = new int[]{id2};
                            break;
                        }
                        extra = new int[]{data << 12 | id2 & 4095};
                        break;
                    }
                    if (extra == null) {
                        extra = new int[]{};
                    }
                    packet2 = new jg(particle, true, (float)location.getX(), (float)location.getY(), (float)location.getZ(), offsetX, offsetY, offsetZ, speed, particleCount, extra);
                }
                radius *= radius;
                if (CraftPlayer.this.getHandle().a == null) {
                    return;
                }
                if (!location.getWorld().equals(CraftPlayer.this.getWorld())) {
                    return;
                }
                int distance = (int)CraftPlayer.this.getLocation().distanceSquared(location);
                if (distance <= radius) {
                    ht packet2;
                    CraftPlayer.this.getHandle().a.a(packet2);
                }
            }

            @Override
            public String getLocale() {
                return CraftPlayer.this.getHandle().bW;
            }

            @Override
            public Set<Player> getHiddenPlayers() {
                HashSet<Player> ret = new HashSet<Player>();
                for (UUID u2 : CraftPlayer.this.hiddenPlayers.keySet()) {
                    ret.add(CraftPlayer.this.getServer().getPlayer(u2));
                }
                return Collections.unmodifiableSet(ret);
            }

            @Override
            public void sendMessage(BaseComponent component) {
                this.sendMessage(new BaseComponent[]{component});
            }

            @Override
            public /* varargs */ void sendMessage(BaseComponent ... components) {
                if (CraftPlayer.this.getHandle().a == null) {
                    return;
                }
                in packet = new in(null, hf.a);
                packet.components = components;
                CraftPlayer.this.getHandle().a.a(packet);
            }

            @Override
            public void sendMessage(ChatMessageType position, BaseComponent component) {
                this.sendMessage(position, new BaseComponent[]{component});
            }

            @Override
            public /* varargs */ void sendMessage(ChatMessageType position, BaseComponent ... components) {
                if (CraftPlayer.this.getHandle().a == null) {
                    return;
                }
                in packet = new in(null, hf.a((byte)position.ordinal()));
                if (position == ChatMessageType.ACTION_BAR) {
                    components = new BaseComponent[]{new TextComponent(BaseComponent.toLegacyText((BaseComponent[])components))};
                }
                packet.components = components;
                CraftPlayer.this.getHandle().a.a(packet);
            }
        };
        this.firstPlayed = System.currentTimeMillis();
    }

    public GameProfile getProfile() {
        return this.getHandle().da();
    }

    @Override
    public boolean isOp() {
        return this.server.getHandle().h(this.getProfile());
    }

    @Override
    public void setOp(boolean value) {
        if (value == this.isOp()) {
            return;
        }
        if (value) {
            this.server.getHandle().a(this.getProfile());
        } else {
            this.server.getHandle().b(this.getProfile());
        }
        this.perm.recalculatePermissions();
    }

    @Override
    public boolean isOnline() {
        return this.server.getPlayer(this.getUniqueId()) != null;
    }

    @Override
    public InetSocketAddress getAddress() {
        if (this.getHandle().a == null) {
            return null;
        }
        SocketAddress addr = this.getHandle().a.a.b();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress)addr;
        }
        return null;
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        if (ignorePose) {
            return 1.62;
        }
        return this.getEyeHeight();
    }

    @Override
    public void sendRawMessage(String message) {
        if (this.getHandle().a == null) {
            return;
        }
        for (hh component : CraftChatMessage.fromString(message)) {
            this.getHandle().a.a(new in(component));
        }
    }

    @Override
    public void sendMessage(String message) {
        if (!this.conversationTracker.isConversingModaly()) {
            this.sendRawMessage(message);
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public String getDisplayName() {
        return this.getHandle().displayName;
    }

    @Override
    public void setDisplayName(String name) {
        this.getHandle().displayName = name == null ? this.getName() : name;
    }

    @Override
    public String getPlayerListName() {
        return this.getHandle().listName == null ? this.getName() : CraftChatMessage.fromComponent(this.getHandle().listName, a.p);
    }

    @Override
    public void setPlayerListName(String name) {
        if (name == null) {
            name = this.getName();
        }
        this.getHandle().listName = name.equals(this.getName()) ? null : CraftChatMessage.fromString(name)[0];
        for (oq player : this.server.getHandle().v()) {
            if (!player.getBukkitEntity().canSee(this)) continue;
            player.a.a(new jp(jp.a.d, this.getHandle()));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer)) {
            return false;
        }
        OfflinePlayer other = (OfflinePlayer)obj;
        if (this.getUniqueId() == null || other.getUniqueId() == null) {
            return false;
        }
        boolean uuidEquals = this.getUniqueId().equals(other.getUniqueId());
        boolean idEquals = true;
        if (other instanceof CraftPlayer) {
            idEquals = this.getEntityId() == ((CraftPlayer)other).getEntityId();
        }
        return uuidEquals && idEquals;
    }

    @Override
    public void kickPlayer(String message) {
        AsyncCatcher.catchOp("player kick");
        if (this.getHandle().a == null) {
            return;
        }
        this.getHandle().a.disconnect(message == null ? "" : message);
    }

    @Override
    public void setCompassTarget(Location loc) {
        if (this.getHandle().a == null) {
            return;
        }
        this.getHandle().a.a(new kn(new et(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
    }

    @Override
    public Location getCompassTarget() {
        return this.getHandle().compassTarget;
    }

    @Override
    public void chat(String msg) {
        if (this.getHandle().a == null) {
            return;
        }
        this.getHandle().a.chat(msg, false);
    }

    @Override
    public boolean performCommand(String command) {
        return this.server.dispatchCommand(this, command);
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        if (this.getHandle().a == null) {
            return;
        }
        String instrumentName = null;
        switch (instrument) {
            case 0: {
                instrumentName = "harp";
                break;
            }
            case 1: {
                instrumentName = "basedrum";
                break;
            }
            case 2: {
                instrumentName = "snare";
                break;
            }
            case 3: {
                instrumentName = "hat";
                break;
            }
            case 4: {
                instrumentName = "bass";
                break;
            }
            case 5: {
                instrumentName = "flute";
                break;
            }
            case 6: {
                instrumentName = "bell";
                break;
            }
            case 7: {
                instrumentName = "guitar";
                break;
            }
            case 8: {
                instrumentName = "chime";
                break;
            }
            case 9: {
                instrumentName = "xylophone";
            }
        }
        float f2 = (float)Math.pow(2.0, ((double)note - 12.0) / 12.0);
        this.getHandle().a.a(new kq(CraftSound.getSoundEffect("block.note." + instrumentName), qg.c, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f2));
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        if (this.getHandle().a == null) {
            return;
        }
        String instrumentName = null;
        switch (instrument.ordinal()) {
            case 0: {
                instrumentName = "harp";
                break;
            }
            case 1: {
                instrumentName = "basedrum";
                break;
            }
            case 2: {
                instrumentName = "snare";
                break;
            }
            case 3: {
                instrumentName = "hat";
                break;
            }
            case 4: {
                instrumentName = "bass";
                break;
            }
            case 5: {
                instrumentName = "flute";
                break;
            }
            case 6: {
                instrumentName = "bell";
                break;
            }
            case 7: {
                instrumentName = "guitar";
                break;
            }
            case 8: {
                instrumentName = "chime";
                break;
            }
            case 9: {
                instrumentName = "xylophone";
            }
        }
        float f2 = (float)Math.pow(2.0, ((double)note.getId() - 12.0) / 12.0);
        this.getHandle().a.a(new kq(CraftSound.getSoundEffect("block.note." + instrumentName), qg.c, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f2));
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        this.playSound(loc, sound, SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        this.playSound(loc, sound, SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, Sound sound, SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null || this.getHandle().a == null) {
            return;
        }
        kq packet = new kq(CraftSound.getSoundEffect(CraftSound.getSound(sound)), qg.valueOf(category.name()), loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
        this.getHandle().a.a(packet);
    }

    @Override
    public void playSound(Location loc, String sound, SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null || this.getHandle().a == null) {
            return;
        }
        ix packet = new ix(sound, qg.valueOf(category.name()), loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
        this.getHandle().a.a(packet);
    }

    @Override
    public void stopSound(Sound sound) {
        this.stopSound(sound, null);
    }

    @Override
    public void stopSound(String sound) {
        this.stopSound(sound, null);
    }

    @Override
    public void stopSound(Sound sound, SoundCategory category) {
        this.stopSound(CraftSound.getSound(sound), category);
    }

    @Override
    public void stopSound(String sound, SoundCategory category) {
        if (this.getHandle().a == null) {
            return;
        }
        gy packetdataserializer = new gy(Unpooled.buffer());
        packetdataserializer.a(category == null ? "" : qg.valueOf(category.name()).a());
        packetdataserializer.a(sound);
        this.getHandle().a.a(new iw("MC|StopSound", packetdataserializer));
    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        if (this.getHandle().a == null) {
            return;
        }
        this.org_bukkit_entity_Player$Spigot_spigot().playEffect(loc, effect, data, 0, 0.0f, 0.0f, 0.0f, 1.0f, 1, 64);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        if (data != null) {
            Validate.isTrue((boolean)(effect.getData() != null && effect.getData().isAssignableFrom(data.getClass())), (String)"Wrong kind of data for this effect!", (Object[])new Object[0]);
        } else {
            Validate.isTrue((boolean)(effect.getData() == null), (String)"Wrong kind of data for this effect!", (Object[])new Object[0]);
        }
        int datavalue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
        this.playEffect(loc, effect, datavalue);
    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        this.sendBlockChange(loc, material.getId(), data);
    }

    @Override
    public void sendBlockChange(Location loc, int material, byte data) {
        if (this.getHandle().a == null) {
            return;
        }
        ij packet = new ij(((CraftWorld)loc.getWorld()).getHandle(), new et(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        packet.b = CraftMagicNumbers.getBlock(material).a(data);
        this.getHandle().a.a(packet);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines) {
        if (this.getHandle().a == null) {
            return;
        }
        if (lines == null) {
            lines = new String[4];
        }
        Validate.notNull((Object)loc, (String)"Location can not be null", (Object[])new Object[0]);
        if (lines.length < 4) {
            throw new IllegalArgumentException("Must have at least 4 lines");
        }
        hh[] components = CraftSign.sanitizeLines(lines);
        awc sign = new awc();
        sign.a(new et(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        System.arraycopy(components, 0, sign.a, 0, sign.a.length);
        this.getHandle().a.a(sign.c());
    }

    @Override
    public boolean sendChunkChange(Location loc, int sx2, int sy2, int sz2, byte[] data) {
        if (this.getHandle().a == null) {
            return false;
        }
        throw new NotImplementedException("Chunk changes do not yet work");
    }

    @Override
    public void sendMap(MapView map) {
        if (this.getHandle().a == null) {
            return;
        }
        RenderData data = ((CraftMapView)map).render(this);
        ArrayList<beu> icons = new ArrayList<beu>();
        for (MapCursor cursor : data.cursors) {
            if (!cursor.isVisible()) continue;
            icons.add(new beu(beu.a.a(cursor.getRawType()), cursor.getX(), cursor.getY(), cursor.getDirection()));
        }
        ji packet = new ji(map.getId(), map.getScale().getValue(), true, icons, data.buffer, 0, 0, 128, 128);
        this.getHandle().a.a(packet);
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        Preconditions.checkArgument((boolean)(location != null), (Object)"location");
        Preconditions.checkArgument((boolean)(location.getWorld() != null), (Object)"location.world");
        location.checkFinite();
        oq entity = this.getHandle();
        if (this.getHealth() == 0.0 || entity.F) {
            return false;
        }
        if (entity.a == null) {
            return false;
        }
        if (entity.aT()) {
            return false;
        }
        Location from = this.getLocation();
        Location to2 = location;
        PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, to2, cause);
        this.server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        entity.o();
        from = event.getFrom();
        to2 = event.getTo();
        oo fromWorld = ((CraftWorld)from.getWorld()).getHandle();
        oo toWorld = ((CraftWorld)to2.getWorld()).getHandle();
        if (this.getHandle().by != this.getHandle().bx) {
            this.getHandle().p();
        }
        if (fromWorld == toWorld) {
            entity.a.teleport(to2);
        } else {
            this.server.getHandle().moveToWorld(entity, toWorld.dimension, true, to2, true);
        }
        return true;
    }

    @Override
    public void setSneaking(boolean sneak) {
        this.getHandle().e(sneak);
    }

    @Override
    public boolean isSneaking() {
        return this.getHandle().aU();
    }

    @Override
    public boolean isSprinting() {
        return this.getHandle().aV();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        this.getHandle().f(sprinting);
    }

    @Override
    public void loadData() {
        this.server.getHandle().q.b(this.getHandle());
    }

    @Override
    public void saveData() {
        this.server.getHandle().q.a(this.getHandle());
    }

    @Deprecated
    @Override
    public void updateInventory() {
        this.getHandle().a(this.getHandle().by);
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        this.getHandle().fauxSleeping = isSleeping;
        ((CraftWorld)this.getWorld()).getHandle().checkSleepStatus();
    }

    @Override
    public boolean isSleepingIgnored() {
        return this.getHandle().fauxSleeping;
    }

    @Override
    public void awardAchievement(Achievement achievement) {
        throw new UnsupportedOperationException("Not supported in this Minecraft version.");
    }

    @Override
    public void removeAchievement(Achievement achievement) {
        throw new UnsupportedOperationException("Not supported in this Minecraft version.");
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        throw new UnsupportedOperationException("Not supported in this Minecraft version.");
    }

    @Override
    public void incrementStatistic(Statistic statistic) {
        this.incrementStatistic(statistic, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic) {
        this.decrementStatistic(statistic, 1);
    }

    @Override
    public int getStatistic(Statistic statistic) {
        Validate.notNull((Object)((Object)statistic), (String)"Statistic cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(statistic.getType() == Statistic.Type.UNTYPED), (String)"Must supply additional paramater for this statistic", (Object[])new Object[0]);
        return this.getHandle().E().a(CraftStatistic.getNMSStatistic(statistic));
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {
        Validate.isTrue((boolean)(amount > 0), (String)"Amount must be greater than 0", (Object[])new Object[0]);
        this.setStatistic(statistic, this.getStatistic(statistic) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) {
        Validate.isTrue((boolean)(amount > 0), (String)"Amount must be greater than 0", (Object[])new Object[0]);
        this.setStatistic(statistic, this.getStatistic(statistic) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) {
        Validate.notNull((Object)((Object)statistic), (String)"Statistic cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(statistic.getType() == Statistic.Type.UNTYPED), (String)"Must supply additional paramater for this statistic", (Object[])new Object[0]);
        Validate.isTrue((boolean)(newValue >= 0), (String)"Value must be greater than or equal to 0", (Object[])new Object[0]);
        qo nmsStatistic = CraftStatistic.getNMSStatistic(statistic);
        this.getHandle().E().a(this.getHandle(), nmsStatistic, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {
        this.incrementStatistic(statistic, material, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) {
        this.decrementStatistic(statistic, material, 1);
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) {
        Validate.notNull((Object)((Object)statistic), (String)"Statistic cannot be null", (Object[])new Object[0]);
        Validate.notNull((Object)((Object)material), (String)"Material cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(statistic.getType() == Statistic.Type.BLOCK || statistic.getType() == Statistic.Type.ITEM), (String)"This statistic does not take a Material parameter", (Object[])new Object[0]);
        qo nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull((Object)nmsStatistic, (String)"The supplied Material does not have a corresponding statistic", (Object[])new Object[0]);
        return this.getHandle().E().a(nmsStatistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        Validate.isTrue((boolean)(amount > 0), (String)"Amount must be greater than 0", (Object[])new Object[0]);
        this.setStatistic(statistic, material, this.getStatistic(statistic, material) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) {
        Validate.isTrue((boolean)(amount > 0), (String)"Amount must be greater than 0", (Object[])new Object[0]);
        this.setStatistic(statistic, material, this.getStatistic(statistic, material) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) {
        Validate.notNull((Object)((Object)statistic), (String)"Statistic cannot be null", (Object[])new Object[0]);
        Validate.notNull((Object)((Object)material), (String)"Material cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(newValue >= 0), (String)"Value must be greater than or equal to 0", (Object[])new Object[0]);
        Validate.isTrue((boolean)(statistic.getType() == Statistic.Type.BLOCK || statistic.getType() == Statistic.Type.ITEM), (String)"This statistic does not take a Material parameter", (Object[])new Object[0]);
        qo nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull((Object)nmsStatistic, (String)"The supplied Material does not have a corresponding statistic", (Object[])new Object[0]);
        this.getHandle().E().a(this.getHandle(), nmsStatistic, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) {
        this.incrementStatistic(statistic, entityType, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) {
        this.decrementStatistic(statistic, entityType, 1);
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) {
        Validate.notNull((Object)((Object)statistic), (String)"Statistic cannot be null", (Object[])new Object[0]);
        Validate.notNull((Object)((Object)entityType), (String)"EntityType cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(statistic.getType() == Statistic.Type.ENTITY), (String)"This statistic does not take an EntityType parameter", (Object[])new Object[0]);
        qo nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull((Object)nmsStatistic, (String)"The supplied EntityType does not have a corresponding statistic", (Object[])new Object[0]);
        return this.getHandle().E().a(nmsStatistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue((boolean)(amount > 0), (String)"Amount must be greater than 0", (Object[])new Object[0]);
        this.setStatistic(statistic, entityType, this.getStatistic(statistic, entityType) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue((boolean)(amount > 0), (String)"Amount must be greater than 0", (Object[])new Object[0]);
        this.setStatistic(statistic, entityType, this.getStatistic(statistic, entityType) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        Validate.notNull((Object)((Object)statistic), (String)"Statistic cannot be null", (Object[])new Object[0]);
        Validate.notNull((Object)((Object)entityType), (String)"EntityType cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(newValue >= 0), (String)"Value must be greater than or equal to 0", (Object[])new Object[0]);
        Validate.isTrue((boolean)(statistic.getType() == Statistic.Type.ENTITY), (String)"This statistic does not take an EntityType parameter", (Object[])new Object[0]);
        qo nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull((Object)nmsStatistic, (String)"The supplied EntityType does not have a corresponding statistic", (Object[])new Object[0]);
        this.getHandle().E().a(this.getHandle(), nmsStatistic, newValue);
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        this.getHandle().timeOffset = time;
        this.getHandle().relativeTime = relative;
    }

    @Override
    public long getPlayerTimeOffset() {
        return this.getHandle().timeOffset;
    }

    @Override
    public long getPlayerTime() {
        return this.getHandle().getPlayerTime();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return this.getHandle().relativeTime;
    }

    @Override
    public void resetPlayerTime() {
        this.setPlayerTime(0, true);
    }

    @Override
    public void setPlayerWeather(WeatherType type) {
        this.getHandle().setPlayerWeather(type, true);
    }

    @Override
    public WeatherType getPlayerWeather() {
        return this.getHandle().getPlayerWeather();
    }

    @Override
    public void resetPlayerWeather() {
        this.getHandle().resetPlayerWeather();
    }

    @Override
    public boolean isBanned() {
        return this.server.getBanList(BanList.Type.NAME).isBanned(this.getName());
    }

    @Override
    public boolean isWhitelisted() {
        return this.server.getHandle().k().a(this.getProfile());
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            this.server.getHandle().d(this.getProfile());
        } else {
            this.server.getHandle().c(this.getProfile());
        }
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (this.getHandle().a == null) {
            return;
        }
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }
        this.getHandle().a(ams.a(mode.getValue()));
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.getByValue(this.getHandle().c.b().a());
    }

    @Override
    public void giveExp(int exp) {
        this.getHandle().m(exp);
    }

    @Override
    public void giveExpLevels(int levels) {
        this.getHandle().a(levels);
    }

    @Override
    public float getExp() {
        return this.getHandle().bR;
    }

    @Override
    public void setExp(float exp) {
        Preconditions.checkArgument((boolean)((double)exp >= 0.0 && (double)exp <= 1.0), (String)"Experience progress must be between 0.0 and 1.0 (%s)", (Object)Float.valueOf(exp));
        this.getHandle().bR = exp;
        this.getHandle().cj = -1;
    }

    @Override
    public int getLevel() {
        return this.getHandle().bP;
    }

    @Override
    public void setLevel(int level) {
        this.getHandle().bP = level;
        this.getHandle().cj = -1;
    }

    @Override
    public int getTotalExperience() {
        return this.getHandle().bQ;
    }

    @Override
    public void setTotalExperience(int exp) {
        this.getHandle().bQ = exp;
    }

    @Override
    public float getExhaustion() {
        return this.getHandle().di().c;
    }

    @Override
    public void setExhaustion(float value) {
        this.getHandle().di().c = value;
    }

    @Override
    public float getSaturation() {
        return this.getHandle().di().b;
    }

    @Override
    public void setSaturation(float value) {
        this.getHandle().di().b = value;
    }

    @Override
    public int getFoodLevel() {
        return this.getHandle().di().a;
    }

    @Override
    public void setFoodLevel(int value) {
        this.getHandle().di().a = value;
    }

    @Override
    public Location getBedSpawnLocation() {
        World world = this.getServer().getWorld(this.getHandle().spawnWorld);
        et bed2 = this.getHandle().de();
        if (world != null && bed2 != null && (bed2 = aed.a(((CraftWorld)world).getHandle(), bed2, this.getHandle().df())) != null) {
            return new Location(world, bed2.p(), bed2.q(), bed2.r());
        }
        return null;
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        this.setBedSpawnLocation(location, false);
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean override) {
        if (location == null) {
            this.getHandle().b(null, override);
        } else {
            this.getHandle().b(new et(location.getBlockX(), location.getBlockY(), location.getBlockZ()), override);
            this.getHandle().spawnWorld = location.getWorld().getName();
        }
    }

    @Nullable
    private static WeakReference<Plugin> getPluginWeakReference(@Nullable Plugin plugin) {
        return plugin == null ? null : pluginWeakReferences.computeIfAbsent(plugin, WeakReference::new);
    }

    @Deprecated
    @Override
    public void hidePlayer(Player player) {
        this.hidePlayer0(null, player);
    }

    @Override
    public void hidePlayer(Plugin plugin, Player player) {
        Validate.notNull((Object)plugin, (String)"Plugin cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)plugin.isEnabled(), (String)"Plugin attempted to hide player while disabled", (Object[])new Object[0]);
        this.hidePlayer0(plugin, player);
    }

    private void hidePlayer0(@Nullable Plugin plugin, Player player) {
        Validate.notNull((Object)player, (String)"hidden player cannot be null", (Object[])new Object[0]);
        if (this.getHandle().a == null) {
            return;
        }
        if (this.equals(player)) {
            return;
        }
        Set<WeakReference<Plugin>> hidingPlugins = this.hiddenPlayers.get(player.getUniqueId());
        if (hidingPlugins != null) {
            hidingPlugins.add(CraftPlayer.getPluginWeakReference(plugin));
            return;
        }
        hidingPlugins = new HashSet<WeakReference<Plugin>>();
        hidingPlugins.add(CraftPlayer.getPluginWeakReference(plugin));
        this.hiddenPlayers.put(player.getUniqueId(), hidingPlugins);
        ol tracker = ((oo)this.entity.l).L;
        oq other = ((CraftPlayer)player).getHandle();
        os entry = tracker.d.a(other.S());
        if (entry != null) {
            entry.d(this.getHandle());
        }
        if (other.sentListPacket) {
            this.getHandle().a.a(new jp(jp.a.e, other));
        }
    }

    @Deprecated
    @Override
    public void showPlayer(Player player) {
        this.showPlayer0(null, player);
    }

    @Override
    public void showPlayer(Plugin plugin, Player player) {
        Validate.notNull((Object)plugin, (String)"Plugin cannot be null", (Object[])new Object[0]);
        this.showPlayer0(plugin, player);
    }

    private void showPlayer0(@Nullable Plugin plugin, Player player) {
        Validate.notNull((Object)player, (String)"shown player cannot be null", (Object[])new Object[0]);
        if (this.getHandle().a == null) {
            return;
        }
        if (this.equals(player)) {
            return;
        }
        Set<WeakReference<Plugin>> hidingPlugins = this.hiddenPlayers.get(player.getUniqueId());
        if (hidingPlugins == null) {
            return;
        }
        hidingPlugins.remove(CraftPlayer.getPluginWeakReference(plugin));
        if (!hidingPlugins.isEmpty()) {
            return;
        }
        this.hiddenPlayers.remove(player.getUniqueId());
        ol tracker = ((oo)this.entity.l).L;
        oq other = ((CraftPlayer)player).getHandle();
        this.getHandle().a.a(new jp(jp.a.a, other));
        os entry = tracker.d.a(other.S());
        if (entry != null && !entry.z.contains(this.getHandle())) {
            entry.b(this.getHandle());
        }
    }

    public void removeDisconnectingPlayer(Player player) {
        this.hiddenPlayers.remove(player.getUniqueId());
    }

    @Override
    public boolean canSee(Player player) {
        return !this.hiddenPlayers.containsKey(player.getUniqueId());
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("name", this.getName());
        return result;
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public oq getHandle() {
        return (oq)this.entity;
    }

    @Override
    public void setHandle(aed entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftPlayer{name=" + this.getName() + '}';
    }

    @Override
    public int hashCode() {
        if (this.hash == 0 || this.hash == 485) {
            this.hash = 485 + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
        }
        return this.hash;
    }

    @Override
    public long getFirstPlayed() {
        return this.firstPlayed;
    }

    @Override
    public long getLastPlayed() {
        return this.lastPlayed;
    }

    @Override
    public boolean hasPlayedBefore() {
        return this.hasPlayedBefore;
    }

    public void setFirstPlayed(long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    public void readExtraData(fy nbttagcompound) {
        this.hasPlayedBefore = true;
        if (nbttagcompound.e("bukkit")) {
            fy data = nbttagcompound.p("bukkit");
            if (data.e("firstPlayed")) {
                this.firstPlayed = data.i("firstPlayed");
                this.lastPlayed = data.i("lastPlayed");
            }
            if (data.e("newExp")) {
                oq handle = this.getHandle();
                handle.newExp = data.h("newExp");
                handle.newTotalExp = data.h("newTotalExp");
                handle.newLevel = data.h("newLevel");
                handle.expToDrop = data.h("expToDrop");
                handle.keepLevel = data.q("keepLevel");
            }
        }
    }

    public void setExtraData(fy nbttagcompound) {
        if (!nbttagcompound.e("bukkit")) {
            nbttagcompound.a("bukkit", new fy());
        }
        fy data = nbttagcompound.p("bukkit");
        oq handle = this.getHandle();
        data.a("newExp", handle.newExp);
        data.a("newTotalExp", handle.newTotalExp);
        data.a("newLevel", handle.newLevel);
        data.a("expToDrop", handle.expToDrop);
        data.a("keepLevel", handle.keepLevel);
        data.a("firstPlayed", this.getFirstPlayed());
        data.a("lastPlayed", System.currentTimeMillis());
        data.a("lastKnownName", handle.h_());
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return this.conversationTracker.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        this.conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        this.conversationTracker.abandonConversation(conversation, details);
    }

    @Override
    public void acceptConversationInput(String input) {
        this.conversationTracker.acceptConversationInput(input);
    }

    @Override
    public boolean isConversing() {
        return this.conversationTracker.isConversing();
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(this.server.getMessenger(), source, channel, message);
        if (this.channels.contains(channel) || SpigotConfig.bungee) {
            iw packet = new iw(channel, new gy(Unpooled.wrappedBuffer((byte[])message)));
            this.getHandle().a.a(packet);
        }
    }

    @Override
    public void setTexturePack(String url) {
        this.setResourcePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        Validate.notNull((Object)url, (String)"Resource pack URL cannot be null", (Object[])new Object[0]);
        this.getHandle().a(url, "null");
    }

    @Override
    public void setResourcePack(String url, byte[] hash) {
        Validate.notNull((Object)url, (String)"Resource pack URL cannot be null", (Object[])new Object[0]);
        Validate.notNull((Object)hash, (String)"Resource pack hash cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(hash.length == 20), (String)("Resource pack hash should be 20 bytes long but was " + hash.length), (Object[])new Object[0]);
        this.getHandle().a(url, BaseEncoding.base16().lowerCase().encode(hash));
    }

    public void addChannel(String channel) {
        if (this.channels.add(channel)) {
            this.server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
        }
    }

    public void removeChannel(String channel) {
        if (this.channels.remove(channel)) {
            this.server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return ImmutableSet.copyOf(this.channels);
    }

    public void sendSupportedChannels() {
        if (this.getHandle().a == null) {
            return;
        }
        Set<String> listening = this.server.getMessenger().getIncomingChannels();
        if (!listening.isEmpty()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            for (String channel : listening) {
                try {
                    stream.write(channel.getBytes("UTF8"));
                    stream.write(0);
                }
                catch (IOException ex2) {
                    Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + this.getName(), ex2);
                }
            }
            this.getHandle().a.a(new iw("REGISTER", new gy(Unpooled.wrappedBuffer((byte[])stream.toByteArray()))));
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getPlayerMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getPlayerMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        afr container = this.getHandle().by;
        if (container.getBukkitView().getType() != prop.getType()) {
            return false;
        }
        this.getHandle().a(container, prop.getId(), value);
        return true;
    }

    public void disconnect(String reason) {
        this.conversationTracker.abandonAllConversations();
        this.perm.clearPermissions();
    }

    @Override
    public boolean isFlying() {
        return this.getHandle().bO.b;
    }

    @Override
    public void setFlying(boolean value) {
        if (!this.getAllowFlight() && value) {
            throw new IllegalArgumentException("Cannot make player fly if getAllowFlight() is false");
        }
        this.getHandle().bO.b = value;
        this.getHandle().w();
    }

    @Override
    public boolean getAllowFlight() {
        return this.getHandle().bO.c;
    }

    @Override
    public void setAllowFlight(boolean value) {
        if (this.isFlying() && !value) {
            this.getHandle().bO.b = false;
        }
        this.getHandle().bO.c = value;
        this.getHandle().w();
    }

    @Override
    public int getNoDamageTicks() {
        if (this.getHandle().ck > 0) {
            return Math.max(this.getHandle().ck, this.getHandle().V);
        }
        return this.getHandle().V;
    }

    @Override
    public void setFlySpeed(float value) {
        this.validateSpeed(value);
        oq player = this.getHandle();
        player.bO.f = value / 2.0f;
        player.w();
    }

    @Override
    public void setWalkSpeed(float value) {
        this.validateSpeed(value);
        oq player = this.getHandle();
        player.bO.g = value / 2.0f;
        player.w();
    }

    @Override
    public float getFlySpeed() {
        return this.getHandle().bO.f * 2.0f;
    }

    @Override
    public float getWalkSpeed() {
        return this.getHandle().bO.g * 2.0f;
    }

    private void validateSpeed(float value) {
        if (value < 0.0f) {
            if (value < -1.0f) {
                throw new IllegalArgumentException("" + value + " is too low");
            }
        } else if (value > 1.0f) {
            throw new IllegalArgumentException("" + value + " is too high");
        }
    }

    @Override
    public void setMaxHealth(double amount) {
        super.setMaxHealth(amount);
        this.health = Math.min(this.health, this.health);
        this.getHandle().u();
    }

    @Override
    public void resetMaxHealth() {
        super.resetMaxHealth();
        this.getHandle().u();
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return this.server.getScoreboardManager().getPlayerBoard(this);
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        Validate.notNull((Object)scoreboard, (String)"Scoreboard cannot be null", (Object[])new Object[0]);
        pa connection = this.getHandle().a;
        if (connection == null) {
            return;
        }
        if (connection.isDisconnected()) {
            // empty if block
        }
        this.server.getScoreboardManager().setPlayerBoard(this, scoreboard);
    }

    @Override
    public void setHealthScale(double value) {
        Validate.isTrue((boolean)((float)value > 0.0f), (String)"Must be greater than 0", (Object[])new Object[0]);
        this.healthScale = value;
        this.scaledHealth = true;
        this.updateScaledHealth();
    }

    @Override
    public double getHealthScale() {
        return this.healthScale;
    }

    @Override
    public void setHealthScaled(boolean scale) {
        this.scaledHealth = scale;
        if (this.scaledHealth != this.scaledHealth) {
            this.updateScaledHealth();
        }
    }

    @Override
    public boolean isHealthScaled() {
        return this.scaledHealth;
    }

    public float getScaledHealth() {
        return (float)(this.isHealthScaled() ? this.getHealth() * this.getHealthScale() / this.getMaxHealth() : this.getHealth());
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    public void setRealHealth(double health) {
        this.health = health;
    }

    public void updateScaledHealth() {
        wi attributemapserver = (wi)this.getHandle().cm();
        Collection<wd> set = attributemapserver.c();
        this.injectScaledMaxHealth(set, true);
        if (this.getHandle().a != null) {
            this.getHandle().a.a(new kv(this.getHandle().S(), set));
            this.sendHealthUpdate();
        }
        this.getHandle().V().b(vq.f, Float.valueOf(this.getScaledHealth()));
        this.getHandle().maxHealthCache = this.getMaxHealth();
    }

    public void sendHealthUpdate() {
        this.getHandle().a.a(new ki(this.getScaledHealth(), this.getHandle().di().a(), this.getHandle().di().e()));
    }

    public void injectScaledMaxHealth(Collection<wd> collection, boolean force) {
        double healthMod;
        if (!this.scaledHealth && !force) {
            return;
        }
        for (wd genericInstance : collection) {
            if (!genericInstance.a().a().equals("generic.maxHealth")) continue;
            collection.remove(genericInstance);
            break;
        }
        double d2 = healthMod = this.scaledHealth ? this.healthScale : this.getMaxHealth();
        if (healthMod >= 3.4028234663852886E38 || healthMod <= 0.0) {
            healthMod = 20.0;
            this.getServer().getLogger().warning(this.getName() + " tried to crash the server with a large health attribute");
        }
        collection.add(new wh(this.getHandle().cm(), new wj(null, "generic.maxHealth", healthMod, 0.0, 3.4028234663852886E38).a("Max Health").a(true)));
    }

    @Override
    public Entity getSpectatorTarget() {
        vg followed = this.getHandle().H();
        return followed == this.getHandle() ? null : followed.getBukkitEntity();
    }

    @Override
    public void setSpectatorTarget(Entity entity) {
        Preconditions.checkArgument((boolean)(this.getGameMode() == GameMode.SPECTATOR), (Object)"Player must be in spectator mode");
        this.getHandle().e(entity == null ? null : ((CraftEntity)entity).getHandle());
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        this.sendTitle(title, subtitle, 10, 70, 20);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (this.getHandle().a == null) {
            return;
        }
        kp times = new kp(fadeIn, stay, fadeOut);
        this.getHandle().a.a(times);
        if (title != null) {
            kp packetTitle = new kp(kp.a.a, CraftChatMessage.fromString(title)[0]);
            this.getHandle().a.a(packetTitle);
        }
        if (subtitle != null) {
            kp packetSubtitle = new kp(kp.a.b, CraftChatMessage.fromString(subtitle)[0]);
            this.getHandle().a.a(packetSubtitle);
        }
    }

    @Override
    public void resetTitle() {
        if (this.getHandle().a == null) {
            return;
        }
        kp packetReset = new kp(kp.a.f, null);
        this.getHandle().a.a(packetReset);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    @Override
    public void spawnParticle(Particle particle, double x2, double y2, double z2, int count) {
        this.spawnParticle(particle, x2, y2, z2, count, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x2, double y2, double z2, int count, T data) {
        this.spawnParticle(particle, x2, y2, z2, count, 0.0, 0.0, 0.0, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double x2, double y2, double z2, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, x2, y2, z2, count, offsetX, offsetY, offsetZ, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x2, double y2, double z2, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawnParticle(particle, x2, y2, z2, count, offsetX, offsetY, offsetZ, 1.0, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(Particle particle, double x2, double y2, double z2, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, x2, y2, z2, count, offsetX, offsetY, offsetZ, extra, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x2, double y2, double z2, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        if (this.getHandle().a == null) {
            return;
        }
        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("data should be " + particle.getDataType() + " got " + data.getClass());
        }
        jg packetplayoutworldparticles = new jg(CraftParticle.toNMS(particle), true, (float)x2, (float)y2, (float)z2, (float)offsetX, (float)offsetY, (float)offsetZ, (float)extra, count, CraftParticle.toData(particle, data));
        this.getHandle().a.a(packetplayoutworldparticles);
    }

    @Override
    public AdvancementProgress getAdvancementProgress(Advancement advancement) {
        Preconditions.checkArgument((boolean)(advancement != null), (Object)"advancement");
        CraftAdvancement craft = (CraftAdvancement)advancement;
        np data = this.getHandle().P();
        k progress = data.b(craft.getHandle());
        return new CraftAdvancementProgress(craft, data, progress);
    }

    @Override
    public String getLocale() {
        return this.getHandle().bW;
    }

    @Override
    public Player.Spigot org_bukkit_entity_Player$Spigot_spigot() {
        return this.spigot;
    }

}

