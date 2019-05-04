/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.MaterialData;

public class FurnaceAndDispenser
extends DirectionalContainer {
    @Deprecated
    public FurnaceAndDispenser(int type) {
        super(type);
    }

    public FurnaceAndDispenser(Material type) {
        super(type);
    }

    @Deprecated
    public FurnaceAndDispenser(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public FurnaceAndDispenser(Material type, byte data) {
        super(type, data);
    }

    @Override
    public FurnaceAndDispenser clone() {
        return (FurnaceAndDispenser)super.clone();
    }
}

