/*
 * Akarin Forge
 */
package org.bukkit.block;

import java.util.HashMap;
import java.util.Map;

public enum PistonMoveReaction {
    MOVE(0),
    BREAK(1),
    BLOCK(2),
    IGNORE(3),
    PUSH_ONLY(4);
    
    private int id;
    private static Map<Integer, PistonMoveReaction> byId;

    private PistonMoveReaction(int id2) {
        this.id = id2;
    }

    @Deprecated
    public int getId() {
        return this.id;
    }

    @Deprecated
    public static PistonMoveReaction getById(int id2) {
        return byId.get(id2);
    }

    static {
        byId = new HashMap<Integer, PistonMoveReaction>();
        for (PistonMoveReaction reaction : PistonMoveReaction.values()) {
            byId.put(reaction.id, reaction);
        }
    }
}

