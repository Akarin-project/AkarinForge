/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableBiMap
 */
package org.bukkit.craftbukkit.v1_12_R1.scoreboard;

import com.google.common.collect.ImmutableBiMap;
import org.bukkit.scoreboard.DisplaySlot;

class CraftScoreboardTranslations {
    static final int MAX_DISPLAY_SLOT = 3;
    static ImmutableBiMap<DisplaySlot, String> SLOTS = ImmutableBiMap.of((Object)((Object)DisplaySlot.BELOW_NAME), (Object)"belowName", (Object)((Object)DisplaySlot.PLAYER_LIST), (Object)"list", (Object)((Object)DisplaySlot.SIDEBAR), (Object)"sidebar");

    private CraftScoreboardTranslations() {
    }

    static DisplaySlot toBukkitSlot(int i2) {
        return (DisplaySlot)((Object)SLOTS.inverse().get((Object)bhk.b(i2)));
    }

    static int fromBukkitSlot(DisplaySlot slot) {
        return bhk.h((String)SLOTS.get((Object)slot));
    }
}

