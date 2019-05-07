/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  net.md_5.specialsource.JarMapping
 */
package io.akarin.forge.remapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.akarin.forge.AkarinForge;
import io.akarin.forge.remapper.AkarinServerRemapper;
import io.akarin.forge.remapper.ReflectionTransformer;
import io.akarin.forge.remapper.ReflectionUtils;
import io.akarin.forge.remapper.RemapUtils;
import net.md_5.specialsource.JarMapping;

public class ReflectionMethods {
    private static final ConcurrentHashMap<String, String> fieldGetNameCache = new ConcurrentHashMap();
    private static final ConcurrentHashMap<String, String> methodGetNameCache = new ConcurrentHashMap();
    private static final ConcurrentHashMap<String, String> simpleNameGetNameCache = new ConcurrentHashMap();

    public static Class<?> forName(String className) throws ClassNotFoundException {
        return ReflectionMethods.forName(className, true, ReflectionUtils.getCallerClassloader());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader classLoader) throws ClassNotFoundException {
        if (className.startsWith("net.minecraft.server." + AkarinForge.getNativeVersion())) {
            className = ReflectionTransformer.jarMapping.classes.getOrDefault(className.replace('.', '/'), className).replace('/', '.');
        }
        return Class.forName(className, initialize, classLoader);
    }

    public static Field getField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, true)) {
            name = RemapUtils.mapFieldName(inst, name);
        }
        return inst.getField(name);
    }

    public static Field getDeclaredField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, false)) {
            name = ReflectionTransformer.remapper.mapFieldName(RemapUtils.reverseMap(inst), name, null);
        }
        return inst.getDeclaredField(name);
    }

    public static /* varargs */ Method getMethod(Class<?> inst, String name, Class<?> ... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, true)) {
            name = RemapUtils.mapMethod(inst, name, parameterTypes);
        }
        try {
            return inst.getMethod(name, parameterTypes);
        }
        catch (NoClassDefFoundError e2) {
            throw new NoSuchMethodException(e2.toString());
        }
    }

    public static /* varargs */ Method getDeclaredMethod(Class<?> inst, String name, Class<?> ... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (RemapUtils.isClassNeedRemap(inst, true)) {
            name = RemapUtils.mapMethod(inst, name, parameterTypes);
        }
        try {
            return inst.getDeclaredMethod(name, parameterTypes);
        }
        catch (NoClassDefFoundError e2) {
            throw new NoSuchMethodException(e2.toString());
        }
    }

    public static String getName(Field field) {
        if (!RemapUtils.isClassNeedRemap(field.getDeclaringClass(), false)) {
            return field.getName();
        }
        String hash = String.valueOf(field.hashCode());
        String cache = fieldGetNameCache.get(hash);
        if (cache != null) {
            return cache;
        }
        String retn = RemapUtils.demapFieldName(field);
        fieldGetNameCache.put(hash, retn);
        return retn;
    }

    public static String getName(Method method) {
        if (!RemapUtils.isClassNeedRemap(method.getDeclaringClass(), true)) {
            return method.getName();
        }
        String hash = String.valueOf(method.hashCode());
        String cache = methodGetNameCache.get(hash);
        if (cache != null) {
            return cache;
        }
        String retn = RemapUtils.demapMethodName(method);
        methodGetNameCache.put(hash, retn);
        return retn;
    }

    public static String getSimpleName(Class<?> inst) {
        if (!RemapUtils.isClassNeedRemap(inst, false)) {
            return inst.getSimpleName();
        }
        String hash = String.valueOf(inst.hashCode());
        String cache = simpleNameGetNameCache.get(hash);
        if (cache != null) {
            return cache;
        }
        String[] name = RemapUtils.reverseMapExternal(inst).split("\\.");
        String retn = name[name.length - 1];
        simpleNameGetNameCache.put(hash, retn);
        return retn;
    }

    public static Class<?> loadClass(ClassLoader inst, String className) throws ClassNotFoundException {
        if (className.startsWith("net.minecraft.")) {
            className = RemapUtils.mapClass(className.replace('.', '/')).replace('/', '.');
        }
        return inst.loadClass(className);
    }
}

