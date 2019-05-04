/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.attribute;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

public class CraftAttributeInstance
implements AttributeInstance {
    private final wd handle;
    private final Attribute attribute;

    public CraftAttributeInstance(wd handle, Attribute attribute) {
        this.handle = handle;
        this.attribute = attribute;
    }

    @Override
    public Attribute getAttribute() {
        return this.attribute;
    }

    @Override
    public double getBaseValue() {
        return this.handle.b();
    }

    @Override
    public void setBaseValue(double d2) {
        this.handle.a(d2);
    }

    @Override
    public Collection<AttributeModifier> getModifiers() {
        ArrayList<AttributeModifier> result = new ArrayList<AttributeModifier>();
        for (we nms : this.handle.c()) {
            result.add(CraftAttributeInstance.convert(nms));
        }
        return result;
    }

    @Override
    public void addModifier(AttributeModifier modifier) {
        Preconditions.checkArgument((boolean)(modifier != null), (Object)"modifier");
        this.handle.c(CraftAttributeInstance.convert(modifier));
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        Preconditions.checkArgument((boolean)(modifier != null), (Object)"modifier");
        this.handle.c(CraftAttributeInstance.convert(modifier));
    }

    @Override
    public double getValue() {
        return this.handle.e();
    }

    @Override
    public double getDefaultValue() {
        return this.handle.a().b();
    }

    private static we convert(AttributeModifier bukkit) {
        return new we(bukkit.getUniqueId(), bukkit.getName(), bukkit.getAmount(), bukkit.getOperation().ordinal());
    }

    private static AttributeModifier convert(we nms) {
        return new AttributeModifier(nms.a(), nms.b(), nms.d(), AttributeModifier.Operation.values()[nms.c()]);
    }
}

