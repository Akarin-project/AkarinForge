/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreature;
import org.bukkit.entity.Ageable;

public class CraftAgeable
extends CraftCreature
implements Ageable {
    public CraftAgeable(CraftServer server, vd entity) {
        super(server, entity);
    }

    @Override
    public int getAge() {
        return this.getHandle().l();
    }

    @Override
    public void setAge(int age2) {
        this.getHandle().c(age2);
    }

    @Override
    public void setAgeLock(boolean lock) {
        this.getHandle().ageLocked = lock;
    }

    @Override
    public boolean getAgeLock() {
        return this.getHandle().ageLocked;
    }

    @Override
    public void setBaby() {
        if (this.isAdult()) {
            this.setAge(-24000);
        }
    }

    @Override
    public void setAdult() {
        if (!this.isAdult()) {
            this.setAge(0);
        }
    }

    @Override
    public boolean isAdult() {
        return this.getAge() >= 0;
    }

    @Override
    public boolean canBreed() {
        return this.getAge() == 0;
    }

    @Override
    public void setBreed(boolean breed) {
        if (breed) {
            this.setAge(0);
        } else if (this.isAdult()) {
            this.setAge(6000);
        }
    }

    @Override
    public vd getHandle() {
        return (vd)this.entity;
    }

    @Override
    public String toString() {
        return "CraftAgeable";
    }
}

