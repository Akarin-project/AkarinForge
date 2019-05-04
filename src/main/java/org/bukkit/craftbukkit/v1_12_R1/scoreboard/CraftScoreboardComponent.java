/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.scoreboard;

import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboard;

abstract class CraftScoreboardComponent {
    private CraftScoreboard scoreboard;

    CraftScoreboardComponent(CraftScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    abstract CraftScoreboard checkState() throws IllegalStateException;

    public CraftScoreboard org_bukkit_craftbukkit_v1_12_R1_scoreboard_CraftScoreboard_getScoreboard() {
        return this.scoreboard;
    }

    abstract void unregister() throws IllegalStateException;
}

