/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.MapMaker
 */
package catserver.server.executor.asm;

import catserver.server.executor.asm.ClassDefiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class SafeClassDefiner
implements ClassDefiner {
    static final SafeClassDefiner INSTANCE = new SafeClassDefiner();
    private final ConcurrentMap<ClassLoader, GeneratedClassLoader> loaders = new MapMaker().weakKeys().makeMap();

    private SafeClassDefiner() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Class<?> defineClass(ClassLoader parentLoader, String name, byte[] data) {
        GeneratedClassLoader loader = this.loaders.computeIfAbsent(parentLoader, GeneratedClassLoader::new);
        Object object = loader.getClassLoadingLock(name);
        synchronized (object) {
            Preconditions.checkState((boolean)(!loader.hasClass(name)), (String)"%s already defined", (Object)name);
            Class c2 = loader.define(name, data);
            assert (c2.getName().equals(name));
            return c2;
        }
    }

    private static class GeneratedClassLoader
    extends ClassLoader {
        protected GeneratedClassLoader(ClassLoader parent) {
            super(parent);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private Class<?> define(String name, byte[] data) {
            Object object = this.getClassLoadingLock(name);
            synchronized (object) {
                assert (!this.hasClass(name));
                Class c2 = this.defineClass(name, data, 0, data.length);
                this.resolveClass(c2);
                return c2;
            }
        }

        @Override
        public Object getClassLoadingLock(String name) {
            return super.getClassLoadingLock(name);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public boolean hasClass(String name) {
            Object object = this.getClassLoadingLock(name);
            synchronized (object) {
                try {
                    Class.forName(name);
                    return true;
                }
                catch (ClassNotFoundException e2) {
                    return false;
                }
            }
        }

        static {
            ClassLoader.registerAsParallelCapable();
        }
    }

}

