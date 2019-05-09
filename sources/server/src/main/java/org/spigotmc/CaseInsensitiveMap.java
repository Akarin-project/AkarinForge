/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  gnu.trove.map.hash.TCustomHashMap
 *  gnu.trove.strategy.HashingStrategy
 */
package org.spigotmc;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;
import java.util.Map;
import org.spigotmc.CaseInsensitiveHashingStrategy;

public class CaseInsensitiveMap<V>
extends TCustomHashMap<String, V> {
    public CaseInsensitiveMap() {
        super((HashingStrategy)CaseInsensitiveHashingStrategy.INSTANCE);
    }

    public CaseInsensitiveMap(Map<? extends String, ? extends V> map) {
        super((HashingStrategy)CaseInsensitiveHashingStrategy.INSTANCE, map);
    }
}

