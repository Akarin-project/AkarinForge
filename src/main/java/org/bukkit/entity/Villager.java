/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.LinkedListMultimap
 *  com.google.common.collect.Multimap
 */
package org.bukkit.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.List;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.NPC;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Merchant;

public interface Villager
extends Ageable,
NPC,
InventoryHolder,
Merchant {
    public Profession getProfession();

    public void setProfession(Profession var1);

    public Career getCareer();

    public void setCareer(Career var1);

    public void setCareer(Career var1, boolean var2);

    @Override
    public Inventory getInventory();

    public int getRiches();

    public void setRiches(int var1);

    public static enum Career {
        FARMER(Profession.FARMER),
        FISHERMAN(Profession.FARMER),
        SHEPHERD(Profession.FARMER),
        FLETCHER(Profession.FARMER),
        LIBRARIAN(Profession.LIBRARIAN),
        CARTOGRAPHER(Profession.LIBRARIAN),
        CLERIC(Profession.PRIEST),
        ARMORER(Profession.BLACKSMITH),
        WEAPON_SMITH(Profession.BLACKSMITH),
        TOOL_SMITH(Profession.BLACKSMITH),
        BUTCHER(Profession.BUTCHER),
        LEATHERWORKER(Profession.BUTCHER),
        NITWIT(Profession.NITWIT);
        
        private static final Multimap<Profession, Career> careerMap;
        private final Profession profession;

        private Career(Profession profession) {
            this.profession = profession;
        }

        public Profession getProfession() {
            return this.profession;
        }

        public static List<Career> getCareers(Profession profession) {
            return careerMap.containsKey((Object)profession) ? ImmutableList.copyOf((Collection)careerMap.get((Object)profession)) : ImmutableList.of();
        }

        static {
            careerMap = LinkedListMultimap.create();
            for (Career career : Career.values()) {
                careerMap.put((Object)career.profession, (Object)career);
            }
        }
    }

    public static enum Profession {
        NORMAL(true),
        FARMER(false),
        LIBRARIAN(false),
        PRIEST(false),
        BLACKSMITH(false),
        BUTCHER(false),
        NITWIT(false),
        HUSK(true);
        
        private final boolean zombie;

        private Profession(boolean zombie) {
            this.zombie = zombie;
        }

        @Deprecated
        public boolean isZombie() {
            return this.zombie;
        }

        public List<Career> getCareers() {
            return Career.getCareers(this);
        }
    }

}

