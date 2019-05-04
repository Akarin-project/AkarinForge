/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.attribute;

import com.google.common.base.Preconditions;
import java.util.Locale;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_12_R1.attribute.CraftAttributeInstance;

public class CraftAttributeMap
implements Attributable {
    private final wg handle;

    public CraftAttributeMap(wg handle) {
        this.handle = handle;
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        Preconditions.checkArgument((boolean)(attribute != null), (Object)"attribute");
        wd nms = this.handle.a(CraftAttributeMap.toMinecraft(attribute.name()));
        return nms == null ? null : new CraftAttributeInstance(nms, attribute);
    }

    static String toMinecraft(String bukkit) {
        int first = bukkit.indexOf(95);
        int second = bukkit.indexOf(95, first + 1);
        StringBuilder sb2 = new StringBuilder(bukkit.toLowerCase(Locale.ENGLISH));
        sb2.setCharAt(first, '.');
        if (second != -1) {
            sb2.deleteCharAt(second);
            sb2.setCharAt(second, bukkit.charAt(second + 1));
        }
        return sb2.toString();
    }
}

