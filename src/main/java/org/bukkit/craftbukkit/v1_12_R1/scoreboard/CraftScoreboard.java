/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 *  com.google.common.collect.Iterables
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.scoreboard;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftCriteria;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftObjective;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScore;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardTranslations;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftTeam;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class CraftScoreboard
implements Scoreboard {
    final bhk board;

    CraftScoreboard(bhk board) {
        this.board = board;
    }

    @Override
    public CraftObjective registerNewObjective(String name, String criteria) throws IllegalArgumentException {
        Validate.notNull((Object)name, (String)"Objective name cannot be null", (Object[])new Object[0]);
        Validate.notNull((Object)criteria, (String)"Criteria cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(name.length() <= 16), (String)("The name '" + name + "' is longer than the limit of 16 characters"), (Object[])new Object[0]);
        Validate.isTrue((boolean)(this.board.b(name) == null), (String)("An objective of name '" + name + "' already exists"), (Object[])new Object[0]);
        CraftCriteria craftCriteria = CraftCriteria.getFromBukkit(criteria);
        bhg objective = this.board.a(name, craftCriteria.criteria);
        return new CraftObjective(this, objective);
    }

    @Override
    public Objective getObjective(String name) throws IllegalArgumentException {
        Validate.notNull((Object)name, (String)"Name cannot be null", (Object[])new Object[0]);
        bhg nms = this.board.b(name);
        return nms == null ? null : new CraftObjective(this, nms);
    }

    public ImmutableSet<Objective> getObjectivesByCriteria(String criteria) throws IllegalArgumentException {
        Validate.notNull((Object)criteria, (String)"Criteria cannot be null", (Object[])new Object[0]);
        ImmutableSet.Builder objectives = ImmutableSet.builder();
        for (bhg netObjective : this.board.c()) {
            CraftObjective objective = new CraftObjective(this, netObjective);
            if (!objective.getCriteria().equals(criteria)) continue;
            objectives.add((Object)objective);
        }
        return objectives.build();
    }

    public ImmutableSet<Objective> getObjectives() {
        return ImmutableSet.copyOf((Iterable)Iterables.transform(this.board.c(), (Function)new Function<bhg, Objective>(){

            public Objective apply(bhg input) {
                return new CraftObjective(CraftScoreboard.this, input);
            }
        }));
    }

    @Override
    public Objective getObjective(DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull((Object)((Object)slot), (String)"Display slot cannot be null", (Object[])new Object[0]);
        bhg objective = this.board.a(CraftScoreboardTranslations.fromBukkitSlot(slot));
        if (objective == null) {
            return null;
        }
        return new CraftObjective(this, objective);
    }

    public ImmutableSet<Score> getScores(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull((Object)player, (String)"OfflinePlayer cannot be null", (Object[])new Object[0]);
        return this.getScores(player.getName());
    }

    public ImmutableSet<Score> getScores(String entry) throws IllegalArgumentException {
        Validate.notNull((Object)entry, (String)"Entry cannot be null", (Object[])new Object[0]);
        ImmutableSet.Builder scores = ImmutableSet.builder();
        for (bhg objective : this.board.c()) {
            scores.add((Object)new CraftScore(new CraftObjective(this, objective), entry));
        }
        return scores.build();
    }

    @Override
    public void resetScores(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull((Object)player, (String)"OfflinePlayer cannot be null", (Object[])new Object[0]);
        this.resetScores(player.getName());
    }

    @Override
    public void resetScores(String entry) throws IllegalArgumentException {
        Validate.notNull((Object)entry, (String)"Entry cannot be null", (Object[])new Object[0]);
        for (bhg objective : this.board.c()) {
            this.board.d(entry, objective);
        }
    }

    @Override
    public Team getPlayerTeam(OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull((Object)player, (String)"OfflinePlayer cannot be null", (Object[])new Object[0]);
        bhh team = this.board.g(player.getName());
        return team == null ? null : new CraftTeam(this, team);
    }

    @Override
    public Team getEntryTeam(String entry) throws IllegalArgumentException {
        Validate.notNull((Object)entry, (String)"Entry cannot be null", (Object[])new Object[0]);
        bhh team = this.board.g(entry);
        return team == null ? null : new CraftTeam(this, team);
    }

    @Override
    public Team getTeam(String teamName) throws IllegalArgumentException {
        Validate.notNull((Object)teamName, (String)"Team name cannot be null", (Object[])new Object[0]);
        bhh team = this.board.d(teamName);
        return team == null ? null : new CraftTeam(this, team);
    }

    public ImmutableSet<Team> getTeams() {
        return ImmutableSet.copyOf((Iterable)Iterables.transform(this.board.g(), (Function)new Function<bhh, Team>(){

            public Team apply(bhh input) {
                return new CraftTeam(CraftScoreboard.this, input);
            }
        }));
    }

    @Override
    public Team registerNewTeam(String name) throws IllegalArgumentException {
        Validate.notNull((Object)name, (String)"Team name cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(name.length() <= 16), (String)("Team name '" + name + "' is longer than the limit of 16 characters"), (Object[])new Object[0]);
        Validate.isTrue((boolean)(this.board.d(name) == null), (String)("Team name '" + name + "' is already in use"), (Object[])new Object[0]);
        return new CraftTeam(this, this.board.e(name));
    }

    public ImmutableSet<OfflinePlayer> getPlayers() {
        ImmutableSet.Builder players = ImmutableSet.builder();
        for (String playerName : this.board.d()) {
            players.add((Object)Bukkit.getOfflinePlayer(playerName.toString()));
        }
        return players.build();
    }

    public ImmutableSet<String> getEntries() {
        ImmutableSet.Builder entries = ImmutableSet.builder();
        for (String entry : this.board.d()) {
            entries.add((Object)entry.toString());
        }
        return entries.build();
    }

    @Override
    public void clearSlot(DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull((Object)((Object)slot), (String)"Slot cannot be null", (Object[])new Object[0]);
        this.board.a(CraftScoreboardTranslations.fromBukkitSlot(slot), null);
    }

    public bhk getHandle() {
        return this.board;
    }

}

