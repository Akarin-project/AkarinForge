package io.akarin.forge.server.layers;

import java.util.Collection;
import java.util.HashSet;

import io.akarin.forge.server.layers.reflection.ReflectionTransformer;
import net.md_5.specialsource.provider.InheritanceProvider;

public class ClassInheritanceProvider implements InheritanceProvider {
	
    public Collection<String> getParents(String className) {
        className = ReflectionTransformer.remapper.map(className);
        try {
            HashSet<String> parents = new HashSet<String>();
            Class<?> reference = Class.forName(className.replace('/', '.').replace('$', '.'), false, this.getClass().getClassLoader());
            Class<?> extend = reference.getSuperclass();
            if (extend != null) {
                parents.add(Remaps.reverseMapClassName(extend));
            }
            for (Class<?> inter : reference.getInterfaces()) {
                if (inter == null) continue;
                parents.add(Remaps.reverseMapClassName(inter));
            }
            return parents;
        } catch (Throwable parents) {
            return null;
        }
    }
}
