/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.ListenableFuture
 *  org.apache.logging.log4j.Logger
 */
package io.akarin.forge.utils;

import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.function.Predicate;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.spigotmc.AsyncCatcher;

public class ThreadSafeList<E>
extends Vector<E> {
    private static final String message = "\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!";
    private final boolean print;

    public ThreadSafeList(boolean print) {
        this.print = print;
    }

    @Override
    public boolean add(E e2) {
        if (this.checkThread()) {
            this.switchPrimaryThread(() -> {
                super.add(e2);
            }
            );
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return true;
        }
        return super.add(e2);
    }

    @Override
    public void add(int index, E element) {
        if (this.checkThread()) {
            this.switchPrimaryThread(() -> {
                super.add(index, element);
            }
            );
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return;
        }
        super.add(index, element);
    }

    @Override
    public boolean remove(Object o2) {
        if (this.checkThread()) {
            this.switchPrimaryThread(() -> {
                super.remove(o2);
            }
            );
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return super.contains(o2);
        }
        return super.remove(o2);
    }

    @Override
    public synchronized E remove(int index) {
        if (this.checkThread()) {
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return null;
        }
        return super.remove(index);
    }

    @Override
    public void clear() {
        if (this.checkThread()) {
            this.switchPrimaryThread(() -> {
                super.clear();
            }
            );
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return;
        }
        super.clear();
    }

    @Override
    public synchronized boolean addAll(Collection<? extends E> c2) {
        if (this.checkThread()) {
            this.switchPrimaryThread(() -> {
                super.addAll(c2);
            }
            );
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return true;
        }
        return super.addAll(c2);
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends E> c2) {
        if (this.checkThread()) {
            this.switchPrimaryThread(() -> {
                super.addAll(index, c2);
            }
            );
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return true;
        }
        return super.addAll(index, c2);
    }

    @Override
    public synchronized void addElement(E obj) {
        if (this.checkThread()) {
            this.switchPrimaryThread(() -> {
                super.addElement(obj);
            }
            );
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return;
        }
        super.addElement(obj);
    }

    @Override
    public synchronized void removeElementAt(int index) {
        if (this.checkThread()) {
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return;
        }
        super.removeElementAt(index);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c2) {
        if (this.checkThread()) {
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return false;
        }
        return super.removeAll(c2);
    }

    @Override
    public synchronized void removeAllElements() {
        if (this.checkThread()) {
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return;
        }
        super.removeAllElements();
    }

    @Override
    public synchronized boolean removeElement(Object obj) {
        if (this.checkThread()) {
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return false;
        }
        return super.removeElement(obj);
    }

    @Override
    public synchronized boolean removeIf(Predicate<? super E> filter) {
        if (this.checkThread()) {
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return false;
        }
        return super.removeIf(filter);
    }

    @Override
    public synchronized Iterator<E> iterator() {
        if (this.checkThread()) {
            if (this.print) {
                FMLLog.log.debug((Object)new UnsupportedOperationException("\u63d2\u4ef6/MOD\u5c1d\u8bd5\u5f02\u6b65\u64cd\u4f5cList\u5df2\u62e6\u622a,\u8bf7\u4e0e\u63d2\u4ef6/MOD\u4f5c\u8005\u53cd\u9988!"));
            }
            return new ArrayList(this).iterator();
        }
        return super.iterator();
    }

    private boolean checkThread() {
        return AsyncCatcher.enabled && !Bukkit.isPrimaryThread();
    }

    private void switchPrimaryThread(Runnable runnable) {
        MinecraftServer.getServerInst().a(runnable);
    }
}

