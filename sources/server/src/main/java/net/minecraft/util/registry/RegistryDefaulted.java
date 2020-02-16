package net.minecraft.util.registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RegistryDefaulted<K, V> extends RegistrySimple<K, V>
{
    private final V defaultObject;

    public RegistryDefaulted(V defaultObjectIn)
    {
        this.defaultObject = defaultObjectIn;
    }

    @Nonnull
    public V getObject(@Nullable K name)
    {
        V v = (V)super.getObject(name);
        return (V)(v == null ? this.defaultObject : v);
    }
}