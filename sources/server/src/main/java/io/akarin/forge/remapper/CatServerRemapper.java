/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  net.md_5.specialsource.JarMapping
 *  net.md_5.specialsource.JarRemapper
 */
package io.akarin.forge.remapper;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;

public class CatServerRemapper
extends JarRemapper {
    public CatServerRemapper(JarMapping jarMapping) {
        super(jarMapping);
    }

    public String mapSignature(String signature, boolean typeSignature) {
        try {
            return super.mapSignature(signature, typeSignature);
        }
        catch (Exception e2) {
            return signature;
        }
    }

    public String mapFieldName(String owner, String name, String desc, int access) {
        return super.mapFieldName(owner, name, desc, -1);
    }
}

