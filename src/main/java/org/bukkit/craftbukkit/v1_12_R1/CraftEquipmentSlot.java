/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1;

import org.bukkit.inventory.EquipmentSlot;

public class CraftEquipmentSlot {
    private static final vl[] slots = new vl[EquipmentSlot.values().length];
    private static final EquipmentSlot[] enums = new EquipmentSlot[vl.values().length];

    private static void set(EquipmentSlot type, vl value) {
        CraftEquipmentSlot.slots[type.ordinal()] = value;
        CraftEquipmentSlot.enums[value.ordinal()] = type;
    }

    public static EquipmentSlot getSlot(vl nms) {
        return enums[nms.ordinal()];
    }

    public static vl getNMS(EquipmentSlot slot) {
        return slots[slot.ordinal()];
    }

    static {
        CraftEquipmentSlot.set(EquipmentSlot.HAND, vl.a);
        CraftEquipmentSlot.set(EquipmentSlot.OFF_HAND, vl.b);
        CraftEquipmentSlot.set(EquipmentSlot.FEET, vl.c);
        CraftEquipmentSlot.set(EquipmentSlot.LEGS, vl.d);
        CraftEquipmentSlot.set(EquipmentSlot.CHEST, vl.e);
        CraftEquipmentSlot.set(EquipmentSlot.HEAD, vl.f);
    }
}

