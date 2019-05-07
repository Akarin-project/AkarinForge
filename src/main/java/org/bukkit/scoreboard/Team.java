/*
 * Akarin Forge
 */
package org.bukkit.scoreboard;

import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;

public interface Team {
    public String getName() throws IllegalStateException;

    public String getDisplayName() throws IllegalStateException;

    public void setDisplayName(String var1) throws IllegalStateException, IllegalArgumentException;

    public String getPrefix() throws IllegalStateException;

    public void setPrefix(String var1) throws IllegalStateException, IllegalArgumentException;

    public String getSuffix() throws IllegalStateException;

    public void setSuffix(String var1) throws IllegalStateException, IllegalArgumentException;

    public ChatColor getColor() throws IllegalStateException;

    public void setColor(ChatColor var1);

    public boolean allowFriendlyFire() throws IllegalStateException;

    public void setAllowFriendlyFire(boolean var1) throws IllegalStateException;

    public boolean canSeeFriendlyInvisibles() throws IllegalStateException;

    public void setCanSeeFriendlyInvisibles(boolean var1) throws IllegalStateException;

    @Deprecated
    public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException;

    @Deprecated
    public void setNameTagVisibility(NameTagVisibility var1) throws IllegalArgumentException;

    @Deprecated
    public Set<OfflinePlayer> getPlayers() throws IllegalStateException;

    public Set<String> getEntries() throws IllegalStateException;

    public int getSize() throws IllegalStateException;

    public Scoreboard getScoreboard();

    @Deprecated
    public void addPlayer(OfflinePlayer var1) throws IllegalStateException, IllegalArgumentException;

    public void addEntry(String var1) throws IllegalStateException, IllegalArgumentException;

    @Deprecated
    public boolean removePlayer(OfflinePlayer var1) throws IllegalStateException, IllegalArgumentException;

    public boolean removeEntry(String var1) throws IllegalStateException, IllegalArgumentException;

    public void unregister() throws IllegalStateException;

    @Deprecated
    public boolean hasPlayer(OfflinePlayer var1) throws IllegalArgumentException, IllegalStateException;

    public boolean hasEntry(String var1) throws IllegalArgumentException, IllegalStateException;

    public OptionStatus getOption(Option var1) throws IllegalStateException;

    public void setOption(Option var1, OptionStatus var2) throws IllegalStateException;

    public static enum OptionStatus {
        ALWAYS,
        NEVER,
        FOR_OTHER_TEAMS,
        FOR_OWN_TEAM;
        

        private OptionStatus() {
        }
    }

    public static enum Option {
        NAME_TAG_VISIBILITY,
        DEATH_MESSAGE_VISIBILITY,
        COLLISION_RULE;
        

        private Option() {
        }
    }

}

