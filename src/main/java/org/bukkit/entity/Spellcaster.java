/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Illager;

public interface Spellcaster
extends Illager {
    public Spell getSpell();

    public void setSpell(Spell var1);

    public static enum Spell {
        NONE,
        SUMMON_VEX,
        FANGS,
        WOLOLO,
        DISAPPEAR,
        BLINDNESS;
        

        private Spell() {
        }
    }

}

