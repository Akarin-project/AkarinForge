/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.material.ExtendedRails;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PressureSensor;
import org.bukkit.material.Rails;

public class DetectorRail
extends ExtendedRails
implements PressureSensor {
    public DetectorRail() {
        super(Material.DETECTOR_RAIL);
    }

    @Deprecated
    public DetectorRail(int type) {
        super(type);
    }

    public DetectorRail(Material type) {
        super(type);
    }

    @Deprecated
    public DetectorRail(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public DetectorRail(Material type, byte data) {
        super(type, data);
    }

    @Override
    public boolean isPressed() {
        return (this.getData() & 8) == 8;
    }

    public void setPressed(boolean isPressed) {
        this.setData((byte)(isPressed ? this.getData() | 8 : this.getData() & -9));
    }

    @Override
    public DetectorRail clone() {
        return (DetectorRail)super.clone();
    }
}

