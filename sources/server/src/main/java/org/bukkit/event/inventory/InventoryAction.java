/*
 * Akarin Forge
 */
package org.bukkit.event.inventory;

public enum InventoryAction {
    NOTHING,
    PICKUP_ALL,
    PICKUP_SOME,
    PICKUP_HALF,
    PICKUP_ONE,
    PLACE_ALL,
    PLACE_SOME,
    PLACE_ONE,
    SWAP_WITH_CURSOR,
    DROP_ALL_CURSOR,
    DROP_ONE_CURSOR,
    DROP_ALL_SLOT,
    DROP_ONE_SLOT,
    MOVE_TO_OTHER_INVENTORY,
    HOTBAR_MOVE_AND_READD,
    HOTBAR_SWAP,
    CLONE_STACK,
    COLLECT_TO_CURSOR,
    UNKNOWN;
    

    private InventoryAction() {
    }
}

