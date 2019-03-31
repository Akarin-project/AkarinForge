package net.minecraft.util.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.IObjectIntIterable;
import net.minecraft.util.IntIdentityHashBiMap;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V>
{
    protected final IntIdentityHashBiMap<V> underlyingIntegerMap = new IntIdentityHashBiMap<V>(256);
    protected final Map<V, K> inverseObjectRegistry;

    public RegistryNamespaced()
    {
        this.inverseObjectRegistry = ((BiMap)this.registryObjects).inverse();
    }

    public void register(int id, K key, V value)
    {
        this.underlyingIntegerMap.put(value, id);
        this.putObject(key, value);
    }

    protected Map<K, V> createUnderlyingMap()
    {
        return HashBiMap.<K, V>create();
    }

    @Nullable
    public V getObject(@Nullable K name)
    {
        return (V)super.getObject(name);
    }

    @Nullable
    public K getNameForObject(V value)
    {
        return this.inverseObjectRegistry.get(value);
    }

    public boolean containsKey(K key)
    {
        return super.containsKey(key);
    }

    public int getIDForObject(@Nullable V value)
    {
        return this.underlyingIntegerMap.getId(value);
    }

    @Nullable
    public V getObjectById(int id)
    {
        return this.underlyingIntegerMap.get(id);
    }

    public Iterator<V> iterator()
    {
        return this.underlyingIntegerMap.iterator();
    }
}