package org.bukkit.util.io;

import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

class Wrapper<T extends Map<String, ?>>
implements Serializable {
    private static final long serialVersionUID = -986209235411767547L;
    final T map;

    static Wrapper<ImmutableMap<String, ?>> newWrapper(ConfigurationSerializable obj) {
        return new Wrapper(ImmutableMap.builder().put((Object)"==", (Object)ConfigurationSerialization.getAlias(obj.getClass())).putAll(obj.serialize()).build());
    }

    private Wrapper(T map) {
        this.map = map;
    }
}

