/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftContainer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryBeacon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftBeacon
extends CraftContainer<avh>
implements Beacon {
    public CraftBeacon(Block block) {
        super(block, avh.class);
    }

    public CraftBeacon(Material material, avh te2) {
        super(material, te2);
    }

    @Override
    public BeaconInventory getSnapshotInventory() {
        return new CraftInventoryBeacon((avh)this.getSnapshot());
    }

    @Override
    public BeaconInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }
        return new CraftInventoryBeacon((avh)this.getTileEntity());
    }

    @Override
    public Collection<LivingEntity> getEntitiesInRange() {
        avj tileEntity = this.getTileEntityFromWorld();
        if (tileEntity instanceof avh) {
            avh beacon = (avh)tileEntity;
            List<aed> nms = beacon.getHumansInRange();
            ArrayList<LivingEntity> bukkit = new ArrayList<LivingEntity>(nms.size());
            for (aed human : nms) {
                bukkit.add(human.getBukkitEntity());
            }
            return bukkit;
        }
        return new ArrayList<LivingEntity>();
    }

    @Override
    public int getTier() {
        return ((avh)this.getSnapshot()).k;
    }

    @Override
    public PotionEffect getPrimaryEffect() {
        return ((avh)this.getSnapshot()).getPrimaryEffect();
    }

    @Override
    public void setPrimaryEffect(PotionEffectType effect) {
        ((avh)this.getSnapshot()).l = effect != null ? uz.a(effect.getId()) : null;
    }

    @Override
    public PotionEffect getSecondaryEffect() {
        return ((avh)this.getSnapshot()).getSecondaryEffect();
    }

    @Override
    public void setSecondaryEffect(PotionEffectType effect) {
        ((avh)this.getSnapshot()).m = effect != null ? uz.a(effect.getId()) : null;
    }

    @Override
    public String getCustomName() {
        avh beacon = (avh)this.getSnapshot();
        return beacon.n_() ? beacon.h_() : null;
    }

    @Override
    public void setCustomName(String name) {
        ((avh)this.getSnapshot()).a(name);
    }
}

