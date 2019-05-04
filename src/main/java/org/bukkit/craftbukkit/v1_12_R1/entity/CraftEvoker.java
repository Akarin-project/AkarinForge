/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSpellcaster;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;

public class CraftEvoker
extends CraftSpellcaster
implements Evoker {
    public CraftEvoker(CraftServer server, acx entity) {
        super(server, entity);
    }

    @Override
    public acx getHandle() {
        return (acx)super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftEvoker";
    }

    @Override
    public EntityType getType() {
        return EntityType.EVOKER;
    }

    @Override
    public Evoker.Spell getCurrentSpell() {
        return Evoker.Spell.values()[this.getHandle().do().ordinal()];
    }

    @Override
    public void setCurrentSpell(Evoker.Spell spell) {
        this.getHandle().a(spell == null ? adm.a.a : adm.a.a(spell.ordinal()));
    }
}

