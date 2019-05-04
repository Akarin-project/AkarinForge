/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1.boss;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class CraftBossBar
implements BossBar {
    private final om handle;
    private final Set<BarFlag> flags;
    private BarColor color;
    private BarStyle style;

    public /* varargs */ CraftBossBar(String title, BarColor color, BarStyle style, BarFlag ... flags) {
        this.flags = flags.length > 0 ? EnumSet.of(flags[0], flags) : EnumSet.noneOf(BarFlag.class);
        this.color = color;
        this.style = style;
        this.handle = new om(CraftChatMessage.fromString(title, true)[0], this.convertColor(color), this.convertStyle(style));
        this.updateFlags();
    }

    private tt.a convertColor(BarColor color) {
        tt.a nmsColor = tt.a.valueOf(color.name());
        return nmsColor == null ? tt.a.g : nmsColor;
    }

    private tt.b convertStyle(BarStyle style) {
        switch (style) {
            default: {
                return tt.b.a;
            }
            case SEGMENTED_6: {
                return tt.b.b;
            }
            case SEGMENTED_10: {
                return tt.b.c;
            }
            case SEGMENTED_12: {
                return tt.b.d;
            }
            case SEGMENTED_20: 
        }
        return tt.b.e;
    }

    private void updateFlags() {
        this.handle.a(this.hasFlag(BarFlag.DARKEN_SKY));
        this.handle.b(this.hasFlag(BarFlag.PLAY_BOSS_MUSIC));
        this.handle.c(this.hasFlag(BarFlag.CREATE_FOG));
    }

    @Override
    public String getTitle() {
        return CraftChatMessage.fromComponent(this.handle.e());
    }

    @Override
    public void setTitle(String title) {
        this.handle.a = CraftChatMessage.fromString(title, true)[0];
        this.handle.a(ik.a.d);
    }

    @Override
    public BarColor getColor() {
        return this.color;
    }

    @Override
    public void setColor(BarColor color) {
        this.color = color;
        this.handle.c = this.convertColor(color);
        this.handle.a(ik.a.e);
    }

    @Override
    public BarStyle getStyle() {
        return this.style;
    }

    @Override
    public void setStyle(BarStyle style) {
        this.style = style;
        this.handle.d = this.convertStyle(style);
        this.handle.a(ik.a.e);
    }

    @Override
    public void addFlag(BarFlag flag) {
        this.flags.add(flag);
        this.updateFlags();
    }

    @Override
    public void removeFlag(BarFlag flag) {
        this.flags.remove((Object)flag);
        this.updateFlags();
    }

    @Override
    public boolean hasFlag(BarFlag flag) {
        return this.flags.contains((Object)flag);
    }

    @Override
    public void setProgress(double progress) {
        Preconditions.checkArgument((boolean)(progress >= 0.0 && progress <= 1.0), (String)"Progress must be between 0.0 and 1.0 (%s)", (Object)progress);
        this.handle.a((float)progress);
    }

    @Override
    public double getProgress() {
        return this.handle.f();
    }

    @Override
    public void addPlayer(Player player) {
        this.handle.a(((CraftPlayer)player).getHandle());
    }

    @Override
    public void removePlayer(Player player) {
        this.handle.b(((CraftPlayer)player).getHandle());
    }

    @Override
    public List<Player> getPlayers() {
        ImmutableList.Builder players = ImmutableList.builder();
        for (oq p2 : this.handle.c()) {
            players.add((Object)p2.getBukkitEntity());
        }
        return players.build();
    }

    @Override
    public void setVisible(boolean visible) {
        this.handle.d(visible);
    }

    @Override
    public boolean isVisible() {
        return this.handle.j;
    }

    @Override
    public void show() {
        this.handle.d(true);
    }

    @Override
    public void hide() {
        this.handle.d(false);
    }

    @Override
    public void removeAll() {
        for (Player player : this.getPlayers()) {
            this.removePlayer(player);
        }
    }

}

