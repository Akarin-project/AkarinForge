/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  net.md_5.specialsource.provider.InheritanceProvider
 */
package io.akarin.forge.remapper;

import java.util.Collection;
import java.util.HashSet;
import net.md_5.specialsource.provider.InheritanceProvider;

public class ClassInheritanceProvider
implements InheritanceProvider {
    public Collection<String> getParents(String className) {
        className = ReflectionTransformer.remapper.map(className);
        try {
            HashSet<String> parents = new HashSet<String>();
            Class reference = Class.forName(className.replace('/', '.').replace('$', '.'), false, this.getClass().getClassLoader());
            Class extend = reference.getSuperclass();
            if (extend != null) {
                parents.add(RemapUtils.reverseMap(extend));
            }
            for (Class inter : reference.getInterfaces()) {
                if (inter == null) continue;
                parents.add(RemapUtils.reverseMap(inter));
            }
            return parents;
        }
        catch (Exception parents) {
            return null;
        }
    }
}

