package io.akarin.forge.server.layers.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import com.google.common.collect.Maps;

import io.akarin.forge.server.layers.Remaps;
import io.akarin.forge.server.layers.misc.Constants;

public class ReflectionMethodMapper {
    private static final Map<String, String> FIELD_NAME_CACHE  = Maps.newConcurrentMap();
    private static final Map<String, String> METHOD_NAME_CACHE = Maps.newConcurrentMap();
    private static final Map<String, String> SIMPLE_NAME_CACHE = Maps.newConcurrentMap();

    public static Class<?> forName(String className) throws ClassNotFoundException {
        return ReflectionMethodMapper.forName(className, true, Reflections.getCallerClassloader());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader classLoader) throws ClassNotFoundException {
        if (className.startsWith(Constants.NMS_DOMAIN)) {
            className = ReflectionTransformer.jarMapping.classes.getOrDefault(className.replace('.', '/'), className).replace('/', '.');
        }
        return Class.forName(className, initialize, classLoader);
    }

    public static Field getField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        if (Remaps.shouldRemapClass(inst, true)) {
            name = Remaps.mapFieldName(inst, name);
        }
        return inst.getField(name);
    }

    public static Field getDeclaredField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        if (Remaps.shouldRemapClass(inst, false)) {
            name = ReflectionTransformer.remapper.mapFieldName(Remaps.reverseMapClassName(inst), name, null);
        }
        return inst.getDeclaredField(name);
    }

    public static Method getMethod(Class<?> inst, String name, Class<?> ... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (Remaps.shouldRemapClass(inst, true)) {
            name = Remaps.remapMethod(inst, name, parameterTypes);
        }
        try {
            return inst.getMethod(name, parameterTypes);
        }
        catch (NoClassDefFoundError e2) {
            throw new NoSuchMethodException(e2.toString());
        }
    }

    public static Method getDeclaredMethod(Class<?> inst, String name, Class<?> ... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (Remaps.shouldRemapClass(inst, true)) {
            name = Remaps.remapMethod(inst, name, parameterTypes);
        }
        try {
            return inst.getDeclaredMethod(name, parameterTypes);
        }
        catch (NoClassDefFoundError e2) {
            throw new NoSuchMethodException(e2.toString());
        }
    }

    public static String getName(Field field) {
        if (!Remaps.shouldRemapClass(field.getDeclaringClass(), false)) {
            return field.getName();
        }
        String hash = String.valueOf(field.hashCode());
        String cache = FIELD_NAME_CACHE.get(hash);
        if (cache != null) {
            return cache;
        }
        String retn = Remaps.demapFieldName(field);
        FIELD_NAME_CACHE.put(hash, retn);
        return retn;
    }

    public static String getName(Method method) {
        if (!Remaps.shouldRemapClass(method.getDeclaringClass(), true)) {
            return method.getName();
        }
        String hash = String.valueOf(method.hashCode());
        String cache = METHOD_NAME_CACHE.get(hash);
        if (cache != null) {
            return cache;
        }
        String retn = Remaps.demapMethodName(method);
        METHOD_NAME_CACHE.put(hash, retn);
        return retn;
    }

    public static String getSimpleName(Class<?> inst) {
        if (!Remaps.shouldRemapClass(inst, false)) {
            return inst.getSimpleName();
        }
        String hash = String.valueOf(inst.hashCode());
        String cache = SIMPLE_NAME_CACHE.get(hash);
        if (cache != null) {
            return cache;
        }
        String[] name = Remaps.reverseMapClassNameExternally(inst).split("\\.");
        String retn = name[name.length - 1];
        SIMPLE_NAME_CACHE.put(hash, retn);
        return retn;
    }

    public static Class<?> loadClass(ClassLoader inst, String className) throws ClassNotFoundException {
        if (className.startsWith(Constants.MINECRAFT_DOMAIN)) {
            className = Remaps.mapClass(className.replace('.', '/')).replace('/', '.');
        }
        return inst.loadClass(className);
    }
}

