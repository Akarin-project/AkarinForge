/*
 * Akarin Forge
 */
package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public interface Objective {
    public String getName() throws IllegalStateException;

    public String getDisplayName() throws IllegalStateException;

    public void setDisplayName(String var1) throws IllegalStateException, IllegalArgumentException;

    public String getCriteria() throws IllegalStateException;

    public boolean isModifiable() throws IllegalStateException;

    public Scoreboard getScoreboard();

    public void unregister() throws IllegalStateException;

    public void setDisplaySlot(DisplaySlot var1) throws IllegalStateException;

    public DisplaySlot getDisplaySlot() throws IllegalStateException;

    @Deprecated
    public Score getScore(OfflinePlayer var1) throws IllegalArgumentException, IllegalStateException;

    public Score getScore(String var1) throws IllegalArgumentException, IllegalStateException;
}

