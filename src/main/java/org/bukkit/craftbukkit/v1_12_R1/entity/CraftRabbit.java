/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAnimals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;

public class CraftRabbit
extends CraftAnimals
implements Rabbit {
    public CraftRabbit(CraftServer server, aaf entity) {
        super(server, entity);
    }

    @Override
    public aaf getHandle() {
        return (aaf)this.entity;
    }

    @Override
    public String toString() {
        return "CraftRabbit{RabbitType=" + (Object)((Object)this.getRabbitType()) + "}";
    }

    @Override
    public EntityType getType() {
        return EntityType.RABBIT;
    }

    @Override
    public Rabbit.Type getRabbitType() {
        int type = this.getHandle().dn();
        return CraftMagicMapping.fromMagic(type);
    }

    @Override
    public void setRabbitType(Rabbit.Type type) {
        aaf entity = this.getHandle();
        if (this.getRabbitType() == Rabbit.Type.THE_KILLER_BUNNY) {
            oo world = ((CraftWorld)this.getWorld()).getHandle();
            entity.br = new xf(world != null && world.E != null ? world.E : null);
            entity.bs = new xf(world != null && world.E != null ? world.E : null);
            entity.initializePathFinderGoals();
        }
        entity.g(CraftMagicMapping.toMagic(type));
    }

    private static class CraftMagicMapping {
        private static final int[] types = new int[Rabbit.Type.values().length];
        private static final Rabbit.Type[] reverse = new Rabbit.Type[Rabbit.Type.values().length];

        private CraftMagicMapping() {
        }

        private static void set(Rabbit.Type type, int value) {
            CraftMagicMapping.types[type.ordinal()] = value;
            if (value < reverse.length) {
                CraftMagicMapping.reverse[value] = type;
            }
        }

        public static Rabbit.Type fromMagic(int magic) {
            if (magic >= 0 && magic < reverse.length) {
                return reverse[magic];
            }
            if (magic == 99) {
                return Rabbit.Type.THE_KILLER_BUNNY;
            }
            return null;
        }

        public static int toMagic(Rabbit.Type type) {
            return types[type.ordinal()];
        }

        static {
            CraftMagicMapping.set(Rabbit.Type.BROWN, 0);
            CraftMagicMapping.set(Rabbit.Type.WHITE, 1);
            CraftMagicMapping.set(Rabbit.Type.BLACK, 2);
            CraftMagicMapping.set(Rabbit.Type.BLACK_AND_WHITE, 3);
            CraftMagicMapping.set(Rabbit.Type.GOLD, 4);
            CraftMagicMapping.set(Rabbit.Type.SALT_AND_PEPPER, 5);
            CraftMagicMapping.set(Rabbit.Type.THE_KILLER_BUNNY, 99);
        }
    }

}

