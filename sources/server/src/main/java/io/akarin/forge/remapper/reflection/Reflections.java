package io.akarin.forge.remapper.reflection;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class Reflections {
    private static SecurityManager sm = new SecurityManager();

    public static Class<?> getCallerClass(int skip) {
        return sm.getCallerClass(skip);
    }

    public static ClassLoader getCallerClassloader() {
        return Reflections.getCallerClass(3).getClassLoader();
    }

    public static Class<?>[] getStackClass() {
        return sm.getStackClass();
    }

    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe)field.get(null);
        }
        catch (ReflectiveOperationException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    static class SecurityManager
    extends java.lang.SecurityManager {
        SecurityManager() {
        }

        public Class<?> getCallerClass(int skip) {
            return this.getClassContext()[skip + 1];
        }

        public Class<?>[] getStackClass() {
            return this.getClassContext();
        }
    }

}

