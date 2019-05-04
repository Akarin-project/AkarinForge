/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PressureSensor;

public class PressurePlate
extends MaterialData
implements PressureSensor {
    public PressurePlate() {
        super(Material.WOOD_PLATE);
    }

    @Deprecated
    public PressurePlate(int type) {
        super(type);
    }

    public PressurePlate(Material type) {
        super(type);
    }

    @Deprecated
    public PressurePlate(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public PressurePlate(Material type, byte data) {
        super(type, data);
    }

    @Override
    public boolean isPressed() {
        return this.getData() == 1;
    }

    @Override
    public String toString() {
        return super.toString() + (this.isPressed() ? " PRESSED" : "");
    }

    @Override
    public PressurePlate clone() {
        return (PressurePlate)super.clone();
    }
}

