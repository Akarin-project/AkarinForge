/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 *  com.mojang.authlib.GameProfile
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftProfileBanEntry;

public class CraftProfileBanList
implements BanList {
    private final pq list;

    public CraftProfileBanList(pq list) {
        this.list = list;
    }

    @Override
    public BanEntry getBanEntry(String target) {
        Validate.notNull((Object)target, (String)"Target cannot be null", (Object[])new Object[0]);
        GameProfile profile = MinecraftServer.getServerInst().aB().a(target);
        if (profile == null) {
            return null;
        }
        pr entry = (pr)this.list.b(profile);
        if (entry == null) {
            return null;
        }
        return new CraftProfileBanEntry(profile, entry, this.list);
    }

    @Override
    public BanEntry addBan(String target, String reason, Date expires, String source) {
        Validate.notNull((Object)target, (String)"Ban target cannot be null", (Object[])new Object[0]);
        GameProfile profile = MinecraftServer.getServerInst().aB().a(target);
        if (profile == null) {
            return null;
        }
        pr entry = new pr(profile, new Date(), StringUtils.isBlank((CharSequence)source) ? null : source, expires, StringUtils.isBlank((CharSequence)reason) ? null : reason);
        this.list.void_a(entry);
        try {
            this.list.f();
        }
        catch (IOException ex2) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-players.json, {0}", ex2.getMessage());
        }
        return new CraftProfileBanEntry(profile, entry, this.list);
    }

    @Override
    public Set<BanEntry> getBanEntries() {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        for (po entry : this.list.getValuesCB()) {
            GameProfile profile = (GameProfile)entry.f();
            builder.add((Object)new CraftProfileBanEntry(profile, (pr)entry, this.list));
        }
        return builder.build();
    }

    @Override
    public boolean isBanned(String target) {
        Validate.notNull((Object)target, (String)"Target cannot be null", (Object[])new Object[0]);
        GameProfile profile = MinecraftServer.getServerInst().aB().a(target);
        if (profile == null) {
            return false;
        }
        return this.list.a(profile);
    }

    @Override
    public void pardon(String target) {
        Validate.notNull((Object)target, (String)"Target cannot be null", (Object[])new Object[0]);
        GameProfile profile = MinecraftServer.getServerInst().aB().a(target);
        this.list.c(profile);
    }
}

