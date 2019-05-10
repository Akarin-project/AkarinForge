/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.potion;

import org.apache.commons.lang.Validate;
import org.bukkit.potion.PotionType;

public final class PotionData {
    private final PotionType type;
    private final boolean extended;
    private final boolean upgraded;

    public PotionData(PotionType type, boolean extended, boolean upgraded) {
        Validate.notNull((Object)((Object)type), (String)"Potion Type must not be null");
        Validate.isTrue((boolean)(!upgraded || type.isUpgradeable()), (String)"Potion Type is not upgradable");
        Validate.isTrue((boolean)(!extended || type.isExtendable()), (String)"Potion Type is not extendable");
        Validate.isTrue((boolean)(!upgraded || !extended), (String)"Potion cannot be both extended and upgraded");
        this.type = type;
        this.extended = extended;
        this.upgraded = upgraded;
    }

    public PotionData(PotionType type) {
        this(type, false, false);
    }

    public PotionType getType() {
        return this.type;
    }

    public boolean isUpgraded() {
        return this.upgraded;
    }

    public boolean isExtended() {
        return this.extended;
    }

    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 23 * hash + (this.extended ? 1 : 0);
        hash = 23 * hash + (this.upgraded ? 1 : 0);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        PotionData other = (PotionData)obj;
        return this.upgraded == other.upgraded && this.extended == other.extended && this.type == other.type;
    }
}

