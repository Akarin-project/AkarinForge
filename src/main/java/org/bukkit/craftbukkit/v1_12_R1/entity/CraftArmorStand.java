/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class CraftArmorStand
extends CraftLivingEntity
implements ArmorStand {
    public CraftArmorStand(CraftServer server, abz entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftArmorStand";
    }

    @Override
    public EntityType getType() {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public abz getHandle() {
        return (abz)super.getHandle();
    }

    @Override
    public ItemStack getItemInHand() {
        return this.getEquipment().getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        this.getEquipment().setItemInHand(item);
    }

    @Override
    public ItemStack getBoots() {
        return this.getEquipment().getBoots();
    }

    @Override
    public void setBoots(ItemStack item) {
        this.getEquipment().setBoots(item);
    }

    @Override
    public ItemStack getLeggings() {
        return this.getEquipment().getLeggings();
    }

    @Override
    public void setLeggings(ItemStack item) {
        this.getEquipment().setLeggings(item);
    }

    @Override
    public ItemStack getChestplate() {
        return this.getEquipment().getChestplate();
    }

    @Override
    public void setChestplate(ItemStack item) {
        this.getEquipment().setChestplate(item);
    }

    @Override
    public ItemStack getHelmet() {
        return this.getEquipment().getHelmet();
    }

    @Override
    public void setHelmet(ItemStack item) {
        this.getEquipment().setHelmet(item);
    }

    @Override
    public EulerAngle getBodyPose() {
        return CraftArmorStand.fromNMS(this.getHandle().bE);
    }

    @Override
    public void setBodyPose(EulerAngle pose) {
        this.getHandle().b(CraftArmorStand.toNMS(pose));
    }

    @Override
    public EulerAngle getLeftArmPose() {
        return CraftArmorStand.fromNMS(this.getHandle().bF);
    }

    @Override
    public void setLeftArmPose(EulerAngle pose) {
        this.getHandle().c(CraftArmorStand.toNMS(pose));
    }

    @Override
    public EulerAngle getRightArmPose() {
        return CraftArmorStand.fromNMS(this.getHandle().bG);
    }

    @Override
    public void setRightArmPose(EulerAngle pose) {
        this.getHandle().d(CraftArmorStand.toNMS(pose));
    }

    @Override
    public EulerAngle getLeftLegPose() {
        return CraftArmorStand.fromNMS(this.getHandle().bH);
    }

    @Override
    public void setLeftLegPose(EulerAngle pose) {
        this.getHandle().e(CraftArmorStand.toNMS(pose));
    }

    @Override
    public EulerAngle getRightLegPose() {
        return CraftArmorStand.fromNMS(this.getHandle().bI);
    }

    @Override
    public void setRightLegPose(EulerAngle pose) {
        this.getHandle().f(CraftArmorStand.toNMS(pose));
    }

    @Override
    public EulerAngle getHeadPose() {
        return CraftArmorStand.fromNMS(this.getHandle().bD);
    }

    @Override
    public void setHeadPose(EulerAngle pose) {
        this.getHandle().a(CraftArmorStand.toNMS(pose));
    }

    @Override
    public boolean hasBasePlate() {
        return !this.getHandle().s();
    }

    @Override
    public void setBasePlate(boolean basePlate) {
        this.getHandle().o(!basePlate);
    }

    @Override
    public void setGravity(boolean gravity) {
        super.setGravity(gravity);
        this.getHandle().Q = !gravity;
    }

    @Override
    public boolean isVisible() {
        return !this.getHandle().aX();
    }

    @Override
    public void setVisible(boolean visible) {
        this.getHandle().h(!visible);
    }

    @Override
    public boolean hasArms() {
        return this.getHandle().r();
    }

    @Override
    public void setArms(boolean arms) {
        this.getHandle().n(arms);
    }

    @Override
    public boolean isSmall() {
        return this.getHandle().p();
    }

    @Override
    public void setSmall(boolean small) {
        this.getHandle().m(small);
    }

    private static EulerAngle fromNMS(fn old) {
        return new EulerAngle(Math.toRadians(old.b()), Math.toRadians(old.c()), Math.toRadians(old.d()));
    }

    private static fn toNMS(EulerAngle old) {
        return new fn((float)Math.toDegrees(old.getX()), (float)Math.toDegrees(old.getY()), (float)Math.toDegrees(old.getZ()));
    }

    @Override
    public boolean isMarker() {
        return this.getHandle().t();
    }

    @Override
    public void setMarker(boolean marker) {
        this.getHandle().p(marker);
    }
}

