package io.akarin.forge.server.layers;

import io.akarin.forge.server.layers.misc.Constants;
import io.akarin.forge.server.layers.reflection.ReflectionTransformer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.md_5.specialsource.JarRemapper;
import org.objectweb.asm.Type;

public class Remaps {
    private static final Map<String, Boolean> REMAP_REQUIREMENT_CACHE = new ConcurrentHashMap<String, Boolean>();
    
    public static String reverseMapClassNameExternally(Class<?> name) {
        return Remaps.reverseMapClassName(name).replace('$', '.').replace('/', '.');
    }

    public static String reverseMapClassName(Class<?> name) {
        return Remaps.reverseMapClassName(Type.getInternalName(name));
    }

    public static String reverseMapClassName(String check) {
        return ReflectionTransformer.REVERSE_CLASS_MAPPING.getOrDefault(check, check);
    }

    public static String remapMethod(Class<?> inst, String name, Class<?> ... parameterTypes) {
        String result = Remaps.remapMethodInternally(inst, name, parameterTypes);
        
        if (result != null)
            return result;
        
        return name;
    }

    public static String remapMethodInternally(Class<?> inst, String name, Class<?> ... parameterTypes) {
        String superMethodName;
        String match = Remaps.reverseMapClassName(inst) + "/" + name;
        Collection<String> colls = ReflectionTransformer.SIMPLE_CLASS_NAME_MAPPING.get(match);
        for (String value : colls) {
            String[] str = value.split("\\s+");
            int i2 = 0;
            for (Type type : Type.getArgumentTypes((String)str[1])) {
                String typename = type.getSort() == 9 ? type.getInternalName() : type.getClassName();
                if (i2 >= parameterTypes.length || !typename.equals(Remaps.reverseMapClassNameExternally(parameterTypes[i2]))) {
                    i2 = -1;
                    break;
                }
                ++i2;
            }
            
            if (i2 < parameterTypes.length)
            	continue;
            
            return ReflectionTransformer.jarMapping.methods.get(value);
        }
        
        Class<?> superClass = inst.getSuperclass();
        if (superClass != null && (superMethodName = Remaps.remapMethodInternally(superClass, name, parameterTypes)) != null) {
            return superMethodName;
        }
        
        for (Class<?> interfaceClass : inst.getInterfaces()) {
            String superMethodName2 = Remaps.remapMethodInternally(interfaceClass, name, parameterTypes);
            if (superMethodName2 == null) continue;
            return superMethodName2;
        }
        return null;
    }

    public static String mapFieldName(Class<?> inst, String name) {
        Class<?> superClass;
        String key = Remaps.reverseMapClassName(inst) + "/" + name;
        String mapped = (String)ReflectionTransformer.jarMapping.fields.get(key);
        if (mapped == null && (superClass = inst.getSuperclass()) != null) {
            mapped = Remaps.mapFieldName(superClass, name);
        }
        return mapped != null ? mapped : name;
    }

    public static String mapClass(String className) {
        String mappedName = JarRemapper.mapTypeName(className, ReflectionTransformer.jarMapping.packages, ReflectionTransformer.jarMapping.classes, className);
        
        if (mappedName.equals(className) && className.startsWith(Constants.NMS_PREFIX_PATH) && !className.contains(Constants.NMS_VERSION)) {
        	// A incorrect NMS version or wrong mappings file can cause this, so fix version for it
            String tNewClassStr = Constants.NMS_PATH.concat("/").concat(className.substring(Constants.NMS_PREFIX_PATH.length()));
            // Remap again in correct version
            return JarRemapper.mapTypeName(tNewClassStr, ReflectionTransformer.jarMapping.packages, ReflectionTransformer.jarMapping.classes, className);
        }
        
        return mappedName;
    }

    public static String demapFieldName(Field field) {
        String name = field.getName();
        String match = Remaps.reverseMapClassName(field.getDeclaringClass()) + "/";
        Collection<String> colls = ReflectionTransformer.REVERSE_FIELD_MAPPING.get(name);
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
        String match = Remaps.reverseMapClassName(method.getDeclaringClass()) + "/";
        Collection<String> colls = ReflectionTransformer.REVERSE_METHOD_MAPPING.get(name);
        for (String value : colls) {
            if (!value.startsWith(match)) continue;
            String[] matched = value.split("\\s+")[0].split("\\/");
            String rtr = matched[matched.length - 1];
            return rtr;
        }
        return name;
    }

    public static boolean shouldRemapClass(Class<?> clazz, boolean checkSuperClass) {
        String className = clazz.getName();
        Boolean cache = REMAP_REQUIREMENT_CACHE.get(className);
        
        if (cache != null)
            return cache;
        
        while (clazz != null && clazz.getClassLoader() != null) {
            if (clazz.getName().startsWith(Constants.MINECRAFT_DOMAIN)) {
                REMAP_REQUIREMENT_CACHE.put(className, Boolean.TRUE);
                return true;
            }
            
            if (checkSuperClass) {
                for (Class<?> interfaceClass : clazz.getInterfaces()) {
                    if (!Remaps.shouldRemapClass(interfaceClass, true))
                    	continue;
                    return true;
                }
                clazz = clazz.getSuperclass();
                continue;
            }
            
            return false;
        }
        
        REMAP_REQUIREMENT_CACHE.put(className, false);
        return false;
    }
}

