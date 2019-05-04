/*
 * Akarin Forge
 */
package org.bukkit.boss;

import java.util.List;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public interface BossBar {
    public String getTitle();

    public void setTitle(String var1);

    public BarColor getColor();

    public void setColor(BarColor var1);

    public BarStyle getStyle();

    public void setStyle(BarStyle var1);

    public void removeFlag(BarFlag var1);

    public void addFlag(BarFlag var1);

    public boolean hasFlag(BarFlag var1);

    public void setProgress(double var1);

    public double getProgress();

    public void addPlayer(Player var1);

    public void removePlayer(Player var1);

    public void removeAll();

    public List<Player> getPlayers();

    public void setVisible(boolean var1);

    public boolean isVisible();

    @Deprecated
    public void show();

    @Deprecated
    public void hide();
}

