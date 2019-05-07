/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.base.Functions
 *  com.google.common.collect.ImmutableMap
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.event.entity;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityDamageEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private static final DamageModifier[] MODIFIERS = DamageModifier.values();
    private static final Function<? super Double, Double> ZERO = Functions.constant(-0.0);
    private final Map<DamageModifier, Double> modifiers;
    private final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions;
    private final Map<DamageModifier, Double> originals;
    private boolean cancelled;
    private final DamageCause cause;

    @Deprecated
    public EntityDamageEvent(Entity damagee, DamageCause cause, double damage) {
        this(damagee, cause, new EnumMap<DamageModifier, Double>((Map<DamageModifier, Double>)ImmutableMap.of((DamageModifier.BASE), damage)), new EnumMap(ImmutableMap.of((Object)((Object)DamageModifier.BASE), ZERO)));
    }

    public EntityDamageEvent(Entity damagee, DamageCause cause, Map<DamageModifier, Double> modifiers, Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        super(damagee);
        Validate.isTrue((boolean)modifiers.containsKey((Object)DamageModifier.BASE), (String)"BASE DamageModifier missing");
        Validate.isTrue((boolean)(!modifiers.containsKey(null)), (String)"Cannot have null DamageModifier");
        Validate.noNullElements(modifiers.values(), (String)"Cannot have null modifier values");
        Validate.isTrue((boolean)modifiers.keySet().equals(modifierFunctions.keySet()), (String)"Must have a modifier function for each DamageModifier");
        Validate.noNullElements(modifierFunctions.values(), (String)"Cannot have null modifier function");
        this.originals = new EnumMap<DamageModifier, Double>(modifiers);
        this.cause = cause;
        this.modifiers = modifiers;
        this.modifierFunctions = modifierFunctions;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public double getOriginalDamage(DamageModifier type) throws IllegalArgumentException {
        Double damage = this.originals.get((Object)type);
        if (damage != null) {
            return damage;
        }
        if (type == null) {
            throw new IllegalArgumentException("Cannot have null DamageModifier");
        }
        return 0.0;
    }

    public void setDamage(DamageModifier type, double damage) throws IllegalArgumentException, UnsupportedOperationException {
        if (!this.modifiers.containsKey((Object)type)) {
            throw type == null ? new IllegalArgumentException("Cannot have null DamageModifier") : new UnsupportedOperationException((Object)((Object)type) + " is not applicable to " + this.getEntity());
        }
        this.modifiers.put(type, damage);
    }

    public double getDamage(DamageModifier type) throws IllegalArgumentException {
        Validate.notNull((Object)((Object)type), (String)"Cannot have null DamageModifier");
        Double damage = this.modifiers.get((Object)type);
        return damage == null ? 0.0 : damage;
    }

    public boolean isApplicable(DamageModifier type) throws IllegalArgumentException {
        Validate.notNull((Object)((Object)type), (String)"Cannot have null DamageModifier");
        return this.modifiers.containsKey((Object)type);
    }

    public double getDamage() {
        return this.getDamage(DamageModifier.BASE);
    }

    public final double getFinalDamage() {
        double damage = 0.0;
        for (DamageModifier modifier : MODIFIERS) {
            damage += this.getDamage(modifier);
        }
        return damage;
    }

    public void setDamage(double damage) {
        double remaining = damage;
        double oldRemaining = this.getDamage(DamageModifier.BASE);
        for (DamageModifier modifier : MODIFIERS) {
            if (!this.isApplicable(modifier)) continue;
            Function<? super Double, Double> modifierFunction = this.modifierFunctions.get((Object)modifier);
            double newVanilla = (Double)modifierFunction.apply(remaining);
            double oldVanilla = (Double)modifierFunction.apply(oldRemaining);
            double difference = oldVanilla - newVanilla;
            double old = this.getDamage(modifier);
            if (old > 0.0) {
                this.setDamage(modifier, Math.max(0.0, old - difference));
            } else {
                this.setDamage(modifier, Math.min(0.0, old - difference));
            }
            remaining += newVanilla;
            oldRemaining += oldVanilla;
        }
        this.setDamage(DamageModifier.BASE, damage);
    }

    public DamageCause getCause() {
        return this.cause;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static enum DamageCause {
        CONTACT,
        ENTITY_ATTACK,
        ENTITY_SWEEP_ATTACK,
        PROJECTILE,
        SUFFOCATION,
        FALL,
        FIRE,
        FIRE_TICK,
        MELTING,
        LAVA,
        DROWNING,
        BLOCK_EXPLOSION,
        ENTITY_EXPLOSION,
        VOID,
        LIGHTNING,
        SUICIDE,
        STARVATION,
        POISON,
        MAGIC,
        WITHER,
        FALLING_BLOCK,
        THORNS,
        DRAGON_BREATH,
        CUSTOM,
        FLY_INTO_WALL,
        HOT_FLOOR,
        CRAMMING;
        

        private DamageCause() {
        }
    }

    @Deprecated
    public static enum DamageModifier {
        BASE,
        HARD_HAT,
        BLOCKING,
        ARMOR,
        RESISTANCE,
        MAGIC,
        ABSORPTION;
        

        private DamageModifier() {
        }
    }

}

