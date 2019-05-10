/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathEvent
extends EntityDeathEvent {
    private int newExp = 0;
    private String deathMessage = "";
    private int newLevel = 0;
    private int newTotalExp = 0;
    private boolean keepLevel = false;
    private boolean keepInventory = false;

    public PlayerDeathEvent(Player player, List<ItemStack> drops, int droppedExp, String deathMessage) {
        this(player, drops, droppedExp, 0, deathMessage);
    }

    public PlayerDeathEvent(Player player, List<ItemStack> drops, int droppedExp, int newExp, String deathMessage) {
        this(player, drops, droppedExp, newExp, 0, 0, deathMessage);
    }

    public PlayerDeathEvent(Player player, List<ItemStack> drops, int droppedExp, int newExp, int newTotalExp, int newLevel, String deathMessage) {
        super(player, drops, droppedExp);
        this.newExp = newExp;
        this.newTotalExp = newTotalExp;
        this.newLevel = newLevel;
        this.deathMessage = deathMessage;
    }

    @Override
    public Player getEntity() {
        return (Player)this.entity;
    }

    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }

    public String getDeathMessage() {
        return this.deathMessage;
    }

    public int getNewExp() {
        return this.newExp;
    }

    public void setNewExp(int exp) {
        this.newExp = exp;
    }

    public int getNewLevel() {
        return this.newLevel;
    }

    public void setNewLevel(int level) {
        this.newLevel = level;
    }

    public int getNewTotalExp() {
        return this.newTotalExp;
    }

    public void setNewTotalExp(int totalExp) {
        this.newTotalExp = totalExp;
    }

    public boolean getKeepLevel() {
        return this.keepLevel;
    }

    public void setKeepLevel(boolean keepLevel) {
        this.keepLevel = keepLevel;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    public boolean getKeepInventory() {
        return this.keepInventory;
    }
}

