/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit;

import org.apache.commons.lang3.Validate;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.potion.Potion;

public class CraftEffect {
    public static <T> int getDataValue(Effect effect, T data) {
        int datavalue;
        block0 : switch (effect) {
            case VILLAGER_PLANT_GROW: {
                datavalue = (Integer)data;
                break;
            }
            case POTION_BREAK: {
                datavalue = ((Potion)data).toDamageValue() & 63;
                break;
            }
            case RECORD_PLAY: {
                Validate.isTrue((boolean)((Material)((Object)data)).isRecord(), (String)"Invalid record type!", (Object[])new Object[0]);
                datavalue = ((Material)((Object)data)).getId();
                break;
            }
            case SMOKE: {
                switch ((BlockFace)((Object)data)) {
                    case SOUTH_EAST: {
                        datavalue = 0;
                        break block0;
                    }
                    case SOUTH: {
                        datavalue = 1;
                        break block0;
                    }
                    case SOUTH_WEST: {
                        datavalue = 2;
                        break block0;
                    }
                    case EAST: {
                        datavalue = 3;
                        break block0;
                    }
                    case UP: 
                    case SELF: {
                        datavalue = 4;
                        break block0;
                    }
                    case WEST: {
                        datavalue = 5;
                        break block0;
                    }
                    case NORTH_EAST: {
                        datavalue = 6;
                        break block0;
                    }
                    case NORTH: {
                        datavalue = 7;
                        break block0;
                    }
                    case NORTH_WEST: {
                        datavalue = 8;
                        break block0;
                    }
                }
                throw new IllegalArgumentException("Bad smoke direction!");
            }
            case STEP_SOUND: {
                Validate.isTrue((boolean)((Material)((Object)data)).isBlock(), (String)"Material is not a block!", (Object[])new Object[0]);
                datavalue = ((Material)((Object)data)).getId();
                break;
            }
            default: {
                datavalue = 0;
            }
        }
        return datavalue;
    }

}

