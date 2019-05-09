/*
 * Akarin Forge
 */
package org.bukkit.scoreboard;

import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

public interface Scoreboard {
    public Objective registerNewObjective(String var1, String var2) throws IllegalArgumentException;

    public Objective getObjective(String var1) throws IllegalArgumentException;

    public Set<Objective> getObjectivesByCriteria(String var1) throws IllegalArgumentException;

    public Set<Objective> getObjectives();

    public Objective getObjective(DisplaySlot var1) throws IllegalArgumentException;

    @Deprecated
    public Set<Score> getScores(OfflinePlayer var1) throws IllegalArgumentException;

    public Set<Score> getScores(String var1) throws IllegalArgumentException;

    @Deprecated
    public void resetScores(OfflinePlayer var1) throws IllegalArgumentException;

    public void resetScores(String var1) throws IllegalArgumentException;

    @Deprecated
    public Team getPlayerTeam(OfflinePlayer var1) throws IllegalArgumentException;

    public Team getEntryTeam(String var1) throws IllegalArgumentException;

    public Team getTeam(String var1) throws IllegalArgumentException;

    public Set<Team> getTeams();

    public Team registerNewTeam(String var1) throws IllegalArgumentException;

    @Deprecated
    public Set<OfflinePlayer> getPlayers();

    public Set<String> getEntries();

    public void clearSlot(DisplaySlot var1) throws IllegalArgumentException;
}

