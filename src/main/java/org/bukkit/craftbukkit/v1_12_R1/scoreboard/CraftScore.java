/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.scoreboard;

import java.util.Collection;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftObjective;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboard;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

final class CraftScore
implements Score {
    private final String entry;
    private final CraftObjective objective;

    CraftScore(CraftObjective objective, String entry) {
        this.objective = objective;
        this.entry = entry;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.entry);
    }

    @Override
    public String getEntry() {
        return this.entry;
    }

    @Override
    public Objective getObjective() {
        return this.objective;
    }

    @Override
    public int getScore() throws IllegalStateException {
        bhi score;
        Map<bhg, bhi> scores;
        bhk board = this.objective.checkState().board;
        if (board.d().contains(this.entry) && (score = (scores = board.c(this.entry)).get(this.objective.getHandle())) != null) {
            return score.c();
        }
        return 0;
    }

    @Override
    public void setScore(int score) throws IllegalStateException {
        this.objective.checkState().board.c(this.entry, this.objective.getHandle()).c(score);
    }

    @Override
    public boolean isScoreSet() throws IllegalStateException {
        bhk board = this.objective.checkState().board;
        return board.d().contains(this.entry) && board.c(this.entry).containsKey(this.objective.getHandle());
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return this.objective.getScoreboard();
    }
}

