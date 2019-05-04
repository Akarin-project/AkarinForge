/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.scoreboard;

import org.apache.commons.lang3.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftCriteria;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScore;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardComponent;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardTranslations;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

final class CraftObjective
extends CraftScoreboardComponent
implements Objective {
    private final bhg objective;
    private final CraftCriteria criteria;

    CraftObjective(CraftScoreboard scoreboard, bhg objective) {
        super(scoreboard);
        this.objective = objective;
        this.criteria = CraftCriteria.getFromNMS(objective);
    }

    bhg getHandle() {
        return this.objective;
    }

    @Override
    public String getName() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return this.objective.b();
    }

    @Override
    public String getDisplayName() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return this.objective.d();
    }

    @Override
    public void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)displayName, (String)"Display name cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(displayName.length() <= 32), (String)("Display name '" + displayName + "' is longer than the limit of 32 characters"), (Object[])new Object[0]);
        CraftScoreboard scoreboard = this.checkState();
        this.objective.a(displayName);
    }

    @Override
    public String getCriteria() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return this.criteria.bukkitName;
    }

    @Override
    public boolean isModifiable() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        return !this.criteria.criteria.b();
    }

    @Override
    public void setDisplaySlot(DisplaySlot slot) throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        bhk board = scoreboard.board;
        bhg objective = this.objective;
        for (int i2 = 0; i2 < 3; ++i2) {
            if (board.a(i2) != objective) continue;
            board.a(i2, null);
        }
        if (slot != null) {
            int slotNumber = CraftScoreboardTranslations.fromBukkitSlot(slot);
            board.a(slotNumber, this.getHandle());
        }
    }

    @Override
    public DisplaySlot getDisplaySlot() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        bhk board = scoreboard.board;
        bhg objective = this.objective;
        for (int i2 = 0; i2 < 3; ++i2) {
            if (board.a(i2) != objective) continue;
            return CraftScoreboardTranslations.toBukkitSlot(i2);
        }
        return null;
    }

    @Override
    public Score getScore(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull((Object)player, (String)"Player cannot be null", (Object[])new Object[0]);
        CraftScoreboard scoreboard = this.checkState();
        return new CraftScore(this, player.getName());
    }

    @Override
    public Score getScore(String entry) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull((Object)entry, (String)"Entry cannot be null", (Object[])new Object[0]);
        CraftScoreboard scoreboard = this.checkState();
        return new CraftScore(this, entry);
    }

    @Override
    public void unregister() throws IllegalStateException {
        CraftScoreboard scoreboard = this.checkState();
        scoreboard.board.k(this.objective);
    }

    @Override
    CraftScoreboard checkState() throws IllegalStateException {
        if (this.getScoreboard().board.b(this.objective.b()) == null) {
            throw new IllegalStateException("Unregistered scoreboard component");
        }
        return this.getScoreboard();
    }

    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.objective != null ? this.objective.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        CraftObjective other = (CraftObjective)obj;
        return this.objective == other.objective || this.objective != null && this.objective.equals(other.objective);
    }
}

