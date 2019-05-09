/*
 * Akarin Forge
 */
package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public interface Score {
    @Deprecated
    public OfflinePlayer getPlayer();

    public String getEntry();

    public Objective getObjective();

    public int getScore() throws IllegalStateException;

    public void setScore(int var1) throws IllegalStateException;

    public boolean isScoreSet() throws IllegalStateException;

    public Scoreboard getScoreboard();
}

