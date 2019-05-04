/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Animals;

public interface Rabbit
extends Animals {
    public Type getRabbitType();

    public void setRabbitType(Type var1);

    public static enum Type {
        BROWN,
        WHITE,
        BLACK,
        BLACK_AND_WHITE,
        GOLD,
        SALT_AND_PEPPER,
        THE_KILLER_BUNNY;
        

        private Type() {
        }
    }

}

