/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftIllager;
import org.bukkit.entity.Spellcaster;

public class CraftSpellcaster
extends CraftIllager
implements Spellcaster {
    public CraftSpellcaster(CraftServer server, adm entity) {
        super(server, entity);
    }

    @Override
    public adm getHandle() {
        return (adm)super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftSpellcaster";
    }

    @Override
    public Spellcaster.Spell getSpell() {
        return Spellcaster.Spell.valueOf(this.getHandle().do().name());
    }

    @Override
    public void setSpell(Spellcaster.Spell spell) {
        Preconditions.checkArgument((boolean)(spell != null), (Object)"Use Spell.NONE");
        this.getHandle().a(adm.a.a(spell.ordinal()));
    }
}

