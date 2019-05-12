package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {

	@Override
	public PlayerInventory getInventory() {

		return null;
	}

	@Override
	public Inventory getEnderChest() {

		return null;
	}

	@Override
	public MainHand getMainHand() {

		return null;
	}

	@Override
	public boolean setWindowProperty(Property prop, int value) {

		return false;
	}

	@Override
	public InventoryView getOpenInventory() {

		return null;
	}

	@Override
	public InventoryView openInventory(Inventory inventory) {

		return null;
	}

	@Override
	public InventoryView openWorkbench(Location location, boolean force) {

		return null;
	}

	@Override
	public InventoryView openEnchanting(Location location, boolean force) {

		return null;
	}

	@Override
	public void openInventory(InventoryView inventory) {

		
	}

	@Override
	public InventoryView openMerchant(Villager trader, boolean force) {

		return null;
	}

	@Override
	public InventoryView openMerchant(Merchant merchant, boolean force) {

		return null;
	}

	@Override
	public void closeInventory() {

		
	}

	@Override
	public ItemStack getItemInHand() {

		return null;
	}

	@Override
	public void setItemInHand(ItemStack item) {

		
	}

	@Override
	public ItemStack getItemOnCursor() {

		return null;
	}

	@Override
	public void setItemOnCursor(ItemStack item) {

		
	}

	@Override
	public boolean hasCooldown(Material material) {

		return false;
	}

	@Override
	public int getCooldown(Material material) {

		return 0;
	}

	@Override
	public void setCooldown(Material material, int ticks) {

		
	}

	@Override
	public boolean isSleeping() {

		return false;
	}

	@Override
	public int getSleepTicks() {

		return 0;
	}

	@Override
	public GameMode getGameMode() {

		return null;
	}

	@Override
	public void setGameMode(GameMode mode) {

		
	}

	@Override
	public boolean isBlocking() {

		return false;
	}

	@Override
	public boolean isHandRaised() {

		return false;
	}

	@Override
	public int getExpToLevel() {

		return 0;
	}

	@Override
	public Entity getShoulderEntityLeft() {

		return null;
	}

	@Override
	public void setShoulderEntityLeft(Entity entity) {

		
	}

	@Override
	public Entity getShoulderEntityRight() {

		return null;
	}

	@Override
	public void setShoulderEntityRight(Entity entity) {

		
	}
    
}
