package org.bukkit.craftbukkit.scoreboard;

import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;

abstract class CraftScoreboardComponent {
    private CraftScoreboard scoreboard;

    CraftScoreboardComponent(CraftScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    abstract CraftScoreboard checkState() throws IllegalStateException;

    public CraftScoreboard getScoreboard() {
        return this.scoreboard;
    }

    abstract void unregister() throws IllegalStateException;
}

