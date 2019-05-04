/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.scoreboard;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardComponent;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

final class CraftTeam
extends CraftScoreboardComponent
implements Team {
    private final bhh team;

    CraftTeam(CraftScoreboard scoreboard, bhh team) {
        super(scoreboard);
        this.team = team;
    }

    @Override
    public String getName() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return this.team.b();
    }

    @Override
    public String getDisplayName() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return this.team.c();
    }

    @Override
    public void setDisplayName(String displayName) throws IllegalStateException {
        Validate.notNull((Object)displayName, (String)"Display name cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(displayName.length() <= 32), (String)("Display name '" + displayName + "' is longer than the limit of 32 characters"), (Object[])new Object[0]);
        CraftScoreboard scoreboard = this.checkState();
        this.team.a(displayName);
    }

    @Override
    public String getPrefix() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return this.team.e();
    }

    @Override
    public void setPrefix(String prefix) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)prefix, (String)"Prefix cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(prefix.length() <= 16), (String)("Prefix '" + prefix + "' is longer than the limit of 16 characters"), (Object[])new Object[0]);
        CraftScoreboard scoreboard = this.checkState();
        this.team.b(prefix);
    }

    @Override
    public String getSuffix() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return this.team.f();
    }

    @Override
    public void setSuffix(String suffix) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)suffix, (String)"Suffix cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(suffix.length() <= 16), (String)("Suffix '" + suffix + "' is longer than the limit of 16 characters"), (Object[])new Object[0]);
        CraftScoreboard scoreboard = this.checkState();
        this.team.c(suffix);
    }

    @Override
    public ChatColor getColor() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return CraftChatMessage.getColor(this.team.m());
    }

    @Override
    public void setColor(ChatColor color) {
        Validate.notNull((Object)((Object)color), (String)"Color cannot be null", (Object[])new Object[0]);
        CraftScoreboard scoreboard = this.checkState();
        this.team.a(CraftChatMessage.getColor(color));
        scoreboard.board.b(this.team);
    }

    @Override
    public boolean allowFriendlyFire() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return this.team.g();
    }

    @Override
    public void setAllowFriendlyFire(boolean enabled) throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        this.team.a(enabled);
    }

    @Override
    public boolean canSeeFriendlyInvisibles() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return this.team.h();
    }

    @Override
    public void setCanSeeFriendlyInvisibles(boolean enabled) throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        this.team.b(enabled);
    }

    @Override
    public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException {
        CraftScoreboard scoreboard = this.checkState();
        return CraftTeam.notchToBukkit(this.team.i());
    }

    @Override
    public void setNameTagVisibility(NameTagVisibility visibility) throws IllegalArgumentException {
        CraftScoreboard scoreboard = this.checkState();
        this.team.a(CraftTeam.bukkitToNotch(visibility));
    }

    @Override
    public Set<OfflinePlayer> getPlayers() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        ImmutableSet.Builder players = ImmutableSet.builder();
        for (String playerName : this.team.d()) {
            players.add((Object)Bukkit.getOfflinePlayer(playerName));
        }
        return players.build();
    }

    @Override
    public Set<String> getEntries() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        ImmutableSet.Builder entries = ImmutableSet.builder();
        for (String playerName : this.team.d()) {
            entries.add((Object)playerName);
        }
        return entries.build();
    }

    @Override
    public int getSize() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return this.team.d().size();
    }

    @Override
    public void addPlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)player, (String)"OfflinePlayer cannot be null", (Object[])new Object[0]);
        this.addEntry(player.getName());
    }

    @Override
    public void addEntry(String entry) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)entry, (String)"Entry cannot be null", (Object[])new Object[0]);
        CraftScoreboard scoreboard = this.checkState();
        scoreboard.board.a(entry, this.team.b());
    }

    @Override
    public boolean removePlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)player, (String)"OfflinePlayer cannot be null", (Object[])new Object[0]);
        return this.removeEntry(player.getName());
    }

    @Override
    public boolean removeEntry(String entry) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)entry, (String)"Entry cannot be null", (Object[])new Object[0]);
        CraftScoreboard scoreboard = this.checkState();
        if (!this.team.d().contains(entry)) {
            return false;
        }
        scoreboard.board.a(entry, this.team);
        return true;
    }

    @Override
    public boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull((Object)player, (String)"OfflinePlayer cannot be null", (Object[])new Object[0]);
        return this.hasEntry(player.getName());
    }

    @Override
    public boolean hasEntry(String entry) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull((Object)"Entry cannot be null");
        CraftScoreboard scoreboard = this.checkState();
        return this.team.d().contains(entry);
    }

    @Override
    public void unregister() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        scoreboard.board.d(this.team);
    }

    @Override
    public Team.OptionStatus getOption(Team.Option option) throws IllegalStateException {
        this.checkState();
        switch (option) {
            case NAME_TAG_VISIBILITY: {
                return Team.OptionStatus.values()[this.team.i().ordinal()];
            }
            case DEATH_MESSAGE_VISIBILITY: {
                return Team.OptionStatus.values()[this.team.j().ordinal()];
            }
            case COLLISION_RULE: {
                return Team.OptionStatus.values()[this.team.k().ordinal()];
            }
        }
        throw new IllegalArgumentException("Unrecognised option " + (Object)((Object)option));
    }

    @Override
    public void setOption(Team.Option option, Team.OptionStatus status) throws IllegalStateException {
        this.checkState();
        switch (option) {
            case NAME_TAG_VISIBILITY: {
                this.team.a(bhm.b.values()[status.ordinal()]);
                break;
            }
            case DEATH_MESSAGE_VISIBILITY: {
                this.team.b(bhm.b.values()[status.ordinal()]);
                break;
            }
            case COLLISION_RULE: {
                this.team.a(bhm.a.values()[status.ordinal()]);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unrecognised option " + (Object)((Object)option));
            }
        }
    }

    public static bhm.b bukkitToNotch(NameTagVisibility visibility) {
        switch (visibility) {
            case ALWAYS: {
                return bhm.b.a;
            }
            case NEVER: {
                return bhm.b.b;
            }
            case HIDE_FOR_OTHER_TEAMS: {
                return bhm.b.c;
            }
            case HIDE_FOR_OWN_TEAM: {
                return bhm.b.d;
            }
        }
        throw new IllegalArgumentException("Unknown visibility level " + (Object)((Object)visibility));
    }

    public static NameTagVisibility notchToBukkit(bhm.b visibility) {
        switch (visibility) {
            case a: {
                return NameTagVisibility.ALWAYS;
            }
            case b: {
                return NameTagVisibility.NEVER;
            }
            case c: {
                return NameTagVisibility.HIDE_FOR_OTHER_TEAMS;
            }
            case d: {
                return NameTagVisibility.HIDE_FOR_OWN_TEAM;
            }
        }
        throw new IllegalArgumentException("Unknown visibility level " + (Object)((Object)visibility));
    }

    @Override
    CraftScoreboard checkState() throws IllegalStateException {
        if (this.getScoreboard().board.d(this.team.b()) == null) {
            throw new IllegalStateException("Unregistered scoreboard component");
        }
        return this.getScoreboard();
    }

    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.team != null ? this.team.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        CraftTeam other = (CraftTeam)obj;
        return this.team == other.team || this.team != null && this.team.equals(other.team);
    }

}

