/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.material.MaterialData;

public class NetherWarts
extends MaterialData {
    public NetherWarts() {
        super(Material.NETHER_WARTS);
    }

    public NetherWarts(NetherWartsState state) {
        this();
        this.setState(state);
    }

    @Deprecated
    public NetherWarts(int type) {
        super(type);
    }

    public NetherWarts(Material type) {
        super(type);
    }

    @Deprecated
    public NetherWarts(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public NetherWarts(Material type, byte data) {
        super(type, data);
    }

    public NetherWartsState getState() {
        switch (this.getData()) {
            case 0: {
                return NetherWartsState.SEEDED;
            }
            case 1: {
                return NetherWartsState.STAGE_ONE;
            }
            case 2: {
                return NetherWartsState.STAGE_TWO;
            }
        }
        return NetherWartsState.RIPE;
    }

    public void setState(NetherWartsState state) {
        switch (state) {
            case SEEDED: {
                this.setData(0);
                return;
            }
            case STAGE_ONE: {
                this.setData(1);
                return;
            }
            case STAGE_TWO: {
                this.setData(2);
                return;
            }
            case RIPE: {
                this.setData(3);
                return;
            }
        }
    }

    @Override
    public String toString() {
        return (Object)((Object)this.getState()) + " " + super.toString();
    }

    @Override
    public NetherWarts clone() {
        return (NetherWarts)super.clone();
    }

}

