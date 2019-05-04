/*
 * Decompiled with CFR 0_119.
 */
package catserver.server.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class CatTreeSet<E>
extends TreeSet<E> {
    private ConcurrentSkipListSet<E> m;
    private static final Object PRESENT = new Object();
    private static final long serialVersionUID = -2479143000061671589L;

    CatTreeSet(ConcurrentSkipListSet<E> m2) {
        this.m = m2;
    }

    public CatTreeSet() {
        this(new ConcurrentSkipListSet());
    }

    public CatTreeSet(Comparator<? super E> comparator) {
        this(new ConcurrentSkipListSet<E>(comparator));
    }

    public CatTreeSet(Collection<? extends E> c2) {
        this();
        this.addAll(c2);
    }

    public CatTreeSet(SortedSet<E> s2) {
        this(s2.comparator());
        this.addAll(s2);
    }

    @Override
    public Iterator<E> iterator() {
        return this.m.iterator();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return this.m.descendingIterator();
    }

    @Override
    public NavigableSet<E> descendingSet() {
        return this.m.descendingSet();
    }

    @Override
    public int size() {
        return this.m.size();
    }

    @Override
    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    @Override
    public boolean contains(Object o2) {
        return this.m.contains(o2);
    }

    @Override
    public boolean add(E e2) {
        return this.m.add(e2);
    }

    @Override
    public boolean remove(Object o2) {
        return this.m.remove(o2);
    }

    @Override
    public void clear() {
        this.m.clear();
    }

    @Override
    public boolean addAll(Collection<? extends E> c2) {
        return this.m.addAll(c2);
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return this.m.subSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return this.m.headSet(toElement, inclusive);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return this.m.tailSet(fromElement, inclusive);
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return this.subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return this.headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return this.tailSet(fromElement, true);
    }

    @Override
    public Comparator<? super E> comparator() {
        return this.m.comparator();
    }

    @Override
    public E first() {
        return this.m.first();
    }

    @Override
    public E last() {
        return this.m.last();
    }

    @Override
    public E lower(E e2) {
        return this.m.lower(e2);
    }

    @Override
    public E floor(E e2) {
        return this.m.floor(e2);
    }

    @Override
    public E ceiling(E e2) {
        return this.m.ceiling(e2);
    }

    @Override
    public E higher(E e2) {
        return this.m.higher(e2);
    }

    @Override
    public E pollFirst() {
        return this.m.pollFirst();
    }

    @Override
    public E pollLast() {
        return this.m.pollLast();
    }

    @Override
    public Object clone() {
        return this.m.clone();
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        s2.defaultWriteObject();
        s2.writeObject(this.m.comparator());
        s2.writeInt(this.m.size());
        for (E e2 : this.m) {
            s2.writeObject(e2);
        }
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("PendingTreeSet");
    }

    @Override
    public Spliterator<E> spliterator() {
        return this.m.spliterator();
    }
}

