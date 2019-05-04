/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.chat.BaseComponent
 */
package org.bukkit.entity;

import java.net.InetSocketAddress;
import java.util.Set;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.scoreboard.Scoreboard;

public interface Player
extends HumanEntity,
Conversable,
CommandSender,
OfflinePlayer,
PluginMessageRecipient {
    public String getDisplayName();

    public void setDisplayName(String var1);

    public String getPlayerListName();

    public void setPlayerListName(String var1);

    public void setCompassTarget(Location var1);

    public Location getCompassTarget();

    public InetSocketAddress getAddress();

    @Override
    public void sendRawMessage(String var1);

    public void kickPlayer(String var1);

    public void chat(String var1);

    public boolean performCommand(String var1);

    public boolean isSneaking();

    public void setSneaking(boolean var1);

    public boolean isSprinting();

    public void setSprinting(boolean var1);

    public void saveData();

    public void loadData();

    public void setSleepingIgnored(boolean var1);

    public boolean isSleepingIgnored();

    @Deprecated
    public void playNote(Location var1, byte var2, byte var3);

    public void playNote(Location var1, Instrument var2, Note var3);

    public void playSound(Location var1, Sound var2, float var3, float var4);

    public void playSound(Location var1, String var2, float var3, float var4);

    public void playSound(Location var1, Sound var2, SoundCategory var3, float var4, float var5);

    public void playSound(Location var1, String var2, SoundCategory var3, float var4, float var5);

    public void stopSound(Sound var1);

    public void stopSound(String var1);

    public void stopSound(Sound var1, SoundCategory var2);

    public void stopSound(String var1, SoundCategory var2);

    @Deprecated
    public void playEffect(Location var1, Effect var2, int var3);

    public <T> void playEffect(Location var1, Effect var2, T var3);

    @Deprecated
    public void sendBlockChange(Location var1, Material var2, byte var3);

    @Deprecated
    public boolean sendChunkChange(Location var1, int var2, int var3, int var4, byte[] var5);

    @Deprecated
    public void sendBlockChange(Location var1, int var2, byte var3);

    public void sendSignChange(Location var1, String[] var2) throws IllegalArgumentException;

    public void sendMap(MapView var1);

    @Deprecated
    public void updateInventory();

    @Deprecated
    public void awardAchievement(Achievement var1);

    @Deprecated
    public void removeAchievement(Achievement var1);

    @Deprecated
    public boolean hasAchievement(Achievement var1);

    public void incrementStatistic(Statistic var1) throws IllegalArgumentException;

    public void decrementStatistic(Statistic var1) throws IllegalArgumentException;

    public void incrementStatistic(Statistic var1, int var2) throws IllegalArgumentException;

    public void decrementStatistic(Statistic var1, int var2) throws IllegalArgumentException;

    public void setStatistic(Statistic var1, int var2) throws IllegalArgumentException;

    public int getStatistic(Statistic var1) throws IllegalArgumentException;

    public void incrementStatistic(Statistic var1, Material var2) throws IllegalArgumentException;

    public void decrementStatistic(Statistic var1, Material var2) throws IllegalArgumentException;

    public int getStatistic(Statistic var1, Material var2) throws IllegalArgumentException;

    public void incrementStatistic(Statistic var1, Material var2, int var3) throws IllegalArgumentException;

    public void decrementStatistic(Statistic var1, Material var2, int var3) throws IllegalArgumentException;

    public void setStatistic(Statistic var1, Material var2, int var3) throws IllegalArgumentException;

    public void incrementStatistic(Statistic var1, EntityType var2) throws IllegalArgumentException;

    public void decrementStatistic(Statistic var1, EntityType var2) throws IllegalArgumentException;

    public int getStatistic(Statistic var1, EntityType var2) throws IllegalArgumentException;

    public void incrementStatistic(Statistic var1, EntityType var2, int var3) throws IllegalArgumentException;

    public void decrementStatistic(Statistic var1, EntityType var2, int var3);

    public void setStatistic(Statistic var1, EntityType var2, int var3);

    public void setPlayerTime(long var1, boolean var3);

    public long getPlayerTime();

    public long getPlayerTimeOffset();

    public boolean isPlayerTimeRelative();

    public void resetPlayerTime();

    public void setPlayerWeather(WeatherType var1);

    public WeatherType getPlayerWeather();

    public void resetPlayerWeather();

    public void giveExp(int var1);

    public void giveExpLevels(int var1);

    public float getExp();

    public void setExp(float var1);

    public int getLevel();

    public void setLevel(int var1);

    public int getTotalExperience();

    public void setTotalExperience(int var1);

    public float getExhaustion();

    public void setExhaustion(float var1);

    public float getSaturation();

    public void setSaturation(float var1);

    public int getFoodLevel();

    public void setFoodLevel(int var1);

    @Override
    public Location getBedSpawnLocation();

    public void setBedSpawnLocation(Location var1);

    public void setBedSpawnLocation(Location var1, boolean var2);

    public boolean getAllowFlight();

    public void setAllowFlight(boolean var1);

    @Deprecated
    public void hidePlayer(Player var1);

    public void hidePlayer(Plugin var1, Player var2);

    @Deprecated
    public void showPlayer(Player var1);

    public void showPlayer(Plugin var1, Player var2);

    public boolean canSee(Player var1);

    public boolean isFlying();

    public void setFlying(boolean var1);

    public void setFlySpeed(float var1) throws IllegalArgumentException;

    public void setWalkSpeed(float var1) throws IllegalArgumentException;

    public float getFlySpeed();

    public float getWalkSpeed();

    @Deprecated
    public void setTexturePack(String var1);

    public void setResourcePack(String var1);

    public void setResourcePack(String var1, byte[] var2);

    public Scoreboard getScoreboard();

    public void setScoreboard(Scoreboard var1) throws IllegalArgumentException, IllegalStateException;

    public boolean isHealthScaled();

    public void setHealthScaled(boolean var1);

    public void setHealthScale(double var1) throws IllegalArgumentException;

    public double getHealthScale();

    public Entity getSpectatorTarget();

    public void setSpectatorTarget(Entity var1);

    @Deprecated
    public void sendTitle(String var1, String var2);

    public void sendTitle(String var1, String var2, int var3, int var4, int var5);

    public void resetTitle();

    public void spawnParticle(Particle var1, Location var2, int var3);

    public void spawnParticle(Particle var1, double var2, double var4, double var6, int var8);

    public <T> void spawnParticle(Particle var1, Location var2, int var3, T var4);

    public <T> void spawnParticle(Particle var1, double var2, double var4, double var6, int var8, T var9);

    public void spawnParticle(Particle var1, Location var2, int var3, double var4, double var6, double var8);

    public void spawnParticle(Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13);

    public <T> void spawnParticle(Particle var1, Location var2, int var3, double var4, double var6, double var8, T var10);

    public <T> void spawnParticle(Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, T var15);

    public void spawnParticle(Particle var1, Location var2, int var3, double var4, double var6, double var8, double var10);

    public void spawnParticle(Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15);

    public <T> void spawnParticle(Particle var1, Location var2, int var3, double var4, double var6, double var8, double var10, T var12);

    public <T> void spawnParticle(Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15, T var17);

    public AdvancementProgress getAdvancementProgress(Advancement var1);

    public String getLocale();

    @Override
    public Spigot org_bukkit_entity_Player$Spigot_spigot();

    public static class Spigot
    extends Entity.Spigot {
        public InetSocketAddress getRawAddress() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Deprecated
        public void playEffect(Location location, Effect effect, int id2, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Deprecated
        public boolean getCollidesWithEntities() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Deprecated
        public void setCollidesWithEntities(boolean collides) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void respawn() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Deprecated
        public String getLocale() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Set<Player> getHiddenPlayers() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void sendMessage(BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public /* varargs */ void sendMessage(BaseComponent ... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void sendMessage(ChatMessageType position, BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public /* varargs */ void sendMessage(ChatMessageType position, BaseComponent ... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}

