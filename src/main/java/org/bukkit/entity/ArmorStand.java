/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public interface ArmorStand
extends LivingEntity {
    public ItemStack getItemInHand();

    public void setItemInHand(ItemStack var1);

    public ItemStack getBoots();

    public void setBoots(ItemStack var1);

    public ItemStack getLeggings();

    public void setLeggings(ItemStack var1);

    public ItemStack getChestplate();

    public void setChestplate(ItemStack var1);

    public ItemStack getHelmet();

    public void setHelmet(ItemStack var1);

    public EulerAngle getBodyPose();

    public void setBodyPose(EulerAngle var1);

    public EulerAngle getLeftArmPose();

    public void setLeftArmPose(EulerAngle var1);

    public EulerAngle getRightArmPose();

    public void setRightArmPose(EulerAngle var1);

    public EulerAngle getLeftLegPose();

    public void setLeftLegPose(EulerAngle var1);

    public EulerAngle getRightLegPose();

    public void setRightLegPose(EulerAngle var1);

    public EulerAngle getHeadPose();

    public void setHeadPose(EulerAngle var1);

    public boolean hasBasePlate();

    public void setBasePlate(boolean var1);

    public boolean isVisible();

    public void setVisible(boolean var1);

    public boolean hasArms();

    public void setArms(boolean var1);

    public boolean isSmall();

    public void setSmall(boolean var1);

    public boolean isMarker();

    public void setMarker(boolean var1);
}

