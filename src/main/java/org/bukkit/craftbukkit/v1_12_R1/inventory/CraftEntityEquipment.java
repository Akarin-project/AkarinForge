/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CraftEntityEquipment
implements EntityEquipment {
    private final CraftLivingEntity entity;

    public CraftEntityEquipment(CraftLivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public ItemStack getItemInMainHand() {
        return this.getEquipment(vl.a);
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        this.setEquipment(vl.a, item);
    }

    @Override
    public ItemStack getItemInOffHand() {
        return this.getEquipment(vl.b);
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        this.setEquipment(vl.b, item);
    }

    @Override
    public ItemStack getItemInHand() {
        return this.getItemInMainHand();
    }

    @Override
    public void setItemInHand(ItemStack stack) {
        this.setItemInMainHand(stack);
    }

    @Override
    public ItemStack getHelmet() {
        return this.getEquipment(vl.f);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        this.setEquipment(vl.f, helmet);
    }

    @Override
    public ItemStack getChestplate() {
        return this.getEquipment(vl.e);
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        this.setEquipment(vl.e, chestplate);
    }

    @Override
    public ItemStack getLeggings() {
        return this.getEquipment(vl.d);
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        this.setEquipment(vl.d, leggings);
    }

    @Override
    public ItemStack getBoots() {
        return this.getEquipment(vl.c);
    }

    @Override
    public void setBoots(ItemStack boots) {
        this.setEquipment(vl.c, boots);
    }

    @Override
    public ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[]{this.getEquipment(vl.c), this.getEquipment(vl.d), this.getEquipment(vl.e), this.getEquipment(vl.f)};
        return armor;
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        this.setEquipment(vl.c, items.length >= 1 ? items[0] : null);
        this.setEquipment(vl.d, items.length >= 2 ? items[1] : null);
        this.setEquipment(vl.e, items.length >= 3 ? items[2] : null);
        this.setEquipment(vl.f, items.length >= 4 ? items[3] : null);
    }

    private ItemStack getEquipment(vl slot) {
        return CraftItemStack.asBukkitCopy(this.entity.getHandle().b(slot));
    }

    private void setEquipment(vl slot, ItemStack stack) {
        this.entity.getHandle().a(slot, CraftItemStack.asNMSCopy(stack));
    }

    @Override
    public void clear() {
        for (vl slot : vl.values()) {
            this.setEquipment(slot, null);
        }
    }

    @Override
    public Entity getHolder() {
        return this.entity;
    }

    @Override
    public float getItemInHandDropChance() {
        return this.getItemInMainHandDropChance();
    }

    @Override
    public void setItemInHandDropChance(float chance) {
        this.setItemInMainHandDropChance(chance);
    }

    @Override
    public float getItemInMainHandDropChance() {
        return this.getDropChance(vl.a);
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        this.setDropChance(vl.a, chance);
    }

    @Override
    public float getItemInOffHandDropChance() {
        return this.getDropChance(vl.b);
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        this.setDropChance(vl.b, chance);
    }

    @Override
    public float getHelmetDropChance() {
        return this.getDropChance(vl.f);
    }

    @Override
    public void setHelmetDropChance(float chance) {
        this.setDropChance(vl.f, chance);
    }

    @Override
    public float getChestplateDropChance() {
        return this.getDropChance(vl.e);
    }

    @Override
    public void setChestplateDropChance(float chance) {
        this.setDropChance(vl.e, chance);
    }

    @Override
    public float getLeggingsDropChance() {
        return this.getDropChance(vl.d);
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        this.setDropChance(vl.d, chance);
    }

    @Override
    public float getBootsDropChance() {
        return this.getDropChance(vl.c);
    }

    @Override
    public void setBootsDropChance(float chance) {
        this.setDropChance(vl.c, chance);
    }

    private void setDropChance(vl slot, float chance) {
        if (slot == vl.a || slot == vl.b) {
            ((vq)this.entity.getHandle()).bt[slot.b()] = chance - 0.1f;
        } else {
            ((vq)this.entity.getHandle()).bu[slot.b()] = chance - 0.1f;
        }
    }

    private float getDropChance(vl slot) {
        if (slot == vl.a || slot == vl.b) {
            return ((vq)this.entity.getHandle()).bt[slot.b()] + 0.1f;
        }
        return ((vq)this.entity.getHandle()).bu[slot.b()] + 0.1f;
    }
}

