/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1.scoreboard;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Set;

final class CraftCriteria {
    static final Map<String, CraftCriteria> DEFAULTS;
    static final CraftCriteria DUMMY;
    final bhq criteria;
    final String bukkitName;

    private CraftCriteria(String bukkitName) {
        this.bukkitName = bukkitName;
        this.criteria = CraftCriteria.DUMMY.criteria;
    }

    private CraftCriteria(bhq criteria) {
        this.criteria = criteria;
        this.bukkitName = criteria.a();
    }

    static CraftCriteria getFromNMS(bhg objective) {
        return DEFAULTS.get(objective.c().a());
    }

    static CraftCriteria getFromBukkit(String name) {
        CraftCriteria criteria = DEFAULTS.get(name);
        if (criteria != null) {
            return criteria;
        }
        return new CraftCriteria(name);
    }

    public boolean equals(Object that) {
        if (!(that instanceof CraftCriteria)) {
            return false;
        }
        return ((CraftCriteria)that).bukkitName.equals(this.bukkitName);
    }

    public int hashCode() {
        return this.bukkitName.hashCode() ^ CraftCriteria.class.hashCode();
    }

    static {
        ImmutableMap.Builder defaults = ImmutableMap.builder();
        for (Map.Entry<String, bhq> entry : bhq.a.entrySet()) {
            String name = entry.getKey().toString();
            bhq criteria = entry.getValue();
            defaults.put((Object)name, (Object)new CraftCriteria(criteria));
        }
        DEFAULTS = defaults.build();
        DUMMY = DEFAULTS.get("dummy");
    }
}

