/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Multimap
 *  net.md_5.specialsource.JarMapping
 *  net.md_5.specialsource.JarRemapper
 *  org.objectweb.asm.Type
 */
package io.akarin.forge.remapper;

import com.google.common.collect.Multimap;

import io.akarin.forge.AkarinForge;
import io.akarin.forge.remapper.ReflectionTransformer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import org.objectweb.asm.Type;

public class RemapUtils {
    private static Map<String, Boolean> classNeedRemap = new ConcurrentHashMap<String, Boolean>();
    public static final String NMS_PREFIX = "net/minecraft/server/";
    public static final String NMS_VERSION = AkarinForge.getNativeVersion();

    public static String reverseMapExternal(Class<?> name) {
        return RemapUtils.reverseMap(name).replace('$', '.').replace('/', '.');
    }

    public static String reverseMap(Class<?> name) {
        return RemapUtils.reverseMap(Type.getInternalName(name));
    }

    public static String reverseMap(String check) {
        return ReflectionTransformer.classDeMapping.getOrDefault(check, check);
    }

    public static /* varargs */ String mapMethod(Class<?> inst, String name, Class<?> ... parameterTypes) {
        String result = RemapUtils.mapMethodInternal(inst, name, parameterTypes);
        if (result != null) {
            return result;
        }
        return name;
    }

    public static /* varargs */ String mapMethodInternal(Class<?> inst, String name, Class<?> ... parameterTypes) {
        Object superMethodName;
        String match = RemapUtils.reverseMap(inst) + "/" + name;
        Collection colls = ReflectionTransformer.methodFastMapping.get((Object)match);
        for (String value : colls) {
            String[] str = value.split("\\s+");
            int i2 = 0;
            for (Type type : Type.getArgumentTypes((String)str[1])) {
                String typename;
                String string = typename = type.getSort() == 9 ? type.getInternalName() : type.getClassName();
                if (i2 >= parameterTypes.length || !typename.equals(RemapUtils.reverseMapExternal(parameterTypes[i2]))) {
                    i2 = -1;
                    break;
                }
                ++i2;
            }
            if (i2 < parameterTypes.length) continue;
            return (String)ReflectionTransformer.jarMapping.methods.get(value);
        }
        Class superClass = inst.getSuperclass();
        if (superClass != null && (superMethodName = RemapUtils.mapMethodInternal(superClass, name, parameterTypes)) != null) {
            return superMethodName;
        }
        for (Class interfaceClass : inst.getInterfaces()) {
            String superMethodName2 = RemapUtils.mapMethodInternal(interfaceClass, name, parameterTypes);
            if (superMethodName2 == null) continue;
            return superMethodName2;
        }
        return null;
    }

    public static String mapFieldName(Class<?> inst, String name) {
        Class superClass;
        String key = RemapUtils.reverseMap(inst) + "/" + name;
        String mapped = (String)ReflectionTransformer.jarMapping.fields.get(key);
        if (mapped == null && (superClass = inst.getSuperclass()) != null) {
            mapped = RemapUtils.mapFieldName(superClass, name);
        }
        return mapped != null ? mapped : name;
    }

    public static String mapClass(String className) {
        String tRemapped = JarRemapper.mapTypeName((String)className, (Map)ReflectionTransformer.jarMapping.packages, (Map)ReflectionTransformer.jarMapping.classes, (String)className);
        if (tRemapped.equals(className) && className.startsWith("net/minecraft/server/") && !className.contains(NMS_VERSION)) {
            String tNewClassStr = "net/minecraft/server/" + NMS_VERSION + "/" + className.substring("net/minecraft/server/".length());
            return JarRemapper.mapTypeName((String)tNewClassStr, (Map)ReflectionTransformer.jarMapping.packages, (Map)ReflectionTransformer.jarMapping.classes, (String)className);
        }
        return tRemapped;
    }

    public static String demapFieldName(Field field) {
        String name = field.getName();
        String match = RemapUtils.reverseMap(field.getDeclaringClass()) + "/";
        Collection colls = ReflectionTransformer.fieldDeMapping.get((Object)name);
        for (String value : colls) {
            if (!value.startsWith(match)) continue;
            String[] matched = value.split("\\/");
            String rtr = matched[matched.length - 1];
            return rtr;
        }
        return name;
    }

    public static String demapMethodName(Method method) {
        String name = method.getName();
        String match = RemapUtils.reverseMap(method.getDeclaringClass()) + "/";
        Collection colls = ReflectionTransformer.methodDeMapping.get((Object)name);
        for (String value : colls) {
            if (!value.startsWith(match)) continue;
            String[] matched = value.split("\\s+")[0].split("\\/");
            String rtr = matched[matched.length - 1];
            return rtr;
        }
        return name;
    }

    public static boolean isClassNeedRemap(Class<?> clazz, boolean checkSuperClass) {
        String className = clazz.getName();
        Boolean cache = classNeedRemap.get(className);
        if (cache != null) {
            return cache;
        }
        while (clazz != null && clazz.getClassLoader() != null) {
            if (clazz.getName().startsWith("net.minecraft.")) {
                classNeedRemap.put(className, true);
                return true;
            }
            if (checkSuperClass) {
                for (Class interfaceClass : clazz.getInterfaces()) {
                    if (!RemapUtils.isClassNeedRemap(interfaceClass, true)) continue;
                    return true;
                }
                clazz = clazz.getSuperclass();
                continue;
            }
            return false;
        }
        classNeedRemap.put(className, false);
        return false;
    }
}

