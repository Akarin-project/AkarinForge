/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.metadata;

import java.lang.ref.SoftReference;
import java.util.concurrent.Callable;
import org.apache.commons.lang.Validate;
import org.bukkit.metadata.MetadataEvaluationException;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.plugin.Plugin;

public class LazyMetadataValue
extends MetadataValueAdapter {
    private Callable<Object> lazyValue;
    private CacheStrategy cacheStrategy;
    private SoftReference<Object> internalValue;
    private static final Object ACTUALLY_NULL = new Object();

    public LazyMetadataValue(Plugin owningPlugin, Callable<Object> lazyValue) {
        this(owningPlugin, CacheStrategy.CACHE_AFTER_FIRST_EVAL, lazyValue);
    }

    public LazyMetadataValue(Plugin owningPlugin, CacheStrategy cacheStrategy, Callable<Object> lazyValue) {
        super(owningPlugin);
        Validate.notNull((Object)((Object)cacheStrategy), (String)"cacheStrategy cannot be null");
        Validate.notNull(lazyValue, (String)"lazyValue cannot be null");
        this.internalValue = new SoftReference<Object>(null);
        this.lazyValue = lazyValue;
        this.cacheStrategy = cacheStrategy;
    }

    protected LazyMetadataValue(Plugin owningPlugin) {
        super(owningPlugin);
    }

    @Override
    public Object value() {
        this.eval();
        Object value = this.internalValue.get();
        if (value == ACTUALLY_NULL) {
            return null;
        }
        return value;
    }

    private synchronized void eval() throws MetadataEvaluationException {
        if (this.cacheStrategy == CacheStrategy.NEVER_CACHE || this.internalValue.get() == null) {
            try {
                Object value = this.lazyValue.call();
                if (value == null) {
                    value = ACTUALLY_NULL;
                }
                this.internalValue = new SoftReference<Object>(value);
            }
            catch (Exception e2) {
                throw new MetadataEvaluationException(e2);
            }
        }
    }

    @Override
    public synchronized void invalidate() {
        if (this.cacheStrategy != CacheStrategy.CACHE_ETERNALLY) {
            this.internalValue.clear();
        }
    }

    public static enum CacheStrategy {
        CACHE_AFTER_FIRST_EVAL,
        NEVER_CACHE,
        CACHE_ETERNALLY;
        

        private CacheStrategy() {
        }
    }

}

