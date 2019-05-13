package io.akarin.forge.server.layers;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;

public class SneakyRemapper extends JarRemapper {
    public SneakyRemapper(JarMapping jarMapping) {
        super(jarMapping);
    }

    public String mapSignature(String signature, boolean typeSignature) {
        try {
            return super.mapSignature(signature, typeSignature);
        } catch (Throwable t) {
            return signature;
        }
    }

    public String mapFieldName(String owner, String name, String desc, int access) {
        return super.mapFieldName(owner, name, desc, -1);
    }
}
