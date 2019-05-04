/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1;

import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftIpBanEntry;

public class CraftIpBanList
implements BanList {
    private final pi list;

    public CraftIpBanList(pi list) {
        this.list = list;
    }

    @Override
    public BanEntry getBanEntry(String target) {
        Validate.notNull((Object)target, (String)"Target cannot be null", (Object[])new Object[0]);
        pj entry = (pj)this.list.b(target);
        if (entry == null) {
            return null;
        }
        return new CraftIpBanEntry(target, entry, this.list);
    }

    @Override
    public BanEntry addBan(String target, String reason, Date expires, String source) {
        Validate.notNull((Object)target, (String)"Ban target cannot be null", (Object[])new Object[0]);
        pj entry = new pj(target, new Date(), StringUtils.isBlank((CharSequence)source) ? null : source, expires, StringUtils.isBlank((CharSequence)reason) ? null : reason);
        this.list.void_a(entry);
        try {
            this.list.f();
        }
        catch (IOException ex2) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-ips.json, {0}", ex2.getMessage());
        }
        return new CraftIpBanEntry(target, entry, this.list);
    }

    @Override
    public Set<BanEntry> getBanEntries() {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        for (String target : this.list.a()) {
            builder.add((Object)new CraftIpBanEntry(target, (pj)this.list.b(target), this.list));
        }
        return builder.build();
    }

    @Override
    public boolean isBanned(String target) {
        Validate.notNull((Object)target, (String)"Target cannot be null", (Object[])new Object[0]);
        return this.list.a(InetSocketAddress.createUnresolved(target, 0));
    }

    @Override
    public void pardon(String target) {
        Validate.notNull((Object)target, (String)"Target cannot be null", (Object[])new Object[0]);
        this.list.c(target);
    }
}

