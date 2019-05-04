/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class Tripwire
extends MaterialData {
    public Tripwire() {
        super(Material.TRIPWIRE);
    }

    @Deprecated
    public Tripwire(int type) {
        super(type);
    }

    @Deprecated
    public Tripwire(int type, byte data) {
        super(type, data);
    }

    public boolean isActivated() {
        return (this.getData() & 4) != 0;
    }

    public void setActivated(boolean act2) {
        int dat = this.getData() & 11;
        if (act2) {
            dat |= 4;
        }
        this.setData((byte)dat);
    }

    public boolean isObjectTriggering() {
        return (this.getData() & 1) != 0;
    }

    public void setObjectTriggering(boolean trig) {
        int dat = this.getData() & 14;
        if (trig) {
            dat |= 1;
        }
        this.setData((byte)dat);
    }

    @Override
    public Tripwire clone() {
        return (Tripwire)super.clone();
    }

    @Override
    public String toString() {
        return super.toString() + (this.isActivated() ? " Activated" : "") + (this.isObjectTriggering() ? " Triggered" : "");
    }
}

