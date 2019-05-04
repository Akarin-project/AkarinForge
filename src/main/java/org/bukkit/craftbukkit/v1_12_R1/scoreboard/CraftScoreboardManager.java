/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_12_R1.util.WeakCollection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.spigotmc.AsyncCatcher;

public final class CraftScoreboardManager
implements ScoreboardManager {
    private final CraftScoreboard mainScoreboard;
    private final MinecraftServer server;
    private final Collection<CraftScoreboard> scoreboards = new WeakCollection<CraftScoreboard>();
    private final Map<CraftPlayer, CraftScoreboard> playerBoards = new HashMap<CraftPlayer, CraftScoreboard>();

    public CraftScoreboardManager(MinecraftServer minecraftserver, bhk scoreboardServer) {
        this.mainScoreboard = new CraftScoreboard(scoreboardServer);
        this.server = minecraftserver;
        this.scoreboards.add(this.mainScoreboard);
    }

    @Override
    public CraftScoreboard getMainScoreboard() {
        return this.mainScoreboard;
    }

    @Override
    public CraftScoreboard getNewScoreboard() {
        AsyncCatcher.catchOp("scoreboard creation");
        CraftScoreboard scoreboard = new CraftScoreboard(new nv(this.server));
        this.scoreboards.add(scoreboard);
        return scoreboard;
    }

    public CraftScoreboard getPlayerBoard(CraftPlayer player) {
        CraftScoreboard board = this.playerBoards.get(player);
        return board == null ? this.getMainScoreboard() : board;
    }

    public void setPlayerBoard(CraftPlayer player, Scoreboard bukkitScoreboard) throws IllegalArgumentException {
        Validate.isTrue((boolean)(bukkitScoreboard instanceof CraftScoreboard), (String)"Cannot set player scoreboard to an unregistered Scoreboard", (Object[])new Object[0]);
        CraftScoreboard scoreboard = (CraftScoreboard)bukkitScoreboard;
        bhk oldboard = this.getPlayerBoard(player).getHandle();
        bhk newboard = scoreboard.getHandle();
        oq entityplayer = player.getHandle();
        if (oldboard == newboard) {
            return;
        }
        if (scoreboard == this.mainScoreboard) {
            this.playerBoards.remove(player);
        } else {
            this.playerBoards.put(player, scoreboard);
        }
        HashSet<bhg> removed = new HashSet<bhg>();
        for (int i2 = 0; i2 < 3; ++i2) {
            bhg scoreboardobjective = oldboard.a(i2);
            if (scoreboardobjective == null || removed.contains(scoreboardobjective)) continue;
            entityplayer.a.a(new kj(scoreboardobjective, 1));
            removed.add(scoreboardobjective);
        }
        for (bhh scoreboardteam : oldboard.g()) {
            entityplayer.a.a(new kl(scoreboardteam, 1));
        }
        this.server.am().a((nv)newboard, player.getHandle());
    }

    public void removePlayer(Player player) {
        this.playerBoards.remove(player);
    }

    public Collection<bhi> getScoreboardScores(bhq criteria, String name, Collection<bhi> collection) {
        for (CraftScoreboard scoreboard : this.scoreboards) {
            bhk board = scoreboard.board;
            for (bhg objective : board.a(criteria)) {
                collection.add(board.c(name, objective));
            }
        }
        return collection;
    }
}

