/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package org.bukkit.craftbukkit.v1_12_R1;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.metadata.PlayerMetadataStore;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

@SerializableAs(value="Player")
public class CraftOfflinePlayer
implements OfflinePlayer,
ConfigurationSerializable {
    private final GameProfile profile;
    private final CraftServer server;
    private final bfb storage;

    protected CraftOfflinePlayer(CraftServer server, GameProfile profile) {
        this.server = server;
        this.profile = profile;
        this.storage = (bfb)server.console.d[0].U();
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    @Override
    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    @Override
    public String getName() {
        Player player = this.getPlayer();
        if (player != null) {
            return player.getName();
        }
        if (this.profile.getName() != null) {
            return this.profile.getName();
        }
        fy data = this.getBukkitData();
        if (data != null && data.e("lastKnownName")) {
            return data.l("lastKnownName");
        }
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return this.profile.getId();
    }

    public Server getServer() {
        return this.server;
    }

    @Override
    public boolean isOp() {
        return this.server.getHandle().h(this.profile);
    }

    @Override
    public void setOp(boolean value) {
        if (value == this.isOp()) {
            return;
        }
        if (value) {
            this.server.getHandle().a(this.profile);
        } else {
            this.server.getHandle().b(this.profile);
        }
    }

    @Override
    public boolean isBanned() {
        if (this.getName() == null) {
            return false;
        }
        return this.server.getBanList(BanList.Type.NAME).isBanned(this.getName());
    }

    public void setBanned(boolean value) {
        if (this.getName() == null) {
            return;
        }
        if (value) {
            this.server.getBanList(BanList.Type.NAME).addBan(this.getName(), null, null, null);
        } else {
            this.server.getBanList(BanList.Type.NAME).pardon(this.getName());
        }
    }

    @Override
    public boolean isWhitelisted() {
        return this.server.getHandle().k().a(this.profile);
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            this.server.getHandle().d(this.profile);
        } else {
            this.server.getHandle().c(this.profile);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("UUID", this.profile.getId().toString());
        return result;
    }

    public static OfflinePlayer deserialize(Map<String, Object> args) {
        if (args.get("name") != null) {
            return Bukkit.getServer().getOfflinePlayer((String)args.get("name"));
        }
        return Bukkit.getServer().getOfflinePlayer(UUID.fromString((String)args.get("UUID")));
    }

    public String toString() {
        return this.getClass().getSimpleName() + "[UUID=" + this.profile.getId() + "]";
    }

    @Override
    public Player getPlayer() {
        return this.server.getPlayer(this.getUniqueId());
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof OfflinePlayer)) {
            return false;
        }
        OfflinePlayer other = (OfflinePlayer)obj;
        if (this.getUniqueId() == null || other.getUniqueId() == null) {
            return false;
        }
        return this.getUniqueId().equals(other.getUniqueId());
    }

    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
        return hash;
    }

    private fy getData() {
        return this.storage.getPlayerData(this.getUniqueId().toString());
    }

    private fy getBukkitData() {
        fy result = this.getData();
        if (result != null) {
            if (!result.e("bukkit")) {
                result.a("bukkit", new fy());
            }
            result = result.p("bukkit");
        }
        return result;
    }

    private File getDataFile() {
        return new File(this.storage.getPlayerDir(), this.getUniqueId() + ".dat");
    }

    @Override
    public long getFirstPlayed() {
        Player player = this.getPlayer();
        if (player != null) {
            return player.getFirstPlayed();
        }
        fy data = this.getBukkitData();
        if (data != null) {
            if (data.e("firstPlayed")) {
                return data.i("firstPlayed");
            }
            File file = this.getDataFile();
            return file.lastModified();
        }
        return 0;
    }

    @Override
    public long getLastPlayed() {
        Player player = this.getPlayer();
        if (player != null) {
            return player.getLastPlayed();
        }
        fy data = this.getBukkitData();
        if (data != null) {
            if (data.e("lastPlayed")) {
                return data.i("lastPlayed");
            }
            File file = this.getDataFile();
            return file.lastModified();
        }
        return 0;
    }

    @Override
    public boolean hasPlayedBefore() {
        return this.getData() != null;
    }

    @Override
    public Location getBedSpawnLocation() {
        fy data = this.getData();
        if (data == null) {
            return null;
        }
        if (data.e("SpawnX") && data.e("SpawnY") && data.e("SpawnZ")) {
            String spawnWorld = data.l("SpawnWorld");
            if (spawnWorld.equals("")) {
                spawnWorld = this.server.getWorlds().get(0).getName();
            }
            return new Location(this.server.getWorld(spawnWorld), data.h("SpawnX"), data.h("SpawnY"), data.h("SpawnZ"));
        }
        return null;
    }

    public void setMetadata(String metadataKey, MetadataValue metadataValue) {
        this.server.getPlayerMetadata().setMetadata(this, metadataKey, metadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin plugin) {
        this.server.getPlayerMetadata().removeMetadata(this, metadataKey, plugin);
    }
}

