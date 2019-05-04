/*
 * Akarin Forge
 */
package org.bukkit.advancement;

import java.util.Collection;
import org.bukkit.Keyed;

public interface Advancement
extends Keyed {
    public Collection<String> getCriteria();
}

