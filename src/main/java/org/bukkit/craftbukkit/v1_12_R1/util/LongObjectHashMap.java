/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class LongObjectHashMap<V>
implements Cloneable,
Serializable {
    static final long serialVersionUID = 2841537710170573815L;
    private static final long EMPTY_KEY = Long.MIN_VALUE;
    private static final int BUCKET_SIZE = 4096;
    private transient long[][] keys;
    private transient V[][] values;
    private transient int modCount;
    private transient int size;

    public LongObjectHashMap() {
        this.initialize();
    }

    public LongObjectHashMap(Map<? extends Long, ? extends V> map) {
        this();
        this.putAll(map);
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean containsKey(long key) {
        return this.get(key) != null;
    }

    public boolean containsValue(V value) {
        for (V val : this.values()) {
            if (val != value && !val.equals(value)) continue;
            return true;
        }
        return false;
    }

    public V get(long key) {
        int index = (int)(this.keyIndex(key) & 4095);
        long[] inner = this.keys[index];
        if (inner == null) {
            return null;
        }
        for (int i2 = 0; i2 < inner.length; ++i2) {
            long innerKey = inner[i2];
            if (innerKey == Long.MIN_VALUE) {
                return null;
            }
            if (innerKey != key) continue;
            return this.values[index][i2];
        }
        return null;
    }

    public V put(long key, V value) {
        int index = (int)(this.keyIndex(key) & 4095);
        long[] innerKeys = this.keys[index];
        V[] innerValues = this.values[index];
        ++this.modCount;
        if (innerKeys == null) {
            innerKeys = new long[8];
            this.keys[index] = innerKeys;
            Arrays.fill(innerKeys, Long.MIN_VALUE);
            innerValues = new Object[8];
            this.values[index] = innerValues;
            innerKeys[0] = key;
            innerValues[0] = value;
            ++this.size;
        } else {
            int i2;
            for (i2 = 0; i2 < innerKeys.length; ++i2) {
                if (innerKeys[i2] == Long.MIN_VALUE) {
                    ++this.size;
                    innerKeys[i2] = key;
                    innerValues[i2] = value;
                    return null;
                }
                if (innerKeys[i2] != key) continue;
                V oldValue = innerValues[i2];
                innerKeys[i2] = key;
                innerValues[i2] = value;
                return oldValue;
            }
            innerKeys = Arrays.copyOf(innerKeys, i2 << 1);
            this.keys[index] = innerKeys;
            Arrays.fill(innerKeys, i2, innerKeys.length, Long.MIN_VALUE);
            innerValues = Arrays.copyOf(innerValues, i2 << 1);
            this.values[index] = innerValues;
            innerKeys[i2] = key;
            innerValues[i2] = value;
            ++this.size;
        }
        return null;
    }

    public V remove(long key) {
        int index = (int)(this.keyIndex(key) & 4095);
        long[] inner = this.keys[index];
        if (inner == null) {
            return null;
        }
        for (int i2 = 0; i2 < inner.length && inner[i2] != Long.MIN_VALUE; ++i2) {
            if (inner[i2] != key) continue;
            V value = this.values[index][i2];
            ++i2;
            while (i2 < inner.length && inner[i2] != Long.MIN_VALUE) {
                inner[i2 - 1] = inner[i2];
                this.values[index][i2 - 1] = this.values[index][i2];
                ++i2;
            }
            inner[i2 - 1] = Long.MIN_VALUE;
            this.values[index][i2 - 1] = null;
            --this.size;
            ++this.modCount;
            return value;
        }
        return null;
    }

    public void putAll(Map<? extends Long, ? extends V> map) {
        for (Map.Entry<Long, V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        if (this.size == 0) {
            return;
        }
        ++this.modCount;
        this.size = 0;
        Arrays.fill((Object[])this.keys, null);
        Arrays.fill(this.values, null);
    }

    public Set<Long> keySet() {
        return new KeySet();
    }

    public Collection<V> values() {
        return new ValueCollection();
    }

    @Deprecated
    public Set<Map.Entry<Long, V>> entrySet() {
        HashSet<Map.Entry<Long, V>> set = new HashSet<Map.Entry<Long, V>>();
        Iterator<Long> iterator = this.keySet().iterator();
        while (iterator.hasNext()) {
            long key = iterator.next();
            set.add(new Entry(key, this.get(key)));
        }
        return set;
    }

    public Object clone() throws CloneNotSupportedException {
        LongObjectHashMap clone = (LongObjectHashMap)super.clone();
        clone.clear();
        clone.initialize();
        Iterator<Long> iterator = this.keySet().iterator();
        while (iterator.hasNext()) {
            long key = iterator.next();
            V value = this.get(key);
            clone.put(key, value);
        }
        return clone;
    }

    private void initialize() {
        this.keys = new long[4096][];
        this.values = new Object[4096][];
    }

    private long keyIndex(long key) {
        key ^= key >>> 33;
        key *= -49064778989728563L;
        key ^= key >>> 33;
        key *= -4265267296055464877L;
        key ^= key >>> 33;
        return key;
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
        Iterator<Long> iterator = this.keySet().iterator();
        while (iterator.hasNext()) {
            long key = iterator.next();
            V value = this.get(key);
            outputStream.writeLong(key);
            outputStream.writeObject(value);
        }
        outputStream.writeLong(Long.MIN_VALUE);
        outputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
        inputStream.defaultReadObject();
        this.initialize();
        do {
            long key = inputStream.readLong();
            Object value = inputStream.readObject();
            if (key == Long.MIN_VALUE && value == null) break;
            this.put(key, value);
        } while (true);
    }

    private class Entry
    implements Map.Entry<Long, V> {
        private final Long key;
        private V value;

        Entry(long k2, V v2) {
            this.key = k2;
            this.value = v2;
        }

        @Override
        public Long getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V v2) {
            V old = this.value;
            this.value = v2;
            LongObjectHashMap.this.put(this.key, v2);
            return old;
        }
    }

    private class ValueCollection
    extends AbstractCollection<V> {
        private ValueCollection() {
        }

        @Override
        public void clear() {
            LongObjectHashMap.this.clear();
        }

        @Override
        public int size() {
            return LongObjectHashMap.this.size();
        }

        @Override
        public boolean contains(Object value) {
            return LongObjectHashMap.this.containsValue(value);
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }
    }

    private class KeySet
    extends AbstractSet<Long> {
        private KeySet() {
        }

        @Override
        public void clear() {
            LongObjectHashMap.this.clear();
        }

        @Override
        public int size() {
            return LongObjectHashMap.this.size();
        }

        @Override
        public boolean contains(Object key) {
            return key instanceof Long && LongObjectHashMap.this.containsKey((Long)key);
        }

        @Override
        public boolean remove(Object key) {
            return LongObjectHashMap.this.remove((Long)key) != null;
        }

        @Override
        public Iterator<Long> iterator() {
            return new KeyIterator();
        }
    }

    private class KeyIterator
    implements Iterator<Long> {
        final LongObjectHashMap<V> iterator;

        public KeyIterator() {
            this.iterator = new ValueIterator();
        }

        @Override
        public void remove() {
            this.iterator.remove();
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public Long next() {
            this.iterator.next();
            return this.iterator.prevKey;
        }
    }

    private class ValueIterator
    implements Iterator<V> {
        private int count;
        private int index;
        private int innerIndex;
        private int expectedModCount;
        private long lastReturned;
        long prevKey;
        V prevValue;

        ValueIterator() {
            this.lastReturned = Long.MIN_VALUE;
            this.prevKey = Long.MIN_VALUE;
            this.expectedModCount = LongObjectHashMap.this.modCount;
        }

        @Override
        public boolean hasNext() {
            return this.count < LongObjectHashMap.this.size;
        }

        @Override
        public void remove() {
            if (LongObjectHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (this.lastReturned == Long.MIN_VALUE) {
                throw new IllegalStateException();
            }
            --this.count;
            LongObjectHashMap.this.remove(this.lastReturned);
            this.lastReturned = Long.MIN_VALUE;
            this.expectedModCount = LongObjectHashMap.this.modCount;
        }

        @Override
        public V next() {
            if (LongObjectHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            long[][] keys = LongObjectHashMap.this.keys;
            ++this.count;
            if (this.prevKey != Long.MIN_VALUE) {
                ++this.innerIndex;
            }
            while (this.index < keys.length) {
                if (keys[this.index] != null) {
                    if (this.innerIndex < keys[this.index].length) {
                        long key = keys[this.index][this.innerIndex];
                        Object value = LongObjectHashMap.this.values[this.index][this.innerIndex];
                        if (key != Long.MIN_VALUE) {
                            this.lastReturned = key;
                            this.prevKey = key;
                            this.prevValue = value;
                            return this.prevValue;
                        }
                    }
                    this.innerIndex = 0;
                }
                ++this.index;
            }
            throw new NoSuchElementException();
        }
    }

}

