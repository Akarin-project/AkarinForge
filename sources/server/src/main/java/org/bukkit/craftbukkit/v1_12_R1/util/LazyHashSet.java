/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class LazyHashSet<E>
implements Set<E> {
    Set<E> reference = null;

    @Override
    public int size() {
        return this.getReference().size();
    }

    @Override
    public boolean isEmpty() {
        return this.getReference().isEmpty();
    }

    @Override
    public boolean contains(Object o2) {
        return this.getReference().contains(o2);
    }

    @Override
    public Iterator<E> iterator() {
        return this.getReference().iterator();
    }

    @Override
    public Object[] toArray() {
        return this.getReference().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a2) {
        return this.getReference().toArray(a2);
    }

    @Override
    public boolean add(E o2) {
        return this.getReference().add(o2);
    }

    @Override
    public boolean remove(Object o2) {
        return this.getReference().remove(o2);
    }

    @Override
    public boolean containsAll(Collection<?> c2) {
        return this.getReference().containsAll(c2);
    }

    @Override
    public boolean addAll(Collection<? extends E> c2) {
        return this.getReference().addAll(c2);
    }

    @Override
    public boolean retainAll(Collection<?> c2) {
        return this.getReference().retainAll(c2);
    }

    @Override
    public boolean removeAll(Collection<?> c2) {
        return this.getReference().removeAll(c2);
    }

    @Override
    public void clear() {
        this.getReference().clear();
    }

    public Set<E> getReference() {
        Set<E> reference = this.reference;
        if (reference != null) {
            return reference;
        }
        this.reference = this.makeReference();
        return this.reference;
    }

    abstract Set<E> makeReference();

    public boolean isLazy() {
        return this.reference == null;
    }

    @Override
    public int hashCode() {
        return 157 * this.getReference().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        LazyHashSet that = (LazyHashSet)obj;
        return this.isLazy() && that.isLazy() || this.getReference().equals(that.getReference());
    }

    public String toString() {
        return this.getReference().toString();
    }
}

