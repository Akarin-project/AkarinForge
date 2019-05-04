/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.Validate;

public final class WeakCollection<T>
implements Collection<T> {
    static final Object NO_VALUE = new Object();
    private final Collection<WeakReference<T>> collection = new ArrayList<WeakReference<T>>();

    @Override
    public boolean add(T value) {
        Validate.notNull(value, (String)"Cannot add null value", (Object[])new Object[0]);
        return this.collection.add(new WeakReference<T>(value));
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        Collection<WeakReference<T>> values = this.collection;
        boolean ret = false;
        for (T value : collection) {
            Validate.notNull(value, (String)"Cannot add null value", (Object[])new Object[0]);
            ret |= values.add(new WeakReference<T>(value));
        }
        return ret;
    }

    @Override
    public void clear() {
        this.collection.clear();
    }

    @Override
    public boolean contains(Object object) {
        if (object == null) {
            return false;
        }
        for (T compare : this) {
            if (!object.equals(compare)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return this.toCollection().containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return !this.iterator().hasNext();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(){
            Iterator<WeakReference<T>> it;
            Object value;

            @Override
            public boolean hasNext() {
                Object value = this.value;
                if (value != null && value != WeakCollection.NO_VALUE) {
                    return true;
                }
                Iterator<WeakReference<T>> it2 = this.it;
                value = null;
                while (it2.hasNext()) {
                    WeakReference<T> ref = it2.next();
                    value = ref.get();
                    if (value == null) {
                        it2.remove();
                        continue;
                    }
                    this.value = value;
                    return true;
                }
                return false;
            }

            @Override
            public T next() throws NoSuchElementException {
                if (!this.hasNext()) {
                    throw new NoSuchElementException("No more elements");
                }
                Object value = this.value;
                this.value = WeakCollection.NO_VALUE;
                return (T)value;
            }

            @Override
            public void remove() throws IllegalStateException {
                if (this.value != WeakCollection.NO_VALUE) {
                    throw new IllegalStateException("No last element");
                }
                this.value = null;
                this.it.remove();
            }
        };
    }

    @Override
    public boolean remove(Object object) {
        if (object == null) {
            return false;
        }
        Iterator<T> it2 = this.iterator();
        while (it2.hasNext()) {
            if (!object.equals(it2.next())) continue;
            it2.remove();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        Iterator<T> it2 = this.iterator();
        boolean ret = false;
        while (it2.hasNext()) {
            if (!collection.contains(it2.next())) continue;
            ret = true;
            it2.remove();
        }
        return ret;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        Iterator<T> it2 = this.iterator();
        boolean ret = false;
        while (it2.hasNext()) {
            if (collection.contains(it2.next())) continue;
            ret = true;
            it2.remove();
        }
        return ret;
    }

    @Override
    public int size() {
        int s2 = 0;
        for (T value : this) {
            ++s2;
        }
        return s2;
    }

    @Override
    public Object[] toArray() {
        return this.toArray(new Object[0]);
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return this.toCollection().toArray(array);
    }

    private Collection<T> toCollection() {
        ArrayList<T> collection = new ArrayList<T>();
        for (T value : this) {
            collection.add(value);
        }
        return collection;
    }

}

