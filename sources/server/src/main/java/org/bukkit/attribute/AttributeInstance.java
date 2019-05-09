/*
 * Akarin Forge
 */
package org.bukkit.attribute;

import java.util.Collection;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public interface AttributeInstance {
    public Attribute getAttribute();

    public double getBaseValue();

    public void setBaseValue(double var1);

    public Collection<AttributeModifier> getModifiers();

    public void addModifier(AttributeModifier var1);

    public void removeModifier(AttributeModifier var1);

    public double getValue();

    public double getDefaultValue();
}

