/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package org.spigotmc;

import java.util.function.Supplier;
import javax.annotation.Nullable;

public class SupplierUtils {
    public static <V> Supplier<V> createUnivaluedSupplier(Supplier<V> completion, boolean doLazily) {
        return doLazily ? new LazyHeadSupplier<V>(completion) : new ValueSupplier<V>(completion.get());
    }

    @Nullable
    public static <V> V getIfExists(@Nullable Supplier<V> supplier) {
        return supplier != null ? (V)supplier.get() : null;
    }

    public static class ValueSupplier<V>
    implements Supplier<V> {
        @Nullable
        private final V value;

        public ValueSupplier(@Nullable V value) {
            this.value = value;
        }

        @Nullable
        @Override
        public V get() {
            return this.value;
        }
    }

    public static class LazyHeadSupplier<V>
    implements Supplier<V> {
        @Nullable
        private Supplier<V> completion;
        @Nullable
        private V value;

        public LazyHeadSupplier(Supplier<V> completion) {
            this.completion = completion;
        }

        @Nullable
        @Override
        public synchronized V get() {
            if (this.completion != null) {
                this.value = this.completion.get();
                this.completion = null;
            }
            return this.value;
        }
    }

}

