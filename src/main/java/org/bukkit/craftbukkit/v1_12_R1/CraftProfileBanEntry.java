/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package org.bukkit.craftbukkit.v1_12_R1;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;

public final class CraftProfileBanEntry
implements BanEntry {
    private final pq list;
    private final GameProfile profile;
    private Date created;
    private String source;
    private Date expiration;
    private String reason;

    public CraftProfileBanEntry(GameProfile profile, pr entry, pq list) {
        this.list = list;
        this.profile = profile;
        this.created = entry.getCreated() != null ? new Date(entry.getCreated().getTime()) : null;
        this.source = entry.getSource();
        this.expiration = entry.c() != null ? new Date(entry.c().getTime()) : null;
        this.reason = entry.d();
    }

    @Override
    public String getTarget() {
        return this.profile.getName();
    }

    @Override
    public Date getCreated() {
        return this.created == null ? null : (Date)this.created.clone();
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public Date getExpiration() {
        return this.expiration == null ? null : (Date)this.expiration.clone();
    }

    @Override
    public void setExpiration(Date expiration) {
        if (expiration != null && expiration.getTime() == new Date(0, 0, 0, 0, 0, 0).getTime()) {
            expiration = null;
        }
        this.expiration = expiration;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void save() {
        pr entry = new pr(this.profile, this.created, this.source, this.expiration, this.reason);
        this.list.void_a(entry);
        try {
            this.list.f();
        }
        catch (IOException ex2) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-players.json, {0}", ex2.getMessage());
        }
    }
}

