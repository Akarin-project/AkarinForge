/*
 * Decompiled with CFR 0_119.
 */
package catserver.server.executor.asm;

import catserver.server.executor.asm.SafeClassDefiner;

public interface ClassDefiner {
    default public boolean isBypassAccessChecks() {
        return false;
    }

    public Class<?> defineClass(ClassLoader var1, String var2, byte[] var3);

    default public static ClassDefiner getInstance() {
        return SafeClassDefiner.INSTANCE;
    }
}

