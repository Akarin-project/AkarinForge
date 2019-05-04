/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Spellcaster;

public interface Evoker
extends Spellcaster {
    @Deprecated
    public Spell getCurrentSpell();

    @Deprecated
    public void setCurrentSpell(Spell var1);

    @Deprecated
    public static enum Spell {
        NONE,
        SUMMON,
        FANGS,
        WOLOLO,
        DISAPPEAR,
        BLINDNESS;
        

        private Spell() {
        }
    }

}

