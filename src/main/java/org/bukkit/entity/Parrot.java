/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;

public interface Parrot
extends Animals,
Tameable,
Sittable {
    public Variant getVariant();

    public void setVariant(Variant var1);

    public static enum Variant {
        RED,
        BLUE,
        GREEN,
        CYAN,
        GRAY;
        

        private Variant() {
        }
    }

}

