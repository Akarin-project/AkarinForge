/*
 * Akarin Forge
 */
package org.bukkit.map;

import java.util.List;
import org.bukkit.World;
import org.bukkit.map.MapRenderer;

public interface MapView {
    @Deprecated
    public short getId();

    public boolean isVirtual();

    public Scale getScale();

    public void setScale(Scale var1);

    public int getCenterX();

    public int getCenterZ();

    public void setCenterX(int var1);

    public void setCenterZ(int var1);

    public World getWorld();

    public void setWorld(World var1);

    public List<MapRenderer> getRenderers();

    public void addRenderer(MapRenderer var1);

    public boolean removeRenderer(MapRenderer var1);

    public boolean isUnlimitedTracking();

    public void setUnlimitedTracking(boolean var1);

    public static enum Scale {
        CLOSEST(0),
        CLOSE(1),
        NORMAL(2),
        FAR(3),
        FARTHEST(4);
        
        private byte value;

        private Scale(int value) {
            this.value = (byte)value;
        }

        @Deprecated
        public static Scale valueOf(byte value) {
            switch (value) {
                case 0: {
                    return CLOSEST;
                }
                case 1: {
                    return CLOSE;
                }
                case 2: {
                    return NORMAL;
                }
                case 3: {
                    return FAR;
                }
                case 4: {
                    return FARTHEST;
                }
            }
            return null;
        }

        @Deprecated
        public byte getValue() {
            return this.value;
        }
    }

}

