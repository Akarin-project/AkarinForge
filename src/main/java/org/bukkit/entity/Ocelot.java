/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;

public interface Ocelot
extends Animals,
Tameable,
Sittable {
    public Type getCatType();

    public void setCatType(Type var1);

    public static enum Type {
        WILD_OCELOT(0),
        BLACK_CAT(1),
        RED_CAT(2),
        SIAMESE_CAT(3);
        
        private static final Type[] types;
        private final int id;

        private Type(int id2) {
            this.id = id2;
        }

        @Deprecated
        public int getId() {
            return this.id;
        }

        @Deprecated
        public static Type getType(int id2) {
            return id2 >= types.length ? null : types[id2];
        }

        static {
            types = new Type[Type.values().length];
            Type[] arrtype = Type.values();
            int n2 = arrtype.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                Type type = arrtype[i2];
                Type.types[type.getId()] = type;
            }
        }
    }

}

