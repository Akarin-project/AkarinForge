/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 */
package org.bukkit.event.entity;

import com.google.common.base.Function;
import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageByBlockEvent
extends EntityDamageEvent {
    private final Block damager;

    @Deprecated
    public EntityDamageByBlockEvent(Block damager, Entity damagee, EntityDamageEvent.DamageCause cause, double damage) {
        super(damagee, cause, damage);
        this.damager = damager;
    }

    public EntityDamageByBlockEvent(Block damager, Entity damagee, EntityDamageEvent.DamageCause cause, Map<EntityDamageEvent.DamageModifier, Double> modifiers, Map<EntityDamageEvent.DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        super(damagee, cause, modifiers, modifierFunctions);
        this.damager = damager;
    }

    public Block getDamager() {
        return this.damager;
    }
}

