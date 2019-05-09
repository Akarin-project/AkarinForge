/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class HashTreeSet<V>
implements Set<V> {
    private HashSet<V> hash = new HashSet();
    private TreeSet<V> tree = new TreeSet();

    @Override
    public int size() {
        return this.hash.size();
    }

    @Override
    public boolean isEmpty() {
        return this.hash.isEmpty();
    }

    @Override
    public boolean contains(Object o2) {
        return this.hash.contains(o2);
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>(){
            private Iterator<V> it;
            private V last;

            @Override
            public boolean hasNext() {
                return this.it.hasNext();
            }

            @Override
            public V next() {
                this.last = this.it.next();
                return this.last;
            }

            @Override
            public void remove() {
                if (this.last == null) {
                    throw new IllegalStateException();
                }
                this.it.remove();
                HashTreeSet.this.hash.remove(this.last);
                this.last = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return this.hash.toArray();
    }

    @Override
    public Object[] toArray(Object[] a2) {
        return this.hash.toArray(a2);
    }

    @Override
    public boolean add(V e2) {
        this.hash.add(e2);
        return this.tree.add(e2);
    }

    @Override
    public boolean remove(Object o2) {
        this.hash.remove(o2);
        return this.tree.remove(o2);
    }

    @Override
    public boolean containsAll(Collection c2) {
        return this.hash.containsAll(c2);
    }

    @Override
    public boolean addAll(Collection c2) {
        this.tree.addAll(c2);
        return this.hash.addAll(c2);
    }

    @Override
    public boolean retainAll(Collection c2) {
        this.tree.retainAll(c2);
        return this.hash.retainAll(c2);
    }

    @Override
    public boolean removeAll(Collection c2) {
        this.tree.removeAll(c2);
        return this.hash.removeAll(c2);
    }

    @Override
    public void clear() {
        this.hash.clear();
        this.tree.clear();
    }

    public V first() {
        return this.tree.first();
    }

}

